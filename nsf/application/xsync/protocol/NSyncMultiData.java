package nsf.application.xsync.protocol;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import nsf.support.collection.NMultiData;
import nsf.application.xsync.NAbstractSync;

/**
 * <pre>
 * aJax로 NMultiData형태의 자료를 전송할때 리용한다.
 * </pre>
 * 
 * @since 2007/1/5
 * @version NSF 1.0
 * @author 김철남 Nova China
 */

public class NSyncMultiData extends NAbstractSync {

	private final NMultiData mData;

	public NSyncMultiData(NMultiData mData){
		super("NMultiData");
		this.mData = mData;
	}

	public NSyncMultiData(String id, NMultiData mData){
		this(mData);
		setAttribute("id", id);
	}

	public String getInnerTag() {
		StringBuffer sb = new StringBuffer();
		Set set = mData.keySet();
		String[] array = (String [])set.toArray(new String[set.size()]);
		int size = array.length;
		for( int inx = 0 ; inx < size ; inx++ ){
			String tagname = array[inx];
			ArrayList al = (ArrayList) mData.get(tagname);
			Iterator i = al.iterator();
			while(i.hasNext()) {
				String value = i.next().toString();
				sb.append(getTag(tagname, getCdata(value))).append("\r\n");
			}
		}
		return sb.toString();
	}


}
