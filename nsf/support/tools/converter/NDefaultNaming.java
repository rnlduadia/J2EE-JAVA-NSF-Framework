package nsf.support.tools.converter;
/**
 * @(#) NDefaultNaming.java
 * 
 */
import java.util.StringTokenizer;
/**
 * <pre>
 * Database의 Column명을 기준으로 Project 기반의 naming rule을 적용하기 위한 Class.
 * 본Class에서 취득한 attribute명과 setter method의 이름을 리용하여 resultset의 Data를 
 * entity(Value Object)형 Class에 저장한다.
 * 
 * 이 NafjNaming Class는 JDK V1.3이상에서 동작한다.
 * </pre>
 * 
 * @since 2007년 1월 5일
 * @version NSF 1.0
 * 
 * @author 강영진, Nova China 
 */
public class NDefaultNaming {
	/**
	 * DBColumn명 혹은 변경하려고 하는 base명을 리용하여 해당하는 attribute명을 return한다.
	 *
	 * @param   columnName Database의 Column명 혹은 변환하려고 하는 base명
	 * @return    변환된 attribute명 
	 */	
	public static String getAttributeName(String columnName ){ 
		String result = "";
		StringTokenizer dbColumnName = new StringTokenizer(columnName, "_");
		int cnt = dbColumnName.countTokens();

		for (int i = 0; i < cnt; i++) 
			if (i == 0) {
				result += dbColumnName.nextToken();
			} else {
				String str = dbColumnName.nextToken();
    			result += str.substring(0, 1).toUpperCase() + 
    			              str.substring(1, str.length());      	
			}	
		return result;
	}
	/**
	 * DBColumn명 혹은 변경하려고 하는 base명을 리용하여 해당하는 set method의 명을 return한다.
	 *
	 * @param   columnName Database의 Column명 혹은 변환하려고 하는 base명
	 * @return  Column명을 기준으로 해당 Column의 set method명
	 */
	public static String getSetMethodName(String columnName) {
		String str = "";
		str = getAttributeName(columnName);
        return  "set" + str.substring(0, 1).toUpperCase() + str.substring(1, str.length());  
	}
	
	/**
	 * DBColumn명 혹은 변경하려고 하는 base명을 리용하여 해당하는 get method의 명을 return한다.
	 *
	 * @param   columnName Database의 Column명 혹은 변환하려고 하는 base명
	 * @return  Column명을 기준으로 해당 Column의 get method명
	 */
	public static String getGetMethodName(String columnName) {
		String str = "";
		str = getAttributeName(columnName);
        return  "get" + str.substring(0, 1).toUpperCase() + str.substring(1, str.length());  
	}
	
}

