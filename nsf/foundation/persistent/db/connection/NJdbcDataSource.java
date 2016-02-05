package nsf.foundation.persistent.db.connection;

/**
 * @(#) NJdbcDataSource.java
 */    

import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import nsf.core.config.NConfiguration;
import nsf.core.log.NLog;

/**
 * <pre>
 *  JDBC 방식의 커넥셕 획득에 필요한
 * NJdbcDataSource를 풀링하여 connection 생성을 진행한다.
 * </pre>
 * @since 2007/01/05
 * @version NSF 1.0
 * @author Nova China 량명호<br>
 */
public class NJdbcDataSource implements DataSource {

    // Property key 설정 변수
	/**
     * NJdbcDataSource Spec의 base property prefix
     */
    private static final String PROP_BASE = "/configuration/nsf/jdbc-datasource/spec<";

	/**
     * NJdbcDataSource Spec의 driver property postfix
     */    
    private static final String PROP_JDBC_DRIVER = ">/driver";

	/**
     * NJdbcDataSource Spec의 url property postfix
     */    
    private static final String PROP_JDBC_URL = ">/url";

	/**
     * NJdbcDataSource Spec의 user property postfix
     */    
    private static final String PROP_JDBC_USERNAME = ">/user";

	/**
     * NJdbcDataSource Spec의 password property postfix
     */    
    private static final String PROP_JDBC_PASSWORD = ">/password";

	/**
     * NJdbcDataSource Spec의 MaximumActiveConnections property
     */ 
    private static final String PROP_POOL_MAX_ACTIVE_CONN = "/configuration/nsf/jdbc-datasource/pool/max-active-connections";

	/**
     * NJdbcDataSource Spec의 MaximumIdleConnections property
     */ 
    private static final String PROP_POOL_MAX_IDLE_CONN = "/configuration/nsf/jdbc-datasource/pool/max-idle-connections";

	/**
     * NJdbcDataSource Spec의 MaximumCheckoutTime property
     */ 
    private static final String PROP_POOL_MAX_CHECKOUT_TIME = "/configuration/nsf/jdbc-datasource/pool/max-checkout-time";

	/**
     * NJdbcDataSource Spec의 TimeToWait property
     */ 
    private static final String PROP_POOL_TIME_TO_WAIT = "/configuration/nsf/jdbc-datasource/pool/wait-time";

	/**
     * NJdbcDataSource Spec의 PingQuery property
     */ 
    private static final String PROP_POOL_PING_QUERY = "/configuration/nsf/jdbc-datasource/pool/ping-query";

	/**
     * NJdbcDataSource Spec의 PingConnectionsOlderThan property
     */ 
    private static final String PROP_POOL_PING_CONN_OLDER_THAN = "/configuration/nsf/jdbc-datasource/pool/ping-connection-time";

	/**
     * NJdbcDataSource Spec의 PingEnabled property
     */ 
    private static final String PROP_POOL_PING_ENABLED = "/configuration/nsf/jdbc-datasource/pool/ping-enabled";

	/**
     * NJdbcDataSource Spec의 QuietMode property
     */ 
    private static final String PROP_POOL_QUIET_MODE = "/configuration/nsf/jdbc-datasource/pool/quiet-mode";

	/**
     * NJdbcDataSource Spec의 PingConnectionsNotUsedFor property
     */ 
    private static final String PROP_POOL_PING_CONN_NOT_USED_FOR = "/configuration/nsf/jdbc-datasource/pool/ping-connections-notused";

	/**
     * NJdbcDataSource Spec의 Connection Type 변수
     */ 
    private int expectedConnectionTypeCode;

	/**
     * NJdbcDataSource의 Pool 정보를 Synchronize하기위한 Object 변수
     */ 
    private static final Object POOL_LOCK = new Object();

	/**
     * NJdbcDataSource의 비활성화된 connection을 관리하는 List 객체
     */ 
    private List idleConnections = new ArrayList();

	/**
     * NJdbcDataSource의 활성화된 connection을 관리하는 List 객체
     */ 
    private List activeConnections = new ArrayList();

	/**
     * NJdbcDataSource에 의해 요청된 connection의 수
     */ 
    private long requestCount = 0;

	/**
     * NJdbcDataSource에 의해 요청에 걸린 루적 시간
     */ 
    private long accumulatedRequestTime = 0;

	/**
     * NJdbcDataSource에 의해 connection 반환에 걸린 루적 시간
     */ 
    private long accumulatedCheckoutTime = 0;

