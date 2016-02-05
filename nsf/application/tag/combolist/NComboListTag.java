package nsf.application.tag.combolist;

import javax.servlet.jsp.JspTagException;

import nsf.application.tag.NTag;
import nsf.support.collection.NMultiData;

/**
 * <pre>
 * jsp의 <select>문을 TagLib로 실현.
 * </pre>
 * 
 * @since 2007/1/5
 * @version NSF 1.0
 * @author 김철남 Nova China
 */
public class NComboListTag extends NTag {

	private NMultiData mData = null;
    private boolean bodyEmptyFlag = true;
    public NComboListTag() {
        super();
    }

	public int doAfterBody() throws JspTagException {

        bodyEmptyFlag = false;
        
        String selectedCode = bodyContent.getString().trim();
        
        int rowSize = mData.keySize("code");
        if( rowSize == 0 ){
            this.printBlankString();
        }
        
        StringBuffer optionString = new StringBuffer();
        for( int inx = 0 ; inx < rowSize ; inx++ ){
            String code = mData.getString("code", inx);
            String value = mData.getString("value", inx);
            if(code.equals(selectedCode)){
                optionString.append("<option value=\"" + code + "\" selected>");
            }else{ 
                optionString.append("<option value=\"" + code + "\">");
            }
            optionString.append(value);
            optionString.append("</option>\n");
        }

        this.printTagBody(optionString.toString());

		return EVAL_PAGE;
	}	
	
    public int doEndTag() throws JspTagException{

        if ( bodyEmptyFlag == true ) {
            int rowSize = mData.keySize("code");
            if( rowSize == 0 ){
                this.printBlankString();
            }
            
            StringBuffer optionString = new StringBuffer();
            for( int inx = 0 ; inx < rowSize ; inx++ ){
                String code = mData.getString("code", inx);
                String value = mData.getString("value", inx);
                optionString.append("<option value=\"" + code + "\">");
                optionString.append(value);
                optionString.append("</option>\n");
            }

            this.printTagString(optionString.toString());
        }

        return EVAL_PAGE;
        
    }
    
	public void setValue(NMultiData value){
		this.mData = value;
	}
	
	public NMultiData getValue(){
		return mData;
	}

}

