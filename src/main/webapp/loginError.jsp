<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<!DOCTYPE html>
<html> 
    <head>
        <title>Ошибка входа в систему.</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href='CSSdoc/mainCSS.css' rel='stylesheet' type="text/css">
    </head>
    <body>
      
            <div id="header">
                <c:import url="/WEB-INF/template/headtemplate.jsp" charEncoding="UTF-8"/>
            </div>
            <div class="center_form"> 
            <h3 align='center'>   Не удалось выполнить вход в систему!<br> Если Вы забыли пароль, вы можете <a href="" >восстановить</a> его.<br> Также можно <a href="" >восстановить имя пользователя.</a><br>
                Если Вы ещё не зарегистрированы,<br> пройти эту простую процедуру можно <a href="<c:url value='registration.jsp'/>" > здесь.</a> </h3>
             <c:import url="/WEB-INF/template/loginF.jsp" charEncoding="UTF-8"/>
            </div>
           </body>
</html>