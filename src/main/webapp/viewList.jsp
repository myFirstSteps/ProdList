<%-- 
    Document   : viewList
    Created on : 20.11.2014, 17:03:24
    Author     : pankratov
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="product" uri="ProductsEL" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Просмотр списков покупок.</title>
        <link href='CSSdoc/mainCSS.css' rel='stylesheet' type="text/css">
    </head>
    <body>
        <c:import url="WEB-INF/template/headtemplate.jsp"/>
        <div  class='panel'> 
            <h1>Просмотр списков</h1>
            <div class="formitem">
                <span>Имя списка:</span><br>
                <select id="listName" onchange="showButton()" name="ListName">
                    <option>Выберите список</option>
                    <c:forEach  var="ListName" items="${product:getListNames(pageContext.servletContext,client)}">              
                        <option>
                            ${ListName}
                        </option>
                    </c:forEach>
                </select>
                <button id="show" title="Показать список" style="display: none;" onclick="showList()"><img height="16" width="16" src="${icons}View.gif" alt="Показать"></button>
            </div>
        </div>
        <div id="list" style="display:none;"  class='panel'>

        </div>
        <div id="card" class="panel" >
            <img id="closeCard" style="float: right;" onclick="$('#card').hide()" class="pointer" title="Закрыть" height="16" width="16" alt="закрыть" src="${icons}Close.gif">
            <div>
            </div>
        </div>
        <script src="scripts/formValidation.js"></script>
        <script>

                    function showButton() {
                        if ($("#listName").val() !== 'Выберите список')
                            $('#show').show();
                        else
                            $('#show').hide();
                    }
                    $(document).ready(function() {
                        showButton();
                        
                    });
                    function showList() {
                        $.post("<c:url value='List.do'/>", {action: 'show', listName: $("#listName").val()}, function(response) {
                            var alist = response.list;
                            if (response.error !== undefined) {
                                alert(response.error);
                                return;
                            }
                            var listhtml = "<h2>Список: " + alist.name + " от " + alist.timeStamp + "</h2>";

                            $.each(alist.products, function(i, e) {
                                listhtml += "<div>" + e + "<button onclick='showCard()'><img height='16' width='16' alt='Показать' src='${icons}View.gif'></button></div>";
                            });
                            $("#list").html(listhtml).show();
                        });
                    }
                    function showCard() {
                        alert("show card");
                        var product = {id: "5"};
                        $.get('<c:url value="ReadProduct.do"/>', {action: "infoCard", product: JSON.stringify([product])}, function(data, status, xhr) {
                            $("#card div").html(data);
                            $("#card").show();
                            var left = (window.innerWidth - $("#card").outerWidth()) / 2;
                            var top = (window.innerHeight - $("#card").outerHeight()) / 2;
                            $("#card").css("left", left).css("top", top);
                        });
                    }
        </script>
    </body>
</html>
