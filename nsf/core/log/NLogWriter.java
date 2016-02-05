package nsf.core.log;

/**
 * @(#) NLogWriter.java
 */

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

import nsf.core.config.NConfiguration;
import nsf.support.tools.file.NFileUtil;

public class NLogWriter extends NLogPrintWriter {

    /**
     * <pre>
     * log 문자렬을 file에 직접 print하는 역할을 한다.
     * PrintWriter를 상속 받아 println() method들만 overrinding하였다.
     * 또한 시스템 날자가 바뀌면 '20001103report.log' 같은 형식의 새로운
     * log file로 변경한다.
     * </pre>
     * 
     * @since 2007년 1월 5일
     * @version NSF1.0
     *
     * @author 조광혁, Nova China
     */

	private int mode;

	private String temp_today;
	SimpleDateFormat dateFormat;
	private LinkedHashMap writerProp = null;
	private String date ;
	long maxFileSize = -1;
	
	/**
	 * NLogWriter는 PrintWriter를 상속받기 때문에 autoflush를 사용하게 된다. <br>
	 * autoFlush를 true로 설정을 하면 행이 끝나면서 buffer의 내용을 출력하게 된다. 
	 *
	 * @param mode Log Functionality Mode.
	 * @param autoFlush autoFlush.
	 * @throws NLogException Log error가 발생할 경우.
	 */
	NLogWriter( int mode, boolean autoFlush ) {
		super( new NNullWriter(), autoFlush );
		this.mode = mode;
		try {
			NConfiguration conf = NConfiguration.getInstance();
			String confDay = conf.getString("/configuration/nsf/log/filedate", "");
//			maxFileSize = conf.getLong("/configuration/nsf/log/maxFileSize", -1);
			
			date = ( confDay.equals("12") ? "yyyyMMddaa" : "yyyyMMdd" );
			dateFormat = new SimpleDateFormat (date, java.util.Locale.US);		
		} catch (Exception e) {
			System.err.println(NLogUtils.toDefaultLogForm("NLogWriter", "NLogWriter", "NSF_LOG_004", "NLogWriter initialization fail : " + e.getMessage()));
		}
	}

	/**
	 * 현재 설정된 날자(today)가 시스템 날자인지를 판단하여 날자가 
	 * 변경되였다면 '20070401sys.log' 같은 형식의 새로운 log file로 
	 * write하기 위하여 Writer의 out객체를 새롭게 얻어낸다. 
	 */
	void checkDate(String spec) {
		String today = dateFormat.format(new java.util.Date());
		
		if (today.equals(temp_today) ) {
			getWriter(spec);
			return;
		}
		if (writerProp != null)
			writerProp.clear();
		setWriter(spec);
	}
	
	/**
	 * 각 준위에 따르는 Writer 객체를 Map에 저장한다.
	 * @param spec write할 객체의 spec
	 */
	void setWriter(String spec){
		try {				
			makeWriter(spec);
			out = (Writer) writerProp.get(spec);		
		} catch (Exception e) {
			out = null;
			System.err.println(NLogUtils.toDefaultLogForm("NLogWriter", "checkDate", "NSF_LOG_004", "can't open log file : " + e.getMessage()));	
		} 
	}

	/**
	 * Writer객체가 존재하면 close해주고 writerProp을 초기화한다.
	 */
	public void close() {
		if ( writerProp != null ) {
			Set set = writerProp.keySet();
			Iterator e = set.iterator();
		
			while( e.hasNext() ) {
				String key = (String) e.next();
				Object value =  writerProp.get(key);
				if ( value instanceof BufferedWriter ) {
					out = (BufferedWriter)value;
					try {
						if(out!=null){
						out.flush();
						out.close();
						}
					}catch( IOException ie){
						System.err.println(
								NLogUtils.toDefaultLogForm(
										"NLogWriter", "close", "NSF_LOG_006", 
										"Can't close Log writer : " + ie.getMessage()));
					}
				} 
			}
			writerProp.clear();
		}
	}
	
