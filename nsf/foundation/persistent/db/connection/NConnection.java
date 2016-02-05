package nsf.foundation.persistent.db.connection;
/**
 * @(#) NConnection.java
 */
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.Map;

/**
 * <pre>
 * NSF에서 제공하는 NDataSourcePool을 리용하여 connection을 요청한 경우, 사용자의 DB 작업 추적의향(Configration에 그 의향을 반영할수 있다)에 
 * 따라, Logging이 가능한 connection을 돌려줄수 있다.  이러한 경우에 NConnection이 그 역할을 수행한다.
 * </pre>
 * @since 2007/01/05
 * @version NSF 1.0
 * @author 량명호       Nova China<br>
 */
class NConnection implements Connection {
	
	private Connection connection = null;
	/**
	 * NConnection의 생성자
	 * @param connection객체
	 * @throws SQLException
	 */
	public NConnection(Connection connection) throws SQLException {
		if (connection == null)
			throw new SQLException("conneciton is null.");
		this.connection = connection;
	}
	
	/**
	 * SQL statement를 위한 Statement객체를 생성함.
	 * @return Statement 객체
	 * @throws SQLException
	 */
	public Statement createStatement() throws SQLException {
		return new NStatement(connection.createStatement());
	}
	
	/**
	 * 파라메터를 적용할 수 있는  SQL statement를 위한 PreparedStatement객체를 생성함.
	 * @param  sql문
	 * @return PreparedStatement 객체
	 * @throws SQLException
	 */
	public PreparedStatement prepareStatement(String sql) throws SQLException {
		return new NPreparedStatement(connection.prepareStatement(sql), sql);
	}
	
	/**
	 * stored procedure를 위한 CallableStatement 객체를  생성함.
	 * @param  sql문
	 * @return PreparedStatement 객체
	 * @throws SQLException
	 */
	public CallableStatement prepareCall(String sql) throws SQLException {
		return connection.prepareCall(sql);
	}
	
	/**
	 * 파라메터로 들어온 sql문을 native sql문으로 변환하여 반환한다.
	 * @param  변환하려고 하는 sql문
	 * @return 변환된 sql문
	 * @throws SQLException
	 */
	public String nativeSQL(String sql) throws SQLException {
		return connection.nativeSQL(sql);
	}
	
	/**
	 * 파라메터로 들어온 값에 따라 현재까지의 DB작업을 commit한다.
	 * @param  commit여부에 대한 Boolean변수.
	 * @throws SQLException
	 */
	public void setAutoCommit(boolean autoCommit) throws SQLException {
		connection.setAutoCommit(autoCommit);
	}
	
	/**
	 * Retrieves the current auto-commit mode for this Connection object
	 * @return the current state of this Connection object's auto-commit mode 
	 * @throws SQLException
	 */
	public boolean getAutoCommit() throws SQLException {
		return connection.getAutoCommit();
	}
	
	/**
	 * Makes all changes made since the previous commit/rollback
	 * permanent and releases any database locks currently held by this Connection object
	 * @throws SQLException
	 */
	public void commit() throws SQLException {
		connection.commit();
	}
	
	/**
	 * Undoes all changes made in the current transaction 
	 * and releases any database locks currently held by this Connection object
	 * @throws SQLException
	 */
	public void rollback() throws SQLException {
		connection.rollback();
	}
	
	/**
	 * Releases this Connection object's database and JDBC resources immediately 
	 * instead of waiting for them to be automatically released
	 * @throws SQLException
	 */
	public void close() throws SQLException {
		connection.close();
	}
	
	/**
	 * Retrieves whether this Connection object has been closed
	 * @return true if this Connection object is closed; false if it is still open 
	 * @throws SQLException
	 */
	public boolean isClosed() throws SQLException {
		return connection.isClosed();
	}
	
	/**
	 * Retrieves a DatabaseMetaData object that contains metadata about the database 
	 * to which this Connection object represents a connection
	 * @return a DatabaseMetaData object for this Connection object 
	 * @throws SQLException
	 */
	public DatabaseMetaData getMetaData() throws SQLException {
		return connection.getMetaData();
	}
	
