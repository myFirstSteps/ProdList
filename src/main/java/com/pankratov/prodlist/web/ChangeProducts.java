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
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.*;
import org.json.simple.parser.*;

/**
 *
 * @author pankratov
 */
public class ChangeProducts extends HttpServlet {

   
   

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
        try(ProductDAO pdao= DAOFactory.getProductDAOInstance(DAOFactory.DAOSource.JDBC, request.getServletContext())){
            Product p= Product.getInstanceFromJSON(request);
            pdao.changeProduct(p);
      /* JSONParser par=new JSONParser();
       System.out.println(request.getParameter("term"));
       JSONArray a=(JSONArray)par.parse(request.getParameter("term"));
       JSONObject o=(JSONObject)a.get(0);
       Set<Map.Entry> s=o.entrySet();
        for(Map.Entry e:s){
            System.out.println(String.format("name: %s \n value: %s",e.getKey(), e.getValue()));
        }*/
            
        
        }catch(Exception ex){}
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
