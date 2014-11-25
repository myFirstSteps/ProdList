<%-- 
    Document   : loginF
    Created on : 09.09.2014, 14:22:10
    Author     : pankratov
--%>

<%@page  contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix='product' uri='ProductsEL' %>


<form class="" method="POST" action="<c:url value='j_security_check'/>">
    <h3>Для входа в систему введите<br> логин пользователя и пароль.</h3>
    <p>логин:<br><input type="text" name="j_username"  value="<c:choose><c:when 
                                test="${!empty cookie.login.value  }">${product: decodeURL(cookie.login.value,'UTF-8')}</c:when><c:otherwise
                                >${sessionScope.user.login}</c:otherwise></c:choose>"></p>
    <p>пароль:<br><input type="password"  name="j_password"></p>  
    <p><input type="submit" value="Войти"></p>
</form> 