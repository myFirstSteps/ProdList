<%-- 
    Document   : newList
    Created on : 06.11.2014, 16:44:53
    Author     : pankratov
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Создание списка покупок.</title>
        <style><%@include file="CSSdoc/mainCSS.css"%></style>
        <link rel="stylesheet" href="<c:url value='CSSdoc/jquery-ui.min.css'/>">
    </head>
    <body>
        <c:import url="WEB-INF/template/headtemplate.jsp"/>
        <div id='newList' class='center_form prodholder'>
            <div id='newListHead'>
                <div>
                    Имя списка:<span id='listName'>Имя списка</span><button onclick='editListName(this)' title='Редактировать'><img height='16' width='16' alt='edit' src='resources/common_image/icons/Modify.gif'></button>
                </div>
                <h2>Список покупок.</h2>
            </div>

            <ol id="list">
                <li>a</li>
                <li>b</li>
                <li>v</li>
                <li>g</li>
            </ol>
            <div id='newListButtons'>
                <button title="Добавить в список новую позицию"><img height="16" width="16" src="resources/common_image/icons/Add.gif" alt='Добавить'></button>
                <button title="Сохранить список"><img height="16" width="16" src="resources/common_image/icons/Save.gif" alt='Сохранить'></button>
                <button title="Удалить из списка все позиции"><img height="16" width="16" src="resources/common_image/icons/Delete.gif" alt='Очистить'></button>
            </div>
        </div>

        <script src="scripts/jquery-ui.js"></script>
        <script>
                        $(function() {
                            $("#list").sortable();
                            $("#list").disableSelection();
                        });

                        function editListName(o) {
                            $(o).replaceWith("<div class='editValues' style='float:none'><input  type='text' value='" + $(o).prev("span").text().trim() + "'><br><button class='accept' \n\
                    onclick='acceptEdit(this)'><img height='16' width='16'\n\src='resources/common_image/icons/Yes.gif'></button>\n\
     <button onclick='denyEdit(this)'><img height='16' width='16' src='resources/common_image/icons/No.gif'></button></div>");
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
                            $(o).parent().prev("span").text($(o).siblings("input").val());
                             denyEdit(o);
                        }
                        
                        function denyEdit(o) {
                            $(o).parent().replaceWith("<button onclick='editListName(this)' title='Редактировать'><img height='16' width='16' alt='edit' src='resources/common_image/icons/Modify.gif'></button>");
                        }
        </script>
    </body>


</html>
