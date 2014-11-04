<%-- 
    Document   : headtemplate
    Created on : 29.07.2014, 14:18:37
    Author     : pankratov
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<c:set var="username" scope="session" value="${pageContext.request.getRemoteUser()}"/>
<c:set var="client" scope="session">
<c:choose>
    <c:when test="${username ne null}">${username}</c:when>
    <c:otherwise>${clid}</c:otherwise>    
</c:choose>
</c:set>
<c:set var="path" value="${pageContext.request.getServletPath()}"/>
<c:set scope="session" var="isAdmin" value="${pageContext.request.isUserInRole('admin')}"/>
<c:set var="ico1img" value='resources/common_image/icons/Home.png'/>
<c:set var="ico2img" value='resources/common_image/icons/Sandbox.png'/>
<c:set var="ico3img" value='resources/common_image/icons/Editor.png'/>
<c:set var="ico4img" value='resources/common_image/icons/New_list.png'/>
<c:set var="ico5img" value='resources/common_image/icons/View_list.png'/>
<c:choose>
    <c:when test="${path eq '/index.jsp'}"> <c:set var="ico1img" value='resources/common_image/icons/Home_active.png'/> </c:when>
    <c:when test="${path eq '/editProduct.jsp'}"><c:set var="ico3img" value='resources/common_image/icons/Editor_active.png'/></c:when>
    <c:when test="${path eq '/newList.jsp'}"><c:set var="ico4img" value='resources/common_image/icons/New_list_active.png'/></c:when>
    <c:when test="${path eq '/viewList.jsp'}"><c:set var="ico5img" value='resources/common_image/icons/View_list_active.png'/></c:when>    
</c:choose>
<c:if test="${path eq '/editProduct.jsp' or path eq '/newList.jsp' or path eq '/viewList.jsp'}">
    <c:set var="ico2img" value='resources/common_image/icons/Sandbox_active.png'/>
</c:if>
    <div id="HeadTemplate">
        <div><span>Интерактивное резюме на<br>позицию Java Junior Developer.<br><em>Панкратов Михаил</em></span></div>
        <div><img  src="<c:url value="/resources/common_image/OCP_JavaSE7Programmer.gif"/>"  width="140" height="71" alt="OCP_JavaSE7Programmer.gif"></div>
    <div  class="Navigation"><a title="Главная" href="<c:url value="index.jsp"/>" ><img class="UnActiveNavIco"  height="80" width="80" src="${ico1img}"> 
            <img class="ActiveNavIco" height="80" width="80" src="resources/common_image/icons/Home_active.png">
        </a>
    </div>
    <div  class="Navigation" >
        <a title="Учебный проект" href="<c:url value="editProduct.jsp"/>" ><img class="UnActiveNavIco" height="100" width="100" src='${ico2img}' >
            <img class="ActiveNavIco" height="100" width="100" src="resources/common_image/icons/Sandbox_active.png">
        </a>
    </div>
        <c:if test="${path eq '/editProduct.jsp' or path eq '/newList.jsp' or path eq '/viewList.jsp'}">
    <div  class="Navigation" >
        <a title="Редактор продуктов" href="<c:url value="editProduct.jsp"/>" ><img class="UnActiveNavIco" height="100" width="100" src='${ico3img}' >
            <img class="ActiveNavIco" height="100" width="100" src="resources/common_image/icons/Editor_active.png">
        </a>
    </div>
    <div  class="Navigation" >
        <a title="Создать список покупок" href="<c:url value="newList.jsp"/>" ><img class="UnActiveNavIco" height="100" width="100" src='${ico4img}' >
            <img class="ActiveNavIco" height="100" width="100" src="resources/common_image/icons/New_list_active.png">
        </a>
    </div>
      <div  class="Navigation" >
        <a title="Просмотреть списки покупок" href="<c:url value="viewList.jsp"/>" ><img class="UnActiveNavIco" height="100" width="100" src='${ico5img}' >
            <img class="ActiveNavIco" height="100" width="100" src="resources/common_image/icons/View_list_active.png">
        </a>
    </div>
        </c:if>
  
        <c:choose>
            <c:when test="${username==null}">

                <c:if test="${path ne'/loginPage.jsp' and path ne '/loginError.jsp'}">
                    <form id="UserForm" action="<c:url value='loginPage.jsp'/>"  method="get">  
                        Вход не выполнен.<br>
                        <input type="submit" value="Войти">                  
                    </form> 
                    <c:set var="lastpage" scope="session" value="${path}"/>
                </c:if> 

            </c:when>
            <c:otherwise> 
                <form id="UserForm" action="LogOut" method="get"> Пользователь: <span id="UserName"><em> ${username}</em></span><br> <button name="path" value="${path}">Выход</button></form>
                </c:otherwise>
            </c:choose>

    
</div>
<script src='scripts/jquery-1.11.1.min.js'></script> 
<script>
    $(".Navigation a").on("mouseover", function() {
        $(this).children(".UnActiveNavIco").hide();
        $(this).children(".ActiveNavIco").show();
    });
    $(".Navigation a").on("mouseout", function() {
        $(this).children(".ActiveNavIco").hide();
        $(this).children(".UnActiveNavIco").show();
    });
</script>




