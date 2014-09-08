<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<!DOCTYPE html>
<html> 
    <head>
        <title>Ошибка входа в систему.</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <style><%@include file="WEB-INF/CSSdoc/mainCSS.css"%></style>
    </head>
    <body>

        <div id="doc">
            <div id="header">
                <c:import url="/WEB-INF/template/headtemplate.jsp" charEncoding="UTF-8"/>
            </div>
            <h3 align='center'>   Не удалось выполнить вход в систему!<br> Если Вы забыли пароль, вы можете <a href="" >восстановить</a> его.<br> Также можно <a href="" >восстановить имя пользователя.</a><br>
               Если Вы ещё не зарегистрированы,<br> пройти эту простую процедуру можно <a href="" > здесь.</a> </h3>
        </div>
    </body>
</html>