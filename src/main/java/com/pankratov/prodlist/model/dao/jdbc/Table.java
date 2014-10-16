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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;
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

    protected Table(String tableName, Connection con, List<String> colNames) throws JDBCDAOException {
        try {
            rowset = RowSetProvider.newFactory().createCachedRowSet();
            this.tableName = tableName;
            columnNames = colNames;
            connection = con;
            //  log.debug(String.format("created table %s with columns: %s rowset:%s", tableName, columnNames, rowset));
        } catch (Exception e) {
            throw new JDBCDAOException("Ошибка при создании:" + Table.class + "для: " + tableName, e);
        }

    }

    protected String getTableName() {
        return tableName;
    }

    private String[] parseConditionMap(TreeMap<Integer, String> s) throws Exception {
        String colNames = "";
        String colValues = "";

        for (Entry<Integer, String> e : s.entrySet()) {

            colNames += ", " + getColumnName(e.getKey());
            colValues += ", '" + e.getValue() + "'";
        }
        colValues = colValues.replaceAll("^, ", "");
        colNames = colNames.replaceAll("^, ", "");
        return new String[]{colNames, colValues};
    }

    

    protected boolean addRecord(TreeMap<Integer, String> c) throws Exception {
        int i = 1;
        String[] s = parseConditionMap(c);
        String params = "";
        String colsNames = s[0];
        String colValues = s[1];

        String query = String.format("insert into %s  (%s) values (%s)", tableName, colsNames, colValues);
        try (Statement st = connection.createStatement();) {
            st.executeUpdate(query);
        }
        return true;
    }

    protected List<List<String>> readRawsWhere(TreeMap<Integer, String> condition) throws Exception {
        LinkedList<List<String>> result = new LinkedList<>();
        List<String> resultRow = new LinkedList<>();
        String[] s = parseConditionMap(condition);
        String columns = s[0];
        String values = s[1];
        String query = String.format("select * from %s where %s = %s", tableName, columns, values);
        try (Statement st = connection.createStatement(); ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                for (int i = 1; i < rs.getMetaData().getColumnCount(); i++) {
                    resultRow.add(rs.getString(i));
                }
                result.add(resultRow);
            }
        }
        return result;
    }

    protected ConcurrentSkipListSet<String> readColumn(int n) throws JDBCDAOException {
        ConcurrentSkipListSet<String> result = new ConcurrentSkipListSet<>();
        try (Statement st = connection.createStatement();) {
            ResultSet res = st.executeQuery(String.format("select %s from %s", this.columnNames.get(n - 1), this.tableName));
            while (res.next()) {
                result.add(res.getString(1));
            }
            res.close();

        } catch (SQLException ex) {

            throw new JDBCDAOException("Ошибка чтения имен пользователя", ex);

        }
        return result;
    }

    protected CachedRowSet getRowSet() {
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
