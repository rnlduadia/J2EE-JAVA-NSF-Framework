package nsf.foundation.front.channel.handler;

/**
 * @(#) NRedirectHandler.java
 */

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nsf.core.exception.NException;
import nsf.foundation.front.channel.parameter.NUrlParameter;

/**
 * <pre>
 * 이 클라스는 지정된 페지로 Redirect하는 Handler 클라스이다.
 * </pre>
 *
 * @since 2007년 1월 5일
 * @version NSF 1.0
 *
 * @author 최정철, Nova China<br>
 */

public class NRedirectHandler implements NReturnUrlHandler {
    
    /**
     * NRedirectHandler기정 구축자
     */
	public NRedirectHandler() {
        super();
    }
	
    /**
     * 지정된 페지로 Redirect한다
     * 
     * @param request
     * @param response
     * @param target
     * @throws NException
     */
    public void doNavigation(HttpServletRequest request, HttpServletResponse response, String target) throws NException {
        if (target == null || target.length() == 0)
        	throw new NException("NSF_URL_003", "Error Occured while redirect : target url is blank");

        if( target.startsWith("/"))
        	target = target + request.getContextPath();

        NUrlParameter up = (NUrlParameter) request.getAttribute("NSF_URLParameter");

        try {
            if (up != null)
                target = target + "?" + up.toURLParameterString();
            StringBuffer html = new StringBuffer();
            html.append("<head>");
            html.append("<title>Redirect to ").append(target).append("</title>");
            html.append("<META http-equiv=\"Cache-Control\" content=\"no-cache; no-store; no-save\">");
            html.append("<meta http-equiv=\"Pragma\" content=\"no-store\">");
            html.append("<script language=\"JScript\">");
            html.append("location.replace(\" " + target + " \");");
            html.append("</script>");
            html.append("</head>");
            html.append("<body/>");
            html.append("</html>");
            response.getOutputStream().print(html.toString());
        } catch (IOException e) {
        	throw new NException("NSF_URL_002", "Error Occured while redirect",  e);
        }
    }
}

