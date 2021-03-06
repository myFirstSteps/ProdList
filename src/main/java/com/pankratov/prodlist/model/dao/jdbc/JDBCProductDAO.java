package com.pankratov.prodlist.model.dao.jdbc;

import com.pankratov.prodlist.model.dao.ProductDAO;
import static com.pankratov.prodlist.model.dao.ProductDAO.KindOfProduct.*;
import com.pankratov.prodlist.model.products.Product;
import java.sql.*;
import java.util.*;
import java.util.concurrent.*;
import javax.servlet.ServletContext;
import org.apache.logging.log4j.*;

public class JDBCProductDAO extends JDBCDAOObject implements ProductDAO {

    private static final Logger log = LogManager.getLogger(JDBCProductDAO.class);
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
    public void close() throws JDBCDAOException {
        try {
            USERS_PRODUCTS_TABLE.getRowSet().release();
            PRODUCTS_TABLE.getRowSet().release();
            IMAGES_TABLE.getRowSet().release();
            getConnection().setAutoCommit(true);
            pool.put(this);
        } catch (SQLException e) {
            log.error("close exception", e);
            throw new JDBCDAOException(e);
        }
    }

    @Override
    protected JDBCProductDAO newInstance() throws JDBCDAOException {
        return new JDBCProductDAO(context);
    }

    static public JDBCProductDAO getInstance(javax.servlet.ServletContext context) throws JDBCDAOException {
        try {
            JDBCProductDAO instance = null;
            JDBCProductDAO.context = context;
            if (pool == null) {
                synchronized (JDBCProductDAO.class) {
                    if (pool == null) {
                        pool = new JDBCDAOPool<>(new JDBCProductDAO(context));
                    }
                }
            }
            instance = pool.get();
            return instance;
        } catch (Exception e) {
            log.error(e);
            throw new JDBCDAOException(String.format("Exception when getting  JDBC%sDAO instance", DAO_NAME), e);
        }
    }

    private JDBCProductDAO(ServletContext context) throws JDBCDAOException {
        super(context, DAO_NAME);
        try {
            ConcurrentHashMap<String, List<String>> tablesMetaData = this.initTables();
            String productsTableName = context.getInitParameter("PRODUCTS_TABLE");
            String usersProductsTableName = context.getInitParameter("USERS_PRODUCTS_TABLE");
            String imagesTableName = context.getInitParameter("IMAGES_TABLE");
            PRODUCTS_TABLE = new Table(productsTableName, getConnection(), tablesMetaData.get(productsTableName));
            USERS_PRODUCTS_TABLE = new Table(usersProductsTableName, getConnection(), tablesMetaData.get(usersProductsTableName));
            IMAGES_TABLE = new Table(imagesTableName, getConnection(), tablesMetaData.get(imagesTableName));

            log.debug("ProductDAO created");
        } catch (Exception e) {
            log.error("JDBCProductDAO creation error", e);
            throw new JDBCDAOException(String.format("Exception when creating  JDBC%sDAO", DAO_NAME), e);
        }
    }

    @Override
    ServletContext getContext() {
        return context;
    }

    //Преобразование прочитанных записей в продукт. 
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

    //Разложение продукта на пары ключ-значение для обращения к таблице.
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
        if ((isAdmin && isProductInTable(product, ORIGINAL)) || (!isAdmin && (product.isOrigin()?isProductInTable(product, USER_COPY):isProductInTable(product, COPY_ONLY)))) {
            throw new AlreadyExistsException();
        }

