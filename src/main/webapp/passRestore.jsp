<%@page  pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Восстановление пароля</title>
        <link href='CSSdoc/mainCSS.css' rel='stylesheet' type="text/css">
    </head>
    <body>
        <div id="header">
            <c:import url="/WEB-INF/template/headtemplate.jsp" charEncoding="UTF-8"/>
        </div>
        <div class="panel center_form">
            <h1>Восстановление пароля</h1>
            <c:choose>
                <c:when test="${afterRestore eq null}">
            <form method="post" action='<c:url value="/User.do"/>' >
                <input type="hidden" name="action" value="passrestore">
                Введите  логин для которого необходимо восстановить пароль. Пароль 
                будет направлен Вам по e-mail.
                <div class="formitem">
                <input id='login' class="mandatory" type='text' name='login'/>
                <input id='send' type="submit" value="Восстановить">
                </div>
            </form>
                </c:when>
                <c:when test="${not empty error}">
                    <h2 class="error">${error}</h2>
                    <a href='<c:url value="index.jsp"/>' >На главную</a>
                </c:when>
                <c:otherwise><h3>На Ваш адрес '${user.email}' отправленно письмо с регистрационными данными.</h3> 
                    <a href='<c:url value="index.jsp"/>' >На главную</a>
                </c:otherwise>
            </c:choose>
        </div>
        <script src="scripts/myJavaScript.js"></script>        
        <script>$("input.mandatory").on('blur keyup', function() {
                emptyCheck(this, "<span class='mandatory error'>Поле не может быть пустым</span><br class='mandatory error'>");
                
            });
            $('body, input').on("change keyup",(function(){if($(".error").length>0) $("#send").attr("disabled","disabled");else $("#send").removeAttr("disabled");}));
                 $("input[type='text']").on('keyup blur', function() {
                dataValidCheck(this, '^[a-z,A-z,a-я,А-Я,0-9]+', "<span class='invalid error'>Значение поля должно начинаться с цифры или буквы.\n\
         </span><br class='invalid error'>");
            });
        </script>     
    </body>
</html>
