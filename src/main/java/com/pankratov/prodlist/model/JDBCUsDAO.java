/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pankratov.prodlist.model;

import javax.sql.*;
import java.sql.*;
import javax.servlet.ServletConfig.*;

/**
 *
 * @author pankratov
 */
public class JDBCUsDAO implements UserDAO {

    private java.sql.Connection conn;
    private final String DB_NAME;
    private final String DB_LOGIN;
    private final String DB_PASSWORD;
    private final String LOGINS_TABLE;
    private final String ROLES_TABLE;
    private final String USER_INFO_TABLE;
    private final String USER_KEY_COL;
    private PreparedStatement readUser;

    public JDBCUsDAO(javax.servlet.ServletContext context) throws Exception {
        DB_NAME = context.getInitParameter("DB_NAME");
        DB_LOGIN =context.getInitParameter("DB_LOGIN");
        DB_PASSWORD = context.getInitParameter("DB_PASSWORD");
        LOGINS_TABLE =context.getInitParameter("LOGINS_TABLE");
        ROLES_TABLE = context.getInitParameter("ROLES_TABLE");
        USER_INFO_TABLE =context.getInitParameter("USER_INFO_TABLE");
        USER_KEY_COL =context.getInitParameter("USER_KEY_COL");
        try {
            conn = DriverManager.getConnection(DB_NAME, DB_LOGIN,DB_PASSWORD);
            readUser = conn.prepareStatement("Select * from " + LOGINS_TABLE+" where " + USER_KEY_COL + "=? UNION "
                    + "Select * from " +ROLES_TABLE+" where " + USER_KEY_COL + "=? UNION "+"Select * from "+USER_INFO_TABLE+ " where " + 
                    USER_KEY_COL + "=?"); 
           javax.sql.rowset.RowSetProvider.newFactory().createCachedRowSet().
                    javax.sql.rowset.CachedRowSet rs= 
               javax.sql.CachedRowSet= new CachedRowSetImpl();
        } catch (Exception e) {
            if (readUser != null) {
                readUser.close();
            }
            if (conn != null) {
                conn.close();
            }
            throw e;
        }

    }

    @Override
    public boolean registerUser(User user) {
        return false;
    }

    @Override
    public User readUser(String name) throws Exception {
        System.out.println("Nenjxrb1");
        for(int i=1;i<=readUser.getParameterMetaData().getParameterCount();i++){
            System.out.println("Nenjxrb0");
            readUser.setString(i, name);
        }
        try (ResultSet rs = readUser.executeQuery();) {
            System.out.println("Nenjxrb");
            while (rs.next()) {
                for(int i=0; i<rs.getMetaData().getColumnCount();){System.out.println(rs.getMetaData().getColumnName(++i));}
                return new User(rs.getString(1), rs.getString(2));
                
            }

        } catch (SQLException ex) {
            Exception e = new Exception("Ошибка чтения пользователя: " + ex);
            e.initCause(ex);
            throw e;
        };
        return null;
    }

    @Override
    public User deleteUser(User user) {
        return null;
    }

    @Override
    public User changeUser(User user) {
        return null;
    }
}