	/**
     * NJdbcDataSource에서 크기가  초과된 요청 개수 
     */ 
    private long claimedOverdueConnectionCount = 0;

	/**
     * NJdbcDataSource에서 크기가 초과된 요청에의한 connection들이 반환된 루적 시간
     */ 
    private long accumulatedCheckoutTimeOfOverdueConnections = 0;

	/**
     * NJdbcDataSource에 Wait걸린 connection 요청에 대한 루적 시간
     */ 
    private long accumulatedWaitTime = 0;

	/**
     * NJdbcDataSource에 대기중인 요청 개수 
     */ 
    private long hadToWaitCount = 0;

	/**
     * NJdbcDataSource에 불량 connection 개수 
     */ 
    private long badConnectionCount = 0;


    // JDBC 접속정보
	/**
     * NJdbcDataSource에 등록된 JDBC Driver 클라스명
     */ 
    private String jdbcDriver;

	/**
     * NJdbcDataSource에 등록된 JDBC 접속 URL
     */ 
    private String jdbcUrl;

	/**
     * NJdbcDataSource에 등록된 JDBC 접속 USERID
     */ 
    private String jdbcUsername;

	/**
     * NJdbcDataSource에 등록된 JDBC 접속 PASSWORD
     */ 
    private String jdbcPassword;

    
    // Configration에 등록된 설정값 변수
	/**
     * 환경파일Configration에 등록된 PoolMaximumActiveConnections 항목변수
     */ 
    private int poolMaximumActiveConnections;

	/**
     * 환경파일Configration에 등록된 PoolMaximumIdleConnections 항목변수
     */ 
    private int poolMaximumIdleConnections;

	/**
     * 환경파일Configration에 등록된 PoolMaximumCheckoutTime 항목변수
     */ 
    private int poolMaximumCheckoutTime;

	/**
     * 환경파일Configration에 등록된 PoolTimeToWait 항목변수
     */     
    private int poolTimeToWait;

	/**
     * 환경파일Configration에 등록된 PoolPingQuery 항목변수
     */     
    private String poolPingQuery;

	/**
     * 환경파일Configration에 등록된 PoolQuietMode 항목변수
     */     
    private boolean poolQuietMode;

	/**
     * 환경파일Configration에 등록된 PoolPingEnabled 항목변수
     */     
    private boolean poolPingEnabled;

	/**
     * 환경파일Configration에 등록된 PoolPingConnectionsOlderThan 항목변수
     */     
    private int poolPingConnectionsOlderThan;

	/**
     * 환경파일Configration에 등록된 PoolPingConnectionsNotUsedFor 항목변수
     */     
    private int poolPingConnectionsNotUsedFor;

	/**
     * Default Constructor
     */  
    public NJdbcDataSource() {
        initialize("default");
    }

	/**
     * Constructor with Spec name
     */  
    public NJdbcDataSource(String dbSpec) {
        initialize(dbSpec);
    }

	/**
     * dbSpec에 해당하는 property 정보를 로드하여 connection 풀을 생성한다.
     * 
     * @param dbSpec Database 스펙명
     */  
    private void initialize(String dbSpec) {
        try {
            NConfiguration conf = NConfiguration.getInstance();
            
            if (dbSpec == null)
                dbSpec = "default";

            jdbcDriver = conf.getString(PROP_BASE + dbSpec + PROP_JDBC_DRIVER, null);
            jdbcUrl = conf.getString(PROP_BASE + dbSpec + PROP_JDBC_URL, null);
            jdbcUsername = conf.getString(PROP_BASE + dbSpec + PROP_JDBC_USERNAME, null);
            jdbcPassword = conf.getString(PROP_BASE + dbSpec + PROP_JDBC_PASSWORD, null);

            poolMaximumActiveConnections = (conf.getInt(PROP_POOL_MAX_ACTIVE_CONN, 10));
            poolMaximumIdleConnections = (conf.getInt(PROP_POOL_MAX_IDLE_CONN, 5));
            poolMaximumCheckoutTime = (conf.getInt(PROP_POOL_MAX_CHECKOUT_TIME, 20000));
            poolTimeToWait = (conf.getInt(PROP_POOL_TIME_TO_WAIT, 15000));
            poolPingEnabled = (conf.getBoolean(PROP_POOL_PING_ENABLED, false));
            poolPingQuery = conf.getString(PROP_POOL_PING_QUERY, "No Ping QUERY SET");
            poolPingConnectionsOlderThan = (conf.getInt(PROP_POOL_PING_CONN_OLDER_THAN, 0));
            poolPingConnectionsNotUsedFor = (conf.getInt(PROP_POOL_PING_CONN_NOT_USED_FOR,0));
            poolQuietMode = (conf.getBoolean(PROP_POOL_QUIET_MODE, true));
            expectedConnectionTypeCode = assembleConnectionTypeCode(jdbcUrl, jdbcUsername, jdbcPassword);

            //Class.forName(jdbcDriver).newInstance();            
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	        classLoader.loadClass(jdbcDriver).newInstance();
			            

        } catch (Exception e) {
            throw new RuntimeException(
                "[NJdbcDataSource] Error while loading properties. Cause: " + e.toString());
        }
    }

