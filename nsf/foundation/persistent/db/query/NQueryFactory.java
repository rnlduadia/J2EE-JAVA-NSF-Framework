package nsf.foundation.persistent.db.query;

/**
 * @(#) NQueryFactory.java
 */

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Observable;
import java.util.Observer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import nsf.core.config.NConfiguration;
import nsf.core.exception.NException;
import nsf.support.collection.NData;

/**
 * <pre>
 * 이 클라스는 xml형태의 sql파일을 Parsing하여 조건에 맞는<br> 
 * sql을 얻어오는 클라스이다<br> 
 * </pre>
 *
 * @since 2007년 1월 5일
 * @version NSF 1.0
 *
 * @author 리향성, Nova China<br>
 */

public class NQueryFactory implements Observer{
    
    private static final NQueryFactory singleton = new NQueryFactory();
    private LinkedHashMap map = null;
    private static final String  PATH = "/configuration/nsf/sql/path";
    private static final String  SEPERATOR = "//";
    
    /**
     * NQueryFactory 기정 구축자
     */
    public NQueryFactory(){
        this.map = new java.util.LinkedHashMap();
    }
    
    /**
     * singleton객체를 얻기위한 메소드
     * @return singleton 객체
     */
    public static NQueryFactory getInstance(){
        return singleton;            
    }
    
    /**
     * 파일이름, 쿼리이름, 파라메터를 가지고 가서 해당 sql을 얻는다.
     * 
     * @param fileName
     * @param statementName
     * @param paramData
     * @return sql문을 되돌린다(파라메터 유지)
     * @throws QueryException
     */
    public String get(String fileName, String statementName, NData paramData) throws NException{

        NConfiguration conf;
        String filePath="";
        try {
            conf = NConfiguration.getInstance();
            String path = conf.getString(PATH, null);
            if (path==null) {
                throw new NException(
                "SQL파일 경로를 Conf파일에서 얻을수 없습니다. Conf파일에서 <sql/path>를 검토하시오");
            }
            filePath = path + fileName;

            if (!map.containsKey(filePath + SEPERATOR + statementName)) {       
                 setQueriesXMLParser(filePath);
            } 
            
        
        } catch (NException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return makeQuery(filePath + SEPERATOR + statementName, paramData);
    }
    
    /**
     * 주어진 파일을 parsing한다
     * 
     * @param filePath
     * @throws NException
     */
    private void setQueriesXMLParser(String filePath) throws NException{

        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(new File(filePath+".xml"));
            Element root = doc.getDocumentElement();

            NodeList children = root.getElementsByTagName("SQL");
            String queryName = null;
            String condition = null;
            String id = null;
            String value = null;
                        
            for (int i = 0; i < children.getLength(); i++) {
                Element statement = (Element) children.item(i);
                Attr attrib = statement.getAttributeNode("query");
            
                if (attrib != null)
                    queryName = attrib.getValue();

                String query = statement.getFirstChild().getNodeValue();
                map.put(filePath + SEPERATOR + queryName, query);
                
                NodeList caseChild = statement.getElementsByTagName("case");
                
                map.put(filePath + SEPERATOR + queryName + SEPERATOR 
                        + "count", caseChild.getLength()+"");
                       
                for (int inx = 0; inx < caseChild.getLength(); inx++) {
                    
                    Element statements = (Element) caseChild.item(inx);
                    value = statements.getFirstChild().getNodeValue().trim();
                    map.put(filePath + SEPERATOR + queryName + SEPERATOR + "value"+inx, value);
                    
                    Attr conditionAttrib = statements.getAttributeNode("when");
                    condition = conditionAttrib.getValue();
                    map.put(filePath + SEPERATOR + queryName + SEPERATOR + "when"+inx, condition);
                    
                    Attr idAttrib = statements.getAttributeNode("id");
                    id = idAttrib.getValue();
                    map.put(filePath + SEPERATOR + queryName + SEPERATOR + "id"+inx, id);
                }
            }
        } catch (IOException e) {
            throw new NException("Can't load a File : " + e.toString());           
        } catch (org.xml.sax.SAXException se) {
            throw new NException("Can't process xml file : " + se.toString());
        } catch (ParserConfigurationException pce) {
            throw new NException("Can't process xml file : " + pce.toString());
        }  catch (NullPointerException ne) {
            throw new NException("Check xml file! Element name doesn't match : " + ne.toString());
        }
    }
    
