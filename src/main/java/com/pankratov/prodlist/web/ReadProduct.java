package com.pankratov.prodlist.web;

import com.pankratov.prodlist.model.dao.*;
import com.pankratov.prodlist.model.dao.DAOFactory;
import static com.pankratov.prodlist.model.dao.ProductDAO.KindOfProduct.*;
import com.pankratov.prodlist.model.products.Product;
import com.pankratov.prodlist.model.products.ProductException;
import java.io.*;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.simple.*;

public class ReadProduct extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Product product;
        List<Product> ls = null;
        String x = null;
        response.setContentType("application/json");
        JSONObject result = new JSONObject();
        String action = request.getParameter("action");
        try (ProductDAO pdao = DAOFactory.getProductDAOInstance(DAOFactory.DAOSource.JDBC, request.getServletContext());) {
            product = Product.getInstanceFromJSON(request);
            if (!product.getAuthor().equals((String) request.getSession().getAttribute("client"))) {
                throw new SecurityException();
            }
            ls = pdao.readProducts(product, BOTH);
            switch (action) {
                case "products":
                    ls = pdao.readProducts(product, USER_COPY);
                    JSONArray products = new JSONArray();
                    for (Product p : ls) {
                        products.add(p.toJSON());
                    }
                    result.put("products", products);
                    break;
                case "count":
                    result.put("prodCount", ls.size());
                    break;
                 case "infoCard":
                     request.setAttribute("products", ls);
                     request.setAttribute("productsOnly", true);
                     request.getRequestDispatcher("/WEB-INF/template/productinfo.jsp").forward(request, response);
                     System.out.println("send");
                     result.put("card", ls.get(0).toJSON());
            }
        } catch (DAOException | ProductException e) {
            result.put("error", "Ошибка при чтении продуктов");
        } catch (SecurityException ex) {
            result.put("error", "Нет прав на чтение данного продукта");
        }
        response.getWriter().println(result);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Product product = null;
        if (ServletFileUpload.isMultipartContent(request)) {
            try {
                DiskFileItemFactory factory = new DiskFileItemFactory();
                ServletFileUpload upl = new ServletFileUpload(factory);
                List<FileItem> x = upl.parseRequest(request);
                TreeMap<String, String> prodInit = new TreeMap<>();
                product = Product.getInstanceFromFormFields(x, request);
            } catch (ProductException | FileUploadException e) {
            }
        } else {
            product = Product.getInstanceFromRequest(request);
        }

        try (ProductDAO pdao = DAOFactory.getProductDAOInstance(DAOFactory.DAOSource.JDBC, request.getServletContext());) {
            request.setAttribute("products", pdao.readProducts(product, BOTH));
            request.getRequestDispatcher(response.encodeURL("editProduct.jsp")).forward(request, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

}
