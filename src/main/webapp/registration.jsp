<%-- 
    Document   : registration
    Created on : 15.09.2014, 21:33:37
    Author     : pankratov
--%>

<%@page  pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
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
        <%--Форма регистрации --%>
        <div class="center_form" id="registration">
            <c:choose> <c:when test="${registration ne 'done'}">
                    <form id="registration" class="center_form" method="post" action="<c:url value='Registration'/>" >
                    <div id='error' class='error'>${error}</div> 
                    <h3>Добро пожаловать на страницу регистрации.</h3>
                    <p>
                        Регистрация не требуется для большинства функционала сайта и тестового приложения, и добавлена ..., совершенно верно, ради
                        организации самого процесса регистрации, так что смело можете оставить это занятие на потом. Если же вам очень интересно посмотреть работает это, 
                        или нет, заполните необходимые поля и нажмите кнопку зарегистрироваться.
                    </p>
                    
                        <p>логин:<br><input  class="mandatory"     value="${regData['0']}"
                                             type="text" title="Login пользователя который вы будете вводить при авторизации" name="login" placeholder="обязательное поле"></p>
                        <p>пароль:<br><input   class="mandatory confirmt"  type="password" title="Пароль" name="password" placeholder="обязательное поле"></p>
                        <p>подтвердить пароль:<br><input class="mandatory confirm"   type="password" title="Пароль" placeholder="обязательное поле"></p>
                        <p>e-mail:<br><input  type="text" title="e-mail" name="e-mail" value="${regData['3']}" placeholder="очень желательное поле"></p>
                        <p>имя:<br><input  type="text" title="Ваше имя" name="name" value="${regData['1']}" placeholder="не обязательное поле"></p>
                        <p>Фамилия:<br><input  type="text" title="Ваша фамилия" value="${regData['2']}" name="family" placeholder="не обязательное поле"></p>
                        <input type="button" onclick="validate(this.form)" value="Зарегистрировать">
                    </form>
                </c:when>
                <c:otherwise>
                    <h2>Добро пожаловать, <em><c:choose> <c:when test="${user.firstName!=''}">${user.firstName}</c:when>
                                <c:otherwise>${user.login}</c:otherwise></c:choose>!</em></h2>
                            <p>Поздравляем Вас, с успешной регистрацией на нашем портале.
                            </p><p>Ваши данные для авторизации:<br>
                                <b>логин: <em>${user.login}</em></b><br>  
                        <b>пароль: <em>${user.password}</em></b>
                    </p>
                    <form method="POST" action="<c:url value='loginPage.jsp'/>">
                        <input type="submit" value="Войти">
                    </form>
                    
                </c:otherwise>
            </c:choose>
        </div>
        <script src="scripts/jquery-1.11.1.min.js"></script>
        <script src="scripts/formValidation.js"></script>
        <script>$("input.mandatory").bind('blur', function() {
                                emptyCheck(this,"<span class='mandatory error'>Поле не может быть пустым</span><br class='mandatory error'>");
                            });
                            $("input.mandatory").bind('keyup', function() {
                                emptyCheck(this,"<span class='mandatory error'>Поле не может быть пустым</span><br class='mandatory error'>");
                            });
                            $("input.confirm").bind('blur', function() {
                                confirmationCheck(this,$("input.confirmt"),"<span class='confirm error'>Значение поля не совпадает с полем</span><br class='confirm error'>");
                            });
                            $("input.confirm").bind('keyup', function() {
                                if($(this).siblings(".confirm.error").length!==0) confirmationCheck(this,$("input.confirmt"),"<span class='confirm error'>Значение поля не совпадает с полем</span><br class='confirm error'>");
                            });
                            $("input[name='login']").bind('keyup', function() {
                                uniqueCheck(this,{name: this.value},"GET","Registration","<span class='unique error'>К сожалению логин уже занят</span><br class='unique error'>"); 
                            });

                            $("input.confirm").bind('keyup', function() {
                                if ($(this).siblings(".confirm.error").length != 0)
                                    confirmationCheck(this);
                            });
                            $("input[type='text'], input[type='password']").bind('keyup', function() {
                                 dataValidCheck(this,'^[a-z,A-z,a-я,А-Я,0-9]+',"<span class='invalid error'>Значение поля должно начинаться с цифры или буквы.\n\
                         </span><br class='invalid error'>"); 
                            });
                            $("input[type='text'], input[type='password']").bind('blur', function() {
                                dataValidCheck(this,'^[a-z,A-z,a-я,А-Я,0-9]+',"<span class='invalid error'>Значение поля должно начинаться с цифры или буквы.\n\
                         </span><br class='invalid error'>"); 
                            });
        </script>     
    </body>
</html> 
