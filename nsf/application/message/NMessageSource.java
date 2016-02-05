package nsf.application.message;

import java.util.Locale;

import nsf.core.exception.NException;

/**
 * <pre>
 * Message를 얻고, 얻은 메세지를 조합하고 관리하기 위한 클라스이다.
 * </pre>
 * @since 2007/1/5
 * @version NSF 1.0
 * @author 김철남 Nova China
 */

public class NMessageSource {

	/**
	 * 메쎄지 class instance(LMessageFile/LMessageDB)
	 */
	private NMessage msgInstance = null;
	/**
	 * 파라미터를 성원변수에 설정한다.
	 * ResourceType(저장방식)에 따른 해당객체를 생성하고 파라메터전달등 초기작업 수행을 위하여
	 * initialize()함수를 실행한다.
	 *
	 * @throws NException
	 */
	public NMessageSource() throws NException {
		initialize("", null, "");
	}
	
    /**
	 * 파라미터를 성원변수에 설정한다.<br>
	 * ResourceType(저장방식)에 따른 해당객체를 생성하고 파라메터전달등 초기작업 수행을 위하여
	 * initialize()함수를 실행한다.
	 *
	 * @param locale 언어/국가(지역)의 Locale정보
	 * @throws NException
	 */
	public NMessageSource(Locale locale) throws NException {
		initialize("", locale, "");
	}
    // 추가 부분

	/**
	 * 취득한 메세지를 parsing하여 파라메터와 조합된 완전한 메세지를 생성한다.
	 *
	 * @param code 취득할 메세지에 해당하는 코드
	 * @return  조합된 완전한 메세지문자렬
	 * @throws NException
	 */
	public String getMessage(String code) throws NException {
		return msgInstance.getMessage(code);
	}
		/**
	/**
	 * 메세지의 언어전환을 위하여 해당 인스턴스의 메소드를 호출한다.
	 *
	 * @param locale 언어와 지역정보
	 */
	public void changeLocale(Locale locale) {
		msgInstance.changeLocale(locale);
	}
	// 추가
	public void changeLocale(Locale locale, String charsetName) {
		msgInstance.changeLocale(locale, charsetName);
	}
	
	/**
	 * 메세지를 생성하고 전달받은 파라메터를 type에 따라 해석하기 위해 해당 클라스에 전달한다.
	 *
	 * @param module 모듈명
	 * @param locale   언어/국가(지역)의 Locale정보
	 * @throws NException
	 */
	public void initialize(String module, Locale locale, String charsetName) throws NException {
		try {
            msgInstance = new NMessage();
			msgInstance.initialize(module, locale, charsetName);
		} catch (NException me) {
			throw me;
		} catch (Exception e) {
			throw new NException("NSF_MSG_003", "error occurred while doing message operation",  e);
		}
	}

	public void loadMessage() throws NException {
		msgInstance.loadMessage();
	}


    /**
     * ReourceType에 해당하는 객체가 캐쉬하고 있는 메쎄지를 clear한다.
     */
	public void refresh() throws NException {
		msgInstance.refresh();
	}
}

