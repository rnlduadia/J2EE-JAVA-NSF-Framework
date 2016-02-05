package nsf.application.page;

/**
 * <pre>
 *  페지의 Navigation 기능을 제공하는 PageNavigation 함수를 담고 있다.   
 * </pre>
 * @since 2007/1/5
 * @version NSF 1.0
 * @author 김철남 Nova China
 */
public interface NPageNavigationIF {
	/**
	 * 페지 상에 현재 Page 위치값을 리턴한다
	 * @return int 현재 보여지는 페이지의 Page 위치
	 */
	int getCurrentPage();

	/**
	 * 페지 상에 현재 Page 의 row 값을 리턴한다
	 * @return int 현재 보여지는 페이지의 row 위치
	 */
	int getCurrentRow();
	
	/**
	 * 검색된 총페지 수를 리턴한다
	 * @return int 검색된 총 페지 수
	 */
	int getPages();
	/**
	 * 검색된 총 Row 수를 리턴한다.
	 * @return int 검색된 총 Row 수
	 */
	int getRows();
	/**
	 * 페지 상에 사용하는 인덱스를 표시하는 함수이다.
	 * @return String 화면에 인덱스를 구성하는 문자렬
	 */
	String showIndex();
	/**
	 * 페지 상에 이전 인덱스 이동을 표시하는 함수이다.
	 * @return String 화면에 이전 인덱스로 이동을 표기하는 문자렬
	 */
	String showMoveBeforeIndex();
	/**
	 * 페지 상에 이전 페지 이동을 표시하는 함수이다.
	 * @return String 화면에 이전 페지로 이동을 표기하는 문자렬
	 */
	String showMoveBeforePage();
	/**
	 * 페지 상에 마지막 페지 이동을 표시하는 함수이다.
	 * @return String 화면에 마지막 페지로 이동을 표기하는 문자렬
	 */
	String showMoveEndPage();
	/**
	 * 페지 상에 첫 페지 이동을 표시하는 함수이다.
	 * @return String 화면에 첫 페지로 이동을 표기하는 문자렬
	 */
	String showMoveFirstPage();
	/**
	 * 페지 상에 다음 인덱스 이동을 표시하는 함수이다.
	 * @return String 화면에 다음 인덱스로 이동을 표기하는 문자렬
	 */
	String showMoveNextIndex();
	/**
	 * 페지 상에 다음 페지 이동을 표시하는 함수이다.
	 * @return String 화면에 다음 페지로 이동을 표기하는 문자렬
	 */
	String showMoveNextPage();
	/**
	 * 페지 상에 Select Box 형식으로 표기된 인덱스를 표시하는 함수이다.
	 * @return String 화면에  Select Box 형식으로 표기된 인덱스를 표기하는 문자렬
	 */
	String showSelectIndex();
	/**
	 * 페지 상에 정렬 기능을 제공하는 필드를 구성할때 사용하는 함수이다.
	 * @return String 정렬 링크가 걸려있는 문자렬
	 * @param title String 화면상에 표시될 필드 문자렬
	 * @param nsfOrderBy String 정렬을 원하는 필드
	 */
	String showSortField(String title, String nsfOrderByColumn);
	
	/**
	 * 페지 작업상 필요한 JavaScript 를 표시하는 함수이다.
	 * @return String JavaScript 함수 문자렬.
	 */	
	String showJavaScript() ;

	/**
	 * 페지 작업상 필요한 HiddenParameter 를 표시하는 함수이다.
	 * @return String HiddenParameter element 문자렬.
	 */	
	String showHiddenParam() ;
	/**
     *페지 작업상 페지당행수를 설정하는 마당을 표시하는 함수이다. 
	 */
    String showRowSize();
}

