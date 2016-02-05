package nsf.support.tools.xml;

/**
 * @(#) NShowDomTree.java
 * 
 */

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

/**
 * <pre>
 * memory상에 떠있는 DOM tree의 모습을 String이나
 * File형태로 보여주는 Util이다.
 * 
 * 이 NShowDomTree Class는 JDK V1.4이상에서 동작한다.
 * </pre>
 * 
 * @since 2007년 1월 5일
 * @version NSF 1.0
 * 
 * @author 강영진, Nova China 
 */
public class NShowDomTree {

    private NShowDomTree() {
    }
    
	/**
	 * Configuration XML File을 Parsing하여 생성한 DOM을 File로 저장한다. 
	 *
	 * @param   doc          Configuration XML File을 Parsing하여 얻은 Document
     * @param   fileName   저장하려는 파일명
	 * @return    file
	 */
	public static void saveDocAsFile(Document doc, String fileName) {
		try {
			TransformerFactory tfFac = TransformerFactory.newInstance();
			// use null transformation
			Transformer tf = tfFac.newTransformer();

			tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

			tf.transform(new DOMSource(doc), new StreamResult(new FileWriter(fileName)));
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}
    /**
     * Configuration XML File을 Parsing하여 생성한 DOM을 String으로 return한다. 
     *
     * @param   doc          Configuration XML File을 Parsing하여 얻은 Document
     * @return   String
     */
    	public static String returnDocAsString(Document doc) {

		StringWriter sw = new StringWriter();
		try {
			TransformerFactory tfFac = TransformerFactory.newInstance();
			// use null transformation
			Transformer tf = tfFac.newTransformer();

			tf.transform(new DOMSource(doc), new StreamResult(sw));
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return sw.toString();
	}

}

