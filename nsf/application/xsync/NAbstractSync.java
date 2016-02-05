package nsf.application.xsync;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import nsf.support.tools.html.NHtmlUtil;

/**
 * <pre>
 * jsp에 해당기능에 맞는 조작을 진행하는 클라스
 * </pre>
 * 
 * @since 2007/1/5
 * @version NSF 1.0
 * @author 김철남 Nova China
 */

public abstract class NAbstractSync implements NXsyncable {

	private final String name;
	private final Map attributes = new HashMap();

	public NAbstractSync(String name) {
		this.name = name;
	}

	protected void setAttribute(String name, String value) {
		attributes.put(name, value);
	}

	public void setOnBeforeAction(String callback){
		attributes.put("onBeforeAction", NHtmlUtil.escape(callback));
	}

	public void setOnAfterAction(String callback){
		attributes.put("onAfterAction",  NHtmlUtil.escape(callback));
	}

	protected String getCdata(String cdata) {
		return "<![CDATA[" + cdata + "]]>";
	}

	protected String getTag(String tag, String data) {
		return "<" + tag + ">" + data + "</" + tag + ">";
	}

	protected String getTag(String tag, String aname, String avalue, String data) {
		StringBuffer sb = new StringBuffer();
		sb.append("<").append(tag).append(" ")
		  .append(aname).append("=\"").append(NHtmlUtil.escape(avalue)).append("\">")
		  .append(data)
		  .append("</").append(tag).append(">\r\n");
		return sb.toString();
        
	}

	protected String getTag(String tag, Map attr, String data) {
		StringBuffer sb = new StringBuffer();
		sb.append("<").append(tag).append(" ");

		Set keySet = attr.keySet();
		String[] keyArray = (String [])keySet.toArray(new String[keySet.size()]);

		int size = keyArray.length;
		for (int idx = 0 ; idx < size ; idx++) {
			String  value = NHtmlUtil.escape((String)attr.get(keyArray[idx]));
			sb.append(keyArray[idx]).append("=\"").append(value).append("\" ");
		}
		sb.append(">\n\r");
		sb.append(data).append("\n\r");
		sb.append("</").append(tag).append(">\r\n");
		return sb.toString();
	}

	public String toXmlString() {
		return getTag(this.name, this.attributes, getInnerTag());
	}

	protected abstract String getInnerTag();

}





















