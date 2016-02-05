/**
 * @(#) CastUtil.java
 *
 */
package nsf.support.tools.util;
import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * <pre>
 * Data를 형변환하기 위한 Class이다.
 *
 * 임의의 Data를 다른 Type의 Data로 변환하기 위해서 사용된다.
 * 중요한 특징으로 Null값을 값을 반환하지 않는다.
 * 어떠한 형이던지 다른 임의의 형으로 변환할 수 있으며 단지.
 * 의미적으로 "abc"문자렬을 수자로 변환하면 무조건 0을 return한다.
 * 그러나 "123"은 자동으로 맞춰준다.
 * 단 입력 Format에 주의할 것은 날짜관련 Data인데...
 * 문자렬을 Date형태로 변환하고자할 경우에
 *
 *     java.sql.Date은 "YYYY-MM-DD"
 *     java.sql.Time은 "HH:MM:SS"
 *     java.sql.Timestamp은 "YYYY-MM-DD HH:MM:SS.SSS"
 *
 * 형태로 맞추어야 한다.
 *
 *     ※문자렬을 날짜Type 변환 예
 *
 * 	"2002-11-23 11:33:02.001" -> Date(2002-11-23)
 * 	"2002-11-23 11:33:02.001" -> Time(11:33:02)
 * 	"2002-11-23 11:33:02.001" -> Timestamp(2002-11-23 11:33:02.001)
 *
 * 	"2002-11-23"              -> Date(2002-11-23)
 * 	"2002-11-23"              -> Time(09:00:00)
 * 	"2002-11-23"              -> Timestamp(2002-11-23 09:00:00.0)
 *
 * 	"11:33:02.001"            -> Date(1970-01-01)
 * 	"11:33:02.001"            -> Time(11:33:02)
 * 	"11:33:02.001"            -> Timestamp(1970-01-01 09:00:00.0)
 * </pre>
 *
 * @since 2007년 1월 5일
 * @version NSF 1.0
 * 
 * @author 강영진, Nova China 
 */
public class CastUtil {
    protected Object value = null;
    private boolean noDefaultCast = false;
    /**
     * Cast도중에 오유가 발생하면 default로 값을 변환하여 return한다.
     * @param value
     */
    public CastUtil(Object value) {
        this.value = value;
    }
    public CastUtil(short value) {
        this.value = new Short(value);
    }

    public CastUtil(int value) {
        this.value = new Integer(value);
    }

    public CastUtil(long value) {
        this.value = new Long(value);
    }

    public CastUtil(float value) {
        this.value = new Float(value);
    }

    public CastUtil(double value) {
        this.value = new Double (value);
    }

    public CastUtil(boolean value) {
        this.value = new Boolean(value);
    }

/**
     * noDefault = true이면
     * 입력 값에 대한 Conversion시 오유가 발생하면 default로 변화하지 않고
     * XRuntimeException을 발생한다.
     * 만약 noDefaultCast = false이면
     * Cast(Object)와 동일하게 동작한다.
     * @param value
     * @param noDefaultCast
     */
    public CastUtil(Object value, boolean noDefaultCast) {
        this(value);
        this.noDefaultCast = noDefaultCast;
    }
    /**
    * 입력값을 int로 변환하여 반환한다.
    * 만약 변환중 오유가 발생하거나 null이면 0을 return한다.
    * 다만 생성자에서 noDefault = true이면서 오유이면 XRuntimeException
    * @return int
    */
    public int cint() {
        int out = 0;
        if (value == null) {
            if (noDefaultCast)
                throw new RuntimeException("clong cast exception : " + value);
            out = 0;
        } else if (value instanceof Number) {
            out = ((Number) value).intValue();
        } else {
            try {
                out = Integer.parseInt(value.toString().trim());
            } catch (Exception e) {
                if (noDefaultCast)
                    throw new RuntimeException("cint cast exception : " + value);
            }
        }
        return out;
    }


