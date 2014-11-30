package com.pankratov.prodlist.model.dao;

import com.pankratov.prodlist.model.list.ProdList;
import java.util.ArrayList;
import java.util.List;

public interface  ProdListDAO extends AutoCloseable{
     List<ProdList> readProdLists(ProdList list) throws DAOException;
     boolean addProdList(ProdList list) throws DAOException;
     ProdList changeProdList(ProdList list) throws DAOException;
     ArrayList readListNames(ProdList list)throws DAOException; 
}
