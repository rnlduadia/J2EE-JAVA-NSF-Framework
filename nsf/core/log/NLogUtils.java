package nsf.core.log;

/**
 * @(#) NLogUtils.java
 */  

public class NLogUtils {

    /**
     * <pre>
     * 메쎄지의 형식을 log Format에 맞추어 되돌린다.<br><br>
     *</pre>
     * 
     * @since 2007년 1월 5일
     * @version NSF1.0
     *
     * @author 조광혁, Nova China         
     */

    /**
	 * 메쎄지의 형식을 log Format에 맞추어 되돌린다.
	 *
	 * @param className 클라스명
	 * @param methodName method명
	 * @param code            메쎄지 코드
	 * @param msg     메쎄지
	 * @return 기본 log Format의 메쎄지
	 */
	public static String toDefaultLogForm(
		String className,
		String methodName,
		String code,
		String msg) {
		return "[" + code + "]　" + className + "-" + methodName + " ▶ " + msg;
	}

	/**
	 * 메쎄지의 형식을 log Format에 맞추어 되돌린다.
	 *
	 * @param className 클라스명
	 * @param methodName method명	 
	 * @param msg     메쎄지
	 * @return 기본 log Format의 메쎄지
	 */
	public static String toDefaultLogForm(
		String className,
		String methodName,	 
		String msg) {
		return toDefaultLogForm(className, methodName, "", msg);
	}	
}

