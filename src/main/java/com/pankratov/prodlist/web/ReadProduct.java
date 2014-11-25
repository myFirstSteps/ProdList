/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pankratov.prodlist.web;

import com.pankratov.prodlist.model.dao.*;
import com.pankratov.prodlist.model.dao.DAOFactory;
import static com.pankratov.prodlist.model.dao.ProductDAO.KindOfProduct.BOTH;
import com.pankratov.prodlist.model.products.Product;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author pankratov
 */
public class ReadProduct extends HttpServlet {

    

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Product product;
        List<Product> ls = null;
        String x = null;
        for(Map.Entry<String,String[]>m:request.getParameterMap().entrySet()){
            System.out.println(">"+m.getKey()+"|"+Arrays.asList(m.getValue()));
        }
        int maxCount = (x = request.getParameter("maxCount")) != null ? new Integer(x) : 0;
        try (ProductDAO pdao = DAOFactory.getProductDAOInstance(DAOFactory.DAOSource.JDBC, request.getServletContext());) {
            product = Product.getInstanceFromJSON(request);
            ls = pdao.readProducts(product, BOTH);
        } catch (Exception e) { System.out.println(e);
            
        }
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (maxCount > 0 && ls.size() > maxCount) {
            JSONObject jsonerr = new JSONObject();
            jsonerr.put("error", "Найедено слишком много продуктов.");
            response.getWriter().println(jsonerr);
        } else {
            JSONArray products = new JSONArray();
            for (Product p : ls) {
                products.add(p.toJSON());
            }
            JSONObject result=new JSONObject();
            result.put("products", products);
            response.getWriter().println(result.toJSONString());
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
        Product product = null;
        if (ServletFileUpload.isMultipartContent(request)) {
            try {
                DiskFileItemFactory factory = new DiskFileItemFactory();
                ServletFileUpload upl = new ServletFileUpload(factory);
                List<FileItem> x = upl.parseRequest(request);
                TreeMap<String, String> prodInit = new TreeMap<>();
                product = Product.getInstanceFromFormFields(x, request);
            } catch (UnsupportedEncodingException | FileUploadException e) {
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