	/**
     * 접속정보를 조합한 hash 값을 반환한다.
     * 
     * @param url 접속주소
     * @param username 사용자명
     * @param password 패스워드
     * @return 접속정보를 조합한 hash 값
     */
    private int assembleConnectionTypeCode(String url, String username, String password) {
        return ("" + url + username + password).hashCode();
    }

	/**
     * Connection을 반환한다.
     * 
     * @return connection
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException {
    	
        return popConnection(jdbcUsername, jdbcPassword);
    }

	/**
     * username에 해당하는 Connection을 반환한다.
     * 
     * @param username 사용자명
     * @param password 패스워드
     * @return connection
     * @throws SQLException
     */
    public Connection getConnection(String username, String password) throws SQLException {
        return popConnection(username, password);
    }

	/**
     * Connection을 반환한다.
     * 
     * @param conn 반환할 connection
     * @throws SQLException
     */
    public void freeConnection(Connection conn) throws SQLException {
    	this.pushConnection(conn);
    }
  
    
	/**
     * DB에 접속 대기할 최대 시간(초)을 설정한다.
     * 
     * @param loginTimeout 접속대기할 최대 시간(초)
     * @throws SQLException
     */
    public void setLoginTimeout(int loginTimeout) throws SQLException {
        DriverManager.setLoginTimeout(loginTimeout);
    }

	/**
     * DB에 접속대기할 최대 시간(초)을 반환한다.
     * 
     * @throws SQLException
     */
    public int getLoginTimeout() throws SQLException {
        return DriverManager.getLoginTimeout();
    }

	/**
     * DriverManager에서 로그/트레이싱에 사용할 PrintWriter object 를 설정한다.
     * 
     * @param logWriter DB정보를 Trace Logging할 Writer
     * @throws SQLException
     */
    public void setLogWriter(PrintWriter logWriter) throws SQLException {
        DriverManager.setLogWriter(logWriter);
    }

	/**
     * DriverManager에서 로그/트레이싱에 설정된 PrintWriter object 를 반환한다.
     * 
     * @throws SQLException
     */
    public PrintWriter getLogWriter() throws SQLException {
        return DriverManager.getLogWriter();
    }

	/**
     * 사용되지 않고있는 connection수를 반환한다.
     * 
     * @return 사용되지 않고있는 connection수
     */
    public int getPoolPingConnectionsNotUsedFor() {
        return poolPingConnectionsNotUsedFor;
    }

	/**
     * JDBC Driver 클라스명을 반환한다.
     * 
     * @return JDBC Driver 클라스명
     */
    public String getJdbcDriver() {
        return jdbcDriver;
    }

	/**
     * DB 접속 URL을 반환한다.
     * 
     * @return DB URL
     */
    public String getJdbcUrl() {
        return jdbcUrl;
    }

	/**
     * DB접속자 아이디를 반환한다.
     * 
     * @return 접속 아이디
     */
    public String getJdbcUsername() {
        return jdbcUsername;
    }

	/**
     * DB접속 패스워드를 반환한다.
     * 
     * @return 접속 패스워드
     */
    public String getJdbcPassword() {
        return jdbcPassword;
    }

	/**
     * 최대 활성 connection 수를 반환한다.
     * 
     * @return 최대 활성 connection 수
     */
    public int getPoolMaximumActiveConnections() {
        return poolMaximumActiveConnections;
    }

	/**
     * 최대 비활성 connection 수를 반환한다.
     * 
     * @return 최대 비활성 connection 수
     */
    public int getPoolMaximumIdleConnections() {
        return poolMaximumIdleConnections;
    }

	/**
     * 최대 connection 반환 시간을 반환한다.
     * 
     * @return 최대 connection 반환 시간
     */
    public int getPoolMaximumCheckoutTime() {
        return poolMaximumCheckoutTime;
    }