	/**
	 * Writer 객체가 존재하지 않을 경우 writer객체를 생성하여 준다.
	 * @param spec write할 객체의 spec
	 */
	private void getWriter(String spec) {
		if(writerProp.containsKey(spec)&& (writerProp.get(spec)!= null|| writerProp.get(spec)!="")){
			out = (BufferedWriter) writerProp.get(spec);
		}else{
			setWriter(spec);
		}
	}

	/**
	 * Log파일을 초기화 시켜주고 BufferdWriter를 리턴한다.
	 *
	 * @return BufferdWriter.
	 * @throws NLogException Log error가 발생할 경우.
	 */
	private void makeWriter(String spec) throws Exception {
		temp_today = dateFormat.format(new java.util.Date());
		String directory = "";
		BufferedWriter buf = null;
		String filename = null;
		try {
			writerProp = new LinkedHashMap();
			// log directory를 java -D옵션으로 우선 체크하고
			// 셋팅이 안되어 있으면 configuration에서 읽는다.
			// 멀티 java instance환경에서 log directory를 분리하고자 할 경우 활용. 
			if(null != System.getProperty("nsf.log.dir"))
				directory = System.getProperty("nsf.log.dir");
			else if(spec.equals("default")){
				NConfiguration conf = NConfiguration.getInstance();
				directory = conf.get("/configuration/nsf/log/directory");
			}else {
				NConfiguration conf = NConfiguration.getInstance();
				directory = conf.get("/configuration/nsf/log/spec<"+spec+">/directory");
			}
			String logname = temp_today + NLog.LOG_FILE_EXT[mode] + ".log";			
			filename = NFileUtil.getCompleteLeadingSeperator(directory)+logname;
			writerProp.put(spec+"file",filename);
			FileWriter fw =  new FileWriter(filename, true);// APPEND MODE			
			
			buf = new BufferedWriter(fw);
			writerProp.put(spec, buf);
		} catch(Exception e) {
			System.err.println(NLogUtils.toDefaultLogForm("NLogWriter", "getWriter", "NSF_LOG_003", "NLog Writer fail : " + e.getMessage()));			
			throw e;
		}
	}

	/**
	 * 문자렬(char[])을 print하고 line을 마친다.
	 *
	 * @param x[] 출력하려고 하는 문자렬.
	 */
	public void println(char x[]) {
		println("default", x);
	}
	/**
	 * spec에 따라 문자렬(char[])을 print하고 line을 마친다.
	 *
	 * @param x[] 출력하려고 하는 문자렬.
	 */
	public void println(String spec, char x[]) {
		synchronized (lock) {
			checkDate(spec);
			super.print( NLog.format.prefix() );
			super.print(x);
			super.print( NLog.format.postfix() );
			super.println();
		}
	}
	
	/**
	 * 문자(char)를 print하고 line을 마친다.
	 *
	 * @param x 출력하려고 하는 문자.
	 */
	public void println(char x) {

		println("default", x);
	}

	/**
	 * 문자(char)를 print하고 line을 마친다.
	 *
	 * @param x 출력하려고 하는 문자.
	 */
	public void println(String spec, char x) {
		synchronized (lock) {
		
			checkDate(spec);
			super.print( NLog.format.prefix() );
			super.print(x);
			super.print( NLog.format.postfix() );
			super.println();
		
		}
	}

	/**
	 * double type을 print하고 line을 마친다.
	 *
	 * @param x 출력하려고 하는 double.
	 */
	public void println(double x) {
		println("default", x);
	}
	/**
	 * double type을 print하고 line을 마친다.
	 *
	 * @param x 출력하려고 하는 double.
	 */
	public void println(String spec, double x) {
		synchronized (lock) {
		
			checkDate(spec);
			super.print( NLog.format.prefix() );
			super.print(x);
			super.print( NLog.format.postfix() );
			super.println();
		}
	}
	
