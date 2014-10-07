/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pankratov.prodlist.web.el;

import com.pankratov.prodlist.model.dao.DAOFactory;
import com.pankratov.prodlist.model.dao.ProductDAO;
import com.pankratov.prodlist.model.dao.jdbc.JDBCProductDAO;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import javax.servlet.ServletContext;

/**
 *
 * @author pankratov
 */
public class ProductEL {

    public static ArrayList getCategories(ServletContext context) throws Exception {
        try (ProductDAO pdao = DAOFactory.getProductDAOInstance(DAOFactory.DAOSource.JDBC, context)) {
            return ((JDBCProductDAO) pdao).readProductGroups();
        }
    }

    public static boolean isImageExist(String path) {
        Path p = java.nio.file.Paths.get(path);
            return Files.exists(p);
    }
}