	/**
     * 접속 wait 시 대기시간을 반환한다.
     * 
     * @return 접속 wait 시 대기시간
     */
    public int getPoolTimeToWait() {
        return poolTimeToWait;
    }

	/**
     * ping test용 sql문를 반환한다.
     * 
     * @return ping test용 쿼리
     */
    public String getPoolPingQuery() {
        return poolPingQuery;
    }

	/**
     * ping test가 가능한지 여부를 반환한다.
     * 
     * @return ping test 가능여부 boolean
     */
    public boolean isPoolPingEnabled() {
        return poolPingEnabled;
    }

	/**
     * Connection 생성시 old connection으로 인식할 시간을 반환한다.
     * 
     * @return Connection 생성시 old connection으로 인식할 시간
     */
    public int getPoolPingConnectionsOlderThan() {
        return poolPingConnectionsOlderThan;
    }

	/**
     * Trace 모드 여부를 반환한다.
     * 
     * @return Trace 모드 여부 boolean
     */
    public boolean isPoolQuietMode() {
        return poolQuietMode;
    }

	/**
     * Trace 모드 시 해당 Object를 Logging한다.
     * 
     * @param o 로깅대상
     */
    private void log(Object o) {
        if (!isPoolQuietMode()) {
            NLog.report.println(o);
        }
    }

	/**
     * Trace 모드 시 해당 Object를 Logging한다.
     * 
     * @param o 로깅대상
     */
    public void setPoolQuietMode(boolean poolQuietMode) {
        this.poolQuietMode = poolQuietMode;
    }

	/**
     * 예상되는 Connection Type을 반환한다.
     * 
     * @return  예상되는 Connection Type
     */
    private int getExpectedConnectionTypeCode() {
        return expectedConnectionTypeCode;
    }

	/**
     * connection 요청 개수를 반환한다.
     * 
     * @return  connection 요청 개수
     */
    public long getRequestCount() {
        synchronized (POOL_LOCK) {
            return requestCount;
        }
    }

	/**
     * connection 요청에서 실제 반환에 이르기까지의 평균시간을 반환한다.
     * 
     * @return  connection 반환 평균시간
     */
    public long getAverageRequestTime() {
        synchronized (POOL_LOCK) {
            return requestCount == 0 ? 0 : accumulatedRequestTime / requestCount;
        }
    }

	/**
     * 평균 대기시간을 반환한다.
     * 
     * @return  평균 대기시간
     */
    public long getAverageWaitTime() {
        synchronized (POOL_LOCK) {
            return hadToWaitCount == 0 ? 0 : accumulatedWaitTime / hadToWaitCount;
        }
    }

	/**
     * Wait 걸린 시간 반환한다.
     * 
     * @return  wait  수 
     */
    public long getHadToWaitCount() {
        synchronized (POOL_LOCK) {
            return hadToWaitCount;
        }
    }

	/**
     * 불량 connection의 수를 반환한다.
     * 
     * @return  불량 connection 수
     */
    public long getBadConnectionCount() {
        synchronized (POOL_LOCK) {
            return badConnectionCount;
        }
    }

	/**
     * Maximum 을  넘어간 경우 DB에 반환되지 않은 connection의 수를 반환한다.
     * 
     * @return  반환되지 않는 connection
     */
    public long getClaimedOverdueConnectionCount() {
        synchronized (POOL_LOCK) {
            return claimedOverdueConnectionCount;
        }
    }

	/**
     * Maximum 을  넘어간 경우 DB에 반환되지 않은 connection의 평균 checkout 시간을 반환한다.
     * 
     * @return  반환되지 않는 connection들의 평균 checkout 시간 
     */
    public long getAverageOverdueCheckoutTime() {
        synchronized (POOL_LOCK) {
            return claimedOverdueConnectionCount == 0
                ? 0
                : accumulatedCheckoutTimeOfOverdueConnections / claimedOverdueConnectionCount;
        }
    }

	/**
     * 평균checkout 시간을 반환한다.
     * 
     * @return 평균 checkout 시간
     */
    public long getAverageCheckoutTime() {
        synchronized (POOL_LOCK) {
            return requestCount == 0 ? 0 : accumulatedCheckoutTime / requestCount;
        }
    }

