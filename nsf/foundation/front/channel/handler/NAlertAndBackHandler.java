package nsf.foundation.front.channel.handler;

/**
 * @(#) NAlertAndBackHandler.java
 */

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nsf.core.config.NConfiguration;
import nsf.core.exception.NException;
import nsf.foundation.front.channel.parameter.NMessageParameter;

/**
 * <pre>
 * 이 클라스는 Alert를 띄운후에 원래 페지로 돌아가야 할 필요가 있는 경우에 리용되는 
 * Handler클라스이다.
 * </pre>
 *
 * @since 2007년 1월 5일
 * @version NSF 1.0
 *
 * @author 최정철, Nova China<br>
 */
public class NAlertAndBackHandler implements NReturnUrlHandler {
    
    /**
     * NAlertAndBackHandler 기정 구축자
     */
    public NAlertAndBackHandler() {
        super();
    }
    
    /**
     * alert을 띄우고 원래 페지로 되돌린다
     * 
     * @param request
     * @param response
     * @param target
     * @throws NException
     */
    public void doNavigation(HttpServletRequest request, HttpServletResponse response, String target) throws NException {
    	int depth;
    	try{
    		depth = target == null ? 1 : Integer.parseInt(target.toString().trim());
    	}catch(NumberFormatException ne){
    		throw new NException("NSF_URL_003", "Error Occured while alert and back : value of return-url must be number format",  ne);
    	}
        if (depth == 0)
            depth = 1;

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
		
        try {
            StringBuffer html = new StringBuffer();
            html.append("<HTML><HEAD>");
            html.append("<SCRIPT>");
            html.append(" alert(\"");
            html.append(message);
            html.append(" \"); ");
            html.append("history.back(-" + depth + ");");
            html.append("</SCRIPT></HEAD>");
            html.append("<BODY></BODY></HTML>");
            response.setContentType("text/html;charset="+encodingType);
            response.getOutputStream().print(html.toString());
        }catch (IOException e) {
        	throw new NException("NSF_URL_002", "Error Occured while alert and back",  e);
        }
    }
}

