package nsf.application.fileupload;

import java.io.UnsupportedEncodingException;

import org.apache.commons.fileupload.FileItem;

/**
 * <pre>
 * FileUpload시에 HttpRequest를 통해 Upload되는 form tag의 input field information을 가지고 있는 POJO(Plain Old Java Object).
 * File 정보는 이 POJO에는 포함되지 않는다.
 * 
 * org.apache.commons.fileupload.DefaultFileItem.java의 Wrapper Class이다. 
 * Wrapper Class이기 때문에 set 계열의 method는 제공하지 않으며, 생성시 DefaultFileItem Instance를 인자로 넘겨야 한다.
 * </pre>
 * 
 * @since 2007/1/5
 * @version NSF 1.0
 * @author 김철남 Nova China
 */

public class NFormInfo {

	/**
	 * Upload된 form tag input의 name attribute에 대응된다.
	 */
	private String fieldName;

	/**
	 * Upload된 form tag input의 value attribute에 대응된다.
	 */
	private String fieldValue;
	
	/**
	 * Don't let anyone instantiate this class without parameter
	 */
	private NFormInfo() {
	}

	/**
	 * FileItem을 이용하여 NFormInfo를 생성한다.
	 * 
	 * @param item commons-fileupload에서 생성된 Uploaded-File Information 
	 * @throws UnsupportedEncodingException
	 * 
	 */
	public NFormInfo(FileItem item, String headerEncoding ) throws UnsupportedEncodingException {
		this.fieldName = item.getFieldName();
		this.fieldValue = item.getString(headerEncoding);
	}

	/**
	* Upload된 form tag input field의 name attribute를 되돌린다.
	* 
	* @return Upload된 form tag input field의 name attribute
	*/
	public String getFieldName() {
		return this.fieldName;
	}

	/**
	* Upload된 form tag input field의 value attribute를 되돌린다. <BR>
	* 
	* @return Upload된 form tag input field의 value attribute
	*/
	public String getFieldValue() {
		return this.fieldValue;
	}
}

