package nsf.application.page.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.Map;

import nsf.application.page.NPageConstants;
import nsf.core.exception.NException;
import nsf.core.log.NLogUtils;
import nsf.support.collection.NData;
import nsf.support.collection.NDataProtocol;
import nsf.support.collection.NMultiData;
import nsf.support.tools.converter.NResultSetConverter;

/**
 * <pre>
 * 페지에서 사용하는  NPageStatement 의 최상위 Abstract Class.
 * NPageStatement Class는 DAO 의 ,method 내에서 주로 사용되며 , 단순히 execute 하는것 
 * 이외에 Statement 에 대한 추가적인 조작이 가능하도록 인터페이스를 지원한다.     
 * </pre>
 * 
 * @since 2007/1/5
 * @version NSF 1.0
 * @author 김철남 Nova China
 */

public abstract class NAbstractPageStatement {

    protected int rowSize;
    protected int pageSize;
    
	protected NData pageData = null;

	protected NData pageIndexData = null;

	protected boolean bindEnabled = false;
	

	/**
	 * <pre>
	 * NAbstractPageStatement constructor 
	 * </pre>
	 * 
	 * @param pageData
	 *            HttpRequest 로 부터 설정된 NData
	 */
	public NAbstractPageStatement(NDataProtocol pageData) {
		this(10, 10, pageData);
	}

	/**
     * NAbstractPageStatement constructor 
     * @param rowSize
     * @param pageSize
     * @param pageData
	 */
    public NAbstractPageStatement(int rowSize, int pageSize, NDataProtocol pageData) {
		this.rowSize = rowSize;
        this.pageSize = pageSize;
        
		if (pageData instanceof NData) {
			((NData) pageData).setBoolean(NPageConstants.REQUEST_DATA_SINGLE_MODE, true);
			this.pageData = (NData) pageData;
		} else {
			this.pageData = NSqlManager.convertNMultiDataToNData((NMultiData) pageData);
		}

	}

	/**
	 * <pre>
	 * SQL 문에 Mapping 될 정보를 NData 로 넘겨주면 , SQL 을 보고 
	 * 자동으로 NData 에서 SQL 에 사용될 값을 추출해 낸후 NData 로 반환한다.  
	 * </pre>
	 */
	private NData getParameterNData(NData paramData, String sql) throws NException {
		NSqlManager mySqlManager = new NSqlManager(sql);
		return mySqlManager.getParameters(paramData);
	}

	private int getInt(String intExpectedStr, int defaultVal) throws NException {

		int intVal = defaultVal;
		try {
			if (intExpectedStr == null || intExpectedStr.equals(""))
				return defaultVal;
			intVal = Integer.parseInt(intExpectedStr);

		} catch (NumberFormatException nfe) {
			throw new NException("NSF_PAG_004", " Error Type-Converting Config Key[" + intExpectedStr + "]", nfe);
		}
		return intVal;

	}

	/**
	 * <pre>
	 *                                                           
	 *  paging index를 계산하여 성원 pageIndexData 에 저장한다. 
	 * </pre>
	 */
	private void processPageIndex(Connection con, String sql, NData paramData) throws SQLException, NException {

		String strTargetRow = this.pageData.getString(NPageConstants.TARGET_ROW);

		int numberOfRowsOfPage = 0;
		String NUMBER_OF_ROWS_OF_PAGE_STR = this.pageData.getString(NPageConstants.NUMBER_OF_ROWS_OF_PAGE);
		if (NUMBER_OF_ROWS_OF_PAGE_STR == null || NUMBER_OF_ROWS_OF_PAGE_STR.equals("")) {
			numberOfRowsOfPage = this.rowSize;
		} else {
			numberOfRowsOfPage = this.pageData.getInt(NPageConstants.NUMBER_OF_ROWS_OF_PAGE);
		}

		int numberOfPagesOfIndex = 0;
		
        String NUMBER_OF_PAGES_OF_INDEX_STR = this.pageData.getString(NPageConstants.NUMBER_OF_PAGES_OF_INDEX);
		if (NUMBER_OF_PAGES_OF_INDEX_STR == null || NUMBER_OF_PAGES_OF_INDEX_STR.equals("")) {
			numberOfPagesOfIndex = this.pageSize;
		} else {
			numberOfPagesOfIndex = this.pageData.getInt(NPageConstants.NUMBER_OF_PAGES_OF_INDEX);
		}

		int targetRow = getInt(strTargetRow, 1);
		int rows = getLastRow(con, sql, paramData);

		if (numberOfRowsOfPage < 0) {
			setNumOfRowsOfPage(rows); // 0 보다 적을 경우 전체 Select
			numberOfRowsOfPage = rows;
		}

		if (rows == 0) {
			targetRow = 1;
		} else if (targetRow > rows) {
			int targetRowPage = (rows / numberOfRowsOfPage);
			if (rows % numberOfRowsOfPage == 0)
				targetRowPage--;
			targetRow = targetRowPage * numberOfRowsOfPage + 1;
		}

		this.pageIndexData = new NData("PAGE_INDEX");
		this.pageIndexData.setInt(NPageConstants.TARGET_ROW, targetRow);
		this.pageIndexData.setInt(NPageConstants.ROWS, rows);
		this.pageIndexData.setInt(NPageConstants.NUMBER_OF_ROWS_OF_PAGE, numberOfRowsOfPage);
		this.pageIndexData.setInt(NPageConstants.NUMBER_OF_PAGES_OF_INDEX, numberOfPagesOfIndex);

	}

