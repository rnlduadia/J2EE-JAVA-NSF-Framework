package nsf.foundation.persistent.db.dao;

/**
 * @(#) NSingleDao.java
 */

import java.io.*;

import java.sql.*;
import java.util.LinkedList;

import nsf.core.exception.NException;
import nsf.core.log.NLog;
import nsf.foundation.persistent.db.query.NQueryMaker;
import nsf.support.collection.NData;
import nsf.support.collection.NMultiData;
import nsf.support.tools.converter.NResultSetConverter;

import oracle.jdbc.driver.*;
import oracle.sql.BLOB;
import oracle.sql.CLOB;

/**
 * <pre>
 *   NSingleDao 클래스는 Transaction이 묶이지 않는 단순 SQL문을 처리할 때 사용하는 클라스이다.
 *   단일 SQL을 execute하고 결과를 return한다.
 * </pre>
 * 
 * @since 2007년 1월 5일
 * @version NSF 1.0
 *
 * @author 리향성, Nova China<br>
 */

public class NSingleDao extends NAbstractDao {

	private String queryString = null;
	private NData parameter = null;
    private NQueryMaker queryMaker = new NQueryMaker();
/**
	 * 일반적인 구축자이다. <BR>
	 */
	public NSingleDao() {
	}
    public NSingleDao(String dbSpec) {
        this.dbSpec = dbSpec;
    }
	/**
	 * 일반적인 구축자이다. <BR>
	 * setQueryString를 사용하지 않고 execute계렬의 메소드를 사용할 수 있다.
	 * 
	 * @param queryString
	 *            쿼리이름
	 * @param parameterData
	 *            쿼리에서 Binding될 파라메터
	 */
	public NSingleDao(String queryString, NData parameterData) throws NException {
		this(queryString, parameterData, "default");
	}

	/**
	 * 일반적인 구축자이다. <BR>
	 * setQueryString를 사용하지 않고 execute계렬의 메소드를 사용할 수 있다.
	 * 
	 * @param queryString
	 *            쿼리이름
	 * @param parameterData
	 *            쿼리에서 Binding될 파라메터
	 * @param dbSpec
	 *            nsf.xml에 정의되어 있는 DB Spec이름
	 */
	public NSingleDao(String newQueryString, NData parameterData, String dbSpec) throws NException {
		this.queryString = newQueryString;
		this.parameter = parameterData;
		this.dbSpec = dbSpec;
		if(parameterData != null){
			parameterData.setNullToInitialize(true);
		}
		this.queryMaker.resolveSQL(queryString, parameterData);
	}

	/**
	 * 쿼리이름과 Parameter Data를 설정한다. 이 메소드는 미리 설정해 놓고 execute()계렬메소드를 바로 사용하고자 할 때
	 * 사용된다.
	 * 
	 * @param queryString
	 *            쿼리이름
	 * @param parameterData
	 *            쿼리에서 Binding될 파라메터
	 */
	public void setQueryString(String queryString, NData parameterData) throws NException {
		this.setQueryString(queryString, parameterData, "default");
	}

	/**
	 * 쿼리이름과 Parameter Data를 설정한다. <BR>
	 * 이 메소드는 미리 설정해 놓고 execute()계렬메소드를 직접 사용하려고 할 때 사용된다.
	 * 
	 * @param queryString
	 *            쿼리이름
	 * @param parameterData
	 *            쿼리에서 Binding될 파라메터
	 * @param dbSpec
	 *            nsf.xml에 정의되어 있는 DB Spec이름
	 */
	public void setQueryString(String newQueryString, NData parameterData, String dbSpec) throws NException {
		this.queryString = newQueryString;
		this.parameter = parameterData;
		this.dbSpec = dbSpec;
		this.queryMaker.resolveSQL(queryString, parameterData);
	}

	/**
	 * 이 메소드는 SELECT SQL문에만 사용할 수 있다. <BR>
	 * 이 메소드는 구축자에서 Query와 관련된 값을 설정하지 않았거나, <BR>
	 * setQueryString메소드를 사용해서 Query를 설정하지 않았을 경우에 <BR>
	 * Query와 관련된 값을 설정과 동시에 executeQuery를 수행하고자 할 때 사용된다. <BR>
	 * 여기서는 자동으로 Connection을 얻어오고, 그 Connection으로 SQL문을 수한다. <BR>
	 * 수행이 끝나면, ResultSet, PaparedStatement, Connection을 close하고 <BR>
	 * 결과를 NMultiData 형태로 Return한다.
	 * 
	 * @param queryString
	 *            쿼리이름
	 * @param parameterData
	 *            쿼리에서 Binding될 파라메터
	 * @return NMultiData SELECT문을 수행 후 얻어진 결과를 NMultiData형태로 Return한다.
	 */
	public NMultiData executeQuery(String newQueryString, NData parameterData) throws NException {
		return this.executeQuery(newQueryString, parameterData, "default");
	}

	/**
	 * 이 메소드는 SELECT SQL문에만 사용할 수 있다. <BR>
	 * 이 메소드는 구축자에서 Query와 관련된 값을 설정하지 않았거나, <BR>
	 * setQueryString메소드를 사용해서 Query를 설정하지 않았을 경우에 <BR>
	 * Query와 관련된 값을 설정과 동시에 executeQuery를 수행하고자 할 때 사용된다. <BR>
	 * 여기서는 자동으로 Connection을 얻어오고, 그 Connection으로 SQL문을 수한다. <BR>
	 * 수행이 끝나면, ResultSet, PaparedStatement, Connection을 close하고 <BR>
	 * 결과를 NMultiData 형태로 Return한다.
	 * 
	 * @param queryString
	 *            쿼리이름
	 * @param parameterData
	 *            쿼리에서 Binding될 파라메터
	 * @param dbSpec
	 *            nsf.xml에 정의되어 있는 DB Spec이름
	 * @return NMultiData SELECT문을 수행 후 얻어진 결과를 NMultiData형태로 Return한다.
	 */
	public NMultiData executeQuery(String newQueryString, NData parameterData, String dbSpec) throws NException {
		this.queryString = newQueryString;
		this.parameter = parameterData;
		this.dbSpec = dbSpec;
		this.queryMaker.resolveSQL(this.queryString, parameterData);
		return this.executeQuery();
	}
    
