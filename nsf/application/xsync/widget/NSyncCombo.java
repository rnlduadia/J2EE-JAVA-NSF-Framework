package nsf.application.xsync.widget;

import nsf.support.collection.NMultiData;
import nsf.application.xsync.NAbstractSync;

/**
 * <pre>
 * aJax로 combo를 만들때 리용한다.
 * </pre>
 * 
 * @since 2007/1/5
 * @version NSF 1.0
 * @author 김철남 Nova China
 */

public class NSyncCombo extends NAbstractSync {

	private final NMultiData data;
	private String defaultCode;
	private String defaultValue;

	public NSyncCombo(String id, NMultiData data){
		this(id, data, true);
	}

	public NSyncCombo(String id, NMultiData data, boolean clear){
		super("select");
		setAttribute("id", id);
		setAttribute("clear", new Boolean(clear).toString());
		this.data = data;
	}

	public void setDefaultOption(String code, String value){
		this.defaultCode = code;
		this.defaultValue = value;
	}

	protected String getInnerTag() {
		StringBuffer sb = new StringBuffer();
		if (this.defaultCode != null && this.defaultValue !=null) {
			sb.append(getTag("option", "value", this.defaultCode,getCdata(this.defaultValue)));
		}

		int rowSize = data.keySize("code");
        //sb.append(getTag("option", "value", "" ,""));
		for( int inx = 0 ; inx < rowSize ; inx++ ){
			sb.append(getTag("option", "value", data.getString("code", inx) ,getCdata(data.getString("value", inx))));
		}
		return sb.toString();
	}

}
