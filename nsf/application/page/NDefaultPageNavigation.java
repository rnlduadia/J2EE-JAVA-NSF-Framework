package nsf.application.page;

import java.lang.reflect.Constructor;

import nsf.core.exception.NException;
import nsf.support.collection.NData;
import nsf.support.collection.NDataProtocol;
import nsf.support.collection.NMultiData;


/**
 * <pre>
 *  페지의 Rendering 기능을 담당하는 Class.
 *  페지의 결과 값을 화면에 정형화된 양식으로 출력한다.  
 * </pre>
 * @since 2007/1/5
 * @version NSF 1.0
 * @author 김철남 Nova China
 */

public class NDefaultPageNavigation {
	/**
	 * <pre>
	 *  데이터베이스로부터 얻어온 결과값, NPageDao의 execute()메소드의 결과값이 설정된다.
	 * </pre>
	 */
	protected Object resultMultiData;
	
	/**
	 * <pre>
	 *  데이터베이스로부터 얻어온 결과값, NPageDao의 execute()메소드의 결과값을 반환한다. 
	 * </pre>
	 * @return 결과 Object 
	 */	
	public Object getResultMultiData() {
		return this.resultMultiData ; 
	}

	/**
	 * <pre>
	 *   HttpRequest 로 부터 설정된 NMultiData 
	 * </pre>
	 */
	protected NMultiData pageMultiData;

	/**
	 * <pre>
	 *  HttpRequest 로 부터 설정된 NMultiData 값을 반환한다. 
	 * </pre>
	 * @return 페이지 구성정보 NMultiData 
	 */	
	public NMultiData getPageMultiData() {
		return this.pageMultiData ; 
	}
	
	
	/**
	 * <pre>
	 * 사용하게 될 NPageNavigation
	 * </pre>
	 */
	protected NPageNavigationIF pageNavigation;

	/**
	 * <pre>
	 *  NPageNavigation 을 반환한다. 
	 * </pre>
	 * @return 페이지 구성정보 NMultiData 
	 */	
	public NPageNavigationIF getPageNavigation() {
		return this.pageNavigation ; 
	}
		
	
	/**
	 * <pre>
	 *   NData 형의 자료를 NMultiData 로 변환한다. 
	 * </pre>
	 * 
	 * @param pageData
	 * @return
	 */
	private static NMultiData convertToNMultiData(NData pageData) {
		NMultiData pageMultiData = new NMultiData("PAGE_MULTI_DATA");
		pageMultiData.addNData(pageData);
		return pageMultiData;
	}

	/**	  
	 * <pre>
	 *  numberOfPagesOfIndex 값이 nsf.xml 에 설정되어있는지 여부를 판단한다.
	 * </pre> 
	 * @param numberOfPagesOfIndex
	 * @return numberOfPagesOfIndex 값이 설정되어 있으면 true , 설정 되어있지 않으면 false  
	 */
	private boolean isPageIndexType(int numberOfPagesOfIndex) {
		if (numberOfPagesOfIndex < 1)
			return false;
		else
			return true;
	}
    
	/**
	 * 
	 * <pre>
	 *  PageNavigationObject 를 nsf.xml 에서 읽어와 실시간에 생성하여 반환한다. 
	 * </pre>
	 * 
	 * @param pageMultiData
	 * @param pageSpec
	 * @return
	 * @throws NException
	 */
	private NAbstractPageNavigation getPageNavigationObject(NMultiData pageMultiData) throws NException {
        
		NAbstractPageNavigation navigationObject = null;
		String navigationClassName = "nsf.application.page.NPageNavigation";

		try {

			Class[] paramClassType = new Class[] { NMultiData.class };
			//Class navigationClass = Class.forName(navigationClassName);
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			Class navigationClass = classLoader.loadClass(navigationClassName);
			
			Constructor navigationClassConstructor =
				navigationClass.getConstructor(paramClassType);
			Object[] paramObjectVal = new Object[] { pageMultiData };
			navigationObject =(NAbstractPageNavigation) navigationClassConstructor.newInstance(paramObjectVal);

		} catch (Exception e) {
			throw new NException(
				"NSF_PAG_005",
				"Instanciation Failed with Fully-Qualified class name["
					+ navigationClassName
					+ "]",
				e);

		}
		return navigationObject;
	}
	/**
	 * <pre>
	 *   NDefaultPageNavigation Default 생성자
	 * </pre> 
	 *	
	 */
	public NDefaultPageNavigation() {		 
	}
	
