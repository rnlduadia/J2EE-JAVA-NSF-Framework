package nsf.core.log;

/**
 * @(#) NLogPrintWriter
 */
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;

public class NLogPrintWriter extends PrintWriter {

    /**
     * <pre>
     * Spec에 따라 Log Writer 객체를 생성하기 위하여 PrintWriter객체를 상속 받아 해당 메소드를 추가.
     * </pre>
     * 
     * @since 2007년 1월 5일
     * @version NSF1.0
     * 
     * @author 조광혁
     */

	public NLogPrintWriter(Writer arg0) {
		super(arg0);
	}

	public NLogPrintWriter(Writer arg0, boolean arg1) {
		super(arg0, arg1);
	}

	public NLogPrintWriter(OutputStream arg0) {
		super(arg0);
	}

	public NLogPrintWriter(OutputStream arg0, boolean arg1) {
		super(arg0, arg1);
	}
	
	/**
	 * spec에 따라 문자렬(char[])을 print하고 line을 마친다.
	 *
	 * @param x[] 출력하려고 하는 문자렬.
	 */
	public void println(String spec, char x[]) {
		super.println(x);
	}
	/**
	 * spec에 따라 문자렬(char[])을 print하고 line을 마친다.
	 *
	 * @param x[] 출력하려고 하는 문자렬.
	 */
	public void print(String spec, char x[]) {
		super.print(x);
	}
	
	/**
	 * 문자(char)를 print하고 line을 마친다.
	 *
	 * @param x 출력하려고 하는 문자.
	 */
	public void println(String spec, char x) {
		super.println(x);
	}
	/**
	 * 문자(char)를 print하고 line을 마친다.
	 *
	 * @param x 출력하려고 하는 문자.
	 */
	public void print(String spec, char x) {
		super.print(x);
	}

	/**
	 * double type을 print하고 line을 마친다.
	 *
	 * @param x 출력하려고 하는 double.
	 */
	public void println(String spec, double x) {
		super.println(x);
	}
	/**
	 * double type을 print하고 line을 마친다.
	 *
	 * @param x 출력하려고 하는 double.
	 */
	public void print(String spec, double x) {
		super.print(x);
	}
	
	/**
	 * float type을 print하고 line을 마친다.
	 *
	 * @param x 출력하려고 하는 float.
	 */
	public void println(String spec, float x) {
		super.println(x);
	}
	/**
	 * float type을 print하고 line을 마친다.
	 *
	 * @param x 출력하려고 하는 float.
	 */
	public void print(String spec, float x) {
		super.print(x);
	}
	
	/**
	 * int type을 print하고 line을 마친다.
	 *
	 * @param x 출력하려고 하는 int.
	 */
	public void println(String spec, int x) {
		super.println(x);
	}
	/**
	 * int type을 print하고 line을 마친다.
	 *
	 * @param x 출력하려고 하는 int.
	 */
	public void print(String spec, int x) {
		super.print(x);
	}
	
	/**
	 * long type을 print하고 line을 마친다.
	 *
	 * @param x 출력하려고 하는 long.
	 */
	public void println(String spec, long x) {
			super.println(x);
	}
	/**
	 * long type을 print하고 line을 마친다.
	 *
	 * @param x 출력하려고 하는 long.
	 */
	public void print(String spec, long x) {
			super.print(x);
	}
	
	/**
	 * Object type중 Throwable type일 경우 해당 Throwable의 
	 * trace를 print하고 line을 마친다.
	 *
	 * @param x 출력하려고 하는 Object
	 */
	public void println(String spec, Object x) {
		super.println(x);
	}
	/**
	 * Object type중 Throwable type일 경우 해당 Throwable의 
	 * trace를 print하고 line을 마친다.
	 *
	 * @param x 출력하려고 하는 Object
	 */
	public void print(String spec, Object x) {
		super.print(x);
	}
	
	/**
	 * String type을 print하고 line을 마친다.
	 *
	 * @param x 출력하려고 하는 String.
	 */
	public void println(String spec, String x) {
		super.println(x);
	}
	/**
	 * String type을 print하고 line을 마친다.
	 *
	 * @param x 출력하려고 하는 String.
	 */
	public void print(String spec, String x) {
		super.print(x);
	}
	
	/**
	 * boolean type을 print하고 line을 마친다.
	 *
	 * @param x 출력하려고 하는 boolean.
	 */
	public void println(String spec, boolean x) {
		super.println(x);
	}
	/**
	 * boolean type을 print하고 line을 마친다.
	 *
	 * @param x 출력하려고 하는 boolean.
	 */
	public void print(String spec, boolean x) {
		super.print(x);
	}
}

