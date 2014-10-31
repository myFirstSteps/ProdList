<%-- 
    Document   : productinfo
    Created on : 16.10.2014, 14:06:43
    Author     : pankratov
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<!DOCTYPE html>
<c:set var="modifyButtonTemplate" value="<button onclick='edit(this)'><img height='16' width='16' alt='edit' src='resources/common_image/icons/Modify.gif'></button>"/>
<c:if test="${isAdmin}"><c:set var="AdminModifyButton" value='${modifyButton}'/></c:if>
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
            <c:if test="${isAdmin}"><div class="proddata">Добавил</div></c:if>
            </div>
            <img  onerror="this.src = 'resources/common_image/product_categories/0.png'">
        <c:forEach items="${products}" var="prod" varStatus="stat">
            <c:set var="ModifyButton" value=""/>
            <c:if test='${(!prod.origin  and (prod.author eq username or prod.author eq cookie.clid.value) and prod.originID eq -1) or isAdmin }'>
                <c:set var="ModifyButton" value="${modifyButtonTemplate}"/>
            </c:if>
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
                <div class="proddata name">${prod.name}${ModifyButton}</div>
                <div class="proddata subName">${prod.subName}${ModifyButton}</div>
                <div class="proddata producer">${prod.producer}${ModifyButton}</div>
                <div class="proddata valueUnits">${prod.value} ${prod.valueUnits}</div>
                <div class="proddata price">${prod.price}${ModifyButton}</div>
                <div class="proddata comment">${prod.comment}${ModifyButton}</div>
                <c:if test="${isAdmin}"><div class="proddata author">${prod.author}</div></c:if>
                </div>
                <div  class="buttons">ttt</div>
        </c:forEach>
    </div>
          
    <script>
        $(document).ready(function(){
            $(".test").css("max-width", window.screen.width*0.10);
        });
        var modifyButton = "${modifyButtonTemplate}";
        var syncButton = "<button class='SyncButton' onclick='sendChanges(this)'><img height='16' width='16' src='resources/common_image/icons/Sync.gif'>Изменить</button>";
        var errIco = "<img class='error' height='20' width='20' src='resources/common_image/icons/Error.ico' alt='error' >";
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
            var cell = $(o).parent().parent();
            var buttons = $(cell).closest("div.prodrow").next(".buttons")
            if (cell.text().trim() !== $(o).siblings("input").val()) {
                $(cell).css("color", "blue");
                $(cell).addClass("edited");
                $(cell).text($(o).siblings("input").val());
                $(buttons).children(".error").remove();
                if ($(buttons).children(".SyncButton").length === 0) {
                    $(buttons).append(syncButton);
                }
            } else {
                $(o).parent().remove();
            }
            $(cell).append(modifyButton);
        }
        function denyEdit(o) {
            $(o).parent().replaceWith(modifyButton);
        }
        function sendChanges(o) {
            if ($(o).hasClass("pressed"))
                return;
            $(o).addClass("pressed");
            var prodrow = $(o).parent().prev(".prodrow");

            var idParts = prodrow.attr("id").split("_");
            var id = idParts[0];
            var originId = idParts[1];
            var origin = idParts.length > 2 ? "true" : "false";
            var classes = ["name", "producer", "group", "subName", "price", "valueUnits", "value", "comment"];
            var ajson = {
                "id": id,
                "originID": originId,
                "origin": origin};

            $.each(classes, function(i, e) {
                var x;
                x = $(prodrow).children("div.proddata.edited." + e).length > 0 ? $(prodrow).children("div.proddata.edited." + e).text() : null;
                if (x !== null)
                    ajson[e] = x !== '' ? x : '\u007F';
            });


            var req = JSON.stringify([ajson]);
            $(o).append("<img src='resources/common_image/icons/loading.gif'>");
            $.getJSON("ChangeProducts", {product: req}, function(data, status, xhr) {
                if (data.error !== null) {
                    $.each(data, function(i, e) {
                        prodrow.children("div.proddata." + i + ".edited").text(e).css("color", "green").removeClass("edited").append(modifyButton);
                    });
                    $(prodrow).next(".buttons").children(".SyncButton").remove();
                }
                else {
                    $(prodrow).next(".buttons").children(".SyncButton").replaceWith(errIco);
                    $(prodrow).next(".buttons").children(".error").attr("title", data.error);
                }
                $(prodrow).children("div.proddata.edited").removeClass(".edited");
            });
        }
    </script>    
</c:if>
