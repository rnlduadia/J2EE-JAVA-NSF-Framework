package nsf.foundation.front.channel.handler;

/**
 * @(#) NAlertAndGoUrlHandler.java
 */

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nsf.core.config.NConfiguration;
import nsf.core.exception.NException;
import nsf.foundation.front.channel.parameter.NMessageParameter;
import nsf.foundation.front.channel.parameter.NUrlParameter;

/**
 * <pre>
 * 이 클라스는 Alert를 띄운후에 지정된 URL로 이행 할 필요가 있는 경우에 리용되는 
 * Handler클라스이다.
 * </pre>
 *
 * @since 2007년 1월 5일
 * @version NSF 1.0
 *
 * @author 최정철, Nova China<br>
 */

public class NAlertAndGoUrlHandler implements NReturnUrlHandler {
    
    /**
     * NAlertAndGoUrlHandler기정 구축자
     */
    public NAlertAndGoUrlHandler() {
        super();
    }
    
    /**
     * alert을 띄우고 지정된 URL로 이행한다
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

        NMessageParameter messageParameter = new NMessageParameter();
        String message = messageParameter.getMessageParameter(request);
        if(message == null || "null".equals(message)){
        	message = "You must set your message using NMessageParameter";
        }
        
		String path = request.getServletPath();
		int pathFirstIndex = path.lastIndexOf("/");
		String action = path.substring(pathFirstIndex+1);
		int pathLastIndex = action.lastIndexOf(".");		
		String specName = action.substring(pathLastIndex+1);
   		if("nsf".equals(specName)){
   			specName = "default";
   		}
		String encodingType = NConfiguration.getInstance().get("/configuration/nsf/navigation<" + specName + ">/encoding-type");
        NUrlParameter up = (NUrlParameter) request.getAttribute("NSF_URLParameter");

        try {
            if (up != null)
                target = target + "?" + up.toURLParameterString();

            StringBuffer html = new StringBuffer();
            html.append("<HTML><BODY>");
            html.append("<SCRIPT>");
            html.append(" alert(\"");
            html.append(message);
            html.append(" \"); ");
            html.append("location.replace(\" " + target + " \");");
            html.append("</SCRIPT>");
            html.append("</BODY></HTML>");
            response.setContentType("text/html;charset="+encodingType);
            response.getOutputStream().print(html.toString());
        }catch (IOException e) {
        	throw new NException("NSF_URL_002", "Error Occured while alert and go url",  e);
        }
    }
}

