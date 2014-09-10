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
public class User {
    private String name;
    private String password;
    private String [] roles;
    public String getName(){return name;}
    public String getPassword(){return password;}
    public User (String...s)throws UserCreationException{
        try{
        name=s[0];
        password=s[1];
        }catch (Throwable tr){ 
            UserCreationException e =new UserCreationException();
            e.initCause(tr);
            throw e; }
    }
    
}
