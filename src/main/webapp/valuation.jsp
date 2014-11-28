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
        <div  class='panel'> 
            <h2>Оцените ресурс и оставьте свой отзыв.</h2>
            <img  src='resources/common_image/Valuation.png'>
            <form method="post" action="valuation">
               
                <span>Оценка:</span> <c:forEach begin="1" varStatus="st" end="5"> <input value="${st.index}" name="valuation" type="radio">${st.index}</c:forEach>
                <br> <div style="margin-top: 10px;"><span>Напишите отзыв:</span><br><textarea rows="5" cols="60" form="valuation"></textarea></div>
                <br><input type="submit" class="pointer" value="Отправить">
            </form>
        </div>
        <script>
            $("#valuation").hide();
        </script>
    </body>
</html>

