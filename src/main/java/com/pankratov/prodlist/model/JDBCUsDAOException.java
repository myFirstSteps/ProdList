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
public class JDBCUsDAOException extends Exception {
     JDBCUsDAOException (String s,Throwable cause){
         super(s,cause);
     }
    
}