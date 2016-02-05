package nsf.core.log;

/**
 * @(#) NLogFormat.java
 */

import nsf.core.log.seqlog.NTxTrace;
import nsf.support.tools.time.NCurrentTime;

public class NLogFormat {

    /**
     * <pre>
     * 기본적으로 제공되는 Log format에 관한 Class이다. 
     * Log를 사용할 때 출력하려고 하는 문자렬 앞에 prefix가 붙고 뒤에 
     * postfix가 붙는다. 
     * 표준적으로 '[현재시간][내용]'이고 날자는 'yyyyMMdd'의 prefix이며 postfix는 없다.
     * </pre>
     * 
     * @since 2007년 1월 5일
     * @version NSF1.0
     * 
     * @author 조광혁, Nova China 
     */

    public NLogFormat() { }

	public static String getObjectInfo(Object o) {
		StringBuffer info = new StringBuffer();
		info.append('[');

		if ( o == null ) {
			info.append("null");
		} else if ( o instanceof javax.servlet.http.HttpServletRequest ) {
			javax.servlet.http.HttpServletRequest req =	(javax.servlet.http.HttpServletRequest)o;
			String user = req.getRemoteUser();
			if ( user != null ) {
				info.append(user+",");
			}
			info.append(req.getRemoteAddr());
			info.append("," + req.getHeader("User-Agent"));
			info.append("," + req.getServerPort() + req.getServletPath() );
		} else {
			Class c = o.getClass();
			String fullname = c.getName();
			String name = null;
			int index = fullname.lastIndexOf('.');
			if ( index == -1 ) {
				name = fullname;
			} else {
				name = fullname.substring(index+1);
			}
			info.append(name);
			info.append( ":" + o.hashCode() );
		}
			
		info.append( "] " );
			
		return info.toString();
	}
	
	/**
	 * Log내용의 뒤부분에 들어갈 String 값을 만들어 준다. 
	 * 현재는 아무내용도 되돌리지 않는다.
	 *
	 * @return "".
	 */  	
	public String postfix() {
		return "";
	}
    
	/**
	 * Log내용의 앞부분에 들어갈 String 값을 만들어 준다. 
	 * 현재 'hh:mm:ss[sss]'의 형식으로 되여 있다.
	 *
	 * @return 현재시간(hh:mm:ss[sss]).
	 */    
	public String prefix() {
		return NTxTrace.getSeqID() + NCurrentTime.currentTimeMillis() + ' ';
	}
	
	/**
	 * Object정보를 현재날자와 함께 얻을 경우 사용한다.
	 *
	 * @return 현재날자(hh:mm:ss[sss])와 Object 정보.
	 */  	
	public String prefix( Object o ) {
		return prefix() + getObjectInfo( o );
	}

}

