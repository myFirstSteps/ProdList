package com.pankratov.prodlist.model.dao.jdbc;

import com.pankratov.prodlist.model.dao.UserDAO;
import com.pankratov.prodlist.model.users.User;
import java.sql.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import javax.servlet.ServletContext;
import javax.sql.rowset.*;
import javax.sql.rowset.RowSetProvider;
import org.apache.logging.log4j.*;

public class JDBCUserDAO extends JDBCDAOObject implements UserDAO {

    private class UserTable extends Table {

        protected UserTable(String tableName, Connection con, List<String> colNames) throws JDBCDAOException {
            super(tableName, con, colNames);
        }

        private CachedRowSet readUser(String name) throws SQLException, JDBCDAOException {
            CachedRowSet crs = getRowSet();
            crs.setCommand("select * from " + getTableName() + " where "
                    + getColumnName(1) + "= '" + name + "'");
            getRowSet().execute(getConnection());
            return getRowSet();
        }

      
    }

    private static final Logger log = org.apache.logging.log4j.LogManager.getLogger(JDBCUserDAO.class);
    private static JDBCDAOPool<JDBCUserDAO> pool;
    private static final String DAO_NAME = "USER";

    private final UserTable LOGINS_TABLE;
    private final UserTable ROLES_TABLE;
    private final UserTable USER_INFO_TABLE;
    private static ServletContext context;

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
    public void close() throws JDBCDAOException {
        try{
        LOGINS_TABLE.getRowSet().release();
        ROLES_TABLE.getRowSet().release();
        USER_INFO_TABLE.getRowSet().release();
        getConnection().setAutoCommit(true);
        pool.put(this);
         } catch (SQLException e) {
            log.error("close exception", e);
            throw new JDBCDAOException(e);
        }
    }

    @Override
    protected JDBCUserDAO newInstance() throws JDBCDAOException {
        return new JDBCUserDAO(context);
    }

    static public JDBCUserDAO getInstance(javax.servlet.ServletContext context) throws JDBCDAOException {
        try {
            JDBCUserDAO instance = null;
            JDBCUserDAO.context = context;
            if (pool == null) {
                synchronized (JDBCUserDAO.class) {
                    if (pool == null) {
                        pool = new JDBCDAOPool<>(new JDBCUserDAO(context));
                    }
                }
            }
            instance = pool.get();
            return instance;
        } catch (Exception e) {
            log.error(e);
            throw new JDBCDAOException(String.format("Exception when getting  JDBC%sDAO instance",DAO_NAME), e);
        }

    }

    private JDBCUserDAO(ServletContext context) throws JDBCDAOException {
        super(context,DAO_NAME);
        String loginsTableName = context.getInitParameter("LOGINS_TABLE");
        String rolesTableName = context.getInitParameter("ROLES_TABLE");
        String userInfoTableName = context.getInitParameter("USER_INFO_TABLE");
        try {
                ConcurrentHashMap<String, List<String>> tablesMetaData=this.initTables();
                LOGINS_TABLE = new UserTable(loginsTableName, getConnection(), tablesMetaData.get(loginsTableName));
                ROLES_TABLE = new UserTable(rolesTableName, getConnection(), tablesMetaData.get(rolesTableName));
                USER_INFO_TABLE = new UserTable(userInfoTableName,  getConnection(), tablesMetaData.get(userInfoTableName));
            
            log.debug("UserDAO created");
        } catch (Exception e) {
            log.error("JDBCUsDAO creation error", e);
            throw new JDBCDAOException("JDBCUsDAO creation error: ", e);
        }

    }

    @Override
    ServletContext getContext() {
        return context;
    }

    @Override
    public User registerUser(User user) throws JDBCDAOException,SQLException {
        try {
            TreeMap<Integer,String> pairs=new TreeMap<>();
            getConnection().setAutoCommit(false);
            pairs.put(1, user.getLogin());
            pairs.put(2, user.getPassword());
            LOGINS_TABLE.addRecord(pairs);
            pairs.put(2,user.getRoles()[0]);
            ROLES_TABLE.addRecord(pairs);
            pairs.put(2, user.getFirstName());
            pairs.put(3, user.getLastName());
            pairs.put(4, user.getEmail());
            USER_INFO_TABLE.addRecord(pairs);
            getConnection().setAutoCommit(true);

        } catch (Exception e) {
            getConnection().rollback();
            if (e.toString().matches(".*Duplicate entry.*for key 'PRIMARY'.*")) {
                
                throw new JDBCDAOException(String.format("Пользователь с loginom: %s уже сушествует.", user.getLogin()));
            } else {
                throw new JDBCDAOException("Ошибка регистрации пользователя.", e);
            }
        }
        finally{getConnection().setAutoCommit(true);}
        return user;
    }

    @Override
    public User readUser(String name) throws Exception {
        User result = null;
        try {
            JoinRowSet jrs = RowSetProvider.newFactory().createJoinRowSet();
            CachedRowSet t = LOGINS_TABLE.readUser(name);
            if (!t.next()) {
                return null;
            }
            jrs.addRowSet(t, 1);
            jrs.addRowSet(ROLES_TABLE.readUser(name), 1);
            jrs.addRowSet(USER_INFO_TABLE.readUser(name), 1);
            Set<String> roles = new TreeSet<>();
            while (jrs.next()) {
                roles.add(jrs.getString(ROLES_TABLE.getColumnName(2)));
            }
            if (jrs.first()) {
                result = new User(jrs.getString(LOGINS_TABLE.getColumnName(1)),
                        jrs.getString(LOGINS_TABLE.getColumnName(2)),
                        roles.toArray(new String[1]),
                        jrs.getString(USER_INFO_TABLE.getColumnName(2)),
                        jrs.getString(USER_INFO_TABLE.getColumnName(3)),
                        jrs.getString(USER_INFO_TABLE.getColumnName(4)));

            }
        } catch (Exception ex) {
            log.error("'readUser error' wrong db tables", ex);

            throw new JDBCDAOException("Reading user Exception: ", ex);
        };
        return result;
    }

    @Override
    public User deleteUser(User user) {
       throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public User changeUser(User user) {
       throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getDAOName() {
        return DAO_NAME;
    }

    @Override
    public ConcurrentSkipListSet<String> readUsersNames() throws JDBCDAOException {
        return LOGINS_TABLE.readColumn(1);
    }

}
