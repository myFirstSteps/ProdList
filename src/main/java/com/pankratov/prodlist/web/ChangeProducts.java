/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pankratov.prodlist.web;

import com.pankratov.prodlist.model.dao.DAOFactory;
import com.pankratov.prodlist.model.dao.ProductDAO;
import static com.pankratov.prodlist.model.dao.ProductDAO.KindOfProduct.*;
import com.pankratov.prodlist.model.dao.jdbc.JDBCDAOException;
import com.pankratov.prodlist.model.products.Product;
import java.io.*;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
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
        try (ProductDAO pdao = DAOFactory.getProductDAOInstance(DAOFactory.DAOSource.JDBC, request.getServletContext())) {
            Product temp, p = Product.getInstanceFromJSON(request);
            String action, client = (String) request.getSession().getAttribute("client");
            switch (action = (String) (request.getParameter("action"))) {
                case "change":
                case "delete":
                    if ((p.isOrigin() && !request.isUserInRole("admin"))) {
                        throw new JDBCDAOException("Нет прав.");
                    }
                    temp = new Product();
                    temp.setOrigin(p.isOrigin());
                    temp.setId(p.getId());
                    if (!(pdao.readProducts(temp, temp.isOrigin() ? ORIGINAL : USER_COPY).get(0).getAuthor().equals(client)
                            || request.isUserInRole("admin"))) {
                        throw new JDBCDAOException("Нет прав.");
                    }
                    if (action.equals("change")) {
                        p = pdao.changeProduct(p);
                    } else {
                        p = pdao.deleteProduct(p);
                    }
                    break;
                case "clone":
                    p = pdao.readProducts(p, ORIGINAL).get(0);
                    temp = new Product();
                    temp.setOriginID(p.getId());
                    temp.setName(p.getName());
                    temp.setGroup(p.getGroup());
                    temp.setAuthor(client);
                    if (pdao.readProducts(temp, USER_COPY).size() > 0) {
                        throw new JDBCDAOException("Пользовательский вариант уже существует.");
                    }
                    p = pdao.addProduct(temp, p.getImageLinks());
                    break;
                case "legalize":
                    if (!request.isUserInRole("admin")) {
                        throw new JDBCDAOException("Нет прав.");
                    }
                    p = pdao.readProducts(p, USER_COPY).get(0);
                    p.setOrigin(true);
                    p.setAuthorRole("admin");
                    temp = new Product();
                    temp.setId(p.getId());
                    p = pdao.addProduct(p, p.getImageLinks());
                    temp.setOriginID(p.getId());
                    temp.setOrigin(false);
                    pdao.changeProduct(temp);
            }
            response.getWriter().println(p.toJSON());
        } catch (Exception ex) {
            JSONObject jsonerr = new JSONObject();
            String couse = ex.getMessage();
            if (ex.getMessage().contains("Data truncation: Out of range")) {
                jsonerr.put("error", "Введено слишком большое число.");
            }
            if (ex.getMessage().contains("Ни одна запись не изменена")) {
                jsonerr.put("error", "Запись не изменена");
            }
            if (ex.getMessage().contains("Нет прав")) {
                jsonerr.put("error", "У вас нет прав на редактирование этого продукта");
            }
            if (ex.getMessage().contains("уже существует")) {
                jsonerr.put("error", "Пользовательский вариант уже существует.");
            }
            if (jsonerr.size() == 0) {
                jsonerr.put("error", "Во время редактирования записи произошла ошибка");
            }
            System.out.println(ex);
            response.getWriter().println(jsonerr);
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
