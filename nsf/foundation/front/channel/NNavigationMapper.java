package nsf.foundation.front.channel;

/**
 * @(#) NNavigationMapper.java
 */
import javax.servlet.http.HttpServletRequest;

import nsf.core.config.NConfiguration;
import nsf.core.exception.NException;
import nsf.support.filemng.xml.NFileManager;

/**
 * <pre>
 * 이 클라스는 Navigation 을 위해서 필요한 Mapper이다. <BR>
 * Navigation을 처리하기 위해 필요한 XML과 Request간의 적절한 Mapping을 처리해 준다.<BR>
 * 그중 대표적인 일은 첫째, Command를 Mapping해서 반환해주며, <BR>
 * 둘째, Return Page를 Mapping해서 반환해주고, <BR>
 * 셋째, Error와 관련된 정보를 Mapping해준다. <BR>
 * </pre>
 *
 * @since 2007년 1월 5일
 * @version NSF 1.0
 *
 * @author 최정철, Nova China<br>
 */
public class NNavigationMapper {

	private static String RETURN_URL_ID = "RETURN_URL_ID";
	private static String RETURN_URL = "returnUrl";
	private static String ACTION = "ACTION";

	/**
	 *
	 * NNavigationMapper 생성자
	*/
	public NNavigationMapper() {
		super();
	}

	private static String actionNameOnly(String fullActionName) throws NException {
		try {
		int pathLastIndex = fullActionName.lastIndexOf(".");
		 return fullActionName.substring(0,pathLastIndex);
		}catch ( Exception e) {
			throw new NException( e ) ;
		}
	}

	private static String actionSpecOnly(String fullActionName) throws NException {
		try {
		int pathLastIndex = fullActionName.lastIndexOf(".");
		String specStr = fullActionName.substring(pathLastIndex+1);
		String aliasStr = NConfiguration.getInstance().get("/configuration/nsf/navigation<" + specStr + ">/navigation-alias");
		if( aliasStr != null && !"".equals(aliasStr)){
			specStr = aliasStr;
			if( "default".equals(specStr) )
				specStr = "nsf";
		}
		return specStr;
		}catch ( Exception e) {
			throw new NException( e ) ;
		}
	}

	/**
	* actionName과 Mapping되는 Command를 반환해 준다.
	* @param action String ActionName
	* @return String Command Name
	* @throws NException Configuration에서 데이터를 가져올 때 발생.
	*/
	public static String getCommandMapper(String actionName) throws NException {
		String commandName = null;
		NFileManager fileManager = NFileManager.getInstance();
		commandName = fileManager.get(actionSpecOnly(actionName) , "/navigation-mapper/action-name<" + actionNameOnly(actionName) + ">/command");

		if (commandName == null)
			throw new NException("NSF_NAV_001", "The action-name(" + actionName + ") is not found.");

		return commandName;
	}

	/**
	* actionName과 Mapping되는 Command를 반환해 준다.
	* @param action String ActionName
	* @param index int Command의 Index
	* @return String Command Name
	* @throws NException Configuration에서 데이터를 가져올 때 발생.
	*/
	public static String getCommandMapper(String actionName, int index) throws NException {
		String commandName = null;
		NFileManager fileManager = NFileManager.getInstance();
		commandName = fileManager.get(actionSpecOnly(actionName),"/navigation-mapper/action-name<" + actionNameOnly(actionName) + ">/command" + index);

		if (commandName == null)
			throw new NException("NSF_NAV_002", "The index(" + index + ") of action-name(" + actionName + ") is not found.");

		return commandName;
	}

	/**
	* actionName과 Mapping되는 Command의 갯수를 반환해 준다.
	* @param action String ActionName
	* @return String Command 갯수
	* @throws NException Configuration에서 데이터를 가져올 때 발생.
	*/
	public static String getCommandNoMapper(String actionName) throws NException {
		String commandNo = null;
		NFileManager fileManager = NFileManager.getInstance();
		commandNo = fileManager.get(actionSpecOnly(actionName),"/navigation-mapper/action-name<" + actionNameOnly(actionName) + ">/command-no");

		return commandNo;
	}

