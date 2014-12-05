
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="product" uri="ProductsEL" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Создание списка покупок</title>
        <link href='CSSdoc/mainCSS.css' rel='stylesheet' type="text/css">
        <link rel="stylesheet" href="CSSdoc/jquery-ui.min.css">
    </head>
    <body>
        <c:set var="categories" value="${product:getCategories(pageContext.servletContext)}"/>
        <c:import url="WEB-INF/template/headtemplate.jsp"/>
        <div id='newList' class='panel'>

            <h1>Список покупок</h1>
            <h4 id='error'  class='error'>${error}</h4>
            <h4 id='success'  ></h4>
            <img class="infographic" style="height: 200px; float: left;" src="resources/common_image/Make_list_infogr.png">          
            <div class="formitem"><span>Имя списка:</span><br><input id='listName' class='mandatory' type="text" value="Список №1"></div>
            <div id='newListButtons'>
                <button title="Добавить в список новую позицию" onclick="$('#prodSelect').show()">
                    <img height="16" width="16" src="${icons}Add.gif" 
                         alt='Добавить'>
                </button>
                <button onclick="saveList()" title="Сохранить список">
                    <img height="16" width="16" src="${icons}Save.gif" 
                         alt='Сохранить'>
                </button>
                <button title="Удалить из списка все позиции" onclick="$('#list').children().remove()">
                    <img height="16" width="16" src="${icons}Delete.gif"
                         alt='Очистить'>
                </button>
            </div>
            <ol id="list">
            </ol>
        </div>

        <div id="prodSelect" hidden="hidden" class="panel">
            <img class="pointer" height="16" width="16" style="float: right;" title='Закрыть форму.' src="${icons}Close.gif" alt="Закрыть" onclick="$('#prodSelect').hide()" >
            <div class='AddEllement main formitem'>
                <span>Категория:</span><br>
                <select name="group" size="1"> 
                    <option>Выберите категорию</option> 
                    <c:forEach var="group" items="${categories}">
                        <option>${group}</option> 
                    </c:forEach>
                </select>
            </div>
            <div class="AddEllement formitem">
                <span>Название:</span><br>
                <select name="name" size="1"> 
                </select>
            </div >
            <div class="AddEllement formitem">
                <span>Уточняющее название:</span><br>
                <select name="subName" size="1"> 
                </select>
            </div>
            <div class="AddEllement formitem">
                <span>Производитель:</span><br>
                <select name="producer" size="1"> 
                    <option></option> 
                </select>
            </div>
            <div class="AddEllement formitem">
                <span>Объем:</span><br>
                <select name="value" size="1"> 
                    <option></option> 
                </select>
            </div>
            <button class="AddEllementButton" hidden="hidden" title="Добавить выбранный продукт в список." onclick="addProduct(this)"><img height="16" width="16" src="${icons}Yes.gif" alt="Добавить к списку"></button>
        </div>

        <script src="scripts/jquery-ui.js"></script>
        <script src="scripts/myJavaScript.js"></script>
        <script>
                var listItem = "<li><input onclick='focus()' type='text' size='1'  value='1'>шт.<button onclick='deleteProduct(this)'><img alt='delete' height='16' width='16' src='${icons}Delete.gif'></button></li>";
                var product;
                var item;
                var id;
                $(document).ready(function() {
                    $("input.mandatory").on('blur keyup', function() {
                        emptyCheck(this, "<span class='mandatory error'>Поле не может быть пустым</span><br class='mandatory error'>");
                    });
                });

                function addProduct(o) {
                    if ($("#list").children("li#" + id).length === 0) {
                        $("#list").append(listItem.replace('<li>', '<li id="' + id + '">' + item));
                    } else {
                        var count = $("#list li[id=" + id + "]").children('input');
                        $(count).val(1 + Number($(count).val())).focus();
                    }
                }
                function deleteProduct(o) {
                    $(o).parents("li").remove();
                }

                $(function() {
                    $("#list").sortable();
                    $("#list").disableSelection();
                    $('.AddEllement.main').siblings('.AddEllement').css('display', 'none');
                    $(".AddEllement select").on("change", function() {
                        clearInfo();
                        var searchable = $(this).parents('div.AddEllement').next(".AddEllement").children("select");
                        $(this).parents('div.AddEllement').nextAll('div.AddEllement').css('display', 'none').find('option').remove();
                        $(searchable).html('<option></option>');
                        if (searchable.length > 0) {
                            var term = JSON.stringify($(searchable).serializeArray().concat($(searchable).parents(".AddEllement").prevAll(".AddEllement").children("select").serializeArray()));
                            $.getJSON('<c:url value="ProductAutocomplete.do"/>', {term: term}, function(data, status, xhr) {
                                $(searchable).children('option').replaceWith(function() {
                                    var values = '<option class="badOption">---Выберите---</option>';
                                    $.each(data, function(i, e) {
                                        values += '<option>' + e + '</option>';
                                    });
                                    return values;
                                });

                                var product = {'group': $(".AddEllement select[name='group']").filter(":visible").val(),
                                    'name': $(".AddEllement select[name='name']").filter(":visible").val(),
                                    'subName': $(".AddEllement select[name='subName']").filter(":visible").val(),
                                    'producer': $(".AddEllement select[name='producer']").filter(":visible").val(),
                                    'value': $(".AddEllement select[name='value']").filter(":visible").val(),
                                };
                                $(searchable).parents(".AddEllement").show();
                                $.getJSON('<c:url value="ReadProduct.do"/>', {action: "products", product: JSON.stringify([product])}, function(data, status, xhr) {
                                    if (data.products !== undefined && data.products.length === 1) {
                                        product = data.products[0];
                                        item = product.name + " " + product.subName + " " + product.producer + " " + product.value + " " + product.valueUnits + " " + product.price + "руб.";
                                        item = item.replace(/любой/g, '');
                                        var isOrigin = (product.origin === true ? '_o' : '');
                                        id = product.id + '_' + product.originID + isOrigin;
                                        $(listItem).children("input").val();
                                        $(".AddEllementButton").show();
                                    } else {
                                        $(".AddEllementButton").hide();
                                    }
                                });
                            });
                        }
                    });
                });
                function saveList() {
                    clearInfo();
                    var items = '';
                    $("#list li").each(function(i, e) {
                        items +=$(e).attr("id");
                        items += "_" + $(e).children("input").val() + " ";
                    });
                    if(items.length===0){$("#error").text("Список пуст. Добавьте позиции в список."); return;};
                    
                    var list = JSON.stringify({name: $("#listName").val(), items: items});
                    splash.show();
                    $.post("List.do", {action: "save", list: list}, function(data, status, xhr) {
                        if (data.error === undefined)
                            $("#success").text("Список '" + data.listNmae + "' успешно сохранен.");
                        else
                            $("#error").text(data.error);
                        splash.hide();
                    });
                }
                function clearInfo() {
                    $("#success").text("");
                    $("#error").text("");

                }
        </script>
    </body>


</html>
