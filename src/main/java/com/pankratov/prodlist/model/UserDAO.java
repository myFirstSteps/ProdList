/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.pankratov.prodlist.model;

/**
 *
 * @author pankratov
 */
public interface UserDAO {
    public  boolean registerUser(User user);
    public User readUser(String name)throws Exception;
    public User deleteUser(User user);
    public User changeUser(User user);
}