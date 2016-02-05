package nsf.foundation.front.channel.handler;

/**
 * @(#) NNullHandler.java
 */

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nsf.core.exception.NException;

/**
 * <pre>
 * 이 클라스는 어떤 특정의 페지로 되돌리거나 이행할 필요가 없을때에 리용하는
 * Handler 클라스이다. 
 * </pre>
 *
 * @since 2007년 1월 5일
 * @version NSF 1.0
 *
 * @author 최정철, Nova China<br>
 */

public class NNullHandler implements NReturnUrlHandler {
    
    /**
     * NNullHandler 기정 구축자
     *
     */
    public NNullHandler() {
        super();
    }
    
    /**
     * 아무런 처리도 진행하지 않는다
     * 
     * @param request
     * @param response
     * @param target
     * @throws NException
     */
    public void doNavigation(HttpServletRequest request, HttpServletResponse response, String target) throws NException {
        //NullHandler do nothing.
    }
}