	public boolean cboolean() {
	    boolean out = false;
        if (value == null) {
            if (noDefaultCast) {
                throw new RuntimeException( "cboolean cast exception : " + value);
            }
            out = false;

        } else if (value instanceof Boolean) {
            out = ((Boolean) value).booleanValue();
        } else {
            try {
                out = new Boolean(value.toString().trim()).booleanValue();
            } catch (Exception e) {
                if (noDefaultCast)
                    throw new RuntimeException( "cboolean cast exception : " + value);
            }
        }
        return out;

    }
    /**
     * 입력값을 Integer로 변환하여 반환한다.
     * 만약 변환중 오유가 발생하거나 null이면 new Integer(0)을 return한다.
     * 단지 생성자에서 noDefault = true이면서 오유이면 XRuntimeException
      * @return Integer
     */
    public Integer cInteger() {
        Integer out = null;
        if (value == null) {
            if (noDefaultCast)
                return null;
            out = new Integer(0);
        } else if (value instanceof Integer) {
            out = (Integer) value;
        } else if (value instanceof Number) {
            out = new Integer(((Number) value).intValue());
        } else {
            try {
                out = new Integer(value.toString().trim());
            } catch (Exception e) {
                if (noDefaultCast)
                    throw new RuntimeException("cInteger cast exception : " + value);
                else
                    out = new Integer(0);
            }
        }
        return out;
    }
    /**
     * 입력값을 long으로 변환하여 반환한다.
     * 만약 변환중 오유가 발생하거나 null이면 0을 return한다.
     * 다만 생성자에서 noDefault = true이면서 오유이면 XRuntimeException
     * @return long
     */
    public long clong() {
        long out = 0;
        if (value == null) {
            if (noDefaultCast)
                throw new RuntimeException("clong cast exception : " + value);
            ;
            out = 0;
        } else if (value instanceof Number) {
            out = ((Number) value).longValue();
        } else {
            try {
                out = Long.parseLong(value.toString().trim());
            } catch (Exception e) {
                if (noDefaultCast)
                    throw new RuntimeException("clong cast exception : " + value);
            }
        }
        return out;
    }
    /**
     * 입력값을 Long으로 변환하여 반환한다.
     * 만약 변환중 오유가 발생하거나 null이면 new Long(0)을 return한다.
     * 다만 생성자에서 noDefault = true이면서 오유이면 XRuntimeException
     * @return Long
     */
    public Long cLong() {
        Long out = null;
        if (value == null) {
            if (noDefaultCast)
                return null;
            out = new Long(0);
        } else if (value instanceof Long) {
            out = (Long) value;
        } else if (value instanceof Number) {
            out = new Long(((Number) value).longValue());
        } else {
            try {
                out = new Long(value.toString().trim());
            } catch (Exception e) {
                if (noDefaultCast)
                    throw new RuntimeException("cLong cast exception : " + value);
                else
                    out = new Long(0);
            }
        }
        return out;
    }
    /**
     * 입력값을 float로 변환하여 반환한다.
     * 만약 변환중 오유가 발생하거나 null이면 0을 return한다.
     * 다만)생성자에서 noDefault = true이면서 오유이면 XRuntimeException
     * @return float
     */
    public float cfloat() {
        float out = 0;
        if (value == null) {
            if (noDefaultCast)
                throw new RuntimeException("clong cast exception : " + value);
            ;
            out = 0;
        } else if (value instanceof Number) {
            out = ((Number) value).floatValue();
        } else {
            try {
                out = Float.parseFloat(value.toString().trim());
            } catch (Exception e) {
                if (noDefaultCast)
                    throw new RuntimeException("cfloat cast exception : " + value);
            }
        }
        return out;
    }
    /**
     * 입력값을 Float로 변환하여 반환한다.
     * 만약 변환중 오유가 발생하거나 null이면 new Float(0)을 return한다.
      * 다만)생성자에서 noDefault = true이면서 오유이면 XRuntimeException
    * @return Float
     */
    public Float cFloat() {
        Float out = null;
        if (value == null) {
            if (noDefaultCast)
                return null;
            out = new Float(0);
        } else if (value instanceof Float) {
            out = (Float) value;
        } else if (value instanceof Number) {
            out = new Float(((Number) value).floatValue());
        } else {
            try {
                out = new Float(value.toString().trim());
            } catch (Exception e) {
                if (noDefaultCast)
                    throw new RuntimeException("cFloat cast exception : " + value);
                else
                    out = new Float(0);
            }
        }
        return out;
    }
    /**
     * 입력값을 double로 변환하여 반환한다.
     * 만약 변환중 오유가 발생하거나 null이면 0을 return한다.
     * 다만)생성자에서 noDefault = true이면서 오유이면 XRuntimeException
     * @return double
     */
    public double cdouble() {
        double out = 0;
        if (value == null) {
            if (noDefaultCast)
                throw new RuntimeException("clong cast exception : " + value);
            ;
            out = 0;
        } else if (value instanceof Number) {
            out = ((Number) value).doubleValue();
        } else {
            try {
                out = Double.parseDouble(value.toString().trim());
            } catch (Exception e) {
                if (noDefaultCast)
                    throw new RuntimeException("cdouble cast exception : " + value);
            }
        }
        return out;
    }
    /**
     * 입력값을 Double로 변환하여 반환한다.
     * 만약 변환중 오유가 발생하거나 null이면 new Double(0)을 return한다.
     * 다만)생성자에서 noDefault = true이면서 오유이면 XRuntimeException
     * @return Double
     */
    public Double cDouble() {
        Double out = null;
        if (value == null) {
            if (noDefaultCast)
                return null;
            out = new Double(0);
        } else if (value instanceof Double) {
            out = (Double) value;
        } else if (value instanceof Number) {
            out = new Double(((Number) value).doubleValue());
        } else {
            try {
                out = new Double(value.toString().trim());
            } catch (Exception e) {
                if (noDefaultCast)
                    throw new RuntimeException("cDouble cast exception : " + value);
                else
                    out = new Double(0);
            }
        }
        return out;
    }
    /**
     * 입력값을 BigDecimal로 변환하여 반환한다.
     * 만약 변환중 오유가 발생하거나 null이면 new BigDecimal(0)을 return한다.
       * 다만)생성자에서 noDefault = true이면서 오유이면 XRuntimeException
    * @return BigDecimal
     */
    public BigDecimal cBigDecimal() {
        BigDecimal out = null;
        if (value == null) {
            if (noDefaultCast)
                return null;
            out = new BigDecimal(0);
        } else if (value instanceof BigDecimal) {
            out = (BigDecimal) value;
        } else if (value instanceof Double) {
            out = new BigDecimal(((Double) value).doubleValue());
        } else {
            try {
                out = new BigDecimal(value.toString().trim());
            } catch (Exception e) {
                if (noDefaultCast)
                    throw new RuntimeException("cBigDecimal cast exception : " + value);
                else
                    out = new BigDecimal(0);
            }
        }
        return out;
    }
    /**
     * 입력값을 String으로 변환하여 반환한다.
     * 만약 변환중 오유가 발생하거나 null이면 ""을 return한다.
      * 다만)생성자에서 noDefault = true이면서 오유이면 XRuntimeException
    * @return String
     */
    public String cString() {
        String out = null;
        if (value == null) {
            if (noDefaultCast)
                return null;
            out = "";
        } else if (value instanceof Double) {
            out = new DecimalFormat("#0.0#################").format((Double) value);
        } else {
            if (noDefaultCast)
                throw new RuntimeException("cString cast exception : " + value);
            else
                out = value.toString();
        }
        return out;
    }

