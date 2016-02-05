package nsf.support.collection;

/**
 * @(#) NCollectionUtility.java
 * 
 */

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import nsf.application.fileupload.NFormInfo;
import nsf.application.fileupload.NMultipartRequest;


/**
 * <pre>
 * 이 Class는 Servlet을 support하기 위한 class이다.<br>
 * HttpServletRequest로 받은 request의 name과 value를 <br>
 * HashMap에 NData Object로 넣어서 사용한다.<br>
 * 사용시 String으로 되여있는 값들을 Conversion해서 사용하는것이<br>
 * 아니고 간단하게 getInt()식으로 가져올수 있게 한다.<br>
 * 모두 static method로 구성되여 있다.<br>
 * </pre>
 * 
 * @since 2007년 1월 5일
 * @version NSF 1.0
 * 
 * @author 강영진, Nova China 
 */

 

public class NCollectionUtility {

	/**
	 * You can't call the constructor.
	 */
	
	private NCollectionUtility() {}
   

	/** 
	 * original String 값이 null 또는 "" 일 경우 defaultStr를 return한다.
	 * @param originalStr String Original String
	 * @param defaultStr String Original String에서 만일 null이 있을 경우 replace될 String
	 * @return String
	 */
    public static String NVL(String originalStr, String defaultStr) {
    	if( originalStr == null || originalStr.length() < 1 )
    		return defaultStr;
		return originalStr;
	}		

	/**
	 * Parameter로 HttpServletRequest를 받아 FORM INPUT Data를 parsing하여 
	 * NData객체에 담아 return한다.
     * getAttributeNames() 사용한다.
	 *
	 * @param req javax.servlet.http.HttpServletRequest
	 * @return NData attribute data
	 */
    public static NData getAttributeBox(HttpServletRequest req)  {
        NData data = new NData("REQUEST_DATA");
        Enumeration e = req.getAttributeNames();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            data.put(key, req.getAttribute(key));
        }
        return data;
    }

	/**
	 * Parameter로 NMultipartRequest를 받아 FORM INPUT Data를 parsing하여 
	 * NData객체에 담아 return한다.
	 * NMultipartRequest개체의 getParameters() 사용한다.
	 * NData의 특성상 같은 이름의 form data가 존재하는 경우 최초의 form data만이 access 가능하다. 
	 * @param mReq nsf.application.fileupload.NMultipartRequest
	 * @return NData Input data
	 */
	public static NData getData(NMultipartRequest mReq)  {
		NData data = new NData("REQUEST_DATA");
		List formData = mReq.getParameters();
		Iterator iter = formData.iterator();
		while (iter.hasNext()) {
			NFormInfo item = (NFormInfo) iter.next();
			String key = item.getFieldName();
			if (!data.containsKey(key)) data.put( key, item.getFieldValue() ); 
		}
		return data;
	}
	
	
	/**
	 * Parameter로 HttpServletRequest를 받아 FORM INPUT Data를 parsing하여 
	 * NData객체에 담아 return한다.
     * getParameterValues() 사용한다.
	 *
	 * @param req javax.servlet.http.HttpServletRequest
	 * @return NData Input data
	 */
	public static NData getData(HttpServletRequest req)  {
		NData data = new NData("REQUEST_DATA");

       	Enumeration e = req.getParameterNames();			
       	
       	while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			data.put( key, req.getParameter(key) );
		}
		
		return data;
	}
	
    /**
	 * Parameter로 HttpServletRequest를 받아 cookie를 가져와서
	 * NData객체에 담아 return한다.
     * getCookies() 사용한다.
	 *
	 * @param req javax.servlet.http.HttpServletRequest
	 * @return NData cookies
	 */
	public static NData getDataFromCookie(HttpServletRequest req)  {
		NData cookieData = new NData("COOKIE_DATA");
		Cookie[] cookies = req.getCookies();
		if (cookies == null) {
			return cookieData;
		}

		for(int i = 0 ;  i < cookies.length ; i++) {
			String key = cookies[i].getName();
			String value = cookies[i].getValue();
			if (value == null) {
				value = "";
			}
			String cookiesValue =  value;
			cookieData.put( key, cookiesValue );
		}
		return cookieData;
	}


	/**
	 * Parameter로 NMultipartRequest를 받아 FORM INPUT Data를 parsing하여 
	 * NData객체에 담아 return한다.
	 * NMultipartRequest개체의 getParameters() 사용한다.
	 * NMultiData의 특성상 같은 이름의 form data가 존재하는 경우에도 모든 form data에 access 가능하다. 
	 * @param mReq  NMultipartRequest
	 * @return formdata가 해석된 NData
	 */
	public static NMultiData getMultiData(NMultipartRequest mReq)  {
		
		NMultiData multiData = new NMultiData("REQUEST_DATA");
		List formData = mReq.getParameters();
		Iterator iter = formData.iterator();
		while (iter.hasNext()) {
			NFormInfo item = (NFormInfo) iter.next();
			String key = item.getFieldName();
			if (!multiData.containsKey(key)) {
				ArrayList list = new ArrayList();
				list.add(item.getFieldValue());
				multiData.put( key, list);
			} else {
				ArrayList list = (ArrayList) multiData.get(key);
				list.add(item.getFieldValue());
			}
		}
		return multiData;
	}
	
	/**
	 * Parameter로 HttpServletRequest를 받아 FORM INPUT Data를 parsing하여 
	 * NVectorBox객체에 담아 return한다.
     * getParameterValues() 사용한다.
	 * @param req javax.servlet.http.HttpServletRequest
	 * @return NVectorBox Input multi-data
	 */
	public static NMultiData getMultiData(HttpServletRequest req)  {
		NMultiData multiData = new NMultiData("requestbox");

        Enumeration e = req.getParameterNames();
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			String []values = req.getParameterValues(key);
			ArrayList list = new ArrayList();			
			for( int i = 0 ; i < values.length ; i++ ){
				list.add(values[i]);
			}
			multiData.put(key, list);
		}
		return multiData;
	}
	

	
	/** 
	 * NData 객체의 복사한 새로운 NData객체를 생성하여 return한다.
 	 * @param data NData 
	 * @return NData Instance  
	 */
    public static NData deepClone(NData data) {
		NData newData = new NData(data.getName());

		NData src = data;
		NData target = newData;
		
		Set set = src.keySet();
		Iterator e = set.iterator();
		
		while( e.hasNext() ) {
			String key = (String) e.next();
			Object value =  src.get(key);
			target.put(key,value);
		}		
		return newData;
	}	
}

