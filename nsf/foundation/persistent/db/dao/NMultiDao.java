package nsf.foundation.persistent.db.dao;

/**
 * @(#) NMultiDao.java
 */ 

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

import nsf.core.exception.NException;
import nsf.core.log.NLog;
import nsf.support.collection.NData;
import nsf.support.collection.NMultiData;

/**
 * <pre>
 * NMultiDao 클라스는 여러개의  SQL문이 하나의 Transaction으로 묶이는 경우에 사용하는 클라스이다.
 * 여기서 Tx는 여러개의 SQL에 대한 Tx처리뿐 아니라, 서로다른 DB에 대한 Tx까지 처리해준다.
 * Transaction이 자동으로 묶이므로 transaction과 관련된 작업을 별도로 하지 않아도 된다.
 * </pre>
 * 
 * @since 2007년 1월 5일
 * @version NSF 1.0
 *
 * @author 리향성, Nova China<br>
 */

public class NMultiDao extends NAbstractDao {
	
    //Multi SQL문을 가지고 있는 객체
	private NMultiData compoundList = new NMultiData();
	private boolean txWarningFlag = false;
	private String debugQueryString = null;
	private boolean transactionFlag = false;
	private NTransactionManager txManager = new NTransactionManager();

	/**
	 * 기정 구축자
	 */
	public NMultiDao() {
		super();
	}
	
	/**
	 * 처리할 SQL을 Add한다. 이때 Add된 것은 자동으로 Transaction이 관리된다. 
	 * 여기에서 파라메터로 넘어오는 NData에는 실제로 SQL문에서 Parsing될 Parameter가 저장되여있어야 한다.
	 * 이 메소드를 사용하여 여러개를 반복적으로 ADD한 후 한번에 executeUpdate한다.
	 * @param newQueryString 쿼리이름
	 * @param parameterData 쿼리에서 Binding될 파라메터
	 */
	public void add(String newQueryString, NData parameterData) throws NException{
		this.add( newQueryString, parameterData, "default");
	}
	
	/** 
	 * SQL문을 하나씩 Add한다. 이후에 execute()를 수행하여 한번에 Query를 수행한다.
	 * <BR>이 메소드는 서로다른 다른 쿼리문을 수행할 필요가 있을때 사용된다.
	 * @param newQueryString 쿼리이름
	 * @param parameterData PreparedStatement를 구성하기위한(?과 매칭되는) 자료를 가지고 있다.
	 * @param dbSpec nsf.xml에 정의되여 있는 DB Spec이름
	 * @return void
	 */
	public void add(String newQueryString, NData parameterData, String dbSpec) throws NException{
		debugQueryString = newQueryString;
		NSingleDao commonDao = new NSingleDao( newQueryString, parameterData, dbSpec );
		compoundList.add(dbSpec, commonDao);
	}

	/**
	 * 처리할 SQL을 입력된 MultiData만큼 Add한다. 이때 Add된 것은 자동으로 Transaction이 관리된다.
	 * 이 메소드는 하나의 SQL문에 같은 형식의 자료가 복수개(NMultiData)로 존재하여 한 번에 처리하고자 할 때 사용한다.
	 * @param newQueryString 쿼리이름
	 * @param parameterDatas 쿼리에서 Binding될 하나 이상의 파라메터
	 */
	public void add(String newQueryString, NMultiData parameterDatas) throws NException{
		this.add(newQueryString, parameterDatas, "default");
	}

	/**
	 * 처리할 SQL을 입력된 MultiData만큼 Add한다. 이때 Add된 것은 자동으로 Transaction이 관리된다.
	 * 이 메소드는 하나의 SQL문에 같은 형식의 자료가 복수개(NMultiData)로 존재하여 한 번에 처리하고자 할 때 사용한다.
	 * @param newQueryString 쿼리이름
	 * @param parameterDatas 쿼리에서 Binding될 하나 이상의 파라메터
	 * @param dbSpec nsf.xml에 정의되여 있는 DB Spec이름
	 */
	public void add(String newQueryString, NMultiData parameterDatas, String dbSpec) throws NException{
		try{
			Set keySet = parameterDatas.keySet();
			int rowSize = parameterDatas.keySize();
			Object[] keyArray = keySet.toArray(new String[keySet.size()]);
			int keySize = keyArray.length;
			for( int inx = 0 ; inx < rowSize ; inx++ ){
				NData newData = new NData();
				for (int jnx = 0 ; jnx < keySize ; jnx++) {
					newData.put(keyArray[jnx].toString(), parameterDatas.get(keyArray[jnx].toString(), inx));
				}
				this.add(newQueryString, newData, dbSpec);
			}
		}catch(Exception e){
			NLog.debug.println(this.getClass().getName() 
					         + ".add() => MultiData가 올바른 형식이 아닙니다.("
							 + e.getMessage() +")"
					         + "\nMultiData의 내용은 다음과 같다.\n" 
							 + parameterDatas.toString());
	  		throw new NException("MultiData가 올바른 형식이 아닙니다.");
		}
	}
	
