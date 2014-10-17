<%-- 
    Document   : productinfo
    Created on : 16.10.2014, 14:06:43
    Author     : pankratov
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<!DOCTYPE html>

<c:set var="prodImg" value="${addProduct.imageLinks[0]}"/>
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
                <td>${addProduct.name}</td>
                <td>${addProduct.subName}</td>
                <td>${addProduct.producer}</td>
                <td>${addProduct.group}</td>
                <td>${addProduct.value} ${addProduct.valueUnits}</td>
                <td>${addProduct.price} руб</td>
                <td>${addProduct.comment}</td>
            </tr>
      
    </table>