	/**
	 * <pre>
	 *   NDefaultPageNavigation 생성자
	 * </pre> 
	 *	 
	 * @param pageData HttpRequest 로 부터 설정된 NMultiData , 페이지 구성을 위한 기본적인 정보.
	 * @param resultMultiData NMultiData 데이터베이스로부터 얻어온 결과값.
	 * @throws NException
	 */
	public NDefaultPageNavigation(NDataProtocol pageData, NMultiData resultMultiData) throws NException {

		NMultiData pageMultiData = null;

		if (pageData instanceof NData)
			pageMultiData = convertToNMultiData((NData) pageData);
		else
			pageMultiData = (NMultiData) pageData;

		NData pageIndexDataFromServer =
			resultMultiData.getNData(NPageConstants.PAGE_INDEX, 0);
		int targetRow =
			pageIndexDataFromServer.getInt(NPageConstants.TARGET_ROW);
		int rows = pageIndexDataFromServer.getInt(NPageConstants.ROWS);
		
		int numberOfRowsOfPage = 0 ;
		String NUMBER_OF_ROWS_OF_PAGE_STR = pageIndexDataFromServer.getString(NPageConstants.NUMBER_OF_ROWS_OF_PAGE);
		if ( NUMBER_OF_ROWS_OF_PAGE_STR == null || NUMBER_OF_ROWS_OF_PAGE_STR.equals("") ) { 
//            numberOfRowsOfPage = NPageConstants.getNumberOfRowsOfPage(pageSpec);
		} else {
		    numberOfRowsOfPage = pageIndexDataFromServer.getInt( NPageConstants.NUMBER_OF_ROWS_OF_PAGE );
		} 
	
		int numberOfPagesOfIndex = 0 ;
		String NUMBER_OF_PAGES_OF_INDEX_STR = pageIndexDataFromServer.getString(NPageConstants.NUMBER_OF_PAGES_OF_INDEX);
		if ( NUMBER_OF_PAGES_OF_INDEX_STR == null || NUMBER_OF_PAGES_OF_INDEX_STR.equals("") ) { 
//            numberOfPagesOfIndex = NPageConstants.getNumberOfPagesOfIndex(pageSpec);
		} else {
			numberOfPagesOfIndex = pageIndexDataFromServer.getInt( NPageConstants.NUMBER_OF_PAGES_OF_INDEX );
		} 
		
		NAbstractPageNavigation navigationObject = getPageNavigationObject(pageMultiData);

		pageMultiData.addInt(NPageConstants.ROWS, rows);
		pageMultiData.addInt(NPageConstants.TARGET_ROW, targetRow);
		
		if (isPageIndexType(numberOfPagesOfIndex))
			navigationObject.setConfig(
				targetRow,
				rows,
				numberOfRowsOfPage,
				numberOfPagesOfIndex);
		else
			navigationObject.setConfig(targetRow, rows, numberOfRowsOfPage);

		this.pageMultiData = pageMultiData;
		this.pageNavigation = (NPageNavigationIF) navigationObject;
		this.resultMultiData =
			resultMultiData.get(NPageConstants.PAGE_RESULT, 0);

	}

	/**
	 * 현재 보여지는 페지를 리턴한다.
	 * @return int 현재 보여지는 페지 위치.
	 */
	public final int getCurrentPage() {
		return pageNavigation.getCurrentPage();
	}

