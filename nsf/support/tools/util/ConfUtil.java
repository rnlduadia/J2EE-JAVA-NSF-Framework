/**
 * @(#) ConfUtil.java
 *
 */
package nsf.support.tools.util;

import nsf.core.config.NConfiguration;
import nsf.core.exception.NException;

/**
 * <pre>
 * NSF Configuration File ( nsf.xml ) 에 정의된 관련 설정값을 얻어올 수 있다.
 * </pre>
 *
 * @since 2007년 1월 5일
 * @version NSF 1.0
 * 
 * @author 강영진, Nova China 
 */
public class ConfUtil {

	/**
	 * <pre>
	 *   NConfiguration 으로 부터 해당 key 에 대한 value 를 반환한다.
	 * </pre>
	 * @param configKey nsf.xml 에 설정된 Configuration Key
	 * @return nsf.xml 에 설정된 Value
	 * @throws NException 설정 파일에서 값을 가져올때 오유가 발생하는 경우
	 */
	public static String getString(String configKey, String defaultVal) throws NException {

		String configValue = null;
		try {

			NConfiguration conf = NConfiguration.getInstance();
			configValue = conf.getString(configKey, defaultVal).trim();

		} catch (NException e) {
			throw new NException(e);
		}
		return configValue;
	}

	/**
	 * <pre>
	 *   주어진 String 매개변수에 대한 Integer value 를 얻어 온다.
	 *   Value 가 존재하지 않거나 , null 일경우 defaultVal 로 설정된 값을 반환한다.
	 * </pre>
	 * @param intExpectedStr Integer Format 의 String Value
	 * @param defaultVal 기본값
	 * @return 주어진 String 매개변수의 Integer Value
	 * @throws LPageException  주어진 String 매개변수가 Interger Format 이 아닐경우
	 */
	public static int getInt(String configKey, int defaultVal) throws NException {

		int configValue = defaultVal;
		try {

			NConfiguration conf = NConfiguration.getInstance();
			configValue = conf.getInt(configKey, defaultVal);

		} catch (NException e) {
			throw new NException(e);
		}

		return configValue;
	}
}