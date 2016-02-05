package nsf.application.message;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Locale;

import nsf.core.exception.NException;
import nsf.core.log.NLog;
import nsf.foundation.persistent.db.connection.NDataSourcePool;
import nsf.support.collection.NData;
/**
 * <pre>
 * DB 메세지 정보에 접근, 필요한 인터페이스를 제공한다
 * </pre>
 * @since 2007/1/5
 * @version NSF 1.0
 * @author 김철남 Nova China
 */
public class NMessage {
	/**
	 * 해당 메시지.
	 */
	private String message = null;
	
	private static NData messageData = new NData();
	private static ArrayList messageTypeData = new ArrayList();
	
	/**
	 * 메세지형태 클라스 instance
	 */
	public NManageType typeInstance = null;	
	
	public NMessage() throws NException { 
		try { 
			
			typeInstance = new NManageType();
		} catch (Exception e) {
			throw new NException("NSF_MSG_006", "error occurred while getting message manage type", e);
		}
	}
	/**
	 * 파라메터를 typehandler에 전달한다. 
	 * 내부적으로 메세지테블의 DB 스펙과 필요한 컬럼명들을 얻는다.
	 *
	 * @param module 모듈명
	 * @param locale 언어/지역이 설정된 Locale객체
	 * @throws NException
	 */
	public void initialize(String module, Locale locale, String charsetName) throws NException {			
		try {
			typeInstance.initialize(module, locale, charsetName);
		} catch(NException ce) {
			throw new NException("NSF_MSG_011", "error occurred while getting message table information from configuration file", ce);
		}
	}
	/**
	 * code에 해당하는 메세지내용을 반환한다.
	 *
	 * @param code 메세지에 해당하는 코드.
	 * @return 해당 code의 message String value.
	 * @throws NException
	 */
	public String getMessage(String msgCode) throws NException {
		String messageKey = resolveMessageKey(msgCode);
		if( messageData.containsKey(messageKey) ) {
			return messageData.getString(messageKey);
		}
		
		NDataSourcePool pool = null;
		Connection conn = null;
		try {
			pool = NDataSourcePool.getInstance();

			conn = pool.getConnection("default");

			Statement stmt = conn.createStatement();
			String realSql = getMsgQuery(msgCode);
			ResultSet rset = stmt.executeQuery(realSql);
			if (rset.next ()) {
    			message = rset.getString("message");
			} else {
				throw new NException("NSF_MSG_012" , "Can't find such a Message Code in Message Table - " + msgCode);
			}
			rset.close();
			stmt.close();
		} catch(SQLException sqle) {
			System.out.println(getMsgQuery(msgCode));
			throw new NException("NSF_MSG_013" , "Fail to get a message from DB" , sqle);
		} catch (Exception e) {
			System.out.println(getMsgQuery(msgCode));			
			throw new NException("NSF_MSG_013" , "Fail to get a message from DB", e);			
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				throw new NException("NSF_MSG_015", "connection close error");
			}
		}
		messageData.setString(messageKey, message);
		return message;
	}
	
	private String resolveMessageKey(String msgCode){
		String realMessageCode = "";
		String moduleConcatStr = "_";
		String localeConcatStr = "_";
		String moduleStr = typeInstance.getModuleName();
		String localeStr = typeInstance.getLocale().toString();

		if( moduleStr == null || moduleStr.equals("")){
			moduleConcatStr = "";
		}
		
		if( localeStr == null || localeStr.equals("")){
			localeConcatStr = "";
		}
	
		realMessageCode = moduleStr + moduleConcatStr + localeStr + localeConcatStr + msgCode;
		return realMessageCode;
	}
	
	/**
	 * 메세지를 얻을수 있는 query를 생성하여 반환한다.
	 * @param code 메세지에 해당하는 코드.
	 * @return 해당 code의 message를 취득할수 있는 query String value.
	 * @throws NException
	 */ 	
	private String getMsgQuery(String msgCode) throws NException {
		return "SELECT message FROM "  + getTableName() + " WHERE code = '" + msgCode + "'";
	}
	/**
	 * 사용 메세지의 언어를 변경한다.
	 *
	 * @param 언어와 지역으로 생성한 Locale 객체
	 */  		
	public void changeLocale(Locale locale) {
		typeInstance.setLocale(locale);
	}
	//추가
	public void changeLocale(Locale locale, String charsetName) {
		typeInstance.setLocale(locale);
		typeInstance.setCharset(charsetName);
	}	
	/**
	 * 메세지가 저장되어 있는 테블명을 반환한다.
	 *
	 * @throws NException
	 */         
	public String getTableName() throws NException {
		return "message" + getAppendString(typeInstance.getModuleName()) + getAppendString(typeInstance.getLocale().toString());
	}
	/**
	 * 전달받은 문자렬이 null이거나 공백이 아닌경우 앞에 _ 를 붙여 리턴한다.
	 *
	 * @param str 변환될 스트링 변수
	 * @return 변환된 스트링 변수
	 */       
	public String getAppendString(String str) {
		if (str == null || str.equals(""))
			return "";
		else
			return "_" + str;
	}
	
    /**
     * 메쎄지형태를 구한다.
     * @return
     */
	private String getMessageType(){
		String realMessageCode = "";
		String moduleConcatStr = "_";
		String moduleStr = typeInstance.getModuleName();
		String localeStr = typeInstance.getLocale().toString();

		if( moduleStr == null || moduleStr.equals("")){
			moduleConcatStr = "";
		}
		
		realMessageCode = moduleStr + moduleConcatStr + localeStr;
		if( realMessageCode == null || realMessageCode.equals("") ){
			realMessageCode = "defaut";
		}
		return realMessageCode;
	}
	
	private String getPreLoadMsgQuery() throws NException {
		
		return "SELECT code, message FROM "  + getTableName();
	}
	
    /**
     * 메쎄지를 생성한다.
     * @throws NException
     */
	private void generateMessage() throws NException{
		NDataSourcePool pool = null;
		Connection conn = null;
		
		try {
			pool = NDataSourcePool.getInstance();

			conn = pool.getConnection("default");

			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(getPreLoadMsgQuery());

			while( rset.next() ) {
				String codeValue = rset.getString("code");
				String msgValue = rset.getString("message");
				
				String messageKey = resolveMessageKey(codeValue);
				messageData.setString(messageKey, msgValue);
			}
			rset.close();
			stmt.close();
		} catch(SQLException sqle) {
			throw new NException("NSF_MSG_013" , "Fail to get a message from DB" , sqle);
		} catch (Exception e) {
			throw new NException("NSF_MSG_013" , "Fail to get a message from DB", e);			
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				throw new NException("NSF_MSG_015", "connection close error");
			}
		}
	}
	
    /**
     * 메쎄지를 로드한다.
     * @throws NException
     */
	public void loadMessage() throws NException{
		String messageTypeStr = getMessageType();
		
		if( messageTypeData.contains(messageTypeStr) ) {
			NLog.debug.println("Already Loaded This Message Type");
		}else {
			messageTypeData.add(messageTypeStr);
		}
		generateMessage();
		System.out.println("messageType\n"+messageTypeData.toString());
	}

	/**
	 * 캐쉬작업이 있을 경우 사용. 
	 */
	public void refresh() {
		messageTypeData.clear();
		messageData.clear();
	}
}

