/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.pankratov.prodlist.model.dao.jdbc;

/**
 *
 * @author pankratov
 */
public class JDBCDAOException extends Exception {
     public JDBCDAOException (String s,Throwable cause){
         super(s,cause);
     }
     public JDBCDAOException (String s){
         super(s);
     }
       public JDBCDAOException (Throwable t){
         super(t);
     }
     
    
}
