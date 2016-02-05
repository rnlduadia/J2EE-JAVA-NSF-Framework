package nsf.foundation.persistent.db.connection;

/**
 * @(#) NConnectionManager.java
 */
import java.sql.Connection;
import java.sql.SQLException;

import nsf.core.log.NLog;

/**
 * <pre>
 *    NDataSourcePool을 리용하여 connection을
 *    조종하는것을 돕는 클라스이다.
 * </pre>
 * 
 * @since 2007/01/05
 * @version NSF 1.0
 * @author Nova China 량명호<br>
 */

public class NConnectionManager {

	/**
	 * NDataSoucePool로부터 instance를 얻어서 connection을 얻어온다.
	 * 
	 * @return conn Connection
	 */
	public static Connection getConnection() throws SQLException {
        
		Connection 	conn = NDataSourcePool.getInstance().getConnection();
		return conn;
	}

	/**
     * NDataSoucePool로부터 실체를 얻어서 nsf.xml 파일의 자료기지 스펙에 해당하는 connection을
     * 얻는다.
     * 
     * @param dataSourceSpec
     * @return
     * @throws SQLException
	 */
	public static Connection getConnection(String dataSourceSpec) throws SQLException {
        
		Connection conn = NDataSourcePool.getInstance().getConnection(dataSourceSpec);
		return conn;
	}

	/**
     * Connection을 해제한다.
     * 
     * @param conn
	 */
	public static void closeConnection(Connection conn) {
		try {

			if (conn != null && !conn.isClosed())
				conn.close();
		} catch (SQLException se) {
			NLog.report.println(se.toString());
		}
	}
}