	/**
     * 현재 풀의 속성 및 통계정보를 반환한다.
     * 
     * @return 속성 및 통계정보
     */
    public String getStatus() {
        StringBuffer buffer = new StringBuffer();

        buffer.append("\n===============================================================");
        buffer.append("\n jdbcDriver                     " + jdbcDriver);
        buffer.append("\n jdbcUrl                        " + jdbcUrl);
        buffer.append("\n jdbcUsername                   " + jdbcUsername);
        buffer.append(
            "\n jdbcPassword                   " + (jdbcPassword == null ? "NULL" : "************"));
        buffer.append("\n poolMaxActiveConnections       " + poolMaximumActiveConnections);
        buffer.append("\n poolMaxIdleConnections         " + poolMaximumIdleConnections);
        buffer.append("\n poolMaxCheckoutTime            " + poolMaximumCheckoutTime);
        buffer.append("\n poolTimeToWait                 " + poolTimeToWait);
        buffer.append("\n poolQuietMode                  " + poolQuietMode);
        buffer.append("\n poolPingEnabled                " + poolPingEnabled);
        buffer.append("\n poolPingQuery                  " + poolPingQuery);
        buffer.append("\n poolPingConnectionsOlderThan   " + poolPingConnectionsOlderThan);
        buffer.append("\n poolPingConnectionsNotUsedFor  " + poolPingConnectionsNotUsedFor);
        buffer.append("\n --------------------------------------------------------------");
        buffer.append("\n activeConnections              " + activeConnections.size());
        buffer.append("\n idleConnections                " + idleConnections.size());
        buffer.append("\n requestCount                   " + getRequestCount());
        buffer.append("\n averageRequestTime             " + getAverageRequestTime());
        buffer.append("\n averageCheckoutTime            " + getAverageCheckoutTime());
        buffer.append("\n claimedOverdue                 " + getClaimedOverdueConnectionCount());
        buffer.append("\n averageOverdueCheckoutTime     " + getAverageOverdueCheckoutTime());
        buffer.append("\n hadToWait                      " + getHadToWaitCount());
        buffer.append("\n averageWaitTime                " + getAverageWaitTime());
        buffer.append("\n badConnectionCount             " + getBadConnectionCount());
        buffer.append("\n===============================================================");
        return buffer.toString();
    }

	/**
     * 활성화된 connection이든, 비활성화된 connection이든 모두 종료한다.
     * 
     * @throws SQLException
     */
    public void forceCloseAll() throws SQLException {
        synchronized (POOL_LOCK) {
            for (int i = activeConnections.size(); i > 0; i--) {
                PooledJDBCConnection conn = (PooledJDBCConnection) activeConnections.remove(i - 1);
                Connection realConn = conn.getRealConnection();
                conn.invalidate();
                realConn.rollback();
                realConn.close();
            }
            for (int i = idleConnections.size(); i > 0; i--) {
                PooledJDBCConnection conn = (PooledJDBCConnection) idleConnections.remove(i - 1);
                Connection realConn = conn.getRealConnection();
                conn.invalidate();
                realConn.rollback();
                realConn.close();
            }
        }
        log("[NJdbcDataSource] Forcefully closed all connections.");
    }

	/**
     * 사용자에의해 반환된 connection이 최대 Idle 크기  이내이면 풀에 저장하고, 그 이상이면 
     * 종료한다.
     * 
     * @throws SQLException
     */
    private void pushConnection(Connection conn1) throws SQLException {
    	PooledJDBCConnection conn = (PooledJDBCConnection)conn1;
        synchronized (POOL_LOCK) {
            activeConnections.remove(conn);
            if (conn.isValid()) {
                if (idleConnections.size() < poolMaximumIdleConnections
                    && conn.getConnectionTypeCode() == getExpectedConnectionTypeCode()) {
                    accumulatedCheckoutTime += conn.getCheckoutTime();
                    conn.getRealConnection().rollback();
                    PooledJDBCConnection newConn = new PooledJDBCConnection(conn.getRealConnection(), this);
                    idleConnections.add(newConn);
                    newConn.setCreatedTimestamp(conn.getCreatedTimestamp());
                    newConn.setLastUsedTimestamp(conn.getLastUsedTimestamp());
                    conn.invalidate();
                    log("[NJdbcDataSource] Returned connection " + newConn.getRealHashCode() + " to pool.");
                    POOL_LOCK.notifyAll();
                } else {
                    accumulatedCheckoutTime += conn.getCheckoutTime();
                    conn.getRealConnection().rollback();
                    conn.getRealConnection().close();
                    log("[NJdbcDataSource] Closed connection " + conn.getRealHashCode() + ".");
                    conn.invalidate();
                }
            } else {
                log(
                    "[NJdbcDataSource] A bad connection ("
                        + conn.getRealHashCode()
                        + ") attempted to return to the pool, discarding connection.");
                badConnectionCount++;
            }
        }
    }

