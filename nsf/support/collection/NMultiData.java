package nsf.support.collection;

/**
 * @(#) NMultiData.java
 * 
 */

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import nsf.core.log.NLog;
import nsf.support.tools.string.NStringUtil;
import nsf.support.tools.string.NUnicodeUtil;

/**
 * <pre>
 * 이 Class는 NAF에서 Data Protocol로 사용되며, Data의 관리를 손쉽게 할 수 있도록 도와준다.<BR>
 * 대표적인 례로 HttpServletRequest로 받은 둘 이상의 Data를 가지고 있는<BR>
 * request의 name과 value를  * Key와 Value로 사용하여 효율적으로 관리를 하고 있으며,<BR>
 * 또한 Multi Value Object의 단점을 보충하여 좀 더 편리하게 사용할 수 있도록 만들어진 Class이다.
 * </pre>
 *
 * @since 2007년 1월 5일
 * @version NSF 1.0
 * 
 * @author 강영진, Nova China 
 */

public class NMultiData extends NDataProtocol {

	/**
	 * 현재 저장되여 있는 Value의 개수를 가지고 있다.
	 */
	private int field_index = 0;

	/**
	 * NData를 저장시 NData의 이름에 대한 Value의 개수를 가지고 있다.
	 */
	HashMap entityKey = null;

	/**
	 * Constructor for NMultiDataProtocol.
	 * @param initialCapacity
	 * @param loadFactor
	 */
	public NMultiData(String name) {
		this.name = name;
	}

