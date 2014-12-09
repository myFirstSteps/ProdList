<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<!DOCTYPE html>
<c:set scope="session" var="isAdmin" value="${role=='admin'}"/>
<c:set var="icons" value='${pageContext.servletContext.getInitParameter("icons")}'/>
<c:set var="OriginProdIco" value="<img class='prodStatIcon' title='Ключевой продукт. 
       Этот продукт виден всем пользователям и являющийся прообразом для пользовательских продуктов.'
       height='16' width='16' src='resources/common_image/icons/Key.gif' alt='Ключевой'>"/>
<c:set var="modifyButtonTemplate" value="<button onclick='edit(this)' 
       title='Редактировать'><img height='16' width='16' alt='edit' src='${icons}Modify.gif'></button>"/>
<c:set var="UsersProdIco" value="<img class='prodStatIcon' title='Пользовательская копия продукта.
       Вы можете изменить некоторые свойства своей копии  ключевого продукта (задать цену, комментарий и т.д.).
       При этом, не тронутые свойства, будут соответствовать свойствам ключевого продукта.
       ' height='16' width='16' src='resources/common_image/icons/Users.gif' alt='Пользовательский'>"/>
<c:set var="newProdIco" value="<img class='prodStatIcon' 
       title='Новый продукт. Этот продукт еще не прошел проверку и не добавлен в общую базу. 
       Пока он виден только Вам, но Вы можете с ним работать, добавлять его в списки и редактировать.' 
       height='16' width='16' src='${icons}New.gif' alt='Новый'>"/>
