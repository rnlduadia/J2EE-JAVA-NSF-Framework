package nsf.application.tag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import nsf.core.config.NConfiguration;
import nsf.support.collection.NMultiData;

/**
 * <pre>
 * NSF 1.0의 NTagLibrary 컴포넌트에서 print를 담당. 
 * print할 자료가 없을 경우 출력할 메세지를 설정할 수도 있다.
 * </pre>
 * 
 * @since 2007/1/5
 * @version NSF 1.0
 * @author 김철남 Nova China
 */
public class NTag extends BodyTagSupport {

    /**
     * data가 없을 경우 출력하는 메세지 
     */
	private String message = "";
    
    /**
     * 메세지를 출력하는 방식(Text or JavaScript 등)을 다양화 하기 위해 message 앞 쪽의 소스코드
     */
	private String preMessageProperty  = "";
	
    /**
     * 메세지를 출력하는 방식(Text or JavaScript 등)을 다양화 하기 위해 message 뒤 쪽의 소스코드
     */
    private String postMessageProperty = "";
	
	/**
	 * loop를 지원하기 위한 변수로, 하위 클라스와 값이 공유되어야 한다.
	 */
	protected int loopIndex = 0;
    
    public void printBlankString() throws JspTagException {
        printTagString("");
    }
	
	/**
	 * Tag 의 위치에 내용을 출력한다.
	 *
	 * @param  printStr				출력하려는 내용
	 * @throws JspTagException 	JspWriter를 이용하던중 IOException이 발생하는 경우.
	 */  
	public void printTagString(String printStr) throws JspTagException{
		JspWriter out = this.pageContext.getOut();
		
		try{
			out.print( printStr );
		}catch(IOException e){
			throw new JspTagException("IO Error: " + e.getMessage(), e.getCause());
		}
	}

	/**
	 * Tag로 둘러 싸여 있는 Body 의 위치에 내용을 출력한다.
	 *
	 * @param  printStr				출력하려는 내용
	 * @throws JspTagException 	JspWriter를 이용하던중 IOException이 발생하는 경우.
	 */  
	public void printTagBody(String printStr) throws JspTagException{	
		JspWriter out = this.getPreviousOut();
		try {
			out.print(printStr);
		} catch (IOException e) {
			throw new JspTagException("IO Error: " + e.getMessage(), e.getCause());
		}
	}
	
	/**
	 * Tag의 Body에 출력하려는 내용 중 특정 변수값에 값을 대입 시킨 후 출력한다.
	 *
	 * @param  	resultData			출력 내용을 구성하기 위한 해당 변수에 대입 할 값.
	 * @return	int					SKIP_BODY or EVAL_BODY_AGAIN
	 * @throws 	JspTagException	    JspWriter를 리용하던중 IOException이 발생하거나,
	 *                              출력 내용을 구성하던 중 변수 이름과 값이 매핑되지 않는 경우.
	 */
	public int printTagBodyWithTemplate(NMultiData resultData ) 
		throws JspTagException {
		
		String originalBodyString = this.getBodyContent().getString();
		ArrayList variableList = makeVariableList(originalBodyString);
		int currentRowCount = resultData.keySize(variableList.get(0));
		
		if( loopIndex == 0 ) {						
			if( variableList.size() == 0 ) {
				printTagBody(originalBodyString);
				return SKIP_BODY;
			}	
			
			if( currentRowCount == 0 ) {
				this.printTagBody(getNoDataFoundMessage());
				return SKIP_BODY;
			}
		}

		if( loopIndex == currentRowCount - 1 ) {			
			for( int i=0; i < currentRowCount; i++ ) {
				for( int j=0; j<variableList.size() / currentRowCount; j++ ) {
					String variable = (String) variableList.get(j);
					String value = resultData.getString(variable, i);
					
					if( value == null ) value = "";
					
					originalBodyString = originalBodyString.replaceFirst("\\$\\{" + variable + "\\}", value);
				}
			}
			
			this.printTagBody(originalBodyString);
			
			return SKIP_BODY;
		}
		else {
			++loopIndex;
			
			return EVAL_BODY_AGAIN;
		}
	}
	
