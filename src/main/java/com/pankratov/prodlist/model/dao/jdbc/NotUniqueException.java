package com.pankratov.prodlist.model.dao.jdbc;

public class NotUniqueException extends JDBCDAOException {
    @Override 
        public String toString(){
        return "Для запрашиваемого объекта найдено более одного совпадения.";
    }
}
