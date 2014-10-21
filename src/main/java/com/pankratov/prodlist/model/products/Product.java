/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pankratov.prodlist.model.products;

import java.util.*;

/**
 *
 * @author pankratov
 */
public class Product {

    private Long id;
    private String name;
    private String subName;
    private String producer;
    private Float value;
    private String valueUnits;
    private String group;
    private Float price;
    private String comment;
    private String author;
    private ArrayList<String> imageLinks = new ArrayList<>();
    private boolean origin = false;
    private Long originID; 

    public Product() {

    }

    public Product(String id, String name, String subName, String producer,
            String value, String valueUnits, String group, String price, String comment, String author,String originID) {
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
        this.originID = originID!=null?new Long(originID):null;
    }
    public Product(Product product,boolean uniqueOnly) {
        this.id = product.id;
        this.name = product.name;
        this.subName = product.subName;
        this.producer = product.producer;
        this.value = product.value;
        if(uniqueOnly){
        this.valueUnits = product.valueUnits;
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
            this.id = (x = initData.get("id")) != null && !x.equals("") ? new Long(x.replace(",", ".")) : null;
        } catch (java.lang.NumberFormatException e) {
        }
        try {
            this.price = (x = initData.get("price")) != null && !x.equals("") ? new Float(x.replace(",", ".")) : null;
        } catch (java.lang.NumberFormatException e) {
        }
        try {
            this.value = (x = initData.get("value")) != null && !x.equals("") ? new Float(x.replace(",", ".")) : null;
        } catch (java.lang.NumberFormatException e) {
        }

        this.name = (x = initData.get("name")) != null && !x.equals("") ? x : null;
        this.subName = (x = initData.get("subName")) != null && !x.equals("") ? x : null;
        this.producer = (x = initData.get("producer")) != null && !x.equals("") ? x : null;
        this.value = (x = initData.get("value")) != null && !x.equals("") ? new Float(x) : null;
        this.valueUnits = (x = initData.get("units")) != null && !x.equals("") ? x : null;
        this.group = (x = initData.get("group")) != null && !x.equals("") ? x : null;
        this.comment = (x = initData.get("comment")) != null && !x.equals("") ? x : null;
        this.author = (x = initData.get("author")) != null && !x.equals("") ? x : null;
        this.origin = (x = initData.get("origin")) != null? true : false;
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
        return group!=null?group.toLowerCase():null;
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
}
