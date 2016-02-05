
package nsf.application.page.dao;
 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;

import nsf.application.page.NPageConstants;
import nsf.core.exception.NException;
import nsf.core.log.NLogUtils;
import nsf.support.collection.NData;
import nsf.support.collection.NDataProtocol;
import nsf.support.collection.NMultiData;
import nsf.support.tools.converter.NResultSetConverter;
 

/**
 * <pre>
 *  MSSQL DATABASE에서 다량의 자료를 페지로 구성할 경우 최적화된 기능을 제공하는 PageStatement이다.      
 * </pre>
 * @since 2007/1/5
 * @version NSF 1.0
 * @author 김철남 Nova China
 */ 

public class NMSSQLPageStatement extends NAbstractPageStatement {
	private String keyField = "" ;	
	private void throwException_If_MSSQL_KEY_FIELD_Is_Not_Set() throws NException {
		
		String keyFieldExpected = pageData.getString(NPageConstants.MSSQL_KEY_FIELD);
		
		if ( keyFieldExpected == null || keyFieldExpected.equals("")) {		   
		  throw new NException(
					"NSF_PAG_007",
					" Error 'MSSQL_KEY_FIELD' Is NOT Set in Parameter Data ( from HTTPServletRequest ).");
		}
		
		this.keyField = keyFieldExpected ;
	}
	
	/**
	 * <pre>
	 *   NMSSQLPageStatement constructor 
	 * </pre>
	 * @param pageData HttpRequest 로 부터 설정된 NData
	 */
	public NMSSQLPageStatement(NDataProtocol pageData) throws NException {
		super(pageData);
		throwException_If_MSSQL_KEY_FIELD_Is_Not_Set();
	}
 	
	/**
     * NMSSQLPageStatement constructor
     * @param rowSize
     * @param pageSize
     * @param pageData
     * @throws NException
	 */
	public NMSSQLPageStatement(int rowSize, int pageSize, NDataProtocol pageData) throws NException  {
		super(rowSize, pageSize, pageData);
		throwException_If_MSSQL_KEY_FIELD_Is_Not_Set();
	}
 	
	/**
	 * <pre>
	 *   쿼리문을 통해 얻어온 결과값들의 개수를 리턴하는 함수이다.
	 * </pre> 
	 * @return int 수행한 쿼리문의 총 Row수
	 * @param conn Connection DB connection
	 * @param sql String SQL 쿼리문
	 * @param NData NData SQL 쿼리문을 구성하기 위한 파라메터	  
	 * @throws SQLException   
	 */
	protected int getLastRow(
		java.sql.Connection conn,
		String sql,
		NData paramData)
		throws SQLException, NException  {

		int retRows = 0;
		StringBuffer neoSql = new StringBuffer();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		neoSql
			.append("select count(*) from (")
			.append(removeOrderByClause(sql))
			.append(") nsfSubQuery ");

		try {
			pstmt = conn.prepareStatement(neoSql.toString());
		    setParamData(pstmt,paramData);
		    
			rs = pstmt.executeQuery();

			if (rs.next()) {			
				retRows = rs.getInt(1);
			}	
		 
		} catch ( SQLException e ) { 
		    NLogUtils.toDefaultLogForm( this.getClass().getName(), "getLastRow()", e.getMessage() );
		    throw e ; 
	    } finally {
			close(rs);
			close(pstmt);
		}

		return retRows;
	}


	/**
	 * <pre>
	 *  다량의 자료를 페지로 구성할 경우 쿼리를 페이징 되도록 수정하기 위한 함수이다.
	 * </pre> 
	 * @param rawSql 수정되기 전의 쿼리문	 
	 * @return 새로 수정된 쿼리문
	 * @throws NException
	 */
	protected String makePageSql(String rawSql, NData paramData, int  targetRow , int pageSize ) {
		
		int totalRecords =  this.pageIndexData.getInt(NPageConstants.ROWS);
		
		targetRow = targetRow - 1 ;
		
		StringBuffer retSql = new StringBuffer();
        rawSql = processOrderByClause( rawSql , totalRecords , this.keyField ) ;
        
		retSql
			.append(" select TOP ").append(pageSize)			
            .append(" * ")
			.append(" from ").append("(").append(rawSql).append(") NSF_TEMP_TABLE_1 ")
            .append(" where ").append(this.keyField).append(" NOT IN ")
            .append(" ( select TOP ").append(targetRow).append("  ").append(this.keyField)
            .append("   from ").append("(").append(rawSql).append(") NSF_TEMP_TABLE_2 ")
            .append(" ) ")
			;
	 
		
		return retSql.toString();
	}

	

	/**
	 * <pre>
	 *  SQL 문에 대해 NData 형의 매핑정보를 이용하여 수행한 후 NMultiData 형태로 
	 *  반환한다. 
	 * </pre>	 
	 */
	protected NMultiData execute(
		Connection conn,
		String sql,
		NData paramData,
		int row,
		int size)
		throws NException, SQLException {

		NMultiData pageResult = new NMultiData("LPAGE_RESULT");
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = getPagedPreparedStatement(conn, sql, paramData, row, size);
			setParamData(pstmt, paramData , paramData.size()+1 ); 
			
			rs = pstmt.executeQuery();
			pageResult = NResultSetConverter.toMultiData(rs);
		} catch (SQLException e) {
			NLogUtils.toDefaultLogForm(
				this.getClass().getName(),
				"execute()",
				e.getMessage());
			throw e;
		} finally {
			close(rs);
			close(pstmt);			
		}

		return pageResult;

	}
	
	private String[] splitOrderByClause(String orgSql) {

		Matcher sqlMatch = NPageConstants.SQL_ORDER_BY_PATTERN.matcher(orgSql);

		int lastOrderByMatchStartIndex = 0;
		while (sqlMatch.find()) {
			lastOrderByMatchStartIndex = sqlMatch.start();
		}

		if (lastOrderByMatchStartIndex == 0)
			return  new String[] { orgSql , ""  }  ;

		int positionOfOrderBy = lastOrderByMatchStartIndex;
		final String candidateForOrderByClause =
			orgSql.substring(positionOfOrderBy);

		char[] candidateCharacters = candidateForOrderByClause.toCharArray();
		final int size = candidateCharacters.length;
		int balance = 0;
		for (int inx = 0; inx < size; inx++) {
			if (candidateCharacters[inx] == ')')
				balance++;
			else if (candidateCharacters[inx] == '(')
				balance--;
		}

		if (balance != 0)
			return  new String[] { orgSql , ""  }  ;
		String withoutOrderBy  = orgSql.substring(0, positionOfOrderBy);
		String OrderByClase  = orgSql.substring( positionOfOrderBy );
		return new String[] { withoutOrderBy , OrderByClase  }  ;
	}
	
	private String removeOrderByClause(String orgSql ) {	
		return splitOrderByClause( orgSql )[0] ;
	}
	
	private String processOrderByClause(String orgSql , int topSize , String keyField ) { 
		String[] sqlPartArray = splitOrderByClause( orgSql );															   
		String orderByAppliedString= " select TOP " + topSize + " *  from ( " +  sqlPartArray[0] + " ) NSF_TEMP_TABLE " ;
		if (  sqlPartArray[1] == null ||  sqlPartArray[1].equals("") ) orderByAppliedString += " ORDER BY "	+ keyField ;
		else orderByAppliedString += sqlPartArray[1] ;
		return orderByAppliedString ;
	}
	
}

