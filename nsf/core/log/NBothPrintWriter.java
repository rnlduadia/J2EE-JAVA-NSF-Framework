package nsf.core.log;

/**
 * @(#) NTeePrintWriter.java
 */

public class NBothPrintWriter extends NLogPrintWriter {

    /**
     * <pre>
     * 한 곳에 프린트하면 동시에 두곳으로 출력하는 Class이다.  
     * NBothPrintWriter를 생성할 때 두개의 PrintWriter을 
     * parameter로 주면 주어진 두개의 PrintWriter에 동시에 출력한다.
     * </pre>
     * 
     * @since 2007년 1월 5일
     * @version NSF 1.0
     *
     * @author 조광혁, Nova China
     */

	private NLogPrintWriter out1;

	private NLogWriter out2;

	public NBothPrintWriter(NLogPrintWriter out1, NLogWriter out2 ) {
		super( new NNullWriter() );
		this.out1 = out1;
		this.out2 = out2;
	}

	/**
	 * 첫번째 PrintWriter를 되돌린다.
	 */
	public NLogPrintWriter getPrintWriter1() {
		return this.out1;
	}
	/**
	 * 두번째 PrintWriter를 되돌린다.
	 */
	public NLogWriter getPrintWriter2() {
		return this.out2;
	}
    
	/**
	 * PrintWriter 1과 PrintWriter 2의 error state를 검사한다.
	 *
	 * @return PrintWriter 1의 error state 혹은 
	 * PrintWriter 2의 error state.
	 */
	public boolean checkError() {
		return out1.checkError() || out2.checkError();
	}
    
	/**
	 * PrintWriter 1과 PrintWriter 2를 close()시킨다. 
	 */
	public synchronized void close() {
		out2.close();
	}
    
	/**
	 * PrintWriter 1과 PrintWriter 2를 flush하고 close한다.
	 */    
	public synchronized void finalize() {
		if ( out1 != null ) {
			out1.flush();
		}
		if ( out2 != null ) {
			out2.flush();
			out2.close();
			out2 = null;
		}
	}
    
	/**
	 * PrintWriter 1과 PrintWriter 2를 각각 flush() 해준다. 
	 */
	public synchronized void flush() {
		out1.flush();
		out2.flush();
	}
    
	/**
	 * PrintWriter 1과 PrintWriter 2의 char배렬을 print한다.
	 *
	 * @param s print하고자 하는 char배렬.
	 */	
	public synchronized void print(char[] s) {
		out1.print( s );
		out2.print( s );
	}
	/**
	 * PrintWriter 1과 PrintWriter 2의 char배렬을 print한다.
	 *
	 * @param s print하고자 하는 char배렬.
	 */	
	public synchronized void print(String spec, char[] s) {
		out1.print( s );
		out2.print( spec, s );
	}
	
	/**
	 * PrintWriter 1과 PrintWriter 2의 char를 print한다.
	 *
	 * @param c print하고자 하는 char.
	 */	
	public synchronized void print(char c) {
		out1.print( c );
		out2.print( c );
	}
	/**
	 * PrintWriter 1과 PrintWriter 2의 char를 print한다.
	 *
	 * @param c print하고자 하는 char.
	 */	
	public synchronized void print(String spec, char c) {
		out1.print( c );
		out2.print( spec, c );
	}
	
	/**
	 * PrintWriter 1과 PrintWriter 2의 double를 print한다.
	 *
	 * @param d print하고자 하는 double.
	 */	
	public synchronized void print(double d) {
		out1.print( d );
		out2.print( d );
	}
	/**
	 * PrintWriter 1과 PrintWriter 2의 double를 print한다.
	 *
	 * @param d print하고자 하는 double.
	 */	
	public synchronized void print(String spec, double d) {
		out1.print( d );
		out2.print( spec, d );
	}
	
	/**
	 * PrintWriter 1과 PrintWriter 2의 float를 print한다.
	 *
	 * @param f print하고자 하는 float.
	 */	
	public synchronized void print(float f) {
		out1.print( f );
		out2.print( f );
	}
	/**
	 * PrintWriter 1과 PrintWriter 2의 float를 print한다.
	 *
	 * @param f print하고자 하는 float.
	 */	
	public synchronized void print(String spec, float f) {
		out1.print( f );
		out2.print( spec, f );
	}
	
	/**
	 * PrintWriter 1과 PrintWriter 2의 int를 print한다.
	 *
	 * @param i print하고자 하는 int.
	 */	
	public synchronized void print(int i) {
		out1.print( i );
		out2.print( i );
	}
	/**
	 * PrintWriter 1과 PrintWriter 2의 int를 print한다.
	 *
	 * @param i print하고자 하는 int.
	 */	
	public synchronized void print(String spec, int i) {
		out1.print( i );
		out2.print( spec, i );
	}
	
