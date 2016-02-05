package nsf.foundation.persistent.db.connection;
/**
 * @(#)NDBTracer.java
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import nsf.core.log.NLog;
import nsf.core.log.seqlog.NTxTrace;

/**
 * <pre>
 * NConnection을 통해서 생성된 NStatement나 NPreparedStatement를 통하여 수행된 execute(), executeUpdate(), executeQuery()
 * 메소드가 호출되였을경우, 관련 정보에 대한 Logging을 담당하는 Util 클라스이다.
 * </pre>
 * @since 2007/01/05
 * @version NSF 1.0
 * @author Nova China 량명호<br>
 */
class NDBTracer {
	/**
	 * Default Constructor
	 */
	NDBTracer() {
		super();
	}

	/**
	 * NPreparedStatement나 NStatement에서 배치잡의 수행이 성공한경우에 작업에 대한 로그를 NLog.sqllog에 출력한다.
	 */
	static void print_sql(String cmd, long tm) {
		NLog.sqllog.println(toLogInfo(new StringBuffer().append(cmd).append(' ').append("Laptime : " + tm).append(' ').append(tm).append(' ')
		.append("Result : batch job success.").toString()));
	}

	/**
	 * NPreparedStatement나 NStatement에서 배치잡의 수행을 실패한 경우에 발생된 Error 로그를 NLog.sqllog에 출력한다.
	 */
	static void print_error(String cmd, long tm, String error) {
		NLog.sqllog.println(
			toLogInfo(
				new StringBuffer()
					.append(cmd).append(' ').append("Laptime : " + tm)
					.append(' ')
					.append(tm)
					.append(' ')
					.append("Result : batch job fail : " + error)
					.toString()));
	}

	/**
	 * NPreparedStatement나 NStatement에서 작업이 성공한경우에 작업에 대한 로그를 NLog.sqllog에 출력한다.
	 */
	static void print_sql(String cmd, String sql, ArrayList params) {

		String eSql;
		if (sql != null) {
			if (NDBTracer.isNone(params) == false)
				eSql = NDBTracer.bindParamToSql(sql, params.toArray(new Object[params.size()]));
			else
				eSql = sql;
		} else {
			eSql = "NOSQL";
		}
		NLog.sqllog.println(
			toLogInfo(new StringBuffer().append(cmd).append("\n").append(eSql).toString()));
	}

	/**
	 * NPreparedStatement나 NStatement에서 작업이 실패한 경우에 발생된 Error 로그를 NLog.sqllog에 출력한다.
	 */
	static void print_error(String cmd, String sql, ArrayList params, String error) {

		String eSql;
		if (sql != null) {
			if (NDBTracer.isNone(params) == false)
				eSql = NDBTracer.bindParamToSql(sql, params.toArray(new Object[params.size()]));
			else
				eSql = sql;
		} else {	
			eSql = "NOSQL";
		}
		StringBuffer log = new StringBuffer().append(cmd).append("\n").append(eSql).append("\n");
		log.append("Result : add batch job fail : " + error);
		NLog.sqllog.println(toLogInfo(log.toString()));
	}
	/**
	 * NPreparedStatement나 NStatement에서 작업이 성공한 경우에 작업에 대한 로그를 NLog.sqllog에 출력한다.
	 */
	static void print_sql(String cmd, long tm, String sql, ArrayList params, int result) {

		String eSql;
		if (sql != null) {
			if (isNone(params) == false)
				eSql = bindParamToSql(sql, params.toArray(new Object[params.size()]));
			else
				eSql = sql;
		} else {
			eSql = "NOSQL";
		}

		StringBuffer log = new StringBuffer();
		log.append(cmd).append(' ').append("Laptime : " + tm).append("\n").append(eSql + "\n");

		if ((eSql.trim().toLowerCase()).startsWith("insert"))
			log.append("Result : " + result + " row(s) inserted.");
		else if ((eSql.trim().toLowerCase()).startsWith("delete"))
			log.append("Result : " + result + " row(s) deleted.");
		else if ((eSql.trim().toLowerCase()).startsWith("update"))
			log.append("Result : " + result + " row(s) updated.");
		else if ((eSql.trim().toLowerCase()).startsWith("select"))
			log.append("Result : select completed.");

		NLog.sqllog.println(toLogInfo(log.toString()));
	}

	/**
	 * NPreparedStatement나 NStatement에서 작업이 실패한 경우에 발생된 Error 로그를 NLog.sqllog에 출력한다.
	 */
	static void print_error(
		String cmd,
		long tm,
		String sql,
		ArrayList params,
		String error) {

		String eSql;
		if (sql != null) {
			if (isNone(params) == false)
				eSql = bindParamToSql(sql, params.toArray(new Object[params.size()]));
			else
				eSql = sql;
		} else {
			eSql = "NOSQL";
		}

		StringBuffer log = new StringBuffer();
		log.append(cmd).append(' ').append("Laptime : " + tm).append("\n").append(eSql + "\n");

		if ((eSql.trim().toLowerCase()).startsWith("insert"))
			log.append("Result : insert fail.:" + error);
		else if ((eSql.trim().toLowerCase()).startsWith("delete"))
			log.append("Result : delete fail.:" + error);
		else if ((eSql.trim().toLowerCase()).startsWith("update"))
			log.append("Result : update fail.:" + error);
		else if ((eSql.trim().toLowerCase()).startsWith("select"))
			log.append("Result : select fail.:" + error);

		NLog.sqllog.println(toLogInfo(log.toString()));
	}