	/**
     * PooledJDBCConnection을 반환한다.
     * 
     * @throws SQLException
     */
    private PooledJDBCConnection popConnection(String username, String password) throws SQLException {
        boolean countedWait = false;
        PooledJDBCConnection conn = null;
        long t = System.currentTimeMillis();
        int localBadConnectionCount = 0;

        while (conn == null) {

            synchronized (POOL_LOCK) {
                if (idleConnections.size() > 0) {
                    // Pool has available connection
                    conn = (PooledJDBCConnection) idleConnections.remove(0);
                    log("[NJdbcDataSource] Checked out connection " + conn.getRealHashCode() + " from pool.");
                } else {
                    // Pool does not have available connection
                    if (activeConnections.size() < poolMaximumActiveConnections) {
                        // Can create new connection
                        conn = new PooledJDBCConnection(DriverManager.getConnection(jdbcUrl, username, password), this);//
                        if (conn.getRealConnection().getAutoCommit()) {
                            conn.getRealConnection().setAutoCommit(false);
                        }

                        log("[NJdbcDataSource] Created connection " + conn.getRealHashCode() + ".");
                    } else {
                        // Cannot create new connection
                        PooledJDBCConnection oldestActiveConnection = (PooledJDBCConnection) activeConnections.get(0);
                        long longestCheckoutTime = oldestActiveConnection.getCheckoutTime();
                        if (longestCheckoutTime > poolMaximumCheckoutTime) {
                            // Can claim overdue connection
                            claimedOverdueConnectionCount++;
                            accumulatedCheckoutTimeOfOverdueConnections += longestCheckoutTime;
                            accumulatedCheckoutTime += longestCheckoutTime;
                            activeConnections.remove(oldestActiveConnection);
                            oldestActiveConnection.getRealConnection().rollback();
                            conn = new PooledJDBCConnection(oldestActiveConnection.getRealConnection(), this);
                            oldestActiveConnection.invalidate();
                            log("[NJdbcDataSource] Claimed overdue connection " + conn.getRealHashCode() + ".");
                        } else {
                            // Must wait
                            try {
                                if (!countedWait) {
                                    hadToWaitCount++;
                                    countedWait = true;
                                }
                                log("[NJdbcDataSource] Waiting as long as " + poolTimeToWait + " milliseconds for connection.");
                                long wt = System.currentTimeMillis();
                                POOL_LOCK.wait(poolTimeToWait);
                                accumulatedWaitTime += System.currentTimeMillis() - wt;
                            } catch (InterruptedException e) {
                                break;
                            }
                        }
                    }
                }
                if (conn != null) {
                    if (conn.isValid()) {
                        conn.getRealConnection().rollback();
                        conn.setConnectionTypeCode(assembleConnectionTypeCode(jdbcUrl, username, password));
                        conn.setCheckoutTimestamp(System.currentTimeMillis());
                        conn.setLastUsedTimestamp(System.currentTimeMillis());
                        activeConnections.add(conn);
                        requestCount++;
                        accumulatedRequestTime += System.currentTimeMillis() - t;
                    } else {
                        log(
                            "[NJdbcDataSource] A bad connection ("
                                + conn.getRealHashCode()
                                + ") was returned from the pool, getting another connection.");
                        badConnectionCount++;
                        localBadConnectionCount++;
                        conn = null;
                        if (localBadConnectionCount > (poolMaximumIdleConnections + 3)) {
                            log("[NJdbcDataSource] Could not get a good connection to the database.");
                            throw new SQLException("[NJdbcDataSource] Could not get a good connection to the database.");
                        }
                    }
                }
            }
        }

        if (conn == null) {
            log("[NJdbcDataSource Error] Unknown severe error condition.  The connection pool returned a null connection.");
            throw new SQLException("[NJdbcDataSource Error] Unknown severe error condition.  The connection pool returned a null connection.");
        }

        return conn;
    }

