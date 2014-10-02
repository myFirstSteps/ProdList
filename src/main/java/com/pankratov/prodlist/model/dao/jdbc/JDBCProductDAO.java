/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.pankratov.prodlist.model.dao.jdbc;

import com.pankratov.prodlist.model.dao.ProductDAO;
import com.pankratov.prodlist.model.products.Product;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 *
 * @author pankratov
 */
public class JDBCProductDAO implements ProductDAO {
    @Override
    public Product readProduct(Product what) throws Exception{
        return null;
    }
    @Override
    public Product addProduct(Product what)throws Exception{
        return null;
    }
    @Override
    public Product deleteProduct(Product what)throws Exception{
        return null;
    }
    @Override
    public ConcurrentSkipListSet<String> readProductNames()throws Exception{
        return null;
    }
    @Override
    public ConcurrentSkipListSet<String> readProductSubNames()throws Exception{
        return null;
    }
    
}