	/**
	 * PrintWriter 1과 PrintWriter 2의 long를 print한다.
	 *
	 * @param l print하고자 하는 long.
	 */	
	public synchronized void print(long l) {
		out1.print( l );
		out2.print( l );
	}
	/**
	 * PrintWriter 1과 PrintWriter 2의 long를 print한다.
	 *
	 * @param l print하고자 하는 long.
	 */	
	public synchronized void print(String spec, long l) {
		out1.print( l );
		out2.print( spec, l );
	}
	
	/**
	 * PrintWriter 1과 PrintWriter 2의 Object를 print한다.
	 *
	 * @param obj print하고자 하는 Object.
	 */	
	public synchronized void print(Object obj) {
		out1.print( obj );
		out2.print( obj );
	}
	/**
	 * PrintWriter 1과 PrintWriter 2의 Object를 print한다.
	 *
	 * @param obj print하고자 하는 Object.
	 */	
	public synchronized void print(String spec, Object obj) {
		out1.print( obj );
		out2.print( spec, obj );
	}
	
	/**
	 * PrintWriter 1과 PrintWriter 2의 String문자렬을 print한다.
	 *
	 * @param s print하고자 하는 String문자렬.
	 */	
	public synchronized void print(String s) {
		out1.print( s );
		out2.print( s );
	}
	/**
	 * PrintWriter 1과 PrintWriter 2의 String문자렬을 print한다.
	 *
	 * @param s print하고자 하는 String문자렬.
	 */	
	public synchronized void print(String spec, String s) {
		out1.print( s );
		out2.print( spec, s );
	}
	
	/**
	 * PrintWriter 1과 PrintWriter 2의 boolean을 print한다.
	 *
	 * @param b print하고자 하는 boolean.
	 */		
	public synchronized void print(boolean b) {
		out1.print( b );
		out2.print( b );
	}
	/**
	 * PrintWriter 1과 PrintWriter 2의 boolean을 print한다.
	 *
	 * @param b print하고자 하는 boolean.
	 */		
	public synchronized void print(String spec, boolean b) {
		out1.print( b );
		out2.print( spec, b );
	}
	
	/**
	 * PrintWriter 1과 PrintWriter 2를 print하고 
	 * line을 마친다.
	 */	
	public void println() {
		out1.println();
		out2.println();
	}
	
	/**
	 * PrintWriter 1과 PrintWriter 2의 char배렬을 
	 * print하고 line을 마친다.
	 *
	 * @param x print하고자 하는 char배렬.
	 */
	public void println(char[] x) {
		out1.println( x );
		out2.println( x );
	}
	/**
	 * PrintWriter 1과 PrintWriter 2의 char배렬을 
	 * print하고 line을 마친다.
	 *
	 * @param x print하고자 하는 char배렬.
	 */
	public void println(String spec, char[] x) {
		out1.println( x );
		out2.println( spec, x );
	}
	
	/**
	 * PrintWriter 1과 PrintWriter 2의 char을 
	 * print하고 line을 마친다.
	 *
	 * @param x print하고자 하는 char.
	 */	
	public void println(char x) {
		out1.println( x );
		out2.println( x );
	}
	/**
	 * PrintWriter 1과 PrintWriter 2의 char을 
	 * print하고 line을 마친다.
	 *
	 * @param x print하고자 하는 char.
	 */	
	public void println(String spec, char x) {
		out1.println( x );
		out2.println( spec, x );
	}
	
	/**
	 * PrintWriter 1과 PrintWriter 2의 double을 
	 * print하고 line을 마친다.
	 *
	 * @param x print하고자 하는 double.
	 */
	public void println(double x) {
		out1.println( x );
		out2.println( x );
	}
	/**
	 * PrintWriter 1과 PrintWriter 2의 double을 
	 * print하고 line을 마친다.
	 *
	 * @param x print하고자 하는 double.
	 */
	public void println(String spec, double x) {
		out1.println( x );
		out2.println( spec, x );
	}
	
	/**
	 * PrintWriter 1과 PrintWriter 2의 float을 
	 * print하고 line을 마친다.
	 *
	 * @param x print하고자 하는 float.
	 */
	public void println(float x) {
		out1.println( x );
		out2.println( x );
	}
	/**
	 * PrintWriter 1과 PrintWriter 2의 float을 
	 * print하고 line을 마친다.
	 *
	 * @param x print하고자 하는 float.
	 */
	public void println(String spec, float x) {
		out1.println( x );
		out2.println( spec, x );
	}
	