        if (!isAdmin) {
            Product templ = new Product(product, true);
            templ.setId(product.getOriginID() != -1l ? product.getOriginID() : -1l);
            List<Product> origin = readProducts(templ, ORIGINAL);
            if (origin.size() > 0) {
                product.setOriginID(origin.get(0).getId());
                product.setName(origin.get(0).getName());
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
        for (String image : imagesPath) {
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
    public Product readProduct(Product product, KindOfProduct kind) throws JDBCDAOException {
        List<Product> p = readProducts(product, kind);
        switch (p.size()) {
            case 1:
                return p.get(0);
            case 0:
                return null;
            default:
                throw new NotUniqueException();
        }
    }

    @Override
    public List<Product> readProducts(Product product, KindOfProduct kind) throws JDBCDAOException {
        List<Product> products = new LinkedList<>();
        LinkedHashSet<List<String>> originRows = new LinkedHashSet<>(), userRows = new LinkedHashSet<>(),
                resultRows = new LinkedHashSet<>(), dublicateRows = new LinkedHashSet<>();
        Product localp;
        originRows.addAll(PRODUCTS_TABLE.readRawsWhere(productToTable(product, ORIGINAL)));
        if (kind == KindOfProduct.USER_COPY || kind == KindOfProduct.BOTH) {
            if (product.getAuthorRole().equals("admin")) {
                product.setAuthor("");
            }
            if (!product.isOrigin() && product.getOriginID() != -1) {
                localp = new Product(product.getOriginID());
                originRows.addAll(PRODUCTS_TABLE.readRawsWhere(productToTable(localp, ORIGINAL)));
            }
            if (originRows.size() > 0) {
                for (List<String> o : originRows) {
                    localp = new Product(-1l, Long.parseLong(o.get(0)), product.getAuthor());
                    userRows.addAll(USERS_PRODUCTS_TABLE.readRawsWhere(productToTable(localp, USER_COPY)));
                }
            }
            userRows.addAll(USERS_PRODUCTS_TABLE.readRawsWhere(productToTable(product, USER_COPY)));
        }
        switch (kind) {
            case ORIGINAL:
                resultRows = originRows;
                break;
            case COPY_ONLY:
            case USER_COPY:
            case BOTH:
                for (List<String> row : userRows) {
                    String originId = row.get(11);
                    List<String> original = null;
                    if (originId != null) {
                        for (List<String> originFields : originRows) {
                            if (originFields.get(0).equals(originId)) {
                                original = originFields;
                                dublicateRows.add(original);
                                originRows.remove(originFields);
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
                resultRows.addAll(userRows);
                if(kind==COPY_ONLY)break;
                resultRows.addAll(originRows);
                if(kind==BOTH) resultRows.addAll(dublicateRows);
        }

        for (List<String> l : resultRows) {
            TreeMap<Integer, String> im = new TreeMap<>();
            Product localProduct = productFromTable(l);
            im.put(localProduct.isOrigin() ? 3 : 4, String.valueOf(localProduct.getId()));
            LinkedList<String> imgLinks = new LinkedList<>();
            for (List<String> imgRes : IMAGES_TABLE.readRawsWhere(im)) {
                imgLinks.add(imgRes.get(1));
            }
            localProduct.setImageLinks(imgLinks);
            products.add(localProduct);
        }
        return products;
    }

    protected ConcurrentSkipListSet<String> readColumn(int col, Product product) throws JDBCDAOException {
        ConcurrentSkipListSet<String> res = new ConcurrentSkipListSet<>();
        res.addAll(PRODUCTS_TABLE.readColumn(col, productToTable(product, ORIGINAL)));
        res.addAll(USERS_PRODUCTS_TABLE.readColumn(col, productToTable(product, USER_COPY)));
        return res;
    }

    @Override
    public ConcurrentSkipListSet<String> readProductNames(Product product) throws JDBCDAOException {
        return readColumn(2, product);
    }

    @Override
    public ConcurrentSkipListSet<String> readProductSubNames(Product product) throws JDBCDAOException {
        return readColumn(3, product);
    }

    @Override
    public ConcurrentSkipListSet<String> readProductProducers(Product product) throws JDBCDAOException {
        return readColumn(4, product);
    }

    @Override
    public ConcurrentSkipListSet<String> readProductValues(Product product) throws JDBCDAOException {
        return readColumn(5, product);
    }

    @Override
    public ArrayList readProductGroups() throws JDBCDAOException {
        return PRODUCTS_TABLE.getEnumValues(7);
    }

    @Override
    public Product changeProduct(Product product) throws JDBCDAOException {
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
        identity.setGroup(product.getGroup());
        identity.setName(product.getName());
        identity.setSubName(product.getSubName());
        identity.setProducer(product.getProducer());
        identity.setValue(product.getValue());

        switch (kind) {
            case ORIGINAL:
                break;
            case BOTH:
            case USER_COPY:
                identity.setOriginID(product.getOriginID());
                identity.setAuthor(product.getAuthor());
        }
        return readProducts(identity, kind).size() > 0;
    }
}