    /**
     * 여기서는 자동으로 Connection을 얻어오고, 그 Connection으로 패키지'문을 수행한다. <BR>
     * 수행이 끝나면, ResultSet, CallableStatement, Connection을 close하고 <BR>
     * 결과를 NMultiData 형태로 Return한다.
     * 
     * @return NMultiData SELECT문을 수행 후 얻어진 결과를 NMultiData형태로 Return한다.
     */
    public NMultiData execute() throws NException {
        Connection conn = null;
        try {
            conn = getConnection(this.dbSpec);
            return execute(conn);
        } catch (NException le) {
            throw le;
        } catch (Exception e) {
            throw new NException(e);
        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (Exception e) {
                NLog.report.println("Exception Occured while connection close : " + e.getMessage());
                throw new NException(e);
            }
        }
    }
    public NMultiData execute0() throws NException {
        Connection conn = null;
        try {
            conn = getConnection(this.dbSpec);
            return execute0(conn);
        } catch (NException le) {
            throw le;
        } catch (Exception e) {
            throw new NException(e);
        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (Exception e) {
                NLog.report.println("Exception Occured while connection close : " + e.getMessage());
                throw new NException(e);
            }
        }
    }    
    /**
     * 여기서는 자동으로 Connection을 얻어오고, 그 Connection으로 패키지'문을 수행한다. <BR>
     * 이 패케지는 pstatus가 하나 더들어 있는것이다 <BR>
     * 수행이 끝나면, ResultSet, CallableStatement, Connection을 close하고 <BR>
     * 결과를 NMultiData 형태로 Return한다.
     * 
     * @return NMultiData SELECT문을 수행 후 얻어진 결과를 NMultiData형태로 Return한다.
     */
    public NMultiData execute_s() throws NException {
        Connection conn = null;
        try {
            conn = getConnection(this.dbSpec);
            return execute_s(conn);
        } catch (NException le) {
            throw le;
        } catch (Exception e) {
            throw new NException(e);
        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (Exception e) {
                NLog.report.println("Exception Occured while connection close : " + e.getMessage());
                throw new NException(e);
            }
        }
    }
       public String[] execute(int a) throws NException {
        Connection conn = null;
        try {
            conn = getConnection(this.dbSpec);
            if(a==1)return execute1(conn);
            else if(a==2) return execute2(conn);
            else if(a==3) return execute3(conn);
            else return execute4(conn);
        } catch (NException le) {
            throw le;
        } catch (Exception e) {
            throw new NException(e);
        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (Exception e) {
                NLog.report.println("Exception Occured while connection close : " + e.getMessage());
                throw new NException(e);
            }
        }
    }
    public NMultiData execute(String query,LinkedList keys,NData parameter1) throws NException {
        Connection conn = null;
        try {
            conn = getConnection(this.dbSpec);
            return pkgexecute(conn,query,keys,parameter1);
        } catch (NException le) {
            throw le;
        } catch (Exception e) {
            throw new NException(e);
        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (Exception e) {
                NLog.report.println("Exception Occured while connection close : " + e.getMessage());
                throw new NException(e);
            }
        }
    }

	/**
	 * 이 메소드는 SELECT SQL문에만 사용할 수 있다. <BR>
	 * 이 메소드는 구축자에서 Query와 관련된 값을 설정했거나, <BR>
	 * setQueryString메소드를 사용해서 Query를 설정했을 경우에 <BR>
	 * 설정되어진 값을 리용해서 executeQuery를 수행한다. <BR>
	 * 여기서는 자동으로 Connection을 얻어오고, 그 Connection으로 SQL문을 수한다. <BR>
	 * 수행이 끝나면, ResultSet, PaparedStatement, Connection을 close하고 <BR>
	 * 결과를 NMultiData 형태로 Return한다.
	 * 
	 * @return NMultiData SELECT문을 수행 후 얻어진 결과를 NMultiData형태로 Return한다.
	 */
	public NMultiData executeQuery() throws NException {
		Connection conn = null;
		try {
			conn = getConnection(this.dbSpec);
			return executeQuery(conn);
		} catch (NException le) {
			throw le;
		} catch (Exception e) {
			throw new NException(e);
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (Exception e) {
				NLog.report.println("Exception Occured while connection close : " + e.getMessage());
				throw new NException(e);
			}
		}
	}

    public NMultiData executeQuery(String query) throws NException {
        Connection conn = null;
        try {
            conn = getConnection(this.dbSpec);
            return executeQuery(conn,query);
        } catch (NException le) {
            throw le;
        } catch (Exception e) {
            throw new NException(e);
        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (Exception e) {
                NLog.report.println("Exception Occured while connection close : " + e.getMessage());
                throw new NException(e);
            }
        }
    }
	/**
	 * 이 메소드는 SELECT SQL문에만 사용할 수 있다. <BR>
	 * 이 메소드는 구축자에서 Query와 관련된 값을 설정하지 않았거나, <BR>
	 * setQueryString메소드를 사용해서 Query를 설정하지 않았을 경우에 <BR>
	 * Query와 관련된 값을 설정과 동시에 executeQuery를 수행하고자 할 때 사용된다. <BR>
	 * 여기서는 자동으로 Connection을 얻어오고, 그 Connection으로 SQL문을 수한다. <BR>
	 * 수행이 끝나면, ResultSet, PaparedStatement, Connection을 close하고 <BR>
	 * 결과를 NData 형태로 Return한다.
	 * 
	 * @param queryString
	 *            쿼리이름
	 * @param parameterData
	 *            쿼리에서 Binding될 파라메터
	 * @return NData SELECT문을 수행 후 얻어진 결과를 NData형태로 Return한다.
	 */
	public NData executeQueryForSingle(String newQueryString, NData parameterData) throws NException {
		return this.executeQueryForSingle(newQueryString, parameterData, "default");
	}

	/**
	 * 이 메소드는 SELECT SQL문에만 사용할 수 있다. <BR>
	 * 이 메소드는 구축자에서 Query와 관련된 값을 설정하지 않았거나, <BR>
	 * setQueryString메소드를 사용해서 Query를 설정하지 않았을 경우에 <BR>
	 * Query와 관련된 값을 설정과 동시에 executeQuery를 수행하고자 할 때 사용된다. <BR>
	 * 여기서는 자동으로 Connection을 얻어오고, 그 Connection으로 SQL문을 수한다. <BR>
	 * 수행이 끝나면, ResultSet, PaparedStatement, Connection을 close하고 <BR>
	 * 결과를 NData 형태로 Return한다.
	 * 
	 * @param queryString
	 *            쿼리이름
	 * @param parameterData
	 *            쿼리에서 Binding될 파라메터
	 * @param dbSpec
	 *            nsf.xml에 정의되어 있는 DB Spec이름
	 * @return NData SELECT문을 수행 후 얻어진 결과를 NData형태로 Return한다.
	 */
	public NData executeQueryForSingle(String newQueryString, NData parameterData, String dbSpec) throws NException {
		this.queryString = newQueryString;
		this.parameter = parameterData;
		this.dbSpec = dbSpec;
		this.queryMaker.resolveSQL(this.queryString, parameterData);
		return this.executeQueryForSingle();
	}
    /**
	 * 이 메소드는 SELECT SQL문에만 사용할 수 있다. <BR>
	 * 이 메소드는 구축자에서 Query와 관련된 값을 설정하했거나, <BR>
	 * setQueryString메소드를 사용해서 Query를 설정했을 경우에 <BR>
	 * 설정되어진 값을 리용해서 executeQuery를 수행한다. <BR>
	 * 여기서는 자동으로 Connection을 얻어오고, 그 Connection으로 SQL문을 수한다. <BR>
	 * 수행이 끝나면, ResultSet, PaparedStatement, Connection을 close하고 <BR>
	 * 결과를 NData 형태로 Return한다.
	 * 
	 * @return NData SELECT문을 수행 후 얻어진 결과를 NData형태로 Return한다.
	 */
	public NData executeQueryForSingle() throws NException {
		Connection conn = null;
		try {
			conn = getConnection(this.dbSpec);
			return executeQueryForSingle(conn);
		} catch (NException le) {
			throw le;
		} catch (Exception e) {
			throw new NException(e);
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (Exception e) {
				NLog.report.println("Exception Occured while connection close : " + e.getMessage());
				throw new NException(e);
			}
		}
	}

