package com.pankratov.prodlist.web;

import com.pankratov.prodlist.model.valuation.Valuation;
import com.pankratov.prodlist.model.dao.*;
import com.pankratov.prodlist.model.dao.jdbc.JDBCValuationDAO;
import java.io.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.*;

public class ValuationServlet extends HttpServlet {

    private static final Logger log = LogManager.getLogger(ValuationServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try (JDBCValuationDAO vald = DAOFactory.getValuationDAOInstance(DAOFactory.DAOSource.JDBC, request.getServletContext());) {
           // request.setCharacterEncoding("UTF-8");
            String ref = (ref = request.getParameter("reference")) != null ? ref : "";
            int rating = request.getParameter("rating") == null ? 0 : Integer.parseInt(request.getParameter("rating"));
            String action = (action = request.getParameter("action")) != null ? action : "";
            RequestDispatcher dispatcher = request.getRequestDispatcher(response.encodeURL("valuation.jsp"));
            Valuation val;

            switch (action) {
                case "write":
                    if (ref.equals("") && rating == 0) {
                        request.setAttribute("error", "Укажите оценку или напишите отзыв.");
                        dispatcher.forward(request, response);
                        return;
                    }
                    val = new Valuation((String) request.getSession().getAttribute("client"), rating, ref, "");
                    val.setOverview(request.getSession().getAttribute("visited").toString());
                    if (vald.addValuation(val)) {
                        request.setAttribute("success", "Спасибо за потраченное время. Ваш отзыв принят.");
                    }
            }
              val=new Valuation();
                   if(!request.isUserInRole("admin")) val.setAuthor((String) request.getSession().getAttribute("client")); else val.setAuthor("admin");
                    request.setAttribute("valuations",vald.readValuations(val));
                    dispatcher.forward(request, response);
        } catch (Exception e) {
            log.error(e);
            throw new ServletException(e);
        }
    }

}
