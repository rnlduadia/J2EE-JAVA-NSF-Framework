package nsf.application.xsync.protocol;

import java.util.Set;

import nsf.support.collection.NData;
import nsf.application.xsync.NAbstractSync;

/**
 * <pre>
 * aJax로 NData형태로 자료를 전송할때 리용한다.
 * </pre>
 * 
 * @since 2007/1/5
 * @version NSF 1.0
 * @author 김철남 Nova China
 */

public class NSyncData extends NAbstractSync {

	private final NData data;

	public NSyncData(NData data){
		super("NData");
		this.data = data;
	}

	public NSyncData(String id, NData data){
		this(data);
		setAttribute("id", id);
	}

	public String getInnerTag() {
		StringBuffer sb = new StringBuffer();
		Set keySet = data.keySet();
		String[] keys = (String [])keySet.toArray(new String[keySet.size()]);
		int size = keys.length;
		for (int idx = 0 ; idx < size ; idx++) {
			String value = data.get(keys[idx]) != null ? data.get(keys[idx]).toString() : "";
			sb.append(getTag(keys[idx], getCdata(value)));
		}
		return sb.toString();
	}

}