	/**
	* actionName과 Mapping되는 Return URL을 반환해 준다.
	* @param action String ActionName
	* @return String Return URL
	* @throws NException Configuration에서 데이터를 가져올 때 발생.
	*/
	public static String getReturnUrlMapper(String actionName) throws NException {
		String returnUrl = null;
		NFileManager fileManager = NFileManager.getInstance();
		returnUrl =fileManager.get(actionSpecOnly(actionName),"/navigation-mapper/action-name<" + actionNameOnly(actionName) + ">/return-url");

        if ( returnUrl == null && !isNullReturnUrlType(actionName) )
			throw new NException("NSF_NAV_003", "The return-url of the action-name(" + actionName + ") is not found.");

		return returnUrl;
	}

	private static boolean isNullReturnUrlType(String actionName) throws NException{
		String handlerClassName;
		String returnUrlType = NNavigationMapper.getReturnUrlTypeMapper(actionName);
        if( returnUrlType == null || "".equals(returnUrlType) ){
       		String specName = actionSpecOnly(actionName);
       		if("nsf".equals(specName)){
       			specName = "default";
       		}
    		String defaultHandler = NConfiguration.getInstance().get("/configuration/nsf/navigation<" + specName + ">/default-handler");
    		if( defaultHandler == null || "".equals(defaultHandler) ){
    			handlerClassName = "nsf.foundation.front.channel.handler.NFowardHandler";
    		}else{
    			handlerClassName = NConfiguration.getInstance().get("/configuration/nsf/navigation-handlers/handler<" + defaultHandler + ">");
    		}
        }else{
        	handlerClassName = NConfiguration.getInstance().get("/configuration/nsf/navigation-handlers/handler<" + returnUrlType + ">");
        }
       	if( "nsf.foundation.front.channel.handler.NNullHandler".equals(handlerClassName) )
       		return true;
		return false;
	}

	/**
	* actionName과 Mapping되는 Return URL을 반환해 준다.
	* @param action String ActionName
	* @return String Return URL
	* @throws NException Configuration에서 데이터를 가져올 때 발생.
	*/
	public static String getReturnUrlMapperWithName(String actionName, String returnUrlName ) throws NException {
		String returnUrl = null;
		NFileManager fileManager = NFileManager.getInstance();
		returnUrl =fileManager.get(actionSpecOnly(actionName),"/navigation-mapper/action-name<" + actionNameOnly(actionName) + ">/return-url<"+returnUrlName+">");

		if (returnUrl == null)
			throw new NException("NSF_NAV_003", "The return-url of the action-name(" + actionName + ") and returnUrlId (" + returnUrlName + ") is not found.");

		return returnUrl;
	}

	/**
	* 별도의 Return Url id가 셋팅되어있는지를 체크하여 셋팅된 경우에는 true를 셋팅되지 않은 경우에는 false를 return한다.
	* @param HttpServletRequest
	* @return boolean
	* @throws Exception
	*/
	public static boolean check_if_setReturnUrlName_called( HttpServletRequest req  ) throws Exception  {
		String urlId = getReturnUrlName(req);
		return ( urlId == null || "".equals(urlId) ) ? false : true ;
	}

	/**
	* 별도로 셋팅된  Return Url Id의 url을 return 해준다..
	* @param HttpServletRequest
	* @return String 셋팅된 return url
	* @throws Exception
	*/
	public static String getReturnUrlName( HttpServletRequest req  ) throws Exception  {
         Object url = req.getAttribute( RETURN_URL_ID );
         if ( url == null ) return null ;
		 else return (String)url ;
	}

	/**
	* 별도로 Return Url Id을 셋팅해준다. 셋팅할 Return Url Id는 navigation을 정의한 xml의 return url name이다.
	* @param HttpServletRequest
	* @param 셋팅할 Return Url Id
	* @throws Exception
	*/
	public static void setReturnUrlName( HttpServletRequest req , String returnUrlId ) throws Exception  {
		req.setAttribute( RETURN_URL_ID , returnUrlId);
	}

	/**
	* 별도로 Return Url Id의 셋팅을 해제해준다.
	* @param HttpServletRequest
	* @param 셋팅을 해제할  Return Url Id
	* @throws Exception
	*/
	public static void unsetReturnUrlName( HttpServletRequest req , String returnUrlId ) throws Exception {
		req.setAttribute( RETURN_URL_ID , null );
	}

