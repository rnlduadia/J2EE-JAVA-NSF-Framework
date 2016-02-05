package nsf.foundation.persistent.db.dao;

/**
 * @(#) NAbstractDao.java
 */ 

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;

import nsf.core.exception.NException;
import nsf.core.log.NLog;
import nsf.foundation.persistent.db.connection.NConnectionManager;
import nsf.support.collection.NData;
import nsf.support.collection.NMultiData;





/**
 * <pre>
 * 이 Class는 최상위 DAO 클라스이다.
 * <BR>이 클라스에서는 Dao콤포넨트의 전체적인 구조와 Mapping, DB와 관련된
 * <BR>일련의 작업을 처리한다.
 * </pre>
 *
 * @since 2007년 1월 5일
 * @version NSF 1.0
 *
 * @author 리향성, Nova China<br>
 */

public abstract class NAbstractDao {

	protected static HashMap mappers = null;
	protected String dbSpec = "default";
	protected String insertQuery = "";
	protected String updateQuery = "";
	protected String deleteQuery = "";
	
	/**
	 * 일반적인 구축자이다.
	 */
	public NAbstractDao() {
		super();
	}

	/**
	 * nsf.xml에 설정된 dbSpec에 맞는 Connection을 반환한다.
	 *
	 * @param dbSpec NSF에 설정된 dbSpec를 지정한다.
	 * @return Connecton을 반환한다
	 * @throws Exception
	 */
	protected Connection getConnection(String dbSpec) throws Exception{
	    return  NConnectionManager.getConnection(dbSpec);
	}
    
    /**
     * ?에 해당한 값을 set***() 하여 값을 대입한다.
     *
     * @param CallableStatement cs,index ,대입값 Object anObject
     * @throws NException
     */
    
    protected void setValue(CallableStatement cs, int index, Object anObject) throws NException {
        try{
            if (anObject==null){
                cs.setNull(index, Types.NULL);
            }else {
                cs.setString(index, (String) anObject);
            }
        }catch(Exception e){
            NLog.report.println("NSF_DAO_001 : Watch your SQL Statement(in SQL File) and NData");
            throw new NException("NSF_DAO_001", "Watch your SQL Statement(in SQL File) and NData", e);
        }
    }
    /**
     * ?에 해당한 값을 set***() 하여 값을 대입한다.
     *
     * @param PreparedStatement ps,index ,대입값 Object anObject
     * @throws NException
     */
	protected void setValue(PreparedStatement ps, int index, Object anObject) throws NException {
		try{
		    if (anObject==null){
		    	ps.setNull(index, Types.NULL);
		    }else {
		        ps.setString(index, (String) anObject);
		    }
		}catch(Exception e){
			NLog.report.println("NSF_DAO_001 : Watch your SQL Statement(in SQL File) and NData");
			throw new NException("NSF_DAO_001", "Watch your SQL Statement(in SQL File) and NData", e);
		}
	}
    	
    /**
     * Insert할 쿼리를 설정한다
     * @param insertQuery
     */
	public void setInsertQuery(String insertQuery){
		this.insertQuery = insertQuery;
	}
	
    /**
     * Update할 쿼리를 설정한다
     * @param updateQuery
     */
	public void setUpdateQuery(String updateQuery){
		this.updateQuery = updateQuery;
	}
	
    /**
     * Delete할 쿼리를 설정한다
     * @param deleteQuery
     */
	public void setDeleteQuery(String deleteQuery){
		this.deleteQuery = deleteQuery;
	}
	
   
	/**
	 * 이 메소드는 CREATE, UPDATE, DELETE SQL문에만 사용할 수 있다.
	 * <BR>여기서는 자동으로 Connection을 얻어오고, 그 Connection으로 SQL문을 수한다.
	 * <BR>수행이 끝나면, PaparedStatement, Connection을 close하고
	 * <BR>결과를 int형으로 Return한다.
	 * @return int CUD Query문을 수행 후 얻어진 결과를 int형태로 Return한다.
	 * @throws SQLException
	 */
	public abstract int executeUpdate() throws NException ;

    /**
     * 이것은 execute()의 상세처리 메소드이다.
     * @return NMultiData SELECT문을 수행 후 얻어진 결과를 NMultiData형태로 Return한다.
     */
    protected abstract NMultiData execute(Connection conn) throws NException;

    /**
	 * 이것은 executeQuery()의 상세처리 메소드이다.
	 * @return NMultiData SELECT문을 수행 후 얻어진 결과를 NMultiData형태로 Return한다.
	 */
	protected abstract NMultiData executeQuery(Connection conn) throws NException;

	/**
	 * 이것은 executeQueryForSingle()의 상세처리 메소드이다.
	 * @return NData SELECT문을 수행 후 얻어진 결과를 NMultiData형태로 Return한다.
	 */
	protected abstract NData executeQueryForSingle(Connection conn) throws NException;

	/**
	 * 이것은 executeUpdate()의 상세처리 메소드이다.
	 * @return int CUD Query문을 수행 후 얻어진 결과를 int형태로 Return한다.
	 */
	protected abstract int executeUpdate(Connection conn) throws NException;
}

