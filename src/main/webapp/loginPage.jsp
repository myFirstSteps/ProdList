<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<!DOCTYPE html>
<html> 
    <head>
        <title>Авторизация.</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <style><%@include file="/CSSdoc/mainCSS.css"%></style>
    </head>
    <body>
        <div id="doc">
            <div id="header">
                <c:import url="/WEB-INF/template/headtemplate.jsp" charEncoding="UTF-8"/>
            </div>
            <c:choose>
                <c:when test="${pageContext.request.getRemoteUser()==null}">
                    <c:import url="/WEB-INF/template/loginF.jsp" charEncoding="UTF-8"/>
                </c:when>
                <c:otherwise>
                    <c:if test="${!empty sessionScope.Attribute.lastpage}"> <jsp:forward page="index.jsp"/></c:if>
                    <jsp:forward page="index.jsp"/>
                    
                  
                </c:otherwise>
            </c:choose>
        </div>
    </body>
</html>






