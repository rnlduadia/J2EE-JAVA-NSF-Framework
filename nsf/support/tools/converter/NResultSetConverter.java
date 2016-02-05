package nsf.support.tools.converter;

/**
 * @(#) NResultSetConverter.java  
 * 
 */
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import nsf.core.exception.NException;
import nsf.core.log.NLog;
import nsf.core.log.NLogUtils;
import nsf.support.collection.NData;
import nsf.support.collection.NMultiData;

/**
 * <pre>
 * ResultSet을 필요한 Data형으로 변환한다.                                        
 * 이 NResultSetConverter 클래스는 JDK V1.3이상에서 동작한다.
 * </pre>
 * 
 * @since 2007년 1월 5일
 * @version NSF 1.0
 * 
 * @author 강영진, Nova China 
 * 2009년 9월 16일 리충일 blob,clob형 추가
 */
public class NResultSetConverter {

	/**
	 * ResultSet에 담겨있는 정보를 NData형으로 변환한다. 수자의 경우 DB column의 Number 길이에 따라 int 혹은
	 * long형으로 저장되며 Float와 Double형식의 경우 Double로 저장된다.
     * blob형,clob형 얻기를 새로 추가
	 * 
	 * @param rs
	 *            ResultSet
	 * @return ld NData
	 */
	public static NData toData(ResultSet rs) throws SQLException {
		NData ld = new NData("ResultSet");
		toData(rs, ld);
		return ld;
	}

	/**
	 * ResultSet에 담겨있는 정보를 NMultiData형으로 변환한다. 수자의 경우 DB column의 Number 길이에 따라
	 * int 혹은 long형으로 저장되며 Float와 Double형식의 경우 Double로 저장된다.
	 * 
	 * @param rs
	 *            ResultSet
	 * @return lmd NMultiData
	 */
	public static NMultiData toMultiData(ResultSet rs) throws SQLException {

		NMultiData lmd = new NMultiData("ResultSet");
		toMultiData(rs, lmd);
		return lmd;
	}
    public static NMultiData toMultiData0(ResultSet rs) throws SQLException {

        NMultiData lmd = new NMultiData("ResultSet");
        toMultiData0(rs, lmd);
        return lmd;
    }
	/**
	 * ResultSet에 담겨있는 정보를 NData형으로 변환한다. 수자의 경우 DB column의 Number 길이에 따라 int 혹은
	 * long형으로 저장되며 Float와 Double형식의 경우 Double로 저장된다.
	 * 
	 * @param rs
	 *            ResultSet
	 * @param ld
	 *            NData ResultSet에 담겨있는 정보를 저장할 변수
	 */
	public static void toData(ResultSet rs, NData ld) throws SQLException {

		try {
			ResultSetMetaData rsmd = rs.getMetaData();
			final int numberOfColumns = rsmd.getColumnCount();
			String[] attributeNameList = new String[numberOfColumns];
			int[] attributeTypeList = new int[numberOfColumns];

			for (int inx = 0; inx < numberOfColumns; inx++) {
				final int index = inx+1;
				attributeNameList[inx] = NDefaultNaming.getAttributeName(rsmd.getColumnLabel(index).toLowerCase());
				attributeTypeList[inx] = rsmd.getColumnType(index);
			}

			if (rs.next()) {
				for (int inx = 0; inx < numberOfColumns; inx++) {
					final String attributeName = attributeNameList[inx];
					final int attributeType = attributeTypeList[inx];
					final int index = inx + 1;

					if (attributeType == Types.VARCHAR || attributeType == Types.CHAR) {
						ld.setString(attributeName, rs.getString(index));
					} else if (attributeType == Types.NUMERIC || attributeType == Types.DECIMAL) {
						BigDecimal bd = rs.getBigDecimal(index);
						if (bd == null) {
							ld.setBigDecimal(attributeName, null);
						} else {
							if (bd.scale() > 0) {
								ld.setBigDecimal(attributeName, bd);
							} else {
								long intExpected = bd.longValue();
								if (intExpected < Integer.MAX_VALUE && intExpected > Integer.MIN_VALUE) {
									ld.setInt(attributeName, bd.intValue());
								} else {
									ld.setLong(attributeName, intExpected);
								}
							}
						}
					} else {
						ld.put(attributeName, rs.getObject(index));
					}
				}
			}
		} catch (SQLException se) {
			NLog.report.println(NLogUtils.toDefaultLogForm("NResultSetConverter", "toData(ResultSet)", "NSF_DAO_008", "error occured while extracting data from ResultSet"));
			throw se;
		}
	}

