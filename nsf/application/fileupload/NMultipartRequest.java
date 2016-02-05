package nsf.application.fileupload;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import nsf.core.config.NConfiguration;
import nsf.core.exception.NException;
import nsf.support.tools.file.NFileUtil;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;

/**
 * <pre>
 * 클라이언트로부터 파일을 업로드하기위하여 클라이언트의 파일정보를 얻기위한 클라스
 * </pre>
 * 
 * @since 2007/1/5
 * @version NSF 1.0
 * @author 김철남 Nova China
 */

public class NMultipartRequest
{

    private NMultipartRequest()
    {
        targetDir = "";
        formInfos = new ArrayList();
        fileInfos = new ArrayList();
    }

    public NMultipartRequest(HttpServletRequest req)
        throws Exception
    {
        targetDir = "";
        formInfos = new ArrayList();
        fileInfos = new ArrayList();
        this.req = req;
        initRequest();
    }

    public NMultipartRequest(String targetDir, HttpServletRequest req)
        throws Exception
    {
        this.targetDir = targetDir;
        formInfos = new ArrayList();
        fileInfos = new ArrayList();
        this.req = req;
        initRequest();
    }

    /**
     * request정보로부터 파일정보를 얻어내여 림시등록부에 복사한다.
     * @throws FileUploadException
     * @throws Exception
     */
    private void initRequest()
        throws FileUploadException, Exception
    {
        DiskFileUpload fu = new DiskFileUpload();
        fu.setSizeMax(-1);
        String tempDirectory= getCompleteLeadingSeperator(NConfiguration.getNsfHome());
        fu.setRepositoryPath(tempDirectory);
        fu.setSizeThreshold(10240);
        fu.setHeaderEncoding("UTF-8");
        List fileItems = null;
        Iterator iter = null;
        FileItem item = null;
        File file = null;
        ArrayList cleanupBuffer = new ArrayList();
        try
        {
            if(targetDir.equals(""))
                targetDir = getCompleteLeadingSeperator(NConfiguration.getNsfHome()) + "/upload";
            File existTest = new File(targetDir);
            if(!existTest.exists())
                throw new NException("NSF_FUP_002 : Uploaded file directory does not exist! Check nsf.xml [" + targetDir + "]");
            File tempExistTest = new File(tempDirectory);
            if(!tempExistTest.exists())
                throw new NException("NSF_FUP_003 : Uploaded temporary file directory does not exist! Check nsf.xml [" + tempDirectory + "]");
            fileItems = fu.parseRequest(req);
            for(iter = fileItems.iterator(); iter.hasNext();)
            {
                item = (FileItem)iter.next();
                if(item.isFormField())
                    formInfos.add(new NFormInfo(item, "UTF-8"));
                else
                if(item.getName().length() != 0)
                {
                    if(item.getSize() <= 0L)
                        throw new Exception("NSF_FUP_004 : Uploaded file size is smaller than 0KB . Check the file's route. File name is [" + item.getName() + "]");
                    file = new File(getCompleteLeadingSeperator(targetDir) + getFileNameChop(item.getName()));
                    
                    file = getConflictSafeFile(file);
                    item.write(file);
                    fileInfos.add(new NFileInfo(item, file));
                    cleanupBuffer.add(file);
                    
                }
            }

        }
        catch(Exception e)
        {
            if(item != null) {
                item.delete();
            }
            
            Iterator i = cleanupBuffer.iterator();
            while( i.hasNext()) {
                file = (File)i.next();
                item.delete();
                file.delete();
            }

            throw e;
        }
    }

    /**
     * request의 파라메터들을 조회한다.
     * @param name
     * @return
     */
    public String getParameter(String name)
    {
        for(Iterator iter = formInfos.iterator(); iter.hasNext();)
        {
            NFormInfo item = (NFormInfo)iter.next();
            if(name.equals(item.getFieldName()))
                return item.getFieldValue();
        }

        return null;
    }

    /**
     * request의 파라메터들을 조회한다.
     * @param name
     * @return
     */
    public String[] getParameterValues(String name)
    {
        ArrayList al = new ArrayList();
        for(Iterator iter = formInfos.iterator(); iter.hasNext();)
        {
            NFormInfo item = (NFormInfo)iter.next();
            if(name.equals(item.getFieldName()))
                al.add(item.getFieldValue());
        }

        return (String[])al.toArray(new String[al.size()]);
    }

