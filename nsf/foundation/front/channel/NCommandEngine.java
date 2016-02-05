package nsf.foundation.front.channel;

/**
 * @(#) NCommandEngine.java
 */

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nsf.core.exception.NException;
import nsf.foundation.front.command.NAbstractCommand;

/**
 * <pre>
 * 이 클라스는 Command를 처리하는 Engine역할을 한다.<br>
 * navigation xml에 정의된 Command들을 찾아내여 실행 한다.<br>
 * xml에 한번에 실행해야할 Command를 하나/복수 로 선언할 수 있고, <br>
 * 하나/복수개로 선언된 Command들은 이 Engine를 통해서 하나씩 실행된다.<br>
 * </pre>
 * 
 * @since 2007년 1월 5일
 * @version NSF 1.0
 * 
 * @author 최정철, Nova China<br>
 */

public class NCommandEngine {

	/** 
	* NCommandEngine 구축자
    */
	public NCommandEngine() {
		super();
	}

	/** 
	* 실행할 Command의 개수를 보고 하나 일때에는 processCommand()를 호출하고 <BR>
	* 복수 일때에는 processMultiCommand()를 호출한다.
	* @param req HttpServletRequest HttpServletRequest객체이다.
	* @param res HttpServletResponse HttpServletResponse객체이다.
	* @param actionName String ActionName 값을 가지고 있다. 
	* @return void
	*/
	public void executeCommand(HttpServletRequest req, HttpServletResponse res, String actionName)
		throws Exception {

		String commandNo = NNavigationMapper.getCommandNoMapper(actionName);

		if (commandNo == null || "".equals(commandNo))
			processCommand(req, res, actionName);
		else
			processMultiCommand(req, res, actionName, commandNo);
	}

	/** 
	* ActionName에 맞는 CommandName을 읽어오고 읽어들인 Command를 실제 execute()한다.
	* @param req HttpServletRequest HttpServletRequest객체이다.
	* @param res HttpServletResponse HttpServletResponse객체이다.
	* @param actionName String ActionName 값을 가지고 있다. 
	* @return void
	*/
	private void processCommand(HttpServletRequest req, HttpServletResponse res, String actionName)
		throws Exception {
		String commandName = NNavigationMapper.getCommandMapper(actionName);
		NAbstractCommand cmd = null;

		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			Class classCmd = classLoader.loadClass(commandName);
			cmd = (NAbstractCommand) classCmd.newInstance();
		} catch (ClassNotFoundException e) {
			throw new NException("NSF_CMD_001", "Command Class Not Found [" + commandName + "]", e);
		} catch (InstantiationException e) {
			throw new NException("NSF_CMD_002","Command Class Could not Instantiation [" + commandName + "]",e);
		} catch (IllegalAccessException e) {
			throw new NException("NSF_CMD_003","Command Class Illegaliy Access [" + commandName + "]",e);
		}
		cmd.execute(req, res);
	}

	/**
	* ActionName에 맞는 CommandName을 하나씩 읽어오고 읽어들인 Command들을<BR>
	* 하나씩 execute()한다. 
	* @param req HttpServletRequest HttpServletRequest객체이다.
	* @param res HttpServletResponse HttpServletResponse객체이다.
	* @param actionName String ActionName 값을 가지고 있다. 
	* @return void
	*/
	private void processMultiCommand(
		HttpServletRequest req,
		HttpServletResponse res,
		String actionName,
		String commandNo)
		throws Exception {

		int commandNumber = Integer.parseInt(commandNo);
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		NAbstractCommand cmd = null;

		for (int i = 0; i < commandNumber; i++) {
			String commandName = NNavigationMapper.getCommandMapper(actionName, i + 1);
			try {
				Class classCmd = classLoader.loadClass(commandName);
				cmd = (NAbstractCommand) classCmd.newInstance();
			} catch (ClassNotFoundException e) {
				throw new NException("NSF_CMD_001", "Command Class Not Found [" + commandName + "]", e);
			} catch (InstantiationException e) {
				throw new NException("NSF_CMD_002","Command Class Could not Instantiation [" + commandName + "]",e);
			} catch (IllegalAccessException e) {
				throw new NException("NSF_CMD_003","Command Class Illegaliy Access [" + commandName + "]",e);
			}
			cmd.execute(req, res);
		}
	}
}

