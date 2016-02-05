package nsf.support.collection;
/**
 * CollectionFilter.java
 */
import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import nsf.foundation.persistent.db.dao.NDaoConstants;
import nsf.support.collection.NMultiData;

/**
 * @since 2007년 1월 5일
 * @version NSF 1.0
 * @author 강영진, Nova China
 * 
 */
public class CollectionFilter {
	public CollectionFilter() {
		super();
	}

	public static  NMultiData cudMultiDataFilter(HttpServletRequest req, String actionKey, String createValue, String updateValue, String deleteValue) {
		NMultiData multiData = new NMultiData("cudMultiData");

		ArrayList indexList = new ArrayList();
		ArrayList actionKeyList = new ArrayList();
		String[] actionValues = req.getParameterValues(actionKey);
		if( actionValues == null )
			return multiData;
		for( int inx = 0 ; inx < actionValues.length ; inx++ ){
			String actionValue = actionValues[inx];
			if( actionValue.equals(createValue) ) {//|| actionValue.equals(updateValue) || actionValue.equals(deleteValue) ){
				indexList.add(""+inx);
				actionKeyList.add(NDaoConstants.CREATE_KEY);
			}else if( actionValue.equals(updateValue) ){
				indexList.add(""+inx);
				actionKeyList.add(NDaoConstants.UPDATE_KEY);
			}else if( actionValue.equals(deleteValue) ){
				indexList.add(""+inx);
				actionKeyList.add(NDaoConstants.DELETE_KEY);
			}
		}
		multiData.put(NDaoConstants.CUD_FILTER_KEY, actionKeyList);

        Enumeration e = req.getParameterNames();
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			String []values = req.getParameterValues(key);
			if(key.equals(actionKey))
				continue;
			
			ArrayList list = new ArrayList();
			int indexLength = indexList.size();
			for( int jnx = 0 ; jnx < indexLength ; jnx++ ){
				int index = Integer.parseInt((String)indexList.get(jnx));
				if(  index >= values.length ){
					list.add(null);
				}else{
				list.add(values[index]);
				}
			}
			multiData.put(key, list);
		}
		return multiData;
	}
}