<%-- 
    Document   : Admin
    Created on : 10.09.2014, 14:01:16
    Author     : pankratov
--%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Администратор</title>
     <style><%@include file="/CSSdoc/mainCSS.css"%></style>
    </head>
    <body>
        <c:import url="/WEB-INF/template/headtemplate.jsp" charEncoding="UTF-8"/> 
        <div>
            <form method="POST" action="<c:url value='showUser'/>">
                <input type="text" title="имя пользователя" name="name"> 
                <input type="submit" title="запросить">
            </form>
        </div>
   

    </body>
</html>
