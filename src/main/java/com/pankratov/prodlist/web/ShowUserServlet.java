/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.pankratov.prodlist.web;

import com.pankratov.prodlist.model.users.User;
import com.pankratov.prodlist.model.users.UserDAO;
import com.pankratov.prodlist.model.users.UserDAOFactory;
import java.io.IOException;
import java.io.PrintWriter;
import com.pankratov.prodlist.model.mail.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author pankratov
 */
@WebServlet(name = "ShowUserServlet", urlPatterns = {"/ShowUser"}, initParams = {
    @WebInitParam(name = "source", value = "db")})
public class ShowUserServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ShowUserServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ShowUserServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

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
        processRequest(request, response);
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
        
        try{
            new MailAgent(this.getServletContext());
            UserDAO ud=UserDAOFactory.getUserDAOInstance(UserDAOFactory.UserDAOType.JDBCUserDAO, this.getServletContext());
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-16");
            
            User u=ud.readUser(request.getParameter("name"));
           
            if(u!=null){
            response.getWriter().println(u);}else response.getWriter().println("Пользователя с именем: "+request.getParameter("name")+
                    "не существует");}
        catch(Exception ex){throw new ServletException(ex);}
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
