package nsf.foundation.front.command;

/**
 * @(#) AbstractCmd.java
 */ 

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nsf.application.message.NMessageSource;
import nsf.core.exception.NException;
import nsf.support.collection.NData;

/**
 * <pre>
 * 실지 개발에서 리용할 command의 Abstract 클라스를 정의한다.<br>
 * </pre>
 * 
 * @since 2007년 1월 5일
 * @version NSF 1.0
 *
 * @author 최정철, Nova China<br>
 */

public abstract class AbstractCmd implements NAbstractCommand {
	
    protected NData sessionData = null;
    
	public abstract void execute(HttpServletRequest req, HttpServletResponse res)
			throws Exception;
    
    public void setAlertMessage(HttpServletRequest req, String messageCode) {
        Locale pageLocale = new Locale("ko");
        String message = "";
        try {
            message = new NMessageSource(pageLocale).getMessage(messageCode);
            req.setAttribute("NSF_ALERT_MESSAGE", message);
        } catch (NException e) {}
         
    }
}
