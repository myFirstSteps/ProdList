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


        <form  class="center_form" method="post" action="productAdd">
            <input   type="text" name="category" id="CategorySelect" > 
                     </form>
        <script src="scripts/jquery-1.11.1.min.js"></script>
        <script src="scripts/jquery-ui.min.js"></script>
        <script src="scripts/PopUpMenu.js"></script>
        <script> $(document).ready(function() {
                  
                    new ImgMenu($('.TabletMenu'), $("#CategorySelect"));  });
              /*  $(document).ready(function() {
                    new ImgMenu($("#addProduct"),this);               //!!!!!!!!!!!!!!!!!!!!!!!!
                var maxDim = 0;
                $("div.CategoryTablet").each(function() {
                    maxDim = $(this).width() > maxDim ? $(this).width() : maxDim > $(this).height() ? maxDim : $(this).height();
                    $(this).mouseover(function() {
                        $(this).addClass("SelectedTablet");
                    });
                    $(this).mouseout(function() {
                        $(this).removeClass("SelectedTablet");
                       
                    });
                    $(this).click(function(){  $("#CategorySelect").attr("value",$(this).children("span").text()); $("div.TabletHolder").toggle("clip");});
                });
                $("div.CategoryTablet").width(maxDim);
                $("div.CategoryTablet").height(maxDim);
                $("div.TabletHolder").hide();
                $("#CategorySelect").click(function()
                {
                    var awidth = window.innerWidth - (15 * window.innerWidth / 100);
                    var aleft = (window.innerWidth - awidth) / 2;
                    var ctop = (window.innerHeight - $("div.TabletHolder").innerHeight()) - window.innerHeight * 10 / 100;
                    var atop = ctop>window.innerHeight/2?ctop:window.innerHeight/2;
                    $(this).parent().position().top + $(this).parent().innerHeight();

                    $("div.TabletHolder").css("width", awidth);
                    $("div.TabletHolder").css("left", aleft);
                    $("div.TabletHolder").css("top", atop);
                    $("div.TabletHolder").toggle("clip", 300);
                });

                $(".TabletHolder").css("visibility", "visible");
            });
            ;*/
        </script>
    </body>
</html>
