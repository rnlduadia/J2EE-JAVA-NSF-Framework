package nsf.foundation.front.channel.handler;

/**
 * @(#) NReturnUrlHandler.java
 */

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nsf.core.exception.NException;

/**
 * <pre>
 * 이 클라스는 URL Handler를 위해 제공되는 인터페이스 클라스이다.
 * </pre>
 *
 * @since 2007년 1월 5일
 * @version NSF 1.0
 *
 * @author 최정철, Nova China<br>
 */

public interface NReturnUrlHandler extends Serializable{
    
    /**
     * 하위 클라스들에서 계승해야 하는 처리 메소드 
     * @param request
     * @param response
     * @param url
     * @throws NException
     */
    public void doNavigation(HttpServletRequest request, HttpServletResponse response, String url) throws NException;
}

