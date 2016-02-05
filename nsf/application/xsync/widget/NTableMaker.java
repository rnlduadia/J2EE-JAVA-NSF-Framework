package nsf.application.xsync.widget;

import java.util.Set;

import nsf.support.collection.NMultiData;
import nsf.application.xsync.NAbstractSync;

/**
 * <pre>
 * aJax로 테이믈을 삽입할때 리용한다.
 * </pre>
 * 
 * @since 2007/1/5
 * @version NSF 1.0
 * @author 김철림 Nova China
 */

public class NTableMaker extends NAbstractSync{


    private NMultiData mdata = null;

    
    public NTableMaker(String tableId, NMultiData data){
        super("table");
        setAttribute("id",tableId);
        this.mdata = data;
    }
    
    public String getInnerTag() {

            Set s = mdata.keySet();
            String[] keys = (String [])s.toArray(new String[s.size()]);
            StringBuffer sb=new StringBuffer();
            
            if(mdata.keySize()==0)
            {
                sb.append("<tr>");  
                for(int i=0;i<s.size();i++)
                {
                    sb.append("<td>");
                    sb.append("</td>");
                }  
                sb.append("</tr>");
            }
            else
            {
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
            }

            return sb.toString();
    }
}

