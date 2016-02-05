package nsf.application.tag.mask;

import javax.servlet.jsp.JspTagException;

import nsf.application.tag.NTag;

/**
 * <pre>
 * 마스크기능을 TagLib로 실현
 * </pre>
 * 
 * @since 2007/1/5
 * @version NSF 1.0
 * @author 김철남 Nova China
 */
public class NMaskTag extends NTag {
	
    private String mask = null;
    public NMaskTag() {
        super();
    }
   
    public int doAfterBody() throws JspTagException {
		String value = bodyContent.getString().trim(); 
        this.printTagBody(getMaskedString(value,mask));
        return SKIP_BODY;
	}
	
    private String getMaskedString(String str, String format){
        int j = 0;
        if (str == null || str.length() ==0) return "";
        if (format == null || format.length() ==0) return str;
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < format.length(); i++) {
            if (format.charAt(i) == '#') {
                if (j >= str.length()) return sb.toString();
                sb.append(str.charAt(j++));
            } else {
                sb.append(format.charAt(i));
            }
        }
        return sb.toString();
    }

	public String getMask() {
		return mask;
	}

	public void setMask(String mask) {
		this.mask = mask;
	}
}

