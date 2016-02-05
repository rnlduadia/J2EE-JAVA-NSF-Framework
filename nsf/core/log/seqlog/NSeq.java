package nsf.core.log.seqlog;

/**
 * @(#) NSeq.java
 */
public class NSeq {
    
    /**
     * <pre>
     * thread별로 개별적인 seq에 대한 logging 처리를 위해 사용하는 자료를 관리하는 클라스.
     * </pre>
     *
     * @since 2007년 1월 5일
     * @version NSF1.0
     * 
     * @author 조광혁, Nova China
     */

    String seqId = null;
	long seqStart = 0;
	long seqPoint = 0;
	long seqPoint_seq = 0;
	boolean logable = true;
}

