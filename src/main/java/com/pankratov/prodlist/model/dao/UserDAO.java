package com.pankratov.prodlist.model.dao;
import com.pankratov.prodlist.model.dao.jdbc.JDBCDAOException;
import com.pankratov.prodlist.model.users.User;
import java.util.concurrent.*;

public interface UserDAO extends AutoCloseable {
    public  User registerUser(User user) throws Exception;
    public User readUser(String name)throws Exception;
    public User deleteUser(User user) throws Exception;
    public User changeUser(User user) throws Exception;
    public ConcurrentSkipListSet<String> readUsersNames()throws Exception;
}
