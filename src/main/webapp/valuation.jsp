
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="product" uri="ProductsEL" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Просмотр списков покупок.</title>
        <link href='CSSdoc/mainCSS.css' rel='stylesheet' type="text/css">
    </head>
    <body>
        <c:import url="WEB-INF/template/headtemplate.jsp"/>
        <div  class='panel'> 
            <c:if test="${success eq null}"><h2>Оцените ресурс и оставьте свой отзыв.</h2></c:if>
            <h4 id='error'  class='error'>${error}</h4>
            <h2 id='success' > ${success}</h2>
            <img  src='resources/common_image/Valuation.png'>
            <c:if test="${success eq null}">
                <form id="valuat" method="post" action="<c:url value='Valuation.do'/>">
                    <input type="hidden" name="action" value="write">
                    <span>Оценка:</span> <c:forEach begin="1" varStatus="st" end="5"> <input value="${st.index}" name="rating" type="radio">${st.index}</c:forEach>
                        <br> <div style="margin-top: 10px;"><span>Напишите отзыв:</span><br><textarea rows="5" cols="60" name="reference" form="valuat"></textarea></div>
                        <br><input type="submit" class="pointer" value="Отправить">
                    </form>
            </c:if>
            <div id="valuations">
                <c:forEach var="vals" items="${valuations}">
                    ${vals.timeStamp}
                    <c:forEach begin="1" var="star" end="${vals.rating}"><img src="${icons}Star.gif" alt="*"></c:forEach> 
               <p>${vals.reference}</p>
                </c:forEach>
            </div>
        </div>
        <script>
            $("#valuation").hide();
        </script>
    </body>
</html>

