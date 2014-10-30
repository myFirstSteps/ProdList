/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pankratov.prodlist.model.products;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItem;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author pankratov
 */
public class Product {

    private Long id=-1l;
    private String name="";
    private String subName="";
    private String producer="";
    private Float value=-1f;
    private String valueUnits="";
    private String group="";
    private Float price=-1f;
    private String comment="";
    private String lastModify="";
    private String author="";
    private String authorRole="";
    private ArrayList<String> imageLinks = new ArrayList<>();
    private boolean origin=true;
    private Long originID=-1l;

    /**
     * @return the lastModify
     */
    public String getLastModify() {
        return lastModify;
    }

    /**
     * @param lastModify the lastModify to set
     */
    public void setLastModify(String lastModify) {
        this.lastModify = lastModify;
    }

   

    private static class ProductFieldsRiper {

        static TreeMap<String, String> ripFields(List<FileItem> f) throws UnsupportedEncodingException {
            TreeMap<String, String> prodInitData = new TreeMap<>();
            for (FileItem i : f) {
                if (i.isFormField() && isProductField(i.getFieldName())) {
                    prodInitData.put(i.getFieldName(), i.getString("UTF-8"));
                }
            }
            return prodInitData;
        }

        static TreeMap<String, String> ripFields(Map<String, String[]> m) {
            TreeMap<String, String> prodInitData = new TreeMap<>();
            for (Map.Entry<String, String[]> i : m.entrySet()) {
                if (isProductField(i.getKey())) {
                    prodInitData.put(i.getKey(), i.getValue()[0]);
                }
            }
            return prodInitData;
        }

        static boolean isProductField(String fieldName) {
            switch (fieldName) {
                case "group":
                case "name":
                case "subName":
                case "producer":
                case "value":
                case "valueUnits":
                case "price":
                case "comment":
                    return true;
            }
            return false;
        }

        static String readAuthor(HttpServletRequest req) {
            String author = req.getRemoteUser() != null ? req.getRemoteUser() : (String) req.getSession().getAttribute("clid");
            return author;
        }

        static String readAuthorRole(HttpServletRequest req) {
            if(req.isUserInRole("admin"))return "admin";
            if(req.isUserInRole("level1"))return "level1";
            return "";

        }
    }

    public Product() {

    }

    public Product(String id, String name, String subName, String producer,
            String value, String valueUnits, String group, String price, String comment, String author, String originID) {
        this.id = new Long(id);
        this.name = name;
        this.subName = subName;
        this.producer = producer;
        this.value = new Float(value);
        this.valueUnits = valueUnits;
        this.group = group;
        this.price = new Float(price);
        this.comment = comment;
        this.author = author;
        this.originID = originID != null ? new Long(originID) : null;
    }

    public Product(Product product, boolean onlyKeyFields) {
        this.id = product.id;
        this.name = product.name;
        this.subName = product.subName;
        this.producer = product.producer;
        this.value = product.value;
        this.valueUnits = product.valueUnits;
        if (!onlyKeyFields) {
            this.group = product.group;
            this.price = product.price;
            this.comment = product.comment;
            this.author = product.author;
            this.originID = product.originID;
        }
    }

    public Product(TreeMap<String, String> initData) {
        String x;
        try {
            this.id = (x = initData.get("id")) != null? new Long(x.replace(",", ".")) : -1;
        } catch (java.lang.NumberFormatException e) { this.id=-1l;
        }
        try {
            this.price = (x = initData.get("price")) != null && !x.equals("") ? new Float(x.replace(",", ".")) : -1;
        } catch (java.lang.NumberFormatException e) { this.price=-1f;
        }
        try {
            this.value = (x = initData.get("value")) != null && !x.equals("") ? new Float(x.replace(",", ".")) : -1;
        } catch (java.lang.NumberFormatException e) { this.value=-1f;
        }

        this.name = (x = initData.get("name")) != null ? x : "";
        this.subName = (x = initData.get("subName")) != null ? x : "";
        this.producer = (x = initData.get("producer")) != null ? x : ""; 
        this.valueUnits = (x = initData.get("valueUnits")) != null ? x : "";
        this.group = (x = initData.get("group")) != null ? x : "";
        this.comment = (x = initData.get("comment")) != null ? x : "";
        this.author = (x = initData.get("author")) != null ? x : "";
        this.authorRole = (x = initData.get("authorRole")) != null ? x : "";
        this.originID=(x = initData.get("originID")) != null ? new Long(x): -1;
        this.origin = ((x = initData.get("origin")) != null && x.equalsIgnoreCase("false"))||originID!=-1 ? false:true;
        this.lastModify=(x = initData.get("lastModify")) != null ? x : "";
    }
    public static Product getInstanceFromJSON(HttpServletRequest request)throws Exception{
        JSONParser par=new JSONParser();
       JSONArray a=(JSONArray)par.parse(request.getParameter("product"));
       JSONObject o=(JSONObject)a.get(0);
        TreeMap<String, String> prodInit = new TreeMap<>();
        for(Map.Entry<String,String> m:(Set<Map.Entry<String,String>>)o.entrySet()){
            prodInit.put(m.getKey(), m.getValue());
        }
        prodInit.put("author", ProductFieldsRiper.readAuthor(request));
        prodInit.put("authorRole", ProductFieldsRiper.readAuthorRole(request));
        return new Product(prodInit);
    }