	/**
	 * Puts this connection in read-only mode as a hint to the driver to enable database optimizations
	 * @throws SQLException
	 */
	public void setReadOnly(boolean readOnly) throws SQLException {
		connection.setReadOnly(readOnly);
	}
	
	/**
	 * Retrieves whether this Connection object is in read-only mode
	 * @return true if this Connection object is read-only; false otherwise 
	 * @throws SQLException
	 */
	public boolean isReadOnly() throws SQLException {
		return connection.isReadOnly();
	}
	
	/**
	 * Sets the given catalog name in order to select a subspace of 
	 * this Connection object's database in which to work
	 * @throws SQLException
	 */
	public void setCatalog(String catalog) throws SQLException {
		connection.setCatalog(catalog);
	}
	
	/**
	 * Retrieves this Connection object's current catalog name
	 * @return the current catalog name or null if there is none 
	 * @throws SQLException
	 */
	public String getCatalog() throws SQLException {
		return connection.getCatalog();
	}
	
	/**
	 * Attempts to change the transaction isolation level for this Connection object to the one given
	 * @throws SQLException
	 */
	public void setTransactionIsolation(int level) throws SQLException {
		connection.setTransactionIsolation(level);
	}
	
	/**
	 * Retrieves this Connection object's current transaction isolation level
	 * @return the current transaction isolation level
	 * @throws SQLException
	 */
	public int getTransactionIsolation() throws SQLException {
		return connection.getTransactionIsolation();
	}
	
	/**
	 * Retrieves the first warning reported by calls on this Connection object
	 * @return the first SQLWarning object or null if there are none 
	 * @throws SQLException
	 */
	public SQLWarning getWarnings() throws SQLException {
		return connection.getWarnings();
	}
	
	/**
	 * Clears all warnings reported for this Connection object
	 * @throws SQLException
	 */
	public void clearWarnings() throws SQLException {
		connection.clearWarnings();
	}
	
	/**
	 * Creates a Statement object that will generate ResultSet objects with the given type and concurrency.
	 * @return a new Statement object 
	 * @throws SQLException
	 */
	public Statement createStatement(int resultSetType, int resultSetConcurrency)
		throws SQLException {
		Statement stmt = connection.createStatement(resultSetType, resultSetConcurrency);
		return new NStatement(stmt);
	}

	/**
	 * Creates a Statement object that will generate ResultSet objects with the given type, 
	 * concurrency, and holdability
	 * @return a new Statement object
	 * @throws SQLException
	 */
	public Statement createStatement(
		int resultSetType,
		int resultSetConcurrency,
		int resultSetHoldability)
		throws SQLException {
		Statement stmt =
			connection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
		return new NStatement(stmt);
	}
	
	/**
	 * Creates a default PreparedStatement object that has the capability to retrieve auto-generated keys.
	 * @return a new PreparedStatement object
	 * @throws SQLException
	 */
	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
		throws SQLException {
		PreparedStatement stmt = connection.prepareStatement(sql, autoGeneratedKeys);
		return new NPreparedStatement(stmt, sql);
	}
	
	/**
	 * Creates a default PreparedStatement object capable of returning the auto-generated keys 
	 * designated by the given array
	 * @return a new PreparedStatement object
	 * @throws SQLException
	 */
	public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
		throws SQLException {
		PreparedStatement stmt = connection.prepareStatement(sql, columnIndexes);
		return new NPreparedStatement(stmt, sql);
	}
	
	/**
	 * Creates a default PreparedStatement object capable of returning the auto-generated keys 
	 * designated by the given array.
	 * @return a new PreparedStatement object
	 * @throws SQLException
	 */
	public PreparedStatement prepareStatement(String sql, String[] columnNames)
		throws SQLException {
		PreparedStatement stmt = connection.prepareStatement(sql, columnNames);
		return new NPreparedStatement(stmt, sql);
	}
	
