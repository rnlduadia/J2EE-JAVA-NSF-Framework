package nsf.application.page.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;

import nsf.application.page.NPageConstants;
import nsf.core.exception.NException;
import nsf.core.log.NLogUtils;
import nsf.support.collection.NData;
import nsf.support.collection.NMultiData;
import nsf.support.tools.converter.NDefaultNaming;

/**
 * <pre>
 *  메쎄지에 필요한 SQL 문 조작에 도움을 주는 유틸리티 클라스이다.  
 * </pre>
 * @since 2007/1/5
 * @version NSF 1.0
 * @author 김철남 Nova China
 */
final class NSqlManager {
	private String sqlStatement;
	private String stringLiteralSkppedSqlStatement;
	private Map paramNameArrayCache =
		NAccumulationHashMap.getInstance().getParamNameArrayCache();

	/**
	 * <pre> 
	 * NSqlManager 생성자
	 * </pre> 
	 * @param sql java.lang.String 파일에서 SQL문을 찾기 위한 Key값	 
	 */
	public NSqlManager(String sql) {
		this.sqlStatement = sql;
		Matcher match =
			NPageConstants.SQL_STRING_LITERAL_SKIP_PATTERN.matcher(
				this.sqlStatement);
		this.stringLiteralSkppedSqlStatement = match.replaceAll("");

	}

	/**
	 * <pre> 
	 *   NData에 저장된 파라미터 값들을 SQL 쿼리 문에 기술된 ? 문자값 순서대로 재구성 반환한다.
	 * </pre>
	 * @return NData SQL 매핑 파라미터 값
	 * @param requestData HttpRequest 로 생성된 NData 
	 * @exception NException
	 */
	public final NData getParameters(NData requestData) throws NException {
		if (paramNameArrayCache.containsKey(this.sqlStatement)) {
			String[] paramNameArrayForCache =
				(String[]) paramNameArrayCache.get(this.sqlStatement);
			return processCacheHit(requestData, paramNameArrayForCache);
		} else {
			return processCacheFail(requestData);
		}
	}

	private void putValue(
		NData valueData,
		String paramValue,
		int valueIndex) {
		valueData.setString("" + (valueIndex), paramValue);
	}

	private String getStringValIgnoreCase(
		NData pageData,
		String paramKey,
		NData paramData,
		int paramIndex)
		throws NException {
		Iterator iter = pageData.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String keyStr = (String) entry.getKey();
			if (paramKey.equalsIgnoreCase(keyStr)) {
				paramData.setString("" + paramIndex, keyStr);
				return (String) entry.getValue();
			}

		}

