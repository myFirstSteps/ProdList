/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.pankratov.prodlist.model.dao.jdbc;

import com.pankratov.prodlist.model.dao.*;
import com.pankratov.prodlist.model.list.ProdList;
import com.pankratov.prodlist.model.products.Product;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.ServletContext;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author pankratov
 */
public class JDBCProdListDAO extends JDBCDAOObject implements ProdListDAO {
    
    private static final Logger log = org.apache.logging.log4j.LogManager.getLogger(JDBCProdListDAO.class);
    private static JDBCDAOPool<JDBCProdListDAO> pool;
    private static final String DAO_NAME = "PRODLIST";
    private static ServletContext context;
    private final Table LISTS_TABLE;
   

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
    public String getDAOName() {
        return DAO_NAME;
    }

    @Override
    public void close() throws SQLException {
       LISTS_TABLE.getRowSet().release();
     
        getConnection().setAutoCommit(true);
        pool.put(this);
    }

    @Override
    protected JDBCProdListDAO newInstance() throws JDBCDAOException {
        return new JDBCProdListDAO(context);
    }

    static public JDBCProdListDAO getInstance(javax.servlet.ServletContext context) throws Exception {
        try {
            JDBCProdListDAO instance = null;
            JDBCProdListDAO.context = context;
            if (pool == null) {
                synchronized (JDBCProdListDAO.class) {
                    if (pool == null) {
                        pool = new JDBCDAOPool<>(new JDBCProdListDAO(context));
                    }
                }
            }
            instance = pool.get();
            return instance;
        } catch (Exception e) {
            throw new JDBCDAOException("Exception when getting  JDBCProdListDAO instance", e);
        }

    }

    private JDBCProdListDAO(ServletContext context) throws JDBCDAOException {
        super(context, DAO_NAME);
        try {
        ConcurrentHashMap<String, List<String>> tablesMetaData=this.initTables();
        String ListsTableName = context.getInitParameter("LISTS_TABLE");
                LISTS_TABLE = new Table(ListsTableName, getConnection(), tablesMetaData.get(ListsTableName));
            log.debug("JDBCProdListDAO created");
        } catch (Exception e) {
            log.error("JDBCProdListDAO creation error", e);
            throw new JDBCDAOException("JDBCProdListDAO creation error: ", e);
        }
    }

    @Override
    ServletContext getContext() {
        return context;
    }
    
    private TreeMap<Integer,String> ListToTable(ProdList list){
       TreeMap<Integer,String>l=new TreeMap<>();
        if (list.getID() != -1) {
            l.put(1, list.getID().toString());
        }
        if (!list.getName().equals("")) {
            l.put(2, list.getName());
        }
        if (!list.getProducts().equals("")) {
            l.put(3, list.getProducts());
        }
        if (!list.getOwnerName().equals("")) {
            l.put(4, list.getOwnerName());
        }
       
        return l;
       
    }
    private ProdList ListFromTable(List<String> fieldValues)throws Exception {
        ProdList pl;
        String[] fields = {"id", "name", "products","ownerName","checked","timeStamp"};
        int i = 0;
        TreeMap<String, String> listInit = new TreeMap<>();    
        for (String value : fieldValues) {
             listInit.put(fields[i++], value);
        }
        pl=new ProdList( listInit);
        return pl;
    }
    
    @Override
     public List<ProdList> readProdLists(ProdList list) throws Exception{
         LinkedList<ProdList> pl=new LinkedList<>();
         for(List<String>ls: LISTS_TABLE.readRawsWhere(ListToTable(list))){
             pl.add(ListFromTable(ls));
         }
         return pl;
     }
    @Override
     public boolean addProdList(ProdList list) throws Exception{
         LISTS_TABLE.addRecord(ListToTable(list));
         return true;
     }
    @Override
     public ProdList changeProdList(ProdList list) throws Exception{
         return null;
     }
        @Override
      public ArrayList readListNames(ProdList list)throws Exception{
          return new ArrayList(LISTS_TABLE.readColumn(2,ListToTable(list)));
          
      } 
    
}
