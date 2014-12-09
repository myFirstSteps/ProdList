package com.pankratov.prodlist.web;

import com.pankratov.prodlist.model.dao.DAOFactory;
import com.pankratov.prodlist.model.dao.UserDAO;
import com.pankratov.prodlist.model.users.User;
import java.io.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.apache.logging.log4j.*;

public class UserServlet extends HttpServlet {

    private static final Logger log = LogManager.getLogger(UserServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       
        response.sendError(405,"use post method.");
           }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try (UserDAO ud = DAOFactory.getUserDAOInstance(DAOFactory.DAOSource.JDBC, this.getServletContext());) {
            String action = request.getParameter("action");
            User u;
            switch (action) {
                case "passrestore":
                    RequestDispatcher dispatcher=request.getRequestDispatcher(response.encodeURL("passRestore.jsp"));
                    u = ud.readUser(request.getParameter("login"));
                    request.setAttribute("afterRestore", "done");
                    if (u == null){ request.setAttribute("error", 
                            String.format("У нас нет пользователя с логином '%s'! Зарегистрируйтесь заново.", request.getParameter("login"))); 
                    dispatcher.forward(request, response); break;}
                    if ( u.getEmail().equals("")){request.setAttribute("error",
                            String.format("Для пользователя с логином %s не задан e-mail. Восстановление пароля невозможно.", u.getLogin()));
                    dispatcher.forward(request, response);
                    break;}
                    
                        request.setAttribute("mailType", "passrestore");
                        request.setAttribute("user", u);
                        request.getRequestDispatcher(response.encodeURL("passRestore.jsp")).forward(request, response);
                    break;

                case "read":
                    u = ud.readUser(request.getParameter("name"));
                    if (u != null) {
                        response.getWriter().println(u);
                    } else {
                        response.getWriter().println("Пользователя с именем: " + request.getParameter("name")
                                + "не существует");
                    }
            }
        } catch (Exception ex) {
            log.error(ex);
        }
    }

}
