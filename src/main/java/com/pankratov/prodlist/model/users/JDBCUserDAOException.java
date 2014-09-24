/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.pankratov.prodlist.model.users;

/**
 *
 * @author pankratov
 */
public class JDBCUserDAOException extends Exception {
     JDBCUserDAOException (String s,Throwable cause){
         super(s,cause);
     }
     JDBCUserDAOException (String s){
         super(s);
     }
     
    
}
