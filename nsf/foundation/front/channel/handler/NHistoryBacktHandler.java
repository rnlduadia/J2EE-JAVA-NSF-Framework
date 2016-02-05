package nsf.foundation.front.channel.handler;

/**
 * @(#) NHistoryBacktHandler.java
 */

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nsf.core.exception.NException;

/**
 * <pre>
 * 이 클라스는 이전페지로 이행할 필요가 있을 경우에 리용하는  
 * Handler클라스이다.
 * </pre>
 *
 * @since 2007년 1월 5일
 * @version NSF 1.0
 *
 * @author 최정철, Nova China<br>
 */

public class NHistoryBacktHandler implements NReturnUrlHandler {
    
    /**
     * NHistoryBacktHandler 기정 구축자
     *
     */
    public NHistoryBacktHandler() {
        super();
    }
    
    /**
     * 이전 페지로 이행한다.
     * 
     * @param request
     * @param response
     * @param target
     * @throws NException
     */
    public void doNavigation(HttpServletRequest request, HttpServletResponse response, String target) throws NException {
        int depth = target == null ? 1 : Integer.parseInt(target.toString().trim());
        if (depth == 0)
            depth = 1;

        try {
            StringBuffer html = new StringBuffer();
            html.append("<head>");
            html.append("<title>Back Depth ").append(target).append("</title>");
            html.append("<META http-equiv=\"Cache-Control\" content=\"no-cache; no-store; no-save\">");
            html.append("<meta http-equiv=\"Pragma\" content=\"no-store\">");
            html.append("<script language=\"JScript\">");
            html.append("history.back(-" + depth + ");");
            html.append("</script>");
            html.append("</head>");
            html.append("<body/>");
            html.append("</html>");
            response.getOutputStream().print(html.toString());
        } catch (IOException e) {
        	throw new NException("NSF_URL_002", "Error Occured while history back",  e);
        }
    }
}