	/**
	 * float type을 print하고 line을 마친다.
	 *
	 * @param x 출력하려고 하는 float.
	 */
	public void println(float x) {
		println("default", x);
	}
	/**
	 * float type을 print하고 line을 마친다.
	 *
	 * @param x 출력하려고 하는 float.
	 */
	public void println(String spec, float x) {
		synchronized (lock) {
			checkDate(spec);
			super.print( NLog.format.prefix() );
			super.print(x);
			super.print( NLog.format.postfix() );
			super.println();
		}
	}
	
	/**
	 * int type을 print하고 line을 마친다.
	 *
	 * @param x 출력하려고 하는 int.
	 */
	public void println(int x) {
		println("default", x);
	}
	/**
	 * int type을 print하고 line을 마친다.
	 *
	 * @param x 출력하려고 하는 int.
	 */
	public void println(String spec, int x) {
		synchronized (lock) {
			checkDate(spec);
			super.print( NLog.format.prefix() );
			super.print(x);
			super.print( NLog.format.postfix() );
			super.println();
		}
	}
	
	/**
	 * long type을 print하고 line을 마친다.
	 *
	 * @param x 출력하려고 하는 long.
	 */
	public void println(long x) {
		println("default", x);
	}
	/**
	 * long type을 print하고 line을 마친다.
	 *
	 * @param x 출력하려고 하는 long.
	 */
	public void println(String spec, long x) {
		synchronized (lock) {
			checkDate(spec);
			super.print( NLog.format.prefix() );
			super.print(x);
			super.print( NLog.format.postfix() );
			super.println();
		}
	}
	
	/**
	 * Object type중 Throwable type일 경우 해당 Throwable의 
	 * trace를 print하고 line을 마친다.
	 *
	 * @param x 출력하려고 하는 Object
	 */
	public void println(Object x) {
		println("default", x);
	}
	/**
	 * Object type중 Throwable type일 경우 해당 Throwable의 
	 * trace를 print하고 line을 마친다.
	 *
	 * @param x 출력하려고 하는 Object
	 */
	public void println(String spec, Object x) {
		String result = "";
		if(x instanceof Throwable) {
			StackTraceElement[] ste = ((Throwable)x).getStackTrace();
			for(int idx=0; idx < ste.length; idx++)
				result += "\n    " + ste[idx].toString();
		} else {
			result = x.toString();
		}
		
		synchronized (lock) {
			checkDate(spec);
			super.print( NLog.format.prefix() );
			super.print(result);
			super.print( NLog.format.postfix() );
			super.println();
		}
	}
	
	/**
	 * Object와 String을 print하고 line을 마친다.
	 *
	 * @param p 출력하려고 하는 대상 Object.
	 * @param x 출력하려고 하는 String.
	 */
	public void println(String spec, Object p, String x) {
		synchronized (lock) {
			checkDate(spec);
			super.print( NLog.format.prefix(p) );
			super.print(x);
			super.print( NLog.format.postfix() );
			super.println();
		}
	}
	
	/**
	 * String type을 print하고 line을 마친다.
	 *
	 * @param x 출력하려고 하는 String.
	 */
	public void println(String x) {
		this.println("default", x);
	}
	/**
	 * String type을 print하고 line을 마친다.
	 *
	 * @param x 출력하려고 하는 String.
	 */
	public void println(String spec, String x) {
		synchronized (lock) {
			checkDate(spec);
			super.print( NLog.format.prefix() );
			super.print(x);
			super.print( NLog.format.postfix() );
			super.println();
		}		
	}
	
	/**
	 * boolean type을 print하고 line을 마친다.
	 *
	 * @param x 출력하려고 하는 boolean.
	 */
	public void println(boolean x) {
		println("default", x);
	}
	/**
	 * boolean type을 print하고 line을 마친다.
	 *
	 * @param x 출력하려고 하는 boolean.
	 */
	public void println(String spec, boolean x) {
		synchronized (lock) {
			checkDate(spec);
			super.print( NLog.format.prefix() );
			super.print(x);
			super.print( NLog.format.postfix() );
			super.println();
		}
	}
}

