package nsf.core.log;
/**
 * @(#) NLog.java
 */

import nsf.core.config.NConfiguration;
import nsf.core.log.NLogFormat;

public class NLog {

    /**
     * <pre>
     * 일반적인 LOG기능을 수행하는 클라스이다.
     * 3가지의 Log기능이 있다.
     * 
     * report : 일반적으로 남겨야 할 정보들을 기록한다. file에 모두 출력한다.
     * debug  : 개발자들이 Debugging을 목적으로 사용할수 있다. console에 출력한다.
     * sqNLog : 개발자들이 Query실행을 감시하기 위하여 사용할수 있다. file, console에 모두 출력한다.
     * </pre>
     * 
     * @since 2007년 1월 5일
     * @version NSF1.0
     * 
     * @author 조광혁, Nova China         
     */

	final static int REPORT = 0;

	final static int DEBUG = 1;

	final static int SQLLOG = 2;

	final static int LOG_COUNT = 3;

	final static String[] TRACE_FLAG =
								{
									"/configuration/nsf/log/tracelevel/report",
									"/configuration/nsf/log/tracelevel/debug",
									"/configuration/nsf/log/tracelevel/sqllog"
								};
	/**
	 * 해당 Log Functionality가 write하는 파일을 구별하기 위한 Constant.
	 * (report, debug, sqNLog)
	 */
	final static String[] LOG_FILE_EXT = {"report", "debug", "sqNLog"};

	static NLogPrintWriter logArr[] = new NLogPrintWriter[LOG_COUNT];

	public static NLogPrintWriter report = logArr[REPORT];

	public static NLogPrintWriter debug = logArr[DEBUG];

	public static NLogPrintWriter sqllog = logArr[SQLLOG];

	static NLogFormat format;

	static {
		try {
			System.out.println("NLog: 시작" );
			init();
		} catch ( Exception e ) {
			System.err.println(NLogUtils.toDefaultLogForm("NLog", "static block", "NLogFormat initialization fail : " + e.getMessage()));
			e.printStackTrace();
		}
	}

	private NLog() {}
	
	/**
	 * NLog class를 초기화 하는 method로서 Log Functionality 동작속성 및
	 * configuration파일의 /configuration/nsf/log/autoflush 속성에 따라
	 * Log Functionality의 NLogWriter를 생성시킨다. 
	 * nsf.xml에 정의된 설정에 따라 파일과 console의 출력여부를 확인한다.
	 */
	private static void init() {
		try {
			NConfiguration conf = NConfiguration.getInstance();
			boolean autoFlush = conf.getBoolean("/configuration/nsf/log/autoflush", false);  // AUTO Flush
			format = new NLogFormat(); // format
			String writer = "";
			
			for ( int mode = 0 ; mode < LOG_COUNT ; mode++ ) {
				System.out.println( TRACE_FLAG[mode] +" = "+ conf.getBoolean(TRACE_FLAG[mode], false));
				writer = conf.getString( "/configuration/nsf/log/writer/"+LOG_FILE_EXT[mode] , "");
				switch ( mode ) {
					case DEBUG :
						if ( conf.getBoolean(TRACE_FLAG[mode], false)){ 
							if(writer.toLowerCase().equals("console") ) {
								logArr[mode] = new NLogPrintWriter( System.out, true );
							} else if(writer.toLowerCase().equals("file")){
								logArr[mode] = new NLogWriter(mode , autoFlush );
							} else
								logArr[mode] = new NBothPrintWriter(new NLogPrintWriter( System.out, true ),
										   new NLogWriter(mode , autoFlush) );
						} else {
							logArr[mode] = new NNullPrintWriter();
						}
						break;
					case REPORT :
					case SQLLOG :
					if ( conf.getBoolean(TRACE_FLAG[mode], false)){ 
						if(writer.toLowerCase().equals("console") ) {
								logArr[mode] = new NLogPrintWriter( System.out, true );
							} else if(writer.toLowerCase().equals("both")){
								logArr[mode] = new NBothPrintWriter(new NLogPrintWriter( System.out, true ),
										   new NLogWriter(mode , autoFlush) );
							} else
								logArr[mode] = new NLogWriter(mode , autoFlush );
					} else {
						logArr[mode] = new NNullPrintWriter();
					}
					break;
						
					default:
					break;
				} 
			} 

			report    = logArr[REPORT];
			debug     = logArr[DEBUG];
			sqllog    = logArr[SQLLOG];
		System.out.println( "NLog: 초기화 성공" );
		} catch(Exception e) {
			System.err.println(NLogUtils.toDefaultLogForm("NLog", "init", "NSF_LOG_001", "NLog initialization fail : " + e.getMessage()));
			e.printStackTrace();
		}
	}
}

