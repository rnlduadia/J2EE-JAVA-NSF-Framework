package nsf.foundation.front.channel.parameter;

/**
 * @(#) NMessageParameter.java
 */

import javax.servlet.http.HttpServletRequest;

/**
 * <pre>
 * 이 클라스는 메세지 파라메터를 설정/얻기하기 위한 내부 클라스이다.<br>
 * </pre>
 * 
 * @since 2007년 1월 5일
 * @version NSF 1.0
 * 
 * @author 최정철, Nova China<br>
 */

public class NMessageParameter {
    
    /**
     * NMessageParameter 기정 구축자
     *
     */
    public NMessageParameter() {
        super();
    }
    
    /**
     * 메세지 파라메터를 얻기위한 메소드
     * @param req
     * @return
     */
    public String getMessageParameter(HttpServletRequest req){
    	return (String)req.getAttribute("NSF_MessageParameter");
    }
    
    /**
     * 메세지 파라메터를 설정하기 위한 메소드
     * @param message
     * @param req
     */
    public void setMessageParameter(String message, HttpServletRequest req) {
    	req.setAttribute("NSF_MessageParameter", message);
    }
    
}

