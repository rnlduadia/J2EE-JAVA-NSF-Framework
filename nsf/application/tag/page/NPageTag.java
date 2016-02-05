package nsf.application.tag.page;


import javax.servlet.jsp.JspTagException;

import nsf.application.page.NDefaultPageNavigation;
import nsf.support.collection.NMultiData;
import nsf.application.tag.NTag;

/**
 * <pre>
 * NSF 1.0의 Page컴포넌트의 UI 부분을 TagLibrary로 구현.
 * Page 관련 TagLibrary의 최상위 클라스로서 페지의 Rendering 기능을 담당하는 Class를 생성하는 역할을 한다.
 * </pre>
 * 
 * @since 2007/1/5
 * @version NSF 1.0
 * @author 김철남 Nova China
 */

public class NPageTag extends NTag {
	private String var = "mPageData";
    /**
     * page의 rendering 을 담당하는 class
     */
	private NDefaultPageNavigation pageNavigation = null;
    private NMultiData pageMultiData = null;
    private NMultiData resultMultiData = null;
	
	public NPageTag() {
		super();
	}

	public void release() {
		super.release();
        this.pageNavigation = null;
	}
	
    public int doStartTag() throws JspTagException{  
    
        pageMultiData = pageNavigation.getPageMultiData();              
        resultMultiData = (NMultiData) pageNavigation.getResultMultiData();

        if(pageMultiData == null) {
            return SKIP_BODY;
        }
        if(pageNavigation.getRows() == 0 ) {
            return SKIP_BODY;
        }

        pageContext.getRequest().setAttribute(getVar(), resultMultiData);
        
        printTagString(this.pageNavigation.showJavaScript() + "\n" + this.pageNavigation.showHiddenParam());
        
        return EVAL_BODY_BUFFERED;

    }
    
    public void setVar(String var) {
        this.var = var;
    }
    
    public String getVar(){
        return this.var;
    }

    public int doAfterBody() throws JspTagException {		
		return EVAL_BODY_INCLUDE;
	}
    
    public int doEndTag() throws JspTagException {   	
    	try {
            
            if(bodyContent != null) {
    			bodyContent.writeOut(bodyContent.getEnclosingWriter());
            }
            
            if(pageMultiData != null && pageNavigation.getRows() != 0 ) {
                StringBuffer tailStr = new StringBuffer();
                tailStr.append("<div align='center'> \n")
                //tailStr.append("<span style='position: fixed; text-align:center; width:100%;'> \n")
                       .append(this.pageNavigation.showMoveBeforeIndex())
                       .append(this.pageNavigation.showMoveBeforePage())
                       .append(this.pageNavigation.showIndex())
                       .append(this.pageNavigation.showMoveNextPage())
                       .append(this.pageNavigation.showMoveNextIndex())
                       //.append("</span>")
                       .append("&nbsp;&nbsp;&nbsp;\n")
                       .append(this.pageNavigation.showRowSize())
                       .append("&nbsp;&nbsp;&nbsp;\n")
                       .append("(" + this.pageNavigation.showSelectIndex() + " / " + this.pageNavigation.getPages() + " )")
                //       .append("</span>");
                     .append("</div>");
                       
                printTagString(tailStr.toString());
            }            
        }
        catch(java.io.IOException e) {
        	throw new JspTagException("NSF_TLD_001 : IO Error: " + e.getMessage(), e.getCause());
        }
        finally {
        	this.release();
        }
        
        return EVAL_PAGE;
    }
    
    public void setPageNavigation(NDefaultPageNavigation pageNavigation){
        this.pageNavigation = pageNavigation;
    }
    
	public NDefaultPageNavigation getPageNavigation() {
		return this.pageNavigation;
	}
    
    public NMultiData getPageMultiData() {
        return this.pageMultiData;
    }
    
    public NMultiData getResultMultiData() {
        return this.resultMultiData;
    }

}

