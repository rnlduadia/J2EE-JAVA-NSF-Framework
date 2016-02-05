package nsf.application.page.dao;
 
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import nsf.application.page.NPageConstants;
import nsf.core.exception.NException;
import nsf.core.log.NLogUtils;
import nsf.support.collection.NData;
import nsf.support.collection.NDataProtocol;
 
/**
 * <pre>
 *  Oracle DATABASE에서 다량의 자료를 페지로 구성할 경우 최적화된 기능을 제공하는 PageStatement이다.      
 * </pre>
 * @since 2007/1/5
 * @version NSF 1.0
 * @author 김철남 Nova China
 */ 

public class NOraclePageStatement extends NAbstractPageStatement {
	/**
	 * <pre>
	 *   NOraclePageStatement constructor 
	 * </pre>
	 * @param pageData HttpRequest 로 부터 설정된 NData
	 */
	public NOraclePageStatement(NDataProtocol pageData) {
		super(pageData);
	}
 	
	/**
     * NOraclePageStatement constructor
     * @param rowSize
     * @param pageSize
     * @param pageData
	 */
	public NOraclePageStatement(int rowSize, int pageSize, NDataProtocol pageData) {
		super(rowSize, pageSize, pageData);
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
	protected int getLastRow(java.sql.Connection conn, String sql, NData paramData) throws SQLException , NException {

		int retRows = 0;
		StringBuffer neoSql = new StringBuffer();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		neoSql
			.append("select count(*) from (")
			.append( sql )
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

		StringBuffer retSql = new StringBuffer();

		retSql
			.append("select ")
			.append(" * ")
			.append(" from ")
			.append("(select ")
			.append(" inner_temp.* ,  rownum as nsfindex ")
			.append(" from ")
			.append("(")
			.append(rawSql)
			.append(") inner_temp )")
			.append(" where nsfindex between ? and ?");
 
		paramData.setString(NPageConstants.PAGING_ROW_START , targetRow + "");
		paramData.setString(NPageConstants.PAGING_ROW_END , (targetRow + pageSize - 1) + "");
		
		return retSql.toString();
	}

}

