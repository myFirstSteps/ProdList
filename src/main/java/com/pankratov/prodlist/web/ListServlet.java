/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.pankratov.prodlist.web;

import com.pankratov.prodlist.model.dao.DAOFactory;
import com.pankratov.prodlist.model.dao.ProdListDAO;
import com.pankratov.prodlist.model.dao.ProductDAO;
import com.pankratov.prodlist.model.list.ProdList;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author pankratov
 */
public class ListServlet extends HttpServlet {

    

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
        response.sendError(405);
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
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JSONObject json= new JSONObject();
        try(ProdListDAO pldao=  DAOFactory.getProdListDAOInstance(DAOFactory.DAOSource.JDBC, request.getServletContext());
            ProductDAO pdao=DAOFactory.getProductDAOInstance(DAOFactory.DAOSource.JDBC, request.getServletContext());    ){
        switch(request.getParameter("action")){
            case "save": if(pldao.addProdList(ProdList.getInstanceFromJSON(request))){
                response.getWriter().println(json);
            }
            break;
            case "show":  
              String listName= request.getParameter("listName");
              ProdList plist=new ProdList();
              plist.setName(listName);
              json.put("list",pldao.readProdLists(plist).get(0).toJSON(pdao));
                response.getWriter().println(json);
        }
        }
        
        catch (Exception e){System.out.println("cc"+e); json.put("error", e.getMessage()); response.getWriter().println(json);}
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