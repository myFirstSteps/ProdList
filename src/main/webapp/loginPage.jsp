<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<!DOCTYPE html>
<html> 
    <head>
        <title>Авторизация.</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <style><%@include file="WEB-INF/CSSdoc/mainCSS.css"%></style>
    </head>
    <body>
        <div id="doc">
            <div id="header">
                <c:import url="/WEB-INF/template/headtemplate.jsp" charEncoding="UTF-8"/>
            </div>
                <c:choose>
                    <c:when test="${pageContext.request.getRemoteUser()==null}">
            <div ID="AuthForm">
                <h4 align="center">Для входа в систему введите<br> имя пользователя и пароль.</h4>
                <form  method="POST" action="<c:url value='j_security_check'/>">
                    <p>имя:<br><input type="text" name="j_username"></p>
                    <p>пароль:<br><input type="password" name="j_password"></p>  
                    <p><input type="submit" value="Войти"></p>
                </form> 
            </div>
                </c:when>
                    <c:otherwise>
                        <c:set target='${cookie}' property="login"> ${pageContext.request.getRemoteUser()}</c:set>    
                        <c:redirect  url="${pageContext.session.getAttribute('lastpage')}"/>
                    </c:otherwise>
                </c:choose>
        </div>
    </body>
</html>






