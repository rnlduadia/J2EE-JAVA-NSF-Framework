package nsf.application.xsync.protocol;

import nsf.application.xsync.NAbstractSync;

/**
 * <pre>
 * aJax로 String형태의 자료를 전송할때 리용한다.
 * </pre>
 * 
 * @since 2007/1/5
 * @version NSF 1.0
 * @author 김철남 Nova China
 */

public class NSyncString extends NAbstractSync {

	private final String text;

	public NSyncString(String text){
		super("string");
		this.text = text;
	}

	public NSyncString(String id, String text){
		this(text);
		setAttribute("id", id);
	}

	public String getInnerTag() {
		return getCdata(text);
	}

}
