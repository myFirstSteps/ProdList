/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.pankratov.prodlist.model.dao;

import com.pankratov.prodlist.model.products.Product;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 *
 * @author pankratov
 */
public interface ProductDAO extends AutoCloseable {
    public Product readProduct(Product what) throws Exception;
    public List<Product> readProducts(Product what) throws Exception;
    public Product addProduct(Product what) throws Exception;
    public Product addProduct(Product what,  String imagePath) throws Exception;
    public ArrayList readProductGroups()throws Exception; 
    public ArrayList readProductValueUnits()throws Exception; 
    public boolean addGroup (String ...group)throws Exception;
    public Product deleteProduct(Product what) throws Exception;
    public ConcurrentSkipListSet<String> readProductNames() throws Exception;
    public ConcurrentSkipListSet<String> readProductSubNames() throws Exception;
       
}
