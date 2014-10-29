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
            <div  id="${prod.id}_${prod.originID}<c:if test="${prod.origin}">_o</c:if>" class="prodrow "> 
                <div class="proddata"> 
                    <c:if test="${prod.origin}"><img class="prodStatIcon"  height="16" width="16" src='resources/common_image/icons/Key.gif' alt="Ключевой"> <br></c:if>
                    <img  height="80" onerror="this.src = 'resources/common_image/product_categories/0.png'"   src='<c:choose> 
                              <c:when test="${prod.imageLinks[0] ne null}"><c:url value='${prod.imageLinks[0]}'/>
                              </c:when>
                              <c:otherwise>resources/common_image/product_categories/0.png
                              </c:otherwise>
                          </c:choose>'>
                </div>
                <div class="proddata group">${prod.group}</div>
                <div class="proddata name">${prod.name}</div>
                <div class="proddata subName">${prod.subName}</div>
                <div class="proddata producer">${prod.producer}</div>
                <div class="proddata valueUnits">${prod.value} ${prod.valueUnits}</div>
                <div class="proddata price">${prod.price}  <button onclick="edit(this)"><img height="16" width="16" alt="edit" src='resources/common_image/icons/Modify.gif'></button></div>
                <div class="proddata comment">${prod.comment}<button onclick="edit(this)"><img height="16" width="16"  alt="edit" src='resources/common_image/icons/Modify.gif' ></button></div>
            </div>
        </c:forEach>
    </div>
    <script>
        var modifyButton="<button  onclick='edit(this)'><img height='16' width='16' alt='edit' src='resources/common_image/icons/Modify.gif'></button>";
        var syncButton="<button class='SyncButton' onclick='sendChanges(this)'><img height='16' width='16' src='resources/common_image/icons/Sync.gif'>Изменить</button>"
        var errIco="<img class='error' height='20' width='20' src='resources/common_image/icons/Error.ico' alt='error' >";
    function edit(o) {
            $(o).replaceWith("<div class='editValues'><input  type='text' value='" + $(o).parent().text().trim() + "'><br><button class='accept' onclick='acceptEdit(this)'><img height='16' width='16'\n\
      src='resources/common_image/icons/Yes.gif'></button>\n\
     <button onclick='denyEdit(this)'><img height='16' width='16' src='resources/common_image/icons/No.gif'></button></div>");
            $(".price .editValues input").on("change blur keyup", function() {
                dataValidCheck(this, '^[0-9]+(?:[.|,])?[0-9]*$', "<span class='invalid error'>Значение поля должно быть целым или десятичным числом.\n\
                 </span><br class='invalid error'>");
                if ($('.editValues .error').length !== 0)
                    $(".accept").attr("disabled", "disabled");
                else
                    $(".accept").removeAttr("disabled");
            });
                       
        }
        function acceptEdit(o) {
            var a = $(o).parent().parent();
            if (a.text().trim() !== $(o).siblings("input").val()) {
                $(a).css("color", "blue");
                $(a).addClass("edited");
                $(a).text($(o).siblings("input").val());
                $(a).closest("div.prodrow").next(".error").remove();
                if ($(a).closest("div.prodrow").next(".SyncButton").length === 0) {
                    $(a).closest("div.prodrow").after(syncButton);
                }
            } else {
                $(o).parent().remove();
            }
            $(a).append(modifyButton);
        }
        function denyEdit(o) {
            $(o).parent().replaceWith(modifyButton);
        }
        function sendChanges(o) {
            if($(o).hasClass("pressed"))return;
            $(o).addClass("pressed");
            var prodrow=$(o).prev(".prodrow");
             
             var idParts=prodrow.attr("id").split("_");
             var id=idParts[0];
             var originId=idParts[1];
             var origin= idParts.length>2?"true":"false"; 
            var req = JSON.stringify(
                    [{
                     "id":id,
                     "originID":originId,
                     "origin": origin,
                     "name": prodrow.children("div.proddata.name.edited").text(),
                     "producer": prodrow.children("div.proddata.producer.edited").text(),
                     "group": prodrow.children("div.proddata.group.edited").text(),
                     "subName": prodrow.children("div.proddata.subName.edited").text(),
                     "price": prodrow.children("div.proddata.price.edited").text(),
                     "valueUnits": prodrow.children("div.proddata.valueUnits.edited").text(),
                     "value": prodrow.children("div.proddata.value.edited").text(),
                     "comment": prodrow.children("div.proddata.comment.edited").text()
                     }] 
                    );
                    $(o).append("<img src='resources/common_image/icons/loading.gif'>");
                    $.getJSON("ChangeProducts", {product: req}, function(data, status, xhr) {
                      if(data.error===undefined){
                      prodrow.children("div.proddata.name.edited").text(data.name).css("color","green").append(modifyButton);
                      prodrow.children("div.proddata.subName.edited").text(data.subName).css("color","green").append(modifyButton);
                      prodrow.children("div.proddata.group.edited").text(data.group).css("color","green").append(modifyButton);
                      prodrow.children("div.proddata.price.edited").text(data.price).css("color","green").append(modifyButton);
                      prodrow.children("div.proddata.group.edited").text(data.group).css("color","green").append(modifyButton);
                      prodrow.children("div.proddata.comment.edited").text(data.comment).css("color","green").append(modifyButton);
                      prodrow.next(".SyncButton").remove();}
                      else{
                          $(prodrow).next(".SyncButton").replaceWith(errIco);
                          $(prodrow).next(".error").attr("title",data.error);
                      }
                    });
                }
    </script>    
</c:if>