    public static Product getInstanceFromRequest(HttpServletRequest req) {
        TreeMap<String, String> prodInit = new TreeMap<>();
        prodInit =  ProductFieldsRiper.ripFields(req.getParameterMap());
        prodInit.put("author", ProductFieldsRiper.readAuthor(req));
        prodInit.put("authorRole", ProductFieldsRiper.readAuthorRole(req));

        return new Product(prodInit);
    }

    public static Product getInstanceFromFormFields(List<FileItem> fields, HttpServletRequest req) throws UnsupportedEncodingException {
        TreeMap<String, String> prodInit = new TreeMap<>();
        prodInit = ProductFieldsRiper.ripFields(fields);
        prodInit.put("author", ProductFieldsRiper.readAuthor(req));
        prodInit.put("authorRole", ProductFieldsRiper.readAuthorRole(req));
        return new Product(prodInit);
    }
    public JSONObject toJSON(){
        JSONObject json=new JSONObject();
        json.put("id", id);
        json.put("name", name);
        json.put("subName",subName);
        json.put("producer", producer);
        json.put("value", value);
        json.put("valueUnits", valueUnits);
        json.put("group",group);
        json.put("price", price);
        json.put("comment", comment);
        json.put("lastModify", lastModify);
        json.put("author", author);
        json.put("authorRole", authorRole);
        json.put("origin", origin);
        json.put("originID", originID);
        json.put("imageLinks", imageLinks);
        return json;
    } 

    
    
    
    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
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
     * @return the subname
     */
    public String getSubName() {
        return subName;
    }

    /**
     * @param subName the subname to set
     */
    public void setSubName(String subName) {
        this.subName = subName;
    }

    /**
     * @return the producer
     */
    public String getProducer() {
        return producer;
    }

    /**
     * @param producer the producer to set
     */
    public void setProducer(String producer) {
        this.producer = producer;
    }

    /**
     * @return the value
     */
    public Float getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(Float value) {
        this.value = value;
    }

    /**
     * @return the valueUnits
     */
    public String getValueUnits() {
        return valueUnits;
    }

    /**
     * @param valueUnits the valueUnits to set
     */
    public void setValueUnits(String valueUnits) {
        this.valueUnits = valueUnits;
    }

    /**
     * @return the group
     */
    public String getGroup() {
        return  group.toLowerCase();
    }

    /**
     * @param group the group to set
     */
    public void setGroup(String group) {
        this.group = group;
    }

    /**
     * @return the price
     */
    public Float getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(Float price) {
        this.price = price;
    }

    /**
     * @return the comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param comment the comment to set
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return String.format("id:%d\nНазвание:%s\nУточняющее название:%s\nПроизводитель:%s\n"
                + "Объем:%7.2f\nЕдиници объема: %s\nГруппа: %s\nЦена:%7.2f\nКомментарий:%s\nДобавил:%s", id,
                name != null ? name : "null", subName != null ? subName : "Любой(-ая,-ое)", producer != null ? producer : "Любой(-ая,-ое)",
                value, valueUnits != null ? valueUnits : "null", group != null ? group.toLowerCase() : "null", price, comment != null ? comment : "", author != null ? author : "гость");
    }

    /**
     * @return the author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @param author the author to set
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * @return the imageLinks
     */
    public ArrayList<String> getImageLinks() {
        return imageLinks;
    }

    /**
     * @param imageLinks the imageLinks to set
     */
    public void setImageLinks(ArrayList<String> imageLinks) {
        this.imageLinks = imageLinks;
    }

    /**
     * @return the origin
     */
    public boolean isOrigin() {
        return origin;
    }

    /**
     * @param origin the origin to set
     */
    public void setOrigin(boolean origin) {
        this.origin = origin;
    }

    /**
     * @return the originID
     */
    public Long getOriginID() {
        return originID;
    }

    /**
     * @param originID the originID to set
     */
    public void setOriginID(Long originID) {
        this.originID = originID;
    }
     /**
     * @return the authorRole
     */
    public String getAuthorRole() {
        return authorRole;
    }

    /**
     * @param authorRole the authorRole to set
     */
    public void setAuthorRole(String authorRole) {
        this.authorRole = authorRole;
    }
}