	/**
	 * 이 메소드는 Update, Delete, Insert SQL문에만 사용할 수 있다. <BR>
	 * 이 메소드는 구축자에서 Query와 관련된 값을 설정하지 않았거나, <BR>
	 * setQueryString메소드를 사용해서 Query를 설정하지 않았을 경우에 <BR>
	 * Query와 관련된 값을 설정과 동시에 executeUpdate를 수행하고자 할 때 사용된다. <BR>
	 * 여기서는 자동으로 Connection을 얻어오고, 그 Connection으로 SQL문을 수한다. <BR>
	 * 수행이 끝나면, PaparedStatement, Connection을 close하고 <BR>
	 * 결과를 int 형태로 Return한다.
	 * 
	 * @param queryString
	 *            쿼리이름
	 * @param parameterData
	 *            쿼리에서 Binding될 파라메터
	 * @return int SQL 수행 후 얻어진 결과를 int형태로 Return한다.
	 */
	public int executeUpdate(String newQueryString, NData parameterData) throws NException {
		return this.executeUpdate(newQueryString, parameterData, "default");
	}

	/**
	 * 이 메소드는 Update, Delete, Insert SQL문에만 사용할 수 있다. <BR>
	 * 이 메소드는 구축자에서 Query와 관련된 값을 설정하지 않았거나, <BR>
	 * setQueryString메소드를 사용해서 Query를 설정하지 않았을 경우에 <BR>
	 * Query와 관련된 값을 설정과 동시에 executeUpdate를 수행하고자 할 때 사용된다. <BR>
	 * 여기서는 자동으로 Connection을 얻어오고, 그 Connection으로 SQL문을 수한다. <BR>
	 * 수행이 끝나면, PaparedStatement, Connection을 close하고 <BR>
	 * 결과를 int 형태로 Return한다.
	 * 
	 * @param queryString
	 *            쿼리이름
	 * @param parameterData
	 *            쿼리에서 Binding될 파라메터
	 * @param dbSpec
	 *            nsf.xml에 정의되어 있는 DB Spec이름
	 * @return int SQL 수행 후 얻어진 결과를 int형태로 Return한다.
	 */
	public int executeUpdate(String newQueryString, NData parameterData, String dbSpec) throws NException {
		this.queryString = newQueryString;
		this.parameter = parameterData;
		this.dbSpec = dbSpec;
		this.queryMaker.resolveSQL(this.queryString, parameterData);
		return this.executeUpdate();
	}

	public void executeUpdateWithJobType(NData parameterData, String dbSpec) throws NException {
		boolean isWrongType = false;
		String jobType = parameterData.getString(NDaoConstants.CUD_FILTER_KEY);
		if (jobType.equals(NDaoConstants.CREATE_KEY)) {
			this.queryString = this.insertQuery;
		} else if (jobType.equals(NDaoConstants.UPDATE_KEY)) {
			this.queryString = this.updateQuery;
		} else if (jobType.equals(NDaoConstants.DELETE_KEY)) {
			this.queryString = this.deleteQuery;
		} else {
			NLog.debug.println("This executUpdate is not CUD Type");
			isWrongType = true;
		}
		if (!isWrongType) {
			this.executeUpdate(this.queryString, parameterData, dbSpec);
		}
	}

	public void executeUpdateWithJobType(NData parameterData) throws NException {
		this.executeUpdateWithJobType(parameterData, "default");
	}

	/**
	 * 이 메소드는 Update, Delete, Insert SQL문에만 사용할 수 있다. <BR>
	 * 이 메소드는 구축자에서 Query와 관련된 값을 설정을 미리 했거나, <BR>
	 * setQueryString메소드를 사용해서 Query를 설정했을 경우에 <BR>
	 * 설정되어진 값을 리용해서 executeUpdate를 수행한다. <BR>
	 * 여기서는 자동으로 Connection을 얻어오고, 그 Connection으로 SQL문을 수한다. <BR>
	 * 수행이 끝나면 PaparedStatement, Connection을 close하고 <BR>
	 * 결과를 int 형태로 Return한다.
	 * 
	 * @return int SQL 수행 후 얻어진 결과를 int형태로 Return한다.
	 */
	public int executeUpdate() throws NException {
		Connection conn = null;
		int result = 0;
		try {
			conn = getConnection(this.dbSpec);
			conn.setAutoCommit(false);
			result = executeUpdate(conn);
			conn.commit();
			return result;
		} catch (NException le) {
			try {
				if (conn != null)
					conn.rollback();
			} catch (SQLException e) {
				NLog.report.println("Exception Occured : while conneection rollback ");
			}
			throw le;
		} catch (Exception le) {
			try {
				if (conn != null)
					conn.rollback();
			} catch (SQLException e) {
				NLog.report.println("Exception Occured : while conneection rollback ");
			}
			throw new NException(le.getMessage(), le);
		} finally {
			try {
				if (conn != null && !conn.isClosed())
					conn.close();
			} catch (Exception e) {
				NLog.report.println("Exception Occured while connection close : " + e.getMessage());
				throw new NException(e);
			}
		}
	}
    public int executeUpdate(String sql) throws NException {
        Connection conn = null;
        int result = 0;
        try {
            conn = getConnection(this.dbSpec);
            conn.setAutoCommit(false);
            result = executeUpdate(conn,sql);
            conn.commit();
            return result;
        } catch (NException le) {
            try {
                if (conn != null)
                    conn.rollback();
            } catch (SQLException e) {
                NLog.report.println("Exception Occured : while conneection rollback ");
            }
            throw le;
        } catch (Exception le) {
            try {
                if (conn != null)
                    conn.rollback();
            } catch (SQLException e) {
                NLog.report.println("Exception Occured : while conneection rollback ");
            }
            throw new NException(le.getMessage(), le);
        } finally {
            try {
                if (conn != null && !conn.isClosed())
                    conn.close();
            } catch (Exception e) {
                NLog.report.println("Exception Occured while connection close : " + e.getMessage());
                throw new NException(e);
            }
        }
    }
    
