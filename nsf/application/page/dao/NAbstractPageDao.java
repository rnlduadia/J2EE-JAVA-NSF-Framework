package nsf.application.page.dao;

import java.sql.Connection;
import java.sql.SQLException;

import nsf.core.exception.NException;
import nsf.core.log.NLogUtils;
import nsf.foundation.persistent.db.connection.NConnectionManager;
import nsf.support.collection.NData;
import nsf.support.collection.NMultiData;

/**
 * <pre>
 * 페지에서 사용하는 NPageDao의 최상위 Abstract Class.
 * NAbstractPageDao Class는 DAO 를 특별히 만들지 않고도 Paging 기능을 제공하기 위한 클래스이다. 
 * </pre>
 * 
 * @since 2007/1/5
 * @version NSF 1.0
 * @author 김철남 Nova China
 */

public abstract class NAbstractPageDao {

	protected NAbstractPageStatement pageStatement = null;

	protected int rowSize;
    protected int pageSize;

	protected String dataSourceInfo = null;

	protected boolean bindEnabled = false;

	/**
	 * <pre>
	 * NAbstractPageDao 에서 내부적으로 사용되는  NPageStatement 인스턴스를 반환한다. 
	 * </pre>
	 * 
	 * @return pageStatement
	 */
	public NAbstractPageStatement getPageStatement() {
		return pageStatement;
	}

    /**
     * NAbstractPageDao의 생성자.
     * @param dataSourceInfo
     */
	protected NAbstractPageDao(String dataSourceInfo) {
		this(dataSourceInfo, 10, 10);
	}

    /**
     * NAbstractPageDao의 생성자.
     * @param dataSourceInfo
     * @param rowSize
     * @param pageSize
     */
	protected NAbstractPageDao(String dataSourceInfo, int rowSize, int pageSize) {
        this.rowSize = rowSize;
        this.pageSize = pageSize;
        this.dataSourceInfo = dataSourceInfo;
	}

	/**
	 * Connection을 맺는 함수이다.
	 * 
	 * @return java.sql.Connection  DB connection
	 * @exception SQLException
	 */
	private final Connection getConnection() throws SQLException {
		if (this.dataSourceInfo == null)
			return NConnectionManager.getConnection();
		else
			return NConnectionManager.getConnection(dataSourceInfo);

	}

	/**
	 * <pre>
	 * SQL 문을 수행하여 결과값을 NMultiData형식으로 저장하여 리턴하는 함수이다.
	 * </pre>
	 * 
	 * @param sql SQL 문
	 * @return NMultiData SQL 문 수행 결과값.
	 * @throws NException
	 */

	public final NMultiData execute(String sql) throws NException, SQLException {
		return execute(sql, null);
	}

	/**
	 * <pre>
	 * SQL 문을 수행하여 결과값을 NMultiData형식으로 저장하여 리턴하는 함수이다.
	 * 이때 SQL 문에서 쓰인 파라메터 정보를 NData 형태로 직접 지정하여 준다. 
	 * NData 의 저장된 순서대로 SQL 문의 ? 와 매핑된다. 
	 * </pre>
	 * 
	 * @param con Connection DataBase Connection .
	 * @param sql String 수행 SQL 문.
	 * @param paramData SQL 문을 구성하기 위한 파라메터 값
	 * @return SQL문 수행 결과값 NMultiData
	 * @throws SQLException
	 * @throws NException
	 */

	public NMultiData execute(String sql, NData paramData) throws NException, SQLException {

		NMultiData rtnVBox = null;
		Connection con = null;

		try {
			con = getConnection();

			if (paramData != null) {
				pageStatement.setBindEnabled(bindEnabled);
				rtnVBox = pageStatement.execute(con, sql, paramData);				
			}
			else{
				rtnVBox = pageStatement.execute(con, sql);
			}
		} catch (SQLException e) {
			NLogUtils.toDefaultLogForm(this.getClass().getName(), "execute()", e.getMessage());
			throw e;
		} finally {

			NConnectionManager.closeConnection(con);
		}
		return rtnVBox;
	}

	/**
	 * <pre>
	 * 페지 스펙에 정의된 한페지당 보여질 건수를 조정하는  함수이다.
	 * </pre>
	 * 
	 * @param int 
	 *            numOfRowsOfPage 한페지당 보여질 건수
	 */
	public void setNumOfRowsOfPage(int numOfRowsOfPage) {
		this.pageStatement.setNumOfRowsOfPage(numOfRowsOfPage);
	}

	/**
	 * <pre>
	 * 페지 스펙에 정의된 한페지당 보여질 page index개수를 조정하는  함수이다.
	 * </pre>
	 * 
	 * @param int
	 *            numberOfPagesOfIndex 한페지당 보여질 건수
	 * @throws NException
	 */
	public void setNumberOfPagesOfIndex(int numberOfPagesOfIndex) {
		this.pageStatement.setNumberOfPagesOfIndex(numberOfPagesOfIndex);
	}

	/**
	 * @return Returns the bindEnabled.
	 */
	public boolean getBindEnabled() {
		return bindEnabled;
	}

	/**
	 * @param bindEnabled
	 *            The bindEnabled to set.
	 */
	public void setBindEnabled(boolean bindEnabled) {
		this.bindEnabled = bindEnabled;
	}
}

