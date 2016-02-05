package nsf.support.filemng.xml;

/**
 * @(#) NControlServlet.java
 * 
 */
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Observable;
import java.util.Observer;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import nsf.core.config.NConfiguration;
import nsf.core.exception.NException;
import nsf.core.log.NLog;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXParseException;

/**
 * <pre>
 * 이 Class는 파일을 Management하는 주 Class이다.<br>
 * 파일은 여러개의 Root Directory별로 관리된다.<br>
 * 또한, Root Directory별로 Sql문이 관리된다. Directory안에는 Directory가 존재할 수 있다.<br>
 * 모든 Root Directory문은 nsf.xml에서 정의된다. ex) nsf.sql.default.url = /public/........<br>
 * 또한 sql문을 가지고 있는 파일들은 여러개의 sql문을 가질 수 있다. <br>
 * 단, sql문은 일정한 형식을 가져야만 하며 ex) key= sql문,<br>
 * Key는 Unique한 이름을 가져야만 한다. ex) student.retrieveMaxNum = select max(num) from student where......<br>
 * </pre>
 *
 * @since 2007년 1월 5일
 * @version NSF 1.0
 * 
 * @author 강영진, Nova China 
 */

public class NFileManager implements Observer {

	//Singleton을 구성하기 위한 자신의(FileManagement)객체를 static으로 선언
	private static NFileManager fileManager = new NFileManager();

	//파일에 대한 Key/Value의 Map을 가지고 있다.
	private LinkedHashMap navigationMap = new LinkedHashMap();
	private LinkedHashMap tempNavigationMap = new LinkedHashMap();
	//File의 List를 가지고 있다.
	private LinkedHashMap fileMap = null;
	private String NSF_DEFAULT_MANAGER_SPEC = "default";
	private String NSF_MANAGER_SPEC = "nsf";
	private boolean NSF_ISREGISTERED_OBSERVER = false;
	private boolean IS_ERROR = false;

	/**
	 * Constructor for FileManagement.
	 */

	private NFileManager(){
		initialize();
	}

	public static NFileManager getInstance(){
		return fileManager;
	}

	/**
	 * nsf.conf에 정의된 sql의 Root Directory의 List를 읽어, 저장한다
	 * <BR>이때 Root Directory디렉토리는 하위 Directory를 가질 수 있다.
	 * <BR>하나의 Root Directory안에 있는 모든 파일은 하나의 ArrayList에 저장되며,
	 * <BR>저장된 ArrayList는 url을 Key로 하여 HashMap형태로 다시 저장된다.
	 * @return void
	 */
	private void readFileList(String managerSpec, String directoryName)
			throws NException {
		ArrayList subFileList = new ArrayList();
		fileMap = null;
		fileMap = new LinkedHashMap();

		File rootFolder = new File(directoryName);
		if (rootFolder == null || !rootFolder.exists()) {
			throw new NException("NSF_FMG_002", "Directory(" + directoryName
					+ "[NULL]) doesn't exist.");
		}

		readDirectoryFileList(rootFolder, subFileList);
		fileMap.put(managerSpec, subFileList);
	}

	/**
	 * Directory안에 Directory를 포함할수 있도록 설계되여져 있기 때문에 이 Method를 통해서
	 * <BR>재귀호출하며 Directory안의 Directory까지 식별하며 FileList를 읽어들여 ArrayList에 저장한다.
	 * @param directoryName 파일을 읽어들일 상위 DirectoryName
	 * @return void
	 */
	private void readDirectoryFileList(File xmlFileCandidateFolder,
			ArrayList subFileList) throws NException {

		if (xmlFileCandidateFolder.isDirectory()) {
			File[] list = xmlFileCandidateFolder.listFiles();
			for (int inx = 0; inx < list.length; inx++) {
				File subFile = list[inx];
				String subFileName = subFile.getName();
				if (subFile.isDirectory()) {
					readDirectoryFileList(subFile, subFileList);
				} else {
					if (subFileName.toUpperCase().endsWith(".XML"))
						subFileList.add(subFile.getAbsolutePath());
				}
			}
		} else {
			throw new NException("NSF_FMG_002", "Directory("
					+ xmlFileCandidateFolder.getAbsolutePath()
					+ ") doesn't exist.");
		}
	}

	// 주어진 targetNodeName 및 subNode 에 대해서 노드 배렬을 얻어 온다.
	private String[] getNodeArray(NConfiguration conf, String targetNodeName,
			String subNode) throws Exception {
		String[] nodeArray = new String[0];
		String[] realNodeArray = new String[0];
		try {

			NodeList targetNodeList = conf.getNodeList(targetNodeName);
			nodeArray = new String[targetNodeList.getLength()];
			int arrayIndex = 0;
			for (int i = 0, size = targetNodeList.getLength(); i < size; i++) {
				Node curNode = targetNodeList.item(i);
				String attrName = curNode.getAttributes().item(0)
						.getNodeValue();
				String attrAddedKey = targetNodeName + "<" + attrName + ">/"
						+ subNode;

				if (attrName != null
						&& attrName.equals(NSF_DEFAULT_MANAGER_SPEC))
					attrName = NSF_MANAGER_SPEC;

				String directoryStr = conf.get(attrAddedKey);
				if( directoryStr == null || "".equals(directoryStr) )
					continue;
				nodeArray[arrayIndex++] = attrName + "," + conf.get(attrAddedKey);
			}
			realNodeArray = new String[arrayIndex];
			for( int inx = 0 ; inx < arrayIndex ; inx++ ){
				realNodeArray[inx] = nodeArray[inx];
			}

		} catch (Exception e) {
			NLog.report.println(this.getClass().getName() + "."
					+ "getNodeArray() NConfiguration 으로부터 [" + targetNodeName
					+ "/" + subNode + "] 을 가져오지 못했습니다." + e);
			throw e ;
		}
		return realNodeArray;
	}

