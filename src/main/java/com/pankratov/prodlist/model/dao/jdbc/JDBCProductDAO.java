package com.pankratov.prodlist.model.dao.jdbc;

import com.pankratov.prodlist.model.dao.ProductDAO;
import static com.pankratov.prodlist.model.dao.ProductDAO.KindOfProduct.*;
import com.pankratov.prodlist.model.products.Product;
import java.io.File;
import java.sql.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;
import javax.servlet.ServletContext;
import javax.sql.rowset.CachedRowSet;
import org.apache.logging.log4j.Logger;


public class JDBCProductDAO extends JDBCDAOObject implements ProductDAO {

    private static final Logger log = org.apache.logging.log4j.LogManager.getLogger(JDBCProductDAO.class);
    private static JDBCDAOPool<JDBCProductDAO> pool;
    private static final String DAO_NAME = "PRODUCT";
    private static ServletContext context;
    private final Table PRODUCTS_TABLE;
    private final Table USERS_PRODUCTS_TABLE;
    private final Table IMAGES_TABLE;

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        try {
            getConnection().close();
            log.debug(String.format("Connection of %s normally closed", this));
        } finally {
            log.debug("Ripped" + this);
        }
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
        getConnection().setAutoCommit(true);
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
        super(context, DAO_NAME);
        try {
        ConcurrentHashMap<String, List<String>> tablesMetaData=this.initTables();
        String productsTableName = context.getInitParameter("PRODUCTS_TABLE");
        String usersProductsTableName = context.getInitParameter("USERS_PRODUCTS_TABLE");
        String imagesTableName = context.getInitParameter("IMAGES_TABLE");
        
           
                PRODUCTS_TABLE = new Table(productsTableName, getConnection(), tablesMetaData.get(productsTableName));
                USERS_PRODUCTS_TABLE = new Table(usersProductsTableName,  getConnection(), tablesMetaData.get(usersProductsTableName));
                IMAGES_TABLE = new Table(imagesTableName,  getConnection(), tablesMetaData.get(imagesTableName));
            
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

    private Product productFromTable(List<String> fieldValues) {
        String[] fields = {"id", "name", "subName", "producer", "value", "valueUnits",
            "group", "price", "comment", "lastModify", "author", "originID"};
        int i = 0;
        TreeMap<String, String> prodInit = new TreeMap<>();
        for (String value : fieldValues) {
            if (i >= fields.length) {
                break;
            }
            prodInit.put(fields[i++], value);
        }
        if (fieldValues.size() > 10) {
            prodInit.put("origin", "false");
        }
        return new Product(prodInit);
    }

    private List<Product> productsFromTable(List<List<String>> fieldValues) {
        List<Product> products = new LinkedList<>();
        for (List<String> record : fieldValues) {
            products.add((productFromTable(record)));
        }
        return products;
    }

    private TreeMap<Integer, String> productToTable(Product product, KindOfProduct kind) {
        TreeMap<Integer, String> s = new TreeMap<>();
        if (product.getId() != -1) {
            s.put(1, product.getId().toString());
        }
        if (!product.getName().equals("")) {
            s.put(2, product.getName());
        }
        if (!product.getSubName().equals("")) {
            s.put(3, product.getSubName());
        }
        if (!product.getProducer().equals("")) {
            s.put(4, product.getProducer());
        }
        if (product.getValue() != -1) {
            s.put(5, product.getValue().toString());
        }
        if (!product.getValueUnits().equals("")) {
            s.put(6, product.getValueUnits());
        }
        if (!product.getGroup().equals("")) {
            s.put(7, product.getGroup());
        }
        if (product.getPrice() != -1) {
            s.put(8, product.getPrice().toString());
        }
        if (!product.getComment().equals("")) {
            s.put(9, product.getComment());
        }
        if (kind == USER_COPY || kind == BOTH) {
            if (!product.getAuthor().equals("")) {
                s.put(11, product.getAuthor());
            }
            if (product.getOriginID() != -1) {
                s.put(12, product.getOriginID().toString());
            }
        }
        for (Map.Entry<Integer, String> escape : s.entrySet()) {
            escape.setValue(escape.getValue().replace("'", "\\'"));
        }

        return s;
    }

    @Override
    public Product addProduct(Product product) throws JDBCDAOException {

        Table table = USERS_PRODUCTS_TABLE;
        boolean isAdmin = product.getAuthorRole().equals("admin");
        if (isAdmin) {
            table = PRODUCTS_TABLE;
            if (!readProductGroups().contains(product.getGroup())) {
                addGroup(product.getGroup());
            }

        }
        if ((isAdmin && isProductInTable(product, ORIGINAL)) || (!isAdmin && isProductInTable(product, USER_COPY))) {
            throw new JDBCDAOException("Данный продукт уже существует.");
        }

        if (!isAdmin) {
            List<Product> origin = readProducts(new Product(product, true), ORIGINAL);
            if (origin.size() > 0) {
                product.setOriginID(origin.get(0).getId());

            }
        }
        product.setId(-1l);
        table.addRecord(productToTable(product, isAdmin ? ORIGINAL : USER_COPY));
        return readProducts(product, isAdmin ? ORIGINAL : USER_COPY).get(0);

    }

    @Override
    public Product addProduct(Product product, LinkedList<String> imagesPath) throws JDBCDAOException {
        Product p = this.addProduct(product);
        LinkedList<String> img = new LinkedList<>();
 
        TreeMap<Integer, String> s = new TreeMap<>();
        for(String image: imagesPath){
        s.put(2, image);
        s.put(p.isOrigin() ? 3 : 4, String.valueOf(p.getId()));
        IMAGES_TABLE.addRecord(s);
         img.add(image);
        }
        p.setImageLinks(img);
        return p;
    }

    @Override
    public Product deleteProduct(Product product) throws JDBCDAOException {
        Table table = product.isOrigin() ? PRODUCTS_TABLE : USERS_PRODUCTS_TABLE;
        table.deleteRowByID(product.getId().toString());
        return product;
    }

    @Override
    public List<Product> readProducts(Product product, KindOfProduct kind) throws JDBCDAOException {
        List<Product> products = new LinkedList<>();
        LinkedList<List<String>> originRows = PRODUCTS_TABLE.readRawsWhere(productToTable(product, ORIGINAL));
        LinkedList<List<String>> userRows = new LinkedList<>();
        LinkedList<List<String>> resultRows = new LinkedList<>();
        if (kind == KindOfProduct.USER_COPY || kind == KindOfProduct.BOTH) {
            if (product.getAuthorRole().equals("admin")) {
                product.setAuthor("");
            }
            userRows = USERS_PRODUCTS_TABLE.readRawsWhere(productToTable(product, USER_COPY));
        }
        switch (kind) {
            case ORIGINAL:
                resultRows = originRows;
                break;
            case BOTH:
            case USER_COPY:
                for (List<String> row : userRows) {
                    String originId = row.get(11);
                    List<String> original = null;
                    if (originId != null) {
                        for (List<String> originFields : originRows) {
                            if (originFields.get(0).equals(originId)) {
                                original = originFields;
                                break;
                            }
                        }
                    }
                    if (original != null) {
                        int j = 0;
                        for (String originalValue : original) {
                            if (row.get(j) == null) {
                                row.set(j, original.get(j));
                            }
                            j++;
                        }
                    }
                }
                if (kind == BOTH) {
                    userRows.addAll(originRows);
                }
                resultRows = userRows;
        }

        for (List<String> l : resultRows) {
            TreeMap<Integer, String> im = new TreeMap<>();
            Product localProduct = productFromTable(l);
            im.put(localProduct.isOrigin() ? 3 : 4, String.valueOf(localProduct.getId()));
            LinkedList<String> imgLinks = new  LinkedList<>();
            for (List<String> imgRes : IMAGES_TABLE.readRawsWhere(im)) {
                imgLinks.add(imgRes.get(1));
            }
            localProduct.setImageLinks(imgLinks);
            products.add(localProduct);
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
    public ConcurrentSkipListSet<String> readProductNames(Product forProduct) throws Exception {
        ConcurrentSkipListSet<String> res = new ConcurrentSkipListSet<>();
        res.addAll(PRODUCTS_TABLE.readColumn(2, productToTable(forProduct, ORIGINAL)));
        res.addAll(USERS_PRODUCTS_TABLE.readColumn(2, productToTable(forProduct, USER_COPY)));
        return res;
    }

    @Override
    public ConcurrentSkipListSet<String> readProductSubNames(Product forProduct) throws Exception {
        ConcurrentSkipListSet<String> res = new ConcurrentSkipListSet<>();
        res.addAll(PRODUCTS_TABLE.readColumn(3, productToTable(forProduct, ORIGINAL)));
        res.addAll(USERS_PRODUCTS_TABLE.readColumn(3, productToTable(forProduct,  USER_COPY)));
        return res;
    }
      
    @Override
    public ConcurrentSkipListSet<String> readProductProducers(Product forProduct) throws Exception {
        ConcurrentSkipListSet<String> res = new ConcurrentSkipListSet<>();
      //  try{
        res.addAll(PRODUCTS_TABLE.readColumn(4, productToTable(forProduct, ORIGINAL)));
        res.addAll(USERS_PRODUCTS_TABLE.readColumn(4, productToTable(forProduct,  USER_COPY)));//}
       // catch(Exception e){System.out.println(e);}
        return res;
    }
    
    @Override
    public ConcurrentSkipListSet<String> readProductValues(Product forProduct) throws Exception{
        ConcurrentSkipListSet<String> res = new ConcurrentSkipListSet<>();
        res.addAll(PRODUCTS_TABLE.readColumn(5, productToTable(forProduct, ORIGINAL)));
        res.addAll(USERS_PRODUCTS_TABLE.readColumn(5, productToTable(forProduct,  USER_COPY)));
        return res;
    } 

    @Override
    public ArrayList readProductGroups() throws JDBCDAOException {
        return PRODUCTS_TABLE.getEnumValues(7);
    }

    @Override
    public Product changeProduct(Product product) throws Exception {

        String prodID = String.valueOf(product.getId());
        product.setId(-1l);

        Product res = new Product();
        res.setId(new Long(prodID));
        if (product.isOrigin()) {
            PRODUCTS_TABLE.updateRowByID(productToTable(product, ORIGINAL), prodID);
            product = new Product();
            product.setId(new Long(prodID));
            res = this.productsFromTable(PRODUCTS_TABLE.readRawsWhere(productToTable(product, ORIGINAL))).get(0);
        } else {
            USERS_PRODUCTS_TABLE.updateRowByID(productToTable(product, USER_COPY), prodID);
            product = new Product();
            product.setId(new Long(prodID));
            res = this.productsFromTable(USERS_PRODUCTS_TABLE.readRawsWhere(productToTable(product, ORIGINAL))).get(0);
        }
        return res;
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

    boolean isProductInTable(Product product, KindOfProduct kind) throws JDBCDAOException {
        Product identity = new Product();
        identity.setName(product.getName());
        identity.setSubName(product.getSubName());
        identity.setProducer(product.getProducer());
        identity.setValue(product.getValue());

        switch (kind) {
            case ORIGINAL:
                break;
            case BOTH:
            case USER_COPY:
                identity.setAuthor(product.getAuthor());
        }

        return readProducts(identity, kind).size() > 0;
    }
}
