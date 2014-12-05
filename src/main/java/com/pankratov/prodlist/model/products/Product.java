
package com.pankratov.prodlist.model.products;

import java.io.UnsupportedEncodingException;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItem;
import org.apache.logging.log4j.*;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;

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
    private LinkedList<String> imageLinks = new LinkedList<>();
    private boolean origin=true;
    private Long originID=-1l;

    private static final Logger log=LogManager.getLogger(Product.class);
    public String getLastModify() {
        return lastModify;
    }

  
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
    public Product(Long id){
        this.id=id;
    }
    public Product(Long id,Long originID){
        this(id);
        this.originID=originID;
        origin=false;
    }
    public Product(Long id,Long originID, String author){
        this(id,originID);
        this.author=author;
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
    public static Product getInstanceFromJSON(HttpServletRequest request) throws ProductException {
       try{JSONParser par=new JSONParser();
       JSONArray a=(JSONArray)par.parse(request.getParameter("product"));
       JSONObject o=(JSONObject)a.get(0);
        TreeMap<String, String> prodInit = new TreeMap<>();
        for(Map.Entry<String,String> m:(Set<Map.Entry<String,String>>)o.entrySet()){
            prodInit.put(m.getKey(), m.getValue());
        }
        prodInit.put("author", ProductFieldsRiper.readAuthor(request));
        prodInit.put("authorRole", ProductFieldsRiper.readAuthorRole(request));
        return new Product(prodInit);}catch(Exception e){
            log.error("Error on ProductfromJSON", e);
            throw new ProductException("Ошибка при создании продукта из JSON request",e);
        }
    }

    public static Product getInstanceFromRequest(HttpServletRequest req) {
        TreeMap<String, String> prodInit  =  ProductFieldsRiper.ripFields(req.getParameterMap());
        prodInit.put("author", ProductFieldsRiper.readAuthor(req));
        prodInit.put("authorRole", ProductFieldsRiper.readAuthorRole(req));

        return new Product(prodInit);
    }

    public static Product getInstanceFromFormFields(List<FileItem> fields, HttpServletRequest req) throws  ProductException {
        try{
        TreeMap<String, String> prodInit  = ProductFieldsRiper.ripFields(fields);
        prodInit.put("author", ProductFieldsRiper.readAuthor(req));
        prodInit.put("authorRole", ProductFieldsRiper.readAuthorRole(req));
        return new Product(prodInit);
        }catch(Exception e){
            log.error("Error on ProductfromFormFields", e);
            throw new ProductException("Ошибка при создании продукта из FormFields request",e);
        }
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public String getValueUnits() {
        return valueUnits;
    }

    public void setValueUnits(String valueUnits) {
        this.valueUnits = valueUnits;
    }

    public String getGroup() {
        return  group.toLowerCase();
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getComment() {
        return comment;
    }

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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LinkedList<String> getImageLinks() {
        return imageLinks;
    }

    public void setImageLinks(LinkedList<String> imageLinks) {
        this.imageLinks = imageLinks;
    }

    public boolean isOrigin() {
        return origin;
    }

    public void setOrigin(boolean origin) {
        this.origin = origin;
    }

    public Long getOriginID() {
        return originID;
    }

    public void setOriginID(Long originID) {
        if(originID!=-1){origin=false;}
        this.originID = originID;
    }

    public String getAuthorRole() {
        return authorRole;
    }

    public void setAuthorRole(String authorRole) {
        this.authorRole = authorRole;
    }
}
