package com.pankratov.prodlist.model.dao.jdbc;

public class TruncationException extends JDBCDAOException {
    @Override public String toString(){
        return "Задано слишком большое числовое значение.";
    }
}
