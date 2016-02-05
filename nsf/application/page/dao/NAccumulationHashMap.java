package nsf.application.page.dao;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import nsf.core.log.NLogUtils;

/**
 * <pre>
 *  NData 와 SQL 문과의 매핑 정보를 캐싱하는 클라스로 Singleton Pattern 으로 구현되였다.  
 * </pre>
 * @since 2007/1/5
 * @version NSF 1.0
 * @author 김철남 Nova China
 */

final class NAccumulationHashMap {
	private Map paramNameArrayCache; // paramNameArrayCache Cache 
	private final static NAccumulationHashMap singletonObj = new NAccumulationHashMap();;

	/**
	 * <pre>
	 *  NAccumulationHashMap Constructor 
	 * </pre>	 
	 */
	private NAccumulationHashMap() {
		try {
			paramNameArrayCache = Collections.synchronizedMap(new HashMap());
		} catch (Exception e) {
			NLogUtils.toDefaultLogForm("LAccumulationHashMap",
					"LAccumulationHashMap()", e.getMessage());
		}
	}

	/**
	 * <pre>
	 *  NAccumulationHashMap 의 Singleton Instance 를 반환한다.
	 * </pre>	  
	 * @return NAccumulationHashMap Instance
	 */
	public static NAccumulationHashMap getInstance() {
		return singletonObj;
	}

	/**
	 * <pre>
	 *  NData 와 SQL 문과의 매핑 정보를 캐싱하고 있는 paramNameArrayCache 를 반환한다. 
	 * </pre> 
	 * @return paramNameArrayCache 캐싱 매핑 정보
	 */
	public Map getParamNameArrayCache() {
		return paramNameArrayCache;
	}
	/**
	 * <pre>
	 *  NData 와 SQL 문과의 매핑 정보를 캐싱하고 있는 paramNameArrayCache 를 초기화 한다. 
	 * </pre>
	 */
	public void reset() {
		if (paramNameArrayCache != null && paramNameArrayCache.size() > 0)
			paramNameArrayCache.clear();
	}

}

