/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pankratov.prodlist.model;

import javax.sql.*;
import java.sql.*;
import javax.servlet.ServletConfig.*;
<<<<<<< HEAD
import javax.sql.rowset.*;
=======
>>>>>>> ceeb933cfaf5b4b181a361abc19e6e1fc452d564

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
<<<<<<< HEAD
    private CachedRowSet loginCRS;
=======
    private final String USER_KEY_COL;
>>>>>>> ceeb933cfaf5b4b181a361abc19e6e1fc452d564
    private PreparedStatement readUser;

    public JDBCUsDAO(javax.servlet.ServletContext context) throws Exception {
        DB_NAME = context.getInitParameter("DB_NAME");
        DB_LOGIN =context.getInitParameter("DB_LOGIN");
        DB_PASSWORD = context.getInitParameter("DB_PASSWORD");
        LOGINS_TABLE =context.getInitParameter("LOGINS_TABLE");
        ROLES_TABLE = context.getInitParameter("ROLES_TABLE");
        USER_INFO_TABLE =context.getInitParameter("USER_INFO_TABLE");
<<<<<<< HEAD
        try (Connection conn=DriverManager.getConnection(DB_NAME,DB_LOGIN,DB_PASSWORD)) {
            ResultSet cmd= conn.getMetaData().getColumns(null, null, null, null);
            String lastTableName="";
            while(cmd.next()){System.out.println(cmd.getString(1)+"  "+cmd.getString(2)+"  "+cmd.getString(3)+"  "+cmd.getString(4));
            if (!lastTableName.equals(cmd.getString(3))){
                switch (cmd.getString(3)){
                    
                    case LOGINS_TABLE: loginCRS= loginCRS.
                }
            }
            
            }
            readUser = conn.prepareStatement("Select * from " + LOGINS_TABLE+" where " + USER_KEY_COL + "=? UNION "
                    + "Select * from " +ROLES_TABLE+" where " + USER_KEY_COL + "=? UNION "+"Select * from "+USER_INFO_TABLE+ " where " + 
                    USER_KEY_COL + "=?"); 
                    CachedRowSet lcrs= RowSetProvider.newFactory().createCachedRowSet();
                    lcrs.setPassword(DB_PASSWORD);
                    lcrs.setUsername(DB_LOGIN);
                    lcrs.setUrl(DB_NAME);
                   // lcrs.setCommand("Select * from "+LOGINS_TABLE);
=======
        USER_KEY_COL =context.getInitParameter("USER_KEY_COL");
        try {
            conn = DriverManager.getConnection(DB_NAME, DB_LOGIN,DB_PASSWORD);
            readUser = conn.prepareStatement("Select * from " + LOGINS_TABLE+" where " + USER_KEY_COL + "=? UNION "
                    + "Select * from " +ROLES_TABLE+" where " + USER_KEY_COL + "=? UNION "+"Select * from "+USER_INFO_TABLE+ " where " + 
                    USER_KEY_COL + "=?"); 
           javax.sql.rowset.RowSetProvider.newFactory().createCachedRowSet().
                    javax.sql.rowset.CachedRowSet rs= 
               javax.sql.CachedRowSet= new CachedRowSetImpl();
>>>>>>> ceeb933cfaf5b4b181a361abc19e6e1fc452d564
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
