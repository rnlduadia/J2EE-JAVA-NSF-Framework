package nsf.support.tools.file;
/**
 * @(#) NWildcardAnalyzer.java
 * 
 */
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <pre>
 * 일반적인 DOS wildcard를 Java의 정규식 Pattern으로 변경한다.
 * 
 * wildcard는 * 및 ?를 사용할 수 있다.
 * ex) *.jsp a*.java b*.?sp
 * 
 * 여러개의 wildcard를 사용할 경우 ;로 구분할 수 있다.
 * ex) *.jap;*.java;*.class
 * 
 * wildcard는 path중 일부에도 적용할 수 있다.
 * ex) /etc/sam*a/*.sh
 * 
 * 이 Class는 JDK V1.4이상에서 동작한다.
 * </pre>
 * 
 * @since 2007년 1월 5일
 * @version NSF 1.0
 * 
 * @author 강영진, Nova China 
 */

public class NWildcardAnalyzer {

	/**
	 * Don't let anyone instantiate this class
	 */
	private NWildcardAnalyzer() {
	}

	/**
	 * Java Style의 정규식 Pattern의 특수문자들을 escape 시키기 위하셔 정의한 정규식 Pattern
	 */
	private static final Pattern escapePattern =
		Pattern.compile("(\\.|\\\\|\\[|\\]|\\^|\\$|\\+|\\{|\\}|\\(|\\)|\\|)");

	/**
	 * DOS File System의 wildcard중 *를 Java Style RegExp로 변경하기 위해 사용되는 정규식 Pattern
	 */
	private static final Pattern asteriskPattern = Pattern.compile("\\*");

	/**
	 * 도스 파일시스템의 wildcard중 ?를 Java Style RegExp로 변경하기 위해 사용되는 정규식 Pattern
	 */
	private static final Pattern questionmarkPattern = Pattern.compile("\\?");

	/**
	 * 여러개의 wildcard 적용시 seperator로 사용될 ;를 정의하기 위해  사용되는 정규식 Pattern
	 */
	private static final Pattern multiplePattern = Pattern.compile("\\;");

	/**
	* 주어진 wildcard를 분석하여 Java 정규식 Pattern으로 return한다.<BR>
	* 대소문자 구분은 하지 않는다. 
	* 
	* @param wildcard 분석 대상이될 wildcard 문자렬
	* 
	* @return 분석된 Java 정규식 Pattern 
	*/
	public static Pattern compileWildcardPattern(String wildcard) {
		return compileWildcardPattern(wildcard, false);
	}

	/**
	* 주어진 wildcard를 분석하여 Java 정규식 Pattern으로 return한다.<BR>
	* 
	* @param wildcard 분석 대상이될 wildcard 문자렬
	* @param ignoreCase wildcard 분석시 대소문자 구분할지를 결정 (true:대소문자 구분)
	* 
	* @return 분석된 Java 정규식 Pattern 
	*/
	public static Pattern compileWildcardPattern(String wildcard, boolean ignoreCase) {

		Matcher escapeMatcher = escapePattern.matcher(wildcard);
		wildcard = escapeMatcher.replaceAll("\\\\$1");

		Matcher asteriskMatcher = asteriskPattern.matcher(wildcard);
		wildcard = asteriskMatcher.replaceAll("(.*)");

		Matcher questionmarkMatcher = questionmarkPattern.matcher(wildcard);
		wildcard = questionmarkMatcher.replaceAll("(.)");

		Matcher multipleMacher = multiplePattern.matcher(wildcard);
		wildcard = multipleMacher.replaceAll(")|(");

		wildcard = "(" + wildcard + ")";

		return Pattern.compile(wildcard, ignoreCase ? Pattern.CASE_INSENSITIVE : 0);
	}

}