	/**
	 * <pre>
	 *                                                           
	 * NSqlManager 로 부터 Order By 가 치환된 형태의 SQL 을 얻는다.  
	 * </pre>
	 */
	private String getPageSQL(String orgSQL) throws SQLException, NException {
		String strNsfOrderBy = this.pageData.getString(NPageConstants.NSF_ORDER_BY);
		NSqlManager mySqlManager = new NSqlManager(orgSQL);
		return mySqlManager.replaceOrderByClause(strNsfOrderBy);
	}

	/**
	 * <pre>
	 * 수행된 SQL문의 결과가 있는지 없는지 판별한다.   
	 * </pre>
	 */
	private boolean NoRowsFound() throws SQLException, NException {
		int rows = pageIndexData.getInt(NPageConstants.ROWS);
		if (rows < 1)
			return true;
		else
			return false;
	}

	/**
	 * <pre>
	 * 수행결과인 NMultiData 에 paging index 정보를 추가한 후 반환한다.
	 * </pre>
	 * 
	 * @param pageResult
	 *            페이징되어 수행된 결과를 담고 있는 NMultiData
	 * @return 페이징인덱스 정보를 추가한 Object
	 */
	public NMultiData getPageIndexNMultiData(Object pageResult) {
		NMultiData pageResultAndIndex = new NMultiData("RESULT_INDEX");
		pageResultAndIndex.add(NPageConstants.PAGE_RESULT, pageResult);
		pageResultAndIndex.addNData(NPageConstants.PAGE_INDEX, this.pageIndexData);
		return pageResultAndIndex;
	}

	/**
	 * <pre>
	 * 설정된 페이징인덱스 정보를 반환한다. 
	 * </pre>
	 * 
	 * @return 페이징인덱스 정보
	 */
	public NData getPageIndexNData() {
		return this.pageIndexData;
	}
	
	/**
	 * <pre>
	 * SQL 문을 수행하여 결과값을 NMultiData형식으로 저장하여 리턴하는 함수이다.
	 * 이때 SQL 문에서 쓰인 파라메터 정보를 NData 형태로 직접 지정하여 준다. 
	 * NData 의 저장된 순서대로 SQL 문의 ? 와 매핑된다. 
	 * </pre>
	 * 
	 * @param con
	 *            Connection DataBase Connection .
	 * @param sql
	 *            String 수행 SQL 문.
	 * @param paramData
	 *            SQL 문을 구성하기 위한 파라미터 값
	 * @return SQL문 수행 결과값 NMultiData
	 * @throws SQLException
	 *             SQL 문의 총 결과 건수 처리시 오류가 발생한 경우
	 * @throws NException
	 */
	public NMultiData execute(Connection con, String sql, NData paramData) throws SQLException, NException {
		return executeQuery(con,sql,paramData,bindEnabled);		
	}
	
