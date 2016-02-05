package nsf.support.tools.time;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @(#) NHistorialWatch.java
 * 
 */

/**
 * JSP등에서 초간단 Debug 용으로 사용할 수 있는 StopWatch. NStopWatch와 틀린점은 history를 구성하고
 * toString() 시에 정해진 Format으로 이를 한번에 뿌려준다는 점이다
 * 
 * @since 2007년 1월 5일
 * @version NSF 1.0
 * 
 * @author 강영진, Nova China 
 */
public class NHistorialWatch {
	private final long start = System.currentTimeMillis();

	private final Map history = new LinkedHashMap();

	private final static DecimalFormat timeFormat = new DecimalFormat(
			"###,##0.000 sec");

	private final static DecimalFormat indexFormat = new DecimalFormat("[000]");

	/**
	 * Default Constructor
	 */
	public NHistorialWatch() {
		tick("watch start");
	}

	public void tick() {
		history.put("", new Long(System.currentTimeMillis()));
	}

	public void tick(String desc) {
		history.put(desc, new Long(System.currentTimeMillis()));
	}

	private long findMaxElapsed() {
		long max = 0;
		Iterator i = history.entrySet().iterator();
		while (i.hasNext()) {
			Map.Entry entry = (Map.Entry) i.next();
			long time = ((Long) entry.getValue()).longValue();
			if (time > max)
				max = time;
		}
		return max;
	}

	private String formatData(int index, String desc, long lap, long accumul,
			boolean max) {
		StringBuffer sb = new StringBuffer();
		sb.append(" *");
		if (max) sb.append("*");
		else sb.append(" ");
		sb.append(indexFormat.format(index)).append(" ");
		sb.append("section: ").append(timeFormat.format(lap/1000.0)).append(", ");

		sb.append("accumulation: ").append(timeFormat.format(accumul/1000.0));
		if (desc.length() > 0)
			sb.append(" [").append(desc).append("]");
		return sb.toString();
	}

	private String formatData(long accumul) {
		StringBuffer sb = new StringBuffer();
		sb.append(" * total: ").append(timeFormat.format(accumul/1000.0));
		return sb.toString();
	}

	/**
	 * 현재의 Lap정보를 Console에 Print한다.
	 * 
	 * @param N/A
	 * @return String Format정보
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		long current = System.currentTimeMillis();
		if (!history.isEmpty()) {
			long max = findMaxElapsed();
			long before = 0;
			long accumul = 0;
			int index = 0;
			Iterator i = history.entrySet().iterator();
			while (i.hasNext()) {
				Map.Entry entry = (Map.Entry) i.next();
				String key = (String) entry.getKey();
				long time = ((Long) entry.getValue()).longValue();
				long lap = before == 0 ? 0 : time - before;
				accumul += lap;
				sb.append(formatData(index++, key, lap, accumul, max == time));
				sb.append("\n");
				before = time;
			}
		}
		sb.append(formatData(current - start));
		return sb.toString();
	}
}

