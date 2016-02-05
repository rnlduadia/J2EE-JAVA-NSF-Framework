/**
 * @(#) StringUtil.java
 *  
 */
package nsf.support.tools.util;

import nsf.core.exception.NException;

/**
 * <pre>
 * String Handling 을 위한  Utility Class
 * </pre>
 *
 * @since 2007년 1월 5일
 * @version NSF 1.0
 * 
 * @author 강영진, Nova China 
 */
public class StringUtil {

    public StringUtil(){}


 	/**
	 * Y / N 을 예/아니오로 바꾸어 준다
	 *
	 * @param ynStr Y/N
	 * @return      에/아니오
	 * @exception 	NException
	 */

    public static String convertYn( String ynStr )
    throws NException
    {

    	try {

    	   if ( ynStr == null ) return "";
    	   if ( ynStr.trim().toUpperCase().equals( "Y" )) return "예" ;
    	   if ( ynStr.trim().toUpperCase().equals( "N" )) return "아니오" ;

    	}catch( Exception e ){
          throw new NException("[StringUtil][convertYn]"+e.getMessage() , e );
        }

    	return "" ;
    }
	/**
	 * limit 자리수 만큼 글자를 잘라준다.
	 *
	 * @param str   대상문자렬
	 * @param limit 자를 자리수
	 * @return      잘라진 문자렬
	 * @exception 	NException
	 */
	public static String shortCutString(String str, final int limit)
	throws NException
	{

      try{

      	if (str == null || limit < 4) return str;

        int len = str.length();
        int cnt=0, index=0;

        while (index < len && cnt < limit)
        {
            if (str.charAt(index++) < 256) // 1바이트 문자라면...
                cnt++;     // 길이 1 증가
            else // 2바이트 문자라면...
            cnt += 2;  // 길이 2 증가
        }

        if (index < len)
            str = str.substring(0, index);

      }catch( Exception e ){
          throw new NException("[StringUtil][shortCutString]"+e.getMessage() , e );
      }

      return str;

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
	public static String replace(String strTarget, String strSearch, String strReplace)
	throws NException
	{
	  String result = null ;
	  try {

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
	  }catch( Exception e ){
          throw new NException("[StringUtil][replace]"+e.getMessage() , e );
      }
	  return result ;
	}


	/**
	 * 대상문자렬(strTarget)의 임의의 위치(loc)에 지정문자렬(strInsert)를 추가한
	 * 문자렬을 반환한다.
	 *
	 * @param strTarget 대상문자렬
	 * @param loc 지정문자렬을 추가할 위치로서 대상문자렬의 첫문자 위치를 0으로 시작한 상대 위치.
	 *            loc가 0 보다 작은 값일 경우는 대상문자렬의 끝자리를 0으로 시작한 상대적 위치.
	 *            맨앞과 맨뒤는 문자렬 + 연산으로 수행가능함으로 제공하지 않는다.
	 * @param strInsert 추가할 문자렬
	 * @exception 	NException
	 * @return 추가완료된 문자렬
	 */
	public static String insert(String strTarget, int loc, String strInsert)
	throws NException
	{
	  String result = null;
	  try{

	  	StringBuffer strBuf = new StringBuffer();
		int lengthSize = strTarget.length();
		if(loc >= 0){
			if(lengthSize < loc){
				loc = lengthSize;
			}
			strBuf.append(strTarget.substring(0,loc));
			strBuf.append(strInsert);
			strBuf.append(strTarget.substring(loc+strInsert.length()));
		}else{
			if(lengthSize < Math.abs(loc)){
				loc = lengthSize * (-1);
			}
			strBuf.append(strTarget.substring(0,(lengthSize - 1) + loc));
			strBuf.append(strInsert);
			strBuf.append(strTarget.substring((lengthSize - 1) + loc + strInsert.length()));
		}
		result = strBuf.toString();
	  }catch( Exception e ){
          throw new NException("[StringUtil][insert]"+e.getMessage() , e );
      }
		return result;
	}


	/**
	 * 대상문자렬(strTarget)에서 지정문자렬(strDelete)을 삭제한 문자렬을 반환한다.
	 *
	 * @param strTarget 대상문자렬
	 * @param strDelete 삭제할 문자렬
	 * @exception 	NException
	 * @return 삭제완료된 문자렬
	 */
	public static String delete(String strTarget, String strDelete)
	throws NException
	{
		return replace(strTarget,strDelete,"");
	}


	/**
	 * 대상문자렬(strTarget)에서 구분문자렬(strDelim)을 기준으로 문자렬을 분리하여
	 * 각 분리된 문자렬을 배열에 할당하여 반환한다.
	 *
	 * @param strTarget 분리 대상 문자렬
	 * @param strDelim 구분시킬 문자렬로서 결과 문자렬에는 포함되지 않는다.
	 * @param bContainNull 구분되어진 문자렬중 공백문자렬의 포함여부.
	 *                     true : 포함, false : 포함하지 않음.
	 * @return 분리된 문자렬을 순서대로 배열에 격납하여 반환한다.
	 * @exception 	NException
	 */
	public static String[] split(String strTarget, String strDelim, boolean bContainNull)
	throws NException
	{
		int index = 0;
		String[] resultStrArray = null ;

		try {

		  resultStrArray = new String[search(strTarget,strDelim)+1];
		  String strCheck = new String(strTarget);
		  while(strCheck.length() != 0) {
		  	  int begin = strCheck.indexOf(strDelim);
		  	  if(begin == -1) {
				  resultStrArray[index] = strCheck;
				  break;
			  } else {
				  int end = begin + strDelim.length();
				  if(bContainNull){
					  resultStrArray[index++] = strCheck.substring(0, begin);
				  }
				  strCheck = strCheck.substring(end);
				  if(strCheck.length()==0 && bContainNull){
					  resultStrArray[index] = strCheck;
					  break;
				  }
			  }
		  }

	   }catch( Exception e ){
          throw new NException("[StringUtil][split]"+e.getMessage() , e );
       }
		return resultStrArray;
	}


	/**
	 * 대상문자렬(strTarget)에서 지정문자렬(strSearch)이 검색된 횟수를,
	 * 지정문자렬이 없으면 0 을 반환한다.
	 *
	 * @param strTarget 대상문자렬
	 * @param strSearch 검색할 문자렬
	 * @return 지정문자렬이 검색되었으면 검색된 횟수를, 검색되지 않았으면 0 을 반환한다.
	 * @exception 	NException
	 */
	public static int search(String strTarget, String strSearch)
	throws NException
	{
	  int result=0;
	  try {

		String strCheck = new String(strTarget);
		for(int i = 0; i < strTarget.length(); ){
			int loc = strCheck.indexOf(strSearch);
			if(loc == -1) {
				break;
			} else {
				result++;
				i = loc + strSearch.length();
				strCheck = strCheck.substring(i);
			}
		}

	  }catch( Exception e ){
          throw new NException("[StringUtil][search]"+e.getMessage() , e );
      }
	  return result;
	}

	public static String escapeDollarMarker(String str) throws NException{
		return StringUtil.replace(str, "$", "\\$");
	}
}