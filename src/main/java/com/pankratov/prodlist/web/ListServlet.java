package com.pankratov.prodlist.web;

import com.pankratov.prodlist.model.dao.DAOFactory;
import com.pankratov.prodlist.model.dao.ProdListDAO;
import com.pankratov.prodlist.model.dao.ProductDAO;
import com.pankratov.prodlist.model.list.ProdList;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class ListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendError(405);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        JSONObject json= new JSONObject();
        try(ProdListDAO pldao=  DAOFactory.getProdListDAOInstance(DAOFactory.DAOSource.JDBC, request.getServletContext());
            ProductDAO pdao=DAOFactory.getProductDAOInstance(DAOFactory.DAOSource.JDBC, request.getServletContext());    ){
        switch(request.getParameter("action")){
            case "save": if(pldao.addProdList(ProdList.getInstanceFromJSON(request))){
                response.getWriter().println(json);
            }
            break;
            case "show":  
              String listName= request.getParameter("listName");
              ProdList plist=new ProdList();
              plist.setName(listName);
              json.put("list",pldao.readProdLists(plist).get(0).toJSON(pdao));
                response.getWriter().println(json);
        }
        }
        
        catch (Exception e){System.out.println("cc"+e); json.put("error", e.getMessage()); response.getWriter().println(json);}
    }

}
