<%-- 
    Document   : newProduct
    Created on : 06.10.2014, 11:24:48
    Author     : pankratov
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="product" uri="ProductsEL" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Добавить продукт.</title>
    </head>
    <body>
        
        ${product:getCategories(pageContext.servletContext)}
        <form id="addProduct" method="post" action="productAdd">
            <select> 
                
            </select>
        </form>
    </body>
</html>
