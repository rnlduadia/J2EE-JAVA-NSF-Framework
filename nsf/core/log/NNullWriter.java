package nsf.core.log;

/**
 * @(#) NNullWriter.java
 */

import java.io.Writer;

public class NNullWriter extends Writer { 

    /**
     * <pre>
     * abstract class인 Writer를 상속받아 아무것도 write하지않는 
     * null Writer.
     * </pre>
     * 
     * @since 2007년 1월 5일
     * @version NSF 1.0
     *
     * @author 조광혁, Nova China
     */

	public NNullWriter() { }
    
	public void close() { }

	public void flush() { }

	public void write(char[] cbuf) { }

	public void write(char[] cbuf, int off, int len) { }

	public void write(int c) { }

	public void write(String str) { }

	public void write(String str, int off, int len) { }
}

