/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pankratov.prodlist.model.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentSkipListSet;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;

/**
 *
 * @author pankratov
 */
public class Table {

    private Connection connection;
    private CachedRowSet rowset;
    private String tableName;
    private List<String> columnNames;

    protected Table(String tableName,Connection con,  List<String> colNames) throws JDBCDAOException {
        try {
            rowset = RowSetProvider.newFactory().createCachedRowSet();
            this.tableName = tableName;
            columnNames = colNames;
            connection=con;
            //  log.debug(String.format("created table %s with columns: %s rowset:%s", tableName, columnNames, rowset));
        } catch (Exception e) {
            throw new JDBCDAOException("Ошибка при создании:" + Table.class + "для: " + tableName, e);
        }

    }
    protected String getTableName(){
        return tableName;
    }

    protected boolean addRecord(String... s) throws Exception {
        int i = 1;
        String params = "";
        for (int j = 0; j < s.length; j++) {
            params += ", ?";
        }
        params = params.replaceAll("^, ", "");
        try(PreparedStatement stat = connection.prepareStatement(String.format("Insert into %s values(%s)", tableName, params));){
        for (String st : s) {
            stat.setString(i++, st);

        }

        stat.execute();
        }
        return true;
    }
    
    protected boolean addRecord(TreeMap<String,Integer> s) throws Exception {
        int i = 1;
        String params = "";
        String colsNames="";
        String colValues="";
        
        for (int j = 0; j < s.size(); j++) {
            
            params += ", ?";
        }
        params = params.replaceAll("^, ", "");
        try(PreparedStatement stat = connection.prepareStatement(String.format("Insert into %s(%s) values(%s)", tableName, params));){
        for (String st : s) {
            stat.setString(i++, st);

        }

        stat.execute();
        }
        return true;
    }
    
    protected ConcurrentSkipListSet<String> readColumn(int n)throws JDBCDAOException {
         ConcurrentSkipListSet<String> result = new ConcurrentSkipListSet<>();
        try (Statement st = connection.createStatement();) {
            ResultSet res = st.executeQuery(String.format("select %s from %s", this.columnNames.get(n-1), this.tableName));
            while (res.next()) {
                result.add(res.getString(1));
            }
            res.close();

        } catch (SQLException ex) {
           
            throw new JDBCDAOException("Ошибка чтения имен пользователя", ex);

        }
        return result;
    }
    protected CachedRowSet getRowSet(){
        return rowset;
    }
  

    protected String getColumnName(int numb) throws JDBCDAOException {
        String res = null;

        try {
            res = columnNames.get(numb - 1);
        } catch (IndexOutOfBoundsException e) {
            throw new JDBCDAOException(String.format("У таблици %s нет столбца с индексом %d", tableName, numb), e);
        }
        return res;
    }
}
