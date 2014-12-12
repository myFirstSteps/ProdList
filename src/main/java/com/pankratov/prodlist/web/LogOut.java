package com.pankratov.prodlist.web;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.*;

public class LogOut extends HttpServlet {

   
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.logout();
        response.sendRedirect(response.encodeRedirectURL(request.getContextPath()+request.getParameter("path")));
        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }


}
