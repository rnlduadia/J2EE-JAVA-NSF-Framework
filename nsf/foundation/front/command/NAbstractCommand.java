package nsf.foundation.front.command;

/**
 * @(#) NAbstractCommand.java
 */ 

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <pre>
 * 실지 개발에서 리용할 command의 interface를 정의한다.<br>
 * </pre>
 * 
 * @since 2007년 1월 5일
 * @version NSF 1.0
 *
 * @author 최정철, Nova China<br>
 */

public interface NAbstractCommand {

/** 
 * command의 main()역할을 하는 함수 servlet에서 호출되게 되며, form-processing 및 result redirect를 사용하기 위하여 req, res를 전달 받아야 한다.
 * @param  req servlet에서 전달받은 HttpServletRequest. form processing을 위하여 사용한다.
 * @param  res servlet에서 전달받은 HttpServletResponse. result redirect을 위하여 사용한다.
 * @return  result redirect로 dispatch될 jsp/html/servlet의 uri. 상대 경로 혹은 절대경로로 구성할 수 있다.
 */
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception;
	
}

