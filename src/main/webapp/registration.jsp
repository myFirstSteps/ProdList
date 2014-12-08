
<%@page  pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Регистрация</title>
        <link href='CSSdoc/mainCSS.css' rel='stylesheet' type="text/css">
    </head>
    <body>
        <div id="header">
            <c:import url="/WEB-INF/template/headtemplate.jsp" charEncoding="UTF-8"/>
        </div>
        <%--Форма регистрации --%>
        <div class="center_form panel" id="registration">
            <c:choose> <c:when test="${registration ne 'done'}">
                    <form id="registrati"  method="post" action="<c:url value='Registration.do'/>" >
                        <span id='error' class='error'>${error}</span> 
                        <h3>Добро пожаловать на страницу регистрации.</h3>
                        <p>
                            Заполните необходимые поля и нажмите кнопку зарегистрировать. 
                        </p>
                        <div>
                            <div class="formitem"><span>Логин:</span><br><input  class="mandatory"     value="${regData['0']}"
                                                                                 type="text" title="Login пользователя который вы будете вводить при авторизации" name="login" placeholder="обязательное поле"></div>
                            <div class="formitem"><span>Пароль:</span><br><input   class="mandatory confirmt"  type="password" title="Пароль" name="password" placeholder="обязательное поле"></div>
                            <div class="formitem"><span>Подтвердить пароль:</span><br><input class="mandatory confirm"   type="password" title="Пароль" placeholder="обязательное поле"></div>
                            <div class="formitem"><span>E-mail:</span><br><input  type="text" title="e-mail" name="e-mail" value="${regData['3']}" placeholder="очень желательное поле"></div>
                            <div class="formitem"><span>Имя:</span><br><input  type="text" title="Ваше имя" name="name" value="${regData['1']}" placeholder="не обязательное поле"></div>
                            <div class="formitem"><span>Фамилия:</span><br><input  type="text" title="Ваша фамилия" value="${regData['2']}" name="family" placeholder="не обязательное поле"></div>
                            <input type="button" onclick="validate(this.form)" value="Зарегистрировать">
                        </div>
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
        <script src="scripts/myJavaScript.js"></script>
        <script>$("input.mandatory").on('blur keyup', function() {
                                emptyCheck(this, "<span class='mandatory error'>Поле не может быть пустым</span><br class='mandatory error'>");
                            });

                            $("input.confirm").bind('blur', function() {
                                confirmationCheck(this, $("input.confirmt"), "<span class='confirm error'>Значение поля не совпадает с полем</span><br class='confirm error'>");
                            });
                            $("input.confirm").bind('keyup', function() {
                                if ($(this).siblings(".confirm.error").length !== 0)
                                    confirmationCheck(this, $("input.confirmt"), "<span class='confirm error'>Значение поля не совпадает с полем</span><br class='confirm error'>");
                            });
                            $("input[name='login']").bind('keyup', function() {
                                uniqueCheck(this, {name: this.value}, "GET", "<c:url value='Registration.do'/>", "<span class='unique error'>К сожалению логин уже занят</span><br class='unique error'>");
                            });

                            $("input.confirm").bind('keyup', function() {
                                if ($(this).siblings(".confirm.error").length != 0)
                                    confirmationCheck(this);
                            });
                            $("input[type='text'], input[type='password']").on('keyup blur', function() {
                                dataValidCheck(this, '^[a-z,A-z,a-я,А-Я,0-9]+', "<span class='invalid error'>Значение поля должно начинаться с цифры или буквы.\n\
                 </span><br class='invalid error'>");
                            });
        </script>     
    </body>
</html> 
