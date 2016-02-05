package nsf.support.tools.file;
/**
 * @(#) NFileUtil.java
 * 
 */
import java.io.File;
import java.text.DecimalFormat;
import java.util.regex.Pattern;

/**
 * @(#) NFileUtil.java
 * <pre>
 * File을 Handling할 때 필요한 Utility Function을 제공한다.
 *
 * 이 Class는 JDK V1.4이상에서 동작한다.
 * </pre>
 * 
 * @since 2007년 1월 5일
 * @version NSF 1.0
 * 
 * @author 강영진, Nova China 
 */
public final class NFileUtil {

	/**
	 * Don't let anyone instantiate this class
	 */
	private NFileUtil() {
	}

	/**
	 * DOS File System의 seperator(\)를 Java Style (/)로 변경하기 위해 사용되는 정규식 Pattern
	 */
	public static final Pattern dosSeperator = Pattern.compile("\\\\");

	/**
	 * 파일 System의 Full Path에서 마지막이 /로 끝나는지를 검사하기 위해 사용되는 정규식 Pattern
	 */
	public static final Pattern lastSeperator = Pattern.compile("/$");

	/**
	* 주어진 파일의 fullpath중 path부분을 제외한 filname part만 분리하여 return한다.<BR>
	* (new File(fullpath)).getName()과 동일하나 File 객체를 사용하지 않고 문자열 Pattern만으로 분석한다.<BR>
	* 만약 fullpath에 / 혹은 \가 존재하지 않는 경우라면 "./" 을 return할 것이다.
	* 
	* @param fullpath Path와 filename으로 이루어진 파일의 fullpath
	* 
	* @return fullpath중 filename part
	*/
	public static String getFileNameChop(String fullpath) {
		if (null == fullpath)
			return null;
		fullpath = dosSeperator.matcher(fullpath).replaceAll("/");
		int pos = fullpath.lastIndexOf("/");
		if (pos > -1)
			return fullpath.substring(pos + 1);
		return fullpath;
	}

	/**
	* 주어진 파일의 fullpath중 filname part를 제외한 path 부분만 분리하여 return한다.<BR>
	* (new File(fullpath)).getParent()와 동일하나 File 객체를 사용하지 않고 문자열 Pattern만으로 분석한다.<BR> 
	* 만약 fullpath에 / 혹은 \가 존재하지 않는 경우라면 "./" 을 return할 것이다.<BR>
	* 만약 \이 fullpath에 존재한다면 모두 /로 변경할 것이다. 
	* 
	* @param fullpath Path와 filename으로 이루어진 파일의 fullpath
	* 
	* @return fullpath중 path
	*/
	public static String getFilePathChop(String fullpath) {
		if (null == fullpath)
			return null;
		fullpath = dosSeperator.matcher(fullpath).replaceAll("/");
		int pos = fullpath.lastIndexOf("/");
		if (pos > -1)
			return fullpath.substring(0, pos + 1);
		else
			return "./";
	}

	/**
	* 주어진 파일의 fullpath의 맨 마지막에 /가 붙어 있는지를 검사하고 없는경우 /를 붙여서 return한다.<BR>
	* 만약 \이 fullpath에 존재한다면 모두 /로 변경될 것이다.
	*  
	* @param fullpath Path와 filename으로 이루어진 파일의 fullpath
	* 
	* @return fullpath의 맨 마지막에 /가 붙어 있는 fullpath 
	*/
	public static String getCompleteLeadingSeperator(String fullpath) {
		if (null == fullpath)
			return null;
		fullpath = dosSeperator.matcher(fullpath).replaceAll("/");
		if (!fullpath.endsWith(File.separator))
			fullpath += "/";
		return fullpath;
	}

	/**
	* 주어진 파일의 fullpath의 맨 마지막에 /가 붙어 있는지를 검사하고 있는 경우 맨 마지막의 /를 제거하여 return한다.<BR>
	* 만약 \이 fullpath에 존재한다면 모두 /로 변경될 것이다.
	*  
	* @param fullpath Path와 filename으로 이루어진 파일의 fullpath
	* 
	* @return fullpath의 맨 마지막에 /가 제거된 fullpath 
	*/
	public static String getRemoveLeadingSeperator(String fileName) {
		if (null == fileName)
			return null;
		fileName = dosSeperator.matcher(fileName).replaceAll("/");
		fileName = lastSeperator.matcher(fileName).replaceAll("");
		return fileName;
	}

	/**
	* 주어진 size를 크기에 따라 Kb, Mb 형태의 읽기 좋은 String으로 변경하여 return한다.<BR>
	* 변경되는 형태는 아래와 같을 것이다. <BR>
	*  0 ~ 1024 : #,###.00 byte  <BR>
	*  1024 ~ 1048576 : #,###.00 Kb  <BR>
	*  1048576 ~ 1073741824 : #,###.00 Mb  <BR>
	* 
	* @param size 변환하기를 원하는 size
	* 
	* @return size 값이 Kb, Mb로 변경된 문자  
	*/
	public static String getFilesizeString(long size) {
		String tail = "";
		if (1024 > size) {
			tail = "byte";
		} else if (1048576 > size) {
			size = size / 1024;
			tail = "Kb";
		} else if (1073741824 > size) {
			size = size / 1024;
			tail = "Mb";
		}
		return new DecimalFormat("#,###.00").format(size) + tail;
	}

}

