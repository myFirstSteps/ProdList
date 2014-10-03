/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.pankratov.prodlist.model.users;

import com.pankratov.prodlist.model.dao.jdbc.JDBCDAOException;

/**
 *
 * @author pankratov
 */
public class User {
    private String login;
    private String password;
    private String [] roles;
    private String firstName;
    private String lastName;
    private String email;
    public User (String login, String password, String[] roles, String firstName,String lastname,String email)throws JDBCDAOException{
        try{
        this.login=login;
        this.password=password;
        this.roles=roles;
        this.firstName=firstName;
        this.lastName=lastname;
        this.email=email;
        }catch (Exception ex){ 
            throw new JDBCDAOException("Не удалось создать пользователя",ex); }
    }
    @Override
    public String toString(){
        String role="";
        for(String s:roles){role+=s+'\n';}
        return ("login:"+this.login+'\n'+"Фамилия:"+this.lastName+'\n'+"имя:"+this.firstName+'\n'+"e-mail:"+this.email+'\n'+"роли: "+role+
         '\n'+"пароль: "+this.password);
    }

    /**
     * @param login the login to set
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the roles
     */
    public String[] getRoles() {
        return roles;
    }

    /**
     * @param roles the roles to set
     */
    public void setRoles(String[] roles) {
        this.roles = roles;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }
    public String getLogin(){return login;}
    public String getPassword(){return password;}
    
}
