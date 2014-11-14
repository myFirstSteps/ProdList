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
        <style><%@include file="CSSdoc/mainCSS.css"%></style>
        <link rel="stylesheet" href="<c:url value='CSSdoc/jquery-ui.min.css'/>">
    </head>
    <body>
        <c:set var="categories" value="${product:getCategories(pageContext.servletContext)}"/>
        <c:import url="WEB-INF/template/headtemplate.jsp"/>
        <div id='newList' class='center_form prodholder'>
            <div id='newListHead'>
                <div>
                    Имя списка:<span id='listName'>Имя списка</span><button onclick='editListName(this)' title='Редактировать'><img height='16' width='16' alt='edit' src='resources/common_image/icons/Modify.gif'></button>
                </div>
                <h2>Список покупок.</h2>
            </div>

            <center><h3>Список пуст. Нажмите на кнопку добавить и воспользуйтесь формой для выбора продуктов.</h3></center>
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

        <div id="prodSelect" class="prodholder center_form">
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
                  <option></option> 
                </select>
            </div >
            <div class="AddEllement">
                <span>Уточняющее название:</span><br>
                <select name="subNname" size="1"> 
                     <option>11</option> 
                     <option>12</option> 
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
            <div>
            </div>

            <script src="scripts/jquery-ui.js"></script>
            <script src="scripts/formValidation.js"></script>
            <script>

                        function editListName(o) {
                            $(o).replaceWith("<div class='editValues' style='float:none'><input  type='text' class='mandatory' value='" + $(o).prev("span").text().trim() + "'><br><button class='accept' \n\
                        onclick='acceptEdit(this)'><img height='16' width='16'\n\src='resources/common_image/icons/Yes.gif'></button>\n\
         <button onclick='denyEdit(this)'><img height='16' width='16' src='resources/common_image/icons/No.gif'></button></div>");
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
                            $(o).parent().replaceWith("<button onclick='editListName(this)' title='Редактировать'><img height='16' width='16' alt='edit' src='resources/common_image/icons/Modify.gif'></button>");
                        }
                        
                        $(function() {
                           $("#list").sortable();
                           $("#list").disableSelection();
                           $('.AddEllement.main').siblings('.AddEllement').css('visibility','hidden');
                           $(".AddEllement select").on("change",function(){ 
                               var searchable=$(this).parents('div.AddEllement').next(".AddEllement").children("select");
                              $(this).parents('div.AddEllement').nextAll('div.AddEllement').find('option').remove(); 
                              $(searchable).html('<option></option>');
                            //   $(this).parents('div.AddEllement').nextAll(".AddEllement").children('select').children().remove();
                               if (searchable.length>0){
                            var term = JSON.stringify($(searchable).serializeArray().concat($(searchable).parents(".AddEllement").prevAll(".AddEllement").children("select").serializeArray())); //$(".ter:input").serializeArray();              //[{category:"фрукты"},{name:"бананы"}];
                                         alert("data>0");
                            $.getJSON("ProductAutocomplete", {term:term}, function(data, status, xhr) {
                               $(searchable).children('option').replaceWith(function(){var values; $.each(data,function(i,e){values+='<option>'+e+'</option>';}); return values;});
                                $(searchable).parents(".AddEllement").css('visibility','visible');
                            });}
                            else{   alert("data<0");
                                
                            }
                           });
                        });
            </script>
    </body>


</html>