	/**
     * PooledJDBCConnection에 대한 ping 검사를 수행하고 결과를  반환한다.
     * 
     * @return connection의 사용가능 여부
     */
    private boolean pingConnection(PooledJDBCConnection conn) {
        boolean result = true;

        try {
            result = !conn.getRealConnection().isClosed();
        } catch (SQLException e) {
            result = false;
        }

        if (result) {
            if (poolPingEnabled) {
                if ((poolPingConnectionsOlderThan > 0 && conn.getAge() > poolPingConnectionsOlderThan)
                    || (poolPingConnectionsNotUsedFor > 0
                        && conn.getTimeElapsedSinceLastUse() > poolPingConnectionsNotUsedFor)) {

                    try {
                        log("[NJdbcDataSource] Testing connection " + conn.getRealHashCode() + "...");
                        Statement statement = conn.createStatement();
                        ResultSet rs = statement.executeQuery(poolPingQuery);
                        rs.close();
                        statement.close();
                        conn.rollback();
                        result = true;
                        log("[NJdbcDataSource] Connection " + conn.getRealHashCode() + " is GOOD!");
                    } catch (Exception e) {
                        try {
                            conn.getRealConnection().close();
                        } catch (Exception e2) {
                            //ignore
                        }
                        result = false;
                        log("[NJdbcDataSource] Connection " + conn.getRealHashCode() + " is BAD!");
                    }
                }
            }
        }
        return result;
    }
    
    /**
     * Pool의 connection 객체가 아닌 real connection 객체를 얻어온다. 
     * @param conn
     * @return real connection
     */
    public static Connection unwrapConnection(Connection conn) {
        if (conn instanceof PooledJDBCConnection) {
            return ((PooledJDBCConnection) conn).getRealConnection();
        } else {
            return conn;
        }
    }

    /**
     * ---------------------------------------------------------------------------------------
     *                               PooledJDBCConnection
     * ---------------------------------------------------------------------------------------
     */
    private static class PooledJDBCConnection implements Connection {

        private int hashCode = 0;
        private NJdbcDataSource dataSource;
        private Connection realConnection;
        private long checkoutTimestamp;
        private long createdTimestamp;
        private long lastUsedTimestamp;
        private int connectionTypeCode;
        private boolean valid;

        public PooledJDBCConnection(Connection connection, NJdbcDataSource dataSource) {

            this.hashCode = connection.hashCode();
            this.realConnection = connection;
            this.dataSource = dataSource;
            this.createdTimestamp = System.currentTimeMillis();
            this.lastUsedTimestamp = System.currentTimeMillis();
            this.valid = true;
        }

        public void invalidate() {
            valid = false;
        }

        public boolean isValid() {
            return valid && realConnection != null && dataSource.pingConnection(this);
        }

        public Connection getRealConnection() {
            return realConnection;
        }

        public int getRealHashCode() {
            if (realConnection == null)
                return 0;
            else
                return realConnection.hashCode();

        }

        public int getConnectionTypeCode() {
            return connectionTypeCode;
        }

        public void setConnectionTypeCode(int connectionTypeCode) {
            this.connectionTypeCode = connectionTypeCode;
        }

        public long getCreatedTimestamp() {
            return createdTimestamp;
        }

        public void setCreatedTimestamp(long createdTimestamp) {
            this.createdTimestamp = createdTimestamp;
        }

        public long getLastUsedTimestamp() {
            return lastUsedTimestamp;
        }

        public void setLastUsedTimestamp(long lastUsedTimestamp) {
            this.lastUsedTimestamp = lastUsedTimestamp;
        }

        public long getTimeElapsedSinceLastUse() {
            return System.currentTimeMillis() - lastUsedTimestamp;
        }

        public long getAge() {
            return System.currentTimeMillis() - createdTimestamp;
        }

        public long getCheckoutTimestamp() {
            return checkoutTimestamp;
        }

        public void setCheckoutTimestamp(long timestamp) {
            this.checkoutTimestamp = timestamp;
        }

        public long getCheckoutTime() {
            return System.currentTimeMillis() - checkoutTimestamp;
        }

        private Connection getValidConnection() {
            if (!valid) {
                throw new RuntimeException("[PooledJDBCConnection Error] Connection has been invalidated (probably released back to the pool).");
            }
            return realConnection;
        }

        // **********************************
        // Implemented Connection Methods
        // **********************************

        public Statement createStatement() throws SQLException {
            return getValidConnection().createStatement();
        }

        public PreparedStatement prepareStatement(String sql) throws SQLException {
            return getValidConnection().prepareStatement(sql);
        }

        public CallableStatement prepareCall(String sql) throws SQLException {
            return getValidConnection().prepareCall(sql);
        }

        public String nativeSQL(String sql) throws SQLException {
            return getValidConnection().nativeSQL(sql);
        }

