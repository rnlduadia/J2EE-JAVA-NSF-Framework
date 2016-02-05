package nsf.foundation.front.channel;

/**
 * @(#) NAbstractServlet.java
 */

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nsf.core.exception.NException;
import nsf.core.log.NLog;
import nsf.core.log.seqlog.NTxTrace;
import nsf.foundation.front.channel.handler.NReturnUrlHandler;

/**
 * <pre>
 * 이 servlet은 Navigation과 관련된 기본 처리를  담당하고 있는 클라스이다.<br>
 * Request가 들어오면 그 Request를 분석하여 이후에 실행해야 할 Command를 찾아서<br>
 * 실행하게 되며, execute후에 발생되는 오유에 대한 Message처리도 수행한다.<br>
 * 그리고 모든 실행을 하고 난 후에는 return 페지로 Dispatch하는 역할을 하고 있다.<br>
 * 이 Servlet은 get/post모든 방식을 처리한다.
 * </pre>
 *
 * @since 2007년 1월 5일
 * @version NSF 1.0
 *
 * @author 최정철, Nova China<br>
 */

public abstract class NAbstractServlet extends HttpServlet {

    /**
     * NAbstractServlet Default 구축자
     */
    public NAbstractServlet(){
        super();
    }

    /**
     * 실지 개발시에 만들게 되는 Servlet에서 이 메소드를 정의해서 거기에서 process메소드를
     * 호출하는 방법으로 기본 처리를 수행한다.
     * 
     * @param req servlet에서 전달받은 HttpServletRequest. form processing을 위하여 사용한다.
     * @param res servlet에서 전달받은 HttpServletResponse. result redirect을 위하여 사용한다.
     * @return void
    */
    protected abstract void catchService(HttpServletRequest req, HttpServletResponse res);

    /**
     * catchService를 호출하여 Get방식을 처리한다<BR>
     * @param req servlet에서 전달받은 HttpServletRequest. form processing을 위하여 사용한다.
     * @param res servlet에서 전달받은 HttpServletResponse. result redirect을 위하여 사용한다.
     * @return void
    */
    protected void doGet(HttpServletRequest req, HttpServletResponse res) {
        NTxTrace.regist("NSF");
        catchService(req, res) ;
    }
    /**
     * catchService를 호출하여 Post방식을 처리한다<BR>
     * @param  req servlet에서 전달받은 HttpServletRequest. form processing을 위하여 사용한다.
     * @param  res servlet에서 전달받은 HttpServletResponse. result redirect을 위하여 사용한다.
     * @return void
    */
    protected void doPost(HttpServletRequest req, HttpServletResponse res) {
        NTxTrace.regist("NSF");
        catchService(req, res) ;
    }

    /**
     * 일반적인 html error page를 생성/표시한다.
     * NAbstractServlet을 상속받은 Servlet이나 command에서 handling되지 않은 exception이 발생하거나 System exception이 발생했을때 사용할 수 있는
     * plain text type의 error page를 생성한다.
     * <BR>주로 개발시에 사용할 수 있을것이다.
     * @param  req servlet에서 전달받은 HttpServletRequest. form processing을 위하여 사용한다.
     * @param  res servlet에서 전달받은 HttpServletResponse. result redirect을 위하여 사용한다.
     * @param  customMsg 오유 메세지
     * @param  e 발생한 오유객체
     * @return void
    */
    protected void printErr(HttpServletRequest req, HttpServletResponse res, String customMsg, Exception e) {
        try {
            StringBuffer buf = new StringBuffer();
            buf.append("JSP/Servlet Error (Catched by NAbstractServlet) :[");
            buf.append(customMsg);
            buf.append("] Request URI: " + req.getRequestURI());
            String user = req.getRemoteUser();
            if ( user != null ) {
                buf.append(", User: " + user );
            }

            buf.append(", User Location: " + req.getRemoteHost() + "(" + req.getRemoteAddr() + ")");

            NLog.report.println(buf);

            e.printStackTrace(NLog.report);

            java.io.PrintWriter out = res.getWriter();
            out.println("<html><head><title>Error</title></head><body bgcolor=white><xmp>");
            out.println(buf.toString());
            e.printStackTrace(out);
            out.println("</xmp></body></html>");
            out.close();

        } catch (Exception ex) { }
    }

