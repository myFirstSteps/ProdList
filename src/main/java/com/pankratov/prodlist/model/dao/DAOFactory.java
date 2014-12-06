
package com.pankratov.prodlist.model.dao;

import com.pankratov.prodlist.model.dao.jdbc.*;

import javax.servlet.ServletContext;


public class DAOFactory {
    private DAOFactory(){}
    public enum DAOSource{JDBC}
    public static UserDAO getUserDAOInstance(DAOSource source, ServletContext context) throws DAOException{
         switch(source){
            case JDBC: 
                return JDBCUserDAO.getInstance(context);
        }
        return null;
    }
    public static ProductDAO getProductDAOInstance(DAOSource source, ServletContext context) throws DAOException{
         switch(source){
            case JDBC: 
                return JDBCProductDAO.getInstance(context);//
        }
        return null;
    }
    public static ProdListDAO getProdListDAOInstance(DAOSource source, ServletContext context) throws DAOException{
         switch(source){
            case JDBC: 
                return JDBCProdListDAO.getInstance(context);//
        }
        return null;
    }
     public static JDBCValuationDAO getValuationDAOInstance(DAOSource source, ServletContext context) throws DAOException{
         switch(source){
            case JDBC: 
                return JDBCValuationDAO.getInstance(context);//
        }
        return null;
    }

}
