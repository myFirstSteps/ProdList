package com.pankratov.prodlist.web;

import com.pankratov.prodlist.model.dao.*;
import static com.pankratov.prodlist.model.dao.ProductDAO.KindOfProduct.*;
import com.pankratov.prodlist.model.dao.jdbc.JDBCDAOException;
import com.pankratov.prodlist.model.products.Product;
import com.pankratov.prodlist.model.products.ProductException;
import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.apache.log4j.*;
import org.json.simple.*;

public class ChangeProducts extends HttpServlet {
private static final Logger log= LogManager.getLogger(ChangeProducts.class);
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
         JSONObject jsonerr = new JSONObject();
        try (ProductDAO pdao = DAOFactory.getProductDAOInstance(DAOFactory.DAOSource.JDBC, request.getServletContext())) { 
            Product temp, p = Product.getInstanceFromJSON(request);
            String action=request.getParameter("action"); 
            String client = (String) request.getSession().getAttribute("client");
            String role = (String) request.getSession().getAttribute("role");
            switch (action) {
                case "change":
                case "delete":
                    if ((p.isOrigin() && !request.isUserInRole("admin"))) {
                        throw new JDBCDAOException("Нет прав.");
                    }
                    temp = new Product();
                    temp.setOrigin(p.isOrigin());
                    temp.setId(p.getId());
                    if (!(pdao.readProduct(temp, temp.isOrigin() ? ORIGINAL : USER_COPY).getAuthor().equals(client)
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
                    p = pdao.readProduct(p, ORIGINAL);
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
                    p = pdao.readProduct(p, USER_COPY);
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
        } catch (DAOException |ProductException ex) {
            
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
                log.error(ex);
                jsonerr.put("error", "Во время редактирования записи произошла ошибка");
            }
            response.getWriter().println(jsonerr);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }


}
