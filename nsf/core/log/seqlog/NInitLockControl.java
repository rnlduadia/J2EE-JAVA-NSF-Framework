package nsf.core.log.seqlog;

/**
 * @(#) NInitLockControl.java
 */

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * <pre>
 * File 기반의 Locking 기능을 제공하는 클라스
 * </pre>
 *
 * @since 2007년 1월 5일
 * @version NSF1.0
 * 
 * @author 조광혁, Nova China
 */

public class NInitLockControl {
	
	/**
	 * 파일상에 Lock을 걸고 Lock에 사용된 문자값을 리턴한다.
	 * @return Lock에 사용된 문자값
	 * @throws Exception
	 */
	
	public String getLock() throws Exception {
		char ch = 'A';
		
		try {
			if (exist() == false) {
				write(ch);
			} else {
				ch = read();
				if (ch == 'Z')
					ch = 'a';
				else if (ch == 'z')
					ch = 'A';
				else
					ch++;
				write(ch);
			}
		} finally {
			try {
			} catch ( Throwable t ) {
				t.printStackTrace();
			}
		}

		return "" + ch;
	}
	
    /**
     * nsf.home의 .nloc파일이 있으면 true를 돌려준다.
     * @return 결과
     */
	protected boolean exist() {
		File file = new File(System.getProperty("nsf.home"), ".nloc");
		return file.canRead();
	}
	
    /**
     * lock으로 사용되는 문자를 nsf.home의 .nloc파일에 쓴다.
     * @param ch lock으로 사용되는 문자
     */
	protected void write(char ch) {
		PrintWriter pw = null ;
		try {
			File file = new File(System.getProperty("nsf.home"), ".nloc");
			pw = new PrintWriter(new FileWriter(file));
			pw.write(ch);			
		} catch (Exception e) {
			e.printStackTrace();
		} finally { 
			try {
			  if ( pw != null )  pw.close();
			} catch ( Exception e ) {
			  e.printStackTrace();
			}
	   }
	}
	
    /**
     * lock으로 사용된 문자를 읽어들인다.
     * @return lock으로 사용된 문자
     */
	protected char read() {
		BufferedInputStream bis = null;
		char ch = 'A';
		try {
			File file = new File(System.getProperty("nsf.home"), ".nloc");
			bis = new BufferedInputStream(new FileInputStream(file));
			ch = (char) bis.read();
		} catch (Exception e) {
			ch = 'A';
			e.printStackTrace();
		} finally { 
			try {
			  if ( bis != null )  bis.close();
			} catch ( Exception e ) {
				e.printStackTrace();
			}
		}
		return ch;
	}
}

