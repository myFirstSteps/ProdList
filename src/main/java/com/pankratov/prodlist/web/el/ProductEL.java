/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.pankratov.prodlist.web.el;

import com.pankratov.prodlist.model.dao.DAOFactory;
import com.pankratov.prodlist.model.dao.ProductDAO;
import com.pankratov.prodlist.model.dao.jdbc.JDBCProductDAO;
import java.util.ArrayList;
import javax.servlet.ServletContext;

/**
 *
 * @author pankratov
 */
public class ProductEL {
    public static ArrayList getCategories(ServletContext context) throws Exception{
        try(ProductDAO pdao=DAOFactory.getProductDAOInstance(DAOFactory.DAOSource.JDBC, context)){
        return ((JDBCProductDAO)pdao).readProductGroups();
        }
    }
}