	/**
	 * 처리할 SQL을 입력된 MultiData만큼 Add한다. 이때 Add된 것은 자동으로 Transaction이 관리된다.
	 * 이 메소드는 하나의 SQL문에 같은 형식의 자료가 복수개(NMultiData)로 존재하여 한 번에 처리하고자 할 때 사용한다.
	 * 이때에 NMultiData에는 자료가  특정키의 값만 복수개 이고 나머지는 하나의 값만이 있는 경우이다.
	 * 이러한 경우는 하나의 Row(NData)를 생성하는데 있어 복수개의 값(PivotKeys)을 기준으로 돌아가면서 값을 채우고
	 * 나머지 key에는 하나밖에 없는 자료를 동일하게 채우는 역할을 한다. 이와 같이 하여
	 * 이 메소드 잘 사용하면 for-loop를 사용하지 않고 편리하게 사용할 수 있다.
	 * @param newQueryString 쿼리이름
	 * @param pivotKeys 중심이 되는 Keys, 하나이상의 key가 있을 경우에는 ","로 구분하여 사용한다.
	 * @param queryArgument 쿼리에서 Binding될 하나 이상의 파라메터
	 */
	public void add(String newQueryString, String pivotKeys, NMultiData parameterDatas) throws NException{
		add(newQueryString, pivotKeys, parameterDatas, "default");
	}
	
	/**
	 * 처리할 SQL을 입력된 MultiData만큼 Add한다. 이때 Add된 것은 자동으로 Transaction이 관리된다.
	 * 이 메소드는 하나의 SQL문에 같은 형식의 자료가 복수개(NMultiData)로 존재하여 한 번에 처리하고자 할 때 사용한다.
	 * 이때에 NMultiData에는 자료가  특정키의 값만 복수개 이고 나머지는 하나의 값만이 있는 경우이다.
	 * 이러한 경우는 하나의 Row(NData)를 생성하는데 있어 복수개의 값(PivotKeys)을 기준으로 돌아가면서 값을 채우고
	 * 나머지 key에는 하나밖에 없는 자료를 동일하게 채우는 역할을 한다. 이와 같이 하여
	 * 이 메소드 잘 사용하면 for-loop를 사용하지 않고 편리하게 사용할 수 있다.
	 * @param newQueryString 쿼리이름
	 * @param pivotKeys 중심이 되는 Keys, 하나이상의 key가 있을 경우에는 ","로 구분하여 사용한다.
	 * @param queryArgument 쿼리에서 Binding될 하나 이상의 파라메터
	 * @param dbSpec nsf.xml에 정의되여 있는 DB Spec이름 
	 */
	public void add(String newQueryString, String pivotKeys , NMultiData parameterDatas, String dbSpec) throws NException{
		try{
			StringTokenizer st = new StringTokenizer(pivotKeys, ", ");
			ArrayList pivotKeysForDelim = new ArrayList();

			while( st.hasMoreTokens() ){
				pivotKeysForDelim.add( st.nextToken() );
			}
			int rowSize = parameterDatas.keySize(pivotKeysForDelim.get(0).toString());
		
			Set keySet = parameterDatas.keySet();
			Object[] keyArray = keySet.toArray(new String[keySet.size()]);
			int keySize = keyArray.length;
			for( int inx = 0 ; inx < rowSize ; inx++ ){
				NData newData = new NData();
				for (int jnx = 0 ; jnx < keySize ; jnx++) {
					if( pivotKeysForDelim.contains(keyArray[jnx]) )
						newData.put(keyArray[jnx].toString(), parameterDatas.get(keyArray[jnx].toString(), inx));
					else
						newData.put(keyArray[jnx].toString(), parameterDatas.get(keyArray[jnx].toString(), 0));
				}
				this.add(newQueryString, newData, dbSpec);
			}
		}catch(Exception e){
			NLog.debug.println(this.getClass().getName() 
					         + ".add() => MultiData가 올바른 형식이 아닙니다.("
							 + e.getMessage() +")"
					         + "\nMultiData의 내용은 다음과 같다.\n" 
							 + parameterDatas.toString());
	  		throw new NException("MultiData가 올바른 형식이 아닙니다.");
		}
	}
	