    /**
     * 이 메소드는 execute계렬 메소드의 내부에서 사용하는 내부메소드이다. <BR>
     * 이곳에서 실제로 Data Access와 관련된 일을 한다.
     * package의 출구변수가 pcursor하나인것 
     * 
     * @param conn
     * DB Connection
     * @return NMultiData SELECT문을 수행 후 얻어진 결과를 NMultiData형태로 Return한다.
     * 
     */
    protected NMultiData execute(Connection conn) throws NException {
        String query = queryMaker.getQuery();
        NData queryArgument = queryMaker.getQueryArgument();
        LinkedList keys = (LinkedList) queryArgument.get("key");

        ResultSet rs = null;
        CallableStatement cs = null;
        String pkgstr="{ call "+ query + "}" ; 
        try {
            cs = conn.prepareCall(pkgstr);
            int parameterSize = keys.size();
            for (int inx = 0; inx < parameterSize; inx++) {
                String key = keys.get(inx).toString();
                Object parameterValue = parameter.getString(key);
                setValue(cs, inx + 1, parameterValue); // params
          }
            cs.registerOutParameter (parameterSize+1, OracleTypes.CURSOR);
            cs.execute ();
            rs = (ResultSet)cs.getObject (parameterSize+1);
            return NResultSetConverter.toMultiData(rs);
        } catch (SQLException se) {
            NLog.report.println("NSF_DAO_006 : " + se);
            throw new NException("NSF_DAO_006", se.getMessage(), se);
        } catch (Exception e) {
            NLog.report.println("NSF_DAO_006 : " + e);
            throw new NException("NSF_DAO_006", e.getMessage(), e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                    rs = null;
                } catch (Exception e) {
                    NLog.report.println("Exception Occured while ResultSet close : " + e.getMessage());
                    throw new NException(e);
                }
            }
            if (cs != null) {
                try {
                    cs.close();
                    cs = null;
                } catch (Exception e) {
                    NLog.report.println("Exception Occured while PreparedStatemnt close : " + e.getMessage());
                    throw new NException(e);
                }
            }
        }
    }
    protected NMultiData execute0(Connection conn) throws NException {
        String query = queryMaker.getQuery();
        NData queryArgument = queryMaker.getQueryArgument();
        LinkedList keys = (LinkedList) queryArgument.get("key");

        ResultSet rs = null;
        CallableStatement cs = null;
        String pkgstr="{ call "+ query + "}" ; 
        try {
            cs = conn.prepareCall(pkgstr);
            
            int parameterSize = keys.size();
            for (int inx = 0; inx < parameterSize; inx++) {
                String key = keys.get(inx).toString();
                Object parameterValue = parameter.getString(key);
                setValue(cs, inx + 1, parameterValue); // params
          }
            cs.registerOutParameter (parameterSize+1, OracleTypes.CURSOR);
            cs.execute ();
            rs = (ResultSet)cs.getObject (parameterSize+1);

            return NResultSetConverter.toMultiData0(rs);
        } catch (SQLException se) {
            NLog.report.println("NSF_DAO_006 : " + se);
            throw new NException("NSF_DAO_006", se.getMessage(), se);
        } catch (Exception e) {
            NLog.report.println("NSF_DAO_006 : " + e);
            throw new NException("NSF_DAO_006", e.getMessage(), e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                    rs = null;
                } catch (Exception e) {
                    NLog.report.println("Exception Occured while ResultSet close : " + e.getMessage());
                    throw new NException(e);
                }
            }
            if (cs != null) {
                try {
                    cs.close();
                    cs = null;
                } catch (Exception e) {
                    NLog.report.println("Exception Occured while PreparedStatemnt close : " + e.getMessage());
                    throw new NException(e);
                }
            }
        }
    }
    /**
     * 이 메소드는 execute계렬 메소드의 내부에서 사용하는 내부메소드이다. <BR>
     * 이곳에서 실제로 Data Access와 관련된 일을 한다.
     * package의 출구변수가 pcursor하나인것 
     * 
     * @param conn
     * DB Connection
     * @return NMultiData SELECT문을 수행 후 얻어진 결과를 NMultiData형태로 Return한다.
     * 
     */
    protected NMultiData execute_s(Connection conn) throws NException {
        String query = queryMaker.getQuery();
        NData queryArgument = queryMaker.getQueryArgument();
        LinkedList keys = (LinkedList) queryArgument.get("key");

        ResultSet rs = null;
        CallableStatement cs = null;
        String pkgstr="{ call "+ query + "}" ; 
        try {
            cs = conn.prepareCall(pkgstr);
            int parameterSize = keys.size();
            for (int inx = 0; inx < parameterSize; inx++) {
                String key = keys.get(inx).toString();
                Object parameterValue = parameter.getString(key);
                setValue(cs, inx + 1, parameterValue); // params
          }
            cs.registerOutParameter (parameterSize+1, OracleTypes.CURSOR);
            cs.registerOutParameter (parameterSize+2, Types.VARCHAR);
            cs.execute ();
            rs = (ResultSet)cs.getObject (parameterSize+1);
            return NResultSetConverter.toMultiData(rs);
        } catch (SQLException se) {
            NLog.report.println("NSF_DAO_006 : " + se);
            throw new NException("NSF_DAO_006", se.getMessage(), se);
        } catch (Exception e) {
            NLog.report.println("NSF_DAO_006 : " + e);
            throw new NException("NSF_DAO_006", e.getMessage(), e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                    rs = null;
                } catch (Exception e) {
                    NLog.report.println("Exception Occured while ResultSet close : " + e.getMessage());
                    throw new NException(e);
                }
            }
            if (cs != null) {
                try {
                    cs.close();
                    cs = null;
                } catch (Exception e) {
                    NLog.report.println("Exception Occured while PreparedStatemnt close : " + e.getMessage());
                    throw new NException(e);
                }
            }
        }
    }
    ////2008.5.5 홍경희 추가
    protected String[] execute1(Connection conn) throws NException {
        String query = queryMaker.getQuery();
        String aa;
        String bb;
        String[] dd = new String[2];
        NData queryArgument = queryMaker.getQueryArgument();
        LinkedList keys = (LinkedList) queryArgument.get("key");
        CallableStatement cs = null;
        String pkgstr="{ call "+ query + "}" ; 
        try {
            cs = conn.prepareCall(pkgstr);
            int parameterSize = keys.size();
            for (int inx = 0; inx < parameterSize; inx++) {
                String key = keys.get(inx).toString();
                Object parameterValue = parameter.getString(key);
                setValue(cs, inx + 1, parameterValue); // params
          }
            cs.registerOutParameter(parameterSize+1, Types.VARCHAR);
            cs.registerOutParameter(parameterSize+2, Types.NUMERIC);
            cs.execute ();
            bb=cs.getString(parameterSize+1);
            aa= Integer.toString(cs.getInt(parameterSize+2));
            dd[0]=bb;
            dd[1]=aa;
          return dd;
        } catch (SQLException se) {
            NLog.report.println("NSF_DAO_006 : " + se);
            throw new NException("NSF_DAO_006", se.getMessage(), se);
        } catch (Exception e) {
            NLog.report.println("NSF_DAO_006 : " + e);
            throw new NException("NSF_DAO_006", e.getMessage(), e);
        }finally {
            if (cs != null) {
                try {
                    cs.close();
                    cs = null;
                } catch (Exception e) {
                    NLog.report.println("Exception Occured while PreparedStatemnt close : " + e.getMessage());
                    throw new NException(e);
                }
            }
        } 
    }
    protected String[] execute2(Connection conn) throws NException {
        String query = queryMaker.getQuery();
        String aa;
        String bb;
        String cc;
        String[] dd = new String[3];
        NData queryArgument = queryMaker.getQueryArgument();
        LinkedList keys = (LinkedList) queryArgument.get("key");
        CallableStatement cs = null;
        String pkgstr="{ call "+ query + "}" ; 
        try {
            cs = conn.prepareCall(pkgstr);
            int parameterSize = keys.size();
            for (int inx = 0; inx < parameterSize; inx++) {
                String key = keys.get(inx).toString();
                Object parameterValue = parameter.getString(key);
                setValue(cs, inx + 1, parameterValue); // params
          }
            cs.registerOutParameter(parameterSize+1, Types.VARCHAR);
            cs.registerOutParameter(parameterSize+2, Types.NUMERIC);
            cs.registerOutParameter(parameterSize+3, Types.VARCHAR);
            cs.execute ();
            bb=cs.getString(4);
            aa= Integer.toString(cs.getInt(5));
            cc=cs.getString(6);
            dd[0]=bb;
            dd[1]=aa;
            dd[2]=cc;
          return dd;
        } catch (SQLException se) {
            NLog.report.println("NSF_DAO_006 : " + se);
            throw new NException("NSF_DAO_006", se.getMessage(), se);
        } catch (Exception e) {
            NLog.report.println("NSF_DAO_006 : " + e);
            throw new NException("NSF_DAO_006", e.getMessage(), e);
        } 
        finally {
            if (cs != null) {
                try {
                    cs.close();
                    cs = null;
                } catch (Exception e) {
                    NLog.report.println("Exception Occured while PreparedStatemnt close : " + e.getMessage());
                    throw new NException(e);
                }
            }
        }
    }
    protected String[] execute3(Connection conn) throws NException {
        String query = queryMaker.getQuery();
        String aa;
        String bb;
        String cc;
        String[] dd = new String[3];
        NData queryArgument = queryMaker.getQueryArgument();
        LinkedList keys = (LinkedList) queryArgument.get("key");
        CallableStatement cs = null;
        String pkgstr="{ call "+ query + "}" ; 
        try {
            cs = conn.prepareCall(pkgstr);
            int parameterSize = keys.size();
            for (int inx = 0; inx < parameterSize; inx++) {
                String key = keys.get(inx).toString();
                Object parameterValue = parameter.getString(key);
                setValue(cs, inx + 1, parameterValue); // params
          }
            cs.registerOutParameter(parameterSize+1, Types.VARCHAR);
            cs.registerOutParameter(parameterSize+2, Types.NUMERIC);
            cs.registerOutParameter(parameterSize+3, Types.VARCHAR);
            cs.execute ();
            bb=cs.getString(parameterSize+1);
            aa= Integer.toString(cs.getInt(parameterSize+2));
            cc=cs.getString(parameterSize+3);
            dd[0]=bb;
            dd[1]=aa;
            dd[2]=cc;
          return dd;
        } catch (SQLException se) {
            NLog.report.println("NSF_DAO_006 : " + se);
            throw new NException("NSF_DAO_006", se.getMessage(), se);
        } catch (Exception e) {
            NLog.report.println("NSF_DAO_006 : " + e);
            throw new NException("NSF_DAO_006", e.getMessage(), e);
        } 
        finally {
            if (cs != null) {
                try {
                    cs.close();
                    cs = null;
                } catch (Exception e) {
                    NLog.report.println("Exception Occured while PreparedStatemnt close : " + e.getMessage());
                    throw new NException(e);
                }
            }
        }
    }
    protected String[] execute4(Connection conn) throws NException {
        String query = queryMaker.getQuery();
        String aa;
        String bb;
        String cc;String ee;
        String[] dd = new String[4];
        NData queryArgument = queryMaker.getQueryArgument();
        LinkedList keys = (LinkedList) queryArgument.get("key");
        CallableStatement cs = null;
        String pkgstr="{ call "+ query + "}" ; 
        try {
            cs = conn.prepareCall(pkgstr);
            int parameterSize = keys.size();
            for (int inx = 0; inx < parameterSize; inx++) {
                String key = keys.get(inx).toString();
                Object parameterValue = parameter.getString(key);
                setValue(cs, inx + 1, parameterValue); // params
          }
            cs.registerOutParameter(parameterSize+1, Types.VARCHAR);
            cs.registerOutParameter(parameterSize+2, Types.NUMERIC);
            cs.registerOutParameter(parameterSize+3, Types.VARCHAR);
            cs.registerOutParameter(parameterSize+4, Types.VARCHAR);
            cs.execute ();
            bb=cs.getString(5);
            aa= Integer.toString(cs.getInt(6));
            cc=cs.getString(7);
            ee=cs.getString(8);
            dd[0]=bb;
            dd[1]=aa;
            dd[2]=cc;
            dd[3]=ee;
          return dd;
        } catch (SQLException se) {
            NLog.report.println("NSF_DAO_006 : " + se);
            throw new NException("NSF_DAO_006", se.getMessage(), se);
        } catch (Exception e) {
            NLog.report.println("NSF_DAO_006 : " + e);
            throw new NException("NSF_DAO_006", e.getMessage(), e);
        } 
        finally {
            if (cs != null) {
                try {
                    cs.close();
                    cs = null;
                } catch (Exception e) {
                    NLog.report.println("Exception Occured while PreparedStatemnt close : " + e.getMessage());
                    throw new NException(e);
                }
            }
        }
    }
    protected NMultiData pkgexecute(Connection conn,String query,LinkedList keys,NData parameter1) throws NException {
        ResultSet rs = null;
        CallableStatement cs = null;
        String pkgstr="{ call "+ query + "}" ; 
        try {
            cs = conn.prepareCall(pkgstr);
            int parameterSize = keys.size();
            for (int inx = 0; inx < parameterSize; inx++) {
                String key = keys.get(inx).toString();
                Object parameterValue = parameter1.getString(key);
                setValue(cs, inx + 1, parameterValue); // params
          }
            cs.registerOutParameter (parameterSize+1, OracleTypes.CURSOR);
            cs.execute ();
            
           rs = (ResultSet)cs.getObject (parameterSize+1);
       
           return NResultSetConverter.toMultiData(rs);
        } catch (SQLException se) {
            NLog.report.println("NSF_DAO_006 : " + se);
            throw new NException("NSF_DAO_006", se.getMessage(), se);
        } catch (Exception e) {
            NLog.report.println("NSF_DAO_006 : " + e);
            throw new NException("NSF_DAO_006", e.getMessage(), e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                    rs = null;
                } catch (Exception e) {
                    NLog.report.println("Exception Occured while ResultSet close : " + e.getMessage());
                    throw new NException(e);
                }
            }
            if (cs != null) {
                try {
                    cs.close();
                    cs = null;
                } catch (Exception e) {
                    NLog.report.println("Exception Occured while PreparedStatemnt close : " + e.getMessage());
                    throw new NException(e);
                }
            }
        }
    }
	/**
	 * 이 메소드는 executeQuery계렬 메소드의 내부에서 사용하는 내부메소드이다. <BR>
	 * 이곳에서 실제로 Data Access와 관련된 일을 한다.
	 * 
	 * @param conn
	 * DB Connection
	 * @return NMultiData SELECT문을 수행 후 얻어진 결과를 NMultiData형태로 Return한다.
	 */
	protected NMultiData executeQuery(Connection conn) throws NException {
		String query = queryMaker.getQuery();
		NData queryArgument = queryMaker.getQueryArgument();
		LinkedList keys = (LinkedList) queryArgument.get("key");

		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(query);
			int parameterSize = keys.size();
			for (int inx = 0; inx < parameterSize; inx++) {
				String key = keys.get(inx).toString();
				Object parameterValue = parameter.getString(key);
				setValue(ps, inx + 1, parameterValue); // params
			}
			rs = ps.executeQuery();
			return NResultSetConverter.toMultiData(rs);
		} catch (SQLException se) {
			NLog.report.println("NSF_DAO_006 : " + se);
			throw new NException("NSF_DAO_006", se.getMessage(), se);
		} catch (Exception e) {
			NLog.report.println("NSF_DAO_006 : " + e);
			throw new NException("NSF_DAO_006", e.getMessage(), e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
					rs = null;
				} catch (Exception e) {
					NLog.report.println("Exception Occured while ResultSet close : " + e.getMessage());
					throw new NException(e);
				}
			}
			if (ps != null) {
				try {
					ps.close();
					ps = null;
				} catch (Exception e) {
					NLog.report.println("Exception Occured while PreparedStatemnt close : " + e.getMessage());
					throw new NException(e);
				}
			}
		}
	}

    protected NMultiData executeQuery(Connection conn,String query) throws NException {
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            return NResultSetConverter.toMultiData(rs);
        } catch (SQLException se) {
            NLog.report.println("NSF_DAO_006 : " + se);
            throw new NException("NSF_DAO_006", se.getMessage(), se);
        } catch (Exception e) {
            NLog.report.println("NSF_DAO_006 : " + e);
            throw new NException("NSF_DAO_006", e.getMessage(), e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                    rs = null;
                } catch (Exception e) {
                    NLog.report.println("Exception Occured while ResultSet close : " + e.getMessage());
                    throw new NException(e);
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                    ps = null;
                } catch (Exception e) {
                    NLog.report.println("Exception Occured while PreparedStatemnt close : " + e.getMessage());
                    throw new NException(e);
                }
            }
        }
    }
	/**
	 * 이 메소드는 executeQueryForSingle계렬 메소드의 내부에서 사용하는 내부메소드이다. <BR>
	 * 이곳에서 실제로 Data Access와 관련된 일을 한다.
	 * 
	 * @param conn
	 *            DB Connection
	 * @return NData SELECT문을 수행 후 얻어진 결과를 NData형태로 Return한다.
	 */
	protected NData executeQueryForSingle(Connection conn) throws NException {
		String query = queryMaker.getQuery();
		NData queryArgument = queryMaker.getQueryArgument();
		LinkedList keys = (LinkedList) queryArgument.get("key");
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(query);

			int parameterSize = keys.size();
			for (int inx = 0; inx < parameterSize; inx++) {
				String key = keys.get(inx).toString();
				Object parameterValue = parameter.getString(key);
				setValue(ps, inx + 1, parameterValue); // params
			}
			rs = ps.executeQuery();

			return NResultSetConverter.toData(rs);
		} catch (SQLException se) {
			NLog.report.println("NSF_DAO_006 : " + se);
			throw new NException("NSF_DAO_006", se.getMessage(), se);
		} catch (Exception e) {
			NLog.report.println("NSF_DAO_006 : " + e);
			throw new NException("NSF_DAO_006", e.getMessage(), e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
					rs = null;
				} catch (Exception e) {
					NLog.report.println("Exception Occured while ResultSet close : " + e.getMessage());
					throw new NException(e);
				}
			}
			if (ps != null) {
				try {
					ps.close();
					ps = null;
				} catch (Exception e) {
					NLog.report.println("Exception Occured while PreparedStatemnt close : " + e.getMessage());
					throw new NException(e);
				}
			}
            
            
		}
	}

    /**
     * 이 메소드는 Blob의 SELECT SQL문에만 사용할 수 있다. <BR>
     * 이 메소드는 구축자에서 Query와 관련된 값을 설정하했거나, <BR>
     * setQueryString메소드를 사용해서 Query를 설정했을 경우에 <BR>
     * 설정되어진 값을 리용해서 executeQueryForBLob(conn, binaryFile)를 수행한다. <BR>
     */
    public File executeQueryBlob(String filepath) throws NException {
            
        Connection conn = null;
        try {
            conn = getConnection(this.dbSpec);
            return executeQueryBlob(conn,filepath);
        } catch (NException le) {
            throw le;
        } catch (Exception e) {
            throw new NException(e);
        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (Exception e) {
                NLog.report.println("Exception Occured while connection close : " + e.getMessage());
                throw new NException(e);
            }
        }
        
    }
    /**
     * 이 메소드는 executeQuery계렬 메소드의 내부에서 사용하는 내부메소드이다. <BR>
     * 이곳에서 실제로 Data Access와 관련된 일을 한다.
     * sql문에서 Blob화일마당을 항상 처음에 ,화일이름마당을 두번째에 놓을것,화일경로는 /tempPath/ 아래에 생긴다
     * @param conn
     * DB Connection
     * @return NMultiData SELECT문을 수행 후 얻어진 결과를 NMultiData형태로 Return한다.
     */
    protected File executeQueryBlob(Connection conn,String filepath) throws NException {
        String query = queryMaker.getQuery();
        NData queryArgument = queryMaker.getQueryArgument();
        LinkedList keys = (LinkedList) queryArgument.get("key");

        BLOB photo = null;
        FileOutputStream out = null;
        InputStream fin = null;
        
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(query);
            int parameterSize = keys.size();
            for (int inx = 0; inx < parameterSize; inx++) {
                String key = keys.get(inx).toString();
                Object parameterValue = parameter.getString(key);
                setValue(ps, inx + 1, parameterValue); // params
            }
            rs = ps.executeQuery();
            rs.next( );
            photo=(BLOB)rs.getBlob(1);
            filepath = filepath +rs.getString(2);
            
            File file = new File(filepath);
            out = new FileOutputStream(file);
            fin = photo.getBinaryStream();
            byte[] buffer = new byte[photo.getBufferSize( )];
            int length = 0;
            while ((length = fin.read(buffer)) != -1) {
            out.write(buffer, 0, length);
            }
            out.close( );
            fin.close( );
            return file;
        } catch (SQLException se) {
            NLog.report.println("NSF_DAO_006 : " + se);
            throw new NException("NSF_DAO_006", se.getMessage(), se);
        } catch (Exception e) {
            NLog.report.println("NSF_DAO_006 : " + e);
            throw new NException("NSF_DAO_006", e.getMessage(), e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                    rs = null;
                } catch (Exception e) {
                    NLog.report.println("Exception Occured while ResultSet close : " + e.getMessage());
                    throw new NException(e);
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                    ps = null;
                } catch (Exception e) {
                    NLog.report.println("Exception Occured while PreparedStatemnt close : " + e.getMessage());
                    throw new NException(e);
                }
            }
        }
    }
    /**
     * 이 메소드는 Clob의 SELECT SQL문에만 사용할 수 있다. <BR>
     * 이 메소드는 구축자에서 Query와 관련된 값을 설정하했거나, <BR>
     * setQueryString메소드를 사용해서 Query를 설정했을 경우에 <BR>
     * 설정되어진 값을 리용해서 executeQueryForBLob(conn, binaryFile)를 수행한다. <BR>
     */
    public File executeQueryClob(String filepath) throws NException {
            
        Connection conn = null;
        try {
            conn = getConnection(this.dbSpec);
            return executeQueryClob(conn,filepath);
        } catch (NException le) {
            throw le;
        } catch (Exception e) {
            throw new NException(e);
        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (Exception e) {
                NLog.report.println("Exception Occured while connection close : " + e.getMessage());
                throw new NException(e);
            }
        }
        
    }
    /**
     * 이 메소드는 executeQuery계렬 메소드의 내부에서 사용하는 내부메소드이다. <BR>
     * 이곳에서 실제로 Data Access와 관련된 일을 한다.
     * sql문에서 Blob화일마당을 항상 처움에 ,화일이름마당을 두번째에 놓을것
     * @param conn
     * DB Connection
     * @return NMultiData SELECT문을 수행 후 얻어진 결과를 NMultiData형태로 Return한다.
     */
    protected File executeQueryClob(Connection conn,String filepath) throws NException {
        String query = queryMaker.getQuery();
        NData queryArgument = queryMaker.getQueryArgument();
        LinkedList keys = (LinkedList) queryArgument.get("key");

        CLOB photo = null;
        Writer out = null;
        Reader fin = null;
        
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(query);
            int parameterSize = keys.size();
            for (int inx = 0; inx < parameterSize; inx++) {
                String key = keys.get(inx).toString();
                Object parameterValue = parameter.getString(key);
                setValue(ps, inx + 1, parameterValue); // params
            }
            rs = ps.executeQuery();
            rs.next( );
            photo=(CLOB)rs.getClob(1);
            filepath = filepath +rs.getString(2);
            
            File file = new File(filepath);
            out = new FileWriter(file);
            fin = photo.getCharacterStream();
            char[] buffer = new char[photo.getBufferSize( )];
            int length = 0;
            while ((length = fin.read(buffer)) != -1) {
            out.write(buffer, 0, length);
            }
            out.close( );
            fin.close( );
            return file;
        } catch (SQLException se) {
            NLog.report.println("NSF_DAO_006 : " + se);
            throw new NException("NSF_DAO_006", se.getMessage(), se);
        } catch (Exception e) {
            NLog.report.println("NSF_DAO_006 : " + e);
            throw new NException("NSF_DAO_006", e.getMessage(), e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                    rs = null;
                } catch (Exception e) {
                    NLog.report.println("Exception Occured while ResultSet close : " + e.getMessage());
                    throw new NException(e);
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                    ps = null;
                } catch (Exception e) {
                    NLog.report.println("Exception Occured while PreparedStatemnt close : " + e.getMessage());
                    throw new NException(e);
                }
            }
        }
    }
    /**
     * 이 메소드는 Clob의 SELECT SQL문에만 사용할 수 있다. <BR>
     * 이 메소드는 구축자에서 Query와 관련된 값을 설정하했거나, <BR>
     * setQueryString메소드를 사용해서 Query를 설정했을 경우에 <BR>
     * 설정되어진 값을 리용해서 executeQueryForBLob(conn, binaryFile)를 수행한다. <BR>
     */
    public String executeQueryStringCLob() throws NException {
        
        Connection conn = null;
        try {
            conn = getConnection(this.dbSpec);
            return executeQueryStringClob(conn);
        } catch (NException le) {
            throw le;
        } catch (Exception e) {
            throw new NException(e);
        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (Exception e) {
                NLog.report.println("Exception Occured while connection close : " + e.getMessage());
                throw new NException(e);
            }
        }
        
    }

