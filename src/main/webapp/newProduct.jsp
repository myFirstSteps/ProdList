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


        <form  class="center_form" id="newProduct" method="post" action="productAdd">
            <div>
                <span>Категория:</span><br>
                <input   size="15" type="text" name="category"  id="CategorySelect" >
            </div>
            <div>
                <span>Название:</span><br>
                <input  type="text" name="name"  class='mandatory' >
            </div>
            <div>
                <span>Уточняющее название:</span><br>
                <input  type="text" name="sub_name" class='mandatory' >
            </div>
            <div>
                <span>Производитель:</span><br>
                <input  type="text" name="producer"  id="Name" >
            </div>
            <div>
                <span id='valueLabel'>Объем:</span><br>
                <input  type="text" size="5" maxlength="8" class="mandatory" name="value" >
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
                <input  type="text" size="5" maxlength="8" class='mandatory'  name="price" >
            </div>
            <div>
                <span>Комментарий:</span><br>
                <input  type="text" name="comment">
            </div><br>
            <div id='img_file'>
                <span>Прикрепить изображение:</span><br>
                <input type="file"  name='image'>
            </div><br>
            <input type="button" onclick="validate(this.form)" value="Добавить">


        </form>
        <script src="scripts/jquery-1.11.1.min.js"></script>
        <script src="scripts/jquery-ui.min.js"></script>
        <script src="scripts/formValidation.js"></script>
        <script src="scripts/PopUpMenu.js"></script>
        <script> 
                var testUnits=function(s){
                    switch(s){
                        case "кг": return "Вес:";
                        case "л": return "Объем:";
                        default : return "В единице:";
                    }
                };
                $(document).ready(function() {
                                new ImgMenu($('.TabletMenu'), $("#CategorySelect"));
                $("#valueUnit").change(function(){$("#valueLabel").text( testUnits($("#valueUnit").val())); 
                                    });
             $("#valueLabel").text( testUnits($("#valueUnit").val()));
             $("input.mandatory").bind('blur', function() {
                        emptyCheck(this, "<span class='mandatory error'>Поле не может быть пустым</span><br class='mandatory error'>");
                    });
                    $("input.mandatory").bind('keyup', function() {
                        emptyCheck(this, "<span class='mandatory error'>Поле не может быть пустым</span><br class='mandatory error'>");
                    });
            });
            
        </script>
    </body>
</html>