    /**
     * 쿼리문을 얻는다.
     * 
     * @param statement
     * @param paramData
     * @return
     * @throws NException
     */
    private String makeQuery(String statement, NData paramData) throws NException{
        try{
            String query = (String)map.get(statement);
            int count = Integer.parseInt((String)map.get(statement + SEPERATOR + "count"));
            NData queryInfo = new NData();
            queryInfo.set("query", query);
            for(int i=0 ; i<count ; i++){
                String condition = (String)map.get(statement+ SEPERATOR+"when"+i);

                queryInfo.set("when", condition);
                queryInfo.set("id", map.get(statement+ SEPERATOR+"id"+i));
                queryInfo.set("value", map.get(statement+ SEPERATOR+"value"+i));
                
                appendQuery(queryInfo, paramData);
            } 
            query = queryInfo.getString("query");
            for(int i=0;i<count;i++){
                String id="\\{"+"\\"+map.get(statement+ SEPERATOR+"id"+i)+"\\}";
                if(query.indexOf("{"+map.get(statement+ SEPERATOR+"id"+i)+"}")>-1){
                    query = query.replaceAll(id, "");
                }
            }
            queryInfo.set("query", query);
            return query;
        }catch(Exception e){
            throw new NException("Can't make a query : " + e.toString());
        }
    }
    
    /**
     * 쿼리에 특정 조건에 따라서 변경이 적용되는 경우에 추가부분을 담당한다
     *  
     * @param queryInfo
     * @param paramData
     * @throws NException
     */
    private void appendQuery(NData queryInfo, NData paramData) throws NException {
        try{
            boolean flag = false;
            String condition = queryInfo.getString("when");
            String query =  queryInfo.getString("query");
            String id = queryInfo.getString("id");
            String value = queryInfo.getString("value");
            String symbol="";
            String left,right;
            if((condition.indexOf("(")>-1) && (condition.indexOf(")")>-1)){
                left = condition.substring(0, condition.indexOf("("));
                right = condition.substring(condition.indexOf(")")+1, condition.length());
                symbol = condition.substring(condition.indexOf("(")+1,condition.indexOf(")"));
                
                if(left.indexOf("${") > -1) {
                    String key = left.substring(left.indexOf("${")+2,left.indexOf("}"));
                    left = paramData.getString(key);
                }
                if(right.indexOf("${") > -1) {
                    String key = right.substring(right.indexOf("${")+2,right.indexOf("}"));
                    right = paramData.getString(key);
                }
                
                if(symbol.equals("==")){
                    if(left.equals(right)){
                        flag=true;
                    }
                } else if(symbol.equals("!=") || symbol.equals("<>")){
                    if(!left.equals(right))
                        flag=true;
                } else if(symbol.equals("!=")){
                    if(!left.equals(right))
                        flag=true;
                } else if(symbol.equals(">")){
                    if(Integer.parseInt(left)>Integer.parseInt(right)){
                        flag=true;
                    }
                } else if(symbol.equals("<")){
                    if(Integer.parseInt(left)<Integer.parseInt(right)){
                        flag=true;
                    }
                } else if(symbol.equals(">=")||symbol.equals("=>")){
                    if(Integer.parseInt(left)>=Integer.parseInt(right)){
                        flag=true;
                    }
                } else if(symbol.equals("<=")||symbol.equals("=<")){
                    if(Integer.parseInt(left)<=Integer.parseInt(right)){
                        flag=true;
                    }
                } else if(symbol.equals("EMPTY")){
                    if(left.equals("")){
                        flag=true;
                    }
                } else if(symbol.equals("NOTEMPTY")){
                    if(!left.equals("")){
                        flag=true;
                    }
                }
                StringBuffer buf = new StringBuffer();
                char[] c = value.toCharArray();
                int len = c.length;
                for (int i = 0; i < len; i++) {
                    if (c[i] == '$') buf.append("\\$");
                    else if (c[i] == '{') buf.append("\\{");
                    else if (c[i] == '}') buf.append("\\}");
                    else buf.append(c[i]);
                }
                value=buf.toString();
                
                if(flag){
                    query = query.replaceAll("\\{"+"\\"+id+"\\}", value+ " \\{"+"\\"+id+"\\}");
                }
                queryInfo.set("query", query);
            }
        }catch(Exception e){
            throw new NException("Can't make a query in appendQuery() : " + e.toString());
        }
    }
    
    /**
     * 객체를 초기화한다.
     */
    public void reset() {
        synchronized ( this ) {
          if ( map != null ) map.clear();
        }  
    }
    
    /**
     * 변경발생시에 객체를 초기화한다.
     */
    public void update(Observable arg0, Object arg1) {
        // TODO Auto-generated method stub
        this.reset();
    }    
}
