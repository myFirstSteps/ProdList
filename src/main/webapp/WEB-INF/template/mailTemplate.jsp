<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<c:set var="URL" value="${pageContext.servletContext.getInitParameter('APPLICATION_URL')}"/>

<html>


    
    <table style=" border-collapse: collapae; text-align: center; width:70%; margin: auto; height:100%;  position: relative; top: -60%; vertical-align: middle;  color: black">
        <tr style="background-color: #303030; color: white; font-weight: bold; text-align: left; padding: 5 px 20px 5px 20px; vertical-align: middle;">
            <td><table><tr><td style=" text-align: center;">Интерактивное резюме на позицию<br> Java Junior Developer.<br><em>Панкратов Михаил</em></td><td> <img style="border-radius: 4px;" src="<c:url value="/resources/common_image/OCP_JavaSE7Programmer.gif"/>"  
                      width="140" height="71" alt="OCP_JavaSE7Programmer.gif"></td></tr></table>
           </td>
        </tr>
        <tr>
            <td>
                <div style=" margin: auto;   background-color:#f0f0f0;   padding: 4px;  height:auto;">
                    <c:choose>
                        <c:when test="${mailType eq 'registration'}">
                    <h2>Добро пожаловать, <em><c:choose> <c:when test="${user.firstName!=''}">${user.firstName}</c:when>
                                <c:otherwise>${user.login}</c:otherwise></c:choose>!</em></h2>
                    <p>Поздравляю Вас с успешной регистрацией на моем <a target="_blank" href="${URL}">портале</a>.
                    </p></c:when>
                    <c:when test="${mailType eq 'passrestore'}">
                        <h2>Вами были запрошены данные аутентификации на ${URL}:</h2>
                    </c:when>
                    </c:choose>
                    <p>Ваши данные для авторизации:<br>
                        <b>логин: <em>${user.login}</em></b><br>
                        <b>пароль: <em>${user.password}</em></b>
                    </p>
                    <p style="text-indent: 30px; ">
                        Напоминаю, что это всего лишь учебный проект. Целью проекта является  показать наличие у меня начальных навыков <em>Java EE</em>.
                        Данный проект бесконечно далек от завершения, а код - от идеала, надеюсь, совершенствовать его длительное время, пробуя на нем все новое и интересное с чем буду сталкиваться.
                        Ну, а пока основная цель - найти работу Java Junior Developer. Чему, надеюсь, поспособствует этот ресурс.
                    </p>
                    
                   
                    <a target="_blank" href="<c:url value="${URL}/editProduct.jsp"/>">Учебный проект</a>
                    &nbsp&nbsp&nbsp
                    <a target="_blank" href="https://github.com/myFirstSteps/ProdList">Исходный код</a>
                    &nbsp&nbsp&nbsp
                    <a target="_blank" href="<c:url value="${URL}/valuation.jsp"/>">Оставить отзыв</a>.
                </div>
            </td>
        </tr>
        <tr>

        </tr>
    </table>
</html>