	/**
	 * PrintWriter 1과 PrintWriter 2의 int을 
	 * print하고 line을 마친다.
	 *
	 * @param x print하고자 하는 int.
	 */	
	public void println(int x) {
		out1.println( x );
		out2.println( x );
	}
	/**
	 * PrintWriter 1과 PrintWriter 2의 int을 
	 * print하고 line을 마친다.
	 *
	 * @param x print하고자 하는 int.
	 */	
	public void println(String spec, int x) {
		out1.println( x );
		out2.println( spec, x );
	}
	
	/**
	 * PrintWriter 1과 PrintWriter 2의 long을 
	 * print하고 line을 마친다.
	 *
	 * @param x print하고자 하는 long.
	 */	
	public void println(long x) {
		out1.println( x );
		out2.println( x );
	}
	/**
	 * PrintWriter 1과 PrintWriter 2의 long을 
	 * print하고 line을 마친다.
	 *
	 * @param x print하고자 하는 long.
	 */	
	public void println(String spec, long x) {
		out1.println( x );
		out2.println( spec, x );
	}
	
	/**
	 * PrintWriter 1과 PrintWriter 2의 Object를 
	 * print하고 line을 마친다.
	 *
	 * @param x print하고자 하는 Object.
	 */
	public void println(Object x) {
		out1.println( x );
		out2.println( x );
	}
	/**
	 * PrintWriter 1과 PrintWriter 2의 Object를 
	 * print하고 line을 마친다.
	 *
	 * @param x print하고자 하는 Object.
	 */
	public void println(String spec, Object x) {
		out1.println( x );
		out2.println( spec, x );
	}
	
	/**
	 * PrintWriter 1과 PrintWriter 2의 String문자렬을 
	 * print하고 line을 마친다.
	 *
	 * @param x print하고자 하는 String문자렬.
	 */
	public void println(String x) {		
		out1.println( x );		
		out2.println( x );		
	}
	/**
	 * PrintWriter 1과 PrintWriter 2의 String문자렬을 
	 * print하고 line을 마친다.
	 *
	 * @param x print하고자 하는 String문자렬.
	 */
	public void println(String spec, String x) {		
		out1.println( x );		
		out2.println( spec, x );		
	}
	
	/**
	 * PrintWriter 1과 PrintWriter 2의 boolean을 
	 * print하고 line을 마친다.
	 *
	 * @param x print하고자 하는 boolean.
	 */		
	public void println(boolean x) {
		out1.println( x );		
		out2.println( x );	
	}
	/**
	 * PrintWriter 1과 PrintWriter 2의 boolean을 
	 * print하고 line을 마친다.
	 *
	 * @param x print하고자 하는 boolean.
	 */		
	public void println(String spec, boolean x) {
		out1.println( x );		
		out2.println( spec, x );	
	}
	
	/**
	 * PrintWriter 1과 PrintWriter 2의 char배렬을 write한다.
	 *
	 * @param buf writer하고자 하는 char배렬.
	 */
	public void write(char[] buf) {
		out1.write( buf );
		out2.write( buf );
	}
	/**
	 * PrintWriter 1과 PrintWriter 2의 char배렬을 일정 
	 * 위치에서 write한다.
	 *
	 * @param buf writer하고자 하는 char배렬.
	 * @param off char배렬 시작시점에서의 offset.
	 * @param len char배렬 길이.
	 */
	public void write(char[] buf, int off, int len) {
		out1.write( buf, off, len );
		out2.write( buf, off, len );
	}
	/**
	 * PrintWriter 1과 PrintWriter 2의 single문자를 write한다.
	 *
	 * @param b writer하고자 하는 문자.
	 */	
	public void write(int b) { 
		out1.write( b );
		out2.write( b );
	}
	/**
	 * PrintWriter 1과 PrintWriter 2의 String문자렬을 write한다.
	 *
	 * @param s writer하고자 하는 String문자렬.
	 */		
	public void write(String s) { 
		out1.write( s );
		out2.write( s );
	}
	/**
	 * PrintWriter 1과 PrintWriter 2의 String문자렬을 일정 
	 * 위치에서 write한다.
	 *
	 * @param s writer하고자 하는 String문자렬.
	 * @param off 문자렬 시작시점에서의 offset.
	 * @param len 문자렬 길이.
	 */
	public void write(String s, int off, int len) { 
		out1.write( s, off, len );
		out2.write( s, off, len );
	}
}

