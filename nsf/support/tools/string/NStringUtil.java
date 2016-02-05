package nsf.support.tools.string;

import nsf.core.exception.NException;

/**
 * @(#) NStringUtil.java
 * 
 */


/**
 * <pre>
 * 이 Class는 String과 관련된 여러가지 Utility를 제공한다.
 * </pre>
 * 
 * @since 2007년 1월 5일
 * @version NSF 1.0
 * 
 * @author 강영진, Nova China 
 */
public class NStringUtil {
	/** 
	 * 파라메터로 넘어온 String을 repeateCount만큼 반복하여 생성한 후 반환한다.
	 * @param str String
	 * @param repeateCount int
	 * @return String
	 */
	public static String makeRepeatString(String str, int repeateCount){
		StringBuffer resultStr = new StringBuffer();
		for( int inx = 0 ; inx < repeateCount ; inx++ ){
			resultStr.append(str);
		}
		return resultStr.toString();
	}
	
	/**
	 * 대상문자렬(strTarget)에서 특정문자렬(strSearch)을 찾아 지정문자렬(strReplace)로
	 * 변경한 문자렬을 반환한다.
	 *
	 * @param strTarget 대상문자렬
	 * @param strSearch 변경대상의 특정문자렬
	 * @param strReplace 변경 시키는 지정문자렬
	 * @exception 	NException
	 * @return 변경완료된 문자렬
	 */
	public static String replace(String strTarget, String strSearch, String strReplace)	{
		String result = null ;
		String strCheck = new String(strTarget);
		StringBuffer strBuf = new StringBuffer();
		while(strCheck.length() != 0) {
			int begin = strCheck.indexOf(strSearch);
			if(begin == -1) {
				strBuf.append(strCheck);
				break;
			} else {
				int end = begin + strSearch.length();
				strBuf.append(strCheck.substring(0, begin));
				strBuf.append(strReplace);
				strCheck = strCheck.substring(end);
			}
		}
		result = strBuf.toString();
		return result ;
	}
}

