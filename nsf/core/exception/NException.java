package nsf.core.exception;

/**
 * @(#) NException.java
 */ 

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.StringTokenizer;


/**
 * <pre>
 * NSF를 사용하거나 구현할 경우 필요한 모든 Exception의 최상위 클라스이다.
 * Exception chaining에 필요한 기본적인 작업을 수행한다.
 *</pre>
 * 
 * @since NSF 2002
 * @version NSF II 2007
 * 
 * @author 최영범  : 2007/01/09
 */
public class NException extends Exception {
	private java.lang.Throwable rootCause;
	private boolean isFirst = true;
    /**
     * Exception처리발생시 발생 내용을 보관
     */
    private String message = "";
    
    /**
     * Exception Code 정보
     */
    private String code = "";
    /**
     * 특정한 메세지 없이 NException을 생성한다.
	 */
    public NException() {
		super();
	}
    /**
     * 특정한 메세지를 갖는 NException을 생성한다.
     * @params 메세지
	 */
    public NException(String s) {
		super(s);
	}
    /**
     * 메세지 코드에 해당하는 메세지와 파라미터를 조합하여 례외처리내용으로 보관한다.
     * 
     * @param code 례외처리내용을 담고있는 메세지코드
     * @param msg 코드에 해당하는 메세지
     */
    public NException(String code, String msg) {
        this("[" + code + "] " + msg);
        this.code = code;
        this.message = "[" + code + "] " + msg;
    }
    /**
     * 메세지 코드에 해당하는 메세지와 파라미터를 조합하며 더불어 례외처리의 원인메세지를 포함하여
     * 례외처리내용으로 보관한다.
     *  
     * @param code 례외처리내용을 담고있는 메세지코드
     * @param msg 코드에 해당하는 메세지
     * @param rootCause 원인이 되는 례외처리
     */
    public NException(String code, String msg, Throwable rootCause) {
        this("[" + code + "] " + msg, rootCause);
        this.code = code;
        this.message = "[" + code + "] " + msg;
    }
    /**
     * 특정한 메세지와 Throwable을 갖는 NException을 생성한다.
     * @param message 메세지
     * @param rootCause exception chaining에 필요한 Throwable
	 */
    public NException(String message, Throwable rootCause) {
		super(message);
		this.rootCause = rootCause;
		this.isFirst = false;
	}
    /**
     * 특정한 Throwable을 갖는 NException을 생성한다.
     * @param rootCause exception chaining에 필요한 Throwable
	 */
    public NException(Throwable rootCause) {
		this();
		this.rootCause = rootCause;
		this.isFirst = false;
	}
    /**
     * NException의 rootCause를 되돌리는 메소드이다.
     * @return Throwable인 rootCause를 되돌린다.
	 */
	public Throwable getRootCause() {
		return rootCause;
	}
    /**
     * Stack Trace를 string형태로 되돌리는 메소드이다.
     * @return Stack Trace를 string 형태로 되돌린다.
	 */
	public String getStackTraceString() {
		StringWriter s = new StringWriter();
		printStackTrace( new PrintWriter(s) );
		return s.toString();
	}
    /**
     * Throwable과 exception chaining한 메세지의 stack trace를 
     * 지정한 error output stream인 System.err에 출력한다.
	 * @see java.lang.Throwable
	 */
	public void printStackTrace() {
		printStackTrace( System.err );
    }
    /**
     * Throwable과 exception chaining한 메세지의 stack trace를 
     * 지정한 error output stream인 PrintStream에 출력한다.
     * @param PrintStream 
	 * @see java.lang.Throwable
	 */
	public void printStackTrace(java.io.PrintStream s) {
		synchronized (s) {
			super.printStackTrace(s);
			if (rootCause != null) {
				rootCause.printStackTrace(s);
			}
            if (isFirst || !(rootCause instanceof NException)  ) {
				s.println("-----------------------------");
			}
		}
	}
    /**
     * Throwable과 exception chaining한 메세지의 stack trace를 
     * 지정한 error output stream인 PrintWriter에 출력한다.
     * @param s PrintWriter
	 * @see java.lang.Throwable
	 */
	public void printStackTrace(java.io.PrintWriter s) {
		synchronized (s) {
			super.printStackTrace(s);
			if (rootCause != null) {
				rootCause.printStackTrace(s);
			}
            if (isFirst || !(rootCause instanceof NException)  ) {
				s.println("-----------------------------");
			}
		}
	}
    /**
     * 례외처리내용을 되돌린다. Exception의 getMessage() method를 override한다.
     * 
     * @return 례외처리의 내용
     */
    public String getMessage() {
        return this.message;
    }
    
    /**
     * Exception Code를 되돌린다.
     * 
     * @return 례외처리의 내용
     */
    public String getCode() {
        return this.code;
    }
    
    /**
     * 례외처리내용을 되돌린다. Exception의 toString() method를 override한다.
     * 
     * @return 례외처리의 내용
     */
    public String toString() {
        String s = getClass().getName();
        String message = this.message;
        return (message != null) ? (s + ": " + message) : s;
    }
    /**
     * 례외처리가 발생한 클라스명을 얻는다.
     * 
     * @return className 클라스명
     */
    private String getClassName() {
        String className = null;
        StackTraceElement[] ste = this.getStackTrace();
        StringTokenizer st = new StringTokenizer(ste[0].getClassName(), ".");
        while (st.hasMoreElements()) {
            className = (String) st.nextElement();
        }
        return className;
    }
    /**
     * 례외처리가 발생한 클라스의 함수명을 얻는다.
     * 
     * @return 함수명
     */
    private String getMethodName() {
        StackTraceElement[] ste = this.getStackTrace();
        return ste[0].getMethodName();
    }
}

