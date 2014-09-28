<%-- 
    Document   : loginF
    Created on : 09.09.2014, 14:22:10
    Author     : pankratov
--%>

<%@page  contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
        <div class="center_form" ID="AuthForm">
            <h4 align="center">Для входа в систему введите<br> логин пользователя и пароль.</h4>
            <form  method="POST" action="<c:url value='j_security_check'/>">
                <p>логин:<br><input type="text" name="j_username"  value="${cookie.login.value}"></p>
                <p>пароль:<br><input type="password" name="j_password"></p>  
                <p><input type="submit" value="Войти"></p>
            </form> 
        </div>