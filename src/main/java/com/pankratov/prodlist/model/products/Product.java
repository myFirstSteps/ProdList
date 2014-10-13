/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.pankratov.prodlist.model.products;

/**
 *
 * @author pankratov
 */
public class Product {
    private long id;
    private String name;
    private String subname;
    private String producer;
    private float value;
    private String valueUnits;
    private String group;
    private float price;
    private String comment;

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
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
    public String getSubname() {
        return subname;
    }

    /**
     * @param subname the subname to set
     */
    public void setSubname(String subname) {
        this.subname = subname;
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
    public float getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(float value) {
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
        return group;
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
    public float getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(float price) {
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
    public String toString(){
      return String.format("id:%d\nНазвание:%s\nУточняющее название:%s\nПроизводитель:%s\n"
              + "Объем:%7.2f\nЕдиници объема: %s\nЦена:%7.2f\nКомментарий:%s", id,
              name!=null?name:"null", subname!=null?subname:"Любой(-ая,-ое)",producer!=null?producer:"Любой(-ая,-ое)",
              value, valueUnits!=null?valueUnits:"null",price,comment!=null?comment:"") ; 
    }
}
