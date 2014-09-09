<%-- 
    Document   : index
    Created on : 29.07.2014, 15:57:04
    Author     : pankratov
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>

<!DOCTYPE html>
<html> 
    <head>
        <title>index</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <style><%@include file="WEB-INF/CSSdoc/mainCSS.css"%></style>
    </head>
    <body>
        <c:import url="/WEB-INF/template/headtemplate.jsp" charEncoding="UTF-8"/> 
        <div>
            <c:choose>
                <c:when test="${pageContext.request.getRemoteUser()==null}">
                    присвоение атрибута dom:${pageContext.session.setAttribute('dom', '11')}
                </c:when>
                <c:otherwise> отображение ранее присвоенного атрибута dom:${pageContext.session.getAttribute('dom')}</c:otherwise>
            </c:choose>
            ${pageContext.session.getAttribute('lastpage')}

            Развернутое пояснение зачем нужен этот сайт
        </div>
    </body>
</html>
