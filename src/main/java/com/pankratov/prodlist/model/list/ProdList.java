/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pankratov.prodlist.model.list;

import com.pankratov.prodlist.model.dao.ProductDAO;
import com.pankratov.prodlist.model.dao.ProductDAO.KindOfProduct;
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
    private String checked="";

    public ProdList(TreeMap<String, String> initData) {
        String x;
        try {
            this.id = (x = initData.get("id")) != null ? new Long(x.replace(",", ".")) : -1;
        } catch (java.lang.NumberFormatException e) {
            this.id = -1l;
        }
        this.products = (x = initData.get("products")) != null ? x : "";
        this.name = (x = initData.get("name")) != null ? x : "";
        this.timeStamp = (x = initData.get("timeStamp")) != null ? x : "";
        this.ownerName = (x = initData.get("ownerName")) != null ? x : "";
        this.checked = (x = initData.get("checked")) != null ? x : "";

    }
    public ProdList(){
        
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
    public JSONObject toJSON(ProductDAO productSource)throws Exception{
        JSONObject prodlist=new JSONObject();
        prodlist.put("name", name);
        prodlist.put("id", id);
        prodlist.put("ownerName", ownerName);
        prodlist.put("timeStamp",timeStamp);
        JSONArray prods=new JSONArray();
        String[] products=this.products.split(" ");
        int i=1;
        for(String p:products){
            String item[]=p.split("_");
            KindOfProduct kind=item[0].contains("o")?ProductDAO.KindOfProduct.ORIGINAL:ProductDAO.KindOfProduct.USER_COPY;
            Product product=new Product();
            product.setId(new Long(item[0].replaceAll("o", "")));
            List<Product> pl=productSource.readProducts(product, kind);
            if(pl.size()<1) continue ;
                 product= pl.get(0);
                 String temp;
                 prods.add(String.format("%d. %s %s %s %.2fруб.   %dшт.",
                         i,product.getName(),
                         (temp=product.getSubName()).equals("любой")?"":temp,
                         (temp=product.getProducer()).equals("любой")?"":temp,
                  new Integer(item[1])       
                 ));
                 i++;
        }
        prodlist.put("products",prods);
        return prodlist;
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
    
    @Override
    public String toString(){
        return String.format("Prodlist:%s\nid:%s\nproducts:%s\nowner:%s\ncreated:%s", name,id.toString(),products,ownerName,timeStamp);
    }

    /**
     * @return the checked
     */
    public String getChecked() {
        return checked;
    }

    /**
     * @param checked the checked to set
     */
    public void setChecked(String checked) {
        this.checked = checked;
    }

}
