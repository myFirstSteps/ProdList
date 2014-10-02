/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pankratov.prodlist.model.dao.jdbc;

import com.pankratov.prodlist.model.dao.UserDAO;
import com.pankratov.prodlist.model.users.User;
import com.sun.rowset.CachedRowSetImpl;
import java.sql.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import javax.servlet.ServletContext;
import javax.sql.rowset.*;
import javax.sql.rowset.RowSetProvider;
import org.apache.logging.log4j.*;

/**
 *
 * @author pankratov
 */
public class JDBCUserDAO extends JDBCDAOObject implements UserDAO, AutoCloseable {

    private class Table {

        private CachedRowSetImpl rowset;
        private String tableName;
        private List<String> columnNames;

        private Table(String tableName, List<String> colNames) throws JDBCUserDAOException {
            try {
                rowset = new CachedRowSetImpl();

                rowset.setUrl(DB_NAME);
                rowset.setPassword(DB_PASSWORD);
                rowset.setUsername(DB_LOGIN);
                this.tableName = tableName;
                columnNames = colNames;
                rowset.setCommand("Select * from " + tableName + " where " + columnNames.get(0) + "=null");
                rowset.execute();
                //  log.debug(String.format("created table %s with columns: %s rowset:%s", tableName, columnNames, rowset));

            } catch (Exception e) {
                log.error("Table creation issue (ex): " + e);
                throw new JDBCUserDAOException("Ошибка при создании:" + Table.class + "для: " + tableName, e);
            }

        }

        private CachedRowSet readUser(String name) throws SQLException {
            rowset.setCommand("select * from " + tableName + " where "
                    + columnNames.get(0) + "= '" + name + "'");
            rowset.execute();
            return rowset;
        }

        private boolean addUser(String... s) throws Exception {
            int i = 1;
            String params = "";
            for (int j = 0; j < s.length; j++) {
                params += ", ?";
            }
            params = params.replaceAll("^, ", "");
            PreparedStatement stat = connection.prepareStatement(String.format("Insert into %s values(%s)", tableName, params));
            for (String st : s) {
                stat.setString(i++, st);

            }

            stat.execute();

            return true;
        }

        private CachedRowSet getAll() throws SQLException {
            rowset.setCommand("select * from " + tableName);
            rowset.execute();
            return rowset;
        }

        private String getColumnName(int numb) throws JDBCUserDAOException {
            String res = null;

            try {
                res = columnNames.get(numb - 1);
            } catch (IndexOutOfBoundsException e) {
                throw new JDBCUserDAOException(String.format("У таблици %s нет столбца с индексом %d", tableName, numb), e);
            }
            return res;
        }
    }

    private static final Logger log = org.apache.logging.log4j.LogManager.getLogger(JDBCUserDAO.class);
    private static JDBCDAOPool<JDBCUserDAO> pool;
    private final String DB_NAME;
    private final String DB_LOGIN;
    private final String DB_PASSWORD;
    private Table LOGINS_TABLE;
    private Table ROLES_TABLE;
    private Table USER_INFO_TABLE;
    private Connection connection;
    private static ServletContext context;

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        try {
            connection.close();
        } finally {
            log.debug("Ripped" + this);
        }
    }

    @Override
    Connection getConnection() {
        return connection;
    }

    @Override
    public void close() throws SQLException, InterruptedException {
        System.out.println("in close");
        LOGINS_TABLE.rowset.release();
        ROLES_TABLE.rowset.release();
        USER_INFO_TABLE.rowset.release();
        pool.put(this);
    }

    @Override
    protected JDBCUserDAO newInstance() throws JDBCUserDAOException {
        return new JDBCUserDAO(context);
    }

    static public JDBCUserDAO getInstance(javax.servlet.ServletContext context) throws Exception {
        try {
            System.out.println("Come to get instance");
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
            throw new JDBCUserDAOException("Exception when getting  JDBCUsDAO instance", e);
        }

    }

    private JDBCUserDAO(ServletContext context) throws JDBCUserDAOException {
        super("USER");
        DB_NAME = context.getInitParameter("DB_NAME");
        DB_LOGIN = context.getInitParameter("DB_LOGIN");
        DB_PASSWORD = context.getInitParameter("DB_PASSWORD");
        String loginsTableName = context.getInitParameter("LOGINS_TABLE");
        String rolesTableName = context.getInitParameter("ROLES_TABLE");
        String userInfoTableName = context.getInitParameter("USER_INFO_TABLE");
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
                LOGINS_TABLE = new Table(loginsTableName, m.get(loginsTableName));
                ROLES_TABLE = new Table(rolesTableName, m.get(rolesTableName));
                USER_INFO_TABLE = new Table(userInfoTableName, m.get(userInfoTableName));
            }
            log.debug("UserDAO created");
        } catch (Exception e) {
            log.error("JDBCUsDAO creation error", e);
            throw new JDBCUserDAOException("JDBCUsDAO creation error: ", e);
        }

    }

    @Override
    ServletContext getContext() {
        return context;
    }

    @Override
    @SuppressWarnings("empty-statement")
    public User registerUser(User user) throws JDBCUserDAOException {
        try {

            LOGINS_TABLE.addUser(user.getLogin(), user.getPassword());
            ROLES_TABLE.addUser(user.getLogin(), user.getRoles()[0]);
            USER_INFO_TABLE.addUser(user.getLogin(), user.getFirstName(), user.getLastName(), user.getEmail());
            connection.commit();

        } catch (Exception e) {
            if (e.toString().matches(".*Duplicate entry.*for key 'PRIMARY'.*")) {
                throw new JDBCUserDAOException(String.format("Пользователь с loginom: %s уже сушествует.", user.getLogin()));
            } else {
                throw new JDBCUserDAOException("Ошибка регистрации пользователя.", e);
            }
        }
        return user;
    }

    @Override
    @SuppressWarnings("empty-statement")
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

            throw new JDBCUserDAOException("Reading user Exception: ", ex);
        };
        return result;
    }

    @Override
    public User deleteUser(User user) {
        return null;
    }

    @Override
    public User changeUser(User user) {
        return null;
    }

    @Override
    public ConcurrentSkipListSet<String> readUsersNames() {
        ConcurrentSkipListSet<String> result = new ConcurrentSkipListSet<>();
        try (Statement st = connection.createStatement();) {
            ResultSet res = st.executeQuery(String.format("select %s from %s", LOGINS_TABLE.columnNames.get(0), LOGINS_TABLE.tableName));
            while (res.next()) {
                result.add(res.getString(1));
            }
        } catch (SQLException ex) {
            log.error("readUserNamesError", ex);

        }
        return result;
    }

}
