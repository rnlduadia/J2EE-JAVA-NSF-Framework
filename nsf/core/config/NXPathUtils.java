package nsf.core.config;

/**
 * @(#) NXPathUtils.java
 */

import javax.xml.transform.TransformerException;

import nsf.core.exception.NException;
import nsf.core.log.NLogUtils;

import org.apache.xpath.XPathAPI;
import org.apache.xpath.objects.XObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class NXPathUtils extends NXmlUtils {

	/**
	 * <pre>
	 * XPath utility를 제공한다.
	 * </pre>
	 * 
     * @since 2007년 1월 5일
     * @version NSF1.0
     * 
     * @author 조광혁, Nova China         
	 */

	/**
	 * XPath string을 evaluate하고 XObject 타입으로 반환한다.
	 *
	 * @param   doc     context node가 root인 document
	 * @param   str       XPath string
	 * @return    결과 XObject
	 * @throws   SAXException - XPath string을 evaluate하는 동안 error발생시
	 */
	protected XObject eval(Document doc, String str) throws TransformerException {

		Node root = doc.getDocumentElement();
		return XPathAPI.eval(root, str);
	}

	/**
	 * XPath string을 evaluate하고 결과를 NodeList 타입으로 반환한다.
	 * document의 root element가 context node로 사용된다.
	 *
	 * @param   doc     context node가 root인 document
	 * @param   str       XPath string
	 * @return    결과 node list
	 * @throws  SAXException - XPath string을 evaluate하는 동안 error발생시
	 */
	public NodeList evalToNodeList(Document doc, String str) throws NException {
		try {
			return (NodeList) eval(doc, str).nodelist();

		} catch (TransformerException e) {

			System.err.println(
				NLogUtils.toDefaultLogForm(
					"NXPathUtils",
					"evalToNodeList(Document doc, String str)",
					"NSF_CNF_017",
					"Cannot get a data for " + str));

			throw new NException("NSF_CNF_017", "Cannot get a data for " + str, e);
		}
	}

	/**
	 * XPath string을 evaluate하고 결과를 node 타입으로 반환한다.
	 * document의 root element가 context node로 사용된다.
	 *
	 * @param   doc     context node가 root인 document
	 * @param   str       XPath string
	 * @return    결과 node 
	 * @throws  SAXException - XPath string을 evaluate하는 동안 error발생시
	 */
	public Node selectSingleNode(Document doc, String str) throws NException {
		try {
			return XPathAPI.selectSingleNode(doc, str);

		} catch (TransformerException e) {

			System.err.println(
				NLogUtils.toDefaultLogForm(
					"NXPathUtils",
					"selectSingleNode(Document doc, String str)",
					"NSF_CNF_017",
					"Cannot get a data for " + str));

			throw new NException("NSF_CNF_017", "Cannot get a data for " + str, e);
		}
	}
}