    /**
     * 
     * @param parameterDatas
     * @param dbSpec
     * @throws NException
     */
	public void addWithJobType(NMultiData parameterDatas, String dbSpec) throws NException{
		String newQueryString = "";
		try{
			Set keySet = parameterDatas.keySet();
			int rowSize = parameterDatas.keySize();
			Object[] keyArray = keySet.toArray(new String[keySet.size()]);
			int keySize = keyArray.length;
			for( int inx = 0 ; inx < rowSize ; inx++ ){
				NData newData = new NData();
				for (int jnx = 0 ; jnx < keySize ; jnx++) {
					newData.put(keyArray[jnx].toString(), parameterDatas.get(keyArray[jnx].toString(), inx));
				}
				boolean isWrongType = false;
				String jobType = parameterDatas.getString(NDaoConstants.CUD_FILTER_KEY, inx);
				if( jobType.equals(NDaoConstants.CREATE_KEY) ){
					newQueryString = this.insertQuery;
				}else if( jobType.equals(NDaoConstants.UPDATE_KEY) ){
					newQueryString = this.updateQuery;
				}else if( jobType.equals(NDaoConstants.DELETE_KEY) ){
					newQueryString = this.deleteQuery; 
				}else{
					NLog.debug.println("This executUpdate is not CUD Type");
					isWrongType = true;
				}
				if( !isWrongType ){
					this.add(newQueryString, newData, dbSpec);
				}
			}
		}catch(Exception e){
			NLog.debug.println(this.getClass().getName() 
					         + ".add() => MultiData가 올바른 형식이 아닙니다.("
							 + e.getMessage() +")"
					         + "\nMultiData의 내용은 다음과 같다.\n" 
							 + parameterDatas.toString());
	  		throw new NException("MultiData가 올바른 형식이 아닙니다.");
		}
	}
	
	/**
     * 
     * @param parameterDatas
     * @throws NException
	 */
	public void addWithJobType(NMultiData parameterDatas) throws NException{
		this.addWithJobType(parameterDatas, "default");
	}
	
    /**
     * 
     * @param parameterData
     * @param dbSpec
     * @throws NException
     */
	public void addWithJobType(NData parameterData, String dbSpec) throws NException{
		String newQueryString = "";
		boolean isWrongType = false;
		String jobType = parameterData.getString( NDaoConstants.CUD_FILTER_KEY);
		if( jobType.equals(NDaoConstants.CREATE_KEY) ){
			newQueryString = this.insertQuery;
		}else if( jobType.equals(NDaoConstants.UPDATE_KEY) ){
			newQueryString = this.updateQuery;
		}else if( jobType.equals(NDaoConstants.DELETE_KEY) ){
			newQueryString = this.deleteQuery; 
		}else{
			NLog.debug.println("This executUpdate is not CUD Type");
			isWrongType = true;
		}
		
		if( !isWrongType ){
			this.add(newQueryString, parameterData, dbSpec);
		}
	}
	
	public void addWithJobType(NData parameterData) throws NException{
		addWithJobType(parameterData, "default");
	}
	
	

	/**
	 * 이 메소드는 CREATE, UPDATE, DELETE SQL문에만 사용할 수 있다.
	 * executeUpdate를 수행하면 기존에 add된 모든 SQL문을 Tx가 묶여서 한 번에 처리된다.
	 * <BR>여기서는 자동으로 Connection을 얻어오고, 그 Connection으로 SQL문을 수행한다.
	 * <BR>수행이 끝나면, PaparedStatement, Connection을 close하고 
	 * <BR>결과를 int형으로 Return한다.
	 * @return int CUD Query문을 수행 후 얻어진 결과를 int형태로 Return한다.
	 * @throws SQLException
	 */
	public int executeUpdate() throws NException {
		if(txWarningFlag)
			NLog.debug.println("Transaction Warning : executeUpdate method already executed => [" + debugQueryString + "]");
	    int result = 0;
	    try {
	    	Set keySet = compoundList.keySet();
			Iterator keyIterator = keySet.iterator();
			while(keyIterator.hasNext()){
				String txSpec = (String)keyIterator.next();
			    Connection conn = txManager.getConnection(txSpec);
			    conn.setAutoCommit(false);
	    		result = executeUpdate(conn, txSpec);
			}
			if( transactionFlag == false ){
				txManager.commit();
			}
	    } catch (NException le) {
	    	try {
	    		if( transactionFlag == false ){
	    			txManager.rollback();
	    		}
	    	} catch (Exception e) {
	    		NLog.report.println("Exception Occured : while conneection rollback ");
		        throw new NException(le);
	    	}
	    	throw le;
	    } catch (Exception re) {
	    	try {
	    		if( transactionFlag == false ){
	    			txManager.rollback();
	    		}
	    	} catch (Exception e) {
	    		NLog.report.println("Exception Occured : while conneection rollback ");
		        throw new NException(re);
	    	}
	    	throw new NException(re.getMessage(), re);
		} finally {
			compoundList.clear();
    		if( transactionFlag == false ){
    			try{
    				txManager.close();
    			}catch(Exception e){
    				throw new NException(e);
    			}
    		}
	    }
		return result;
	}

