<%-- 
    Document   : productinfo
    Created on : 16.10.2014, 14:06:43
    Author     : pankratov
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<!DOCTYPE html>

<c:if test="${products ne null}">
    <div id="prodtable">
        <div class="prodheader"> 
            <div class="proddata">Изображение</div>
            <div class="proddata">Категория</div>
            <div class="proddata">Название</div>
            <div class="proddata">Уточняющее название</div>
            <div class="proddata">Производитель</div>
            <div class="proddata">Объем</div>
            <div class="proddata">Цена</div>
            <div class="proddata">Комментарий</div>
        </div>
        <c:forEach items="${products}" var="prod" varStatus="stat">
            <div class="prodrow"> 
                <div class="proddata"> 
                    <img  height="80" src='<c:choose> 
                             <c:when test="${prod.imageLinks[0] ne null}"><c:url value='${prod.imageLinks[0]}'/>
                             </c:when>
                             <c:otherwise>resources/common_image/product_categories/0.png
                             </c:otherwise>
                         </c:choose>'>
                </div>
                <div class="proddata">${prod.group}</div>
                <div class="proddata">${prod.name}</div>
                <div class="proddata">${prod.subName}</div>
                <div class="proddata">${prod.producer}</div>
                <div class="proddata">${prod.value} ${prod.valueUnits}</div>
                <div class="proddata">${prod.price}</div>
                <div class="proddata">${prod.comment}</div>
            </div>
        </c:forEach>
    </div>
</c:if>
