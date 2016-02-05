package nsf.core.config;

/**
 * @(#) NConfiguration.java
*/

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Observable;

import nsf.core.exception.NException;
import nsf.core.log.NLogUtils;
import nsf.support.tools.xml.NShowDomTree;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class NConfiguration extends Observable {

	/**
	 * <pre>
	 * configuration 정보를 XML파일에서 통합관리하는 class로서 configuration정보가 프로그람
	 * 소스 내에 하드코딩 되여 있지 못하게 한다.
	 * </pre>
	 * 
	 * @since 2007년 1월 5일
	 * @version NSF1.0
	 * 
	 * @author 조광혁, Nova China         
	 */

	/**
	 * nsf.xml을 parsing하기 위한 NXmlUtils 객체 instance.
	 */
	private NXmlUtils utils;
	/**
	 * nsf.xml을 파싱한 DOM 모델의 Document.
	 */
	private Document doc;
	/**
	 * NConfiguration Single instacne 변수.
	 */
	private static NConfiguration xc;
	/**
	 * 시스템 프로퍼티에서 가져올 nsf_home 변수.
	 */
	private static String nsf_home = null;
	/**
	 * nsf_home에 mapping 되는 태그.
	 */
	private static String NSF_HOME_TAG = "#home";
	/**
	 * nsf_home에 mapping 되는 태그의 길이 변수.
	 */
	private static int NSF_HOME_TAG__SIZE = NSF_HOME_TAG.length();
	/**
	 * configuration file의 기정 이름.
	 */
	private final static String default_file_name = "nsf.xml";
	/**
	 * configuration file을 parsing한 후 이를 매핑할 LinkedHashMap 객체 instance.
	 */
	private static LinkedHashMap configurationMap = new LinkedHashMap();
	/**
	 * configuration file을 parsing한 후 이를 매핑할 때 사용되는 구분자.
	 */
	private static char GUBUN = '/';

	/**
	 * System property에서 nsf_home의 값을 얻어와서 configuration파일의 절대경로를 파악한다.
	 *
	 * @param   filename   위치를 얻으려는 파일명
	 * @return    file의 경로
	 */
	public static String getConfFileLocation(String filename) throws NException {
		try {
			nsf_home = System.getProperty("nsf.home");
            File default_file = new File(nsf_home + "/conf", filename);
			if (!default_file.exists()) {
				throw new Exception("File not Exists, Check the Configuration XML file");
			}
			return default_file.getAbsolutePath();
		} catch (Exception e) {
			System.err.println(
				NLogUtils.toDefaultLogForm(
					"NConfiguration",
					"getConfFileLocation(" + filename + ")",
					"NSF_CNF_000",
					"Check the nsf.xml file location"));
			throw new NException("NSF_CNF_000", "Check the nsf.xml file location", e);
		}

	}
	
	/**
	 * System property에서 nsf_home의 값을 얻어온다.
	 *	 
	 * @return    System property에서 nsf_home의 값
	 */

	/**
	 * NConfiguration을 생성한다. 내부적으로 singleton pattern을 적용하여, 단 하나의 NConfiguration Instance를 사용한다.	 
	 *
	 * @return  NConfiguration의 instance.
	 * @throws NException configuration error가 발생할 경우.
	 */
	public static synchronized NConfiguration getInstance() throws NException {
		try {
			if (xc == null) {
				xc = new NConfiguration(getConfFileLocation(default_file_name));
			}
			return xc;
		} catch (NException e) {
			System.err.println(
				NLogUtils.toDefaultLogForm(
					"NConfiguration",
					"getInstance()",
					"NSF_CNF_001",
					"Error on loading Configuration Instance"));
			throw new NException(
				"NSF_CNF_001",
				"Error on loading Configuration Instance",
				e);
		}
	}

	/**
	 * 이미 존재하는 org.w3c.dom.Document instance에 기반한
	 * NConfiguration object 를 생성한다.
	 * 이 document는 root element를 반드시 가져야 한다.
	 *
	 * @param   doc     기존 Document instance
	 * @throws  NException - doc이 root element를 가지고 있지 않을때
	 */
	private void init(Document doc) throws NException {

		this.utils = NXmlUtils.getImpl();
		this.doc = doc;

		configurationMap = null; // init  
		configurationMap = new LinkedHashMap();

		Node root = doc.getDocumentElement();
		// Check the existence of the root element
		if (root == null) {

			System.err.println(
				NLogUtils.toDefaultLogForm(
					"NConfiguration",
					"init(Document doc)",
					"NSF_CNF_002",
					"Error on initiating DOM : Check the root element of nsf.xml document"));

			throw new NException(
				"NSF_CNF_002",
				"Error on initiating DOM : Check the root element of nsf.xml document");
		} else {

			createConfigurationMap(root);
			// 초기화 작업 nsf.xml 의 모든 내용을 HashMap 화 

			NodeList children = root.getChildNodes();
			String rootNodeNm = root.getNodeName();
			for (int inx = 0; inx < children.getLength(); inx++) {
				Node childNode = children.item(inx);
				if (!childNode.getNodeName().equals("nsf")
					&& childNode.getNodeType() == Node.ELEMENT_NODE) {

					// 프로젝트 설정 외부 파일인 경우 처리 

					String childNodeName = childNode.getNodeName();
					String childNodeAttrValue = childNode.getAttributes().item(0).getNodeValue();
					//					NodeList childList= childNode.getChildNodes();

					String fullPath =
						rootNodeNm
							+ GUBUN
							+ childNodeName
							+ "<"
							+ childNodeAttrValue
							+ ">"
							+ GUBUN
							+ "src";
					String srcPath = get(fullPath);

					fullPath =
						rootNodeNm
							+ GUBUN
							+ childNodeName
							+ "<"
							+ childNodeAttrValue
							+ ">"
							+ GUBUN
							+ "validate";
					String validate = get(fullPath);

					boolean validateBool = new Boolean(validate).booleanValue();
					Document subDoc = parsing(srcPath, validateBool);

					createConfigurationMap(
						subDoc.getDocumentElement(),
						rootNodeNm + GUBUN + childNodeName + "<" + childNodeAttrValue + ">");

					NodeList nlst = subDoc.getDocumentElement().getChildNodes();
					int len = nlst.getLength();
					Node nd = null;
					for (int k = 0; k < len; k++) {
						nd = doc.importNode(nlst.item(k), true);
						children.item(inx).appendChild(nd);
					}

				}

			}

		}
	}

	/**
	 * XML resource를 parse하고 NConfiguration object를 생성한다.
	 *
	 * @param   name        XML resource의 name
	 * @param   validating  XML validation을 위한 flag
	 * @throws  NException configuration error가 발생할 경우.
	 */
	private NConfiguration(String name) throws NException {
		try {
			boolean validating = false;
			doc = parsing(name, validating);
			System.out.println("Document : " + doc.toString());
			init(doc);
		} catch (Exception e) {
			System.err.println(
				NLogUtils.toDefaultLogForm(
					"NConfiguration",
					"NConfiguration(String name, boolean validating)",
					"NSF_CNF_003",
					"Error on parsing : Check the nsf.xml"));

			throw new NException(
				"NSF_CNF_003",
				"Error on parsing : Check the nsf.xml",
				e);
		}
	}

	/**
	 * nsf.xml 파일을 읽어 DOM 객체의 ELEMENT_NODE를  key로 하고
	 * TEXT_NODE를 value로 한 configurationMap을 만든다. 
	 *
	 * @param  node        configuration file을 parsing하여 만든 DOM객체의 Root Node
	 * @throws NException configuration error가 발생할 경우.
	 */
	private static void createConfigurationMap(Node node) throws NException {
		createConfigurationMap(node, node.getNodeName());
	}

	/**
	 * nsf.xml 파일을 읽어 DOM 객체의 ELEMENT_NODE를  key로 하고
	 * TEXT_NODE를 value로 한 configurationMap을 만든다. 
	 *
	 * @param  node        configuration file을 parsing하여 만든 DOM객체의 Root Node
	 * @param  header    configuration file을 parsing하여 만든 DOM객체를 mapping 하는 LinkedHashMap에 
	 *                                 key를 만들어 넣을 때 붙일 header
	 * @throws NException configuration error가 발생할 경우.
	 */
	private static void createConfigurationMap(Node node, String header)
		throws NException {
		try {
			short NODE_TYPE = node.getNodeType();

			if (NODE_TYPE == Node.ELEMENT_NODE) {
				NodeList children = node.getChildNodes();
				if (children != null) { // 그 수 만큼 재귀호출				 
					for (int i = 0, size = children.getLength(); i < size; i++) {
						// element인 경우 attribute에 대한 처리를 해주어야 한다.
						Node childNode = children.item(i);
						String childNodeName = childNode.getNodeName();
						short CHILD_NODE_TYPE = childNode.getNodeType();
						String childNodeValue = childNode.getNodeValue();

						NamedNodeMap attrNodeList = childNode.getAttributes();
						if (attrNodeList != null && attrNodeList.getLength() > 0) {

							for (int attrListInx = 0, sizeInx = attrNodeList.getLength();
								attrListInx < sizeInx;
								attrListInx++) {
								createConfigurationMap(
									childNode,
									header
										+ GUBUN
										+ childNodeName
										+ "<"
										+ attrNodeList.item(attrListInx).getNodeValue()
										+ ">");
								// 재귀호출
							}
						} else {
							if (CHILD_NODE_TYPE == Node.TEXT_NODE
								&& compareWhiteSpace(childNodeValue)) {
								createConfigurationMap(childNode, header);
								// 재귀호출
							} else {
								createConfigurationMap(childNode, header + GUBUN + childNodeName);
								// 재귀호출
							}

						}

					}
				}
			} else if (NODE_TYPE == Node.TEXT_NODE) { // text인 경우		

				String mapValue = node.getNodeValue();
				if (mapValue != null && compareWhiteSpace(mapValue)) {
					mapValue = mapValue.trim();
					String mapKey = header;
					// mapValue #home 변신 코드 삽입
					if (mapValue.startsWith(NSF_HOME_TAG)) {
						mapValue = nsf_home + mapValue.substring(NSF_HOME_TAG__SIZE);
					}
					node.setNodeValue(mapValue);
					configurationMap.put(mapKey, mapValue);
				}

			}else if (NODE_TYPE == Node.CDATA_SECTION_NODE) { // text인 경우		

				String mapValue = node.getNodeValue();
				if (mapValue != null && compareWhiteSpace(mapValue)) {
					mapValue = mapValue.trim();
					String mapKey = header.substring(0,header.indexOf("#cdata")-1);
					// mapValue #home 변신 코드 삽입
					if (mapValue.startsWith(NSF_HOME_TAG)) {
						mapValue = nsf_home + mapValue.substring(NSF_HOME_TAG__SIZE);
					}
					node.setNodeValue(mapValue);
					configurationMap.put(mapKey, mapValue);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();

			System.err.println(
				NLogUtils.toDefaultLogForm(
					"NConfiguration",
					"createConfigurationMap(Node node, String header)",
					"NSF_CNF_004",
					"Error on generating configurationMap"));

			throw new NException(
				"NSF_CNF_004",
				"Error on generating configurationMap",
				e);
		}
	}

	/**
	 * parameter로 넘어온 key에서 공백 유무를 점검한다.
	 *
	 * @param  String str   
	 * @return  boolean
	 */
	private static boolean compareWhiteSpace(String str) {

		char[] charStr = str.toCharArray();

		for (int i = 0, size = charStr.length; i < size; i++) {
			if (!Character.isWhitespace(charStr[i]))
				return true; // String이 존재한다. 
		}
		return false; // White Space만 존재한다.

	}

	/**
	 * configuration 파일이 변경되었을 경우, 변경된 값을 반영하기 위해 사용되는 Method이다.
	 *
	 * @throws NException - configuration error가 발생할 경우.
	 */
	public synchronized void refresh() throws NException {
		try {
			doc = parsing(getConfFileLocation(default_file_name), false);
		} catch (Exception e) {

			System.err.println(
				NLogUtils.toDefaultLogForm(
					"NConfiguration",
					"refresh() ",
					"NSF_CNF_005",
					"Error on refreshing the nsf.xml DOM"));

			throw new NException(
				"NSF_CNF_005",
				"Error on refreshing the nsf.xml DOM ",
				e);
		}

		init(doc);
		setChanged();
		notifyObservers();
	}

	/**
	* 해당 Configuration file을 parsing하여 dom tree를 생성한다.
	*
	* @param parsing할 파일 name
	* @param DTD check 여부
	* @return  생성된 Document
	* @throws NException - configuration error가 발생할 경우.
	*/
	private Document parsing(String name, boolean validating) throws NException {
		try {
			Document temp;

			File conf_file = new File(name);
			FileInputStream in = new FileInputStream(conf_file);

			if (conf_file == null || !conf_file.exists() || !conf_file.canRead()) {
				throw new NException(
					"NSF_CNF_006",
					"Error on parsing ",
					new IOException("Failed open the [" + name + "] File : "));
			}
			try {
				this.utils = NXmlUtils.getImpl();
				temp = NXmlUtils.parse(new java.io.BufferedInputStream(in), validating);
				return temp;
			} catch (NException e) {
				System.err.println(
					NLogUtils.toDefaultLogForm(
						"NConfiguration",
						"parsing(String name, boolean validating)",
						e.getMessage()));

				throw new NException("NSF_CNF_006", "Error on parsing ", e);
			} finally {
				if (in != null)
					in.close();
			}

		} catch (FileNotFoundException e) {
			System.err.println(
				NLogUtils.toDefaultLogForm(
					"NConfiguration",
					"parsing(String name, boolean validating)",
					e.getMessage()));

			throw new NException("NSF_CNF_006", "Error on parsing ", e);
		} catch (IOException e) {

			System.err.println(
				NLogUtils.toDefaultLogForm(
					"NConfiguration",
					"parsing(String name, boolean validating)",
					e.getMessage()));

			throw new NException("NSF_CNF_006", "Error  on parsing ", e);
		}
	}

	/**
	 * <pre>
	 * Configuration 파일로 부터 특정 위치의 정보를 얻어 올때 사용한다.
	 * 인자 값으로 특정 정보가 위치한 경로를 지정한다.
	 * 
	 * </pre>
	 * @param   String key  특정 정보가 위치한 경로
	 *                  e.g. /configuration/dataSources/dataSource<db2>/providerUrl
	 * @return  String value
	 * @throws NException configuration error가 발생할 경우.
	 */
	public String get(String key) throws NException {
		if (configurationMap == null) {

			System.err.println(
				NLogUtils.toDefaultLogForm(
					"NConfiguration",
					"get(String key)",
					"NSF_CNF_007",
					"Error on loading configurationMap"));

			throw new NException("NSF_CNF_007", "Error on loading configurationMap");
		}
		if (key.charAt(0) == GUBUN) {
			key = key.substring(1);
		}

		String value = (String) configurationMap.get(key);

		if (value == null) {
			return "";
		} else {
			return value;
		}

	}

	/**
	 * <pre>
     * Configuration 파일로 부터 특정 위치의 정보를 얻어 올때 사용한다.
	 * 인자 값으로 특정 정보가 위치한 경로와 값이 없을 경우 Default로 사용될 값을 지정한다.
	 * </pre>
	 * @param   String key   특정 정보가 위치한 경로
	 * @return  String value Default 값
	 * @throws NException configuration error가 발생할 경우.
	 */
	public String getString(String elemPath, String defaultValue) throws NException {
		try {
			String value = get(elemPath);

			if (value.equals("")) {
				return defaultValue;
			} else {
				return value;
			}
		} catch (Exception e) {

			System.err.println(
				NLogUtils.toDefaultLogForm(
					"NConfiguration",
					"getString(String elemPath, String defaultValue)",
					"NSF_CNF_008",
					"Error on getter method of configurationMap"));

			throw new NException(
				"NSF_CNF_008",
				"Error on getter method of configurationMap",
				e);
		}
	}

	/**
	* Boolean 형태의 XML ELEMENT의 값을 얻어온다.
	* parameter로 넘겨주는 element의 path는 XPath string을 만들기 위해 사용한다.
	*
	* @param   String elemPath    element의 절대경로
	* @param   int defaultValue     value가 없는 경우 리턴 될  boolean type 디폴트 값
	* @return  element data의 boolean value
	* @throws   NException - XPath string을 evaluate하는 동안 error 발생시
	*/
	public boolean getBoolean(String elemPath, boolean defaultValue)
		throws NException {
		try {
			String value = get(elemPath);
			String temp = value.toUpperCase();
			if ("TRUE".equals(temp) || "FALSE".equals(temp)) {
				return new Boolean(value.trim()).booleanValue();
			} else {
				return defaultValue;
			}

		} catch (Exception e) {

			System.err.println(
				NLogUtils.toDefaultLogForm(
					"NConfiguration",
					"getBoolean(String elemPath, boolean defaultValue)",
					"NSF_CNF_008",
					"Error on getter method of configurationMap"));

			throw new NException(
				"NSF_CNF_008",
				"Error on getter method of configurationMap",
				e);
		}
	}

	/**
	* Integer 형태의 XML ELEMENT의 값을 얻어온다.
	* parameter로 넘겨주는 element의 path는 XPath string을 만들기 위해 사용한다.
	*
	* @param   String elemPath    element의 절대경로
	* @param   int defaultValue     value가 없는 경우 리턴 될  int type 디폴트 값
	* @return    element data의 int value
	* @throws   NException - XPath string을 evaluate하는 동안 error 발생시
	*/
	public int getInt(String elemPath, int defaultValue) throws NException {
		try {
			String value = get(elemPath);
			if (value.equals("")) {
				return defaultValue;
			} else {
				return new Integer(value.trim()).intValue();
			}
		} catch (Exception e) {

			System.err.println(
				NLogUtils.toDefaultLogForm(
					"NConfiguration",
					"getInteger(String elemPath, int defaultValue)",
					"NSF_CNF_008",
					"Error on getter method of configurationMap"));

			throw new NException(
				"NSF_CNF_008",
				"Error on getter method of configurationMap",
				e);
		}
	}

	/**
	 * Long 형태의 XML ELEMENT의 값을 얻어온다.
	 * parameter로 넘겨주는 element의 path는 XPath string을 만들기 위해 사용한다.
	 *
	 * @param   String elemPath    element의 절대경로
	 * @param    long defaultValue  value가 없는 경우 리턴 될 long type 디폴트 값
	 * @return    element data의 long value
	 * @throws   NException - XPath string을 evaluate하는 동안 error 발생시
	 */
	public long getLong(String elemPath, long defaultValue) throws NException {
		try {
			String value = get(elemPath);
			if (value.equals("")) {
				return defaultValue;
			} else {
				return new Long(value.trim()).longValue();
			}
		} catch (Exception e) {

			System.err.println(
				NLogUtils.toDefaultLogForm(
					"NConfiguration",
					"getLong(String elemPath, long defaultValue)",
					"NSF_CNF_008",
					"Error on getter method of configurationMap"));

			throw new NException(
				"NSF_CNF_008",
				"Error on getter method of configurationMap",
				e);
		}
	}

	// seperator003. 이하 DOM 객체에서 get()해오는 methods

	/**
	 * XPath를 사용하여 XML ELEMENT의 값을 얻어온다.
	 *
	 * @param   path    element의 절대경로
	 *                 e.g. /Configuration/DataSources/DataSource<3>/name
	 *                 (node의 first child의 index는 1이다.)
	 * @return    Element object
	 * @throws   NException - XPath사용중 error발생시, 
	 * @throws   IllegalArgumentException - node가 element가 아닐 경우 혹은 존재하지 않을때
	 */
	public Element getElement(String elemPath)
		throws NException, IllegalArgumentException {
		try {
			NodeList list = utils.evalToNodeList(doc, elemPath);
			Node node = (list.getLength() > 0) ? list.item(0) : null;
			if (node instanceof Element)
				return (Element) node;
			else
				System.err.println(
					NLogUtils.toDefaultLogForm(
						"NConfiguration",
						"getElement(String elemPath)",
						"NSF_CNF_009",
						"IllegalArguementException : "
							+ elemPath
							+ " isn't the path of an element"));

			throw new NException(
				"NSF_CNF_009",
				"IllegalArguementException : " + elemPath + " isn't the path of an element",
				new IllegalArgumentException());
		} catch (Exception e) {

			System.err.println(
				NLogUtils.toDefaultLogForm(
					"NConfiguration",
					"getElement(String elemPath)",
					"NSF_CNF_010",
					"Error on getter method of DOM"));

			throw new NException("NSF_CNF_010", "Error on getter method of DOM", e);
		}
	}

	/**
	 * 동일한 절대 경로 및 이름을 가지는 XML Element의 개수를 되돌린다.
	 *
	 * @param   parentPath  parent element의 절대경로
	 * @param   elemName    element의 이름
	 * @return  element의 수
	 * @throws  NException - XPath string을 evaluate하는 중에 error 발생시
	 */
	public int getElementCount(String parentPath, String elemName) throws NException {
		try {
			String path = parentPath + GUBUN + elemName;
			NodeList list = utils.evalToNodeList(doc, path);
			return list.getLength();
		} catch (Exception e) {

			System.err.println(
				NLogUtils.toDefaultLogForm(
					"NConfiguration",
					"getElementCount(String parentPath, String elemName)",
					"NSF_CNF_010",
					"Error on getter method of DOM"));

			throw new NException("NSF_CNF_010", "Error on getter method of DOM", e);
		}
	}

	/**
	* DOM의 document 객체를 되돌린다.
	*
	* @return    현재 Document tree
	*/
	public Document getDom() {
		return doc;
	}

	/**
	* XPath를 지정하여 ELEMENT nodelist를 얻어온다.
	*
	* @param   elemPath    element의 절대경로
	* @return    NodeList
	* @throws   SAXException - XPath string을 evaluate하는 동안 error 발생시
	*/
	public NodeList getNodeList(String elemPath) throws NException {
		try {
			return utils.evalToNodeList(doc, elemPath);
		} catch (Exception e) {

			System.err.println(
				NLogUtils.toDefaultLogForm(
					"NConfiguration",
					"getNodeList(String elemPath)",
					"NSF_CNF_010",
					"Error on getter method of DOM"));

			throw new NException("NSF_CNF_010", "Error on getter method of DOM", e);
		}
	}

	/**
	* XPath를 지정하여 ELEMENT node를 얻어온다.
	*
	* @param   elemPath    element의 절대경로
	* @return    Node
	* @throws   SAXException - XPath string을 evaluate하는 동안 error 발생시
	*/
	public Node getNode(String elemPath) throws NException {
		try {
			return utils.selectSingleNode(doc, elemPath);
		} catch (Exception e) {

			System.err.println(
				NLogUtils.toDefaultLogForm(
					"NConfiguration",
					"getNode(String elemPath)",
					"NSF_CNF_010",
					"Error on getter method of DOM"));

			throw new NException("NSF_CNF_010", "Error on getter method of DOM", e);
		}
	}

	/**
	 * XPath를 지정하여 element의 attribute값을 얻어온다.
	 * parameter로 넘겨지는 element의 path와 attribute의 name은
	 * XPath string을 구성하는데 사용한다 : string(elemPath/@attrName)
	 *
	 * @param   elemPath    element의 절대경로
	 * @param   attrName    attribute의 이름 
	 * @return    attribute의 값
	 * @throws   SAXException - XPath string을 evaluate하는 동안 error 발생시
	 */
	public String getAttribute(String elemPath, String attrName) throws NException {
		String path = elemPath + GUBUN + '@' + attrName;
		try {
			return get(path);
		} catch (Exception e) {

			System.err.println(
				NLogUtils.toDefaultLogForm(
					"NConfiguration",
					"getAttribute(String elemPath, String attrName)",
					"NSF_CNF_010",
					"Error on getter method of DOM"));

			throw new NException("NSF_CNF_010", "Error on getter method of DOM", e);
		}
	}

	/**
	 * 동일한 절대 경로 및 이름을 가지는 XML Element의 값들을 되돌린다.
	 * character data를 얻기 위해 XPath를 사용한다.
	 *
	 * @param   arrayPath   parent element의 절대경로
	 * @param  arrName     array를 가지고 있는 element name
	 * @param  elemName  element의 이름
	 * @return   element의 character data
	 * @throws  SAXException - XPath string을 evaluate하는 동안 error 발생시
	 */
	public String[] getArray(String arrayPath, String arrName, String elemName)
		throws NException {
		try {
			int length = 0;
			length = getElementCount(arrayPath, arrName);
			String array[] = new String[length];
			for (int i = 0, j = 1; i < length; i++, j++) {
				String path = arrayPath + GUBUN + arrName + '[' + j + ']' + GUBUN + elemName;
				array[i] = get(path);
			}
			return array;
		} catch (Exception e) {

			System.err.println(
				NLogUtils.toDefaultLogForm(
					"NConfiguration",
					"getArray(String arrayPath, String arrName, String elemName)",
					"NSF_CNF_010",
					"Error on getter method of DOM"));

			throw new NException("NSF_CNF_010", "Error on getter method of DOM", e);
		}
	}

	/**
	* 현재 Document tree의 내용을 file형태 혹은 console로 출력한다.
	*
	* @param   gb    output target을 parameter로 받는다.
	*/
	public void printConfDom(String gb) {
		if ((gb.trim()).equals("file"))
			NShowDomTree.saveDocAsFile(doc, nsf_home + "/conf/dom.xml");
		else {
			System.out.println(NShowDomTree.returnDocAsString(doc));
		}
	}

	/**
	* 현재 Document tree의 내용을 string값으로 되돌린다.
	*
	* @return   현재 dom tree의 내용
	*/
	public String showConfDom() {
		return NShowDomTree.returnDocAsString(doc);
	}

    /**
     * nsf.home의 값을 얻어온다.
     * 
     * @return nsf.home의 값
     */
    public static String getNsfHome() {
        return System.getProperty("nsf.home");
    }
}

