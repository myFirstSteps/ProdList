/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pankratov.prodlist.web;

import com.pankratov.prodlist.model.dao.DAOFactory;
import com.pankratov.prodlist.model.dao.DAOFactory.DAOSource;
import com.pankratov.prodlist.model.dao.jdbc.JDBCDAOException;
import com.pankratov.prodlist.model.users.User;
import com.pankratov.prodlist.model.dao.UserDAO;
import java.io.IOException;
import java.util.concurrent.*;
import javax.servlet.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.*;

public class Registration extends HttpServlet {

    private static final Logger log = LogManager.getLogger(Registration.class);
    private static ConcurrentSkipListSet<String> logins;
    
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
        response.getWriter().print(result);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
         
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
        } catch (Exception e) {
            log.error("Ошибка регистрации пользователя", e);
        }
    }
}
