package nsf.application.page.dao;

import nsf.core.exception.NException;
import nsf.support.collection.NDataProtocol;

/**
 * <pre>
 *  MSSQL DATABASE에서 다량의 자료를 페지로 구성할 경우 최적화된 기능을 제공하는 PageDao이다.      
 * </pre>
 * @since 2007/1/5
 * @version NSF 1.0
 * @author 김철남 Nova China
 */
public final class NMSSQLPageDao extends NAbstractPageDao {

	/**
     * NMSSQLPageDao 생성자.
     * @param dataSourceInfo
     * @param rowSize
     * @param pageSize
     * @param pageData
     * @throws NException
	 */
	public NMSSQLPageDao(String dataSourceInfo, int rowSize, int pageSize, NDataProtocol pageData) throws NException {
		super(dataSourceInfo, rowSize, pageSize);
		pageStatement = new NMSSQLPageStatement(rowSize, pageSize, pageData);
	}

}

