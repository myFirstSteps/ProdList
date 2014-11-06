/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.pankratov.prodlist.model.dao.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.ServletContext;

/**
 *
 * @author pankratov
 */
//Интерфейс маркер
public abstract class JDBCDAOObject implements AutoCloseable {
    abstract protected JDBCDAOObject newInstance()throws Exception; 
     private long offerTime = System.currentTimeMillis();  
     private boolean pensioner = false;
     private Connection connection;
    /**
     * @return the offerTime
     */
    protected  JDBCDAOObject(ServletContext context, String DAO_NAME) throws JDBCDAOException {
        try{
      String DB_NAME = context.getInitParameter(String.format("JDBCDAO_%s_URL",DAO_NAME));
      String DB_LOGIN =  context.getInitParameter(String.format("JDBCDAO_%s_LOGIN",DAO_NAME));
      String DB_PASSWORD = context.getInitParameter(String.format("JDBCDAO_%s_PASSWORD",DAO_NAME));
        connection = DriverManager.getConnection(DB_NAME, DB_LOGIN, DB_PASSWORD);
        connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);}
        catch (Exception e){ try{connection.close();}finally{ throw new JDBCDAOException (e);}}
        
    } 
    protected ConcurrentHashMap<String, List<String>> initTables()throws SQLException{
         ConcurrentHashMap<String, List<String>> m = new ConcurrentHashMap<>();
        try (ResultSet colMetaData = connection.getMetaData().getColumns(null, null, null, null);) {
                String lastTableName = "", columnName = "", currentTableName = "";
               
                while (colMetaData.next()) {
                    currentTableName = colMetaData.getString(3);
                    columnName = colMetaData.getString(4);
                    if (!lastTableName.equals(currentTableName)) {
                        m.putIfAbsent(currentTableName, new ArrayList<String>());
                        lastTableName = currentTableName;
                    }
                    m.get(currentTableName).add(columnName);
                }
        }
        return m;
    }
    public long getOfferTime() {
        return offerTime;
    }
    
    abstract ServletContext getContext();

    /**
     * @param offerTime the offerTime to set
     */
    public void setOfferTime(long offerTime) {
        this.offerTime = offerTime;
    }
    public Connection getConnection(){
        return connection;
    }

    /**
     * @return the pensioner
     */
    public boolean isPensioner() {
        return pensioner;
    }

    /**
     * @param pensioner the pensioner to set
     */
    public void setPensioner(boolean pensioner) {
        this.pensioner = pensioner;
    }
    abstract public String getDAOName();
}
