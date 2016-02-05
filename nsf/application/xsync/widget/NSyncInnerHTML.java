package nsf.application.xsync.widget;

import java.util.Set;

import nsf.support.collection.NData;
import nsf.application.xsync.NAbstractSync;

/**
 * <pre>
 * aJax로 html을 삽입할때 리용한다.
 * </pre>
 * 
 * @since 2007/1/5
 * @version NSF 1.0
 * @author 김철남 Nova China
 */

public class NSyncInnerHTML extends NAbstractSync{

	private final NData data;

	public NSyncInnerHTML(String id, String text){
		super("innerHTML");
		data = new NData();
		data.set(id, text);
	}

	public NSyncInnerHTML(NData data){
		super("innerHTML");
		this.data = data;
	}

	public String getInnerTag() {
		StringBuffer sb = new StringBuffer();
		Set keySet = data.keySet();
		String[] keys = (String [])keySet.toArray(new String[keySet.size()]);
		int size = keys.length;
		for (int idx = 0 ; idx < size ; idx++) {
			String value = data.get(keys[idx]) != null ? data.get(keys[idx]).toString() : "";
			sb.append(getTag("cell", "id", keys[idx], getCdata(value)));
 		}
		return sb.toString();
	}
}

