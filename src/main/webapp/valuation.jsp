<%-- 
    Document   : valuation
    Created on : 27.11.2014, 15:32:12
    Author     : pankratov
--%>
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
        <div  class='panel prodholder'> 
            <h2>Оцените ресурс и оставьте свой отзыв.</h2>
            <img  src='resources/common_image/Valuation.png'>
        </div>
        <form method="post" action="valuation">
            <span>оценка:</span><input  type="radio">
        </form>

        <script>
            $("#valuation").hide();
        </script>
    </body>
</html>

