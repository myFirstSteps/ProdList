package com.pankratov.prodlist.web;

import com.pankratov.prodlist.model.dao.DAOFactory;
import com.pankratov.prodlist.model.dao.ProductDAO;
import com.pankratov.prodlist.model.products.Product;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.apache.logging.log4j.*;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;

//Автодополнение полей формы редактора продуктов
public class ProductAutocomplete extends HttpServlet { 
   
    private static Logger log= org.apache.logging.log4j.LogManager.getLogger(ProductAutocomplete.class);
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
         try { 
           
            ConcurrentSkipListSet<String> list = new ConcurrentSkipListSet<>();
            TreeMap<String, String> prodInit = new TreeMap<>();
            String key = "name";
            response.setContentType("application/json");
            JSONParser parser = new org.json.simple.parser.JSONParser();
            Object reqJSONObj = parser.parse(request.getParameter("term"));
            JSONArray array = (JSONArray) reqJSONObj;

            JSONObject keyObj = (JSONObject) array.get(0);
            for (Object ar : array) {
                JSONObject obj = (JSONObject) ar;
                if (obj == keyObj) {
                    key = (String) obj.get("name");
                }
                prodInit.put((String) obj.get("name"), (String) obj.get("value"));
            }
            Product prod = new Product(prodInit);
            prod.setAuthor((String)request.getSession().getAttribute("client"));
            try (ProductDAO pdao = DAOFactory.getProductDAOInstance(DAOFactory.DAOSource.JDBC, request.getServletContext());) {
                switch (key) {
                    case "name":
                        list = pdao.readProductNames(prod);
                        break;
                    case "subName":
                        list = pdao.readProductSubNames(prod);
                        break;
                    case "producer":
                        list = pdao.readProductProducers(prod);
                        break;
                    case "value":
                        list = pdao.readProductValues(prod);
                        break;    
                }
            }
            JSONObject resp = new JSONObject();
            int i = 1;
            for (String s : list) {
                resp.put(i++, s);
            }
            response.getWriter().println(resp);
        } catch (Exception ex) { 
            log.error(ex);
            JSONObject jsonerr=new JSONObject();
            jsonerr.put("error", ex.toString());
            response.getWriter().println(jsonerr);
        }
       
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
      response.sendError(405);
    }

   

}
