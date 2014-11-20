<%-- 
    Document   : viewList
    Created on : 20.11.2014, 17:03:24
    Author     : pankratov
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="product" uri="ProductsEL" %>
<!DOCTYPE html>
<html>
    <head>
       <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Просмотр списков покупок.</title>
        <link href='CSSdoc/mainCSS.css' rel='stylesheet' type="text/css">
    </head>
    <body>
         <c:import url="WEB-INF/template/headtemplate.jsp"/>
          ${product:getListNames(pageContext.servletContext,client)}
         <div  class='center_form prodholder'>
             <select>
                 <option>
                 <option>    
             </select>
         </div>
    </body>
</html>
