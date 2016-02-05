package nsf.foundation.front.channel.parameter;

/**
 * @(#) NUrlParameter.java
 */

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

/**
 * <pre>
 * 이 클라스는 URL을 처리하기 위해 사용되는 클라스이다.<br>
 * </pre>
 * 
 * @since 2007년 1월 5일
 * @version NSF 1.0
 * 
 * @author 최정철, Nova China<br>
 */

public class NUrlParameter {
    private Map map = new HashMap();
    private String _encoding;
    private String _anchor;
    
    /**
     * NUrlParameter 기정 구축자
     */
    public NUrlParameter() {
        super();
    }
    
    /**
     * 주어진 코드로서 Encoding을 설정한다.
     * @param encode
     */
    public void setEncoding(String encode) {
        _encoding = encode;
    }
    
    /**
     * Encoding 값을 얻는다
     * @return
     */
    public String getEncoding(){
        return _encoding;
    }
    
    /**
     * URL을 Encoding처리 한다.
     * @return
     * @throws UnsupportedEncodingException
     */
    public String toURLParameterString() throws UnsupportedEncodingException{
        if (map.isEmpty()) return "";
	    Iterator i =  map.entrySet().iterator();
	    StringBuffer sb = new StringBuffer();

	    while (i.hasNext()) {
	        Map.Entry entry =  (Map.Entry)i.next();
	        sb.append(encode(entry.getKey())).append("=").append(encode(entry.getValue()));
	        if (i.hasNext()) sb.append("&");
	    }
	    
	    if (_anchor !=null && _anchor.length() > 0) sb.append("#").append(_anchor);
        return sb.toString();
    }
    
    /**
     * 오브젝트에 대해서 Encoding 처리를 진행한다.
     * @param o
     * @return
     * @throws UnsupportedEncodingException
     */
    protected String encode(Object o) throws UnsupportedEncodingException{
        return  (_encoding==null) ? (String)o : URLEncoder.encode((String)o, _encoding);
    }
    
    /**
     * 특정 key를 가지고 그것이 map에 들어 있는지를 검사한다.
     * @param key
     * @return
     */
    public boolean containsKey(String key){
        return map.containsKey(key);
    }
    
    /**
     * 특정 value를 가지고 그것이 map에 들어있는지를 검사한다.
     * @param value
     * @return
     */
    public boolean containsValue(String value){
        return map.containsValue(value);
    }
    
    public Set entrySet() {
        return map.entrySet();
    }
    
    /**
     * 특정 key에 해당한 파라메터 값을 얻는다.
     * @param key
     * @return
     */
    public String getParameter(String key) {
        return (String) map.get(key);
    }
    
    /**
     * map이 비지 않았는가를 검사한다.
     * @return
     */
    public boolean isEmpty() {
        return map.isEmpty();
    }
    
    public Set keySet() {
        return map.keySet();
    }
    
    /**
     * map에 값을 넣는다.
     * @param key
     * @param value
     */
    public void setParameter(String key, String value) {
        map.put(key, value);
    }
    
    /**
     * ahchor에 값을 설정한다.
     * @param value
     */
    public void setAnchor(String value) {
        _anchor = value;
        //map.put(key, value);
    }
    
    /**
     * 입구로 map형태의 자료를 받아 설정한다.
     * @param t
     */
    public void putAll(Map t) {
        map.putAll(t);
    }
    
    /**
     * map에 들어있는 자료의 개수를 얻는다.
     */
    public int size() {
        return map.size();
    }
    
    /**
     * map을 초기화 한다.
     * @param key
     * @return
     */
    public Object remove(String key){
        return map.remove(key);
    }
    
    /**
     * map에 들어 있는 값들을 되돌린다.
     * @return
     */
    public Collection values(){
        return map.values();
    }
    
    /**
     * request에 객체를 설정한다.
     * @param req
     */
    public void set(HttpServletRequest req){
        req.setAttribute("NSF_URLParameter", this);
    }    
}

