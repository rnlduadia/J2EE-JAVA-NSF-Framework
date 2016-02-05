package nsf.core.log;

/**
 * @(#) NNullPrintWriter.java
 */

public class NNullPrintWriter extends NLogPrintWriter { 

    /**
     * <pre>
     * UNIX의 /dev/null의 java version이다. 
     * 출력방향을 NNullPrintWriter로 하면 실제로는 
     * 아무일도 일어나지 않는다. 즉, 불필요한 출력을
     * 없에려고 할때 사용할수 있다.
     * </pre>
     * 
     * @since 2007년 1월 5일
     * @version NSF 1.0
     *
     * @author 조광혁, Nova China
     */

    public NNullPrintWriter() {
		super( new NNullWriter() );
	}

	public boolean checkError() {
		return false;
	}

	public void close() { }

	public void flush() { }

	public void print(char[] s) { }

	public void print(char c) { }

	public void print(double d) { }

	public void print(float f) { }

	public void print(int i) { }

	public void print(long l) { }

	public void print(Object obj) { }

	public void print(String s) { }

	public void print(boolean b) { }

	public void println() { }

	public void println(char[] x) { }

	public void println(char x) { }

	public void println(double x) { }

	public void println(float x) { }

	public void println(int x) { }

	public void println(long x) { }

	public void println(Object x) { }

	public void println(String x) { }

	public void println(boolean x) { }

	public void write(char[] buf) { }

	public void write(char[] buf, int off, int len) { }

	public void write(int b) { }

	public void write(String s) { }

	public void write(String s, int off, int len) { }
}

