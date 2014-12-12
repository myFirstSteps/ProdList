package com.pankratov.prodlist.model.list;

import com.pankratov.prodlist.model.dao.ProductDAO;
import com.pankratov.prodlist.model.dao.ProductDAO.KindOfProduct;
import static com.pankratov.prodlist.model.dao.ProductDAO.KindOfProduct.*;
import com.pankratov.prodlist.model.products.Product;
import java.text.*;
import java.util.*;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ProdList {

    private Long id = -1l;
    private String name = "";
    private String products = "";
    private String timeStamp = "";
    private String ownerName = "";
    private String checked="";
    private Date time=new Date();

    public ProdList(TreeMap<String, String> initData) {
        String x;
        try {
            this.id = (x = initData.get("id")) != null ? new Long(x.replace(",", ".")) : -1;
        } catch (java.lang.NumberFormatException e) {
            this.id = -1l;
        }
        this.products = (x = initData.get("products")) != null ? x : "";
        this.name = (x = initData.get("name")) != null ? x : "";
        setTimeStamp((x = initData.get("timeStamp")) != null ? x : "");
        this.ownerName = (x = initData.get("ownerName")) != null ? x : "";
        this.checked = (x = initData.get("checked")) != null ? x : "";

    }
    public ProdList(){
        
    }
    public ProdList(String name,String owner){
        this.name=name!=null?name:"";
        this.ownerName=owner!=null?owner:"";
    }

    public static ProdList getInstanceFromJSON(HttpServletRequest request) throws Exception {
        JSONParser par = new JSONParser();
        JSONObject a = (JSONObject) par.parse(request.getParameter("list"));
        TreeMap<String, String> listInit = new TreeMap<>();
        listInit.put("name",(String)a.get("name"));
        listInit.put("products",(String)a.get("items"));
        listInit.put("ownerName",
               (String) request.getSession().getAttribute("client"));
        return new ProdList(listInit);
    }
    public JSONObject toJSON(ProductDAO productSource)throws Exception{
        JSONObject prodlist=new JSONObject();
        Product res;
        prodlist.put("name", name);
        prodlist.put("id", id);
        prodlist.put("ownerName", ownerName);
        prodlist.put("timeStamp",timeStamp);
        JSONArray prods=new JSONArray();
        String[] products=this.products.split(" ");
        int i=1;
        for(String p:products){
            JSONObject single=new JSONObject();
            String item[]=p.split("_");
            KindOfProduct kind=item[2].equals("o")?ORIGINAL:USER_COPY;
            Product product=new Product();
            product.setId(new Long(item[0]));
             res=productSource.readProduct(product, kind);
            if( res==null&&kind==USER_COPY){ 
                product=new Product();
                product.setId(Long.parseLong(item[1]));
                res=productSource.readProduct(product, ORIGINAL);
                if (res==null){ 
                     single.put("key", "0");
                     single.put("value", String.format("%d. Продукт был удалён!",i++));
                    prods.add(single);
                    continue;
                }
            }
                 String temp;
                 String key=res.getId()+"_"+res.getOriginID()+(res.isOrigin()?"_o":"");
                 single.put("key", key);
                 single.put("value", String.format("%d. %s %s %s    %dшт.",
                         i,res.getName(),
                         (temp=res.getSubName()).equals("любой")?"":temp,
                         (temp=res.getProducer()).equals("любой")?"":temp, 
                         
                  new Integer(item[item.length-1])       
                 ));
                 prods.add(single);
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
        this.timeStamp =  timeStamp;
        try {
            this.time = java.sql.Timestamp.valueOf( timeStamp);
            this.timeStamp = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM,
                    new Locale("ru","RU")).format(this.time);
        } catch (IllegalArgumentException e) {
        }
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