	/**
	 * ResultSet에 담겨있는 정보를 NMultiData형으로 변환한다. 수자의 경우 DB column의 Number 길이에 따라
	 * int 혹은 long형으로 저장되며 Float와 Double형식의 경우 Double로 저장된다.
	 * 
	 * @param rs
	 *            ResultSet
	 * @param lmd
	 *            NMultiData ResultSet에 담겨있는 정보를 저장할 변수
	 */
	public static void toMultiData(ResultSet rs, NMultiData lmd) throws SQLException {

		try {
			ResultSetMetaData rsmd = rs.getMetaData();
			final int numberOfColumns = rsmd.getColumnCount();
			String[] attributeNameList = new String[numberOfColumns];
			int[] attributeTypeList = new int[numberOfColumns];

			for (int inx = 0; inx < numberOfColumns; inx++) {
				final int index = inx+1;
				attributeNameList[inx] = NDefaultNaming.getAttributeName(rsmd.getColumnLabel(index).toLowerCase());
				attributeTypeList[inx] = rsmd.getColumnType(index);
			}

			while (rs.next()) {

				for (int inx = 0; inx < numberOfColumns; inx++) {

					final String attributeName = attributeNameList[inx];
					final int attributeType = attributeTypeList[inx];
					final int index = inx + 1;

					if (attributeType == Types.VARCHAR || attributeType == Types.CHAR) {
						lmd.add(attributeName, rs.getString(index));
					} else if (attributeType == Types.NUMERIC || attributeType == Types.DECIMAL) {
						BigDecimal bd = rs.getBigDecimal(index);
						if (bd == null) {
							lmd.addBigDecimal(attributeName, null);
						} else {
							if (bd.scale() > 0) {
								lmd.addBigDecimal(attributeName, bd);
							} else {
								long intExpected = bd.longValue();
								if (intExpected < Integer.MAX_VALUE && intExpected > Integer.MIN_VALUE) {
									lmd.addInt(attributeName, bd.intValue());
								} else {
									lmd.addLong(attributeName, intExpected);
								}
							}
						}
					} else {
						lmd.add(attributeName, rs.getObject(index));
					}
				}
			}
		} catch (SQLException se) {
			NLog.report.println(NLogUtils.toDefaultLogForm("NResultSetConverter", "toMultiData(ResultSet)", "NSF_DAO_008", "error occured while extracting data from ResultSet"));
			throw se;
		}
	}
    public static void toMultiData0(ResultSet rs, NMultiData lmd) throws SQLException {

        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            final int numberOfColumns = rsmd.getColumnCount();
            String[] attributeNameList = new String[numberOfColumns];
            int[] attributeTypeList = new int[numberOfColumns];

            for (int inx = 0; inx < numberOfColumns; inx++) {
                final int index = inx+1;
                //attributeNameList[inx] = NDefaultNaming.getAttributeName(rsmd.getColumnLabel(index).toLowerCase());
                attributeNameList[inx] = NDefaultNaming.getAttributeName("field"+inx);

                attributeTypeList[inx] = rsmd.getColumnType(index);
            }

            while (rs.next()) {

                for (int inx = 0; inx < numberOfColumns; inx++) {

                    final String attributeName = attributeNameList[inx];
                    final int attributeType = attributeTypeList[inx];
                    final int index = inx + 1;

                    if (attributeType == Types.VARCHAR || attributeType == Types.CHAR) {
                        lmd.add(attributeName, rs.getString(index));
                    } else if (attributeType == Types.NUMERIC || attributeType == Types.DECIMAL) {
                        BigDecimal bd = rs.getBigDecimal(index);
                        if (bd == null) {
                            lmd.addBigDecimal(attributeName, null);
                        } else {
                            if (bd.scale() > 0) {
                                lmd.addBigDecimal(attributeName, bd);
                            } else {
                                long intExpected = bd.longValue();
                                if (intExpected < Integer.MAX_VALUE && intExpected > Integer.MIN_VALUE) {
                                    lmd.addInt(attributeName, bd.intValue());
                                } else {
                                    lmd.addLong(attributeName, intExpected);
                                }
                            }
                        }
                    } else {
                        lmd.add(attributeName, rs.getObject(index));
                    }
                }
            }
        } catch (SQLException se) {
            NLog.report.println(NLogUtils.toDefaultLogForm("NResultSetConverter", "toMultiData(ResultSet)", "NSF_DAO_008", "error occured while extracting data from ResultSet"));
            throw se;
        }
    }   
    
	/**
	 * ResultSet에 담겨있는 정보를 Entity로 변환한다. 변환시 Entity의 set함수를 리용하여 변수에 값을 저장한다.
	 * 
	 * @param rs
	 *            Data를 담고 있는 ResultSet
	 * @param target
	 *            Data를 저장할 Entity Object
	 */
	public static void toEntity(ResultSet rs, Object target) throws SQLException, NException {
		if (target == null) {
			throw new NException("NSF_DAO_005", "Target Entity Class is null");
		}

		ResultSetMetaData rsmd = rs.getMetaData();
		final int numberOfColumns = rsmd.getColumnCount();
		String[] attributeNameList = new String[numberOfColumns];
		String[] setMethodNameList = new String[numberOfColumns];

		for (int inx = 0; inx < numberOfColumns; inx++) {
			final int index = inx+1;
			attributeNameList[inx] = NDefaultNaming.getAttributeName(rsmd.getColumnLabel(index).toLowerCase());
			setMethodNameList[inx] = NDefaultNaming.getSetMethodName(rsmd.getColumnName(index).toLowerCase());
		}

		Method setMethod = null;

		try {
			if (rs.next()) {
				Class c = target.getClass();
				for (int inx = 0; inx < numberOfColumns; inx++) {

					final String attributeName = attributeNameList[inx];
					final String setMethodName = setMethodNameList[inx];
					final int index = inx + 1;

					Field field = null;
					try {
						field = c.getDeclaredField(attributeName);
						setMethod = getSetterMethod(target, field, setMethodName);
						setMethod.invoke(target, getParameterValue(field, rs, index));
					} catch (NoSuchFieldException nsfe) {
						continue;
					}
				}
			}
		} catch (SQLException se) {
			NLog.report.println(NLogUtils.toDefaultLogForm("NResultSetConverter", "toEntity", "NSF_DAO_008", "error occured while extracting data from ResultSet"));
			throw se;
		} catch (Exception e) {
			throw new NException("NSF_DAO_008", "error occured while extracting data from ResultSet");
		}
	}

	/**
	 * ResultSet에 담겨있는 정보를 Entity의 ArrayList로 변환한다. 변환시 Entity의 set함수를 리용하여 변수에
	 * 값을 저장한다. Multi처리를 위하여 Entity Object를 ArrayList에 담아 반환한다.
	 * 
	 * @param rs
	 *            Data를 담고 있는 ResultSet
	 * @param target
	 *            Data를 저장할 Entity Object
	 * @return list Data를 담고 있는 Entity Object의 집합
	 */
	public static ArrayList toMultiEntity(ResultSet rs, Object target) throws SQLException, NException {
		if (target == null) {
			throw new NException("NSF_DAO_005", "Target Entity Class is null");
		}

		ResultSetMetaData rsmd = rs.getMetaData();
		final int numberOfColumns = rsmd.getColumnCount();
		String[] attributeNameList = new String[numberOfColumns];
		String[] setMethodNameList = new String[numberOfColumns];

		for (int inx = 0; inx < numberOfColumns; inx++) {
			final int index = inx+1;
			attributeNameList[inx] = NDefaultNaming.getAttributeName(rsmd.getColumnLabel(index).toLowerCase());
			setMethodNameList[inx] = NDefaultNaming.getSetMethodName(rsmd.getColumnName(index).toLowerCase());
		}

		ArrayList list = new ArrayList();
		Method setMethod = null;

		try {
			while (rs.next()) {

				Class c = target.getClass();
				Object valueObject = c.newInstance();
				for (int inx = 0; inx < numberOfColumns; inx++) {

					final String attributeName = attributeNameList[inx];
					final String setMethodName = setMethodNameList[inx];
					final int index = inx + 1;

					Field field = null;
					try {
						field = c.getDeclaredField(attributeName);
						setMethod = getSetterMethod(valueObject, field, setMethodName);
						setMethod.invoke(valueObject, getParameterValue(field, rs, index));
					} catch (NoSuchFieldException nsfe) {
						continue;
					}
				}
				list.add(valueObject);
			}
			return list;
		} catch (SQLException se) {
			NLog.report.println(NLogUtils.toDefaultLogForm("NResultSetConverter", "toMultiEntity", "NSF_DAO_008", "error occured while extracting data from ResultSet"));
			throw se;
		} catch (Exception e) {
			throw new NException("NSF_DAO_008", "error occured while extracting data from ResultSet");
		}
	}

	/**
	 * ResultSet에 담겨있는 정보를 Method를 통하여 Entity에 저장할때 Method 실행때 필요한 Parameter를 반환한다.
	 * 
	 * @param filed
	 *            Method를 리용하여 설정될 Field
	 * @param rs
	 *            Data를 가지고 있는 ResultSet
	 * @param i
	 *            ResultSet의 column Index
	 * @return paramValueArray Object[] setter Method실행시 필요한 Parameter
	 */
	private static Object[] getParameterValue(Field field, ResultSet rs, int i) throws SQLException {
		Object[] paramValueArray = null;
		Class fieldtype = field.getType();

		if (fieldtype == String.class) {
			paramValueArray = new Object[] { ((rs.getObject(i) == null) ? "" : rs.getString(i)) };
		} else if (fieldtype == int.class) {
			paramValueArray = new Object[] { new Integer(rs.getInt(i)) };
		} else if (fieldtype == double.class) {
			paramValueArray = new Object[] { new Double(rs.getDouble(i)) };
		} else if (fieldtype == long.class) {
			paramValueArray = new Object[] { new Long(rs.getLong(i)) };
		} else if (fieldtype == float.class) {
			paramValueArray = new Object[] { new Float(rs.getFloat(i)) };
		} else if (fieldtype == boolean.class) {
			paramValueArray = new Object[] { new Boolean(rs.getBoolean(i)) };
		}

		return paramValueArray;
	}

	/**
	 * ResultSet에 담겨있는 정보를 Method를 통하여 Entity에 저장할때 필요한 Method를 return한다.
	 * 
	 * @param valueObject
	 *            Method를 가지고 있는 Object
	 * @param filed
	 *            Method를 리용하여 설정될 Field
	 * @param methodName
	 *            함수명
	 * @return method method명을 통하여 얻기한 함수
	 */
	private static Method getSetterMethod(Object valueObject, Field field, String methodName) throws Exception {
		Method method = null;

		try {
			Class[] paramTypeClasses = new Class[] { field.getType() };
			method = valueObject.getClass().getMethod(methodName, paramTypeClasses);
		} catch (Exception e) {
			throw e;
		}

		return method;
	}

	/**
	 * ResultSet에 담겨있는 정보를 Entity형으로 변환한다.
	 * 
	 * @param rs
	 *            Data를 담고 있는 ResultSet
	 * @param target
	 *            Data를 저장할 Entity Object
	 */
	public static void toPublicEntity(ResultSet rs, Object target) throws SQLException, NException, Exception {
		if (target == null) {
			throw new NException("NSF_DAO_005", "Target Entity Class is null");
		}
		StringBuffer sb = new StringBuffer();

		ResultSetMetaData rsmd = rs.getMetaData();
		final int numberOfColumns = rsmd.getColumnCount();
		String[] attributeNameList = new String[numberOfColumns];
		String[] setMethodNameList = new String[numberOfColumns];

		for (int inx = 0; inx < numberOfColumns; inx++) {
			final int index = inx+1;
			attributeNameList[inx] = NDefaultNaming.getAttributeName(rsmd.getColumnLabel(index).toLowerCase());
			setMethodNameList[inx] = NDefaultNaming.getSetMethodName(rsmd.getColumnName(index).toLowerCase());
		}

		try {
			if (rs.next()) {

				Class c = target.getClass();

				for (int inx = 0; inx < numberOfColumns; inx++) {
					final String attributeName = attributeNameList[inx];
					final int index = inx + 1;

					Field field = null;
					try {
						field = c.getField(attributeName);
					} catch (NoSuchFieldException nse) {
						continue;
					}
					Class fieldtype = field.getType();

					if (fieldtype == String.class) {
						field.set(target, ((rs.getObject(index) == null) ? "" : rs.getString(index)));
					} else if (fieldtype == int.class) {
						field.setInt(target, rs.getInt(index));
					} else if (fieldtype == double.class) {
						field.setDouble(target, rs.getDouble(index));
					} else if (fieldtype == long.class) {
						field.setLong(target, rs.getLong(index));
					} else if (fieldtype == float.class) {
						field.setFloat(target, rs.getFloat(index));
					} else if (fieldtype == boolean.class) {
						field.setBoolean(target, rs.getBoolean(index));
					}
					sb.append(fieldtype + " : " + field.get(target) + "\n");
				}
			}
		} catch (SQLException se) {
			NLog.report.println(NLogUtils.toDefaultLogForm("NResultSetConverter", "toPublicEntity", "NSF_DAO_008", "error occured while extracting data from ResultSet"));
			throw se;
		} catch (Exception e) {
			throw new NException("NSF_DAO_008", "error occured while extracting data from ResultSet");
		}
	}
}

