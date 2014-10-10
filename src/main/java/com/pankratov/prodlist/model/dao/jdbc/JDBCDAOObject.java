/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.pankratov.prodlist.model.dao.jdbc;

import java.sql.Connection;
import javax.servlet.ServletContext;

/**
 *
 * @author pankratov
 */
//Интерфейс маркер
public abstract class JDBCDAOObject implements AutoCloseable {
    abstract protected JDBCDAOObject newInstance()throws Exception; 
     private long offerTime = System.currentTimeMillis();
     private static final String dAOName="JDBCDAOObject";
     private boolean pensioner = false;
    /**
     * @return the offerTime
     */
    protected  JDBCDAOObject() {
      
    } 
    public long getOfferTime() {
        return offerTime;
    }
    abstract ServletContext getContext();

    /**
     * @param offerTime the offerTime to set
     */
    public void setOfferTime(long offerTime) {
        this.offerTime = offerTime;
    }
    abstract Connection getConnection();

    /**
     * @return the pensioner
     */
    public boolean isPensioner() {
        return pensioner;
    }

    /**
     * @param pensioner the pensioner to set
     */
    public void setPensioner(boolean pensioner) {
        this.pensioner = pensioner;
    }
    abstract public String getDAOName();
}