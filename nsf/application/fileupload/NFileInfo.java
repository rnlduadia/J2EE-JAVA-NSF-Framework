package nsf.application.fileupload;

import java.io.File;

import nsf.support.tools.file.NFileUtil;

import org.apache.commons.fileupload.FileItem;

/**
 * <pre>
 * FileUpload시에 HttpRequest를 통해 Upload되는 File의 information을 가지고 있는 POJO(Plain Old Java Object).
 * org.apache.commons.fileupload.DefaultFileItem.java의 Wrapper Class이다. 
 * Wrapper Class이기 때문에 set 계렬의 함수는 제공하지 않으며, 생성시 DefaultFileItem Instance를 인자로 넘겨야 한다.
 * </pre>
 * 
 * @since 2007/1/5
 * @version NSF 1.0
 * @author 김철남 Nova China
 */

public class NFileInfo {

	/**
	 * Upload된 File의 form tag input의 name attribute에 대응된다.
	 */
	private String fieldName;

	/**
	 * Upload된 File의 form tag input의 value attribute에 대응된다.
	 */
	private String fieldValue;

	/**
	 * Upload된 File의 contentType 
	 */
	private String contentType;

	/**
	 * Upload된 File의 filname (Policy에 의해 변경되기 전의 client 상의 filename) 
	 */
	private String clientFileName;

	/**
	 * Upload된 File의 filname (path는 포함되지 않으며, Policy에 의해 변경된 filename) 
	 */
	private String fileName;

	/**
	 * Upload된 File의 fullpath (path + filename, Policy에 의해 변경된 filename) 
	 */
	private String path;

	/**
	 * Upload된 File의 path (filename은 포함되지 않는다.) 
	 */
	private String dir;

	/**
	 * Upload된 File의 File 객체 
	 */
	private File file;

	/**
	 * Don't let anyone instantiate this class without parameter
	 */
	private NFileInfo() {
	}

	/**
	 * FileItem을 이용하여 NFileInfo를 생성한다.
	 * 
	 * @param item commons-fileupload에서 생성된 Uploaded-File Information 
	 * 
	 */
	public NFileInfo(FileItem item, File newFileInfo ) {
		
        this.fieldName = item.getFieldName();
		this.fieldValue = item.getName();
		this.fileName = NFileUtil.getFileNameChop(newFileInfo.getName());	
		
		this.clientFileName = NFileUtil.getFileNameChop(this.fieldValue);
						
		this.dir = NFileUtil.getFilePathChop(newFileInfo.getAbsolutePath());
		this.path = this.dir + this.fileName;
		this.contentType = item.getContentType();
		
		
	}
 
		

	/**
	* Upload된 File의 form tag input field의 name attribute를 되돌린다.
	* 
	* @return Upload된 File의 form tag input field의 name attribute
	*/
	public String getFieldName() {
		return this.fieldName;
	}

	/**
	* Upload된 File의 form tag input field의 value attribute를 리턴한다. 
	* 실제로 이 값은 Upload된 File에 대한 Client의 fullpath와 일치할 것이다.
	* 
	* @return Upload된 File의 form tag input field의 value attribute
	*/
	public String getFieldValue() {
		return this.fieldValue;
	}

	/**
	* Upload된 File의 filename을 되돌린다. (path는 포함되지 않는다.)
	*  
	* @return Upload된 File의 filename
	*/
	public String getFileName() {
		return this.fileName;
	}

	/**
	* Upload된 File의 Client상의 fullPath를 되돌린다.<BR>
	* getFieldValue() 메소드와 완전히 동일하다.
	*  
	* @return Upload된 File의 client 상의 fullpath
	*/
	public String getClientPath() {
		return this.fieldValue;
	}

	/**
	* Upload된 File의 Client상의 fullPath를 되돌린다.<BR>
	* getFieldValue() 메소드와 완전히 동일하다.
	*  
	* @return Upload된 File의 client 상의 fullpath
	*/
	public String getClientFileName() {
		return clientFileName;
	}

	/**
	* Upload된 File의 Server상의 저장된 fullPath를 되돌린다.
	*  
	* @return Upload된 File의 server상의 저장된 위치(fullpath)
	*/
	public String getServerPath() {
		return path;
	}

	/**
	* Upload된 File의 content-type을 되돌린다.<BR>
	* 이 content-type은 HTTP Header상의 content type과 대응될 것이다.
	*  
	* @return Upload된 File의 content-type
	*/
	public String getContentType() {
		return this.contentType;
	}

	/**
	* Upload된 File의 크기(size)를 되돌린다.<BR>
	*  
	* @return Upload된 File의 content-type
	*/
	public long getSize() {
		return getFile().length();
	}

	/**
	* Upload된 File로부터 자바 File 객체를 구성하여 되돌린다.
	*  
	* @return Upload된 File로 부터 생성된 File 객체 
	*/
	public File getFile() {
		if (null == this.file)
			this.file = new File(path);
		return this.file;
	}

	/**
	* Upload된 File을 제거한다. <BR>
	* java.io.File.delete()에 대응된다.<BR> 
	* Deletes the file or directory denoted by this abstract pathname. <BR>
	* 
	* @return true if and only if the file is successfully deleted; false otherwise
	*/
	public boolean delete() {
		return getFile().delete();
	}

	/**
	* Upload된 File의 이름/위치를 변경한다. <BR>
	* java.io.File.renameTo(File dest)에 대응된다.<BR> 
	* Renames the file denoted by this abstract pathname.
	* @param File dest 대상 파일 
	*/
	public void renameTo(File dest) {
		getFile().renameTo(dest);
	}
}