	/**
	 *이 메소드는 executeUpdate계렬 메소드의 내부에서 사용하는 내부메소드이다.
	 * @param txSpec DB Spec
	 * @param conn DB Connection
	 * @return int SELECT문을 수행 후 얻어진 결과를 int형태로 Return한다.
	 */
	protected int executeUpdate(Connection conn, String txSpec) throws NException {
        int result = 0;
		int keySize = compoundList.keySize(txSpec);
		for(int inx = 0 ; inx < keySize ; inx++) {
			NSingleDao dao = (NSingleDao)compoundList.get(txSpec, inx);
			result += dao.executeUpdate(conn);
		}
		txWarningFlag = true;
		return result;
	}
	
	/**
	 *이 메소드는 executeUpdate계렬 메소드의 내부에서 사용하는 내부메소드이다.
	 * @param conn DB Connection
	 * @return int SELECT문을 수행 후 얻어진 결과를int형태로 Return한다.
	 */
	protected int executeUpdate(Connection conn) throws NException {
		NLog.debug.println(this.getClass().getName() + ".executeUpdate() => Cann't use executeUpdate() in NMultiDao ");
		throw new NException("NSF_ADAO_008", "Cann't use executeUpdate() in NMultiDao");
	}


	/**
	 * 이 메소드는 사용하지 말것을 권고한다.
	 * @param conn DB Connection
	 * @return NMultiData SELECT문을 수행 후 얻어진 결과를 NMultiData형태로 Return한다.
	 */
	protected NMultiData execute(Connection conn) throws NException {
		NLog.debug.println(this.getClass().getName() + ".executeQuery() => Cann't use executeQueryForSingle() in NMultiDao ");
		throw new NException("NSF_ADAO_008", "Cann't use executeQueryForSingle() in NMultiDao");
	}
    /**
     * 이 메소드는 사용하지 말것을 권고한다.
     * @param conn DB Connection
     * @return NMultiData SELECT문을 수행 후 얻어진 결과를 NMultiData형태로 Return한다.
     */
    protected NMultiData executeQuery(Connection conn) throws NException {
        NLog.debug.println(this.getClass().getName() + ".executeQuery() => Cann't use executeQueryForSingle() in NMultiDao ");
        throw new NException("NSF_ADAO_008", "Cann't use executeQueryForSingle() in NMultiDao");
    }
	
	/**
	 * 이 메소드는 사용하지 말것을 권고한다.
	 * @param conn DB Connection
	 * @return NData SELECT문을 수행 후 얻어진 결과를NData형태로 Return한다.
	 */
	protected NData executeQueryForSingle(Connection conn) throws NException {
		NLog.debug.println(this.getClass().getName() + ".executeQuery() => Cann't use executeQueryForSingle() in NMultiDao ");
		throw new NException("NSF_ADAO_009", "Cann't use executeQueryForSingle() in NMultiDao");
	}
	
	/**
	 * Tx를 임의로 묶어서 처리하고자 할 때 사용할 수 있다.
	 * 이것은 Tx의 시작을 의미한다.
	 */
	public void startTransaction() throws NException{
		if( transactionFlag != false ){
			throw new NException("NSF_ADAO_012", "Already transaction was started");
		}
		transactionFlag = true;
	}
	
	/**
	 * startTransaction()이후의 작업을 commit한다.
	 */
	public void commit() throws NException{
		try{
			if( transactionFlag == false ){
				throw new NException("Transaction was not started or ended");
			}
	    	txManager.commit();
		}catch(Exception e){
			throw new NException("Exception Occured while commit : " + e.getMessage(), e);
		}finally{
			try{
				txManager.close();
			}catch(Exception e){
				throw new NException(e);
			}
		}
	}
	
	/**
	 * startTransaction()이후의 작업을 rollback한다.
	 */
	public void rollback() throws NException{
		try{
			if( transactionFlag == false ){
				throw new NException("Transaction was not started or ended");
			}
	    	txManager.rollback();
		}catch(Exception e){
			throw new NException("Exception Occured while rollback : " + e.getMessage(), e);
		}finally{
			try{
				txManager.close();
			}catch(Exception e){
				throw new NException(e);
			}
		}
	}
}

