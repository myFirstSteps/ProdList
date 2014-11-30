
package com.pankratov.prodlist.model.dao.jdbc;

import com.pankratov.prodlist.model.dao.DAOException;

public class JDBCDAOException extends DAOException {
     public JDBCDAOException(){}
     public JDBCDAOException (Throwable cause,String s){
         super(cause,s);
     }
     public JDBCDAOException (String s, Throwable cause){
         super(cause,s);
     }
     public JDBCDAOException (String s){
         super(s);
     }
       public JDBCDAOException (Throwable t){
         super(t);
     }
     
    
}
