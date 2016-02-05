package nsf.support.collection;

/**
 * @(#) NData.java
 * 
 */

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.math.BigDecimal;
import nsf.core.log.NLog;


/**
 * <pre>
 * 이 Class는 NAF에서 Data Protocol로 사용되며, Data의 관리를 손쉽게 할 수 있도록 도와준다.<BR> 
 * 대표적인 례로 HttpServletRequest로 받은 Data를 가지고 있는<BR> 
 * request의 name과 value를  Key와 Value로 사용하여 효률적으로 관리를 하고 있으며,<br>
 * 또한 Value Object의 단점을 보충하여 좀 더 편리하게 사용할수 있도록 만들어진 Class이다.
 * </pre>
 * 
 * @since 2007년 1월 5일
 * @version NSF 1.0
 * 
 * @author 강영진, Nova China 
 */

public class NData extends NDataProtocol {

	
	/**
	 * Constructor for NDataProtocol.
	 * @param initialCapacity
	 * @param loadFactor
	 */
	public NData(String name) {
		this.name = name;
	}

	/**
	 * Constructor for NDataProtocol.
	 * @param initialCapacity
	 * @param loadFactor
	 */
	public NData(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	/**
	 * Constructor for NDataProtocol.
	 * @param initialCapacity
	 */
	public NData(int initialCapacity) {
		super(initialCapacity);
	}

	/**
	 * Constructor for NDataProtocol.
	 */
	public NData() {
		super();
	}

	/**
	 * Constructor for NDataProtocol.
	 * @param m
	 */
	public NData(Map m) {
		super(m);
	}

	/**
	 * Constructor for NDataProtocol.
	 * @param initialCapacity
	 * @param loadFactor
	 * @param accessOrder
	 */
	public NData(
		int initialCapacity,
		float loadFactor,
		boolean accessOrder) {
		super(initialCapacity, loadFactor, accessOrder);
	}

	/** 
    * 해당 key값에 Object형의 value를 설정한다.
    * @param key String
    * @param value Object
    */
	public void set(Object key, Object value) {
		super.put(key, value);
	}
	
	/** 
    * 해당 key값에 Object형의 value를 설정한다.
    * @param key String
    * @param value Object
    */
	public void setString(Object key, String value) {
		super.put(key, value);
	}	

	/** 
	 * 해당 key값에 primitive type인 int형의 value를 설정한다.
	 * @param key String
	 * @param value int
	 */
	public void setInt(Object key, int value) {
		Integer integer = new Integer(value);
		super.put(key, integer);
	}
	
	/** 
	 * 해당 key값에 primitive type인 double형의 value를 설정한다.
	 * @param key String
	 * @param value double
	 */
	public void setDouble(Object key, double value) {
		Double dou = new Double(value);
		super.put(key, dou);
	}
	
	/** 
	 * 해당 key값에 primitive type인 float형의 value를 설정한다.
	 * @param key String
	 * @param value float
	 */
	public void setFloat(Object key, float value) {
		Float flo = new Float(value);
			super.put(key, flo);
	}
	
	/** 
	 * 해당 key값에 primitive type인 long형의 value를 설정한다.
	 * @param key String
	 * @param value long
	 */
	public void setLong(Object key, long value) {
		Long lon = new Long(value);
		super.put(key, lon);
	}
	
	/** 
	 * 해당 key값에 primitive type인 short형의 value를 설정한다.
	 * @param key String
	 * @param value short
	 */
	public void setShort(Object key, short value) {
		Short shor = new Short(value);
		super.put(key, shor);
	}	
	
	/** 
	 * 해당 key값에 primitive type인 boolean형의 value를 설정한다.
	 * @param key String
	 * @param value boolean
	 */
	public void setBoolean(Object key, boolean value) {
		Boolean bool = new Boolean(value);
		super.put(key, bool);
	}
	
    /** 
    * 해당 key값에 primitive type인 BigDecimal형의 value를 설정한다.
    * @param key String
    * @param value BigDecimal
    */
    public void setBigDecimal(Object key, BigDecimal value) {
        super.put(key, value);
    }
    
	/** 
    * 해당 key값에 Object형의 value를 설정한다.
    * @param key String
    * @param value Object
    * @deprecated
    */
	public void putString(Object key, String value) {
		super.put(key, value);
	}	
	
	/** 
    * 해당 key값에 primitive type인 int형의 value를 설정한다.
    * @param key String
    * @param value int
    * @deprecated
    */
	public void putInt(Object key, int value) {
		Integer integer = new Integer(value);
		super.put(key, integer);
	}
			
	/** 
    * 해당 key값에 primitive type인 double형의 value를 설정한다.
    * @param key String
    * @param value double
    * @deprecated
    */
	public void putDouble(Object key, double value) {
		Double dou = new Double(value);
		super.put(key, dou);
	}	

	/** 
    * 해당 key값에 primitive type인 float형의 value를 설정한다.
    * @param key String
    * @param value float
    * @deprecated
    */
	public void putFloat(Object key, float value) {
		Float flo = new Float(value);
		super.put(key, flo);
	}				

	/** 
    * 해당 key값에 primitive type인 long형의 value를 설정한다.
    * @param key String
    * @param value long
    * @deprecated
    */
	public void putLong(Object key, long value) {
		Long lon = new Long(value);
		super.put(key, lon);
	}	
	
	/** 
    * 해당 key값에 primitive type인 short형의 value를 설정한다.
    * @param key String
    * @param value short
    * @deprecated
    */
	public void putShort(Object key, short value) {
		Short shor = new Short(value);
		super.put(key, shor);
	}	


	
	/** 
    * 해당 key값에 primitive type인 boolean형의 value를 설정한다.
    * @param key String
    * @param value boolean
    * @deprecated
    */
	public void putBoolean(Object key, boolean value) {
		Boolean bool = new Boolean(value);
		super.put(key, bool);
	}
	
    /** 
    * 해당 key값에 primitive type인 BigDecimal형의 value를 설정한다.
    * @param key String
    * @param value BigDecimal
    * @deprecated
    */    
    public void putBigDecimal(Object key, BigDecimal value) {
        super.put(key, value);
    }
    
    /** 
    * 전달받은 key값에 해당되는 BegDecimal값을 return한다.
    * 만일 Value가 존재하지 않을 경우에는 null또는 BigDecimal(0)(isNullToBlank()가 true인경우)을 return한다.
    * @param key String
    * @return BigDecimal
    */
    public BigDecimal getBigDecimal(Object key) {
        
        Object o = get(key);
        if (o == null) {
            return new BigDecimal(0);
        } else {
            return (BigDecimal)o;
        }
    }
    
    /** 
    * 전달받은 key값에 해당되는 Value를 Object로 return한다.
    * 만일 Value가 존재하지 않을 경우에는 null또는 빈문자렬(nullToInitialize가 true인경우)을 return한다.
    * @param key String
    * @return Object
    */
	public Object get( Object key ) {
			Object o = super.get(key);
			
			if ( o == null  ) {
				if(  this.nullToInitialize ) {
					return "";	
				}else{
					return null;
				}
			}else{
				return o;
			}
	}
				

    /** 
    * 전달받은 key값에 해당되는 int값을 return한다.
    * 만일 Value가 존재하지 않을 경우에는 null또는 0(nullToInitialize가 true인경우)을 return한다.
    * @param key String
    * @return int
    */				
	public int getInt( Object key ) {
		Object o = super.get(key);
		
			if ( o == null  ) {
				if( this.nullToInitialize ) {
					return 0;
				}else{
					NLog.report.println("[RuntimeException in NData] Key(" + key +") does not exist in " + this.name + " NData or Key(" + key +")'s value is null."  );
					throw new RuntimeException( "[RuntimeException in NData] Key(" + key +") does not exist in " + this.name + " NData or Key(" + key +")'s value is null."  );
				}
			}else{
				Class classType = o.getClass() ;				
				if ( classType == Integer.class ){
					return ((Integer)o).intValue();
				} else if (classType == Short.class ) {
					return ((Short)o).shortValue();
				}				

				if ( classType == String.class ){
					try{
						return Integer.parseInt( o.toString() );
					}catch(Exception e){
						NLog.report.println("[RuntimeException in NData] Key(" +key+")" + " Type(int) does not match : It's type is not int."  );											
						throw new RuntimeException( "[RuntimeException in NData] Value Type(int) does not match : It's type is not int." );						
					}
				}
				NLog.report.println("[RuntimeException in NData] Key(" +key+")" + " Type(int) does not match : It's type is not int."  );															
				throw new RuntimeException( "[RuntimeException in NData] Value Type(int) does not match : It's type is not int." );
			}
	}
	
    /** 
    * 전달받은 key값에 해당되는 double값을 return한다.
    * 만일 Value가 존재하지 않을 경우에는 null또는 0.0(nullToInitialize가 true인경우)을 return한다.
    * @param key String
    * @return double
    */				
	public double getDouble( Object key ){
		Object o = super.get(key);
		
			if ( o == null  ) {
				if( this.nullToInitialize ) {
					return 0.0;
				}else{
					NLog.report.println("[RuntimeException in NData] Key(" + key +") does not exist in " + this.name + " NData or Key(" + key +")'s value is null."  );
					throw new RuntimeException( "[RuntimeException in NData] Key(" + key +") does not exist in " + this.name + " NData or Key(" + key +")'s value is null."  );
				}
			}else{
				
				Class classType = o.getClass() ;
				
				if ( classType == Double.class ){
					return ((Double)o).doubleValue();
				} else if ( classType == Float.class ){
					return ((Float)o).floatValue();
				} 
				if (  classType == String.class || classType == BigDecimal.class ){
					try{
						return Double.parseDouble( o.toString() );
					}catch(Exception e){
						NLog.report.println("[RuntimeException in NData] Key(" +key+")" + " Type(double) does not match : It's type is not double."  );											
						throw new RuntimeException( "[RuntimeException in NData] Value Type(double) does not match : It's type is not double." );						
					}
				}				
				NLog.report.println("[RuntimeException in NData] Key(" +key+")" + " Type(double) does not match : It's type is not double."  );															
				throw new RuntimeException( "[RuntimeException in NData] Value Type(double) does not match : It's type is not double." );
			}
	}	

    /** 
    * 전달받은 key값에 해당되는 float값을 return한다.
    * 만일 Value가 존재하지 않을 경우에는 null또는 0.0(nullToInitialize가 true인경우)을 return한다.
    * @param key String
    * @return float
    */				
	public float getFloat( Object key ){
		Object o = super.get(key);
		
			if ( o == null  ) {
				if( this.nullToInitialize ) {
					return (float)0.0;
				}else{
					NLog.report.println("[RuntimeException in NData] Key(" + key +") does not exist in " + this.name + " NData or Key(" + key +")'s value is null."  );
					throw new RuntimeException( "[RuntimeException in NData] Key(" + key +") does not exist in " + this.name + " NData or Key(" + key +")'s value is null."  );
				}
			}else{
				
				Class classType = o.getClass() ;
				
				if ( classType == Float.class  ){
					return ((Float)o).floatValue();
				}
                
				if ( classType == String.class || classType == BigDecimal.class ){
					try{
						return Float.parseFloat( o.toString() );
					}catch(Exception e){
						NLog.report.println("[RuntimeException in NData] Key(" +key+")" + " Type(float) does not match : It's type is not float."  );											
						throw new RuntimeException( "[RuntimeException in NData] Value Type(float) does not match : It's type is not float." );						
					}
				}				
				NLog.report.println("[RuntimeException in NData] Key(" +key+")" + " Type(float) does not match : It's type is not float."  );															
				throw new RuntimeException( "[RuntimeException in NData] Value Type(float) does not match : It's type is not float." );
			}
	}		
	
    /** 
    * 전달받은 key값에 해당되는 Value를 long로 return한다.
    * 만일 Value가 존재하지 않을 경우에는 null또는 0(nullToInitialize가 true인경우)을 return한다.
    * 내부에 getString()을 리용하여 구현되여 있다.
    * @param key String
    * @return long
    */				
	public long getLong( Object key ) {
		Object o = super.get(key);
		
			if ( o == null  ) {
				if( this.nullToInitialize ) {
					return 0;
				}else{
					NLog.report.println("[RuntimeException in NData] Key(" + key +") does not exist in " + this.name + " NData or Key(" + key +")'s value is null."  );
					throw new RuntimeException( "[RuntimeException in NData] Key(" + key +") does not exist in " + this.name + " NData or Key(" + key +")'s value is null."  );
				}
			}else{
				Class classType = o.getClass();
				
				if ( classType == Long.class ){
					return ((Long)o).longValue();
				} else if ( classType == Integer.class ){
					return ((Integer)o).intValue();
				} else if (classType == Short.class ) {
					return ((Short)o).shortValue();
				}			
				
				if ( classType == String.class ){
					try{
						return Long.parseLong( o.toString() );
					}catch(Exception e){
						NLog.report.println("[RuntimeException in NData] Key(" +key+")" + " Type(long) does not match : It's type is not long."  );											
						throw new RuntimeException( "[RuntimeException in NData] Value Type(long) does not match : It's type is not long." );						
					}
				}				
				NLog.report.println("[RuntimeException in NData] Key(" +key+")" + " Type(long) does not match : It's type is not long."  );															
				throw new RuntimeException( "[RuntimeException in NData] Value Type(long) does not match : It's type is not long." );
			}
	}

	
	/** 
	    * 전달받은 key값에 해당되는 Value를 short으로 return한다.
	    * 만일 Value가 존재하지 않을 경우에는 Exception 또는 0( nullToInitialize가 true인경우)을 return한다.
	    * 내부에 getString()을 리용하여 구현되여 있다.
	    * @param key String
	    * @return short
	    */
	public short getShort( Object key ) {
		Object o = super.get(key);
		
			if ( o == null  ) {
				if( this.nullToInitialize ) {
					return 0;
				}else{
					NLog.report.println("[RuntimeException in NData] Key(" + key +") does not exist in " + this.name + " NData or Key(" + key +")'s value is null."  );
					throw new RuntimeException( "[RuntimeException in NData] Key(" + key +") does not exist in " + this.name + " NData or Key(" + key +")'s value is null."  );
				}
			}else{
				
				Class classType = o.getClass();
				
				if ( classType == Short.class ){
					return ((Short)o).shortValue();
				}

				if ( classType == String.class ){
					try{
						return Short.parseShort( o.toString() );
					}catch(Exception e){
						NLog.report.println("[RuntimeException in NData] Key(" +key+")" + " Type(short) does not match : It's type is not short."  );
						throw new RuntimeException( "[RuntimeException in NData] Value Type(short) does not match : It's type is not short." );
					}
				}
				NLog.report.println("[RuntimeException in NData] Key(" +key+")" + " Type(short) does not match : It's type is not short."  );
				throw new RuntimeException( "[RuntimeException in NData] Value Type(short) does not match : It's type is not short." );
			}
	}	
	
	
	/** 
	    * 전달받은 key값에 해당되는 Value를 boolean으로 return한다.
	    * 만일 Value가 존재하지 않을 경우에는 Exception 또는 false( nullToInitialize가 true인경우)을 return한다.
	    * 내부에 getString()을 리용하여 구현되여 있다.
	    * @param key String
	    * @return boolean
	    */
	public boolean getBoolean( Object key ) {
		Object o = super.get(key);
		
			if ( o == null  ) {
				if( this.nullToInitialize ) {
					return false;
				}else{
					NLog.report.println("[RuntimeException in NData] Key(" + key +") does not exist in " + this.name + " NData or Key(" + key +")'s value is null."  );
					throw new RuntimeException( "[RuntimeException in NData] Key(" + key +") does not exist in " + this.name + " NData or Key(" + key +")'s value is null."  );
				}
			}else{
				if ( o.getClass().isInstance( new Boolean(true) ) ){
					return ((Boolean)o).booleanValue();
				}
				
				if ( o.getClass().isInstance( new String() ) ){
					try{
						return Boolean.valueOf( o.toString() ).booleanValue();
					}catch(Exception e){
						NLog.report.println("[RuntimeException in NData] Key(" +key+")" + " Type(boolean) does not match : It's type is not boolean."  );
						throw new RuntimeException( "[RuntimeException in NData] Value Type(boolean) does not match : It's type is not boolean." );
					}
				}
				NLog.report.println("[RuntimeException in NData] Key(" +key+")" + " Type(boolean) does not match : It's type is not boolean."  );
				throw new RuntimeException( "[RuntimeException in NData] Value Type(boolean) does not match : It's type is not boolean." );
			}
	}	
	
    /** 
    * 전달받은 key값에 해당되는 Value를 Object로 return한다.
    * 만일 Value가 존재하지 않을 경우에는 null또는 빈문자렬(nullToInitialize가 truen인경우)을 return한다.
    * 내부에 getString()을 리용하여 구현되여 있다.
    * @param key String
    * @return String
    */					
	public String getString( Object key ) {
		Object o = super.get(key);
		
			if ( o == null  ) {
				if(  this.nullToInitialize ) {
					return "";
				}else{
					return null;
				}
			}else{
				return o.toString();
			}
	}	
	
	/**
    * HttpServletRequest의 request로 만든 HashMap을 읽어서
    * name과 value로 문자렬을 만들어 return한다.
    * @return java.lang.String
    */
	public String toString() {
		int max = super.size() - 1;
		StringBuffer buf = new StringBuffer();

		Set keySet = super.keySet();
		Iterator keys = keySet.iterator();

		buf.append("\t-----------------[NData Result]------------------");
		buf.append("\n\t\t   KEY\t\t|\t  VALUE");
		buf.append("\n\t-------------------------------------------------");

		for (int i = 0; i <= max; i++) {
			Object o = keys.next();
			if (o == null) {
				buf.append( "" );
			} else {
				String str = o.toString();
				if( str.length() < 6 ){
					buf.append("\n\t  " + o + "\t\t\t|    " + this.getString(o) );
				}else if( str.length() < 14 ){
						buf.append("\n\t  " + o + "\t\t|    " + this.getString(o) );
				} else if(str.length() < 22 ){
					buf.append("\n\t  " + o + "\t|    " + this.getString(o) );
				}else {
					buf.append("\n\t  " + o + "|    " + this.getString(o) );
				}
			}//else if
		}// end for
		buf.append("\n\t-------------------------------------------------");
		return buf.toString();
    }
}