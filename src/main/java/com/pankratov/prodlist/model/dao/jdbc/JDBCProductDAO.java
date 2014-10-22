/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pankratov.prodlist.model.dao.jdbc;

import com.pankratov.prodlist.model.dao.ProductDAO;
import com.pankratov.prodlist.model.products.Product;
import java.io.File;
import java.sql.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;
import javax.servlet.ServletContext;
import javax.sql.rowset.CachedRowSet;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author pankratov
 */
public class JDBCProductDAO extends JDBCDAOObject implements ProductDAO {

    private static final Logger log = org.apache.logging.log4j.LogManager.getLogger(JDBCProductDAO.class);
    private static JDBCDAOPool<JDBCProductDAO> pool;
    private static final String DAO_NAME = "PRODUCT";
    private static ServletContext context;
    private final String DB_NAME;
    private final String DB_LOGIN;
    private final String DB_PASSWORD;
    private final ProductTable PRODUCTS_TABLE;
    private final ProductTable USERS_PRODUCTS_TABLE;
    private final ProductTable IMAGES_TABLE;
    private final Connection connection;

    private class ProductTable extends Table {

        protected ProductTable(String tableName, Connection con, List<String> colNames) throws JDBCDAOException {
            super(tableName, con, colNames);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        try {
            connection.close();
            log.debug(String.format("Connection of %s normally closed", this));
        } finally {
            log.debug("Ripped" + this);
        }
    }

    @Override
    Connection getConnection() {
        return connection;
    }

    @Override
    public String getDAOName() {
        return DAO_NAME;
    }

    @Override
    public void close() throws SQLException {
        USERS_PRODUCTS_TABLE.getRowSet().release();
        PRODUCTS_TABLE.getRowSet().release();
        IMAGES_TABLE.getRowSet().release();
        connection.setAutoCommit(true);
        pool.put(this);
    }

    @Override
    protected JDBCProductDAO newInstance() throws JDBCDAOException {
        return new JDBCProductDAO(context);
    }

    static public JDBCProductDAO getInstance(javax.servlet.ServletContext context) throws Exception {
        try {
            JDBCProductDAO instance = null;
            JDBCProductDAO.context = context;
            if (pool == null) {
                synchronized (JDBCUserDAO.class) {
                    if (pool == null) {
                        pool = new JDBCDAOPool<>(new JDBCProductDAO(context));
                    }
                }
            }
            instance = pool.get();
            return instance;
        } catch (Exception e) {
            throw new JDBCDAOException("Exception when getting  JDBCUsDAO instance", e);
        }

    }

    private JDBCProductDAO(ServletContext context) throws JDBCDAOException {
        DB_NAME = context.getInitParameter("DB_PROD_NAME");
        DB_LOGIN = context.getInitParameter("DB_PROD_LOGIN");
        DB_PASSWORD = context.getInitParameter("DB_PROD_PASSWORD");
        String productsTableName = context.getInitParameter("PRODUCTS_TABLE");
        String usersProductsTableName = context.getInitParameter("USERS_PRODUCTS_TABLE");
        String imagesTableName = context.getInitParameter("IMAGES_TABLE");
        try {
            connection = DriverManager.getConnection(DB_NAME, DB_LOGIN, DB_PASSWORD);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
            try (ResultSet colMetaData = connection.getMetaData().getColumns(null, null, null, null);) {
                String lastTableName = "", columnName = "", currentTableName = "";
                ConcurrentHashMap<String, List<String>> m = new ConcurrentHashMap<>();
                while (colMetaData.next()) {
                    currentTableName = colMetaData.getString(3);
                    columnName = colMetaData.getString(4);
                    if (!lastTableName.equals(currentTableName)) {
                        m.putIfAbsent(currentTableName, new ArrayList<String>());
                        lastTableName = currentTableName;
                    }
                    m.get(currentTableName).add(columnName);
                }
                PRODUCTS_TABLE = new JDBCProductDAO.ProductTable(productsTableName, connection, m.get(productsTableName));
                USERS_PRODUCTS_TABLE = new JDBCProductDAO.ProductTable(usersProductsTableName, connection, m.get(usersProductsTableName));
                IMAGES_TABLE = new JDBCProductDAO.ProductTable(imagesTableName, connection, m.get(imagesTableName));
            }
            log.debug("ProductDAO created");
        } catch (Exception e) {
            log.error("JDBCProductDAO creation error", e);
            throw new JDBCDAOException("JDBCProductDAO creation error: ", e);
        }
    }

    @Override
    ServletContext getContext() {
        return context;
    }

    private Product productFromTable(List<String> s) {
        return new Product(s.get(0), s.get(1), s.get(2), s.get(3), s.get(4), s.get(5), s.get(6), s.get(7), s.get(8), s.size() > 9 ? s.get(9) : null,
                s.size() > 10 ? s.get(10) : null);
    }

