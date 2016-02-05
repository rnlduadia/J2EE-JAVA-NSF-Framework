package nsf.application.tag.page;

import javax.servlet.jsp.JspTagException;

import nsf.application.page.NDefaultPageNavigation;
import nsf.support.collection.NMultiData;

/**
 * <pre>
 * NSF 1.0의  Page컴포넌트의 UI 부분을 TagLibrary로 구현.
 * Page 가 구성되였을 때 해당 컬럼별로 Sorting 할 수 있는 메소드를 대신한다.
 * </pre>
 * 
 * @since 2007/1/5
 * @version NSF 1.0
 * @author 김철남 Nova China
 */

public class NPageSortTag extends NPageTag {

	/**
     * NPageSortTagLib의 parent
     */
	private NPageTag parent = null;
    private NDefaultPageNavigation pageNavigation = null;
    private NMultiData pageMultiData = null;
    
    /**
     * 구성된 page의 title
     */
	private String title = null;
    
    /**
     * 구성된 page의 title에 대한 실제 column 이름
     */
	private String column = null;
	
	public NPageSortTag() {
		super();
	}
	
    /**
     * page의 renderering 중 showSortField 메소드를 호출하여 결과를 출력 
     * 
     * @return  SKIP_BODY
     */
	public int doStartTag() throws JspTagException { 
		parent = (NPageTag)findAncestorWithClass(this, NPageTag.class);  
    	if( parent == null ) {
    		throw new JspTagException("NSF_TLD_002 : tag 'pageSort'cannot be used without its parent tag 'page'.");
    		
    	}
        
        pageNavigation = parent.getPageNavigation();
        pageMultiData  = parent.getPageMultiData();
        
   		String tagString = "";    		
       	if( pageMultiData == null || pageNavigation == null ) {
       		tagString = title;
       	}
       	else {
       		tagString = pageNavigation.showSortField(title, column);
       	}

       	this.printTagString(tagString);		

		return SKIP_BODY;
	}
	
	public int doEndTag() throws JspTagException {    	
		return EVAL_PAGE;
	}	
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public void setColumn(String column) {
		this.column = column;
	}
	
	public String getColumn() {
		return this.column;
	}
}

