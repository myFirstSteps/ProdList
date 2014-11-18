/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pankratov.prodlist.model.list;

import com.pankratov.prodlist.model.products.Product;
import java.util.*;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author pankratov
 */
public class ProdList {

    private Long id = -1l;
    private String name = "";
    private String products = "";
    private String timeStamp = "";
    private String ownerName = "";

    public ProdList(TreeMap<String, String> initData) {
        String x;
        try {
            this.id = (x = initData.get("id")) != null ? new Long(x.replace(",", ".")) : -1;
        } catch (java.lang.NumberFormatException e) {
            this.id = -1l;
        }

        this.name = (x = initData.get("name")) != null ? x : "";
        this.timeStamp = (x = initData.get("timeStamp")) != null ? x : "";
        this.ownerName = (x = initData.get("ownerName")) != null ? x : "";

    }

    public static ProdList getInstanceFromJSON(HttpServletRequest request) throws Exception {
        JSONParser par = new JSONParser();
        JSONObject a = (JSONObject) par.parse(request.getParameter("list"));
        TreeMap<String, String> listInit = new TreeMap<>();
        listInit.put("name",(String)a.get("name"));
        listInit.put("products",(String)a.get("items"));
        listInit.put("ownerName",
                request.getRemoteUser() != null ? request.getRemoteUser() : (String) request.getSession().getAttribute("clid"));
        return new ProdList(listInit);
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the products
     */
    public String getProducts() {
        return products;
    }

    /**
     * @param products the products to set
     */
    public void setProducts(String itemKeys) {
        this.products = products;
    }

    /**
     * @return the timeStamp
     */
    public String getTimeStamp() {
        return timeStamp;
    }

    /**
     * @param timeStamp the timeStamp to set
     */
    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    /**
     * @return the ownerName
     */
    public String getOwnerName() {
        return ownerName;
    }

    /**
     * @param ownerName the ownerName to set
     */
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    /**
     * @return the ID
     */
    public Long getID() {
        return id;
    }

    /**
     * @param ID the ID to set
     */
    public void setID(Long id) {
        this.id = id;
    }

}
