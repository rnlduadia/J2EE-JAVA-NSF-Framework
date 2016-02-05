package nsf.support.tools.time;


/**
 * @(#) NStopWatch.java
 * 
 */

/**
 * 간단한 Performance Test용으로 Elapsed Time, Total Time, Average Time을 구할수 있는 
 * 기능이 구현되여있다.
 *
 * @since 2007년 1월 5일
 * @version NSF 1.0
 * 
 * @author 강영진, Nova China 
 */
public class NStopWatch {
	int count = 0;
	long start = 0;
	long current = 0;
	long max = 0;
	long min = 0;
	
	/**
	 * Default Constructor
	 */
	public NStopWatch() {
		reset();
	}

	/**
	 * 마지막 Lap Time에서 현재 Lap까지의 시간을 구한다.
	 * @param N/A
	 * @return long elapsed 시간
	 */
	public long getElapsed() {

		count++;
		long now = System.currentTimeMillis();
		long elapsed = (now - current);
		current = now;

		if (elapsed > max) {
				max = elapsed;
		}
		if (elapsed < min) {
			min = elapsed;
		}

		return elapsed;
	}

	/**
	 * LapTime Check 회수를 반환한다.
	 * @param N/A
	 * @return int 전체 Check Count 
	 */
	public int getCount() {
		return count;
	}

	/**
	 * Check된 LapTime 중 최단시간을 반환한다.
	 * @param N/A
	 * @return long 최소시간
	 */
	public long getMinTime() {
		return min;
	}

	/**
	 * Chcek된 LapTime 중 최장시간을 반환한다.
	 * @param N/A
	 * @return long 최대시간 
	 */
	public long getMaxTime() {
		return max;
	}

	/**
	 * 평균 Lap Time을 반환한다.
	 * @param N/A
	 * @return long 평균시간 
	 */
	public long getAvgTime() {
		if (count > 0) {
			return Math.round((double) (current - start) / (double) count);
		} else {
			return 0;
		}
	}

	/**
	 * Start시점에서 최종 LapTime까지의 총 시간을 반환한다.
	 * @param N/A
	 * @return long 전체시간
	 */
	public long getTotalElapsed() {
		current = System.currentTimeMillis();
		return (current - start);
	}

	/**
	 * StopWatch의 초기화
	 * @param N/A
	 * @return N/A 
	 */
	public void reset() {
		start = System.currentTimeMillis();
		current = start;
		count = 0;
		max = 0;
		min = 0;
	}

	/**
	 * 현재의 Lap정보를 Console에 Print한다.
	 * @param N/A
	 * @return String Lap정보 
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();

		float taskCount = getCount();
		float taskTime = getTotalElapsed() / 1000F;
		float taskMin = getMinTime() / 1000F;
		float taskMax = getMaxTime() / 1000F;
		float taskAvg = getAvgTime() / 1000F;
		buffer.append(
			"\nCount : "
				+ taskCount
				+ "\nTotal : "
				+ taskTime
				+ "\nMax : "
				+ taskMax
				+ "\nMin : "
				+ taskMin
				+ "\nAvg : "
				+ taskAvg
				+ "\n");

		return buffer.toString();
	}
}