    /**
     * 결과화면(html/jsp등)을 dispatch한다.
     *
     * @param  req servlet에서 전달받은 HttpServletRequest. form processing을 위하여 사용한다.
     * @param  res servlet에서 전달받은 HttpServletResponse. result redirect을 위하여 사용한다.
     * @param  jspfile dispatch할 html/jsp의 uri. 상대경로 혹은 절대경로를 모두 모두 사용할 수 있다.
     * @return void
    */
    protected void errorPrintJsp(HttpServletRequest req, HttpServletResponse res, String jspfile) {
        RequestDispatcher dispatcher = null;
        try {
            dispatcher = getServletContext().getRequestDispatcher(jspfile);
            dispatcher.forward(req, res);
        } catch (Exception e) {
            this.printErr(req,res,"printJsp Failed",e);
        }
    }

    /**
     * 결과화면(html/jsp등)을 dispatch한다.
     *
     * @param  req servlet에서 전달받은 HttpServletRequest. form processing을 위하여 사용한다.
     * @param  res servlet에서 전달받은 HttpServletResponse. result redirect을 위하여 사용한다.
     * @param  jspfile dispatch할 html/jsp의 uri. 상대경로 혹은 절대경로를 모두 모두 사용할 수 있다.
     * @return void
    */
    protected void successPrintJsp(HttpServletRequest req, HttpServletResponse res, String jspfile, String returnType, String actionName) throws NException{
    	String handlerClassName = NNavigationMapper.getReturnUrlTypeClassMapper(actionName);
    	NReturnUrlHandler handler = null;
    	try {
    		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    		Class classCmd = classLoader.loadClass(handlerClassName);
    		handler = (NReturnUrlHandler) classCmd.newInstance();
    		handler.doNavigation(req, res, jspfile);
    	} catch (ClassNotFoundException e) {
    		throw new NException("NSF_NAV_005", "Handler Class Not Found [" + handlerClassName + "]", e);
    	} catch (InstantiationException e) {
    		throw new NException("NSF_CMD_006","Handler Class Could not Instantiation [" + handlerClassName + "]",e);
    	} catch (IllegalAccessException e) {
    		throw new NException("NSF_CMD_007","Handler Class Illegaliy Access [" + handlerClassName + "]",e);
    	}
    }

	/**
	 * Command를 찾아서 실행하고(execute), Command가 완료된 후에는
	 * 돌아갈 return page를 찾아서 forword해 주는 역할을 한다.
	 *
     * @param  req servlet에서 전달받은 HttpServletRequest. form processing을 위하여 사용한다.
     * @param  res servlet에서 전달받은 HttpServletResponse. result redirect을 위하여 사용한다.
 	*/
	protected void process(HttpServletRequest req, HttpServletResponse res) throws Exception {

		String path = req.getServletPath();
		int pathFirstIndex = path.lastIndexOf("/");
		pathFirstIndex++;
		String action = path.substring(pathFirstIndex);

		NNavigationMapper.setAction(req, action); // Command 에서 사용가능 하도록

		NCommandEngine commandEngine = new NCommandEngine();
		commandEngine.executeCommand(req, res, action);

		String returnUrlName = null ;
		String returnUrlType = null;

		if(  NNavigationMapper.check_if_setReturnUrl_called (req) ) {
			returnUrlName = NNavigationMapper.getReturnUrl(req);
		} else {
			if ( NNavigationMapper.check_if_setReturnUrlName_called(req) ) returnUrlName = NNavigationMapper.getReturnUrlMapperWithName(action, NNavigationMapper.getReturnUrlName(req) ) ;
			else returnUrlName = NNavigationMapper.getReturnUrlMapper( action );
		}
		returnUrlType = NNavigationMapper.getReturnUrlTypeMapper( action );
		if (!returnUrlName.equals("null") && returnUrlName != null ) successPrintJsp(req, res, returnUrlName, returnUrlType, action );
	}

	/**
	 * 만일 process를 진행하던 중에 오유가 발생하면, 이 메소드를 호출해야 한다.
	 * 이 메소드가 호출되면, 현재 수행중인 command에 대한 error page를 찾아서
	 * forward 시켜준다.
	 *
	 * @param  req servlet에서 전달받은 HttpServletRequest. form processing을 위하여 사용한다.
	 * @param  res servlet에서 전달받은 HttpServletResponse. result redirect을 위하여 사용한다.
	*/
	protected void processError(HttpServletRequest req, HttpServletResponse res) {
		String path = req.getRequestURI();
		int pathFirstIndex = path.lastIndexOf("/");
		pathFirstIndex++;
		String actionName = path.substring(pathFirstIndex);

		try {
			String errorURI = NNavigationMapper.getErrorMapper( actionName );
			errorPrintJsp ( req, res, errorURI );
		} catch (NException e) {
			printErr ( req, res, "getErrorPage Failed.",e );
		}
	}
}

