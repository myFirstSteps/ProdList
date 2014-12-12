
package com.pankratov.prodlist.model.dao.jdbc;

import com.pankratov.prodlist.model.valuation.Valuation;
import com.pankratov.prodlist.model.dao.DAOException;
import com.pankratov.prodlist.model.dao.ValuationDAO;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.ServletContext;
import org.apache.logging.log4j.*;

public class JDBCValuationDAO extends JDBCDAOObject implements ValuationDAO{
     private static final Logger log = LogManager.getLogger(JDBCValuationDAO.class);
    private static JDBCDAOPool<JDBCValuationDAO> pool;
    private static final String DAO_NAME = "VALUATION";
    private static ServletContext context;
    private final Table VAL_TABLE;
    
    
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        try {
            getConnection().close();
            log.debug(String.format("Connection of %s normally closed", this));
        } finally {
            log.debug("Ripped" + this);
        }
    }

    @Override
    protected JDBCValuationDAO newInstance() throws JDBCDAOException {
      return new JDBCValuationDAO(context);
    }
    
     private JDBCValuationDAO(ServletContext context) throws JDBCDAOException {
        super(context, DAO_NAME);
        try {
            ConcurrentHashMap<String, List<String>> tablesMetaData = this.initTables();
            String ListsTableName = context.getInitParameter("VAL_TABLE");
            VAL_TABLE = new Table(ListsTableName, getConnection(), tablesMetaData.get(ListsTableName));
            log.debug("JDBCValuationDAO created");
        } catch (Exception e) {
            log.error("JDBCValuationDAO creation error", e);
            throw new JDBCDAOException("JDBCValuationDAO creation error: ", e);
        }
    }

    @Override
    ServletContext getContext() {
       return context;
    }

    @Override
    public String getDAOName() {
       return  DAO_NAME; 
    }

    @Override
    public void close() throws Exception {
        try {
            VAL_TABLE.getRowSet().release();
            getConnection().setAutoCommit(true);
            pool.put(this);
        } catch (SQLException e) {
            log.error("close exception", e);
            throw new JDBCDAOException(e);
        }
    }

     static public JDBCValuationDAO getInstance(javax.servlet.ServletContext context) throws JDBCDAOException {
        try {
            JDBCValuationDAO instance = null;
            JDBCValuationDAO.context = context;
            if (pool == null) {
                synchronized (JDBCValuationDAO.class) {
                    if (pool == null) {
                        pool = new JDBCDAOPool<>(new JDBCValuationDAO(context));
                    }
                }
            }
            instance = pool.get();
            return instance;
        } catch (Exception e) {
            log.error(e);
            throw new JDBCDAOException(String.format("Exception when getting  JDBC%sDAO instance", DAO_NAME), e);
        }

    }
    protected TreeMap<Integer,String> ValuationToTable(Valuation val){
        TreeMap<Integer,String> result=new TreeMap<>();
         if (val.getRating()!= -1) {
            result.put(2, String.valueOf(val.getRating()));
        }
        if (!val.getReference().equals("")) {
            result.put(3, val.getReference());
        }
        if (!val.getOverview().equals("")) {
            result.put(4, val.getOverview());
        }
         if (!val.getAuthor().equals("")) {
            result.put(5, val.getAuthor());
        }
        return result;  
        
    }
    
    protected Valuation  ValuationFromTable( List<String> fieldValues){
        Valuation val=new Valuation(Long.parseLong(fieldValues.get(0)),Integer.parseInt(fieldValues.get(1)),
        fieldValues.get(2),fieldValues.get(3),fieldValues.get(4),fieldValues.get(5)); 
        return val;  
    }
    
    @Override
    public Set<Valuation> readValuations(Valuation val) throws DAOException {
        Set<Valuation> result =new TreeSet<>();
        LinkedList<List<String>> rows=new LinkedList<>();
        if(val.getAuthor().equals("admin")){
            for(String s:VAL_TABLE.readColumn(5)){
                rows.addAll(VAL_TABLE.readRawsWhere(ValuationToTable(new Valuation(s,-1, "", ""))));
            }
        } else rows.addAll(VAL_TABLE.readRawsWhere(ValuationToTable(val)));
        for(List<String>rec:rows){
            result.add(ValuationFromTable(rec));
        }
        return result;
         
    }

    @Override
    public boolean addValuation(Valuation val) throws DAOException {
        VAL_TABLE.addRecord(ValuationToTable(val));return true;
    }
    
}
