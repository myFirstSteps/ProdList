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
import com.sun.rowset.CachedRowSetImpl;
import java.io.*;

/**
 *
 * @author pankratov
 */
public class JDBCUsDAO implements UserDAO {

    private class Table {

        private CachedRowSetImpl rowset;
        private String tableName;
        private List<String> columnNames;

        private Table(String tableName, List<String> colNames) throws JDBCUsDAOException  {
            try {
                rowset = new CachedRowSetImpl();
       
                rowset.setUrl(DB_NAME);
                rowset.setPassword(DB_PASSWORD);
                rowset.setUsername(DB_LOGIN);
                this.tableName = tableName;
                columnNames = colNames;
                rowset.setCommand("Select * from "+tableName+" where "+columnNames.get(0)+"=null");
                rowset.execute();
                log.debug(String.format("created table %s with columns: %s rowset:%s", tableName, columnNames, rowset));

            } catch (Exception e) {
                log.error("Table creation issue (ex): " + e);
                throw new JDBCUsDAOException("Ошибка при создании:"+Table.class+"для: "+tableName,e);
            }

        }

        private CachedRowSet readUser(String name) throws SQLException {
            rowset.setCommand("select * from " + tableName + " where "
                    + columnNames.get(0) + "= '" + name + "'");
            rowset.execute();
            return rowset;
        }
        private boolean registerUser(Connection con,String...s) throws Exception{ 
            int i=1;
            String params="";
            for(int j=0;j<s.length;j++){params+=", ?";}
            params=params.replaceAll("^, ", "");
            String command=String.format("Insert into %s values(%s)",tableName,params);
            rowset.setCommand(command);
            for(String st:s){
               //  ByteArrayOutputStream os=new ByteArrayOutputStream();
                // java.io.DataOutputStream ds= new DataOutputStream(os);
               // ds.writeUTF(st);
              //  ds.flush();
                rowset.setString(i++, st);
            }
            rowset.execute(con);
            return true;
        }

        private CachedRowSet getAll() throws SQLException {
            rowset.setCommand("select * from " + tableName);
            rowset.execute();
            return rowset;
        }

        private String getColumnName(int numb) throws JDBCUsDAOException {
            String res = null;

            try {
                res = columnNames.get(numb - 1);
            } catch (IndexOutOfBoundsException e) {
                throw new JDBCUsDAOException(String.format("У таблици %s нет столбца с индексом %d", tableName, numb), e);
            }
            return res;
        }
    }

    private static final Logger log = org.apache.logging.log4j.LogManager.getLogger(JDBCUsDAO.class);
    private static JDBCUsDAO instance;
    private final String DB_NAME;
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
        try{instance = new JDBCUsDAO(context);
        log.debug("i am created" + instance);
        context.setAttribute("JDBCUserDAO", instance);
        return instance;}catch(Exception e){
            context.setAttribute("JDBCUserDAO",null);
            throw new JDBCUsDAOException("Exception when getting  JDBCUsDAO instance",e);}
    }

    private JDBCUsDAO(javax.servlet.ServletContext context) throws  JDBCUsDAOException {

        DB_NAME = context.getInitParameter("DB_NAME")+"?useUnicode=true&characterEncoding=UTF-8";
        DB_LOGIN = context.getInitParameter("DB_LOGIN");
        DB_PASSWORD = context.getInitParameter("DB_PASSWORD");
        String loginsTableName =context.getInitParameter("LOGINS_TABLE");
        String rolesTableName = context.getInitParameter("ROLES_TABLE");
        String userInfoTableName = context.getInitParameter("USER_INFO_TABLE");
        try (Connection conn = DriverManager.getConnection(DB_NAME, DB_LOGIN, DB_PASSWORD)) {
            ResultSet colMetaData = conn.getMetaData().getColumns(null, null, null, null);
            String lastTableName = "", columnName = "", currentTableName = "";
            ConcurrentHashMap<String, List<String>> m = new ConcurrentHashMap<>();
            while (colMetaData.next()) {
                currentTableName = colMetaData.getString(3);
                columnName = colMetaData.getString(4);
                if (!lastTableName.equals(currentTableName)) {
                    m.putIfAbsent(currentTableName, new ArrayList<String>());
                    lastTableName = currentTableName;
                }
                m.get(currentTableName).add(columnName);
            }
            LOGINS_TABLE = new Table(loginsTableName, m.get(loginsTableName));
            ROLES_TABLE = new Table(rolesTableName, m.get(rolesTableName));
            USER_INFO_TABLE = new Table(userInfoTableName, m.get(userInfoTableName));

        } catch (Exception e) {
            log.error("JDBCUsDAO creation error", e);
            throw new JDBCUsDAOException ("JDBCUsDAO creation error: ",e);
        }

    }

    @Override
    public boolean registerUser(User user) throws JDBCUsDAOException{
     
        if(logins.contains(user.getLogin())) throw new JDBCUsDAOException("Ошибка регистрации пользователя. Пользователь"
                + " с логином: '"+user.getLogin()+"' уже существует.");
        try(Connection con=DriverManager.getConnection(DB_NAME, DB_LOGIN,DB_PASSWORD)){
        con.setAutoCommit(false);
        LOGINS_TABLE.registerUser(con,user.getLogin(),user.getPassword());
        ROLES_TABLE.registerUser(con,user.getLogin(),user.getRoles()[0]);
        USER_INFO_TABLE.registerUser(con,user.getLogin(),user.getFirstName(),user.getLastName(),user.getEmail());
        con.commit();
        }catch(Exception e){ throw new JDBCUsDAOException("Ошибка регистрации пользователя.",e);}
        return false;
    }

    @Override
    public User readUser(String name) throws Exception {
        User result = null;
        try {
            JoinRowSet jrs = RowSetProvider.newFactory().createJoinRowSet();
            jrs.addRowSet(LOGINS_TABLE.readUser(name), 1);
            jrs.addRowSet(ROLES_TABLE.readUser(name), 1);
            jrs.addRowSet(USER_INFO_TABLE.readUser(name), 1);
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
        } catch (Exception ex) {
            log.error("'readUser error' wrong db tables", ex);
           
            throw new JDBCUsDAOException("Reading user Exception: ",ex);
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
                CachedRowSet crs = LOGINS_TABLE.getAll();

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
