package com.pankratov.prodlist.web.el;

import com.pankratov.prodlist.model.dao.DAOFactory;
import com.pankratov.prodlist.model.dao.ProdListDAO;
import com.pankratov.prodlist.model.dao.ProductDAO;
import com.pankratov.prodlist.model.dao.jdbc.JDBCProductDAO;
import com.pankratov.prodlist.model.list.ProdList;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import javax.servlet.ServletContext;

public class ProductEL {

    public static ArrayList getCategories(ServletContext context) throws Exception {
        try (ProductDAO pdao = DAOFactory.getProductDAOInstance(DAOFactory.DAOSource.JDBC, context)) {
            return ((JDBCProductDAO) pdao).readProductGroups();
        }
    }
    public static ArrayList getUnits(ServletContext context) throws Exception {
        try (ProductDAO pdao = DAOFactory.getProductDAOInstance(DAOFactory.DAOSource.JDBC, context)) {
            return ((JDBCProductDAO) pdao).readProductValueUnits();
        }
    }
    public static ArrayList getListNames(ServletContext context, String owner) throws Exception {
        try (ProdListDAO pdao = DAOFactory.getProdListDAOInstance(DAOFactory.DAOSource.JDBC, context)) {
            ProdList pl=new ProdList();
            pl.setOwnerName(owner);
             System.out.println("items"+ pdao.readListNames(pl).size());
             System.out.println("items"+ pdao.readListNames(pl));
            return  pdao.readListNames(pl);
           
        }
    }

    public static boolean isImageExist(String path) {
        Path p = java.nio.file.Paths.get(path);
            return Files.exists(p);
    }
    public static String decodeURL(String url,String characterSet)throws UnsupportedEncodingException{
        return URLDecoder.decode(url,  characterSet);
    }
}
