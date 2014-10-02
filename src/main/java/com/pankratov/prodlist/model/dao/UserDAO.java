/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.pankratov.prodlist.model.dao;
import com.pankratov.prodlist.model.dao.jdbc.JDBCUserDAOException;
import com.pankratov.prodlist.model.users.User;
import java.util.concurrent.*;

/**
 *
 * @author pankratov
 */
public interface UserDAO extends AutoCloseable {
    public  User registerUser(User user) throws JDBCUserDAOException;
    public User readUser(String name)throws Exception;
    public User deleteUser(User user);
    public User changeUser(User user);
    public ConcurrentSkipListSet<String> readUsersNames();
}
