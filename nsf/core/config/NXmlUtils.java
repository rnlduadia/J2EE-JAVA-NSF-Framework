package nsf.core.config;

/**
 * @(#) NXmlUtils.java
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import nsf.core.exception.NException;
import nsf.core.log.NLogUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * <pre>
 * JAXP API를 이용하여 parser에 독립적인 XML utility을 제공한다.
 * </pre>
 * 
 * @since 2007년 1월 5일
 * @version NSF1.0
 * 
 * @author 조광혁, Nova China         
 */

public abstract class NXmlUtils {

	protected static DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	protected static DocumentBuilderFactory vdbf = DocumentBuilderFactory.newInstance();
	static {
		dbf.setValidating(false);
		vdbf.setValidating(true);
	}

	/**
	 * document builder를 생성한다.
	 *
	 * @param   validating XML validation을 위한 validating  flag
	 * @return    DocumentBuilder object
	 * @throws   NException - ParserConfigurationException 발생시
	 */
	public static DocumentBuilder createBuilder(boolean validating)
		throws NException {
		try {
			DocumentBuilder builder = null;

			if (validating)
				builder = vdbf.newDocumentBuilder();
			else
				builder = dbf.newDocumentBuilder();

			builder.setEntityResolver(new EntityResolver() {

				public InputSource resolveEntity(String publicID, String systemID) {

					publicID = System.getProperty("nsf.home") + "/conf/" + publicID;
					systemID = System.getProperty("nsf.home") + "/conf/" + systemID;

					if (publicID != null && publicID.endsWith(".dtd")) {
						try {
							FileInputStream in = new FileInputStream(publicID);
							if (in != null) {
								return new InputSource(in);
							}
						} catch (FileNotFoundException e) {
							throw new InternalError(e.getMessage());
						}
					}
					return null;
				}
			});

			return builder;
		} catch (InternalError e) {

			System.err.println(
				NLogUtils.toDefaultLogForm(
					"NXmlUtils",
					"createBuilder(boolean validating)",
					e.getMessage()));

			throw new NException(
				"NSF_CNF_011",
				"Error caused by InternalError on parsing",
				e);
		} catch (ParserConfigurationException e) {

			System.err.println(
				NLogUtils.toDefaultLogForm(
					"NXmlUtils",
					"createBuilder(boolean validating)",
					e.getMessage()));

			throw new NException(
				"NSF_CNF_012",
				"Error caused by ParserConfigurationException on parsing",
				e);

		}
	}

	/**
	 * empty document를 생성한다.
	 *
	 * @return      생성된 document
	 */
	public static Document createEmptyDocument() throws Exception {
		try {
			return createBuilder(false).newDocument();
		} catch (Exception e) {
			throw e;
		}

	}

	/**
	 * XML 파일을 parse하고 org.w3c.dom.Document 인스턴스를 반환한다.
	 *
	 * @param   file            XML file
	 * @param   validating  XML validation을 위한 flag
	 * @return    Document object
	 * @throws  IOException  -  I/O error 발생시
	 * @throws  SAXException - parsing과정에서 error발생시
	 */
	public static Document parse(File file, boolean validating) throws NException {
		try {
			return createBuilder(validating).parse(file);
		} catch (IOException e) {

			System.err.println(
				NLogUtils.toDefaultLogForm(
					"NXmlUtils",
					"parse(File file, boolean validating)",
					e.getMessage()));

			throw new NException(
				"NSF_CNF_013",
				"Error caused by IOException on parsing",
				e);
		} catch (SAXException e) {

			System.err.println(
				NLogUtils.toDefaultLogForm(
					"NXmlUtils",
					"parse(File file, boolean validating)",
					e.getMessage()));

			throw new NException(
				"NSF_CNF_014",
				"Error caused by SAXException on parsing",
				e);
		}
	}

	/**
	 * XML input stream을 parse하고 org.w3c.dom.Document 인스턴스를 반환한다.
	 *
	 * @param   in              XML input stream
	 * @param   validating  XML validation을 위한 flag
	 * @return    Document object
	 * @throws  IOException      -  I/O error 발생시
	 * @throws  SAXException   -  parsing과정에서 error발생시
	 */
	public static Document parse(InputStream in, boolean validating)
		throws NException {
		try {
			return createBuilder(validating).parse(in);
		} catch (IOException e) {

			System.err.println(
				NLogUtils.toDefaultLogForm(
					"NXmlUtils",
					"parse(InputStream in, boolean validating)",
					e.getMessage()));

			throw new NException(
				"NSF_CNF_013",
				"Error caused by IOException on parsing",
				e);

		} catch (SAXException e) {

			System.err.println(
				NLogUtils.toDefaultLogForm(
					"NXmlUtils",
					"parse(InputStream in, boolean validating)",
					e.getMessage()));

			throw new NException(
				"NSF_CNF_014",
				"Error caused by SAXException on parsing",
				e);
		}
	}

	/**
	 * XPath string을 evaluate하고 결과를 NodeList로 반환한다.
	 * document의 root element가 context node로 사용된다.
	 * 
	 * @param   doc    context node가 root인 document
	 * @param   str      XPath string
	 * @return    결과 node lists
	 * @throws   SAXException - XPath string의 evaluation과정에서 error발생시
	 */
	public abstract NodeList evalToNodeList(Document doc, String str)
		throws NException;

	/**
	 * XPath string을 evaluate하고 결과를 Node로 반환한다.
	 * document의 root element가 context node로 사용된다.
	 * 
	 * @param   doc    context node가 root인 document
	 * @param   str      XPath string
	 * @return    결과 node 
	 * @throws   SAXException - XPath string의 evaluation과정에서 error발생시
	 */
	public abstract Node selectSingleNode(Document doc, String str) throws NException;

	protected static Class utilsClass = null;

	static {

		// Load the class
		String className = "nsf.core.config.NXPathUtils";

		try {
			//utilsClass = Class.forName(className);		
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			utilsClass = classLoader.loadClass(className);			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * NXmlUtils instance를 생성 반환한다.
	 *
	 * @return      utils class의 new instance
	 */
	public static NXmlUtils getImpl() throws NException {

		try {
			if (utilsClass == null)
				throw new InternalError("Don't have a NXmlUtils implementation");

			// Return a new instance of the utils class
			return (NXmlUtils) utilsClass.newInstance();
		} catch (InternalError e) {

			System.err.println(
				NLogUtils.toDefaultLogForm("NXmlUtils", "getImpl()", e.getMessage()));

			throw new NException("NSF_CNF_015", "NXmlUtils class exists", e);
		} catch (Exception e) {

			System.err.println(
				NLogUtils.toDefaultLogForm("NXmlUtils", "getImpl()", e.getMessage()));
			e.printStackTrace();
			throw new NException(
				"NSF_CNF_016",
				"Couldn't instantiate the NXmlUtils implementation",
				e);
		}
	}
}

