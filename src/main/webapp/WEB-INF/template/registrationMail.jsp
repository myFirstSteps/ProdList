<%-- 
    Document   : registrationMail
    Created on : 25.09.2014, 11:46:52
    Author     : pankratov
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<html>
    
       
      
        <table style=" border-collapse: collapae; text-align: center; width:100%; height:100%;  position: relative; top: -60%; vertical-align: center; border: black thin; border-radius: 8px; color: black">
           
            <tr>
                <td>
                      
                        <div style=" margin: auto;   background-color:#e6f7f4; border-radius: 8px;  padding: 4px; width:70%; height:auto;">
                            <h2>Добро пожаловать, <em><c:choose> <c:when test="${user.firstName!=''}">${user.firstName}</c:when>
                                        <c:otherwise>${user.login}</c:otherwise></c:choose>!</em></h2>
                            <p>Поздравляем Вас, с успешной регистрацией на нашем <a href="${APPLICATION_URL}">портале</a>.
                            </p><p>Ваши данные для авторизации:<br>
                                <b>логин: <em>${user.login}</em></b><br>
                                <b>пароль: <em>${user.password}</em></b>
                            </p>
                        </div>
                    
                </td>
            </tr>
            <tr>
                <td>
                     <img style=" border-radius: 8px;" height="600px" width="600px" src="/ProdList/resources/bird_image_2.jpg"/>
                    </td>
                </tr>
        </table>
    </div>
</html>