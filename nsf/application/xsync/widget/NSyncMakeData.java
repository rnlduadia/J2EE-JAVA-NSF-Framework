package nsf.application.xsync.widget;

import java.util.Set;

import nsf.support.collection.NMultiData;
import nsf.application.xsync.NAbstractSync;

/**
 * <pre>
 * aJax로 Tree구조를 삽입할때 리용한다.
 * </pre>
 * 
 * @since 2008/4/2
 * @version NSF 1.0
 * @author 리충일 Nova China
 */

public class NSyncMakeData extends NAbstractSync{


    private NMultiData mdata = null;

    
    public NSyncMakeData(String treedivId, NMultiData data,String cmdname){
        super(cmdname);
        setAttribute("id",treedivId);
        this.mdata = data;
    }
    public NSyncMakeData(String treedivId, NMultiData data,String cmdname,String row){
        super(cmdname);
        setAttribute("id",treedivId);
        setAttribute("name",row);
        this.mdata = data;
    }
    public String getInnerTag() {

            Set s = mdata.keySet();
            String[] keys = (String [])s.toArray(new String[s.size()]);
            StringBuffer sb=new StringBuffer();
            
            for(int j=0;j<mdata.keySize();j++)
                {
                        sb.append("<tr>");  
                        for(int i=0;i<s.size();i++)
                        {
                            sb.append("<td>");
                            sb.append( getCdata(mdata.getString(keys[i],j)) );
                            sb.append("</td>");
                        }  
                        sb.append("</tr>"); 
                }
           

            return sb.toString();
    }
}

