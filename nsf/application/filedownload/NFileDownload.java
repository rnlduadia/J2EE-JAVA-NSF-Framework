package nsf.application.filedownload;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nsf.core.exception.NException;
import nsf.support.tools.util.CastUtil;

/**
 * <pre>
 * 파일을 다운로드한다.
 * </pre>
 * 
 * @since 2007/1/5
 * @version NSF 1.0
 * @author 김철남 Nova China
 */

public class NFileDownload {

	private static final int BUFFER_SIZE = 4096;

	private final HttpServletRequest request;

	private final HttpServletResponse response;

	private String contentType = "application/octect-stream";

	private String encoding = "UTF-8";

	public NFileDownload(HttpServletRequest req, HttpServletResponse res) {
		super();
		this.request = req;
		this.response = res;
	}

    /**
     * 파일을 클라이언트로 다운로드한다.
     * @param url
     * @throws NException
     */
	public void redirectTo(String url) throws NException {
		try {
			response.sendRedirect(url);
		} catch (IOException e) {
			throw new NException("IOExcetion occur : " + url, e);
		}
	}

    /**
     * 파일을 복사한다.
     * @param file
     * @throws NException
     */
	public void streamTo(File file) throws NException {
		streamTo(file, file.getName());
	}

    /**
     * 파일을 복사한다.
     * @param file,String alias
     * @throws NException
     */
	public void streamTo(File file, String alias) throws NException {
		FileInputStream fin = null;
		BufferedOutputStream fout = null;

		try {
			if (!file.canRead()) {
				throw new NException("file cannot read : " + file.getName());
			}

			response.reset();

			setHeaders(alias, file.length());

			fin = new FileInputStream(file);
			FileChannel fc = fin.getChannel();
			fout = new java.io.BufferedOutputStream(response.getOutputStream());

			ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
			int length = -1;
			while ((length = fc.read(buffer)) != -1) {
				buffer.flip();
				fout.write(buffer.array(), 0, length);
				buffer.clear();
			}
		} catch (FileNotFoundException e) {
			throw new NException("file not found : " + file.getName(), e);
		} catch (IOException e) {
			throw new NException("IOExcetion occur : " + file.getName(), e);
		} finally {
			if (fin != null)
				try {
					fin.close();
				} catch (Exception e) {
				}
			if (fout != null)
				try {
					fout.flush();
					fout.close();
				} catch (Exception e) {
				}
		}
	}

    /**
     * 파일의 header정보를 설정한다.
     * @param filename
     * @param filelength
     */
	private void setHeaders(String filename, long filelength) {

		response.setContentType(getContentType(filename));
	
		if (request.getHeader("User-Agent").indexOf("MSIE 5.5") != -1) {
			response.setHeader("Content-Disposition", "filename=" + getEncodedName(filename) + ";");
		} else {
			response.setHeader("Content-Disposition", "attachment;filename=" + getEncodedName(filename) + ";");
		}

		response.setHeader("Content-Length", new CastUtil(filelength).cString());
		response.setHeader("Content-Transfer-Encoding", "binary;");
		response.setHeader("Pragma", "no-cache;");
		response.setHeader("Expires", "-1;");
	}

    /**
     * encode값을 얻는다.
     * @param name
     * @return
     */
	private String getEncodedName(String name) {
        String filename = name;
        try {
            String userAgent = request.getHeader("User-Agent");
            //IE대응
           if (userAgent.indexOf("MSIE") > -1) {
               filename = java.net.URLEncoder.encode(name, "UTF-8");
           } 
           //Firefox대응
           else {
                filename = new String(name.getBytes("UTF-8"), "ISO-8859-1");
           }
        } catch (UnsupportedEncodingException e) {
        }
        return filename;
        
        
       
        
	}

	private String getContentType(String filename) {
		return this.contentType += "; charset=" + this.encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

}