    /**
     * 입력값을 java.sql.Date으로 변환하여 반환한다.
     * 만약 변환중 오유가 발생하거나 null이면 new java.sql.Date(0)을 return한다.
     * 다만)생성자에서 noDefault = true이면서 오유이면 XRuntimeException
     * @return Date
     */
    public java.sql.Date cDate() {
        java.sql.Date out = null;
        if (value == null) {
            if (noDefaultCast)
                return null;
            out = new java.sql.Date(0);
        } else if (value instanceof Number) {
            out = new java.sql.Date(((Number) value).longValue());
        } else if (value instanceof java.util.Date) {
            out = new java.sql.Date(((java.util.Date) value).getTime());
        } else {
            try {
                String v = value.toString().trim();
                //포멧 yyyy-mm-dd
                if (v.length() == 8) {
                	v = v.substring(0,4)+"-" + v.substring(4, 6)+"-"+ v.substring(6,8);
                } else if (v.length() > 10) {
                    v = v.substring(0, 10);
                }
                out = java.sql.Date.valueOf(v);
            } catch (Exception e) {
                if (noDefaultCast)
                    throw new RuntimeException("cDate cast exception : " + value);
                else
                    out = new java.sql.Date(0);
            }
        }
        return out;
    }
    /**
     * 입력값을 java.sql.Time으로 변환하여 반환한다.
     * 만약 변환중 오유가 발생하거나 null이면 new java.sql.Time(0)을 return한다.
     * 다만)생성자에서 noDefault = true이면서 오유이면 XRuntimeException
     * @return Time
     */
    public java.sql.Time cTime() {
        java.sql.Time out = null;
        if (value == null) {
            if (noDefaultCast)
                return null;
            out = new java.sql.Time(0);
        } else if (value instanceof Number) {
            out = new java.sql.Time(((Number) value).longValue());
        } else if (value instanceof java.util.Date) {
            out = new java.sql.Time(((java.util.Date) value).getTime());
        } else {
            try {
                String v = value.toString().trim();
                //포멧 hh:mm:ss
                if (v.length() >= 19)
                    v = v.substring(11, 19);
                else if (v.length() > 8)
                    v = v.substring(0, 8);
                out = java.sql.Time.valueOf(v);
            } catch (Exception e) {
                if (noDefaultCast)
                    throw new RuntimeException("cTime cast exception : " + value);
                else
                    out = new java.sql.Time(0);
            }
        }
        return out;
    }
    /**
     * 입력값을 java.sql.Timestamp으로 변환하여 반환한다.
     * 만약 변환중 오유가 발생하거나 null이면 new java.sql.Timestamp(0) 을 return한다.
     * 다만)생성자에서 noDefault = true이면서 오유이면 XRuntimeException
     * @return Timestamp
     */
    public java.sql.Timestamp cTimestamp() {
        java.sql.Timestamp out = null;
        if (value == null) {
            if (noDefaultCast)
                return null;
            out = new java.sql.Timestamp(0);
        } else if (value instanceof Number) {
            out = new java.sql.Timestamp(((Number) value).longValue());
        } else if (value instanceof java.sql.Timestamp) {
            out = (java.sql.Timestamp) value;
        } else if (value instanceof java.util.Date) {
            out = new java.sql.Timestamp(((java.util.Date) value).getTime());
        } else {
            try {
                String v = value.toString().trim();
                if (v.length() == 10) {
                    v = v + " 09:00:00";
                } else if (v.length() == 8) {
                    v = new StringBuffer().append(v.substring(0, 4)).append('-').append(v.substring(4, 6)).append('-').append(v.substring(6, 8)).append(" 09:00:00").toString();
                }
                //Format yyyy-mm-dd hh:mm:ss.fffffffff
                out = java.sql.Timestamp.valueOf(v);
            } catch (Exception e) {
                if (noDefaultCast)
                    throw new RuntimeException("cTimestamp cast exception : " + value);
                else
                    out = new java.sql.Timestamp(0);
            }
        }
        return out;
    }
}