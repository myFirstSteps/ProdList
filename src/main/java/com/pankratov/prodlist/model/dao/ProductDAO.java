/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.pankratov.prodlist.model.dao;

import com.pankratov.prodlist.model.dao.jdbc.JDBCDAOException;
import com.pankratov.prodlist.model.products.Product;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 *
 * @author pankratov
 */
public interface ProductDAO extends AutoCloseable {
    public enum KindOfProduct{ORIGINAL,USER_COPY,BOTH} //Перечисление определяющее из какой группы необходимо считывать продукт(общие, пользовательские, оба).
    public List<Product> readProducts(Product product, KindOfProduct kind) throws Exception;
    public Product readProduct(Product product, KindOfProduct kind) throws Exception;
    public Product addProduct(Product product) throws Exception;
    public Product addProduct(Product product, LinkedList<String> imagesPath) throws Exception;
    public ArrayList readProductGroups()throws Exception; 
    public ArrayList readProductValueUnits()throws Exception; 
    public boolean addGroup (String ...group)throws Exception;
    public Product deleteProduct(Product product) throws Exception;
    public Product changeProduct(Product product)throws Exception;
    public ConcurrentSkipListSet<String> readProductNames() throws Exception;
    public ConcurrentSkipListSet<String> readProductSubNames() throws Exception;
    public ConcurrentSkipListSet<String> readProductProducers() throws Exception;
    public ConcurrentSkipListSet<String> readProductNames(Product forProduct) throws Exception;
    public ConcurrentSkipListSet<String> readProductSubNames(Product forProduct) throws Exception;
    public ConcurrentSkipListSet<String> readProductProducers(Product forProduct) throws Exception; 
    public ConcurrentSkipListSet<String> readProductValues(Product forProduct) throws Exception;        
}
