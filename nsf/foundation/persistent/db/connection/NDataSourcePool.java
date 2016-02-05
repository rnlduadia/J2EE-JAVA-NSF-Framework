package nsf.foundation.persistent.db.connection;
/**
 * @(#)NDataSourcePool.java
 */
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import nsf.core.config.NConfiguration;
import nsf.core.log.NLog;

/**
 * <pre>
 *기본적으로 리용하는 두가지 방식 , JNDI 방식과 JDBC 방식중에서 NSF에서는 JDBC방식의  Connection 획득에 필요한
 * NJdbcDataSource를 풀링하여 Connection 생성을 진행한다.
 * NDataSourcePool을 통해서 리턴되는 Connection 자원은 리용자의 목적에따라, DB 작업추적 
 *  가능한 NConnection을 리턴하거나, 일반적인 Connection을 리턴한다.
 * 
 * Default Connnection을 얻는 클라스는 NJdbcDataSource 이며, 이 정보는 Configration파일을 통하여 수정될수 있다.
 * </pre>
 * @since 200701/05
 * @version NSF 1.0
 * @author Nova China 량명호<br>
 */
public class NDataSourcePool implements Observer {

	/**
	 * JDBCTYPE의 Connection을 정의하는 int 변수
	 */
	public static final int JDBCTYPE = 1;

	/**
	 * NDataSourcePool의 싱글턴 객체
	 */
	private static NDataSourcePool singleton = null;

	/**
	 * 리용하는 DataSource의 Default 스펙명 정의
	 */
	private static final String DEFAULT_DATASOURCE = "default";

	/**
	 * 리용하는 jdbcDataSource 를 풀링하는 HashMap 변수
	 */
	private HashMap jdbcDataSourcePool = null;

	/**
	 * DB추적의향을 반영하는 변수 
	 */
	private boolean traceSwitch = false;

	/**
	 * 데이터소스 타입의 Default 정의
	 */
	private String defaultType = "JDBC";

	/**
	 * Default Constructor
	 * @throws Exception 
	 */
	private NDataSourcePool() {
		super();
		jdbcDataSourcePool = new HashMap();
		try {
			NConfiguration conf = NConfiguration.getInstance();
            String temp = conf.getString("/configuration/nsf/default-connectionPool", "JDBC");
			traceSwitch = conf.getBoolean("/configuration/nsf/log/dataBase-trace", false);
            
            if (!temp.equals(defaultType)){
                NLog.report.println("Default Pool 이 JDBC가 아닙니다. Configration에서 JDBC로 설정하고 다시 접속하십시오.");
                return ;
            }
		} catch (nsf.core.exception.NException ce) {
			defaultType = "JDBC";
		}
	}

	/**
	 * 환경파일(Configration파일)에서 지정한 Default DataSource Type의 Default Spec 에 대한 Connection 반환.
	 *
	 * @return Default Spec의 Connection
	 * @throws SQLException connection 획득 error가 발생할 경우.
	 */
	public Connection getConnection() throws SQLException {
		if (defaultType.equals("JDBC"))
			return getJDBCConnection(DEFAULT_DATASOURCE);
		else
			return null;
	}

	/**
	 * 환경파일(Configration파일)에서 지정한 Default DataSource Type의 dataSourceSpec 에 대한 Connection 반환.
	 *
	 * @param dataSourceSpec 
	 * @return dataSourceSpec에 해당하는 Connection
	 * @throws SQLException connection 획득 error가 발생할 경우.
	 */
	public Connection getConnection(String dataSourceSpec) throws SQLException {
		if (defaultType.equals("JDBC"))
			return getJDBCConnection(dataSourceSpec);
		else
			return null;
	}

	/**
	 * JDBC Type의 dataSourceSpec Spec 에 대한 Connection 반환.
	 *
	 * @param  dataSourceSpec 
	 * @return dataSourceSpec에 해당하는 Connection
	 * @throws SQLException connection 획득 error가 발생할 경우.
	 */
	public Connection getJDBCConnection(String dataSourceSpec) throws SQLException {
		
		NJdbcDataSource simpleDataSource = null;

		synchronized (this) {
			simpleDataSource = (NJdbcDataSource) jdbcDataSourcePool.get(dataSourceSpec);
			if (simpleDataSource == null) {
				// Configration파일에 설정된 DataSource로 dataSourcePool 구성.
				NJdbcDataSource ds = new NJdbcDataSource(dataSourceSpec);
				jdbcDataSourcePool.put(dataSourceSpec, ds);
				simpleDataSource = ds;
			}
		}
		if (traceSwitch)
			return new NConnection(simpleDataSource.getConnection());
		else
			return simpleDataSource.getConnection();
	}

	/**
	 * JDBC Type의 Default Spec 에 대한 Connection 정보를 반환
	 *
	 * @return Default Spec 에 해당하는 Connection 정보
	 */
	public String printJDBCConnStatus() {
		return printJDBCConnStatus(DEFAULT_DATASOURCE);
	}

	/**
	 * JDBC Type의 dataSourceSpec Spec 에 대한 Connection 정보를 반환
	 *
	 * @param  dataSourceSpec 데이터소스 스펙
	 * @return dataSourceSpec Spec 에 해당하는 Connection 정보
	 */
	public String printJDBCConnStatus(String dataSourceSpec) {
		NJdbcDataSource simpleDataSource = null;

		synchronized (this) {
			simpleDataSource = (NJdbcDataSource) jdbcDataSourcePool.get(dataSourceSpec);
			if (simpleDataSource == null) {

				NJdbcDataSource ds = new NJdbcDataSource(dataSourceSpec);
				jdbcDataSourcePool.put(dataSourceSpec, ds);
				simpleDataSource = ds;
			}
		}
		return simpleDataSource.getStatus();
	}

	/**
	 * 싱글턴 대상 객체가 존재하지 않을시 인스턴스화 한다.
	 *
	 * @return NDataSourcePool의 싱글턴 객체
	 */
	public static synchronized NDataSourcePool getInstance() {
		if (singleton == null) {
			singleton = new NDataSourcePool();

			try {
				NConfiguration conf = NConfiguration.getInstance();
				conf.addObserver(singleton);
			} catch (Exception e) {
				NLog.report.println(
					"[NDataSourcePool.getInstance()] : Fail to regist NDataSourcePool Object as observer.");
			}

		}
		return singleton;
	}

	/**
	 * NFileManager class 초기화를 위해 refresh()를 호출한다.
	 * @param o the observable object.
	 * @param arg notifyObservers method에 전달되어지는 argument.
	 */
	public void update(Observable o, Object arg) {
		if (singleton != null) {
			singleton = new NDataSourcePool();
			NLog.report.println("[NDataSourcePool.update()] : Update notifyed by NConfiguration.");
		}
	}
}

