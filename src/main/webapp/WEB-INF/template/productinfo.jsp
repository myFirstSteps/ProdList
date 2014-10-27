<%-- 
    Document   : productinfo
    Created on : 16.10.2014, 14:06:43
    Author     : pankratov
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<!DOCTYPE html>

<c:if test="${products[0] ne null}">


    <div id="prodtable">
        <div class="prodheader"> 
            <div class="proddata">Изображение</div>
            <div class="proddata">Категория</div>
            <div class="proddata">Название</div>
            <div class="proddata">Уточняющее название</div>
            <div class="proddata">Производитель</div>
            <div class="proddata">Объем</div>
            <div class="proddata">Цена, руб</div>
            <div class="proddata">Комментарий</div>
        </div>
        <img  onerror="this.src = 'resources/common_image/product_categories/0.png'">
        <c:forEach items="${products}" var="prod" varStatus="stat">
            <div  id="${prod.id}_${prod.originID}" class="prodrow "> 
                <div class="proddata"> 
                    <c:if test="${prod.origin}"><img class="prodStatIcon"  height="16" width="16" src='resources/common_image/icons/Key.gif' alt="Ключевой"> <br></c:if>
                    <img  height="80" onerror="this.src = 'resources/common_image/product_categories/0.png'"   src='<c:choose> 
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
                <div class="proddata">${prod.price}  <button onclick="edit(this)"><img height="16" width="16" alt="edit" src='resources/common_image/icons/Modify.gif'></button></div>
                <div class="proddata">${prod.comment}<button onclick="edit(this)"><img height="16" width="16"  alt="edit" src='resources/common_image/icons/Modify.gif' ></button></div>
            </div>
        </c:forEach>
    </div>
    <script>
        function edit(o) {
            $(o).replaceWith("<div><input type='text' value='" + $(o).parent().text() + "'><br><button onclick='acceptEdit(this)'><img height='16' width='16'\n\
      src='resources/common_image/icons/Yes.gif'></button>\n\
     <button onclick='denyEdit(this)'><img height='16' width='16' src='resources/common_image/icons/No.gif'></button></div>");
        }
        function acceptEdit(o) {
            var a = $(o).parent().parent();
            $(a).css("color", "blue");
            a.text($(o).siblings("input").val());
            a.append("<button onclick='edit(this)'><img height='16' width='16' src='resources/common_image/icons/Modify.gif'></button>");
            $(a).closest("div.prodrow").after("<button><img height='16' width='16' src='resources/common_image/icons/Sync.gif' >Изменить</button>");
            
        }
        function denyEdit(o) {
            $(o).parent().replaceWith("<button onclick='edit(this)'><img height='16' width='16' src='resources/common_image/icons/Modify.gif'></button>");
        }
    </script>    
</c:if>
