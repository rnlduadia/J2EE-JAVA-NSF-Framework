package nsf.application.xsync.widget;

import java.util.Set;

import nsf.support.collection.NData;
import nsf.support.collection.NMultiData;
import nsf.application.xsync.NAbstractSync;

/**
 * <pre>
 * aJax로 input를 setting할때 리용한다.
 * </pre>
 * 
 * @since 2007/1/5
 * @version NSF 1.0
 * @author 김철남 Nova China
 */

public class NSyncAtag extends NAbstractSync{
    
    private NData mdata = null;
    
    public NSyncAtag(String Aid, NData data){
        super("Atag");
        setAttribute("id",Aid);
        this.mdata = data;
    }

    protected String getInnerTag() {
        Set s = mdata.keySet();
        String[] keys = (String [])s.toArray(new String[s.size()]);
        StringBuffer sb=new StringBuffer();
                 
        for(int i=0;i<s.size();i++)
        {
            sb.append("<tr>"); 
            sb.append("<td>");
            sb.append( getCdata(mdata.getString(keys[i])) );
            sb.append("</td>");
            sb.append("</tr>");
        }  

        return sb.toString();

    }
}

