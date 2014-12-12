
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="product" uri="ProductsEL" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Просмотр списков покупок</title>
        <link href='CSSdoc/mainCSS.css' rel='stylesheet' type="text/css">
    </head>
    <body>
        <c:import url="WEB-INF/template/headtemplate.jsp"/>
        <div  class='panel'> 
            <h1>Просмотр списков</h1>
            <h4 id='error'  class='error'>${error}</h4>
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
        <script src="scripts/myJavaScript.js"></script>
        <script>

                    function showButton() {
                        if ($("#listName").val() !== 'Выберите список')
                            $('#show').show();
                        else
                            $('#show').hide();
                    }
                    function showList() {
                        $("#error").text('');
                        splash.show();
                        $.post("<c:url value='List.do'/>", {action: 'show', listName: $("#listName").val()}, function(response) {
                            splash.hide();
                            var alist = response.list;
                            if (response.error !== undefined) {
                                 $("#error").text(response.error);
                                return;
                            }
                            var listhtml = "<h2>Список: " + alist.name + " от " + alist.timeStamp + "</h2>";
                            
                            $.each(alist.products, function(i, e) {
                                listhtml += "<div id='"+e.key+"'>" + e.value + (e.key!=='0'?"<button onclick='showCard(this)'><img height='16' width='16' alt='Показать' src='${icons}View.gif'></button></div>":"");
                            });
                            $("#list").html(listhtml).show();
                        });
                    }
                    function showCard(o) {
                        var product =  SplitID($(o).parent("div").attr("id"));
                        splash.show();    
                        $.get('<c:url value="ReadProduct.do"/>', {action: "infoCard", product: JSON.stringify([product])}, function(data, status, xhr) {
                            $("#card div").html(data);
                            $("#card").show();
                            var left = (window.innerWidth - $("#card").outerWidth()) / 2;
                            var top = (window.innerHeight - $("#card").outerHeight()) / 2;
                            $("#card").css("left", left).css("top", top);
                            splash.hide();
                            imgScreen();
                        });
                    }
        </script>
    </body>
</html>
