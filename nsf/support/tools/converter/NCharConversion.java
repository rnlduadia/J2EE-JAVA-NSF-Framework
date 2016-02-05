package nsf.support.tools.converter;
/**
 * @(#) NCharConversion.java
 * 
 */
import java.io.UnsupportedEncodingException;

/**
 * <pre>
 * java에서 조선글을 사용하기 위하여 사용하는 Class
 * 영문(8859_1)과 조선글(KSC5601)은 다른 Character Set을 사용하기때문에
 * 조선글을 사용하기 위해서는 다른 Character Set을 사용하는 것이 필요하다.
 * 이 Class에서는 영문을 조선글로, 조선글을 영문으로 바꾸는 2개의 Method를 제공한다.
 * </pre>
 * 
 * @since 2007년 1월 5일
 * @version NSF 1.0
 * 
 * @author 강영진, Nova China 
 */
public final class NCharConversion {

	/**
	* Don't let anyone instantiate this class
	*/
	private NCharConversion() {}
	/**
    * 영문을 조선글로 Conversion해주는 Method.
	* (8859_1 --> KSC5601)
    * @param english 조선글로 바꾸어질 영문 String
    * @return 조선글로 바꾸어진 String
	*/
	public static synchronized String E2K( String english ) {
		String korean = null;
	
		if (english == null ) {
			return null;
		}
		
		try { 
			korean = new String(new String(english.getBytes("8859_1"), "KSC5601"));
		}
		catch( UnsupportedEncodingException e ){
			korean = new String(english);
		}
		return korean;
	}
	/**
    * 조선글을 영문으로 Conversion해주는 Method.
	* ( KSC5601 --> 8859_1 )
    * @param korean 영문으로 바꾸어질 조선글 String
    * @return 영문로 바꾸어진 String
	*/
	public static synchronized String K2E( String korean ) {
		String english = null;
		
		if (korean == null ) {
			return null;
		}
		
		english = new String(korean);
		try { 
			english = new String(new String(korean.getBytes("KSC5601"), "8859_1"));
		}
		catch( UnsupportedEncodingException e ){
			english = new String(korean);
		}
		return english;
	}
}