    /**
     * 쿼리를 수행한다.
     * @param con
     * @param sql
     * @param paramData
     * @return
     * @throws SQLException
     * @throws NException
     */
	private NMultiData executeQuery(Connection con, String sql, NData paramData) throws SQLException, NException {
	
		processPageIndex(con, sql, paramData);
		if (NoRowsFound()) {
			NMultiData pageResult = new NMultiData("LPAGE_RESULT");
			return getPageIndexNMultiData(pageResult);
		}

		NMultiData pageResult = execute(con, getPageSQL(sql), paramData, pageIndexData.getInt(NPageConstants.TARGET_ROW), pageIndexData.getInt(NPageConstants.NUMBER_OF_ROWS_OF_PAGE));

		return getPageIndexNMultiData(pageResult);
	}
	
    /**
     * 쿼리를 수행한다.
     * @param con
     * @param sql
     * @param paramData
     * @param isAutoDao
     * @return
     * @throws SQLException
     * @throws NException
     */
	private NMultiData executeQuery(Connection con, String sql, NData paramData, boolean isAutoDao) throws SQLException, NException {

		if(isAutoDao==false) {
			return executeQuery(con, sql, paramData);
		}
		
		char[] rawSql = sql.toCharArray();
		StringBuffer neoSql = new StringBuffer();
		final int size = rawSql.length;

		boolean isString = false;
		boolean isParameter = false;

		StringBuffer strbuf = null;
		int index = 1;

		NData neoParamData = new NData();

		for (int inx = 0; inx < size; inx++) {

			final char token = rawSql[inx];

			if (isParameter == true) {
				strbuf.append(token);
			}

			if (token == '$') {
				if (inx != 0 && rawSql[inx - 1] == '$') {
					neoSql.append('$');
				}
				isString = true;
			} else if (token == '{' && isString == true) {
				strbuf = new StringBuffer();
				isParameter = true;
				isString = false;
			} else if (token == '}' && isParameter == true) {

				isParameter = false;
				final String candidateKey = strbuf.toString();
				final String key = candidateKey.substring(0, candidateKey.length() - 1);

				final Object value = paramData.get(key);

				if (value != null) {
					neoParamData.put(index + "", value);
					neoSql.append("?");
					index++;
				} else {
					neoSql.append("${" + key + "}");
				}
			} else if (isParameter == false) {
				neoSql.append(token);
			}
		}
		return executeQuery(con, neoSql.toString(), neoParamData);
	}

	/**
	 * <pre>
	 * SQL 문을 수행하여 결과값을 NMultiData형식으로 저장하여 리턴하는 함수이다.
	 * 이때 SQL 문에서 쓰일 파라메터 정보를 HttpRequest 로 부터 자동으로 생성하여 
	 * SQL 문의 ? 와 매핑한다. 이때 SQL 문이 1:1 관계로만 표현되는 경우 및 between , in 으로  
	 * 구성된 간결한 SQL의 경우에 가능하다.   
	 * </pre>
	 * 
	 * @param con
	 *            Connection DataBase Connection .
	 * @param sql
	 *            String 수행 SQL 문.
	 * @return SQL문 수행 결과값 NMultiData
	 * @throws SQLException
	 * @throws NException
	 */
	public NMultiData execute(Connection con, String sql) throws SQLException, NException {
		NData paramData = getParameterNData(this.pageData, sql);
		return execute(con, sql, paramData);
	}

	/**
	 * <pre>
	 * 파라메터로 넘어온 PreparedStatement 에 대해서 NData 의 내용을 가지고 
	 * 순서대로 setString 을  호출한다.	 
	 * </pre>
	 * 
	 * @param pstmt
	 *            PreparedStatement 페이징하려는 SQL 문 .
	 * @param paramData
	 *            NData SQL 문 의 ? 에 매핑되는 값 정보 .
	 * @throws SQLException
	 */
	 private void callSetMethod(PreparedStatement pstmt, int inx, String key, Object paramObj, NData paramData) throws SQLException, NException {

		Class paramClassType = paramObj.getClass();
		if (paramClassType == String.class) {
			pstmt.setString(inx, paramData.getString(key));
		} else if (paramClassType == Integer.class) {
			pstmt.setInt(inx, paramData.getInt(key));
		} else if (paramClassType == Long.class) {
			pstmt.setLong(inx, paramData.getLong(key));
		} else if (paramClassType == Float.class) {
			pstmt.setFloat(inx, paramData.getFloat(key));
		} else if (paramClassType == Double.class) {
			pstmt.setDouble(inx, paramData.getFloat(key));
		} else if (paramClassType == Date.class) {
			pstmt.setDate(inx, (Date) paramData.get(key));
		} else if (paramClassType == Timestamp.class) {
			pstmt.setTimestamp(inx, (Timestamp) paramData.get(key));
		} else if (paramClassType == BigDecimal.class) {
			pstmt.setBigDecimal(inx, (BigDecimal) paramData.get(key));
		} else {
			throw new NException("NSF_PAG_008", " Error Unsupported Type - [" + paramClassType + "]");
		}

	}

