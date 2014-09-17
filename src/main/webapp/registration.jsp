<%-- 
    Document   : registration
    Created on : 15.09.2014, 21:33:37
    Author     : pankratov
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Регистрация</title>
        <style><%@include file="/CSSdoc/mainCSS.css"%></style>
    </head>
    <body>
        <div id="header">
            <c:import url="/WEB-INF/template/headtemplate.jsp" charEncoding="UTF-8"/>
        </div>
        <div class="center_form" id="registration">
            <h3>Добро пожаловать на страницу регистрации.</h3>
            <p>
                Регистрация не требуется для большинства функционала сайта и тестового приложения, и добавлена ..., совершенно верно, ради
                организации самого процесса регистрации, так что смело можете оставить это занятие на потом. Если же вам очень интересно посмотреть работает это, 
                или нет, заполните необходимые поля и нажмите кнопку зарегистрироваться.
            </p>
            <script> function myFunc(){alert("Rere");}</script>
            <form method="post" action="Registration">
                <p>login:<br><input      onkeyup="checkLogin(this.value)"
                                    type="text" title="Login пользователя который вы будете вводить при авторизации" name="login" placeholder="обязательное поле"></p>
                <p>пароль:<br><input  type="password" title="Пароль" name="password" placeholder="обязательное поле"></p>
                <p>повторить пароль:<br><input  type="password" title="Пароль" name="password_check" placeholder="обязательное поле"></p>
                <p>e-mail:<br><input  type="text" title="e-mail" name="e-mail" placeholder="очень желательное поле"></p>
                <p>имя:<br><input  type="text" title="Ваше имя" name="name" placeholder="не обязательное поле"></p>
                <p>Фамилия:<br><input  type="text" title="Ваша фамилия" name="family" placeholder="не обязательное поле"></p>
                <input type="submit" value="Зарегистрировать">
            </form>
        </div>
        <script src="scripts/jquery-1.11.1.min.js"> </script>
        <script src="scripts/ajaxSupport.js"></script>
    </body>
</html>
