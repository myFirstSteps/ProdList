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
public class JDBCUsDAO implements UserDAO{
    private java.sql.Connection conn;
    private final String source;
    private final String table;
    private final String col;
    private final String login;
    private final String pwd;
    private PreparedStatement readUser;
    public JDBCUsDAO(javax.servlet.ServletConfig config) throws Exception{  
        source=config.getInitParameter("source");
        table=config.getInitParameter("table");
        col=config.getInitParameter("col");
        login=config.getInitParameter("adminLogin");
        pwd=config.getInitParameter("password");
        try{
        conn=DriverManager.getConnection(source,login,pwd);
        readUser= conn.prepareStatement("Select * from "+table+" where "+col+"=?" );
        
        
                }catch (Exception e){
                    if (readUser!=null)readUser.close();
                    if (conn!=null)conn.close();
                    throw e;
                }
        
    }
    @Override public  boolean registerUser(User user){ return false;}
    @Override public User readUser(String name)throws Exception{
        readUser.setString(1, name);
        try(ResultSet rs=readUser.executeQuery();){
           System.out.println("Before query");
           System.out.println("Select * from "+table+" where "+col+"="+name);
           while (rs.next())
            return new User (rs.getString(1),rs.getString(2));
        
        }
        catch (SQLException ex){};
        return null;}
    @Override public User deleteUser(User user){return null;}
    @Override public User changeUser(User user){return null;}
}