		throw new NException(
			"NSF_PAG_001",
			"Value for PARAM["
				+ paramKey
				+ "] Not Assigned from HTTPServletRequest.");

	}

	private Object getObjectValIgnoreCase(
		NData pageData,
		String paramKey,
		NData paramData,
		int paramIndex)
		throws NException {
		Iterator iter = pageData.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String keyStr = (String) entry.getKey();
			if (paramKey.equalsIgnoreCase(keyStr)) {
				paramData.setString("" + paramIndex, keyStr);
				return entry.getValue();
			}
		}

		throw new NException(
			"NSF_PAG_001",
			"Value for PARAM["
				+ paramKey
				+ "] Not Assigned from HTTPServletRequest.");

	}

	private void checkNull(String paramKey, Object paramValue)
		throws NException {
		if (paramValue == null)
			throw new NException(
				"NSF_PAG_001",
				"Value for PARAM["
					+ paramKey
					+ "] Not Assigned from HTTPServletRequest.");
	}

	private final NData processCacheHit(
		NData requestData,
		String[] paramNameArrayForCache)
		throws NException {
		NData valueData = new NData("VALUE_NData");

		int valueIndex = 0;

		for (int i = 0, size = paramNameArrayForCache.length; i < size; i++) {
			Object paramValueObj = requestData.get(paramNameArrayForCache[i]);
			checkNull(paramNameArrayForCache[i], paramValueObj);

			if (paramValueObj instanceof String) {
				valueIndex++;
				putValue(valueData, (String) paramValueObj, valueIndex);
			} else {
				ArrayList valList = (ArrayList) paramValueObj;
				int valListSize = valList.size();
				for (int j = 0; j < valListSize; j++) {
					valueIndex++;
					putValue(
						valueData,
						//paramNameArrayForCache[j],
						(String) valList.get(j),
						valueIndex);
				}
			}
		}

		return valueData;
	}

	/**
	 * <pre>
	 *   SQL 에 ? 가 몇개있는지 개수를 반환한다. 
	 * </pre>
	 * 
	 * @return
	 * @throws NException
	 */
	private final int questionMarkMatchCount(String questionMarkString) {
		Matcher questionMarkMatch =
			NPageConstants.QUESTION_MARK_PATTERN.matcher(questionMarkString);
		int matchCount = 0;
		while (questionMarkMatch.find())
			matchCount++;
		return matchCount;
	}

	private void checkQuestionMarkMatch(
		String questionMarkGroup)
		throws NException {
		int questionMarkCount = questionMarkMatchCount(questionMarkGroup);
		checkQuestionMarkMatch(questionMarkCount, questionMarkCount);
	}

	private void checkQuestionMarkMatch(
		int questionMarkCount,
		int fragmentCount)
		throws NException {

		if (questionMarkCount != fragmentCount) {
			throw new NException(
				"NSF_PAG_006",
				"questionMarkCount["
					+ questionMarkCount
					+ "] is not equal to assignValueCount["
					+ fragmentCount
					+ "]");
		}
	}

	/**
	 * <pre>
	 *  SQL 문과 매핑 정보가 캐싱되여있지 않은 경우 새롭게 파싱한다.  
	 *  파싱된 매핑 정보는 NAccumulationHashMap 을 통해 캐싱된다.    
	 * </pre>
	 * 
	 * @param requestData
	 * @return
	 * @throws NException
	 */
	private final NData processCacheFail(NData requestData)
		throws NException {

		boolean REQUEST_DATA_IS_SINGLE =
			requestData.getBoolean(NPageConstants.REQUEST_DATA_SINGLE_MODE);

		NData valueData = new NData("VALUE_NData");
		NData paramData = new NData("PARAM_NData");
		int questionMarkCount =
			questionMarkMatchCount(this.stringLiteralSkppedSqlStatement);
		if (questionMarkCount == 0)
			return valueData;

		Matcher paramMatch =
			NPageConstants.SQL_STMT_PATTERN.matcher(
				this.stringLiteralSkppedSqlStatement);

		String paramName = "";
		String paramValue = "";

		int valueIndex = 0;
		int paramIndex = 0;
		while (paramMatch.find()) {

			paramName = paramMatch.group(NPageConstants.SQL_COL_NAME_INDEX);

			String unaryOperatorIndicator =
				paramMatch.group(NPageConstants.SQL_UNARY_OPERATOR_INDEX);

			if (unaryOperatorIndicator != null) {
				
                paramValue = getStringValIgnoreCase(requestData, paramName, paramData, ++paramIndex);
				valueData.setString("" + ++valueIndex, paramValue);

			} else {
                
				String questionMarkGroup =
					paramMatch.group(NPageConstants.SQL_Q_MARK_GROUP_INDEX);

				if (REQUEST_DATA_IS_SINGLE) {
					paramValue =
						getStringValIgnoreCase(requestData, paramName, paramData, ++paramIndex);
					valueData.setString("" + ++valueIndex, paramValue);
				} else {
					Object valObject =
						getObjectValIgnoreCase(requestData, paramName, paramData, ++paramIndex);

					if (valObject instanceof String) {
						valueData.setString("" + ++valueIndex, (String) valObject);
					} else {

						ArrayList valList = (ArrayList) valObject;
						int entryValListSize = valList.size();

						checkQuestionMarkMatch(questionMarkGroup);
						
                        for (int j = 0; j < entryValListSize; j++) {
							valueData.setString(
								"" + ++valueIndex,
								(String) valList.get(j));
						}
					}

				}

			}

		}

		checkQuestionMarkMatch(questionMarkCount, valueIndex);

		String[] paramNameArrayForCache =
			(String[]) paramData.values().toArray(new String[paramData.size()]);

		paramNameArrayCache.put(this.sqlStatement, paramNameArrayForCache);

		return valueData;

	}
	/**
	 * 퀴리수행 결과값들의 컬럼명들을 자바 Naming Convention에 따라 변형하는 함수이다.
	 * getAttributeName 메소드의 결과를 사용한다. 
	 * @return String 변형 후 컬럼명
	 * @param columnName String 변형 전 컬럼명
	 */
	public static final String mutateColumnName(String columnName) {
		return NDefaultNaming.getAttributeName(columnName);
	}

	/**
	 * <pre>
	 *  입력된 SQL 문 에서 마지막으로 나오는 order by 절을 제거한 SQL 을 반환한다. 
	 * </pre>
	 * 
	 * @param orgSql OrderBy절을 제거할 SQL문
	 * @return OrderBy절이 제거된 SQL문
	 */
	public final static String removeOrderByClause(String orgSql) {

		Matcher sqlMatch = NPageConstants.SQL_ORDER_BY_PATTERN.matcher(orgSql);

		int lastOrderByMatchStartIndex = 0;
		while (sqlMatch.find()) {
			lastOrderByMatchStartIndex = sqlMatch.start();
		}

		if (lastOrderByMatchStartIndex == 0)
			return orgSql;

		int positionOfOrderBy = lastOrderByMatchStartIndex;
		final String candidateForOrderByClause =
			orgSql.substring(positionOfOrderBy);

		char[] candidateCharacters = candidateForOrderByClause.toCharArray();
		final int size = candidateCharacters.length;
		int balance = 0;
		for (int inx = 0; inx < size; inx++) {
			if (candidateCharacters[inx] == ')')
				balance++;
			else if (candidateCharacters[inx] == '(')
				balance--;
		}

		if (balance != 0)
			return orgSql;
		return orgSql.substring(0, positionOfOrderBy);

	}
	/**
	 * 현재 관리되고 있는 SQL문 중 OrderBy절을 다른 문자렬로 교체한 값을 반환한다.
	 * @param  OrderBy절을 변경할 SQL문
	 * @return OrderBy절이 변경된 SQL문	 
	 */
	public final String replaceOrderByClause(String orderBy) {
		if (!(orderBy == null || orderBy.trim().equals("")))
			return removeOrderByClause(this.sqlStatement) + " " + orderBy;
		else
			return this.sqlStatement;
	}

	/**
	 * <pre>
	 *  설정되는 PageData 의 원래 TYPE 이 NData 임을 표시 한다. 
	 * </pre>
	 * 
	 * @param pageMultiData Request 로 부터 추출된 NMultiData Object 
	 * @return NData의 Key 값이 소문자로 변환된 NData 
	 */
	public static NData convertNMultiDataToNData(NMultiData pageMultiData) {
		try {
			NData pageData = new NData("PAGE_DATA");
			boolean REQUEST_DATA_SINGLE_MODE_FLAG = true;
			Iterator iter = pageMultiData.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				String entryKey = (String) entry.getKey();
				ArrayList valList = (ArrayList) entry.getValue();
				if (valList.size() == 1)
					pageData.setString(entryKey, (String) valList.get(0));
				else {
					pageData.put(entryKey, valList);
					REQUEST_DATA_SINGLE_MODE_FLAG = false;
				}
			}
			pageData.setBoolean(
				NPageConstants.REQUEST_DATA_SINGLE_MODE,
				REQUEST_DATA_SINGLE_MODE_FLAG);
			return pageData;
		} catch (Exception e) {
			NLogUtils.toDefaultLogForm(
				"LSqlManager",
				"convertNMultiDataToNData",
				e.getMessage());
			return null;
		}
	}

}

