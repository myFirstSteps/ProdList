/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pankratov.prodlist.model.dao.jdbc;

import java.sql.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.*;
import java.util.regex.*;
import javax.sql.rowset.*;

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

    private String[] parseConditionMap(TreeMap<Integer, String> s) throws JDBCDAOException {
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

    protected boolean addRecord(TreeMap<Integer, String> c) throws JDBCDAOException {
        int i = 1;
        String[] s = parseConditionMap(c);
        String params = "";
        String colsNames = s[0];
        String colValues = s[1];
        String query = String.format("insert into %s  (%s) values (%s)", tableName, colsNames, colValues);
        try (Statement st = connection.createStatement();) {
            st.executeUpdate(query);
        } catch (SQLException e) {
            if( e.toString().contains("Duplicate entry"))throw new JDBCDAOException("Данный продукт уже существует.");else
            throw new JDBCDAOException(String.format("Ошибка при добавлении данных в таблицу %s", tableName), e);
        }
        return true;
    }

    protected boolean addEnumValues(int col, String... newValues) throws JDBCDAOException {
        boolean result = false;
        try (Statement st = connection.createStatement();
                ResultSet source = st.executeQuery(String.format("Describe %s %s",
                                this.getTableName(), this.getColumnName(col)));) {
            StringBuilder insertion = new StringBuilder("'"), enums = null;
            String def = "";
            while (source.next()) {
                enums = new StringBuilder(source.getString(2));
                def = source.getString(5);
            }
            for (String s : newValues) {
                if (enums != null && enums.indexOf("'" + s + "'") == -1) {
                    insertion.append(",'" + s);
                }
            }
            if (insertion.length() > 3) {
                enums.insert(enums.lastIndexOf("','"), insertion);

                st.executeUpdate(String.format("Alter table %s modify %s %s not null default '%s'", this.getTableName(), this.getColumnName(col), enums, def));
                result = true;
            }
        } catch (SQLException | JDBCDAOException ex) {

            throw new JDBCDAOException("Ошибка добавления категорий продуктов", ex);
        }
        return result;
    }

    protected LinkedList<List<String>> readRawsWhere(TreeMap<Integer, String> condition) throws JDBCDAOException {
        LinkedList<List<String>> result = new LinkedList<>();
        List<String> resultRow = new LinkedList<>();

        String param = "";
        for (Entry<Integer, String> st : condition.entrySet()) {
            if (param.length() > 0) {
                param += " and ";
            }
            param += getColumnName(st.getKey()) + "= '" + st.getValue() + "'";
        }
        String query = String.format("select * from %s where %s", tableName, param);
        try (Statement st = connection.createStatement(); ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                resultRow=new LinkedList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    resultRow.add(rs.getString(i));
                }
                result.add(resultRow);
            }
        } catch (SQLException e) {
            throw new JDBCDAOException(String.format("Ошибка при чтении данных из таблицы %s", tableName), e);
        }
        return result;
    }

    protected ConcurrentSkipListSet<String> readColumn(int n) throws JDBCDAOException {
        ConcurrentSkipListSet<String> result = new ConcurrentSkipListSet<>();
        String query = String.format("select %s from %s", this.columnNames.get(n - 1), this.tableName);
        try (Statement st = connection.createStatement(); ResultSet res = st.executeQuery(query);) {
            while (res.next()) {
                result.add(res.getString(1));
            }
        } catch (SQLException ex) {

            throw new JDBCDAOException("Ошибка чтения имен пользователя", ex);
        }
        return result;
    }
    protected ConcurrentSkipListSet<String> readColumn(int n, TreeMap<Integer, String> condition) throws JDBCDAOException {
        ConcurrentSkipListSet<String> result = new ConcurrentSkipListSet<>();
        String param = "";
        for (Entry<Integer, String> st : condition.entrySet()) {
            if (param.length() > 0) {
                param += " and ";
            }
            param += getColumnName(st.getKey()) + " like '%" + st.getValue().replace("'", "\\'") + "%'";
        }
      
        
        String query = String.format("select %s from %s where %s", this.columnNames.get(n - 1), this.tableName,param);
     
        try (Statement st = connection.createStatement(); ResultSet res = st.executeQuery(query);) {
            while (res.next()) {
                result.add(res.getString(1));
            }
        } catch (SQLException ex) {

            throw new JDBCDAOException("Ошибка чтения имен пользователя", ex);
        }
        return result;
    }

    protected ArrayList getEnumValues(int col) throws JDBCDAOException {
        ArrayList<String> result = new ArrayList<>();
        String query = String.format("Describe %s %s", this.getTableName(), this.getColumnName(col));
        try (Statement st = connection.createStatement();
                ResultSet res = st.executeQuery(query);) {

            Pattern p = Pattern.compile("(?:(?:enum[(]\')|(?:,\'))(.+?)(?:(?:\',)|(?:\'[)]))");
            while (res.next()) {
                Matcher m = p.matcher(res.getString(2));
                int start = 0;
                while (start >= 0 && m.find(start)) {
                    result.add(m.group(1));
                    start = m.end(1);
                }

            }
        } catch (SQLException ex) {

            throw new JDBCDAOException(String.format("Ошибка чтения enum значений столбца %s таблицы %s", this.getColumnName(col), this.getTableName()), ex);
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
