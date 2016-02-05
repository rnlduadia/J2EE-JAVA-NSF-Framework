package nsf.foundation.front.channel;

/**
 * @(#) GeneralServlet.java
 */

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nsf.core.log.NLog;
import nsf.foundation.front.channel.NAbstractServlet;
import nsf.support.tools.time.NHistorialWatch;

/**
 * <pre>
 * 이 클라스는 개발시에 실지 리용되는 일반 Servlet클라스이다.<br>
 * </pre>
 * 
 * @since 2007년 1월 5일
 * @version NSF 1.0
 * 
 * @author 최정철, Nova China<br>
 */

public class GeneralServlet extends NAbstractServlet
{
    /**
     * GeneralServlet 구축자
     *
     */
    public GeneralServlet()
    {
    }

    /**
     * NAbstractServlet에서 선언한 메소드를 정의하여 process메소드를
     * 호출하는 방법으로 기본 처리를 수행한다.
     * 
     * @param req servlet에서 전달받은 HttpServletRequest. form processing을 위하여 사용한다.
     * @param res servlet에서 전달받은 HttpServletResponse. result redirect을 위하여 사용한다.
     * @return void
    */ 
    protected void catchService(HttpServletRequest req, HttpServletResponse res)
    {
        NHistorialWatch hw = new NHistorialWatch();
        req.setAttribute("nsf.servlet.stopwatch", hw);
        try
        {
            hw.tick("Processing command begin");
            process(req, res);
        }
        catch(Exception e)
        {
            NLog.debug.println("Unexpected Exception occurred - " + e.toString());
            String errorCode = e.getMessage();
            if(errorCode == null || !errorCode.startsWith("nova."))
                errorCode = "nova.err.com.desc";
            req.setAttribute("nsf.servlet.error.code", errorCode);
            req.setAttribute("nsf.servlet.error.exception", e);
            req.setAttribute("nsf.servlet.error.request_uri", req.getRequestURI());
            processError(req, res);
        }
    }

    public static final String watchKey = "nsf.servlet.stopwatch";
}