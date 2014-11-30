
package com.pankratov.prodlist.model.dao.jdbc;

//Добавляемые данные уже присутствуют в БД
public class AlreadyExistsException extends JDBCDAOException{
    @Override 
    public String toString(){
        return "Добавляемые данные уже существуют.";
    } 
}
