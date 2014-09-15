/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.pankratov.prodlist.model;

/**
 *
 * @author pankratov
 */
public  class UserDAOFactory {
    private UserDAOFactory(){
       
    }
    public enum UserDAOType{JDBCUserDAO()}
    public static UserDAO getUserDAOInstance(UserDAOType type,  javax.servlet.ServletContext context) throws Exception{   
        switch(type){
            case JDBCUserDAO: return new JDBCUsDAO(context);
        }
        return null;
    }
}
