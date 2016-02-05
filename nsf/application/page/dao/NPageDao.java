package nsf.application.page.dao;

import java.lang.reflect.Constructor;
import java.util.LinkedList;

import nsf.foundation.persistent.db.query.NQueryMaker;
import nsf.application.page.dao.NAbstractPageDao;
import nsf.core.config.NConfiguration;
import nsf.core.exception.NException;
import nsf.support.collection.NData;
import nsf.support.collection.NDataProtocol;
import nsf.support.collection.NMultiData;

/**
 * <pre>
 * 자료를 조회하여 페지로 보여주려고 할때 리용하는 dao이다.
 * </pre>
 * 
 * @since 2007/1/5
 * @version NSF 1.0
 * @author 김철남 Nova China
 *
 * 2007.10.03 리경범 createDao메소드 수정
 */

public class NPageDao
{

    private String queryString;
    private NData parameter;
    private NQueryMaker queryMaker;
    private String dbspec;
    private int rowSize;
    private int pageSize;
    
    public NPageDao(String newQueryString, NData parameterData, int rowSize, int pageSize) throws NException {
        
        this(newQueryString, parameterData, rowSize, pageSize, "default");
    }
   public NPageDao(String newQueryString, NData parameterData, int pageSize) throws NException {
        
        this(newQueryString, parameterData,pageSize, "default");
    }
    public NPageDao(String newQueryString, NData parameterData, int rowSize, int pageSize, String dbspec) throws NException {
        
        queryMaker = new NQueryMaker();
        queryString = newQueryString;
        parameter = parameterData;
        this.rowSize = rowSize;
        this.pageSize = pageSize;
        this.dbspec = dbspec;
        queryMaker.resolveSQL(queryString, parameterData);
    }
    public NPageDao(NData parameterData,int rowSize, int pageSize, String dbspec) throws NException {
        parameter = parameterData;
        this.rowSize = rowSize;
        this.pageSize = pageSize;
        this.dbspec = dbspec;
    }
    /**
     * 리호남
     */
    public NPageDao(String newQueryString,NData parameterData,int pageSize,String dbspec) throws NException {
        queryMaker = new NQueryMaker();
        queryString = newQueryString;
        parameter = parameterData;
        this.pageSize = pageSize;
        this.dbspec =dbspec;
        if(!parameter.containsKey("rowSize") || parameter.getString("rowSize").equals("") || parameter.getInt("rowSize")==0)
          this.rowSize=10;
        else
          this.rowSize=parameter.getInt("rowSize");
        queryMaker.resolveSQL(queryString, parameterData);
    }
    /**
     * sql을 실행한다.
     * @return
     * @throws NException
     */
    public NMultiData executeQuery() throws NException {
        
        String query = queryMaker.getQuery();
        NData queryArgument = queryMaker.getQueryArgument();
        LinkedList keys = (LinkedList)queryArgument.get("key");
        int parameterSize = keys.size();
        for(int inx = 0; inx < parameterSize; inx++)
        {
            String key = keys.get(inx).toString();
            Object parameterValue = parameter.getString(key);
            query = query.replaceFirst("\\?", "'" + parameterValue.toString() + "'");
        }

        String queryStr = query;
        NAbstractPageDao dao = createDao();
        
        try {
            NMultiData resultMultiData = dao.execute(queryStr);
            return resultMultiData;
        } catch(NException le) {
            throw le;
        } catch(Exception e) {
            throw new NException(e);
        }
    }
    public NMultiData executeQuery(String query) throws NException {
        String queryStr = query;
        NAbstractPageDao dao = createDao();
        
        try {
            NMultiData resultMultiData = dao.execute(queryStr);
            return resultMultiData;
        } catch(NException le) {
            throw le;
        } catch(Exception e) {
            throw new NException(e);
        }
    }

    protected NData getParameter()
    {
        return parameter;
    }

    protected NQueryMaker getQueryMaker()
    {
        return queryMaker;
    }

    protected String getQueryString()
    {
        return queryString;
    }

    protected void setQueryMaker(NQueryMaker queryMaker)
    {
        this.queryMaker = queryMaker;
    }
    
    /**
     * dao를 생성한다.
     * @return
     * @throws NException
     * 
     * 2007.10.03  리경범  paramObjectVal 설정부분 에서 "default"  --> dbspec으로 수정
     */
    public NAbstractPageDao createDao() throws NException {

        NAbstractPageDao pageDao = null;
        String pageDaoClassName = null;
        
        try {                                        
            NConfiguration conf = NConfiguration.getInstance();                                                  
            String jdbcDriverName = conf.getString("/configuration/nsf/jdbc-datasource/spec<" + this.dbspec + ">/driver", "").trim();
            
            if ( jdbcDriverName.equals("com.mysql.jdbc.Driver") ) {
                pageDaoClassName = "nsf.application.page.dao.NMySqlPageDao";
            } else if ( jdbcDriverName.equals("com.microsoft.sqlserver.jdbc.SQLServerDriver") ) {
                pageDaoClassName = "nsf.application.page.dao.NMSSQLPageDao";
            } else {
                pageDaoClassName = "nsf.application.page.dao.NOraclePageDao";
            }
        
            Class[] paramClassType = new Class[] { String.class, int.class, int.class, NDataProtocol.class };
            
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Class pageDaoClass = classLoader.loadClass(pageDaoClassName);
            Constructor pageDaoClassConstructor = pageDaoClass.getConstructor(paramClassType);
            Object[] paramObjectVal = new Object[] { dbspec, new Integer(this.rowSize), new Integer(this.pageSize), this.parameter};
            pageDao = (NAbstractPageDao) (pageDaoClassConstructor.newInstance(paramObjectVal));

        } catch (Exception e) {
            throw new NException( 
                    "NSF_PAG_005", 
                    "Instanciation Failed with Fully-Qualified class name[" + pageDaoClassName + "]"+ e.getMessage() ,e);
        }
    
        return pageDao;
    }
}