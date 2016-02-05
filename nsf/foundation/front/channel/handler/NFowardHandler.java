package nsf.foundation.front.channel.handler;

/**
 * @(#) NFowardHandler.java
 */

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nsf.core.exception.NException;

/**
 * <pre>
 * 이 클라스는 지정된 목적 페지로 Dispatch를 진행하는 Handler클라스이다.
 * </pre>
 *
 * @since 2007년 1월 5일
 * @version NSF 1.0
 *
 * @author 최정철, Nova China<br>
 */

public class NFowardHandler implements NReturnUrlHandler {
    
    /**
     *  NFowardHandler기정 구축자
     */
    public NFowardHandler() {
        super();
    }

    /**
     * 지정된 페지로 Dispatch한다
     * 
     * @param request
     * @param response
     * @param target
     * @throws NException
     */
    public void doNavigation(HttpServletRequest request, HttpServletResponse response, String target) throws NException {
        if (target == null || target.length() == 0)
            return;
        RequestDispatcher dispatcher = null;
        try {
            dispatcher = request.getRequestDispatcher(target);
            dispatcher.forward(request, response);
        } catch (ServletException e) {
            throw new NException("NSF_URL_001", "Error Occured while url dispatch",  e);
        } catch (IOException e) {
            throw new NException("NSF_URL_001", "Error Occured while url dispatch",  e);
        }
    }
}

