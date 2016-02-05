package nsf.foundation.front.command;
/**
 * @(#) NCommandException.java
 */

import nsf.core.exception.NException;

/**
 * <pre>
 * channel-command pattern의 Servelt-command 구조에서, 
 * command에서 발생한 Excption을 command를 call한 servlet으로 throw할 경우 사용하는 Excption이다.
 * command에서 걸러야 하는 대부분의 Excption들은 가능한 command에서 처리를 하여야 하는 것이 기본적인 가이드이며,
 * 만약 project에서 공통적인 error-handling이 필요할 경우에 가능한 Servlet에 공통 Excption handling을 사용하지 말고
 * abstract command 하나 만든 다음 공통 Excption handling을 하고 이를 다시 상속받아서 개발자들에게 제공하는것이 좋다.
 * 이것은 servlet이 단순한 form-porcess / jsp result redirect 기능만을 제공하고
 * 실제 data handling을 command에서 하는 channel-command의 각 role을 잘 고민하여 결정하여야 할 것이다.
 * 다시 말해서 Excption은 command에서 처리하고 그렇지 못할 excption을 command를 call한 servlet으로 throw할 경우에
 * 사용할 수 있는 Excption이 바로 NCommandException이다.
 * </pre>
 *
 * @since 2007년 1월 5일
 * @version NSF 1.0
 *
 * @author 최정철, Nova China<br>
 */

public class NCommandException extends NException {
	/**
	 * 특정한 메시지 없이 NCommandException을 생성한다.
	 */
	public NCommandException() {
		super();
	}

	/**
	 * 특정한 메시지를 갖는 NCommandException을 생성한다.
	 * @param code 해당 메세지 코드
	 * @param msg  메세지코드에 해당하는 메세지 스트링
	 */
	public NCommandException(String code, String msg) {
		super(code, msg);
	}

	/**
	 * 특정한 메시지를 갖는 NCommandException을 생성한다.
	 * @param s     해당 메세지 코드
	 * @param arg  메세지코드에 해당하는 메세지 스트링
	 * @param rootCause   원인Exception
	 */
	public NCommandException(String s, String arg, Throwable rootCause) {
		super(s, arg, rootCause);
	}
}

