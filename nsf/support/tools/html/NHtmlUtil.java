package nsf.support.tools.html;
/**
 * @(#) NHtmlUtil.java
 *
 */
import java.text.DecimalFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nsf.core.log.NLog;
/**
 * Html Tag를 다루는데 필요한 Utility Function을 제공한다.
 *
 * @since 2007년 1월 5일
 * @version NSF 1.0
 * 
 * @author 강영진, Nova China 
 */

public final class NHtmlUtil {
	/**
	 * Don't let anyone instantiate this class
	 */
	private NHtmlUtil() {
	}
	/**
	* 문자렬중 &lt;br&gt;을 new linew(\n)으로 Conversion해주는 Method.
	* @param s &lt;br&gt;이 들어간 문자렬
	* @return \n으로 바꾸어진 문자렬
	*/

	public static synchronized String Br2N(String s) {
		return replaceString("<BR>", "\n", s);
	}
	/**
	 * Cache 방지등에 필요한 Dummy String을 반환한다. 이때 Calendar객체를 내부적으로 사용한다.
	 * @return 구해진 Dummy String
	 */
	public static synchronized String getDummyString() {

		return java.util.Calendar.getInstance().toString();
	}
	/**
	 * Cache 방지등에 필요한 Dummy String을 length길이 만큼 반환한다. 이때 Calendar객체를 내부적으로 사용한다.
	 * @param length Dummy String을 자를 length
	 * @return 구해진 Dummy String
	 */
	public static synchronized String getDummyString(int length) {
		String dummy = java.util.Calendar.getInstance().toString();

		return (dummy.length() <= length) ? dummy : dummy.substring(0, length);

	}
	/**
	 * Random한 Color를 구한다. 구해진 Color는 #RRGGBB의 형태를 취한다.
	 * @return 구해진 Random Color String
	 */
	public static synchronized String getRandomColor() {
		String rCo = Integer.toHexString((int) (java.lang.Math.random() * 256));
		String gCo = Integer.toHexString((int) (java.lang.Math.random() * 256));
		String bCo = Integer.toHexString((int) (java.lang.Math.random() * 256));

		return "#" + rCo + gCo + bCo;
	}
	/**
	* 문자렬중 new linew(\n)을  &lt;br&gt;으로 Conversion해주는 Method.
	* @param s \n이 들어간 java String
	* @return &lt;br&gt;로 바꾸어진 html String
	*/
	public static synchronized String N2Br(String s) {
		return replaceString("\n", "<BR>", s);
	}
	/**
	* 문자렬중에 특정 문자렬을 다른 문자렬로 치환한다.
	* @param find 찾을 부분 문자렬
	* @param to 변경될 부분 문자렬
	* @param s 원래의 문자렬 (Source String)
	* @return 변경된 문자렬
	*/

	public static synchronized String replaceString(String find, String to, String s) {

		StringBuffer content = new StringBuffer();

		while (s.length() > 0) {
			int position = s.indexOf(find);

			if (position == -1) {
				content.append(s);
				break;
			}

			if (position != 0)
				content.append(s.substring(0, position));

			content.append(to);

			if (s.length() == position + 1)
				break;
			s = s.substring(position + 1);
		}

		return content.toString();
	}

	/**
	 * content-type이 "multipart/form-data"인지를 검사한다.
	 * <BR> request.getContentType() method를 사용하며 이때 구해지는 문자렬을 적절한 크기로 잘라서 비교하는 logic을 사용한다.
	 * <BR> (참고) html의 form에서 특별하게 enctype을 지정하지 않는경우 content-Type은 application/x-www-form-urlencoded이다.
	 * @param  req servlet에서 전달받은 HttpServletRequest. 실제의 content type을 구하기 위해 사용한다.
	 * @return boolean "multipart/form-data"인경우 true, 그렇지 않을경우 false.
	*/
	public static boolean isMultipart(HttpServletRequest req) {
		String contentType = null;
		String multipartContentType = "multipart/form-data";
		contentType = req.getContentType();

		return (
			contentType != null
				&& contentType.length() > 19
				&& multipartContentType.equals(contentType.substring(0, 19)))
			? true
			: false;
	}

	/**
	 * Client가 Microsoft Internet Explorer5.0이상 Browser에서
	 * 요청하였는지 Check한다.
	 * @param req HttpServletRequest
	 * @return boolean
	 */
	public static boolean isOverIE50(HttpServletRequest req) {
		String user_agent = req.getHeader("user-agent");

		if (user_agent == null) {
			return false;
		}

		int index = user_agent.indexOf("MSIE");
		if (index == -1) {
			return false;
		}

		int version = 0;
		try {
			version = Integer.parseInt(user_agent.substring(index + 5, index + 5 + 1));
		} catch (Exception e) {
		}

		if (version < 5) {
			return false;
		}

		return true;
	}

	/**
	 * 입력된 Parameter가 null이라면 ""을 return한다. 
	 * @param o Object
	 * @return String
	 */
	public static String nvl(Object o){
		return NHtmlUtil.nvl((String)o);
	}
	
