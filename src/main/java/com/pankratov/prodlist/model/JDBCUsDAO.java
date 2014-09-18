/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pankratov.prodlist.model;

import org.apache.logging.log4j.*;
import javax.sql.*;
import java.util.*;
import java.util.concurrent.*;
import java.sql.*;
import javax.sql.rowset.RowSetProvider;
import javax.servlet.ServletConfig.*;
import javax.sql.rowset.*;

/**
 *
 * @author pankratov
 */
public class JDBCUsDAO implements UserDAO {

    private class Table {

        private CachedRowSet rowset;
        private String tableName;
        private List<String> columnNames;

        private Table(String tableName, List<String> colNames) {
            try {
                rowset = RowSetProvider.newFactory().createCachedRowSet();
                rowset.setUrl(DB_NAME);
                System.out.println("ID:++++++"+rowset.getSyncProvider().getProviderID());
              //  System.out.println(rowset.getSyncProvider().;
                rowset.setPassword(DB_PASSWORD);
                rowset.setUsername(DB_LOGIN);
                this.tableName = tableName;
                columnNames = colNames;
                log.debug(String.format("created table %s with columns: %s rowset:%s", tableName,columnNames,rowset));
     
            } catch (Exception e) {
                log.error("Table creation issue (ex): " + e);
            }

        }

        private CachedRowSet readUser(String name) throws SQLException {
            rowset.setCommand("select * from " + tableName + " where "
                    + columnNames.get(0) + "= '" + name + "'");
            rowset.execute();
            return rowset;
        }
        private CachedRowSet getAll()throws SQLException{
            rowset.setCommand("select * from " + tableName);
            rowset.execute();
            return rowset;
        }
        private String getColumnName(int numb)throws JDBCUsDAOException{
            String res=null;
        
            try{
            res= columnNames.get(numb-1);
            }catch(IndexOutOfBoundsException e){ throw new JDBCUsDAOException(String.format("У таблици %s нет столбца с индексом %d", tableName,numb),e);}
            return res;
        }
    }

    private static final Logger log = org.apache.logging.log4j.LogManager.getLogger(JDBCUsDAO.class);
    private static JDBCUsDAO instance;
    private static String DB_NAME;
    private final String DB_LOGIN;
    private final String DB_PASSWORD;
    private Table LOGINS_TABLE;
    private Table ROLES_TABLE;
    private Table USER_INFO_TABLE;
    private ConcurrentSkipListSet<String> logins = null;

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        log.debug("Ripped" + this);
    }

    static JDBCUsDAO getInstance(javax.servlet.ServletContext context) throws Exception {
        instance = new JDBCUsDAO(context);
        log.debug("i am created" + instance);
        context.setAttribute("JDBCUserDAO", instance);
        return instance;
    }

    private JDBCUsDAO(javax.servlet.ServletContext context) throws Exception {

        DB_NAME = context.getInitParameter("DB_NAME");
        DB_LOGIN = context.getInitParameter("DB_LOGIN");
        DB_PASSWORD = context.getInitParameter("DB_PASSWORD");
        String loginsTableName = context.getInitParameter("LOGINS_TABLE");
        String rolesTableName = context.getInitParameter("ROLES_TABLE");
        String userInfoTableName = context.getInitParameter("USER_INFO_TABLE");
        try (Connection conn = DriverManager.getConnection(DB_NAME, DB_LOGIN, DB_PASSWORD)) {
            ResultSet colMetaData = conn.getMetaData().getColumns(null, null, null, null);
            String lastTableName = "", columnName = "", currentTableName = "";
            ConcurrentHashMap<String,List<String>> m=new ConcurrentHashMap<>();
            while (colMetaData.next()) {
                currentTableName = colMetaData.getString(3);
                columnName = colMetaData.getString(4);
                if (!lastTableName.equals(currentTableName)) {
                    m.putIfAbsent(currentTableName, new ArrayList<String>());
                    lastTableName = currentTableName;
                }
               m.get(currentTableName).add(columnName);
            }
            LOGINS_TABLE= new Table(loginsTableName, m.get(loginsTableName));
            ROLES_TABLE = new Table(rolesTableName, m.get(rolesTableName));
            USER_INFO_TABLE =  new Table(userInfoTableName, m.get(userInfoTableName));
            
        } catch (Exception e) {
            log.error("JDBCUsDAO creation error", e);
            throw e;
        }

    }

    @Override
    public boolean registerUser(User user) {
        return false;
    }

    @Override
    public User readUser(String name) throws Exception {
        User result = null;
        try {
            JoinRowSet jrs = RowSetProvider.newFactory().createJoinRowSet();
            jrs.addRowSet(LOGINS_TABLE.readUser(name),1);
            jrs.addRowSet(ROLES_TABLE.readUser(name),1);
            jrs.addRowSet(USER_INFO_TABLE.readUser(name),1);
            Set<String> roles = new TreeSet<>();
            while (jrs.next()) {
                roles.add(jrs.getString(ROLES_TABLE.getColumnName(2)));
            }  
            if (jrs.first()) {
                result = new User(jrs.getString(LOGINS_TABLE.getColumnName(1)),
                        jrs.getString(LOGINS_TABLE.getColumnName(2)), 
                        roles.toArray(new String[1]),
                        jrs.getString(USER_INFO_TABLE.getColumnName(2)),
                        jrs.getString(USER_INFO_TABLE.getColumnName(3)), 
                        jrs.getString(USER_INFO_TABLE.getColumnName(4)));
            
                    }
           LOGINS_TABLE.rowset.moveToInsertRow();
             LOGINS_TABLE.rowset.updateString(1, "fuck");
             LOGINS_TABLE.rowset.updateString(2, "badpass");
              LOGINS_TABLE.rowset.insertRow();
               LOGINS_TABLE.rowset.moveToCurrentRow();
              LOGINS_TABLE.rowset.acceptChanges();
           
        } catch (Exception ex) {
            log.error("'readUser error' wrong db tables", ex);
            Exception e = new Exception("Ошибка чтения пользователя: " + ex);
            e.initCause(ex);
            throw e;
        };
        return result;
    }

    @Override
    public User deleteUser(User user) {
        return null;
    }

    @Override
    public User changeUser(User user) {
        return null;
    }

    @Override
    public boolean isUserExsists(String name) {
        if (logins == null) {
            try {
                logins = new ConcurrentSkipListSet<>();
                CachedRowSet crs=LOGINS_TABLE.getAll();
                
                while (crs.next()) {
                    logins.add(crs.getString(1));
                }
            } catch (Exception ex) {
                log.error("login exsists issue", ex);
            }

        }
        return logins.contains(name);
    }
}