	protected void setParamData(PreparedStatement pstmt, NData paramData) throws SQLException, NException {
		setParamData(pstmt, paramData, 1);
	}

	protected void setParamData(PreparedStatement pstmt, NData paramData, int startInx) throws SQLException, NException {

		if (paramData == null)
			throw new NException("NSF_PAG_008", " Error Unsupported Type - [ Parameter Data ] IS NULL ");
		int pstmtInx = startInx;
		Iterator iter = paramData.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String key = (String) entry.getKey();
			Object entryValObject = entry.getValue();

			// 파라메터 설정값이 NULL 인 경우에 자동변환은 해당 컬럼 타입을알수 없기 때문에 불가능
			// 따라서 Exception 으로 처리 하고 , 사용시 바깥에서 미리 NULL 체크후 변환한 후에
			// 파라메터를 설정하는 것으로 가이드 한다.

			if (entryValObject == null)
				throw new NException("NSF_PAG_008", " Error Unsupported Type - [ Parameter Object ] IS NULL ");
			callSetMethod(pstmt, pstmtInx, key, entryValObject, paramData);
			pstmtInx++;
		}

	}

	/**
	 * <pre>
	 * SQL 문에 대해 NData 형의 매핑정보를 이용하여 PreparedStatement 를 생성한다. 
	 * </pre>
	 */
	protected PreparedStatement getPagedPreparedStatement(Connection conn, String sql, NData paramData, int row, int size) throws SQLException, NException {

		PreparedStatement pstmt = null;

		try {

			// final String columnNames = getColumnNames(conn, sql, paramData);
			final String neoSql = makePageSql(sql, paramData, row, size);
			pstmt = conn.prepareStatement(neoSql);
			setParamData(pstmt, paramData);

		} catch (SQLException e) {
			NLogUtils.toDefaultLogForm(this.getClass().getName(), "getPagedPreparedStatement()", e.getMessage());
			throw e;
		}
		return pstmt;
	}

	/**
	 * <pre>
	 * SQL 문에 대해 NData 형의 매핑정보를 이용하여 수행한 후 NMultiData 형태로 
	 * 반환한다. 
	 * </pre>
	 */
	protected NMultiData execute(Connection conn, String sql, NData paramData, int row, int size) throws NException, SQLException {

		NMultiData pageResult = new NMultiData("LPAGE_RESULT");
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = getPagedPreparedStatement(conn, sql, paramData, row, size);
			rs = pstmt.executeQuery();
			pageResult = NResultSetConverter.toMultiData(rs);
		} catch (SQLException e) {
			NLogUtils.toDefaultLogForm(this.getClass().getName(), "execute()", e.getMessage());
			throw e;
		} finally {
			close(rs);
			close(pstmt);
		}

		return pageResult;

	}

	/**
	 * <pre>
	 * SQL 문에 대해 NData 형의 매핑정보를 이용하여 PreparedStatement 를 반환한다. 
	 * 이때 SQL 문에서 쓰일 파라메터 정보를 HttpRequest 로 부터 자동으로 생성하여 
	 * SQL 문의 ? 와 매핑한다. 이때 SQL 문이 1:1 관계로만 표현되는 경우 및 between , in 으로    
	 * 구성된 간결한 SQL의 경우에 가능하다.   
	 * </pre>
	 * 
	 * @return PreparedStatement
	 * @param con
	 *            Connection DataBase Connection .
	 * @param sql
	 *            String SQL 쿼리문.
	 * @throws SQLException
	 * @throws NException
	 */
	public PreparedStatement getPagedPreparedStatement(Connection con, String sql) throws NException, SQLException {
		NData paramData = getParameterNData(this.pageData, sql);
		return getPagedPreparedStatement(con, sql, paramData);
	}

	/**
	 * <pre>
	 * SQL 문에 대해 NData 형의 매핑정보를 이용하여 PreparedStatement 를 반환한다. 
	 * 이때 SQL 문에서 쓰인 파라메터 정보를 NData 형태로 직접 지정하여 준다. 
	 * NData 의 저장된 순서대로 SQL 문의 ? 와 매핑된다. 
	 * </pre>
	 * 
	 * @return PreparedStatement
	 * @param con
	 *            Connection DataBase Connection .
	 * @param sql
	 *            String SQL 쿼리문.
	 * @throws SQLException
	 * @throws NException
	 */
	public PreparedStatement getPagedPreparedStatement(Connection con, String sql, NData paramData) throws NException, SQLException {

		processPageIndex(con, sql, paramData);
		PreparedStatement lpstmt = getPagedPreparedStatement(con, getPageSQL(sql), paramData, pageIndexData.getInt(NPageConstants.TARGET_ROW), pageIndexData.getInt(NPageConstants.NUMBER_OF_ROWS_OF_PAGE));
		return lpstmt;
	}

	/**
	 * <pre>
	 * 쿼리문을 통해 얻어온 결과값들의 갯수를 리턴하는 함수이다.
	 * </pre>
	 * 
	 * @return int 수행한 쿼리문의 총 Row수
	 * @param conn
	 *            Connection 데이터 베이스 커넥션
	 * @param sql
	 *            String SQL 쿼리문
	 * @param NData
	 *            NData SQL 쿼리문을 구성하기 위한 파라미터
	 * @throws SQLException
	 *             JDBC 작업시 오류가 발생한 경우
	 */
	protected abstract int getLastRow(Connection conn, String sql, NData paramData) throws SQLException, NException;

	/**
	 * <pre>
	 * 대량의 데이터값을 페이지로 구성할 경우 쿼리를 페이징 되도록 수정하기 위한 함수이다.
	 * </pre>
	 * 
	 * @param rawSql
	 *            수정되기 전의 쿼리문
	 * @return 새로이 수정된 쿼리문
	 * @throws SQLException
	 *             JDBC 작업시 오류가 발생한 경우
	 */
	protected abstract String makePageSql(String rawSql, NData paramData, int targetRow, int pageSize) throws SQLException;

	/**
	 * <pre>
	 * Statement을 끊는 함수이다.
	 * </pre>
	 * 
	 * @param pstmt
	 *            PreparedStatement
	 * @throws SQLException
	 */
	protected final void close(PreparedStatement pstmt) throws SQLException {
		if (pstmt != null)
			pstmt.close();
	}

	/**
	 * <pre>
	 * ResultSet을 끊는 함수이다.
	 * </pre>
	 * 
	 * @param rs
	 *            ResultSet
	 * @throws SQLException
	 */
	protected final void close(ResultSet rs) throws SQLException {
		if (rs != null)
			rs.close();
	}

	/**
	 * <pre>
	 * 페이지 스펙에 정의된 한페이지당 보여질 건수를 조정하는  함수이다.
	 * </pre>
	 * 
	 * @param int
	 *            numOfRowsOfPage 한페이지당 보여질 건수
	 * @throws NException
	 */
	public void setNumOfRowsOfPage(int numOfRowsOfPage) {
		this.pageData.setInt(NPageConstants.NUMBER_OF_ROWS_OF_PAGE, numOfRowsOfPage);
	}

	/**
	 * <pre>
	 * 페이지 스펙에 정의된 한페이지당 보여질 페이지 인덱스 갯수를 조정하는  함수이다.
	 * </pre>
	 * 
	 * @param int
	 *            numberOfPagesOfIndex 한페이지당 보여질 건수
	 * @throws NException
	 */
	public void setNumberOfPagesOfIndex(int numberOfPagesOfIndex) {
		this.pageData.setInt(NPageConstants.NUMBER_OF_PAGES_OF_INDEX, numberOfPagesOfIndex);
	}

	/**
	 * @return Returns the bindEnabled.
	 */
	public boolean getBindEnabled() {
		return bindEnabled;
	}

	/**
	 * @param bindEnabled
	 * The bindEnabled to set.
	 */
	public void setBindEnabled(boolean bindEnabled) {
		this.bindEnabled = bindEnabled;
	}
}

