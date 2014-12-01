package com.pankratov.prodlist.web;

import com.pankratov.prodlist.model.dao.*;
import static com.pankratov.prodlist.model.dao.ProductDAO.KindOfProduct.*;
import com.pankratov.prodlist.model.dao.jdbc.AlreadyExistsException;
import com.pankratov.prodlist.model.dao.jdbc.TruncationException;
import com.pankratov.prodlist.model.products.Product;
import com.pankratov.prodlist.model.products.ProductException;
import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.apache.logging.log4j.*;
import org.json.simple.*;

public class ChangeProducts extends HttpServlet {
private static final Logger log= LogManager.getLogger(ChangeProducts.class);
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendError(405,"Необходим метод POST");
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        JSONObject jsonerr = new JSONObject();
        try (ProductDAO pdao = DAOFactory.getProductDAOInstance(DAOFactory.DAOSource.JDBC, request.getServletContext())) { 
            Product temp=new Product(), p = Product.getInstanceFromJSON(request);
            String action=request.getParameter("action"); 
            String client = (String) request.getSession().getAttribute("client");
            String role = (String) request.getSession().getAttribute("role");
            switch (action) {
                case "change":
                case "delete":    
                    temp.setOrigin(p.isOrigin());
                    temp.setId(p.getId());
                    String author=pdao.readProduct(temp, temp.isOrigin() ? ORIGINAL : USER_COPY).getAuthor();
                    if (!(author.equals(client)
                            || role.equals("admin"))||(p.isOrigin()&&!role.equals("admin"))) {
                        throw new SecurityException(String.format("Нет прав на выполнение операции \"%s\" над продуктом  \"%s\".",action,p.getId()+p.getOriginID()));
                    }
                    if (action.equals("change")) {
                        p = pdao.changeProduct(p);
                    } else {
                        p = pdao.deleteProduct(p);
                    }
                    break;
                case "clone":
                    p = pdao.readProduct(p, ORIGINAL);
                    temp.setOriginID(p.getId());
                    temp.setName(p.getName());
                    temp.setGroup(p.getGroup());
                    temp.setAuthor(client); 
                    p = pdao.addProduct(temp, p.getImageLinks());
                    break;
                case "legalize":
                    if (!role.equals("admin")) {
                        throw new SecurityException(String.format("Нет прав на выполнение операции \"legalize\" над продуктом  %s.",p.getId()+p.getOriginID()));}
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
            response.getWriter().println(p.toJSON()); return;
        }catch(ProductException ex){
            jsonerr.put("error", "Ошибка обработки запроса");
        }catch(SecurityException e){ 
            jsonerr.put("error", "У вас нет прав на редактирование этого продукта.");
        }catch (AlreadyExistsException e){
            jsonerr.put("error", "Пользовательский вариант уже существует.");
        } 
        catch (TruncationException e){
            jsonerr.put("error", "Введено слишком большое число.");
        }
        catch (DAOException ex) {
            if (ex.getMessage().contains("Ни одна запись не изменена")) {
                jsonerr.put("error", "Запись не изменена");
            }
        }
        catch (Exception ex){  
                log.error(ex);
                jsonerr.put("error", "Во время редактирования записи произошла ошибка");
           
        }
         response.getWriter().println(jsonerr);   
    }
}