        public void setAutoCommit(boolean autoCommit) throws SQLException {

            getValidConnection().setAutoCommit(autoCommit);
        }

        public boolean getAutoCommit() throws SQLException {
            return getValidConnection().getAutoCommit();
        }

        public void commit() throws SQLException {
            getValidConnection().commit();
        }

        public void rollback() throws SQLException {
            getValidConnection().rollback();
        }

        public void close() throws SQLException {
            dataSource.pushConnection(this);
        }

        public boolean isClosed() throws SQLException {
            return getValidConnection().isClosed();
        }

        public DatabaseMetaData getMetaData() throws SQLException {
            return getValidConnection().getMetaData();
        }

        public void setReadOnly(boolean readOnly) throws SQLException {
            getValidConnection().setReadOnly(readOnly);
        }

        public boolean isReadOnly() throws SQLException {
            return getValidConnection().isReadOnly();
        }

        public void setCatalog(String catalog) throws SQLException {
            getValidConnection().setCatalog(catalog);
        }

        public String getCatalog() throws SQLException {
            return getValidConnection().getCatalog();
        }

        public void setTransactionIsolation(int level) throws SQLException {
            getValidConnection().setTransactionIsolation(level);
        }

        public int getTransactionIsolation() throws SQLException {
            return getValidConnection().getTransactionIsolation();
        }

        public SQLWarning getWarnings() throws SQLException {
            return getValidConnection().getWarnings();
        }

        public void clearWarnings() throws SQLException {
            getValidConnection().clearWarnings();
        }

        public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
            return getValidConnection().createStatement(resultSetType, resultSetConcurrency);
        }

        public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
            throws SQLException {
            return getValidConnection().prepareCall(sql, resultSetType, resultSetConcurrency);
        }

        public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency)
            throws SQLException {
            return getValidConnection().prepareCall(sql, resultSetType, resultSetConcurrency);
        }

        public Map getTypeMap() throws SQLException {
            return getValidConnection().getTypeMap();
        }

        public void setTypeMap(Map map) throws SQLException {
            getValidConnection().setTypeMap(map);
        }

        public int hashCode() {
            return hashCode;
        }

        public boolean equals(Object obj) {
            if (obj instanceof PooledJDBCConnection) {
                return realConnection.hashCode() == (((PooledJDBCConnection) obj).realConnection.hashCode());
            } else if (obj instanceof Connection) {
                return hashCode == obj.hashCode();
            } else {
                return false;
            }
        }

        // **********************************
        // JDK 1.4 JDBC 3.0 Methods below
        // **********************************

        public void setHoldability(int holdability) throws SQLException {
            getValidConnection().setHoldability(holdability);
        }

        public int getHoldability() throws SQLException {
            return getValidConnection().getHoldability();
        }

        public Savepoint setSavepoint() throws SQLException {
            return getValidConnection().setSavepoint();
        }

        public Savepoint setSavepoint(String name) throws SQLException {
            return getValidConnection().setSavepoint(name);
        }

        public void rollback(Savepoint savepoint) throws SQLException {
            getValidConnection().rollback(savepoint);
        }

        public void releaseSavepoint(Savepoint savepoint) throws SQLException {
            getValidConnection().releaseSavepoint(savepoint);
        }

        public Statement createStatement(
            int resultSetType,
            int resultSetConcurrency,
            int resultSetHoldability)
            throws SQLException {
            return getValidConnection().createStatement(
                resultSetType,
                resultSetConcurrency,
                resultSetHoldability);
        }

        public PreparedStatement prepareStatement(
            String sql,
            int resultSetType,
            int resultSetConcurrency,
            int resultSetHoldability)
            throws SQLException {
            return getValidConnection().prepareStatement(
                sql,
                resultSetType,
                resultSetConcurrency,
                resultSetHoldability);
        }

        public CallableStatement prepareCall(
            String sql,
            int resultSetType,
            int resultSetConcurrency,
            int resultSetHoldability)
            throws SQLException {
            return getValidConnection().prepareCall(
                sql,
                resultSetType,
                resultSetConcurrency,
                resultSetHoldability);
        }

        public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
            return getValidConnection().prepareStatement(sql, autoGeneratedKeys);
        }

        public PreparedStatement prepareStatement(String sql, int columnIndexes[]) throws SQLException {
            return getValidConnection().prepareStatement(sql, columnIndexes);
        }

        public PreparedStatement prepareStatement(String sql, String columnNames[]) throws SQLException {
            return getValidConnection().prepareStatement(sql, columnNames);
        }
    }
}

