package nsf.application.xsync.widget;

import nsf.support.tools.html.NHtmlUtil;
import nsf.application.xsync.NAbstractSync;

/**
 * <pre>
 * aJax로 alert을 띄울때 리용한다.
 * </pre>
 * 
 * @since 2007/1/5
 * @version NSF 1.0
 * @author 김철남 Nova China
 */

public class NSyncText extends NAbstractSync{

	private final String text;

	public NSyncText(String text,String txtId){
		super("text");
        setAttribute("id",txtId);
		this.text = text;
	}
	public String getInnerTag() {
		return getCdata(NHtmlUtil.escape(text));
	}

}