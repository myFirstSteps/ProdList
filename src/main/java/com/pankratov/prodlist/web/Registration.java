/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pankratov.prodlist.web;

import com.pankratov.prodlist.model.dao.DAOFactory;
import com.pankratov.prodlist.model.dao.DAOFactory.DAOSource;
import com.pankratov.prodlist.model.mail.MailAgent;
import com.pankratov.prodlist.model.dao.jdbc.JDBCDAOException;
import com.pankratov.prodlist.model.users.User;
import com.pankratov.prodlist.model.dao.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.*;
import javax.servlet.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import org.apache.commons.mail.*;
import org.apache.logging.log4j.*;


/**
 *
 * @author pankratov
 */
public class Registration extends HttpServlet {

    private static final Logger log = LogManager.getLogger(Registration.class);
    private static ConcurrentSkipListSet<String> logins;

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
        String result = "";
        ServletContext context = this.getServletContext();
        if (logins == null) {
            try (UserDAO ud = DAOFactory.getUserDAOInstance(DAOSource.JDBC, context);) {
                logins = ud.readUsersNames();
            } catch (Exception e) {
                log.error("Проверка logina", e);
            }
        }
        if (logins.contains(request.getParameter("name"))) {
            result = request.getParameter("name");
        }

        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(result);
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
        try {
            request.setCharacterEncoding("UTF-8");
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

            User user = new User(login, password, new String[]{"level1"}, name, lastName, email);
            try (UserDAO dao = DAOFactory.getUserDAOInstance(DAOSource.JDBC, this.getServletContext());) {
                user = dao.registerUser(user);
                if (user != null) {
                    request.setAttribute("registration", "done");
                    request.setAttribute("user", user);
                    request.setAttribute("mailType", "registration");
                    request.getSession().setAttribute("user", user);
                    logins= dao.readUsersNames();
                }
            }
        } catch (JDBCDAOException ex) {
            log.error("Ошибка создания пользователя", ex);
            System.out.println(ex);
        } catch (Exception e) {
            log.error("Ошибка регистрации пользователя", e);
            System.out.println(e);
        }
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
