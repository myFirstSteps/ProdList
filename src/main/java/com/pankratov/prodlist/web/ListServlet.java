package com.pankratov.prodlist.web;

import com.pankratov.prodlist.model.dao.DAOFactory;
import com.pankratov.prodlist.model.dao.ProdListDAO;
import com.pankratov.prodlist.model.dao.ProductDAO;
import com.pankratov.prodlist.model.dao.jdbc.AlreadyExistsException;
import com.pankratov.prodlist.model.list.ProdList;
import java.io.*;
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
        ProdList list=new ProdList();
        String action=request.getParameter("action");
        
        try(ProdListDAO pldao=  DAOFactory.getProdListDAOInstance(DAOFactory.DAOSource.JDBC, request.getServletContext());
            ProductDAO pdao=DAOFactory.getProductDAOInstance(DAOFactory.DAOSource.JDBC, request.getServletContext());    ){
        switch(action){
            case "save": 
                list=ProdList.getInstanceFromJSON(request);
                if(pldao.addProdList(list)){
                    json.put("listNmae", list.getName());
            }
            break;
            case "show":  
              String listName= request.getParameter("listName");
              ProdList plist=new ProdList();
              plist.setName(listName);
              json.put("list",pldao.readProdLists(plist).get(0).toJSON(pdao));
               
        }
        }catch(AlreadyExistsException e){json.put("error", String.format("Список с именем %s уже существует.",list.getName() ));}
        
        catch (Exception e){
            String act=action!=null?action:"обработке";
            switch(act){
                case "save":act="cохранении";break;
                case "show":act="чтении";break;
            }
            json.put("error", String.format("При %s списка произошла ошибка.",act)); }
        response.getWriter().println(json);
    }

}