	/**
	 * FileManagement의 초기화를 한다.
	 * <BR>1. 모든 FileList를 저장한다.
	 * <BR>2. FileList에 저장된 파일에서 SQL문을 읽어와서 저장한다.
	 * @return void
	 */
	private void initialize(){
		try {
			NConfiguration conf = NConfiguration.getInstance();
			String[] navigationArray = getNodeArray(conf, "/configuration/nsf/navigation", "directory");

			for (int i = 0, size = navigationArray.length; i < size; i++) {
				String nodeName = navigationArray[i].split(",")[0];
				String nodeValue = navigationArray[i].split(",")[1];
				this.readFileList(nodeName, nodeValue);
				this.generateMapper(nodeName);
			}

			navigationMap.clear();
			navigationMap = tempNavigationMap;
			tempNavigationMap = null;
			tempNavigationMap =  new LinkedHashMap();

			if (!NSF_ISREGISTERED_OBSERVER ){
				conf.addObserver(this);
				NSF_ISREGISTERED_OBSERVER = true;
			}
		} catch (Exception e) {
			IS_ERROR = true;
			e.printStackTrace( NLog.debug );
		}
	}
	private void checkDuplicate(LinkedHashMap eachNavigationMap,
			String managerSpec, String fileKey) throws Exception {
		if (eachNavigationMap.containsKey(fileKey)) {
			throw new NException("NSF_FMG_004", "Duplicate Key ["
					+ managerSpec + "-" + fileKey + "] Found ");
		}
	}

	private void generateMapper(String managerSpec) throws NException {
		NFileManagerHandler handler = null;
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser saxParser = factory.newSAXParser();
			ArrayList fileList = (ArrayList) fileMap.get(managerSpec);
			LinkedHashMap eachNavigationMap = new LinkedHashMap();

			int fileSize = fileList.size();
			for (int inx = 0; inx < fileSize; inx++) {
				handler = new NFileManagerHandler();
				saxParser.parse(new File((String) fileList.get(inx)), handler);
				LinkedHashMap eachFileMap = handler.getMap();
				Iterator eachFileKeyIterator = eachFileMap.keySet().iterator();

				for (; eachFileKeyIterator.hasNext();) {
					String fileKey = (String) eachFileKeyIterator.next();
					String fileValue = (String) eachFileMap.get(fileKey);
					checkDuplicate(eachNavigationMap, managerSpec, fileKey);
					eachNavigationMap.put(fileKey, fileValue);
				} //end for
			} //end for

			tempNavigationMap.put(managerSpec, eachNavigationMap);
		} catch(SAXParseException se){
			throw new NException("NSF_FMG_003","XML Parsing Exception : You must check XML Validation=>"+se.getMessage());
		} catch (Exception e) {
			throw new NException("NSF_FMG_003", this.getClass().getName()
					+ "." + "generateMapper()", e);
		}
	}

	/**
	 * domain에 대한 key의 값을 반환한다.
	 * @return String key에 맞는 value값
	 */
	public String get(String domain, String key) throws NException {
		LinkedHashMap tempMap = (LinkedHashMap) navigationMap.get(domain);
		if (tempMap == null)
			throw new NException("NSF_FMG_005", "Domain Name(" + domain
					+ ") doesn't exist. Check navigation name in the nsf.xml");

		return (String) tempMap.get(key);
	}

	/**
	 * key의 값을 반환한다.
	 * @return String key에 맞는 value값
	 */
	public String get(String key) throws NException {
		return get(NSF_MANAGER_SPEC, key);
	}

	/**
	 * 현재 이 객체는 singleton으로 작동하고 있다, 만일, 파일의 정보나, 값이 변경되였을 경우
	 * 이 Method를 통해서 refresh를 할 수 있다.
	 */
	public void refresh() throws NException{
		synchronized (this) {
			initialize();
			if( IS_ERROR ){
				IS_ERROR = false;
				throw new NException("XML Parsing Exception : You must check XML Validation");
			}
		}
	}

	/**
	 * NFileManager class 초기화를 위해 refresh()를 호출한다.
	 * @param o the observable object.
	 * @param arg notifyObservers method에 전달되여지는 argument.
	 */
	public void update(Observable o, Object arg) {
		try{
			this.refresh();
		}catch(Exception e){}
	}
}

