package com.pankratov.prodlist.model.dao.jdbc;
// Абстрактный класс, реализующий JDBC DAO
import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.ServletContext;
import org.apache.logging.log4j.*;

public abstract class JDBCDAOObject implements AutoCloseable {
    private static final Logger log=LogManager.getLogger(JDBCDAOObject.class);
    abstract protected JDBCDAOObject newInstance()throws JDBCDAOException; 
     private long offerTime = System.currentTimeMillis();  //Время возвращения объекта в пул.
     private boolean pensioner = false; // если true, объект редко запрашивается из пула и его можно уничтожить
     private Connection connection; 
  
    protected  JDBCDAOObject(ServletContext context, String DAO_NAME) throws JDBCDAOException {
        try{
      String DB_NAME = context.getInitParameter(String.format("JDBCDAO_%s_URL",DAO_NAME)); //connection url из дескриптора
      String DB_LOGIN =  context.getInitParameter(String.format("JDBCDAO_%s_LOGIN",DAO_NAME));// логин и пароль для подключения
      String DB_PASSWORD = context.getInitParameter(String.format("JDBCDAO_%s_PASSWORD",DAO_NAME));
        connection = DriverManager.getConnection(DB_NAME, DB_LOGIN, DB_PASSWORD);
        connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);}
        catch (SQLException | NullPointerException e){ try{connection.close();}finally{ log.error(e); throw new JDBCDAOException (e);}}
        
    } 
    //Чтение имен столбцов таблиц БД. Для удобства разворачивания на разных серверах обращение к таблицам БД идет по номеру, а не по имени столбца.
    protected ConcurrentHashMap<String, List<String>> initTables()throws SQLException{
         ConcurrentHashMap<String, List<String>> m = new ConcurrentHashMap<>();
        try (ResultSet colMetaData = connection.getMetaData().getColumns(null, null, null, null);) {
                String lastTableName = "", columnName = "", currentTableName = "";
               
                while (colMetaData.next()) {
                    currentTableName = colMetaData.getString(3);
                    columnName = colMetaData.getString(4);
                    if (!lastTableName.equals(currentTableName)) {
                        m.putIfAbsent(currentTableName, new ArrayList<String>());
                        lastTableName = currentTableName;
                    }
                    m.get(currentTableName).add(columnName);
                }
        }
        return m;
    }
    public long getOfferTime() {
        return offerTime;
    }
    
    abstract ServletContext getContext();

    public void setOfferTime(long offerTime) {
        this.offerTime = offerTime;
    }
    public Connection getConnection(){
        return connection;
    }

    public boolean isPensioner() {
        return pensioner;
    }

    public void setPensioner(boolean pensioner) {
        this.pensioner = pensioner;
    }
    abstract public String getDAOName();
}