<c:if test="${products[0] ne null}">


    <table id="prodtable">
        <tr  class="prodheader"> 
            <th class="proddata">Изображение</th>
            <th class="proddata">Категория</th>
            <th class="proddata">Название</th>
            <th class="proddata">Уточняющее название</th>
            <th class="proddata">Производитель</th>
            <th class="proddata">Кол-во единиц</th>
            <th class="proddata">Единицы</th>
            <th class="proddata">Цена, руб</th>
            <th class="proddata">Комментарий</th>
            <c:if test="${isAdmin}"><th class="proddata">Добавил</th></c:if>
            <c:if test="${not productsOnly}"> <th class="proddata"></th> </c:if>
            </tr>
        <c:forEach items="${products}" var="prod" varStatus="stat">
            <c:set var="ModifyButton" value=""/>
            <c:set var="DeleteButton" value=""/>
            <c:if test='${ productsOnly eq null and (!prod.origin  and (prod.author eq client or prod.author eq cookie.clid.value) ) or isAdmin }'>
                <c:set var="ModifyButton" value="${modifyButtonTemplate}"/><c:set var="DeleteButton" value='<button title="Удалить продукт" onclick="deleteProduct(this)">
                                                                                  <img src="${icons}Delete.gif" alt="Удалить"></button>'/>         
            </c:if>
            <tr  id="${prod.id}_${prod.originID}<c:if test="${prod.origin}">_o</c:if>" class="prodrow "> 
                    <td class="proddata">
                    <c:choose>
                        <c:when test="${prod.origin}">${OriginProdIco}</c:when>
                        <c:when test="${not prod.origin and prod.originID eq -1}">${newProdIco}</c:when>
                        <c:when test="${not prod.origin and prod.originID ne -1}">${UsersProdIco}</c:when>
                    </c:choose>    
                    <br>    
                    <img  class='prodImg' height="80" onerror="this.src = '${icons}No_Image.gif'"   src='<c:choose> 
                              <c:when test="${prod.imageLinks[0] ne null}"><c:url value='${prod.imageLinks[0]}'/>
                              </c:when>
                              <c:otherwise>${icons}No_Image.gif
                              </c:otherwise>
                          </c:choose>'>
                </td>
                <td class="proddata group">${prod.group}</td>
                <td class="proddata name">${prod.name}${ModifyButton}</td>
                <td class="proddata subName">${prod.subName}${ModifyButton}</td>
                <td class="proddata producer">${prod.producer}${ModifyButton}</td>
                <td class="proddata value">${prod.value}${ModifyButton} </td>
                <td class="proddata valueUnits">${prod.valueUnits}</td>
                <td class="proddata price">${prod.price}${ModifyButton}</td>
                <td class="proddata comment">${prod.comment}${ModifyButton}</td>
                <c:if test="${isAdmin}"><td class="proddata author">${prod.author}</td></c:if>
                <c:if test="${not productsOnly}">
                <td class="proddata">${DeleteButton}
                    <c:if test="${prod.origin and not isAdmin}">
                        <button class='cloneButton' title="Создать пользовательскую копию." onclick="clone(this)">
                            <img src="${icons}Copy.gif" alt="Клонировать"></button>
                        </c:if>
                        <c:if test="${not prod.origin and isAdmin and prod.originID eq -1}"><button class='legalizeButton' title="Добавить в основную базу." onclick="legalize(this)">
                            <img src="resources/common_image/icons/Yes.gif" alt="Легализовать"></button></c:if>
                    </td>
                    </c:if>
                </tr>

        </c:forEach>
    </table>
            <c:if test="${productsOnly eq null }">
    <script>
        /*Здесь очень много кривого, избыточного, безобразного javascript кода. Его обязательно нужно переработать, но пока, у этой задачи низкий приоритет.*/
        $(document).ready(function() {
            $(".proddata").css("max-width", $("#prodtable").parent().innerWidth() * 0.25);
        });
        var modifyButton = "${modifyButtonTemplate}";
        var userProdIco = "${UsersProdIco}";
        var originProdIco = "${OriginProdIco}";
        var syncButton = "<button class='SyncButton' onclick='sendChanges(this)'><img height='16' width='16' src='${icons}Sync.gif'>Изменить</button>";
        var errIco = "<img class='error' height='20' width='20' src='${icons}Error.ico' alt='error' >";
        function edit(o) {
            $(o).replaceWith("<div class='editValues'><input  type='text' value='" + $(o).parent().text().trim() + "'>\n\
   <br><button class='accept' onclick='acceptEdit(this)'><img height='16' width='16'\n\
     src='${icons}Yes.gif'></button>\n\
    <button onclick='denyEdit(this)'><img height='16' width='16' src='${icons}No.gif'></button></div>");
            $(".price .editValues input, .value .editValues input").on("change blur keyup", function() {
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
            var x = $(cell).closest("tr.prodrow");

            var buttons = $(cell).closest("tr.prodrow").next(".buttons").children("td");
            if (cell.text().trim() !== $(o).siblings("input").val()) {
                $(cell).css("color", "blue");
                $(cell).addClass("edited");
                $(cell).text($(o).siblings("input").val());
                $(buttons).children(".error").remove();
                if ($(x).next(".buttons").length === 0) {
                    $(x).after('<tr  style="text-align: left" class="buttons"><td colspan="8">' + syncButton + '</td></tr>');
                }
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
            var prodrow = $(o).parents('tr').prev("tr");
            var json = SplitID(prodrow.attr("id"));
            var classes = ["name", "producer", "group", "subName", "price", "valueUnits", "value", "comment"];
            $.each(classes, function(i, e) {
                var x;
                x = $(prodrow).children("td.proddata.edited." + e).length > 0 ? $(prodrow).children("td.proddata.edited." + e).text() : null;
                if (x !== null)
                    json[e] = x !== '' ? x : '\u007F';
            });
            var req = JSON.stringify([json]);
            $(o).append("<img src='${icons}loading.gif'>");
            $.post('<c:url value= "ChangeProducts.do"/>', {product: req, action: "change"}, function(data, status, xhr) {
                if (data.error === undefined) {
                    $.each(data.product, function(i, e) {
                        prodrow.children("td.proddata." + i + ".edited").text(e).css("color", "green").removeClass("edited").append(modifyButton);
                    });
                    $(prodrow).next(".buttons").children("td").children(".SyncButton").remove();
                    if ($(prodrow).next(".buttons").children("td").children().length === 0)
                        $(prodrow).next(".buttons").remove();
                }
                else {
                    $(prodrow).next(".buttons").children("td").children(".SyncButton").replaceWith(errIco);
                    $(prodrow).next(".buttons").children("td").children(".error").attr("title", data.error);
                }
                $(prodrow).children("td.proddata.edited").removeClass(".edited");
            });
        }
        function clone(o) {
            PostJSON(o, "clone");
            return;
        }
        function legalize(o) {
            PostJSON(o, "legalize");
        }
        function deleteProduct(o) {
            PostJSON(o, "delete");
        }
        function PostJSON(o, action) {
            splash.show();
            var row = $(o).parents(".prodrow");
            var json = SplitID($(row).attr("id"));
            var req = JSON.stringify([json]);
            $.post('<c:url value="ChangeProducts.do"/>', {product: req, action: action}, function(data, status, xhr) {
                splash.hide();
                if (data.error === undefined) {
                    $(row).next(".buttons").remove();
                    switch (data.action) {
                        case "delete":
                            $(row).remove();
                            break;
                        case "clone":
                        case "legalize":
                            var prodClone = $(row).clone();
                            $(row).after(prodClone);
                            $(prodClone).children("*").css("color", "green");
                            var origin=data.product.origin===true?"_o":"";
                            $(prodClone).attr("id", data.product.id + "_" + data.product.originID+origin);
                            var modFields = ["name", "producer", "subName", "price", "value", "comment"];
                            $.each(modFields, function(i, e) {
                                $(prodClone).children("td.proddata." + e).text(data.product.e).append(data.action === "clone" ? modifyButton : "");
                            });
                            if (data.action === "clone") {
                                $(prodClone).find(".cloneButton").replaceWith("<button title='Удалить продукт' onclick='deleteProduct(this)'>\n\
        <img src='${icons}Delete.gif' alt='Удалить'></button>");
                                $(prodClone).find(".prodStatIcon").replaceWith(userProdIco);
                            } else {
                                $(prodClone).find(".legalizeButton").remove();
                                 $(prodClone).find(".prodStatIcon").replaceWith(originProdIco);
                            }
                            break;
                    }
                }
                else {
                    $(o).replaceWith(errIco);
                    $(row).find('.error').attr("title", data.error);
                }
            });
        }
    </script>    
    </c:if>
</c:if>