    /**
     * request의 파라메터들을 조회한다.
     * @return
     */
    public String[] getParameterValues()
    {
        ArrayList al = new ArrayList();
        NFormInfo item;
        for(Iterator iter = formInfos.iterator(); iter.hasNext(); al.add(item.getFieldValue()))
            item = (NFormInfo)iter.next();

        return (String[])al.toArray(new String[al.size()]);
    }

    /**
     * request의 파라메터들을 조회한다.
     * @return
     */
    public Enumeration getParameterNames()
    {
        Vector names = new Vector();
        NFormInfo item;
        for(Iterator iter = formInfos.iterator(); iter.hasNext(); names.add(item.getFieldName()))
            item = (NFormInfo)iter.next();

        return names.elements();
    }

    /**
     * request의 파라메터들을 조회한다.
     * @return
     */
    public List getParameters()
    {
        return formInfos;
    }

    /**
     * upload된 파일정보를 얻는다.
     * @param name
     * @return
     */
    public NFileInfo getFileInfo(String name)
    {
        for(Iterator iter = fileInfos.iterator(); iter.hasNext();)
        {
            NFileInfo item = (NFileInfo)iter.next();
            if(name.equals(item.getFieldName()))
                return item;
        }

        return null;
    }

    /**
     * 여러개의 파일을 upload한경우 파일정보리스트를 조회한다.
     * @param name
     * @return
     */
    public NFileInfo[] getFileInfos(String name)
    {
        ArrayList al = new ArrayList();
        for(Iterator iter = fileInfos.iterator(); iter.hasNext();)
        {
            NFileInfo item = (NFileInfo)iter.next();
            if(name.equals(item.getFieldName()))
                al.add(item);
        }

        return (NFileInfo[])al.toArray(new NFileInfo[al.size()]);
    }

    public NFileInfo[] getFileInfos()
    {
        return (NFileInfo[])fileInfos.toArray(new NFileInfo[fileInfos.size()]);
    }

    /**
     * 여러개의 파일을 upload하는경우 파일머리부정보리스트를 조회한다.
     * @return
     */
    public Enumeration getFileInfoNames()
    {
        Vector names = new Vector();
        NFileInfo item;
        for(Iterator iter = fileInfos.iterator(); iter.hasNext(); names.add(item.getFieldName()))
            item = (NFileInfo)iter.next();

        return names.elements();
    }

    public List getFileInfoList()
    {
        return fileInfos;
    }

    /**
    * 주어진 파일의 fullpath의 맨 마지막에 /가 붙어 있는지를 검사하고 없는경우 /를 붙여서 리턴한다.<BR>
    * 만약 \이 fullpath에 존재한다면 모두 /로 변경될 것이다.
    *  
    * @param fullpath Path와 filename으로 이루어진 파일의 fullpath
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
    * 주어진 파일의 fullpath중 path부분을 제외한 filname part만 분리하여 리턴한다.<BR>
    * (new File(fullpath)).getName()과 동일하나 File 객체를 사용하지 않고 문자열 패턴만으로 분석한다.<BR>
    * 만약 fullpath에 / 혹은 \가 존재하지 않는 경우라면 "./" 을 리턴할 것이다.
    * 
    * @param fullpath Path와 filename으로 이루어진 파일의 fullpath
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
    * Upload된 filename이 적합한지 검사하여 적합한 이름으로 재구성한다.<BR> 
    * abcd.txt -> abcd[0].txt -> abcd[1].txt 와 같은 형태로 filename이 변경된 file 객체를 리턴한다.
    *   
    * @param  file filename file객체
    * @return filename 
    */
    public File getConflictSafeFile(File file) {
        if (!file.exists())
            return file;
        String filename = file.getName();
        int lastDot = filename.lastIndexOf('.');
        String extension = (lastDot == -1) ? "" : filename.substring(lastDot);
        String prefix =
            (lastDot == -1) ? filename : filename.substring(0, lastDot);
        int count = 0;
        do {
            file =
                new File(
                    NFileUtil.getCompleteLeadingSeperator(file.getParent())
                        + prefix
                        + "["
                        + count
                        + "]"
                        + extension);
            count++;
        } while (file.exists());

        return file;
    }

    /**
     * dos 파일시스템의 seperator(\)를 Java Style (/)로 변경하기 위해 사용된다.
     */
    public static final Pattern dosSeperator = Pattern.compile("\\\\");

    private HttpServletRequest req;
    private String targetDir;
    private List formInfos;
    private List fileInfos;
}
