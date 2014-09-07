<%-- 
    Document   : headtemplate
    Created on : 29.07.2014, 14:18:37
    Author     : pankratov
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<div id="HeadT">
    <div id="RC"> 
        <c:choose>
            <c:when test="${pageContext.request.getRemoteUser()==null}">
                <form ID="LoginF" action="${pageContext.response.encodeURL("loginPage.jsp")}"  method="get">  
                    <c:if test="${pageContext.request.getServletPath()ne'/loginPage.jsp'}">
                        Вход не выполнен.<br>
                        <center><input type="submit" value="Войти"> </center>
                    </c:if> 
                </form> 
            </c:when>
            <c:otherwise>   
                <span> Выполнен вход под именем:</span><span> <c:out value='${pageContext.request.getRemoteUser()}'/></span>
            </c:otherwise>
        </c:choose>
    </div>

    <table><td><span>Интерактивное резюме на<br>позицию Java Junior Developer.<br><em>Панкратов Михаил</em></span></td>
        <td><img src="<c:url value="/resources/OCP_JavaSE7Programmer.gif"/>" allign="top" width="140" height="71" alt="OCP_JavaSE7Programmer.gif"><br></td>
    </table>
</div>


