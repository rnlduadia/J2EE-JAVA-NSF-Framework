package nsf.support.filemng.xml;

/**
 * @(#) NFileManagerHandler.java
 * 
 */ 
import java.util.LinkedHashMap;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * <pre>
 * 이 Class는 xml을 HashMap형태로 변환하여 return해주는 역할을 하는 유용한 Class이다.<br>
 * 
 * </pre>
 *
 * @since 2007년 1월 5일
 * @version NSF 1.0
 * 
 * @author 강영진, Nova China 
 */

public class NFileManagerHandler extends DefaultHandler {

	private StringBuffer path = new StringBuffer();
	private LinkedHashMap hashMap = new LinkedHashMap();
	private String elementName;
	private StringBuffer elementValue;

	public NFileManagerHandler() {
		super();
	}
    public void clearMap() {
    	hashMap.clear();    	
    }

	/** 
	 * Receive notification of the start of an element. 
     * By default, do nothing. Application writers may override this method in a subclass 
     * to take specific actions at the start of each element (such as allocating a new tree 
     * node or writing output to a file).
	 * @param attrs - The specified or defaulted attributes.
     * @param sName - The local name (without prefix), or the empty string if Namespace processing is not being performed.
     * @param qName - The qualified name (with prefix), or the empty string if qualified names are not available. 
	 */
	public void startElement(String namespaceURI, String sName, String qName, Attributes attrs) throws SAXException {
		String eName = sName;
		if ("".equals(eName))
			eName = qName;
		path = path.append("/").append(eName);
		elementName = eName;

		if (attrs != null) {
			for (int i = 0; i < attrs.getLength(); i++) {
				String aName = attrs.getLocalName(i);
				if ("".equals(aName))
					aName = attrs.getQName(i);
				String value = attrs.getValue( i );
				if( aName.equals("type")){
					path = path.append("<type:").append(value).append(">");
				}
				else
					path = path.append("<").append(value).append(">");
			}
		}
	}

	/** 
	 * Receive notification of the end of an element.
	 * By default, do nothing. Application writers may override this method in a subclass
	 * to take specific actions at the end of each element (such as finalising a tree 
	 * node or writing output to a file).
     * @param sName - The local name (without prefix), or the empty string if Namespace processing is not being performed.
     * @param qName - The qualified XML 1.0 name (with prefix), or the empty string if qualified names are not available.
     */
	public void endElement(String namespaceURI, String sName, String qName) throws SAXException {
		String eName = sName;

		if ("".equals(eName)){
			eName = qName;
		}

		if (eName.equals(elementName) ) {
			hashMap.put(path.toString().trim(), elementValue.toString().trim());
		}
		
		elementValue = null;
		int lastSlashindex = path.lastIndexOf("/");
		path = new StringBuffer(path.substring(0, lastSlashindex));
	}
	
	/** 
	 * Receive notification of character data inside an element. 
	 * By default, do nothing. Application writers may override this method
	 * to take specific actions for each chunk of character data  
	 * (such as adding the data to a node or buffer, or printing it to a file).
	 * @param ch - The characters.
	 * @param start - The start position in the character array.
	 * @param length - The number of characters to use from the character array. 
	 */
	public void characters(char[] ch, int start, int length) throws SAXException {
		String str = new String(ch, start, length);
		if (elementValue == null)
			elementValue = new StringBuffer(str);
		else
			elementValue.append(str);
	}

	/** 
	 * xml을 통해서 만들어진 LinkedHashMap을 return한다.  
	 * @return LinkedHashMap 
	 */
	public LinkedHashMap getMap() {
		return hashMap;
	}

}