/**
 * 이 메소드는 executeQuery계렬 메소드의 내부에서 사용하는 내부메소드이다. <BR>
 * 이곳에서 실제로 Data Access와 관련된 일을 한다.
 * sql문에서 Blob화일마당을 항상 처움에 ,화일이름마당을 두번째에 놓을것
 * @param conn
 * DB Connection
 * @return NMultiData SELECT문을 수행 후 얻어진 결과를 NMultiData형태로 Return한다.
 */
protected String executeQueryStringClob(Connection conn) throws NException {
    String query = queryMaker.getQuery();
    NData queryArgument = queryMaker.getQueryArgument();
    LinkedList keys = (LinkedList) queryArgument.get("key");

    CLOB photo = null;
    String cont = "";
    String out = new String();
    Reader in =null;
    
    ResultSet rs = null;
    PreparedStatement ps = null;
    try {
        ps = conn.prepareStatement(query);
        int parameterSize = keys.size();
        for (int inx = 0; inx < parameterSize; inx++) {
            String key = keys.get(inx).toString();
            Object parameterValue = parameter.getString(key);
            setValue(ps, inx + 1, parameterValue); // params
        }
        rs = ps.executeQuery();
        rs.next( );
        if(rs.getRow()!=0){
        photo=(CLOB)rs.getClob(1);
        in = photo.getCharacterStream();
        int length = (int)photo.length();
        char[] buffer = new char[1024];
        while ((length = in.read(buffer)) != -1) {
            cont = cont + out.valueOf(buffer, 0, length);        
            }
        in.close( );
        in = null;        
        }
        return cont;
        
    } catch (SQLException se) {
        NLog.report.println("NSF_DAO_006 : " + se);
        throw new NException("NSF_DAO_006", se.getMessage(), se);
    } catch (Exception e) {
        NLog.report.println("NSF_DAO_006 : " + e);
        throw new NException("NSF_DAO_006", e.getMessage(), e);
    } finally {
        if (rs != null) {
            try {
                rs.close();
                rs = null;
            } catch (Exception e) {
                NLog.report.println("Exception Occured while ResultSet close : " + e.getMessage());
                throw new NException(e);
            }
        }
        if (ps != null) {
            try {
                ps.close();
                ps = null;
            } catch (Exception e) {
                NLog.report.println("Exception Occured while PreparedStatemnt close : " + e.getMessage());
                throw new NException(e);
            }
        }
    }
}
    /**
     * 이 메소드는 Blob의 SELECT SQL문에만 사용할 수 있다. <BR>
     * 이 메소드는 구축자에서 Query와 관련된 값을 설정하했거나, <BR>
     * setQueryString메소드를 사용해서 Query를 설정했을 경우에 <BR>
     * 설정되어진 값을 리용해서 executeQueryForBLob(conn, binaryFile)를 수행한다. <BR>
     */
    public int executeInsertForBlob(File binaryFile) throws NException {
        
        Connection conn = null;
        int result = 0;
        try {
            conn = getConnection(this.dbSpec);
            conn.setAutoCommit(false);
            result=executeInsertForBLob(conn, binaryFile);
            conn.commit();
            return result;
        } catch (NException le) {
            throw le;
        } catch (Exception e) {
            throw new NException(e);
        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (Exception e) {
                NLog.report.println("Exception Occured while connection close : " + e.getMessage());
                throw new NException(e);
            }
        }
    }
    
    /**
     * 이 메소드는 executeQueryForBLob계렬 메소드의 내부에서 사용하는 내부메소드이다. <BR>
     * Blob,Clob처리를 한다
     * 
     * @param conn
     *            DB Connection
     * @return NData SELECT문을 수행 후 얻어진 결과를 NData형태로 Return한다.
     */
    protected int executeInsertForBLob(Connection conn,File binaryFile) throws NException {
        
        FileInputStream fin = null;
        BLOB blobdata = null; 
        OutputStream out=null;
        String query = queryMaker.getQuery();
        NData queryArgument = queryMaker.getQueryArgument();
        LinkedList keys = (LinkedList) queryArgument.get("key");
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(query);

            int parameterSize = keys.size();
            for (int inx = 0; inx < parameterSize; inx++) {
                String key = keys.get(inx).toString();
                Object parameterValue = parameter.getString(key);
                setValue(pstmt, inx + 1, parameterValue); // params
            }
            rs = pstmt.executeQuery();
            rs.next( );
            blobdata = ((OracleResultSet)rs).getBLOB(1);
            out = blobdata.getBinaryOutputStream( );
            
            fin =  new FileInputStream(binaryFile);
            byte[] buffer = new byte[blobdata.getBufferSize( )];
            int length = 0;
            while ((length = fin.read(buffer)) != -1) {
            out.write(buffer, 0, length);
            }
            out.close( );
            out = null;
            fin.close( );
            fin = null;
            return 1;
        } catch (SQLException se) {
            NLog.report.println("NSF_DAO_006 : " + se);
            throw new NException("NSF_DAO_006", se.getMessage(), se);
        } catch (Exception e) {
            NLog.report.println("NSF_DAO_006 : " + e);
            throw new NException("NSF_DAO_006", e.getMessage(), e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                    rs = null;
                } catch (Exception e) {
                    NLog.report.println("Exception Occured while ResultSet close : " + e.getMessage());
                    throw new NException(e);
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                    pstmt = null;
                } catch (Exception e) {
                    NLog.report.println("Exception Occured while PreparedStatemnt close : " + e.getMessage());
                    throw new NException(e);
                }
            }
            
            
        }
    }
    
    /**
     * 이 메소드는 Blob의 insert SQL문에만 사용할 수 있다. <BR>
     * 이때 clob로 입력하려는 <String 문자렬>을 입력파라메터로 주어야 한다
     * executeInsertForClob(conn, binaryFile)를 수행한다. <BR>
     */
    public int executeInsertStringClob(String content) throws NException {
        
        Connection conn = null;
        int result = 0;
        try {
            conn = getConnection(this.dbSpec);
            conn.setAutoCommit(false);
            result=executeInsertStringCLob(conn, content);
            conn.commit();
            return result;
        } catch (NException le) {
            throw le;
        } catch (Exception e) {
            throw new NException(e);
        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (Exception e) {
                NLog.report.println("Exception Occured while connection close : " + e.getMessage());
                throw new NException(e);
            }
        }
    }
    /**
     * 이 메소드는 executeQueryForCLob계렬 메소드의 내부에서 사용하는 내부메소드이다. <BR>
     * Blob,Clob처리를 한다
     * 
     * @param conn
     *            DB Connection
     * @return NData SELECT문을 수행 후 얻어진 결과를 NData형태로 Return한다.
     */
    protected int executeInsertStringCLob(Connection conn,String content) throws NException {
        
        CLOB clobdata = null; 
        Writer out = null;
        String query = queryMaker.getQuery();
        NData queryArgument = queryMaker.getQueryArgument();
        LinkedList keys = (LinkedList) queryArgument.get("key");
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(query);

            int parameterSize = keys.size();
            for (int inx = 0; inx < parameterSize; inx++) {
                String key = keys.get(inx).toString();
                Object parameterValue = parameter.getString(key);
                setValue(pstmt, inx + 1, parameterValue); // params
            }
            rs = pstmt.executeQuery();
            rs.next( );
            clobdata = ((OracleResultSet)rs).getCLOB(1);
            out = clobdata.getCharacterOutputStream( );
            out.write(content);
            
            out.close( );
            out = null;
           
            return 1;
        } catch (SQLException se) {
            NLog.report.println("NSF_DAO_006 : " + se);
            throw new NException("NSF_DAO_006", se.getMessage(), se);
        } catch (Exception e) {
            NLog.report.println("NSF_DAO_006 : " + e);
            throw new NException("NSF_DAO_006", e.getMessage(), e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                    rs = null;
                } catch (Exception e) {
                    NLog.report.println("Exception Occured while ResultSet close : " + e.getMessage());
                    throw new NException(e);
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                    pstmt = null;
                } catch (Exception e) {
                    NLog.report.println("Exception Occured while PreparedStatemnt close : " + e.getMessage());
                    throw new NException(e);
                }
            }
            
            
        }
    }
    /**
     * 이 메소드는 clob의 insert SQL문에만 사용할 수 있다. <BR>
     * 이때 clob로 입력하려는 <File 화일>을 입력파라메터로 주어야 한다
     * executeInsertForClob(conn, binaryFile)를 수행한다. <BR>
     */
    public int executeInsertForClob(File binaryFile) throws NException {
        
        Connection conn = null;
        int result = 0;
        try {
            conn = getConnection(this.dbSpec);
            conn.setAutoCommit(false);
            result=executeInsertForCLob(conn, binaryFile);
            conn.commit();
            return result;
        } catch (NException le) {
            throw le;
        } catch (Exception e) {
            throw new NException(e);
        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (Exception e) {
                NLog.report.println("Exception Occured while connection close : " + e.getMessage());
                throw new NException(e);
            }
        }
    }
    /**
     * 이 메소드는 executeQueryForCLob계렬 메소드의 내부에서 사용하는 내부메소드이다. <BR>
     * Blob,Clob처리를 한다
     * 
     * @param conn
     *            DB Connection
     * @return NData SELECT문을 수행 후 얻어진 결과를 NData형태로 Return한다.
     */
    protected int executeInsertForCLob(Connection conn,File binaryFile) throws NException {
        
        FileReader fin = null;
        CLOB clobdata = null; 
        Writer out = null;
        String query = queryMaker.getQuery();
        NData queryArgument = queryMaker.getQueryArgument();
        LinkedList keys = (LinkedList) queryArgument.get("key");
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(query);

            int parameterSize = keys.size();
            for (int inx = 0; inx < parameterSize; inx++) {
                String key = keys.get(inx).toString();
                Object parameterValue = parameter.getString(key);
                setValue(pstmt, inx + 1, parameterValue); // params
            }
            rs = pstmt.executeQuery();
            rs.next( );
            clobdata = ((OracleResultSet)rs).getCLOB(1);
            out = clobdata.getCharacterOutputStream( );
            
            fin = new FileReader(binaryFile);
            char[] buffer = new char[clobdata.getBufferSize( )];
            int length = 0;
            while ((length = fin.read(buffer)) != -1) {
            out.write(buffer, 0, length);
            }
            out.close( );
            out = null;
            fin.close( );
            fin = null;
            return 1;
        } catch (SQLException se) {
            NLog.report.println("NSF_DAO_006 : " + se);
            throw new NException("NSF_DAO_006", se.getMessage(), se);
        } catch (Exception e) {
            NLog.report.println("NSF_DAO_006 : " + e);
            throw new NException("NSF_DAO_006", e.getMessage(), e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                    rs = null;
                } catch (Exception e) {
                    NLog.report.println("Exception Occured while ResultSet close : " + e.getMessage());
                    throw new NException(e);
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                    pstmt = null;
                } catch (Exception e) {
                    NLog.report.println("Exception Occured while PreparedStatemnt close : " + e.getMessage());
                    throw new NException(e);
                }
            }
            
            
        }
    }
    
    /**
	 * 이 메소드는 executeUpdate계렬 메소드의 내부에서 사용하는 내부메소드이다. <BR>
	 * 이곳에서 실제로 Data Access와 관련된 일을 한다.
	 * 
	 * @param conn
	 *            DB Connection
	 * @return int executeUpdate를 수행 후 얻어진 결과를 int형태로 Return한다.
	 */
	protected int executeUpdate(Connection conn) throws NException {
		String query = queryMaker.getQuery();
		NData queryArgument = queryMaker.getQueryArgument();
		LinkedList keys = (LinkedList) queryArgument.get("key");

		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(query);
			int parameterSize = keys.size();
			for (int inx = 0; inx < parameterSize; inx++) {
				String key = keys.get(inx).toString();
				Object parameterValue =parameter.getString(key);
				setValue(ps, inx + 1, parameterValue); // params
			}
			return ps.executeUpdate();
		} catch (SQLException se) {
			NLog.report.println("NSF_DAO_006 : " + se);
			throw new NException("NSF_DAO_006", se.getMessage(), se);
		} catch (Exception e) {
			NLog.report.println("NSF_DAO_006 : " + e);
			throw new NException("NSF_DAO_006", e.getMessage(), e);
		} finally {
			if (ps != null) {
				try {
					ps.close();
					ps = null;
				} catch (Exception e) {
					NLog.report.println("Exception Occured while PreparedStatemnt close : " + e.getMessage());
					throw new NException(e);
				}
			}
		}
	}
    
    protected int executeUpdate(Connection conn,String query) throws NException {
        

        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(query);
            return ps.executeUpdate();
        } catch (SQLException se) {
            NLog.report.println("NSF_DAO_006 : " + se);
            throw new NException("NSF_DAO_006", se.getMessage(), se);
        } catch (Exception e) {
            NLog.report.println("NSF_DAO_006 : " + e);
            throw new NException("NSF_DAO_006", e.getMessage(), e);
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                    ps = null;
                } catch (Exception e) {
                    NLog.report.println("Exception Occured while PreparedStatemnt close : " + e.getMessage());
                    throw new NException(e);
                }
            }
        }
    }
    
	/**
	 * parameterData를 반환한다.
	 * 
	 * @return NData
	 */
	protected NData getParameter() {
		return parameter;
	}

	/**
	 * NQueryMaker를 반환한다.
	 * 
	 * @return NQueryMaker
	 */
	protected NQueryMaker getQueryMaker() {
		return queryMaker;
	}

	/**
	 * Setting된 QueryString을 반환한다.
	 * 
	 * @return String
	 */
	protected String getQueryString() {
		return queryString;
	}

	/**
	 * NQueryMaker자료를 설정한다.
	 * 
	 * @param queryMaker
	 * @return void
	 */
	protected void setQueryMaker(NQueryMaker queryMaker) {
		this.queryMaker = queryMaker;
	}
}

