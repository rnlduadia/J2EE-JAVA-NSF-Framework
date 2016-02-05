package nsf.application.page;

import java.util.regex.Pattern;

/**
 * <pre>
 *  NPage Component 에서 사용하는 상수 클라스
 * </pre>
  * @since 2007/1/5
 * @version NSF 1.0
 * @author 김철남 Nova China
 */

public class NPageConstants {
	
	/** "MSSQL_KEY_FIELD" */
	public static String MSSQL_KEY_FIELD = "MSSQL_KEY_FIELD";

	/** "PAGING_ROW_START" */
	public static String PAGING_ROW_START = "PAGING_ROW_START";
	
	/** "PAGING_ROW_END" */
	public static String PAGING_ROW_END = "PAGING_ROW_END";
	 
	/** "PAGING_ROW_SIZE" */
	public static String PAGING_ROW_SIZE = "PAGING_ROW_SIZE";
	
	/** "rows" */
	public static String ROWS = "rows";
	/** "targetRow" */
	public static String TARGET_ROW = "targetRow";
	/** "nsfOrderBy" */
	public static String NSF_ORDER_BY = "nsfOrderBy";
	
	/** "PAGE_INDEX" */
	public static String PAGE_INDEX = "PAGE_INDEX";

	/** "PAGE_RESULT" */
	public static String PAGE_RESULT = "PAGE_RESULT";

	/** "NUMBER_OF_ROWS_OF_PAGE" */
	public static String NUMBER_OF_ROWS_OF_PAGE = "NUMBER_OF_ROWS_OF_PAGE";

	/** "NUMBER_OF_PAGES_OF_INDEX" */
	public static String NUMBER_OF_PAGES_OF_INDEX = "NUMBER_OF_PAGES_OF_INDEX";

	/** "REQUEST_DATA_SINGLE_MODE" */
	public static String REQUEST_DATA_SINGLE_MODE = "REQUEST_DATA_SINGLE_MODE";

	// SQL Pattern 관련 정의 값 
	private static String MORE_THAN_ONCE = "+";
	private static String ZERO_OR_MORE = "*";
	private static String BLANK = "\\s" + ZERO_OR_MORE;
	private static String LPAREN = "\\(";
	private static String RPAREN = "\\)";
	private static String COMMA = "\\,";
	private static String WORD = "\\w";
	private static String Q_MARK_ONLY = "[\\?]";
	private static String Q_MARK =
		BLANK
			+ WORD
			+ ZERO_OR_MORE
			+ LPAREN
			+ ZERO_OR_MORE
			+ BLANK
			+ Q_MARK_ONLY
			+ BLANK
			+ RPAREN
			+ ZERO_OR_MORE;
	private static String VAR =
		"[\\.]|" + BLANK + "(" + WORD + MORE_THAN_ONCE + ")" + BLANK;
	private static String UNARY_OPERATOR = "(=|like|<|>|<=|>=|<>|!=)" + Q_MARK;
	private static String BETWEEN_PATTERN =
		"(between)" + Q_MARK + "(and)" + Q_MARK;
	private static String IN_PATTERN =
		"(in)"
			+ BLANK
			+ LPAREN
			+ "("
			+ Q_MARK
			+ BLANK
			+ COMMA
			+ ZERO_OR_MORE
			+ BLANK
			+ ")"
			+ MORE_THAN_ONCE
			+ RPAREN;

	/** "Question Mark Count Pattern" */
	public static Pattern QUESTION_MARK_PATTERN = Pattern.compile(Q_MARK_ONLY);

	/** "SQL Statement Parsing Pattern" */
	public static Pattern SQL_STMT_PATTERN =
		Pattern.compile(
			VAR
				+ "("
				+ UNARY_OPERATOR
				+ "|"
				+ BETWEEN_PATTERN
				+ "|"
				+ IN_PATTERN
				+ ")",
			Pattern.CASE_INSENSITIVE);

	/** "SQL order by Statement Parsing Pattern" */
	public static Pattern SQL_ORDER_BY_PATTERN =
		Pattern.compile(
			BLANK + "order" + BLANK + MORE_THAN_ONCE + "by" + BLANK,
			Pattern.CASE_INSENSITIVE);

	/** "SQL String literal SKIP Pattern" */
	public static Pattern SQL_STRING_LITERAL_SKIP_PATTERN =
		Pattern.compile(BLANK + "\\'.*\\'", Pattern.CASE_INSENSITIVE);

	/** "SQL_COL_NAME_INDEX" */
	public static int SQL_COL_NAME_INDEX = 1;
	/** "SQL_Q_MARK_GROUP_INDEX" */
	public static int SQL_Q_MARK_GROUP_INDEX = 2;
	/** "SQL_UNARY_OPERATOR_INDEX" */
	public static int SQL_UNARY_OPERATOR_INDEX = 3;
	/** "SQL_BETWEEN_OPERATOR_INDEX" */
	public static int SQL_BETWEEN_OPERATOR_INDEX = 4;
	/** "SQL_IN_OPERATOR_INDEX" */
	public static int SQL_IN_OPERATOR_INDEX = 6;

}

