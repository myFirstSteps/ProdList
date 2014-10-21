<%-- 
    Document   : productinfo
    Created on : 16.10.2014, 14:06:43
    Author     : pankratov
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<!DOCTYPE html>

<c:set var="prodImg" value="${addedProduct.imageLinks[0]}"/>
<table class="productTable">

    <tr>
        <c:if test="${prodImg ne null}">
            <th>Изображение</th>
            </c:if>
        <th>Название</th>
        <th>Уточняющее название</th>
        <th>Производитель</th>
        <th>Группа</th>
        <th>Объем</th>
        <th>Цена</th>
        <th>Комментарий</th>
    </tr>
    <tr>
        <c:if test="${prodImg ne null}">
            <td id="prodTableImg"><img  src="<c:url value='${prodImg}'/>"   height="80" alt="изображение продукта"></td>
            </c:if>
        <td>${addedProduct.name}</td>
        <td>${addedProduct.subName}</td>
        <td>${addedProduct.producer}</td>
        <td>${addedProduct.group}</td>
        <td>${addedProduct.value} ${addedProduct.valueUnits}</td>
        <td>${addedProduct.price} руб</td>
        <td>${addedProduct.comment}</td>
    </tr>

</table>


<div id="temp_prodholder">
    <div class="temp_prodheader"> 
        <div class="temp_raw">Изображение</div>
        <div class="temp_raw">Категория</div>
        <div class="temp_raw">Название</div>
        <div class="temp_raw">Уточняющее название</div>
        <div class="temp_raw">Производитель</div>
        <div class="temp_raw">Объем</div>
        <div class="temp_raw">Группа</div>
        <div class="temp_raw">Цена</div>
        <div class="temp_raw">Комментарий</div>
    </div>
     <div class="temp_prodheader"> 
        <div class="temp_raw">Изображеvvvvvcvcvcvние</div>
        <div class="temp_raw">Категория</div>
        <div class="temp_raw">Название</div>
        <div class="temp_raw">Уточняющее название</div>
        <div class="temp_raw">Производитель</div>
        <div class="temp_raw">Объем</div>
        <div class="temp_raw">Группа</div>
        <div class="temp_raw">Цена</div>
        <div class="temp_raw">Комментарий</div>
    </div>
</div>