	/**
	 * 입력된 Parameter가 null이라면 ""을 return한다. 
	 * @param str String
	 * @return String
	 */
	public static String nvl(String str){
		return NHtmlUtil.nvl(str, "");
	}

	/**
	 * 입력된 Parameter가 null이라면 defaultValue를  return한다. 
	 * @param originalStr String
	 * @param defaultValue String
	 * @return String
	 */
	public static String nvl(String originalStr, String defaultValue){
    	if( originalStr == null || originalStr.length() < 1 )
    		return defaultValue;
		return originalStr;
	}

	/**
	 * str을 format에 맞게 masking하는 기능을 한다.
	 * format의 형식은 #으로 표시할 수 있다.
	 * ex) ("1234567890", "###,###,####") => 123,456,7890
	 * @param str String
	 * @param format String
	 * @return String
	 */
	public static String mask(String str, String format){
        int j = 0;
        if (str == null || str.length() ==0) return "";
        if (format == null || format.length() ==0) return str;
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < format.length(); i++) {
            if (format.charAt(i) == '#') {
                if (j >= str.length()) return sb.toString();
                sb.append(str.charAt(j++));
            } else {
                sb.append(format.charAt(i));
            }
        }
        return sb.toString();
	}

	/**
	 * str을 decimal format에 맞게 변환하여 return하는 기능을 한다.
	 * 여기서 format는 DecimalFormat의 형식과 일치한다.
	 * @param str String
	 * @param format String
	 * @return String
	 */
	public static String decimal(String str, String format){
        if (str == null || str.length() ==0) return "";
        if (format == null || format.length() ==0) return str;
        DecimalFormat df = new DecimalFormat(format);
        try {
            return df.format(new Double(str).doubleValue());
        } catch (Exception e) {
            NLog.debug.println(e.getMessage());
        }
        return str;
	}
	
	/**
	 * cache를 제거하는 기능을 한다.
	 * @param response HttpServletResponse
	 */
	public static void blockHttpCache(HttpServletResponse response) {
		  response.setDateHeader("Expires",0);
		  response.setHeader("Pragma","no-cache");
		  response.setHeader("Cache-Control","no-cache");
	}

	/**
	 * 전화번호를 옳바른 format에 맞게 return한다.
	 * @param phoneStr String 
	 */
	public static String maskPhone(String phoneStr)throws RuntimeException{
		if (phoneStr == null) return "";
		int strSize = phoneStr.length();

		if( strSize <= 8){
			if( strSize == 7)
				return NHtmlUtil.mask(phoneStr, "###-####");
			else
				return NHtmlUtil.mask(phoneStr, "####-####");
		}

		if( phoneStr.startsWith("02")){
			if( strSize == 9)
				return NHtmlUtil.mask(phoneStr, "##-###-####");
			else
				return NHtmlUtil.mask(phoneStr, "##-####-####");
		}

		String localNumber = phoneStr.substring(3);
		if( "0130".equals(localNumber) || "0502".equals(localNumber) || "0505".equals(localNumber) ){
			if( strSize == 11 )
				return NHtmlUtil.mask(phoneStr, "####-###-####");
			else
				return NHtmlUtil.mask(phoneStr, "####-####-####");
		}

		if( strSize == 10 ){
			return NHtmlUtil.mask(phoneStr, "###-###-####");
		}

		if( strSize == 11 ){
			return NHtmlUtil.mask(phoneStr, "###-####-####");
		}
		NLog.debug.println("This Phone Number is wrong format");
		return phoneStr;
	}

	/**
	 * Html과 관련된 특정 문자를 escape한다.
	 * 례) "&"=>"&amp;", "<"=>"&lt;", ">"=>"&gt;", """=>"&quot;", "\'"=>"&#039;", "&"=>"&amp;" 
	 * @param str String 
	 */
	public static String escape(String str) {
		if ( str == null ){
			return null;
		}

		StringBuffer escapedStr = new StringBuffer();
		char[] ch = str.toCharArray();
		int charSize = ch.length;
		for ( int i=0; i < charSize; i++) {
			if      ( ch[i] == '&' )
				escapedStr.append("&amp;");
			else if ( ch[i] == '<' )
				escapedStr.append("&lt;");
			else if ( ch[i] == '>' )
				escapedStr.append("&gt;");
			else if ( ch[i] == '"' )
				escapedStr.append("&quot;");
			else if ( ch[i] == '\'')
				escapedStr.append("&#039;");
			else escapedStr.append(ch[i]);
		}
		return escapedStr.toString();
	}

	/**
	 * escape와 반대의 기능을 한다.
	 * 례) "&"<="&amp;", "<"<="&lt;", ">"<="&gt;", """<="&quot;", "\'"<="&#039;", "&"<="&amp;" 
	 * @param str String 
	 */
	public static String unEscape(String str) {
		if ( str == null ){
			return null;
		}

		str = str.replaceAll("&amp;", "&");
		str = str.replaceAll("&&lt;", "<");
		str = str.replaceAll("&gt;", ">");
		str = str.replaceAll("&quot;", "\"");
		str = str.replaceAll("&039;", "'");
		return str;
	}
}

