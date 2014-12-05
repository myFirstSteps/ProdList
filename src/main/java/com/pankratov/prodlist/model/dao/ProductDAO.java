package com.pankratov.prodlist.model.dao;


import com.pankratov.prodlist.model.products.Product;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;


public interface ProductDAO extends AutoCloseable {
    @Override
    public void close() throws DAOException;
    public enum KindOfProduct{ORIGINAL,USER_COPY,BOTH,COPY_ONLY} //Перечисление определяющее из какой группы необходимо считывать продукт(общие, пользовательские, оба).
    public List<Product> readProducts(Product product, KindOfProduct kind) throws DAOException;
    public Product readProduct(Product product, KindOfProduct kind) throws DAOException;
    public Product addProduct(Product product) throws DAOException;
    public Product addProduct(Product product, LinkedList<String> imagesPath) throws DAOException;
    public ArrayList readProductGroups()throws DAOException; 
    public ArrayList readProductValueUnits()throws DAOException; 
    public boolean addGroup (String ...group)throws DAOException;
    public Product deleteProduct(Product product) throws DAOException;
    public Product changeProduct(Product product)throws DAOException;
    public ConcurrentSkipListSet<String> readProductNames(Product forProduct) throws DAOException;
    public ConcurrentSkipListSet<String> readProductSubNames(Product forProduct) throws DAOException;
    public ConcurrentSkipListSet<String> readProductProducers(Product forProduct) throws DAOException; 
    public ConcurrentSkipListSet<String> readProductValues(Product forProduct) throws DAOException;        
}
