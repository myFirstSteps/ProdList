<%-- 
    Document   : errorPage
    Created on : 29.10.2014, 23:18:35
    Author     : pankratov
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Ошибка</title>
 <link rel="stylesheet" href="CSSdoc/mainCSS.css">
</head>

<body>
    <c:import url='/WEB-INF/template/headtemplate.jsp' charEncoding='UTF-8'/>
    
    <div class="centerDiv">
              <c:choose>               
                  <c:when test="${pageContext.errorData.statusCode eq 404}">
                      <img class="errorImg" src=" resources/common_image/error_pics/error_404.png">
                      <h4> Мы всюду искали "${pageContext.errorData.requestURI}", но похоже его у нас просто нет.</h4>
                  </c:when>
                  <c:when test="${pageContext.errorData.throwable ne null}">
                      <img class="errorImg" src=" resources/common_image/error_pics/exeption.png">
                      <h4> Похоже я где-то ошибся. Если быть точнее: ${pageContext.exception.message}.</h4>
                  </c:when>
                  <c:when test="${pageContext.errorData.statusCode ne 0}">     
                       <img class="errorImg" src="resources/common_image/error_pics/error_page.png">
                       <h4> Что-то пошло не так. Надеемя это скоро пройдет.</h4>
                  </c:when>
                  <c:otherwise>
                     <h2>Кажется пока всё хорошо. Не на что жаловаться.</h2>
                  </c:otherwise>    
              </c:choose>
              
    </div>
</body>
</html>