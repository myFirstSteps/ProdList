/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.pankratov.prodlist.web;

import com.pankratov.prodlist.model.dao.DAOFactory;
import com.pankratov.prodlist.model.dao.ProductDAO;
import com.pankratov.prodlist.model.products.Product;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentSkipListSet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author pankratov
 */
public class ProductAutocomplete extends HttpServlet {

   
   
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
         try {
            ConcurrentSkipListSet<String> list = new ConcurrentSkipListSet<>();
            TreeMap<String, String> prodInit = new TreeMap<>();
            String key = "name";

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

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
                }
            }
            JSONObject resp = new JSONObject();
            int i = 1;
            for (String s : list) {
                resp.put(i++, s);
            }
            response.getWriter().println(resp);
        } catch (Exception ex) {
            throw new IOException(ex);
        }
       
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
      response.sendError(405);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
