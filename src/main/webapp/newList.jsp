<%-- 
    Document   : newList
    Created on : 06.11.2014, 16:44:53
    Author     : pankratov
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Создание списка покупок.</title>
        <style><%@include file="CSSdoc/mainCSS.css"%></style>
        <link rel="stylesheet" href="<c:url value='CSSdoc/jquery-ui.min.css'/>">
        
    </head>
    <body>
        <c:import url="WEB-INF/template/headtemplate.jsp"/>
        <div class='center_form prodholder'>
            <img class="errorImg" src=" resources/common_image/error_pics/exeption.png">
            <ol id="sortable">
                <li>a</li>
                <li>b</li>
                <li>v</li>
                <li>g</li>
            </ol>
        </div>
        
         <script src="scripts/jquery-ui.js"></script>
        <script>
            $(function() {
                $("#sortable").sortable();
                $("#sortable").disableSelection();
            });
        </script>
    </body>


</html>