	/**
	 * 현재 보여지는 페지의 첫번째 row 를 리턴한다.
	 * @return int 현재 보여지는 페지의 첫번째 row.
	 */
	public final int getCurrentRow() {
		return pageNavigation.getCurrentRow();
	}

	/**
	 * 검색된 총페지수를 리턴한다.
	 * @return int 검색된 총 페이지 수.
	 */

	public final int getPages() {
		return pageNavigation.getPages();
	}
	/**
	 * 검색된 총 Row 수를 리턴한다.
	 * @return int 검색된 총 Row 수.
	 */

	final public int getRows() {
		return pageNavigation.getRows();
	}
	/**
	 * 페지 상에 사용하는 인덱스를 표시하는 함수이다.
	 * @return String 화면에 인덱스를 구성하는 문자렬.
	 */

	public final String showIndex() {

		return pageNavigation.showIndex();
	}
	/**
	 * 페지 상에 이전 인덱스 이동을 표시하는 함수이다.
	 * @return String 화면에 이전 인덱스로 이동을 표기하는 문자렬.
	 */

	public final String showMoveBeforeIndex() {
		return pageNavigation.showMoveBeforeIndex();
	}
	/**
	 * 페지상에 이전 페지 이동을 표시하는 함수이다.
	 * @return String 화면에 이전 페지로 이동을 표기하는 문자렬.
	 */

	public final String showMoveBeforePage() {
		return pageNavigation.showMoveBeforePage();
	}
	/**
	 * 페지상에 마지막 페지 이동을 표시하는 함수이다.
	 * @return String 화면에 마지막 페지로 이동을 표기하는 문자렬.
	 */

	public final String showMoveEndPage() {
		return pageNavigation.showMoveEndPage();
	}
	/**
	 * 페지상에 첫 페지 이동을 표시하는 함수이다.
	 * @return String 화면에 첫 페지로 이동을 표기하는 문자렬.
	 */

	public final String showMoveFirstPage() {
		return pageNavigation.showMoveFirstPage();
	}
	/**
	 * 페지상에 다음 인덱스 이동을 표시하는 함수이다.
	 * @return String 화면에 다음 인덱스로 이동을 표기하는 문자렬.
	 */
	public final String showMoveNextIndex() {
		return pageNavigation.showMoveNextIndex();
	}
	/**
	 * 페지상에 다음 페지 이동을 표시하는 함수이다.
	 * @return String 화면에 다음 페지로 이동을 표기하는 문자렬.
	 */
	public final String showMoveNextPage() {
		return pageNavigation.showMoveNextPage();
	}
	/**
	 * 페지상에 Select Box 형식으로 표기된 인덱스를 표시하는 함수이다.
	 * @return String 화면에  Select Box 형식으로 표기된 인덱스를 표기하는 문자렬.
	 */
	public final String showSelectIndex() {
		return pageNavigation.showSelectIndex();
	}

	/**
	 * 페지상에 정렬 기능을 제공하는 필드를 구성할때 사용하는 함수이다.<BR>
	 * @return String 정렬 link가 걸려있는 문자렬.
	 * @param title String 화면상에 표시될 필드 문자렬.
	 * @param nsfOrderBy String 정렬을 원하는 데이터 베이스 컬럼명.
	 */
	public String showSortField(String title, String nsfOrderBy) {
		return pageNavigation.showSortField(title, nsfOrderBy);
	}
	/**
	 * 페지 작업상 필요한 JavaScript 를 표시하는 함수이다.
	 * @return String JavaScript 함수 문자렬.
	 */
	public String showJavaScript() {
		return pageNavigation.showJavaScript();
	}

	/**
	 * 페지 작업상 필요한 HiddenParameter 를 표시하는 함수이다.
	 * @return String HiddenParameter element 문자렬.
	 */
	public String showHiddenParam() {
		return pageNavigation.showHiddenParam();
	}
   /**
    * 페지 작업상 페지당행수를 설정하는 마당을 표시하는 함수이다. 
    */
    public String showRowSize(){
        return pageNavigation.showRowSize();
    }
}

