
package nsf.support.collection;

/**
 * @(#) NDataProtocol.java 
 * 
 */

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <pre>
 * NAF에서 사용하는 모든 Value Object는 NData 와  NMultiData형을 기본으로 하고 있고,<br>
 * 또한 이 Object들은 NDataProtocol을 상속받아 사용하고 있다.<br>
 * 즉 NDataProtocol은 NAF에서 사용하는 모든 Value Object들의 Protocol형이라고 보면 된다.<br>
 * </pre>
 * 
 * @since 2007년 1월 5일
 * @version NSF 1.0
 * 
 * @author 강영진, Nova China 
 */

public class NDataProtocol extends LinkedHashMap {

    /**
	 * NDataProtocol의 이름
	 */
	protected String name = null;
	/**
	 * Option속성으로, Null값을 Initialize해서 Return해 준다.
	 */	
	protected boolean nullToInitialize = false;

	/**
	 * @param arg0
	 * @param arg1
	 */
	public NDataProtocol(int arg0, float arg1) {
		super(arg0, arg1);
	}

	/**
	 * @param arg0
	 */
	public NDataProtocol(int arg0) {
		super(arg0);
	}

	/**
	 * 
	 */
	public NDataProtocol() {
		super();
	}

	/**
	 * @param arg0
	 */
	public NDataProtocol(Map arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 */
	public NDataProtocol(int arg0, float arg1, boolean arg2) {
		super(arg0, arg1, arg2);
	}

	/**
	 * Returns the name.
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * 이름을 설정한다.
	 * @param name The name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * nullToInitialize에 대한 설정 여부를 확인한다.
	 * nullToInitialize의 역할은 기본적으로 false이나, true로 설정하면, null대신 설정되여 있는 default값을
	 * return한다. 
	 * @return boolean
	 */
	public boolean isNullToInitialize() {
		return nullToInitialize;
	}

	/**
	 * nullToInitialize의 값을 설정한다.
	 * nullToInitialize의 역할은 기본적으로 false이나, true로 설정하면, null대신 설정되여 있는 default값을
	 * return한다. 
	 * @param nullToInitialize The nullToInitialize to set
	 */
	public void setNullToInitialize(boolean nullToInitialize) {
		this.nullToInitialize = nullToInitialize;
	}
}

