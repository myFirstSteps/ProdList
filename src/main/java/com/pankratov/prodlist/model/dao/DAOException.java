package com.pankratov.prodlist.model.dao;

public class DAOException extends Exception{
  public DAOException(){      
    }
   public DAOException(String message){  
        super(message);
    }
   public DAOException(Throwable reason){  
        super(reason);
    }
   public DAOException(Throwable reason, String message){  
        super( message, reason );
    }
   
}