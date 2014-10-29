/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.pankratov.prodlist.web;

import com.pankratov.prodlist.model.dao.DAOFactory;
import com.pankratov.prodlist.model.dao.ProductDAO;
import com.pankratov.prodlist.model.dao.jdbc.JDBCDAOException;
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
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        try(ProductDAO pdao= DAOFactory.getProductDAOInstance(DAOFactory.DAOSource.JDBC, request.getServletContext())){
            Product p= Product.getInstanceFromJSON(request);
           p=pdao.changeProduct(p);
           response.getWriter().println(p.toJSON());    
        }catch(Exception ex){
            JSONObject jsonerr=new JSONObject();
            String couse=ex.getMessage();
            if(ex.getMessage().contains("Data truncation: Out of range"))jsonerr.put("error", "Введено слишком большое число.");
            if(ex.getMessage().contains("Ни одна запись не изменена"))jsonerr.put("error", "Запись не изменена");
            if(jsonerr.size()==0)jsonerr.put("error", "Во время редактирования записи произошла ошибка");
            response.getWriter().println(jsonerr); }
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
