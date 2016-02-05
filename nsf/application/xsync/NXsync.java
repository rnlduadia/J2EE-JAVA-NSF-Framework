package nsf.application.xsync;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <pre>
 * aJax로 자료를 전송하는 기능.
 * </pre>
 * 
 * @since 2007/1/5
 * @version NSF 1.0
 * @author 김철남 Nova China
 */
public class NXsync {

	private final List nodes = new ArrayList();

	private final String encode;

	public NXsync(String encode) {
		this.encode = encode;
	}

	public NXsync() {
		this.encode = "UTF-8";
	}


	public void add(NXsyncable handler) {
		nodes.add(handler);
	}

	public String toXmlString() {
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version='1.0' encoding=\"").append(this.encode).append("\"?>\r\n");
		sb.append("<xsync>\r\n");

		Iterator i = nodes.iterator();
		while(i.hasNext()) {
			NXsyncable peer = (NXsyncable)i.next();
			sb.append(peer.toXmlString());
		}

		sb.append("</xsync>");
		return sb.toString();

	}

	public void fire(HttpServletRequest req, HttpServletResponse res) throws Exception {
		res.setContentType("application/xml; charset=" + this.encode);
		PrintWriter pw = null;
		try {
			pw = res.getWriter();
			pw.write(this.toXmlString());
		} catch (Exception e) {
			pw.write("<?xml version='1.0' encoding=\""+this.encode+"\"?>\r\n");
			pw.write("<xsync error=\"true\">\r\n");
			pw.write("<![CDATA[" + e + "]]>");
			pw.write("</xsync>");

		} finally {
			pw.flush();
			pw.close();
		}

	}
}
