<%-- 
    Document   : headtemplate
    Created on : 29.07.2014, 14:18:37
    Author     : pankratov
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<c:set var="username" scope="page" value="${pageContext.request.getRemoteUser()}"/>
<c:set var="path" value="${pageContext.request.getServletPath()}"/>
<div id="HeadT">
    <div id="RC"> 
        <c:choose>
            <c:when test="${username==null}">
                <form ID="LoginF" action="<c:url value='loginPage.jsp'/>"  method="get">  
                    <c:if test="${path ne'/loginPage.jsp' and path ne '/loginError.jsp'}">
                        Вход не выполнен.<br>
                        <center><input type="submit" value="Войти"> </center>
                        <c:set var="lastpage" scope="session" value="${path}"/>
                    </c:if> 
                </form> 
            </c:when>
            <c:otherwise> 
                <span> Выполнен вход под именем:</span><br><span> ${username}</span><form action="LogOut" method="get"><button name="path" value="${path}">Выход</button></form>
            </c:otherwise>
        </c:choose>
    </div>

    <table><td><span>Интерактивное резюме на<br>позицию Java Junior Developer.<br><em>Панкратов Михаил</em></span></td>
        <td><img src="<c:url value="/resources/OCP_JavaSE7Programmer.gif"/>" allign="top" width="140" height="71" alt="OCP_JavaSE7Programmer.gif"><br></td>
    </table>
</div>


