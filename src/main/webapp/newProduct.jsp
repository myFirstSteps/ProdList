<%-- 
    Document   : newProduct
    Created on : 06.10.2014, 11:24:48
    Author     : pankratov
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="product" uri="ProductsEL" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Добавить продукт.</title>
        <style><%@include file="/CSSdoc/mainCSS.css"%></style>
    </head>
    <body>
        <div id="header">
            <c:import url="/WEB-INF/template/headtemplate.jsp" charEncoding="UTF-8"/>
        </div>

        <c:set var="imgRoot" value="/resources/common_image/product_categories/"/>
        <c:set var="imgDir" value="${pageContext.servletContext.getRealPath(imgRoot)}"/>
        <c:set var="categories" value="${product:getCategories(pageContext.servletContext)}"/>
        <div class='TabletMenu'>
            <c:forEach  items="${categories}" varStatus="step" var="category">
                <div class='Tablet'>
                    <c:set var="imgPath" value="${imgDir}/${step.count}.png"></c:set>
                        <img src="
                        <c:choose> 
                            <c:when test='${product:isImageExist(imgPath)}'> <c:url value="${imgRoot}${step.count}.png"/></c:when>
                            <c:otherwise> <c:url value="${imgRoot}0.png"/></c:otherwise> 
                        </c:choose>
                        " alt="Картинка группы"><br>
                    <span>${category}</span>
                </div>
            </c:forEach>
        </div>


        <form  class="center_form" id="newProduct" method="post"  enctype="multipart/form-data" action= '<c:url value="addProduct"/>'>
            <c:if test='${addedProduct ne null}'>
                <div class="productTable">
                    <h4>Продукт успешно добавлен.</h4>

                    <c:import url="/WEB-INF/template/productinfo.jsp"/> 
                </div>
            </c:if>

            <h2>Добавить новый продукт в базу.</h2>
            <div id='error' class='error'>${error}</div><br>
            <c:set var="categoryValue" value="овощи" /> 
            <c:if test="${newProduct.group ne null}"><c:set var="categoryValue" value="${newProduct.group}"/></c:if>
            <c:set var="nameValue" value="помидоры" /> 
            <c:if test="${newProduct.name ne null}"><c:set var="nameValue" value="${newProduct.name}"/></c:if>
            <c:set var="subNameValue" value="сливовидные" /> 
            <c:if test="${newProduct.subName ne null}"><c:set var="subNameValue" value="${newProduct.subName}"/></c:if>
            <c:set var="producerValue" value="любой" /> 
            <c:if test="${newProduct.producer ne null}"><c:set var="producerValue" value="${newProduct.producer}"/></c:if>
            <c:set var="valueUnitsValue" value="кг" /> 
            <c:if test="${newProduct.valueUnits ne null}"><c:set var="valueUnitsValue" value="${newProduct.valueUnits}"/></c:if>
            <c:set var="valueValue" value="1.0" /> 
            <c:if test="${newProduct.value ne null}"><c:set var="valueValue" value="${newProduct.value}"/></c:if>
            <c:set var="priceValue" value="1.00" /> 
            <c:if test="${newProduct.price ne null}"><c:set var="priceValue" value="${newProduct.price}"/></c:if>
            <c:set var="commentValue" value="" /> 
            <c:if test="${newProduct.comment ne null}"><c:set var="commentValue" value="${newProduct.comment}"/></c:if>

                <div>
                    <span>Категория:</span><br>
                    <input   size="15" type="text" name="group" class="validCheck" value='${categoryValue}' id="CategorySelect" >
            </div>
            <div  class="ui-widget">
                <span>Название:</span><br>
                <input id="tags" type="text" value='${nameValue}' autocomplete='off' name="name"  class='mandatory validCheck"' >
            </div>
            <div>
                <span>Уточняющее название:</span><br>
                <input  type="text" value='${subNameValue}' class='validCheck' name="subName"  >
            </div>
            <div>
                <span>Производитель:</span><br>
                <input  type="text" name="producer"  value='${producerValue}' class="validCheck" id="Name" >
            </div>
            <div>
                <span id='valueLabel'>Объем:</span><br>
                <input  type="text" size="5" maxlength="8" class="validNumberCheck"  value='${valueValue}' name="value" >
            </div>
            <div>
                <span>Единици:</span><br>
                <select name="units" id='valueUnit'>
                    <c:forEach items="${product:getUnits(pageContext.servletContext)}" var="unit"> 
                        <option>${unit}</option>
                    </c:forEach>
                </select>
            </div>
            <div>
                <span>Цена (руб):</span><br>
                <input  type="text" size="5" maxlength="8" class="validNumberCheck" value='${priceValue}' name="price" >
            </div>
            <div>
                <span>Комментарий:</span><br>
                <input  type="text" name="comment" value="${commentValue}">
            </div><br>
            <div id='img_file'>
                <span>Прикрепить изображение:</span><br>
                <input type="file" id="a"   accept="image/jpeg,image/png,image/gif" name='imageFile'>
            </div><br>
            <input type="button" onclick="validate(this.form)" value="Добавить">
        </form>

        

        <script src="scripts/jquery-1.11.1.min.js"></script>
        <script src="scripts/jquery-ui.min.js"></script>
        <script src="scripts/formValidation.js"></script>
        <script src="scripts/PopUpMenu.js"></script>

        <script>

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
                    new ImgMenu($('.TabletMenu'), $("#CategorySelect"));
                    $("#valueUnit").change(function() {
                        $("#valueLabel").text(testUnits($("#valueUnit").val()));
                    });
                    $("#valueLabel").text(testUnits($("#valueUnit").val()));
                    $("#img_file").on('change', function() {
                        alert($("#a").val());
                    })
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
                });


        </script>
        
    </body>
</html>
