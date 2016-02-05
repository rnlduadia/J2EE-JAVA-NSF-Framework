package nsf.application.tag.message;

import java.util.Locale;

import javax.servlet.jsp.JspTagException;

import nsf.application.tag.NTag;
import nsf.application.message.NMessageSource;
import nsf.core.exception.NException;

/**
 * <pre>
 * NSF 1.0 의 Message컴포넌트에 대해 메세지를 TagLibrary로 구현.
 * </pre>
 *
 * @since 2007/1/5
 * @version NSF 1.0
 * @author 김철남 Nova China
 */
public class NMessageTag extends NTag {

	/**
     * 사용자가 입력하는 code(저장된 메세지에 대한 key 값) 정보
     */
    private String code = null;
    
    /**
     * 최종 리턴되는 메세지 값
     */
	private String message = "";

    /**
     * NSF의 NMessageSource 클래스
     */
	protected NMessageSource messageSource = null;

    public NMessageTag() {
        super();
    }

    public void release() {
        super.release();
        this.code = "";
        this.message = "";
    }

    /**
     * message tag의 attribute에 저장된 값을 인자로 LMessageSource를 생성하여 해당 메세지를 구성한 뒤 출력한다.
     * 이 때 set-locale-automatic 옵션이 true로 지정된 경우 page context 에서 해당 locale정보를 읽어온다.
     * 이 값은 typehander가 LLangType, LModuleLangType, LPerfectType 인 경우에만 가능하며,
     * 이 외의 경우에는 Exception이 발생된다.
     *
     * @return  int     EVAL_PAGE
     */
	public int doEndTag() throws JspTagException {
		try {
			Locale pageLocale = new Locale("ko");
            message = new NMessageSource(pageLocale).getMessage(code);
            
            this.printTagString(message);
        }
        catch(NException e) {
        	throw new JspTagException(e.getMessage(), e.getCause());
		}

		return EVAL_PAGE;
	}
    
    public void setCode(String code){
    	this.code = code;
    }
    
	public String getCode() {
		return this.code;
	}
	
}

