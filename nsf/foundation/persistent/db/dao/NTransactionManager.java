package nsf.foundation.persistent.db.dao;

/**
 * @(#) NTransactionManager.java
 */ 

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import nsf.core.log.NLog;
import nsf.foundation.persistent.db.connection.NConnectionManager;

/**
 * <pre>
 * 이 클라스는 Dao내부에서 Transaction을 처리하기 위해서 사용된다.
 * </pre>
 * 
 * @since 2007년 1월 5일
 * @version NSF 1.0
 *
 * @author 리향성, Nova China<br>
 */

public class NTransactionManager {
	private HashMap connectionMap = new HashMap();
	
	public NTransactionManager() {
		super();
	}
	
	/** 
	 * Connection을 얻어온다.
	 * @param dbSpec nsf.xml에 정의되어 있는 DB Spec이름
	 * @return Connection
	 */
	public Connection getConnection(String dbSpec) throws Exception{
		if( connectionMap.containsKey(dbSpec) ){
			return (Connection)connectionMap.get(dbSpec);
		}else{
			Connection conn = NConnectionManager.getConnection(dbSpec);
			connectionMap.put(dbSpec, conn);
			return conn;
		}
	}
	
	/** 
	 * Tx관리되는 모든 Connection을 close한다.
	 */
	public void close() throws Exception{
		Set keySet = connectionMap.keySet();
		Iterator keyIterator = keySet.iterator();
		while(keyIterator.hasNext()){
			String connectionKey = (String)keyIterator.next();
			this.closeConnection(connectionKey);
		}
		connectionMap.clear();
	}
	
	/** 
	 * 하나의 Connection을 close한다.
	 */
	private void closeConnection(String dbSpec) throws Exception{
		Connection conn = (Connection)connectionMap.get(dbSpec);
		try {
			if (conn != null && !conn.isClosed())
				conn.close();
		} catch (SQLException se) {
			NLog.report.println(se.toString());
		}
	}
	
	/** 
	 * Commit한다.
	 */
	public void commit() throws Exception{
		Set keySet = connectionMap.keySet();
		Iterator keyIterator = keySet.iterator();
		while(keyIterator.hasNext()){
			String connectionKey = (String)keyIterator.next();
			Connection conn = (Connection)connectionMap.get(connectionKey);
			conn.commit();
		}
	}
	
	/** 
	 * Rollback한다.
	 */
	public void rollback() throws Exception{
		Set keySet = connectionMap.keySet();
		Iterator keyIterator = keySet.iterator();
		while(keyIterator.hasNext()){
			String connectionKey = (String)keyIterator.next();
			Connection conn = (Connection)connectionMap.get(connectionKey);
			conn.rollback();
		}		
	}
}