    private TreeMap<Integer, String> productToTable(Product product) {
        TreeMap<Integer, String> s = new TreeMap<>();
        if (product.getId() != null) {
            s.put(1, product.getId().toString());
        }
        if (product.getName() != null) {
            s.put(2, product.getName());
        }
        if (product.getSubName() != null) {
            s.put(3, product.getSubName());
        }
        if (product.getProducer() != null) {
            s.put(4, product.getProducer());
        }
        if (product.getValue() != null) {
            s.put(5, product.getValue().toString());
        }
        if (product.getValueUnits() != null) {
            s.put(6, product.getValueUnits());
        }
        if (product.getGroup() != null) {
            s.put(7, product.getGroup());
        }
        if (product.getPrice() != null) {
            s.put(8, product.getPrice().toString());
        }
        if (product.getComment() != null) {
            s.put(9, product.getComment());
        }
        if (product.getAuthor() != null) {
            s.put(10, product.getAuthor());
        }
        return s;
    }
    private TreeMap<Integer, String> productToTable(Product product, boolean full) {
        TreeMap<Integer, String> s = new TreeMap<>();
        s=this.productToTable(product);
        if (!full) return s; 
        if (product.getAuthor() != null) {
            s.put(10, product.getAuthor());
        }
        return s;
    }

    @Override
    public Product addProduct(Product product) throws JDBCDAOException {

        ProductTable table = USERS_PRODUCTS_TABLE;
        boolean isAdmin=product.getAuthorRole().equals("admin");
        if (isAdmin) {
            table = PRODUCTS_TABLE;
            if (!readProductGroups().contains(product.getGroup())) {
                addGroup(product.getGroup());
            }
        }
        if (readProducts(new Product(product, true)).size() > 0) {
            throw new JDBCDAOException("Данный продукт уже существует.");
        }
      
        table.addRecord(productToTable(product,!isAdmin));

        return readProducts(product).get(0);

    }

    @Override
    public Product addProduct(Product product, String imagePath) throws JDBCDAOException {
        Product p = this.addProduct(product);
        TreeMap<Integer, String> s = new TreeMap<>();
        s.put(2, imagePath);
        s.put(product.isOrigin() ? 3 : 4, String.valueOf(p.getId()));
        IMAGES_TABLE.addRecord(s);
        ArrayList<String> img = new ArrayList<>();
        img.add(imagePath);
        p.setImageLinks(img);
        return p;
    }

    @Override
    public Product deleteProduct(Product what) throws JDBCDAOException {
        return null;
    }

   /* @Override
    public Product readProduct(Product product) throws JDBCDAOException {
        Table table = product.isOrigin() ? PRODUCTS_TABLE : USERS_PRODUCTS_TABLE;
        LinkedList<List<String>> pr = table.readRawsWhere(productToTable(product));
        if (pr.size() > 1) {
            throw new JDBCDAOException("Во время чтения продукта, произошла ошибка.\n Объект не уникален.");
        }
        return productFromTable(pr.peek());
    }*/

    @Override
    public List<Product> readProducts(Product product) throws JDBCDAOException {
        List<Product> products = new LinkedList<>();
        
      
        LinkedList<List<String>> pr =  PRODUCTS_TABLE.readRawsWhere(productToTable(product));
        pr.addAll( USERS_PRODUCTS_TABLE.readRawsWhere(productToTable(product,true)));
        for (List<String> l : pr) {
            products.add(productFromTable(pr.poll()));
        }
        return products;
    }

    @Override
    public ConcurrentSkipListSet<String> readProductNames() throws JDBCDAOException {
        return PRODUCTS_TABLE.readColumn(2);
    }

    @Override
    public ConcurrentSkipListSet<String> readProductSubNames() throws JDBCDAOException {
        return PRODUCTS_TABLE.readColumn(3);
    }
    @Override
    public ConcurrentSkipListSet<String> readProductProducers() throws JDBCDAOException {
        return PRODUCTS_TABLE.readColumn(4);
    }
     @Override
    public ConcurrentSkipListSet<String> readProductNames(Product forProduct) throws Exception{
        ConcurrentSkipListSet<String> res=new ConcurrentSkipListSet<>();
        res.addAll(PRODUCTS_TABLE.readColumn(2,productToTable(forProduct)));
        res.addAll(USERS_PRODUCTS_TABLE.readColumn(2,productToTable(forProduct)));
        return res;
    }
     @Override
    public ConcurrentSkipListSet<String> readProductSubNames(Product forProduct) throws Exception{
        ConcurrentSkipListSet<String> res=new ConcurrentSkipListSet<>();
        res.addAll(PRODUCTS_TABLE.readColumn(3,productToTable(forProduct)));
        res.addAll(USERS_PRODUCTS_TABLE.readColumn(3,productToTable(forProduct)));
        return res;
    }
     @Override
    public ConcurrentSkipListSet<String> readProductProducers(Product forProduct) throws Exception{
        ConcurrentSkipListSet<String> res=new ConcurrentSkipListSet<>();
        res.addAll(PRODUCTS_TABLE.readColumn(4,productToTable(forProduct)));
        res.addAll(USERS_PRODUCTS_TABLE.readColumn(4,productToTable(forProduct)));
        return res;
    }

    @Override
    public ArrayList readProductGroups() throws JDBCDAOException {
        return PRODUCTS_TABLE.getEnumValues(7);
    }

    @Override
    public ArrayList readProductValueUnits() throws JDBCDAOException {
        return PRODUCTS_TABLE.getEnumValues(6);
    }

    @Override
    public boolean addGroup(String... group) throws JDBCDAOException {
        if (group.length == 0) {
            return false;
        }
        PRODUCTS_TABLE.addEnumValues(7, group);
        return true;
    }
}
