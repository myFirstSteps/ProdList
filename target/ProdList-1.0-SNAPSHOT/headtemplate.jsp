<%-- 
    Document   : headtemplate
    Created on : 29.07.2014, 14:18:37
    Author     : pankratov
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<div id="HeadT">
    <c:choose >
        
        <c:when test="${pageContext.request.asyncStarted}"> ttyty</c:when>
    </c:choose>
    <form ID="LoginF" action="LogIn" method="post">
    Авторизация:<br>
    <input type="text" name="username">
    <input type="file" name="password">
    </form>
    <table><td><span>Интерактивное резюме на<br>позицию Java Junior Developer.<br><em>Панкратов Михаил</em></span></td>
        <td><img src="OCP_JavaSE7Programmer.gif" allign="top" width="140" height="71" alt="OCP_JavaSE7Programmer.gif"><br></td>
    </table>
</div>


