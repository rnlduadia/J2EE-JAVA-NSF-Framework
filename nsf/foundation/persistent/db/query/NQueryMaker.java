package nsf.foundation.persistent.db.query;

/**
 * @(#) NQueryMaker.java
 */

import java.util.LinkedList;
import nsf.support.collection.NData;
import nsf.core.exception.NException;

/**
 * <pre>
 * 이 클라스는 내부적으로 사용되는 클라스이다.
 * 이 클라스는 내부적으로 SQL문을 Query Parsing하여 SQL문을 효률적으로 저장하는
 * 역할을 한다.
 * </pre>
 *
 * @since 2007년 1월 5일
 * @version NSF 1.0
 *
 * @author 리향성, Nova China<br>
 */

public class NQueryMaker {
	private String query = "";
	private NData queryArgument = null;

	/**
	 * SQL문을 resolve한다.
	 * @param queryString Query의 위치
	 * @param parameterData 쿼리에서 바인딩될 파라미터
	 * @return void
	 * @throws QueryException 
	 */
	public void resolveSQL(String queryString, NData parameterData) {

        String queryStr = null;
    	int index = queryString.lastIndexOf('/');
    	String filePath = queryString.substring(0, index);
    	String attributeName = queryString.substring(index+1);
    	try {
            queryStr = NQueryFactory.getInstance().get(filePath, attributeName, parameterData);
            System.out.print(queryStr);
            if( queryStr == null ) {
                throw new NException("Does not exist Query String(fileName:" + filePath + ", attributeName:"+ attributeName +")" );
            }
            this.replaceStr(queryStr);
        } catch (NException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}


	/**
	 * SQL문중 변경이 필요한 부분을 처리한다.(Binding시)
	 * @param realQuery SQL Query
	 * @return void
	 */
	private void replaceStr(String realQuery) {
		StringBuffer queryStr = new StringBuffer(realQuery);
		queryArgument = new NData();
		LinkedList keyList = new LinkedList(); 
		queryArgument.put("key", keyList);
		int length = queryStr.length();
		int stringIndex = 0;
		while(true){
			int firstIndex = queryStr.indexOf("${", stringIndex);
			int lastIndex = queryStr.indexOf("}", firstIndex);
			if( stringIndex >= length || firstIndex == -1)
				break;
			String argumentString = queryStr.substring(firstIndex, ++lastIndex);

			LinkedList keys = (LinkedList)queryArgument.get("key");
            String key = argumentString.substring(2,argumentString.length()-1);
			keys.add(key);
			queryArgument.put("key", keys);
			queryStr.replace(firstIndex, lastIndex, "?");
			stringIndex = firstIndex;
		}
		query = queryStr.toString();
	}


	/**
	 * Parsing된 Query를 반환한다.
	 * @return String
	 */
	public String getQuery() {
		return query;
	}

	/**
	 * 파라메터를 반환한다.
	 * @return String
	 */
	public NData getQueryArgument() {
		return queryArgument;
	}
}

