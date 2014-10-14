/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pankratov.prodlist.model.dao.jdbc;

import com.pankratov.prodlist.model.dao.ProductDAO;
import com.pankratov.prodlist.model.products.Product;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

        private CachedRowSet readProductByName(String name) throws SQLException, JDBCDAOException {
            CachedRowSet crs = getRowSet();
            crs.setCommand("select * from " + getTableName() + " where "
                    + getColumnName(1) + "= '" + name + "'");
            getRowSet().execute(getConnection());
            return getRowSet();
        }

        private boolean addProduct(TreeMap<String,Integer> s) throws Exception {
            addRecord(s);
            return true;
        }

        protected ArrayList getEnumValues(int col) {
            ArrayList<String> result = new ArrayList<>();

            try (Statement st = connection.createStatement();
                    ResultSet res = st.executeQuery(String.format("Describe %s %s",
                                    this.getTableName(), this.getColumnName(col)));) {

                Pattern p = Pattern.compile("(?:(?:enum[(]\')|(?:,\'))(.+?)(?:(?:\',)|(?:\'[)]))");
                while (res.next()) {
                    Matcher m = p.matcher(res.getString(2));
                    int start = 0;
                    while (start >= 0 && m.find(start)) {
                        System.out.println("matcher " + m.group(1));
                        result.add(m.group(1));
                        start = m.end(1);
                    }

                }
            } catch (SQLException | JDBCDAOException ex) {

                new JDBCDAOException("Ошибка чтения категорий продуктов", ex);
            }
            return result;

        }

        protected boolean addEnumValues(int col, String... newValues) throws Exception {
            boolean result=false;
            try (Statement st = connection.createStatement();
                    ResultSet source = st.executeQuery(String.format("Describe %s %s",
                                    this.getTableName(), this.getColumnName(col)));
                    PreparedStatement dest = connection.prepareStatement("Alter table ? modify ? ? not null default '?'");) {
                StringBuilder insertion = new StringBuilder("'"), enums = null;
                String def = "";
                while (source.next()) {
                    enums = new StringBuilder(source.getString(2));
                    def = source.getString(5);
                }
                for (String s : newValues) {
                    if (enums != null && enums.indexOf("'" + s + "'") == -1) {
                        insertion.append(",'" + s);
                    }
                }
                if (insertion.length() > 3) {
                    enums.insert(enums.lastIndexOf("','"), insertion);
                    
                    st.executeUpdate(String.format("Alter table %s modify %s %s not null default '%s'", this.getTableName(), this.getColumnName(col), enums, def));
                    result=true;
                }
            } catch (SQLException | JDBCDAOException ex) {

                throw new JDBCDAOException("Ошибка добавления категорий продуктов", ex);
            }
            return result;
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
        PRODUCTS_TABLE.getRowSet().release();
        IMAGES_TABLE.getRowSet().release();
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
            connection.setAutoCommit(false);
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

    @Override
    public Product readProduct(Product what) throws Exception {
        return null;
    }

    @Override
    public Product addProduct(Product what) throws Exception {
        this.addProduct(what, "гость");
        return null;
    }

    @Override
    public Product addProduct(Product what, String whosAdd) throws Exception {
        switch (whosAdd) {
            case "admin":
                if (!readProductGroups().contains(what.getGroup())) {
                        addGroup(what.getGroup());
                }
                TreeMap<String,Integer> s=new TreeMap<>();
                s.put(what.getName(), 2);
                s.put(what.getSubname(), 3);
                s.put(what.getProducer(), 4);
                s.put(String.valueOf(what.getValue()), 5);
                s.put(what.getValueUnits(), 6);
                s.put(what.getGroup(), 7);
                s.put(String.valueOf(what.getPrice()), 8);
                s.put(what.getComment(), 10);
                PRODUCTS_TABLE.addProduct(s);
        }
        return null;
    }

    @Override
    public Product addProduct(Product what, String whosAdd, File prodImage) throws Exception {
        this.addProduct(what, whosAdd);
        return null;
    }

    @Override
    public Product deleteProduct(Product what) throws Exception {
        return null;
    }

    @Override
    public ConcurrentSkipListSet<String> readProductNames() throws Exception {
        return PRODUCTS_TABLE.readColumn(2);
    }

    @Override
    public ConcurrentSkipListSet<String> readProductSubNames() throws Exception {
        return PRODUCTS_TABLE.readColumn(2);
    }

    @Override
    public ArrayList readProductGroups() {
        return PRODUCTS_TABLE.getEnumValues(7);
    }

    @Override
    public ArrayList readProductValueUnits() {
        return PRODUCTS_TABLE.getEnumValues(6);
    }

    @Override
    public boolean addGroup(String... group) throws Exception {
        if (group.length == 0) {
            return false;
        }
        PRODUCTS_TABLE.addEnumValues(7, group);
        return false;
    }
}
