<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="product" uri="ProductsEL" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Редактор продуктов.</title>
        <link href='CSSdoc/mainCSS.css' rel='stylesheet' type="text/css">
        <link rel="stylesheet" type="text/css" href='CSSdoc/jquery-ui.min.css'>
    </head>
    <body>
        <c:import url="/WEB-INF/template/headtemplate.jsp"/>
        <c:set var="imgRoot" value="/resources/common_image/product_categories/"/>
        <c:set var="imgDir" value="${pageContext.servletContext.getRealPath(imgRoot)}"/>
        <c:set var="categories" value="${product:getCategories(pageContext.servletContext)}"/>
        <div class='TabletMenu'>
            <img id="closeMenu" class="pointer" title="Закрыть" height="16" width="16" alt="закрыть" src="${icons}Close.gif">
            <c:forEach  items="${categories}" varStatus="step" var="category">
                <div class='Tablet pointer'>
                    <c:set var="imgPath" value="${imgDir}/${step.count}.png"></c:set>
                        <img height="100" width="100" src="
                        <c:choose> 
                            <c:when test='${product:isImageExist(imgPath)}'> <c:url value="${imgRoot}${step.count}.png"/></c:when>
                            <c:otherwise> <c:url value="${imgRoot}0.png"/></c:otherwise> 
                        </c:choose>
                        " alt="Картинка группы"><br>
                    <span>${category}</span>
                </div>
            </c:forEach>
        </div>
        <form  class="panel" id="newProduct" method="post"  enctype="multipart/form-data" action= '<c:url value="AddProduct.do"/>'>

            <h1>Редактор продуктов</h1>
             <h4 id='error' class='error'>${error}</h4>
            <img class="infographic" src="resources/common_image/Edit_products_infogr.png" alt=""/>       
            <c:set var="categoryValue" value="" /> 
            <c:if test="${newProduct.group ne null}"><c:set var="categoryValue" value="${newProduct.group}"/></c:if>
            <c:set var="nameValue" value="" /> 
            <c:if test="${newProduct.name ne null}"><c:set var="nameValue" value="${newProduct.name}"/></c:if>
            <c:set var="subNameValue" value="" /> 
            <c:if test="${newProduct.subName ne null}"><c:set var="subNameValue" value="${newProduct.subName}"/></c:if>
            <c:set var="producerValue" value="" /> 
            <c:if test="${newProduct.producer ne null}"><c:set var="producerValue" value="${newProduct.producer}"/></c:if>
            <c:set var="valueUnitsValue" value="" /> 
            <c:if test="${newProduct.valueUnits ne null}"><c:set var="valueUnitsValue" value="${newProduct.valueUnits}"/></c:if>
            <c:set var="priceValue" value="" /> 
            <c:if test="${newProduct.price ne -1.0 }"><c:set var="priceValue" value="${newProduct.price}"/></c:if>
            <c:set var="commentValue" value="" /> 
            <c:if test="${newProduct.comment ne null}"><c:set var="commentValue" value="${newProduct.comment}"/></c:if>
                <div  class="formitem">
                    <span>Категория:</span><br>
                    <input   size="15" type="text" name="group" class="validCheck autocomplDepended" value='${categoryValue}' id="CategorySelect" >
            </div>
            <div class="formitem">
                <span>Название:</span><br>
                <input placeholder="Обязательное поле" id="name" type="text" value='${nameValue}' autocomplete='off' name="name"  class='mandatory autocompl validCheck autocomplDepended' >
            </div>
            <div class="formitem">
                <span>Уточняющее название:</span><br>
                <input  type="text" value='${subNameValue}' class='validCheck autocompl autocomplDepended' name="subName"  >
            </div>
            <div class="formitem">
                <span>Производитель:</span><br>
                <input  type="text" name="producer"  value='${producerValue}' class="validCheck autocompl autocomplDepended" id="Name" >
            </div>
            <div class="formitem">
                <span id='valueLabel'>Объем:</span><br>
                <input  type="text" size="5" maxlength="8" class="validNumberCheck"  value='${valueValue}' name="value" >
            </div>
            <div class="formitem">
                <span>Единицы:</span><br>
                <select name="valueUnits" id='valueUnits'>
                    <c:forEach items="${product:getUnits(pageContext.servletContext)}" var="units"> 
                        <option>${units}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="formitem">
                <span>Цена (руб):</span><br>
                <input  type="text" size="5" maxlength="8" class="validNumberCheck" value='${priceValue}' name="price" >
            </div>
            <div class="formitem">
                <span>Комментарий:</span><br>
                <input  type="text" name="comment" value="${commentValue}">
            </div>
            <div class="formitem">
                <span>Прикрепить изображение:</span><br>
                <input class="pointer" type="file" id="a"   accept="image/jpeg,image/png,image/gif" name='imageFile'>
            </div>
            <div style="width:100%;">
            <button type="button" onclick="validate(this.form)" title="Добавить продукт в базу."><img alt="Добавить" height="16" width="16" src="${icons}Add.gif"></button>    
            <button formaction="<c:url value='ReadProduct.do'/>" title="Показать продукты."><span style="vertical-align: top;" id="found"></span><img alt="Показать" height="16" width="16" src="${icons}View.gif"></button><span id="found"></span>
            </div>
        </form>
        <c:if test='${products ne null}'>
            <div class='panel'>
                <c:if test="${status ne null}"><h4>${status}</h4></c:if>
                <c:import url="/WEB-INF/template/productinfo.jsp"/>         
            </div> 
        </c:if> 
        <script src="scripts/jquery-ui.js"></script>
        <script src="scripts/myJavaScript.js"></script>
        <script>
                imgMenu = new ImgMenu($('.TabletMenu'), $("#CategorySelect"));
                var testUnits = function(s) {
                    switch (s) {
                        case "кг":
                            return "Вес:";
                        case "л":
                            return "Объем:";
                        default :
                            return "В единице:";
                    }
                };
                $(document).ready(function() {
                    $("#valueUnits").change(function() {
                        $("#valueLabel").text(testUnits($("#valueUnits").val()));
                    });
                    $("#valueLabel").text(testUnits($("#valueUnits").val()));
                    $("input.mandatory").on('blur keyup', function() {
                        emptyCheck(this, "<span class='mandatory error'>Поле не может быть пустым</span><br class='mandatory error'>");
                    });
                    $("input.validCheck").on('keyup blur', function() {
                        dataValidCheck(this, '^[a-z,A-z,a-я,А-Я,0-9]+', "<span class='invalid error'>Значение поля должно начинаться с цифры или буквы.\n\
                 </span><br class='invalid error'>");
                    });
                    $("input.validNumberCheck").on('keyup blur', function() {
                        dataValidCheck(this, '^[0-9]+(?:[.|,])?[0-9]*$', "<span class='invalid error'>Значение поля должно быть целым или десятичным числом.\n\
                 </span><br class='invalid error'>");
                    });
                    $.each($("input.autocompl"), function(k, v) {
                        prodAutoComplete(v, $("input.autocomplDepended"));
                    });
                });
                function prodAutoComplete(field, dependent) {
                    $(field).autocomplete({
                        minLength: 2,
                        source: function(request, response) {
                            var term = request.term;
                            request.term = JSON.stringify($(field).serializeArray().concat($(dependent).filter("[name!=" + $(field).attr("name") + "]").serializeArray()));
                            $.getJSON("<c:url value="ProductAutocomplete.do"/>", request, function(data, status, xhr) {
                                if (data.error === undefined) {
                                    response(data);
                                } else {
                                    response(new (Object));
                                }
                            });
                        }
                    });
                }
                ;
                $("input[type='text']").on('change', function() {
                    var product = {'group': $("input[name='group']").val(),
                        'name': $("input[name='name']").val(),
                        'subName': $("input[name='subName']").val(),
                        'producer': $("input[name='producer']").val(),
                        'value': $("input[name='value']").val(),
                        'price': $("input[name='price']").val(),
                        'comment': $("input[name='comment']").val()
                    };
                    $.getJSON('<c:url value="ReadProduct.do"/>', {action: "count", product:JSON.stringify([product])}, function(data, status, xhr) {
                        if (data.error === undefined) {
                            $('#found').text("("+data.prodCount+")");
                        }else  $('#found').text("");
                    });
                });
        </script>
    </body>
</html>
