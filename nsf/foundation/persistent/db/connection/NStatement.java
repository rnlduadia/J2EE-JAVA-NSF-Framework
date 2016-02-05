package nsf.foundation.persistent.db.connection;
/**
 * @(#) NStatement.java
 */
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;

import nsf.support.tools.time.NStopWatch;

/**
 * <pre>
 * NStatement는 NConnection을 통하여 Statement를 생성할 경우에 리턴된다.
 * NStatement는 사용자에 의해서 execute(), executeUpdate(), executeQuery()가 수행될때, 관련 DB작업에 
 * 대한 로그정보를 남겨줄수 있도록 NDBTrace 클라스를 통하여 적절한 메소드를 호출해주는 역할을 한다. 
 * </pre>
 * @since 2007/01/05
 * @version NSF 1.0
 * @author Nova China 량명호<br>
 */
public class NStatement implements Statement {
	private Statement statement;
	public NStatement(Statement statement) {
		this.statement = statement;
	}
	/**
	 * Executes the given SQL statement
	 * @param SQL statement 
	 * @return a ResultSet object
	 * @throws SQLException
	 * @see java.sql.Statement#executeQuery(String)
	 */
	public ResultSet executeQuery(String sql) throws SQLException {
		NStopWatch watch = new NStopWatch();
		boolean isError = false;
		try {
			return statement.executeQuery(sql); //new _ResultSet(statement.executeQuery(sql));
		} catch (SQLException se) {
			isError = true;
			NDBTracer.print_error("STMT.EQ", watch.getElapsed(), sql, null, se.toString());
			throw se;
		} finally {
			if ( !isError )
			NDBTracer.print_sql("STMT.EQ", watch.getElapsed(), sql, null, 0);
		}
	}
	/**
	 * Executes the given SQL statement
	 * @param SQL statement
	 * @throws SQLException
	 * @see java.sql.Statement#executeUpdate(String)
	 */
	public int executeUpdate(String sql) throws SQLException {
		NStopWatch watch = new NStopWatch();
		int result = 0;
		boolean isError = false;
		try {
			result = statement.executeUpdate(sql);
		} catch (SQLException se) {
			isError = true;
			NDBTracer.print_error("STMT.EU", watch.getElapsed(), sql, null, se.toString());
			throw se;
		} finally {
			if ( !isError )
			NDBTracer.print_sql("STMT.EU", watch.getElapsed(), sql, null, result);
		}
		return result;
	}
	/**
	 * Releases this Statement object's database and JDBC resources immediately 
	 * @throws SQLException
	 * @see java.sql.Statement#close()
	 */
	public void close() throws SQLException {
		try {
			statement.close();
			//ResTrace.dec("stmt");
		} catch (SQLException e) {
			//XLog.print(false, "stmt close error => " + e);
			throw e;
		}
	}
	/**
	 * Retrieves the maximum number of bytes 
	 * @return the current column size limit 
	 * @throws SQLException
	 * @see java.sql.Statement#getMaxFieldSize()
	 */
	public int getMaxFieldSize() throws SQLException {
		return statement.getMaxFieldSize();
	}
	/**
	 * Sets the limit for the maximum number of bytes
	 * @param the new column size limit 
	 * @throws SQLException
	 * @see java.sql.Statement#setMaxFieldSize(int)
	 */
	public void setMaxFieldSize(int max) throws SQLException {
		statement.setMaxFieldSize(max);
	}
	/**
	 * Retrieves the maximum number of rows 
	 * @return the current maximum number of rows 
	 * @throws SQLException
	 * @see java.sql.Statement#getMaxRows()
	 */
	public int getMaxRows() throws SQLException {
		return statement.getMaxRows();
	}
	/**
	 * Sets the limit for the maximum number of rows 
	 * @param the new max rows limit
	 * @throws SQLException
	 * @see java.sql.Statement#setMaxRows(int)
	 */
	public void setMaxRows(int max) throws SQLException {
		statement.setMaxRows(max);
	}
	/**
	 * Sets escape processing on or off.
	 * @param true to enable escape processing
	 * @throws SQLException
	 * @see java.sql.Statement#setEscapeProcessing(boolean)
	 */
	public void setEscapeProcessing(boolean enable) throws SQLException {
		statement.setEscapeProcessing(enable);
	}
	/**
	 * Retrieves the number of seconds
	 * @return the current query timeout limit  
	 * @throws SQLException
	 * @see java.sql.Statement#getQueryTimeout()
	 */
	public int getQueryTimeout() throws SQLException {
		return statement.getQueryTimeout();
	}
	/**
	 * Sets the number of seconds
	 * @param the new query timeout limit 
	 * @throws SQLException
	 * @see java.sql.Statement#setQueryTimeout(int)
	 */
	public void setQueryTimeout(int seconds) throws SQLException {
		statement.setQueryTimeout(seconds);
	}
	/**
	 * Cancels this Statement object
	 * @throws SQLException
	 * @see java.sql.Statement#cancel()
	 */
	public void cancel() throws SQLException {
		statement.cancel();
	}
	/**
	 * Retrieves the first warning reported by calls on this Statement object
	 * @return the first SQLWarning object 
	 * @throws SQLException
	 * @see java.sql.Statement#getWarnings()
	 */
	public SQLWarning getWarnings() throws SQLException {
		return statement.getWarnings();
	}
	/**
	 * Clears all the warnings reported on this Statement object
	 * @throws SQLException
	 * @see java.sql.Statement#clearWarnings()
	 */
	public void clearWarnings() throws SQLException {
		statement.clearWarnings();
	}
	/**
	 * Sets the SQL cursor name to the given String
	 * @param the new cursor name
	 * @throws SQLException
	 * @see java.sql.Statement#setCursorName(String)
	 */
	public void setCursorName(String name) throws SQLException {
		statement.setCursorName(name);
	}
	/**
	 * Executes the given SQL statement
	 * @param sql statement
	 * @throws SQLException
	 * @see java.sql.Statement#execute(String)
	 */
	public boolean execute(String sql) throws SQLException {
		NStopWatch watch = new NStopWatch();
		boolean isError = false;
		try {
			return statement.execute(sql);
		} catch (SQLException se) {
			isError = true;
			NDBTracer.print_error("STMT.E", watch.getElapsed(), sql, null, se.toString());
			throw se;
		} finally {
			if ( !isError )
			NDBTracer.print_sql("STMT.E", watch.getElapsed(), sql, null, 0);
		}
	}
	/**
	 * Retrieves the current result as a ResultSet object
	 * @return the current result as a ResultSet object 
	 * @throws SQLException
	 * @see java.sql.Statement#getResultSet()
	 */
	public ResultSet getResultSet() throws SQLException {
		return statement.getResultSet();
		/*ResultSet rs = statement.getResultSet();
		if (rs instanceof _ResultSet)
			return rs;
		else
			return new _ResultSet(rs);*/
	}
	/**
	 * Retrieves the current result as an update count
	 * @return the current result as an update count
	 * @throws SQLException
	 * @see java.sql.Statement#getUpdateCount()
	 */
	public int getUpdateCount() throws SQLException {
		return statement.getUpdateCount();
	}
	/**
	 * Moves to this Statement object's next result
	 * @return true if the next result is a ResultSet object
	 * @throws SQLException
	 * @see java.sql.Statement#getMoreResults()
	 */
	public boolean getMoreResults() throws SQLException {
		return statement.getMoreResults();
	}
	/**
	 * Gives the driver a hint as to the direction
	 * @param the initial direction for processing rows 
	 * @throws SQLException
	 * @see java.sql.Statement#setFetchDirection(int)
	 */
	public void setFetchDirection(int direction) throws SQLException {
		statement.setFetchDirection(direction);
	}
	/**
	 * Retrieves the direction for fetching rows from database tables 
	 * @return the default fetch direction for result sets 
	 * @throws SQLException
	 * @see java.sql.Statement#getFetchDirection()
	 */
	public int getFetchDirection() throws SQLException {
		return statement.getFetchDirection();
	}
	/**
	 * Gives the JDBC driver a hint as to the number of rows
	 * @param the number of rows to fetch 
	 * @throws SQLException
	 * @see java.sql.Statement#setFetchSize(int)
	 */
	public void setFetchSize(int rows) throws SQLException {
		statement.setFetchSize(rows);
	}
	/**
	 * Retrieves the number of result set rows that is the default fetch size 
	 * @return the default fetch size 
	 * @throws SQLException
	 * @see java.sql.Statement#getFetchSize()
	 */
	public int getFetchSize() throws SQLException {
		return statement.getFetchSize();
	}
	/**
	 * Retrieves the result set concurrency for ResultSet objects generated by this Statement object
	 * @return int
	 * @throws SQLException
	 * @see java.sql.Statement#getResultSetConcurrency()
	 */
	public int getResultSetConcurrency() throws SQLException {
		return statement.getResultSetConcurrency();
	}
	/**
	 * Retrieves the result set type for ResultSet objects generated by this Statement object
	 * @return int
	 * @throws SQLException
	 * @see java.sql.Statement#getResultSetType()
	 */
	public int getResultSetType() throws SQLException {
		return statement.getResultSetType();
	}
	/**
	 * Adds the given SQL command to the current list of commmands for this Statement object
	 * @param sql statement
	 * @throws SQLException
	 * @see java.sql.Statement#addBatch(String)
	 */
	public void addBatch(String sql) throws SQLException {
		NDBTracer.print_sql("STMT.AB", sql, null);
		statement.addBatch(sql);
	}
	/**
	 * Empties this Statement object's current list of SQL commands
	 * @throws SQLException
	 * @see java.sql.Statement#clearBatch()
	 */
	public void clearBatch() throws SQLException {
		statement.clearBatch();
	}
	/**
	 * Submits a batch of commands to the database for execution 
	 * @return an array of update counts
	 * @throws SQLException
	 * @see java.sql.Statement#executeBatch()
	 */
	public int[] executeBatch() throws SQLException {
		NStopWatch watch = new NStopWatch();
		boolean isError = false;
		try {
			return statement.executeBatch();
		} catch (SQLException se) {
			isError = true;
			NDBTracer.print_error("STMT.EB", watch.getElapsed(), se.toString());
			throw se;
		} finally { 
			if ( !isError )
			NDBTracer.print_sql("STMT.EB", watch.getElapsed());
		}
	}
	/**
	 * Retrieves the Connection object that produced this Statement object
	 * @return the connection 
	 * @throws SQLException
	 * @see java.sql.Statement#getConnection()
	 */
	public Connection getConnection() throws SQLException {
		return new NConnection(this.statement.getConnection());
	}
	/**
	 * Executes the given SQL statement
	 * @param SQL statement
	 * @param autoGeneratedKeys
	 * @return true if the first result is a ResultSet object
	 * @throws SQLException
	 * @see java.sql.Statement#execute(String, int)
	 */
	public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
		NStopWatch watch = new NStopWatch();
		boolean isError = false;
		try {
			return statement.execute(sql, autoGeneratedKeys);
		} catch (SQLException se) {
			isError = true;
			NDBTracer.print_error("STMT.E", watch.getElapsed(), sql, null, se.toString());
			throw se;
		} finally {
			if ( !isError )
			NDBTracer.print_sql("STMT.E", watch.getElapsed(), sql, null, 0);
		}
	}
	/**
	 * Executes the given SQL statement
	 * @param SQL statement
	 * @param columnIndexes
	 * @return true if the first result is a ResultSet object
	 * @throws SQLException
	 * @see java.sql.Statement#execute(String, in[])
	 */
	public boolean execute(String sql, int[] columnIndexes) throws SQLException {
		NStopWatch watch = new NStopWatch();
		boolean isError = false;
		try {
			return statement.execute(sql, columnIndexes);
		} catch (SQLException se) {
			isError = true;
			NDBTracer.print_error("STMT.E", watch.getElapsed(), sql, null, se.toString());
			throw se;
		} finally {
			if ( !isError )
			NDBTracer.print_sql("STMT.E", watch.getElapsed(), sql, null, 0);
		}
	}
	/**
	 * Executes the given SQL statement
	 * @param SQL statement
	 * @param columnNames
	 * @return true if the first result is a ResultSet object
	 * @throws SQLException
	 * @see java.sql.Statement#execute(String, String[])
	 */
	public boolean execute(String sql, String[] columnNames) throws SQLException {
		NStopWatch watch = new NStopWatch();
		boolean isError = false;
		try {
			return statement.execute(sql, columnNames);
		} catch (SQLException se) {
			isError = true;
			NDBTracer.print_error("STMT.E", watch.getElapsed(), sql, null, se.toString());
			throw se;
		} finally {
			if ( !isError )
			NDBTracer.print_sql("STMT.E", watch.getElapsed(), sql, null, 0);
		}
	}
	/**
	 * Executes the given SQL statement 
	 * @param SQL statement
	 * @param autoGeneratedKeys
	 * @return the row count for INSERT, UPDATE or DELETE statements
	 * @throws SQLException
	 * @see java.sql.Statement#executeUpdate(String, int)
	 */
	public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
		NStopWatch watch = new NStopWatch();
		int result = 0;
		boolean isError = false;
		try {
			result = statement.executeUpdate(sql, autoGeneratedKeys);
		} catch (SQLException se) {
			isError = true;
			NDBTracer.print_error("STMT.EU", watch.getElapsed(), sql, null, se.toString());
			throw se;
		} finally {
			if ( !isError )
			NDBTracer.print_sql("STMT.EU", watch.getElapsed(), sql, null, result);
		}
		return result;
	}
	/**
	 * Executes the given SQL statement 
	 * @param SQL statement
	 * @param columnIndexes
	 * @return the row count for INSERT, UPDATE or DELETE statements
	 * @throws SQLException
	 * @see java.sql.Statement#executeUpdate(String, int[])
	 */
	public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
		NStopWatch watch = new NStopWatch();
		int result = 0;
		boolean isError = false;
		try {
			result = statement.executeUpdate(sql, columnIndexes);
		} catch (SQLException se) {
			isError = true;
			NDBTracer.print_error("STMT.EU", watch.getElapsed(), sql, null, se.toString());
			throw se;
		} finally {
			if ( !isError )
			NDBTracer.print_sql("STMT.EU", watch.getElapsed(), sql, null, result);
		}
		return result;
	}
	/**
	 * Executes the given SQL statement 
	 * @param SQL statement
	 * @param columnNames
	 * @return the row count for INSERT, UPDATE or DELETE statements
	 * @throws SQLException
	 * @see java.sql.Statement#executeUpdate(String, String[])
	 */
	public int executeUpdate(String sql, String[] columnNames) throws SQLException {
		NStopWatch watch = new NStopWatch();
		int result = 0;
		boolean isError = false;
		try {
			result = statement.executeUpdate(sql, columnNames);
		} catch (SQLException se) {
			isError = true;
			NDBTracer.print_error("STMT.EU", watch.getElapsed(), sql, null, se.toString());
			throw se;
		} finally {
			if ( !isError )
			NDBTracer.print_sql("STMT.EU", watch.getElapsed(), sql, null, result);
		}
		return result;
	}
	/**
	 * Retrieves any auto-generated keys created as a result of executing this Statement object
	 * @return a ResultSet object
	 * @throws SQLException
	 * @see java.sql.Statement#getGeneratedKeys()
	 */
	public ResultSet getGeneratedKeys() throws SQLException {
		return statement.getGeneratedKeys();
	}
	/**
	 * Moves to this Statement object's next result
	 * @param current
	 * @return true if the next result is a ResultSet object
	 * @throws SQLException
	 * @see java.sql.Statement#getMoreResults(int)
	 */
	public boolean getMoreResults(int current) throws SQLException {
		return statement.getMoreResults(current);
	}
	/**
	 * Retrieves the result set holdability for ResultSet objects generated by this Statement object
	 * @return int
	 * @throws SQLException
	 * @see java.sql.Statement#getResultSetHoldability()
	 */
	public int getResultSetHoldability() throws SQLException {
		return statement.getResultSetHoldability();
	}
}

