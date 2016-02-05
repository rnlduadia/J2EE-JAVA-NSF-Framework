package nsf.application.xsync.widget;

import java.util.Set;

import nsf.support.collection.NData;
import nsf.application.xsync.NAbstractSync;

/**
 * <pre>
 * aJax로 input를 setting할때 리용한다.
 * </pre>
 * 
 * @since 2007/1/5
 * @version NSF 1.0
 * @author 김철남 Nova China
 */

public class NSyncDivHTML extends NAbstractSync{

    private final NData data;

    public NSyncDivHTML(String divid, String text){
        super("divHTML");
        setAttribute("id",divid);
        data = new NData();
        data.set(divid, text);
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