	/**
	 * Creates a PreparedStatement object that will generate ResultSet objects with the given type 
	 * and concurrency
	 * @return a new PreparedStatement object
	 * @throws SQLException
	 */
	public PreparedStatement prepareStatement(
		String sql,
		int resultSetType,
		int resultSetConcurrency)
		throws SQLException {
		PreparedStatement stmt =
			connection.prepareStatement(sql, resultSetType, resultSetConcurrency);
		return new NPreparedStatement(stmt, sql);
	}
	
	/**
	 * Creates a PreparedStatement object that will generate ResultSet objects with the given type,
	 * concurrency, and holdability
	 * @return a new PreparedStatement object
	 * @throws SQLException
	 */
	public PreparedStatement prepareStatement(
		String sql,
		int resultSetType,
		int resultSetConcurrency,
		int resultSetHoldability)
		throws SQLException {
		PreparedStatement stmt =
			connection.prepareStatement(
				sql,
				resultSetType,
				resultSetConcurrency,
				resultSetHoldability);
		return new NPreparedStatement(stmt, sql);
	}
	
	/**
	 * Creates a CallableStatement object that will generate ResultSet objects with the given type 
	 * and concurrency
	 * @return a new CallableStatement object
	 * @throws SQLException
	 */
	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency)
		throws SQLException {
		return connection.prepareCall(sql, resultSetType, resultSetConcurrency);
		//CallableStatement stmt = connection.prepareCall(sql, resultSetType, resultSetConcurrency);
		//return new _CallableStatement( stmt, sql);
	}

	/**
	 * Creates a CallableStatement object that will generate ResultSet objects with the given type 
	 * and concurrency
	 * @return a new CallableStatement object
	 * @throws SQLException
	 */
	public CallableStatement prepareCall(
		String sql,
		int resultSetType,
		int resultSetConcurrency,
		int resultSetHoldability)
		throws SQLException {
		return connection.prepareCall(
			sql,
			resultSetType,
			resultSetConcurrency,
			resultSetHoldability);
		//CallableStatement stmt = connection.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
		//return new _CallableStatement( stmt, sql);
	}
	
	/**
	 * Retrieves the Map object associated with this Connection object
	 * @return the java.util.Map object associated with this Connection object
	 * @throws SQLException
	 */
	public Map getTypeMap() throws SQLException {
		return connection.getTypeMap();
	}
	
	/**
	 * Installs the given TypeMap object as the type map for this Connection object
	 * @throws SQLException
	 */
	public void setTypeMap(Map map) throws SQLException {
		connection.setTypeMap(map);
	}
	/**
	 * Retrieves the current holdability of ResultSet objects created using this Connection object.
	 * @return the holdability 
	 * @throws SQLException
	 */
	public int getHoldability() throws SQLException {
		return connection.getHoldability();
	}
	/**
	 * Removes the given Savepoint object from the current transaction
	 * @throws SQLException
	 */
	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		connection.releaseSavepoint(savepoint);
	}
	/**
	 * Undoes all changes made after the given Savepoint object was set
	 * @throws SQLException
	 */
	public void rollback(Savepoint savepoint) throws SQLException {
		connection.rollback(savepoint);
	}
	/**
	 * Changes the holdability of ResultSet objects created using this Connection object to the given holdability.
	 * @throws SQLException
	 */
	public void setHoldability(int holdability) throws SQLException {
		connection.setHoldability(holdability);
	}
	/**
	 * Creates an unnamed savepoint in the current transaction and returns the new Savepoint object that represents it
	 * @return the new Savepoint object 
	 * @throws SQLException
	 */
	public Savepoint setSavepoint() throws SQLException {
		return connection.setSavepoint();
	}
	/**
	 * Creates a savepoint with the given name in the current transaction and returns the new Savepoint object that represents it.
	 * @return the new Savepoint object
	 * @throws SQLException
	 */
	public Savepoint setSavepoint(String name) throws SQLException {
		return connection.setSavepoint(name);
	}
}