	/**
	 * ValueList가 Null이거나 size()가 0이면 리턴 true 그렇지 않으면 false
	 * @param vl
	 * @return boolean
	 */
	static boolean isNone(Object vl) {
		return (vl == null);
	}
	
	/**
	 * value가 Null이거나 size()가 0이면 리턴 true 그렇지 않으면 false
	 * @param value
	 * @return boolean
	 */
	static boolean isNone(String value) {
		return (value == null || value.length() == 0);
	}
	/**
	 * value가 Null이거나 size()가 0이면 리턴 true 그렇지 않으면 false
	 * @param value
	 * @return boolean
	 */
	static boolean isNone(Number value) {
		return (value == null || value.doubleValue() == 0);
	}
	/**
	 * value가 Null이거나 size()가 0이면 리턴 true 그렇지 않으면 false
	 * @param value
	 * @return boolean
	 */
	static boolean isNone(List value) {
		return (value == null || value.size() == 0);
	}
	/**
	 * value가 Null이거나 size()가 0이면 리턴 true 그렇지 않으면 false
	 * @param value
	 * @return boolean
	 */
	static boolean isNone(Object[] value) {
		return (value == null || value.length == 0);
	}
	/**
	 * value가 Null이거나 size()가 0이면 리턴 true 그렇지 않으면 false
	 * @param value
	 * @return boolean
	 */
	static boolean isNone(Map value) {
		return (value == null || value.size() == 0);
	}
	
	/**
	 * NPreparedStatement에서 수행된 query와 설정 된 파라메터 정보를 매핑한 query문을 리턴한다.
	 * @param sql NPreparedStatement 생성시 입력된 query문
	 * @param values LPreparedStatement에 설정 된 파라메터집합
	 * @return 파라메터를 포함한 Query문
	 */
	static String bindParamToSql(String sql, Object[] values) {
		if (sql == null || values == null) {
			return sql;
		} else {
			StringBuffer sb = new StringBuffer();
			StringTokenizer st = new StringTokenizer(sql, "?");
			if (st.hasMoreTokens())
				sb.append(st.nextToken());
			int i = 0;
			while (st.hasMoreTokens()) {
				if (i < values.length) {
					if (values[i] instanceof String || values[i] instanceof java.util.Date)
						sb.append("'" + values[i] + "'");
					else
						sb.append(values[i]);
					i++;
				} else {
					sb.append('?');
				}
				sb.append(st.nextToken());
			}
			while (i < values.length) {
				if (values[i] instanceof String || values[i] instanceof java.util.Date)
					sb.append("'" + values[i] + "'");
				else
					sb.append(values[i]);
				i++;
			}
			return sb.toString();
		}
	}
	
	/**
	 * NPreparedStatement에서 수행된 query와 설정 된 파라메터 정보를 매핑한 query문을 리턴한다.
	 * @param sql NPreparedStatement 생성시 입력된 query문
	 * @param value NPreparedStatement에 설정 된 파라메터
	 * @return 파라메터 포함한 Query문
	 */
	static String bindParamToSql(String sql, Object value) {
		if (sql == null)
			return null;
		char[] ch = sql.toCharArray();
		ArrayList pValue = new ArrayList();
		for (int i = 0; i < ch.length; i++) {
			if ('?' == ch[i])
				pValue.add(value);
		}
		return bindParamToSql(sql, pValue.toArray());
	}
	
	/**
	 * NPreparedStatement에서 설정된 query문중 '?' 의 수를 리턴한다.
	 * @param sql NPreparedStatement 생성시 입력된 query문
	 * @return 파라메터의 수
	 */
	static int bindParamCountOnSql(String sql) {
		if (sql == null)
			return 0;
		char[] ch = sql.toCharArray();
		int cnt = 0;
		for (int i = 0; i < ch.length; i++) {
			if ('?' == ch[i])
				cnt++;
		}
		return cnt;
	}

	/**
	 * DB관련된 작업의 로그를 남길 때, 가독성을 위해 각 로그라인의 첫부분에 행 별 구분을 할수 있는 태그정보를 
	 * 붙여서 리턴한다.
	 * 
	 * @param dbLog 태그정보를 포함하지 않는 DB 로그
	 * @return 태그정보를 포함한 로그형태
	 */
	static String toLogInfo(String dbLog) {
		String sequence = NTxTrace.getSeqID().trim();
		if (sequence.equals(""))
			sequence = "NO_ID_" + dbLog.hashCode();
		StringBuffer result = new StringBuffer();
		java.util.StringTokenizer st = new java.util.StringTokenizer(dbLog, "\n");

		while (st.hasMoreTokens())
			result.append("\n" + sequence + "=" + st.nextToken());

		return result.toString();
	}
}

