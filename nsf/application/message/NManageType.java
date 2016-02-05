package nsf.application.message;

import java.util.Locale;

import nsf.core.exception.NException;

/**
 * <pre>
 * 전달된 파라메터를 해석하여 변수에 설정하고 언어에 관련된 작업을 위한 메소드를 제공한다.<br><br>
 *</pre>
 * @since 2007/1/5
 * @version NSF 1.0
 * @author 김철남 Nova China
 */
public class NManageType {
	private Locale locale = null;
	private String charsetName = null;
	/**
	 * 전달된 파라미터를 이용하여 메시지 관리 방식을 초기화 한다.
	 *
	 * @param module 모듈명
	 * @param locale 언어/지역이 설정되여 있는 Locale객체
	 * @throws NException 
	 */  	
	public void initialize(String module, Locale locale, String charsetName) throws NException {
		if (!isEmpty(module) || locale == null)
		     throw new NException("NSF_MSG_015","message manage type is not valid... check your manage type and usage");
		     		
		this.locale = locale;
		this.charsetName = charsetName;
	}	
	/**
	 * 해당모듈명을 반환한다. 여기서는 동작하지 않는다.
	 *
	 * @return 해당모듈명(ex 시스템명)
	 */  		
	public String getModuleName() {
		return "";
	}
	/**
	 * 해당언어와 국가에 해당하는 Locale을 반환한다.
	 *
	 * @return Locale
	 */  		
	public Locale getLocale() {
        return locale;
	}
	/**
	 * 언어와 지역을 세팅한다.
	 * 
	 * @param locale 언어/지역을 세팅한 locale객체 
	 */  		
	public void setLocale(Locale locale) {
		this.locale = locale;
	}	
	/**
	 * 모듈을 세팅한다. 여기서는 동작하지 않는다.
	 * 
	 * @param module 모듈명
	 */  	
	public void setModule(String module) {}
	
	/**
	 * charaset encoding type을 설정한다. 
	 */
	public void setCharset(String charsetName) {
		this.charsetName = charsetName;
	}
	
	/**
	 * character encoding type을 얻는다.
	 */
	public String getCharset() {
		return charsetName;
	}	
     /**
      * 파라메터가 비여있는지 확인한다.
      * 
      * @param param 문자렬값
      * @return 파라메터가 비여있는지 여부
      */
     public boolean isEmpty(String param) {
         return param == null || param.equals("");
    }

}

