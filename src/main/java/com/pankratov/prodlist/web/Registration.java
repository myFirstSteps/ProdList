/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pankratov.prodlist.web;

import com.pankratov.prodlist.model.users.JDBCUserDAOException;
import com.pankratov.prodlist.model.users.User;
import com.pankratov.prodlist.model.users.UserDAO;
import com.pankratov.prodlist.model.users.UserDAOFactory;
import com.pankratov.prodlist.model.mail.MailAgent;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import org.apache.logging.log4j.*;
import org.apache.logging.log4j.*;
import org.apache.commons.mail.*;

/**
 *
 * @author pankratov
 */
public class Registration extends HttpServlet {

    private static final Logger log = LogManager.getLogger(Registration.class);

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
            out.println("<title>Servlet Registration</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Registration at " + request.getContextPath() + "</h1>");
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
        String is = "free";
        ServletContext context = this.getServletContext();
        try {
            UserDAO ud = UserDAOFactory.getUserDAOInstance(UserDAOFactory.UserDAOType.JDBCUserDAO, context);
            if ((ud.isUserExsists(request.getParameter("name")))) {
                is = "busy";
            }
        } catch (Exception e) {
            log.error("Проверка logina", e);
        }
        response.setContentType("text/plain");
        response.getWriter().print("login is " + is);
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
        request.setCharacterEncoding("UTF8");
        String login = request.getParameter("login");
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        String lastName = request.getParameter("family");
        String email = request.getParameter("e-mail");
        request.getSession().setAttribute("regData", new String[]{login, name, lastName, email});
        if (login.equals("") || password.equals("") || true) {
            request.getAttributeNames().nextElement();
            request.setAttribute("error", "Значение поля " + ((login.equals("")) ? "login" : "пароль") + " не может быть пустым.");
        }
       
       User user=new User(login, password, new String[]{"level1"}, name, lastName, email);
        user=UserDAOFactory.getUserDAOInstance(UserDAOFactory.UserDAOType.JDBCUserDAO, this.getServletContext()).registerUser(user);
        
        if (user!=null){
            request.setAttribute("user", user);
            request.setAttribute("mailType","registration");
        }
        response.setCharacterEncoding("UTF-8");
      }catch(JDBCUserDAOException ex){log.error("Ошибка создания пользователя", ex); System.out.println(ex);}
      catch(Exception e){log.error("Ошибка регистрации пользователя",e); System.out.println(e);}
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
