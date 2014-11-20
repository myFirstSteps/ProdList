/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.pankratov.prodlist.model.dao;

import com.pankratov.prodlist.model.list.ProdList;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author pankratov
 */
public interface  ProdListDAO extends AutoCloseable{
     List<ProdList> readProdLists(ProdList list) throws Exception;
     boolean addProdList(ProdList list) throws Exception;
     ProdList changeProdList(ProdList list) throws Exception;
     ArrayList readListNames(ProdList list)throws Exception; 
}
