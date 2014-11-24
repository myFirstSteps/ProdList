<%-- 
    Document   : newList
    Created on : 06.11.2014, 16:44:53
    Author     : pankratov
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="product" uri="ProductsEL" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Создание списка покупок.</title>
        <link href='CSSdoc/mainCSS.css' rel='stylesheet' type="text/css">
        <link rel="stylesheet" href="CSSdoc/jquery-ui.min.css">
    </head>
    <body>
        <c:set var="categories" value="${product:getCategories(pageContext.servletContext)}"/>
        <c:import url="WEB-INF/template/headtemplate.jsp"/>
        <div id='newList' class='center_form prodholder'>
            <div id='newListHead'>
                <div>
                    Имя списка:<span id='listName'>Имя списка</span>
                    <button onclick='editListName(this)' title='Редактировать'>
                        <img height='16' width='16' alt='edit' src='${icons}Modify.gif'>
                    </button>
                </div>
                <h2>Список покупок.</h2>
            </div>

            <center class="emptyList"><h3>Список пуст. Нажмите на кнопку добавить и воспользуйтесь формой для выбора продуктов.</h3></center>
            <ol id="list">

            </ol>
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
        </div>

        <div id="prodSelect" hidden="hidden" class="prodholder center_form">
            <img height="16" width="16" style="float: right; cursor: pointer" title='Закрыть форму.' src="${icons}Close.gif" alt="Закрыть" onclick="$('#prodSelect').hide()" >
            <div class='AddEllement main'>
                <span>Категория:</span><br>
                <select name="group" size="1"> 
                    <option>Выберите категорию</option> 
                    <c:forEach var="group" items="${categories}">
                        <option>${group}</option> 
                    </c:forEach>
                </select>
            </div>
            <div class="AddEllement">
                <span>Название:</span><br>
                <select name="name" size="1"> 
                </select>
            </div >
            <div class="AddEllement">
                <span>Уточняющее название:</span><br>
                <select name="subName" size="1"> 
                </select>
            </div>

            <div class="AddEllement">
                <span>Производитель:</span><br>
                <select name="producer" size="1"> 
                    <option></option> 
                </select>
            </div>
            <div class="AddEllement">
                <span>Объем:</span><br>
                <select name="value" size="1"> 
                    <option></option> 
                </select>
            </div>
            <button class="AddEllementButton" hidden="hidden" title="Добавить выбранный продукт в список." onclick="addProduct(this)"><img height="16" width="16" src="${icons}Yes.gif" alt="Добавить к списку"></button>
        </div>

        <script src="scripts/jquery-ui.js"></script>
        <script src="scripts/formValidation.js"></script>
        <script>
                var listItem = "<li><input onclick='focus()' type='text' size='1'  value='1'>шт.<button onclick='deleteProduct(this)'><img alt='delete' height='16' width='16' src='${icons}Delete.gif'></button></li>";
                var product;
                var item;
                var id;
                function editListName(o) {
                    $(o).replaceWith("<div class='editValues' style='float:none'><input  type='text' class='mandatory' value='" + $(o).prev("span").text().trim() + "'><br><button class='accept' \n\
                        onclick='acceptEdit(this)'><img height='16' width='16'\n\src='${icons}Yes.gif'></button>\n\
         <button onclick='denyEdit(this)'><img height='16' width='16' src='${icons}No.gif'></button></div>");
                    $("input.mandatory").on('blur keyup', function() {
                        emptyCheck(this, "<span class='mandatory error'>Поле не может быть пустым</span><br class='mandatory error'>");
                        if ($('.editValues .error').length !== 0)
                            $(".accept").attr("disabled", "disabled");
                        else
                            $(".accept").removeAttr("disabled");
                    });

                }
                function acceptEdit(o) {
                    $(o).parent().prev("span").text($(o).siblings("input").val());
                    denyEdit(o);
                }

                function denyEdit(o) {
                    $(o).parent().replaceWith("<button onclick='editListName(this)' title='Редактировать'><img height='16' width='16' alt='edit' src='${icons}Modify.gif'></button>");
                }
                function addProduct(o) {
                    $(".emptyList").remove();
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
                        var searchable = $(this).parents('div.AddEllement').next(".AddEllement").children("select");
                        $(this).parents('div.AddEllement').nextAll('div.AddEllement').css('display', 'none').find('option').remove();
                        $(searchable).html('<option></option>');
                        if (searchable.length > 0) {
                            var term = JSON.stringify($(searchable).serializeArray().concat($(searchable).parents(".AddEllement").prevAll(".AddEllement").children("select").serializeArray())); //$(".ter:input").serializeArray();              //[{category:"фрукты"},{name:"бананы"}];
                            $.getJSON("ProductAutocomplete", {term: term}, function(data, status, xhr) {
                                $(searchable).children('option').replaceWith(function() {
                                    var values = '<option class="badOption">Выберите</option>';
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
                                $.getJSON("ReadProduct", {maxCount: 1, product: JSON.stringify([product])}, function(data, status, xhr) {
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
                    var items='' ;
                    $("#list li").each(function(i, e) {
                        var idParts = $(e).attr("id").split("_");
                        items+=String(idParts[0]);
                        items+=String(idParts.length > 2?"o":"");
                        alert($(e).children("input").val());
                        items+="_"+$(e).children("input").val()+" ";
                    });
                    var list=JSON.stringify({name:$("#listName").text(),items:items});
                    $.post("List",{action:"save",list:list},function(data,status,xhr){
                      if(data.error===undefined)alert("Список успешно сохранен.");else alert("Не удалось сохранить список.\n"+data.error);
                    });
                   
                }
                
        </script>
    </body>


</html>
