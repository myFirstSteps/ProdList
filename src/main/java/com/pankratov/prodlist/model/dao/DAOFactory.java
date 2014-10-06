/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.pankratov.prodlist.model.dao;

import com.pankratov.prodlist.model.dao.jdbc.JDBCProductDAO;
import com.pankratov.prodlist.model.dao.jdbc.JDBCUserDAO;
import javax.servlet.ServletContext;

/**
 *
 * @author pankratov
 */
public class DAOFactory {
    private DAOFactory(){}
    public enum DAOSource{JDBC}
    public static UserDAO getUserDAOInstance(DAOSource source, ServletContext context) throws Exception{
         switch(source){
            case JDBC: 
                return JDBCUserDAO.getInstance(context);
        }
        return null;
    }
    public static ProductDAO getProductDAOInstance(DAOSource source, ServletContext context) throws Exception{
         switch(source){
            case JDBC: 
                return JDBCProductDAO.getInstance(context);//
        }
        return null;
    }
    

}
