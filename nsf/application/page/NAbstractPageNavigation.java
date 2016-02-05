package nsf.application.page;

/**
 * <pre>
 *  페지의 Navigation 기능을 담당하는 최상위 Abstract Class.
 *  매개의 페지를 조회할 수 있는 기능과 페지에 대한 기본적인 정보를 제공한다.
 *  페지에 대한 정보는 내부적으로 NPageNavigationUtility를 통해 계산한다.
 *  자신이 원하는 형태의 NPageNavigation을 작성하기를 원한다면 NAbstractPageNavigaion를 
 *  상속받아 구현하도록 한다. 
 * </pre>
  * @since 2007/1/5
 * @version NSF 1.0
 * @author 김철남 Nova China
 */

public abstract class NAbstractPageNavigation implements NPageNavigationIF {

	private int currentRow;
	private int currentPage;
	private int currentIndex;

	private int numberOfRowsOfPage;
	private int numberOfPagesOfIndex;

	private int rows;
	private int pages;
	private int indexes;

	/**
	 * NAbstractPageNavigation의 기본 생성자 
	 */
	public NAbstractPageNavigation() {
		super();
	}
	/**
	 * 페지 상에서 현재 Index 위치값을 리턴한다
	 * @return int 현재 보여지는 페이지의 Index 위치
	 */
	protected final int getCurrentIndex() {
		return currentIndex;
	}
	/**
	 * 페지 상에 현재 Page 위치값을 리턴한다
	 * @return int 현재 보여지는 페지의 Page 위치
	 */
	public final int getCurrentPage() {
		return currentPage;
	}

	/**
	 * 페지 상에 현재 Page 위치값을 리턴한다
	 * @return int 현재 보여지는 페지의 Page 위치
	 */
	public final int getCurrentRow() {
		return currentRow;
	}

	/**
	 * 페지에 사용되는 Index의 총 개수를 리턴한다. 
	 * @return int Index의 총 개수
	 */
	protected final int getIndexes() {
		return indexes;
	}
	/**
	 * 하나의 인덱스를 구성하는데 필요한 Page 수를 리턴한다. 
	 * @return int 한 인덱스에 보여지는 Page수 
	 */
	protected final int getNumberOfPagesOfIndex() {
		return numberOfPagesOfIndex;
	}
	/**
	 * 하나의 페지를 구성하는데 필요한 Row의 수를 리턴한다.
	 * @return int 한 페지에 보여지는 Row수
	 */
	protected final int getNumberOfRowsOfPage() {
		return numberOfRowsOfPage;
	}
	/**
	 * 페지에 사용되는 Page 총 개수를 리턴한다.
	 * @return int Page의 총 개수
	 */
	public final int getPages() {
		return pages;
	}
	/**
	 * 페지에 사용되는 Row의 총 개수를 리턴한다.
	 * @return int Row의 총 개수
	 */
	public final int getRows() {
		return rows;
	}
	/**
	 * NPageNavigation에 필요한 값들을 설정한다.
	 * @param currentRow int 현재 보여지는 페지의 첫번째 Row 위치
	 * @param rows int 페지 상의 Row 수
	 * @param numberOfRowsOfPage int 한 페지당 보여지는 Row 수
	 */
	protected final void setConfig(int currentRow, int rows, int numberOfRowsOfPage) {

		this.currentRow = currentRow;
		this.rows = rows;
		this.numberOfRowsOfPage = numberOfRowsOfPage;

		this.currentPage =
			getPageOfRow(
				this.currentRow,
				getNumberOfRowsOfPage());
		this.pages =
			getPageOfRow(rows, getNumberOfRowsOfPage());

		this.numberOfPagesOfIndex = pages;
		this.currentIndex =
			getIndexOfPage(
				this.currentPage,
				numberOfPagesOfIndex);
		this.indexes =
			getIndexOfPage(
				this.pages,
				numberOfPagesOfIndex);
	}
	/**
	 * NPageNavigation에 필요한 값들을 설정한다.
	 * @param currentRow int 현재 보여지는 페지의 첫번째 Row 위치
	 * @param rows int 페지 상의 Row 수
	 * @param numberOfRowsOfPage int 한 페지당 보여지는 Row 수
	 * @param numberOfPagesOfIndex int 한 Index당 보여지는 Page 수
	 */
	protected final void setConfig(int currentRow, int rows, int numberOfRowsOfPage, int numberOfPagesOfIndex) {

		this.currentRow = currentRow;
		this.rows = rows;
		this.numberOfRowsOfPage = numberOfRowsOfPage;

		this.currentPage =
			getPageOfRow(
				this.currentRow,
				getNumberOfRowsOfPage());
		this.pages =
			getPageOfRow(rows, getNumberOfRowsOfPage());

		this.numberOfPagesOfIndex = numberOfPagesOfIndex;
		this.currentIndex =
			getIndexOfPage(
				this.currentPage,
				numberOfPagesOfIndex);
		this.indexes =
			getIndexOfPage(
				this.pages,
				numberOfPagesOfIndex);

	}
    
    /**
     * 인덱스 위치값을 통해 인덱스의 첫 페지 위치를 리턴하는 함수이다.
     * @return int 주어진 인덱스 위치값에 대한 첫 페지 위치값
     * @param index int 인덱스 위치값
     * @param numberOfPagesOfIndex int 한 인덱스를 구성하기 위한 페지 수 
     */
    public static int getFirstPageOfIndex(int index, int numberOfPagesOfIndex) {
        return (index - 1) * numberOfPagesOfIndex + 1;
    }
    /**
     * 페지 위치값을 통해 페지의 첫 Row 위치를 리턴하는 함수이다.
     * @return int 주어진 페지 위치값에 대한 첫 Row 위치값
     * @param page int 페지 위치값
     * @param numberOfRowsOfPage int 한 페지를 구성하기 위한 Row 수
     */
    public static int getFirstRowOfPage(int page, int numberOfRowsOfPage) {
        return (page - 1) * numberOfRowsOfPage + 1;
    }
    /**
     * 페지 위치값을 통해 페지가 속한 인덱스 위치값을 리턴하는 함수이다.
     * @return int 인덱스 위치값
     * @param pages int 페지 위치값
     * @param numberOfPagesOfIndex int 한 인덱스를 구성하기 위한 페지 수
     */
    public static int getIndexOfPage(int pages, int numberOfPagesOfIndex) {
        return (int) Math.ceil((float) pages / numberOfPagesOfIndex);
    }
    /**
     * Row 위치값을 통해 Row가 속한 페지 위치값을 리턴하는 함수이다.
     * @return int 페지 위치값
     * @param rows int Row 위치값
     * @param numberOfRowsOfPage int 한 페지를 구성하기 위한 Row 수
     */
    public static int getPageOfRow(int rows, int numberOfRowsOfPage) {
         return (int) Math.ceil((float) rows / numberOfRowsOfPage);
    }
    
}