	/**
	 * Constructor for NMultiDataProtocol.
	 * @param initialCapacity
	 * @param loadFactor
	 */
	public NMultiData(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	/**
	 * Constructor for NMultiDataProtocol.
	 * @param initialCapacity
	 */
	public NMultiData(int initialCapacity) {
		super(initialCapacity);
	}

	/**
	 * Constructor for NMultiDataProtocol.
	 */
	public NMultiData() {
		super();
	}

	/**
	 * Constructor for NMultiDataProtocol.
	 * @param m
	 */
	public NMultiData(Map m) {
		super(m);
	}

	/**
	 * Constructor for NMultiDataProtocol.
	 * @param initialCapacity
	 * @param loadFactor
	 * @param accessOrder
	 */
	public NMultiData(int initialCapacity, float loadFactor, boolean accessOrder) {
		super(initialCapacity, loadFactor, accessOrder);
	}

	/**
	* 해당 key값에 해당하는 index번째의 Value를 파라메터로 전달된 value로 Replace한다.
	* @param key String
	* @param index int
	* @param replaceValue Object
	*/
	public void modify(Object key, int index, Object replaceValue) {
		if (!super.containsKey(key)) {
			NLog.report.println("[NException in NMultiData] Key(" + key + ") does not exist in NMultiData(" + this.name + ") ");
			throw new RuntimeException("[RuntimeException in NMultiData] Key(" + key + ") does not exist in NMultiData(" + this.name + ") ");
		}
		ArrayList arrayList = (ArrayList)this.get(key);
		int valueSize = arrayList.size();
		if( !(valueSize > index) ){
			NLog.report.println("[RuntimeException in NMultiData] Index("+ index +") of Key(" + key + ") exceeds size(" + (valueSize-1) + ") of NMultiData ");
			throw new RuntimeException("[RuntimeException in NMultiData] Index("+ index +") of Key(" + key + ") exceeds size(" + (valueSize-1) + ") of NMultiData");
		}
		arrayList.set(index, replaceValue);
	}

	/**
	* 해당 key값에 해당하는 index번째의 Value를 파라메터로 전달된 value로 Replace한다.
	* @param key String
	* @param index int
	* @param replaceValue int
	*/
	public void modifyInt(Object key, int index, int replaceValue) {
		if (!super.containsKey(key)) {
			NLog.report.println("[RuntimeException in NMultiData] Key(" + key + ") does not exist in NMultiData(" + this.name + ") ");
			throw new RuntimeException("[RuntimeException in NMultiData] Key(" + key + ") does not exist in NMultiData(" + this.name + ") ");
		}
		ArrayList arrayList = (ArrayList)this.get(key);
		int valueSize = arrayList.size();
		if( !(valueSize > index) ){
			NLog.report.println("[RuntimeException in NMultiData] Index("+ index +") of Key(" + key + ") exceeds size(" + (valueSize-1) + ") of NMultiData ");
			throw new RuntimeException("[RuntimeException in NMultiData] Index("+ index +") of Key(" + key + ") exceeds size(" + (valueSize-1) + ") of NMultiData");
		}
		arrayList.set(index, new Integer(replaceValue));
	}


	/**
	* 해당 key값에 해당하는 index번째의 Value를 파라메터로 전달된 value로 Replace한다.
	* @param key String
	* @param index int
	* @param replaceValue String
	*/
	public void modifyString(Object key, int index, String replaceValue) {
		if (!super.containsKey(key)) {
			NLog.report.println("[RuntimeException in NMultiData] Key(" + key + ") does not exist in NMultiData(" + this.name + ") ");
			throw new RuntimeException("[RuntimeException in NMultiData] Key(" + key + ") does not exist in NMultiData(" + this.name + ") ");
		}
		ArrayList arrayList = (ArrayList)this.get(key);
		int valueSize = arrayList.size();
		if( !(valueSize > index) ){
			NLog.report.println("[RuntimeException in NMultiData] Index("+ index +") of Key(" + key + ") exceeds size(" + (valueSize-1) + ") of NMultiData ");
			throw new RuntimeException("[RuntimeException in NMultiData] Index("+ index +") of Key(" + key + ") exceeds size(" + (valueSize-1) + ") of NMultiData");
		}
		arrayList.set(index, replaceValue);
	}


	/**
	* 해당 key값에 해당하는 index번째의 Value를 파라메터로 전달된 value로 Replace한다.
	* @param key String
	* @param index int
	* @param replaceValue double
	*/
	public void modifyDouble(Object key, int index, double replaceValue) {
		if (!super.containsKey(key)) {
			NLog.report.println("[RuntimeException in NMultiData] Key(" + key + ") does not exist in NMultiData(" + this.name + ") ");
			throw new RuntimeException("[RuntimeException in NMultiData] Key(" + key + ") does not exist in NMultiData(" + this.name + ") ");
		}
		ArrayList arrayList = (ArrayList)this.get(key);
		int valueSize = arrayList.size();
		if( !(valueSize > index) ){
			NLog.report.println("[RuntimeException in NMultiData] Index("+ index +") of Key(" + key + ") exceeds size(" + (valueSize-1) + ") of NMultiData ");
			throw new RuntimeException("[RuntimeException in NMultiData] Index("+ index +") of Key(" + key + ") exceeds size(" + (valueSize-1) + ") of NMultiData");
		}
		arrayList.set(index, new Double(replaceValue));
	}

	/**
	* 해당 key값에 해당하는 index번째의 Value를 파라메터로 전달된 value로 Replace한다.
	* @param key String
	* @param index int
	* @param replaceValue double
	*/
	public void modifyFloat(Object key, int index, float replaceValue) {
		if (!super.containsKey(key)) {
			NLog.report.println("[RuntimeException in NMultiData] Key(" + key + ") does not exist in NMultiData(" + this.name + ") ");
			throw new RuntimeException("[RuntimeException in NMultiData] Key(" + key + ") does not exist in NMultiData(" + this.name + ") ");
		}
		ArrayList arrayList = (ArrayList)this.get(key);
		int valueSize = arrayList.size();
		if( !(valueSize > index) ){
			NLog.report.println("[RuntimeException in NMultiData] Index("+ index +") of Key(" + key + ") exceeds size(" + (valueSize-1) + ") of NMultiData ");
			throw new RuntimeException("[RuntimeException in NMultiData] Index("+ index +") of Key(" + key + ") exceeds size(" + (valueSize-1) + ") of NMultiData");
		}
		arrayList.set(index, new Float(replaceValue));
	}

	/**
	* 해당 key값에 해당하는 index번째의 Value를 파라메터로 전달된 value로 Replace한다.
	* @param key String
	* @param index int
	* @param replaceValue long
	*/
	public void modifyLong(Object key, int index, long replaceValue) {
		if (!super.containsKey(key)) {
			NLog.report.println("[RuntimeException in NMultiData] Key(" + key + ") does not exist in NMultiData(" + this.name + ") ");
			throw new RuntimeException("[RuntimeException in NMultiData] Key(" + key + ") does not exist in NMultiData(" + this.name + ") ");
		}
		ArrayList arrayList = (ArrayList)this.get(key);
		int valueSize = arrayList.size();
		if( !(valueSize > index) ){
			NLog.report.println("[RuntimeException in NMultiData] Index("+ index +") of Key(" + key + ") exceeds size(" + (valueSize-1) + ") of NMultiData ");
			throw new RuntimeException("[RuntimeException in NMultiData] Index("+ index +") of Key(" + key + ") exceeds size(" + (valueSize-1) + ") of NMultiData");
		}
		arrayList.set(index, new Long(replaceValue));
	}

	/**
	* 해당 key값에 해당하는 index번째의 Value를 파라메터로 전달된 value로 Replace한다.
	* @param key String
	* @param index int
	* @param replaceValue short
	*/
	public void modifyShort(Object key, int index, short replaceValue) {
		if (!super.containsKey(key)) {
			NLog.report.println("[RuntimeException in NMultiData] Key(" + key + ") does not exist in NMultiData(" + this.name + ") ");
			throw new RuntimeException("[RuntimeException in NMultiData] Key(" + key + ") does not exist in NMultiData(" + this.name + ") ");
		}
		ArrayList arrayList = (ArrayList)this.get(key);
		int valueSize = arrayList.size();
		if( !(valueSize > index) ){
			NLog.report.println("[RuntimeException in NMultiData] Index("+ index +") of Key(" + key + ") exceeds size(" + (valueSize-1) + ") of NMultiData ");
			throw new RuntimeException("[RuntimeException in NMultiData] Index("+ index +") of Key(" + key + ") exceeds size(" + (valueSize-1) + ") of NMultiData");
		}
		arrayList.set(index, new Short(replaceValue));
	}

	/**
	* 해당 key값에 해당하는 index번째의 Value를 파라메터로 전달된 value로 Replace한다.
	* @param key String
	* @param index int
	* @param replaceValue boolean
	*/
	public void modifyBoolean(Object key, int index, boolean replaceValue) {
		if (!super.containsKey(key)) {
			NLog.report.println("[RuntimeException in NMultiData] Key(" + key + ") does not exist in NMultiData(" + this.name + ") ");
			throw new RuntimeException("[RuntimeException in NMultiData] Key(" + key + ") does not exist in NMultiData(" + this.name + ") ");
		}
		ArrayList arrayList = (ArrayList)this.get(key);
		int valueSize = arrayList.size();
		if( !(valueSize > index) ){
			NLog.report.println("[RuntimeException in NMultiData] Index("+ index +") of Key(" + key + ") exceeds size(" + (valueSize-1) + ") of NMultiData ");
			throw new RuntimeException("[RuntimeException in NMultiData] Index("+ index +") of Key(" + key + ") exceeds size(" + (valueSize-1) + ") of NMultiData");
		}
		arrayList.set(index, new Boolean(replaceValue));
	}

	/**
	* 해당 key값에 해당하는 index번째의 Value를 파라메터로 전달된 value로 Replace한다.
	* @param key String
	* @param index int
	* @param replaceValue BigDecimal
	*/
	public void modifyBigDecimal(Object key, int index, BigDecimal replaceValue) {
		if (!super.containsKey(key)) {
			NLog.report.println("[RuntimeException in NMultiData] Key(" + key + ") does not exist in NMultiData(" + this.name + ") ");
			throw new RuntimeException("[RuntimeException in NMultiData] Key(" + key + ") does not exist in NMultiData(" + this.name + ") ");
		}
		ArrayList arrayList = (ArrayList)this.get(key);
		int valueSize = arrayList.size();
		if( !(valueSize > index) ){
			NLog.report.println("[RuntimeException in NMultiData] Index("+ index +") of Key(" + key + ") exceeds size(" + (valueSize-1) + ") of NMultiData ");
			throw new RuntimeException("[RuntimeException in NMultiData] Index("+ index +") of Key(" + key + ") exceeds size(" + (valueSize-1) + ") of NMultiData");
		}
		arrayList.set(index, replaceValue);
	}

	/**
	* 해당 key값에 Object형의 value를 설정한다.
	* @param key String
	* @param value Object
	*/
	public void add(Object key, Object value) {
		if (!super.containsKey(key)) {
			ArrayList arrayList = new ArrayList();
			arrayList.add(value);
			super.put(key, arrayList);
		} else {
			ArrayList arrayList = (ArrayList) super.get(key);
			arrayList.add(value);
		}
	}

	/**
	* 해당 key값에 String형의 value를 설정한다.
	* @param key String
	* @param value String
	*/
	public void addString(Object key, String value) {
		if (!super.containsKey(key)) {
			ArrayList arrayList = new ArrayList();
			arrayList.add(value);
			super.put(key, arrayList);
		} else {
			ArrayList arrayList = (ArrayList) super.get(key);
			arrayList.add(value);
		}
	}

	/**
	* 해당 key값에 String형의 value를 설정한다.
	* @param key String
	* @param value int
	*/
	public void addInt(Object key, int value) {
		Integer valueInt = new Integer(value);

		if (!super.containsKey(key)) {
			ArrayList arrayList = new ArrayList();
			arrayList.add(valueInt);
			super.put(key, arrayList);
		} else {
			ArrayList arrayList = (ArrayList) super.get(key);
			arrayList.add(valueInt);
		}
	}

	/**
	* 해당 key값에 double형의 value를 설정한다.
	* @param key String
	* @param value double
	*/
	public void addDouble(Object key, double value) {
		Double valueDouble = new Double(value);

		if (!super.containsKey(key)) {
			ArrayList arrayList = new ArrayList();
			arrayList.add(valueDouble);
			super.put(key, arrayList);
		} else {
			ArrayList arrayList = (ArrayList) super.get(key);
			arrayList.add(valueDouble);
		}
	}

	/**
	* 해당 key값에 String형의 value를 설정한다.
	* @param key String
	* @param value float
	*/
	public void addFloat(Object key, float value) {
		Float valueFloat = new Float(value);

		if (!super.containsKey(key)) {
			ArrayList arrayList = new ArrayList();
			arrayList.add(valueFloat);
			super.put(key, arrayList);
		} else {
			ArrayList arrayList = (ArrayList) super.get(key);
			arrayList.add(valueFloat);
		}
	}

	/**
	* 해당 key값에 Long형의 value를 설정한다.
	* @param key String
	* @param value long
	*/
	public void addLong(Object key, long value) {
		Long valueLong = new Long(value);

		if (!super.containsKey(key)) {
			ArrayList arrayList = new ArrayList();
			arrayList.add(valueLong);
			super.put(key, arrayList);
		} else {
			ArrayList arrayList = (ArrayList) super.get(key);
			arrayList.add(valueLong);
		}
	}

	/**
	* 해당 key값에 Short형의 value를 설정한다.
	* @param key String
	* @param value short
	*/
	public void addShort(Object key, short value) {
		Short valueShort = new Short(value);

		if (!super.containsKey(key)) {
			ArrayList arrayList = new ArrayList();
			arrayList.add(valueShort);
			super.put(key, arrayList);
		} else {
			ArrayList arrayList = (ArrayList) super.get(key);
			arrayList.add(valueShort);
		}
	}

	/**
	* 해당 key값에 Boolean형의 value를 설정한다.
	* @param key String
	* @param value boolena
	*/
	public void addBoolean(Object key, boolean value) {
		Boolean valueBoolean = new Boolean(value);

		if (!super.containsKey(key)) {
			ArrayList arrayList = new ArrayList();
			arrayList.add(valueBoolean);
			super.put(key, arrayList);
		} else {
			ArrayList arrayList = (ArrayList) super.get(key);
			arrayList.add(valueBoolean);
		}
	}

    /**
    * 해당 key값에 primitive type인 BigDecimal형의 value를 설정한다.
    * @param key String
    * @param value BigDecimal
    */
    public void addBigDecimal(Object key, BigDecimal value) {
        if (!super.containsKey(key)) {
            ArrayList arrayList = new ArrayList();
            arrayList.add(value);
            super.put(key, arrayList);
        } else {
            ArrayList arrayList = (ArrayList) super.get(key);
            arrayList.add(value);
        }
    }
    /**
    * 전달받은 key값에 해당되는 BegDecimal값을 return한다.
    * 만일 Value가 존재하지 않을 경우에는 null또는 BigDecimal(0)(isNullToBlank()가 true인경우)을 return한다.
    * @param key String
    * @return BigDecimal
    */
    public BigDecimal getBigDecimal(Object key, int index) {
        Object o = getObject(key, index);
        if (o == null) {
            return new BigDecimal(0);
        } else {
            return (BigDecimal)o;
        }
    }


	private Object getObject(Object key, int index) {
		Object o = null;
		ArrayList arrayList = (ArrayList) super.get(key);

		if ( arrayList == null ) {
			if (this.nullToInitialize) {
				return null;
			} else {
				NLog.report.println("[RuntimeException in NMultiData] Key(" + key + ") does not exist in NMultiData(" + this.name + ") ");
				throw new RuntimeException("[RuntimeException in NMultiData] Key(" + key + ") does not exist in NMultiData(" + this.name + ") ");
			}
		}

		try {
			if( index >= arrayList.size()){
				return null;
			}
			o = arrayList.get(index);

		} catch ( IndexOutOfBoundsException ioe) {
			NLog.report.println("[RuntimeException in NMultiData] Index(" + index + ") in NMultiData(" + this.name + ") is out of Bounds.");
			throw new RuntimeException("[RuntimeException in NMultiData] Index(" + index + ") in NMultiData(" + this.name + ") is out of Bounds.");
		}
		return o;
	}

	/**
	* 전달받은 key값에 해당되는 Value를 Object로 return한다.
	* 만일 Value가 존재하지 않을 경우에는 null또는 빈문자렬(isNullToBlank()가 true인경우)을 return한다.
	* @param key String
	* @return Object
	*/
	public Object get(Object key, int index) {
		return getObject(key,index);
	}

	/**
	* 전달받은 key값에 해당되는 int값을 return한다.
	* 만일 Value가 존재하지 않을 경우에는 null또는 0(isNullToBlank()가 true인경우)을 return한다.
	* @param key String
	* @return int
	*/
	public int getInt(Object key, int index) {
		Object o = getObject(key, index);

		if (o == null) {
			if (isNullToInitialize())
				return 0;
			else
				throw new RuntimeException("[RuntimeException in NMultiData] Value of the Key(" + key + ") is null");

		} else {
			Class classType = o.getClass();

			if ( classType == Integer.class ){
				return ((Integer)o).intValue();
			} else if (classType == Short.class ) {
				return ((Short)o).shortValue();
			}

			if ( classType == String.class ){
				try {
					return Integer.parseInt(o.toString());
				} catch (Exception e) {
					NLog.report.println("[RuntimeException in NMultiData] Value of the Key(" + key + ") Type(int) does not match : It's type is not int");
					throw new RuntimeException("[RuntimeException in NMultiData] Value of the Key(" + key + ") Type(int) does not match : It's type is not int");
				}
			}
			NLog.report.println("[RuntimeException in NMultiData] Value of the Key(" + key + ") Type(int) does not match : It's type is not int");
			throw new RuntimeException("[RuntimeException in NMultiData] Value of the Key(" + key + ") Type(int) does not match : It's type is not int");
		}
	}

	/**
	* 전달받은 key값에 해당되는 int값을 return한다.
	* 만일 Value가 존재하지 않을 경우에는 null또는 0(isNullToBlank()가 true인경우)을 return한다.
	* @param key String
	* @return double
	*/
	public double getDouble(Object key, int index) {
		Object o = getObject(key, index);
		if (o == null) {
			if (isNullToInitialize())
				return 0;
			else
				throw new RuntimeException("[RuntimeException in NMultiData] Value of the Key(" + key + ") is null");

		} else {
			Class classType = o.getClass() ;

			if ( classType == Double.class ){
				return ((Double)o).doubleValue();
			} else if ( classType == Float.class ){
				return ((Float)o).floatValue();
			}

            if (  classType == String.class || classType == BigDecimal.class ){
				try {
					return Double.parseDouble(o.toString());
				} catch (Exception e) {
					NLog.report.println("[RuntimeException in NMultiData] Value of the Key(" + key + ") Type(double) does not match : It's type is not double");
					throw new RuntimeException("[RuntimeException in NMultiData] Value of the Key(" + key + ") Type(double) does not match : It's type is not double");
				}
			}
			NLog.report.println("[RuntimeException in NMultiData] Value of the Key(" + key + ")  Type(double) does not match : It's type is not double");
			throw new RuntimeException("[RuntimeException in NMultiData] Value of the Key(" + key + ")  Type(double) does not match : It's type is not double");
		}
	}

	/**
	* 전달받은 key값에 해당되는 int값을 return한다.
	* 만일 Value가 존재하지 않을 경우에는 null또는 0(isNullToBlank()가 true인경우)을 return한다.
	* @param key String
	* @return float
	*/
	public float getFloat(Object key, int index) {
		Object o = getObject(key, index);

		if (o == null) {
			if (isNullToInitialize())
				return (float)0.0;
			else
				throw new RuntimeException("[RuntimeException in NMultiData] Value of the Key(" + key + ") is null");
		} else {
			Class classType = o.getClass() ;

			if ( classType == Float.class  ){
				return ((Float)o).floatValue();
			}

            if (  classType == String.class || classType == BigDecimal.class ){
				try {
					return Float.parseFloat(o.toString());
				} catch (Exception e) {
					NLog.report.println("[RuntimeException in NMultiData] Value of the Key(" + key + ") Type(float) does not match : It's type is not float");
					throw new RuntimeException("[RuntimeException in NMultiData] Value of the Key(" + key + ") Type(float) does not match : It's type is not float");
				}
			}
			NLog.report.println("[RuntimeException in NMultiData] Value of the Key(" + key + ") Type(float) does not match : It's type is not float");
			throw new RuntimeException("[RuntimeException in NMultiData] Value of the Key(" + key + ") Type(float) does not match : It's type is not float");
		}
	}

	/**
	* 전달받은 key값에 해당되는 int값을 return한다.
	* 만일 Value가 존재하지 않을 경우에는 null또는 0(isNullToBlank()가 true인경우)을 return한다.
	* @param key String
	* @return long
	*/
	public long getLong(Object key, int index) {
		Object o = getObject(key, index);

		if (o == null) {
			if (isNullToInitialize())
				return 0;
			else
				throw new RuntimeException("[RuntimeException in NMultiData] Value of the Key(" + key + ") is null");
		} else {
			Class classType = o.getClass();

			if ( classType == Long.class ){
				return ((Long)o).longValue();
			} else if ( classType == Integer.class ){
				return ((Integer)o).intValue();
			} else if (classType == Short.class ) {
				return ((Short)o).shortValue();
			}

			if ( classType == String.class ){
				try {
					return Long.parseLong(o.toString());
				} catch (Exception e) {
					NLog.report.println("[RuntimeException in NMultiData] Value of the Key(" + key + ") Type(long) does not match : It's type is not long");
					throw new RuntimeException("[RuntimeException in NMultiData] Value of the Key(" + key + ") Type(long) does not match : It's type is not long");
				}
			}
			NLog.report.println("[RuntimeException in NMultiData] Value of the Key(" + key + ") Type(long) does not match : It's type is not long");
			throw new RuntimeException("[RuntimeException in NMultiData] Value of the Key(" + key + ") Type(long) does not match : It's type is not long");
		}
	}

	/**
	* 전달받은 key값에 해당되는 int값을 return한다.
	* 만일 Value가 존재하지 않을 경우에는 null또는 0(isNullToBlank()가 true인경우)을 return한다.
	* @param key String
	* @return short
	*/
	public short getShort(Object key, int index) {
		Object o = getObject(key, index);

		if (o == null) {
			if (isNullToInitialize())
				return 0;
			else
				throw new RuntimeException("[RuntimeException in NMultiData] Value of the Key(" + key + ")  is null");
		} else {
			Class classType = o.getClass();

			if ( classType == Short.class ){
				return ((Short)o).shortValue();
			}

			if ( classType == String.class ){
				try {
					return Short.parseShort(o.toString());
				} catch (Exception e) {
					NLog.report.println("Key(" + key + ")" + " Type(short) does not match : It's type is not short");
					throw new RuntimeException("[RuntimeException in NMultiData] Value of the Key(" + key + ") Type(short) does not match : It's type is not short");
				}
			}
			NLog.report.println("Key(" + key + ")" + " Type(short) does not match : It's type is not short");
			throw new RuntimeException("[RuntimeException in NMultiData] Value of the Key(" + key + ") Type(short) does not match : It's type is not short");
		}
	}

	/**
	* 전달받은 key값에 해당되는 boolean값을 return한다.
	* 만일 Value가 존재하지 않을 경우에는 null또는 0(isNullToBlank()가 true인경우)을 return한다.
	* @param key String
	* @return boolean
	*/
	public boolean getBoolean(Object key, int index) {
		Object o = getObject(key, index);

		if (o == null) {
			if (isNullToInitialize())
				return false;
			else
				throw new RuntimeException("[RuntimeException in NMultiData] Value of the Key(" + key + ") is null");
		} else {
            Class classType = o.getClass();
			if (classType == Boolean.class) {
				return ((Boolean) o).booleanValue();
			}
			if (classType == String.class) {
				try {
					return Boolean.getBoolean(o.toString());
				} catch (Exception e) {
					NLog.report.println("[RuntimeException in NMultiData] Value of the Key(" + key + ") Type(short) does not match : It's type is not short");
					throw new RuntimeException("[RuntimeException in NMultiData] Value of the Key(" + key + ") Type(short) does not match : It's type is not short");
				}
			}
			NLog.report.println("[RuntimeException in NMultiData] Value of the Key(" + key + ") Type(short) does not match : It's type is not short");
			throw new RuntimeException("[RuntimeException in NMultiData] Value of the Key(" + key + ") Type(short) does not match : It's type is not short");
		}
	}

	/**
	* 전달받은 key값에 해당되는 Value를 Object로 return한다.
	* 만일 Value가 존재하지 않을 경우에는 null또는 빈문자렬(isNullToBlank()가 truen인경우)을 return한다.
	* 내부에 getString()을 이용하여 구현되여 있다.
	* @param key String
	* @return String
	*/
	public String getString(Object key, int index) {
		Object o = getObject(key, index);

		if (o == null) {
			if (isNullToInitialize())
				return "";
			else
				return null;
		} else {
			return o.toString();
			/*  만일 String에 대해서도 엄격한 형규정을 필요로 한다면 위 줄을 지우고 아래 주석을 풀어주면 된다.
				if ( o.getClass().isInstance( new String() ) ){
					return (String)o;
				}
					throw new RuntimeException( "Value Type(String) does not match : It's type is not double" );
			*/
		}
	}

	/**
	* key의 index에 있는 값을 삭제한다.<br>
	* @param key NMultiData에 존재하는 key 값
	* @param index 특정 키 값에 존재하는 index
	* @return java.lang.Object 삭제된 객체를 반환한다.
	*/
	public Object remove(Object key, int index) {
		if (super.containsKey(key)) {
			return ((ArrayList) super.get(key)).remove(index);
		} else {
			return null;
		}
	}

	/**
	* NMultiData에 존재하는 key에 해당하는 size를 리턴한다.<br>
	* 키가 존재하지 않으면, 0을 리턴한다.
	* @return int
	*/
	public int keySize(Object key) {
		if (super.containsKey(key)) {
			return ((ArrayList) super.get(key)).size();
		} else {
			return 0;
		}
	}

	/**
	* 이 메소드는 키의 값을 전혀 알 수없을 때 사용하는 기능이다.
	* 키의 값을 전혀 알지 못하기 때문에 키의 첫번째 키를 사용하여 size를 알아낸다.
	* 키를 알고 있을때에는 keySize(Object key) 메소드를 사용하는 것이 Performance면에서 뛰어나다.
	* @return int
	*/
	public int keySize() {
		Set tempSet = super.keySet();
		Iterator iterator = tempSet.iterator();
		if (iterator.hasNext()) {
			String key = iterator.next().toString();
			return ((ArrayList) super.get(key)).size();
		} else {
			return 0;
		}
	}

	/**
	* NMultiData에 저장되여 있는 값의 index번째에 있는 값을 NData형으로 반환한다. <br>
	* 즉, 여러건의 Data를 가지고 있는 NMultiData에서 한 건씩 꺼내서 사용하고자 할때<br>
	* 사용하면 효율적이다.
	* @param index int
	* @return NData
	*/
	public NData getNData(int index) {
		NData singleData = new NData("");
		Set tempSet = super.keySet();
		Iterator iterator = tempSet.iterator();
		while (iterator.hasNext()) {
			String key = iterator.next().toString();
			//Object o = ((ArrayList) super.get(key)).get(index);
			Object o = getObject(key ,index);
			singleData.put(key, o);
		}
		return singleData;
	}

	/**
	* NMultiData에 값을 저장할때, addNData(String dataName)를  통해서 이름으로 구분하여 저장하였을 경우<BR>
	* 저장되여 있는 값의 dataname의 index번째에 있는 값을 NData형으로 반환한다. <br>
	* 즉, 이기종의 여러건의 Data를 가지고 있는 NMultiData에서 원하는 종류의 Data를<br>
	* 한 건씩 꺼내서 사용하고자 할때 사용하면 효룔적이다.
	* @param dataName String
	* @param index int
	* @return NData
	*/
	public NData getNData(String dataName, int index) {
		NData singleData = new NData(dataName);
		String prefix = dataName + ".";
		Set tempSet = super.keySet();
		Iterator iterator = tempSet.iterator();
		while (iterator.hasNext()) {
			String key = iterator.next().toString();
			int key_index = key.indexOf(".");
			String realKey = key.substring(key_index + 1);
			if (key.startsWith(prefix)) {
				//Object o = ((ArrayList) super.get(key)).get(index);
				Object o = getObject(key ,index);
				singleData.put(realKey, o);
			}
		}
		return singleData;
	}

	/**
	* NData형을 바로 NMultiData에 값을 저장할때 사용한다.
	* @param data NData
	*/
	public void addNData(NData data) {
		Set tempSet = data.keySet();
		Iterator iterator = tempSet.iterator();
		while (iterator.hasNext()) {
			Object key = iterator.next();
			if (this.containsKey(key)) {
				int field_size = ((ArrayList) this.get(key)).size();
				if (field_size != field_index) {
					for (int inx = field_size; inx < field_index; inx++) {
						this.add(key, null);
					} // end for
				} // end if
				this.add(key, data.get(key));
			} // end if
			else {
				for (int inx = 0; inx < field_index; inx++) {
					this.add(key, null);
				}
				this.add(key, data.get(key));
			} // end else
		} // end while
		field_index++;
	}

	/**
	* 이기종의NData형을 NMultiData에 값을 저장할때 사용한다.<br>
	* 즉, 서로다른 형태의 NData형을 NMultiData에 저장하기 때문에<br>
	* dataName을 각별히 주의하여서 지정하여 주어야 한다.<br>
	* 또한, dataName를 부여한 값은 가져올 때에도 getNData(dataName, index)를 통해서<br>
	* 가져와야 한다.<br>
	* @param data NData
	*/
	public void addNData(String dataName, NData data) {
		int entitySize = 0;
		if (entityKey == null) {
			entityKey = new HashMap(5);
		} else {
			if (entityKey.containsKey(dataName))
				entitySize = ((Integer) entityKey.get(dataName)).intValue();
		}

		Set tempSet = data.keySet();
		Iterator iterator = tempSet.iterator();
		while (iterator.hasNext()) {
			Object key = iterator.next();
			String dataKey = dataName + "." + key;
			//if (this.containsKey(dataName + "." + key)) {
			if (this.containsKey(dataKey)) {
				int fieldSize = ((ArrayList) this.get(dataKey)).size(); //값이 없을 때 0일까?
				if (fieldSize != entitySize) {
					for (int inx = fieldSize; inx < entitySize; inx++) {
						this.add(dataKey, null);
					} // end for
				} // end if
				this.add(dataKey, data.get(key));
			} // end if
			else {
				for (int inx = 0; inx < entitySize; inx++) {
					this.add(dataKey, null);
				}
				this.add(dataKey, data.get(key));
			} // end else
		} // end while
		entityKey.put(dataName, new Integer(entitySize + 1));
	}

//	private String makeByteString(int startIndex, int endIndex, byte[] sourceStr){
//		StringBuffer resultStr = new StringBuffer();
//		for( int inx = startIndex ; inx < endIndex ; inx++ ){
//			resultStr.append(sourceStr[inx]);
//		}
//		return resultStr.toString();
//	}


	/**
	* NMultiData에 저장되여 있는 값을 읽어서 알아보기 쉽게 <br>
	* name과 value로 문자렬을 만들어 return한다.
	* @return java.lang.String
	*/
	public String toString() {
		boolean checkLongString = true;
		StringBuffer buf = new StringBuffer();

		Set keySet = this.keySet();
		int keySize = keySet.size();
		String[] keyStr = new String[keySize];
		keySet.toArray( keyStr );
		buf.append(" [NMultiData Result]-------------");
		buf.append(NStringUtil.makeRepeatString("-", (keySize-1)*23-1));
		buf.append("|");
		buf.append("\n |{index}| ");
		int keyLoopNumber = 0;
		while(checkLongString){
			checkLongString = false;
			for( int inx = 0 ; inx < keySize ; inx++){
				int keyLength = keyStr[inx].length();
				int printKeyLength=0;
				if( keyLength > (keyLoopNumber+1)*20 ){
					printKeyLength = (keyLoopNumber+1)*20;
					checkLongString = true;
				}else{
					if( keyLoopNumber == 0 || keyLength > (keyLoopNumber*20) ){
						printKeyLength = keyLength;
					}else{
						printKeyLength = 0;
					}
				}
				if( printKeyLength != 0){
					buf.append(keyStr[inx].substring(keyLoopNumber*20, printKeyLength));
				}
				if( printKeyLength != 0 && (printKeyLength%20) == 0){
					printKeyLength = 20;
				}else{
					buf.append(NStringUtil.makeRepeatString(" ", 20-(printKeyLength%20)));
				}
				buf.append(" | ");
			}//end for
			if( checkLongString == false )
				break;
			buf.append("\n |       | ");
			keyLoopNumber++;
		}//end while
		buf.append("\n |-------------------------------");
		buf.append(NStringUtil.makeRepeatString("-", (keySize-1)*23-1));
		buf.append("|");


		int rowSize = 0;
		for( int inx = 0 ; inx < keySize ; inx++ ) {
			int rowSizeOfKey = this.keySize(keyStr[inx]);
			if( rowSizeOfKey > rowSize ){
				rowSize = rowSizeOfKey;
			}
		}

		for( int inx = 0 ; inx < rowSize ; inx++ ){
			buf.append("\n |  ");
			String indexStr = ""+ inx;
			buf.append(indexStr);
			buf.append(NStringUtil.makeRepeatString(" ", 5-indexStr.length()));
			buf.append("| ");
			checkLongString = true;
			int lineLoopNumber = 0;
			while(checkLongString){
					checkLongString = false;
					for( int jnx = 0 ; jnx < keySize ; jnx++){
					String tmpValue = this.getString( keyStr[jnx], inx );
					if( tmpValue == null )
						tmpValue = "null";
					int[] uniCode = NUnicodeUtil.getUnicodeLineArray(tmpValue, 20);

					int valueLength = tmpValue.getBytes().length;
					int printValueLength=0;
					int lastUnicodeNumber = 0;
					int beforeLastUnicodeNumber = 0;
					String printString = "";
					for( int knx = 0 ; knx < uniCode.length ; knx++ ){
						if( uniCode[knx] <= lineLoopNumber+1 )
							lastUnicodeNumber++;
					}
					for( int knx = 0 ; knx < uniCode.length ; knx++ ){
						if( uniCode[knx] <= lineLoopNumber )
							beforeLastUnicodeNumber++;
					}
					if( valueLength+lastUnicodeNumber > (lineLoopNumber+1)*20 ){
						printValueLength = (lineLoopNumber+1)*20 - lastUnicodeNumber;
						checkLongString = true;
						if( lineLoopNumber == 0 ){
							printString = new String(tmpValue.getBytes(), lineLoopNumber*20, printValueLength-lineLoopNumber*20);
							buf.append( printString );
						}

						else{
							printValueLength += beforeLastUnicodeNumber;
							printString = new String(tmpValue.getBytes(), lineLoopNumber*20-beforeLastUnicodeNumber, printValueLength-lineLoopNumber*20);
							buf.append( printString );
						}
					}else{
						if( lineLoopNumber == 0 || valueLength+lastUnicodeNumber > (lineLoopNumber*20) ){
							printValueLength = valueLength;
							if( lineLoopNumber == 0 ){
								printString = new String(tmpValue.getBytes(), lineLoopNumber*20, printValueLength-lineLoopNumber*20+lastUnicodeNumber);
								buf.append( printString );
							}else{
								printValueLength += lastUnicodeNumber;
								printString = new String(tmpValue.getBytes(), lineLoopNumber*20-lastUnicodeNumber, printValueLength-lineLoopNumber*20);
								buf.append( printString );
							}
						}else{
							printValueLength = 0;
						}
					}//end else

					if( printValueLength != 0 && (printValueLength%20) == 0){
						printValueLength = 20;
						int unicodeNumber = NUnicodeUtil.countUnicode(printString);
						buf.append(NStringUtil.makeRepeatString(" ", unicodeNumber));
					}else{
						int unicodeNumber = NUnicodeUtil.countUnicode(printString);
						buf.append(NStringUtil.makeRepeatString(" ", unicodeNumber));
						if( printString.length() != 0 && NUnicodeUtil.isUnicode( printString.charAt(printString.length()-1) ) )
							buf.append(NStringUtil.makeRepeatString(" ", 20-((printValueLength)%20)));
						buf.append(NStringUtil.makeRepeatString(" ", 20-((printValueLength)%20)));
					}
					buf.append(" | ");
				}//end for
				if( checkLongString == false )
					break;
				buf.append("\n |       | ");
				lineLoopNumber++;
			}//end while

			buf.append("\n |-------------------------------");
			buf.append(NStringUtil.makeRepeatString("-", (keySize-1)*23-1));
			buf.append("|");
		}
		buf.append("\n [Total Row Size] = ");
		buf.append("" + rowSize);

		return buf.toString();
	}
}