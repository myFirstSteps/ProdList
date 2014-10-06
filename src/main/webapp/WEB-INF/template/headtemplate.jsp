<%-- 
    Document   : headtemplate
    Created on : 29.07.2014, 14:18:37
    Author     : pankratov
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<c:set var="username" scope="page" value="${pageContext.request.getRemoteUser()}"/>
<c:set var="path" value="${pageContext.request.getServletPath()}"/>
<div id="HeadTemplate">


    <div id ="author" class="HeadTemplate"><span>Интерактивное резюме на<br>позицию Java Junior Developer.<br><em>Панкратов Михаил</em></span></div>
    <div  id='logo' class="HeadTemplate" ><img src="<c:url value="/resources/OCP_JavaSE7Programmer.gif"/>"  width="140" height="71" alt="OCP_JavaSE7Programmer.gif"></div>
    <div id="right" class='HeadTemplate'>
        <c:choose>
            <c:when test="${username==null}">

                <c:if test="${path ne'/loginPage.jsp' and path ne '/loginError.jsp'}">
                    <form id="UserForm" action="<c:url value='loginPage.jsp'/>"  method="get">  
                        Вход не выполнен.<br>
                        <input type="submit" value="Войти">                  
                    </form> 
                    <c:set var="lastpage" scope="session" value="${path}"/>
                </c:if> 

            </c:when>
            <c:otherwise> 
                <form id="UserForm" action="LogOut" method="get"> Пользователь: <span id="UserName"><em> ${username}</em></span><br> <button name="path" value="${path}">Выход</button></form>
                </c:otherwise>
            </c:choose>

    </div>
</div>