	/**
	* 별도의 Return Url이 셋팅되어있는지를 체크하여 셋팅된 경우에는 true를 셋팅되지 않은 경우에는 false를 return한다.
	* @param HttpServletRequest
	* @return boolean
	* @throws Exception
	*/
	public static boolean check_if_setReturnUrl_called( HttpServletRequest req  ) throws Exception  {
		String urlId = getReturnUrl(req);
		return ( urlId == null || "".equals(urlId) ) ? false : true ;
	}

	/**
	* xml에 정의한 url을 사용하지 않고 직접 Return Url을 셋팅 해준다.
	* @param HttpServletRequest
	* @param 셋팅할 return url
	* @throws Exception
	*/
	public static void setReturnUrl( HttpServletRequest req , String returnUrl) throws Exception  {
		req.setAttribute( RETURN_URL , returnUrl);
	}

	/**
	* xml에 정의한 url을 사용하지 않고 직접 셋팅된  Return Url을 return 해준다.
	* @param HttpServletRequest
	* @return String 셋팅된 return url
	* @throws Exception
	*/
	public static String getReturnUrl( HttpServletRequest req ) throws Exception  {
        Object url = req.getAttribute( RETURN_URL  );
        if ( url == null ) return null ;
		 else return (String)url ;
	}

	/**
	* action을 셋팅하여 준다.
	* @param HttpServletRequest
	* @return String 셋팅할 action
	* @throws Exception
	*/
	public static void setAction( HttpServletRequest req , String action) throws Exception  {
		req.setAttribute( ACTION , action);
	}

	/**
	* 설정된 action을 return 해준다.
	* @param HttpServletRequest
	* @return String action
	* @throws Exception
	*/
	public static String getAction( HttpServletRequest req ) throws Exception  {
		 return (String)req.getAttribute( ACTION  );
	}

	/**
	* xml상에 정의된  Return Url을 return 해준다.
	* @param HttpServletRequest
	* @return String 셋팅된 return url
	* @throws Exception
	*/
	public static String getOrgReturnUrl( HttpServletRequest req ) throws Exception  {
		return getReturnUrlMapper( getAction(req) );
	}

	/**
	* actionName과 Mapping되는 Error시 Message를 보여줄 Page를 를 반환해 준다.
	* @param action String ActionName
	* @return String Error Page
	* @throws NException Configuration에서 데이터를 가져올 때 발생.
	*/
	public static String getErrorMapper(String actionName) throws NException {
		String messageName = null;

		NFileManager fileManager = NFileManager.getInstance();
		messageName = fileManager.get(actionSpecOnly(actionName),"/navigation-mapper/action-name<" + actionNameOnly(actionName) + ">/error");

		if (messageName == null || messageName.equals(""))
		    messageName = fileManager.get(actionSpecOnly(actionName),"/navigation-mapper/global-setting/error");

		if (messageName == null)
			throw new NException("NSF_NAV_004", "The error page of the action-name(" + actionName + ") is not found.");

		return messageName;
	}

	public static String getReturnUrlTypeMapper(String actionName) throws NException {
		String commandNo = null;
		NFileManager fileManager = NFileManager.getInstance();
		commandNo = fileManager.get(actionSpecOnly(actionName),"/navigation-mapper/action-name<" + actionNameOnly(actionName) + ">/return-type");

		return commandNo;
	}

	public static String getReturnUrlTypeClassMapper(String actionName) throws NException{
		String handlerClassName;
		String returnUrlType = NNavigationMapper.getReturnUrlTypeMapper(actionName);
       	if( returnUrlType == null || "".equals(returnUrlType) ){
       		String specName = actionSpecOnly(actionName);
       		if("nsf".equals(specName)){
       			specName = "default";
       		}
    		String defaultHandler = NConfiguration.getInstance().get("/configuration/nsf/navigation<" + specName + ">/default-handler");
    		if( defaultHandler == null || "".equals(defaultHandler) ){
    			handlerClassName = "nsf.foundation.front.channel.handler.NFowardHandler";
    		}else{
    			handlerClassName = NConfiguration.getInstance().get("/configuration/nsf/navigation-handlers/handler<" + defaultHandler + ">");
    		}
        }else{
        	handlerClassName = NConfiguration.getInstance().get("/configuration/nsf/navigation-handlers/handler<" + returnUrlType + ">");
        }
       	return handlerClassName;

	}
}

