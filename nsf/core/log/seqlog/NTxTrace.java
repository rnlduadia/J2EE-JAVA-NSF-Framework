package nsf.core.log.seqlog;
/**
 * @(#) NTxTrace.java
 */

public class NTxTrace {
    /**
     * <pre>
     * seqId를 생성하고 관리하는 역활을 수행한다.
     * </pre>
     *
     * @since 2007년 1월 5일
     * @version NSF1.0
     * 
     * @author 조광혁, NOVA CHINA
     */
	private static ThreadLocal _seq_global = new ThreadLocal();
	private static String global_seq_id_str = "";
	private static long global_seq_id_seq = 0;
	private static String _last_ip="";
	static {
    	_last_ip = "";
		try {
			global_seq_id_str = new NInitLockControl().getLock();
		} catch (Exception e) {
		}
	}
	/**
	 * SeqID를 되돌린다.
	 * @return String
	 */
	public static String getSeqID() {
		NSeq tx_map = (NSeq) _seq_global.get();
		if (tx_map == null)
			return "";
		return tx_map.seqId + " ";
	}
	
    /**
     * 전달되는 Key를 리용하여 SEQID를 생성하고 등록한다
     * @param key
     */
	public static void regist(String key) {
		NSeq txMap = new NSeq();
		if ( !(_seq_global.get() != null)) global_seq_id_seq++ ;
		if (key == null)
			key = "";
			
		long txstart = System.currentTimeMillis();
		
		txMap.seqId =
			new StringBuffer(key)
				.append(_last_ip)
				.append(global_seq_id_str)
		        .append("_")
				.append(Long.toHexString(global_seq_id_seq))
				.toString();
				
		txMap.seqStart = txstart;
		txMap.seqPoint = txstart;
		txMap.seqPoint_seq = 0;
		_seq_global.set(txMap);
	}
}

