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
                $(a).text($(o).siblings("input").val());
                if ($(a).closest("div.prodrow").next(".SyncButton").length === 0) {
                    $(a).closest("div.prodrow").after("<button class='SyncButton' onclick='sendChanges(this)'><img height='16' width='16' src='resources/common_image/icons/Sync.gif'>Изменить</button>");
                }
            } else {
                $(o).parent().remove();
            }
            $(a).append("<button  onclick='edit(this)'><img height='16' width='16' src='resources/common_image/icons/Modify.gif'></button>");
        }
        function denyEdit(o) {
            $(o).parent().replaceWith("<button  onclick='edit(this)'><img height='16' width='16' src='resources/common_image/icons/Modify.gif'></button>");
        }
        function sendChanges(o){
                //   JSON.stringify(["aname":"name"]);
           /*     $.getJSON( "ProductAutocomplete", { name: "John", time: "2pm" } )
                .done(function( json ) {
                //console.log( "JSON Data: " + json.users[ 3 ].name );
                })
                .fail(function( jqxhr, textStatus, error ) {
              //  var err = textStatus + ", " + error;
                  //  console.log( "Request Failed: " + err );
                });
                */
                    $.getJSON("ProductAutocomplete", "term", function(data, status, xhr) {
                                alert("vv");
                            });
                   alert();
        
        }
    </script>    
</c:if>
