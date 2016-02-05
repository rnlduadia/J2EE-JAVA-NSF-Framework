package nsf.support.tools.time;
/**
 * @(#) NCurrentTime.java
 * 
 */

/**
 * <pre>
 * System.currentTimeMillis()을 리용하여 현재시간을 구한다.
 * </pre>
 *
 * @since 2007년 1월 5일
 * @version NSF 1.0
 * 
 * @author 강영진, Nova China 
 */
public class NCurrentTime {
	
	private NCurrentTime() {
	}
	
	/**
	 * System.currentTimeMillis()을 리용하여 현재시간을 계산한다. 
	 *
	 * @return 현재시간(hh:mm:ss).
	 */ 
	public static String currentTime() {
		return new StringBuffer().append(mk2(currHH()))
								 .append(':')
								 .append(mk2(currMM()))
								 .append(':')
								 .append(mk2(currSS()))
								 .toString();
	}
	/**
	 * System.currentTimeMillis()을 리용하여 현재시간을 계산한다. 
	 *
	 * @return 현재시간(hh:mm:ss[sss]).
	 */ 
	public static String currentTimeMillis() {
		return new StringBuffer().append(mk2(currHH()))
								 .append(':')
								 .append(mk2(currMM()))
								 .append(':')
								 .append(mk2(currSS()))
								 .append('[')
								 .append(mk3(currSSS()))
								 .append(']')
								 .toString();
	}

	private static long currHH() {
		long value = System.currentTimeMillis();
		return (value / (1000L * 60L * 60L) + 9) % 24L;	
	}
	private static long currMM() {
		long value = System.currentTimeMillis();
		return (value / (1000L * 60L)) % 60L;	
	}
	private static long currSS() {
		long value = System.currentTimeMillis();
		return (value / 1000L) % 60L;	
	}
	private static long currSSS() {
		long value = System.currentTimeMillis();
		return value % 1000L;	
	}	
	private static String mk2(long value) {
		if (value < 10)
			return "0" + value;
		else
			return "" + (value);
	}
	private static String mk3(long value) {
		if (value < 10)
			return "00" + value;
		else if (value < 100)
			return "0" + value;
		else
			return "" + (value);
	}
}

