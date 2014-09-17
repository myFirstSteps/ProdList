/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pankratov.prodlist.model;

import org.apache.logging.log4j.*;
import javax.sql.*;
import java.util.*;
import java.sql.*;
import javax.sql.rowset.RowSetProvider;
import javax.servlet.ServletConfig.*;
import javax.sql.rowset.*;

/**
 *
 * @author pankratov
 */
public class JDBCUsDAO implements UserDAO {

    private static final Logger log = org.apache.logging.log4j.LogManager.getLogger(JDBCUsDAO.class);
    private static JDBCUsDAO instance;
    private final String DB_NAME;
    private final String DB_LOGIN;
    private final String DB_PASSWORD;
    private final String LOGINS_TABLE;
    private final String ROLES_TABLE;
    private final String USER_INFO_TABLE;
    private TreeSet<String> logins = null;
    private HashMap<String, List<String>> tablesMetaData = new HashMap<>();
    private HashMap<String, CachedRowSet> CRS = new HashMap<>();

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
        LOGINS_TABLE = context.getInitParameter("LOGINS_TABLE");
        ROLES_TABLE = context.getInitParameter("ROLES_TABLE");
        USER_INFO_TABLE = context.getInitParameter("USER_INFO_TABLE");
        try (Connection conn = DriverManager.getConnection(DB_NAME, DB_LOGIN, DB_PASSWORD)) {
            ResultSet colMetaData = conn.getMetaData().getColumns(null, null, null, null);
            String lastTableName = "";
            while (colMetaData.next()) {
                if (!lastTableName.equals(colMetaData.getString(3))) {
                    lastTableName = colMetaData.getString(3);
                    tablesMetaData.put(lastTableName, new ArrayList<String>());
                }
                tablesMetaData.get(lastTableName).add(colMetaData.getString(4));
            }
            for (String tableName : tablesMetaData.keySet()) {
                CachedRowSet cs = RowSetProvider.newFactory().createCachedRowSet();
                cs.setPassword(DB_PASSWORD);
                cs.setUsername(DB_LOGIN);
                cs.setUrl(DB_NAME);
                CRS.put(tableName, cs);
            }

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
            for (String tableName : CRS.keySet()) {
                CachedRowSet cs = CRS.get(tableName);
                cs.setCommand("Select * from " + tableName + " where " + tablesMetaData.get(tableName).get(0) + " ='" + name + "'");
                cs.execute();
                if (cs.next()) {
                    jrs.addRowSet(cs, 1);
                }
            }
            Set<String> roles = new TreeSet<>();
            while (jrs.next()) {
                roles.add(jrs.getString(tablesMetaData.get(ROLES_TABLE).get(1)));
            }
            if (jrs.first()) {
                result = new User(jrs.getString(tablesMetaData.get(LOGINS_TABLE).get(0)), jrs.getString(tablesMetaData.get(LOGINS_TABLE).get(1)), roles.toArray(new String[1]),
                        jrs.getString(tablesMetaData.get(USER_INFO_TABLE).get(1)), jrs.getString(tablesMetaData.get(USER_INFO_TABLE).get(2)), jrs.getString(tablesMetaData.get(USER_INFO_TABLE).get(3)));
            }
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
            System.out.println("here i go");
            try { 
                logins=new TreeSet<>();
                CRS.get(LOGINS_TABLE).setCommand("select ("+tablesMetaData.get(LOGINS_TABLE).get(0)+") from "+LOGINS_TABLE);
                CRS.get(LOGINS_TABLE).execute();
                while(CRS.get(LOGINS_TABLE).next()){ logins.add(CRS.get(LOGINS_TABLE).getString(1));}
                System.out.println("here");
                System.out.println(logins);
            } catch (Exception ex) {
                log.debug("не читает пользователя", ex);
            }
           
        }
         return logins.contains(name);
    }
}