	/**
	 * Tag의 Body에 출력하려는 내용 중 변수의 리스트를 뽑아내서 리턴한다.
	 *
	 * @param	originalBodyString	Body의 원래 내용을 담은 String
	 * @return	ArrayList			Body에 출력하려는 내용 중 변수의 리스트를 담은 ArrayList
	 */
	public ArrayList makeVariableList(String originalBodyString) {
		int startIndex = 0;
		int endIndex = 0;
		ArrayList returnArray = new ArrayList();
		
		do {
			endIndex = originalBodyString.indexOf("${", startIndex);
			if( endIndex != -1 ){
				startIndex = originalBodyString.indexOf("}", endIndex);
				returnArray.add(originalBodyString.substring(endIndex + 2, startIndex));
			}
			else {
				break;
			}
			startIndex++;
		}
		while( startIndex != 0 );
		
		return returnArray;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public void setPreMessageProperty(String preMessageProperty) {
		this.preMessageProperty = preMessageProperty;
	}
	
	public String getPreMessageProperty() {
		return this.preMessageProperty;
	}
	
	public void setPostMessageProperty(String postMessageProperty) {
		this.postMessageProperty = postMessageProperty;
	}
	
	public String getPostMessageProperty() {
		return this.postMessageProperty;
	}

	/**
	 * Tag의 위치에 원하는 메세지를 원하는 형태로 조합하여 리턴한다.
	 * 이 메소드는 사용자가 메세지의 내용과 형태를 tag 의 attribute로 지정한 경우에만
	 * 사용이 가능하며, 빈 값이 설정된 경우 Exception 을 발생시킨다.
	 * 기정으로 지정된 값을 출력받으려고 할 경우 true를 인자로 넘기면 된다. 
	 *
	 * @param  	message				출력하려는 내용
	 * @param  	preMessageProperty	메세지 앞 부분의 형태 (Table 혹은 Script 구문)
	 * @param  	postMessageProperty	메세지 앞 부분의 형태 (Table 혹은 Script 구문)
	 * @return	String				Resolved Message
	 * @throws  JspTagException 
	 */
	public String getResolvedMessage(boolean noDataMsg) throws JspTagException {		
		if( this.message.equals("") ) {
			if( noDataMsg ) {
				setNoDataFoundMessage();				
			}
			else {
				throw new JspTagException("message property in your tag isn't defined.");
			}
		}

		return getPreMessageProperty() + getMessage() + getPostMessageProperty();
	}
	
	public String getResolvedMessage() throws JspTagException {
		return getResolvedMessage(true);
	}

	public String getBlankMessage() {
		return "";
	}
	
	public String getNoDataFoundMessage() throws JspTagException {
		return getResolvedMessage(true);
	}
	
	public void setNoDataFoundMessage()
	{
		setMessage("There is No Data.");
		setPreMessageProperty ("<TR align=center><TD colspan=30> ");
		setPostMessageProperty ("</TD></TR>");
	}
	
    private Locale getLocale(String arg){
        if(arg.indexOf('_') > -1){
            String language = (arg.substring(0, arg.indexOf('_')));
            String country = (arg.substring(arg.indexOf('_') + 1, arg.length()));
            return new Locale(language, country);

        }else{
            return  new Locale(arg);
        }
    }

    protected Locale getBestLocale(String language, String country) throws Exception{
        if(language != null && country != null){
            return new Locale(language, country);
        }

        if(language != null){
            return getLocale(language);
        }

        boolean setAutomatic = false;
        String localeBindKey = null;

        try{
            NConfiguration conf = NConfiguration.getInstance();
            setAutomatic = conf.getBoolean("/configuration/nsf/message/set-locale-automatic", false);
            localeBindKey = conf.getString("/configuration/nsf/message/locale-bind-key", "");
        }catch(Exception e){
            throw e;
        }

        if(!setAutomatic){
            throw new JspTagException("Can't set locale..");
        }

        String localeBindValue = (String)pageContext.getRequest().getAttribute(localeBindKey);

        if(localeBindValue != null){
            return getLocale(localeBindValue);
        }

        Locale locale = pageContext.getRequest().getLocale();
        
        if (locale != null) {
            return locale;
        }        
        return Locale.getDefault();
    }
	
}

