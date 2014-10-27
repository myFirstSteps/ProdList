<%-- 
    Document   : headtemplate
    Created on : 29.07.2014, 14:18:37
    Author     : pankratov
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<c:set var="username" scope="page" value="${pageContext.request.getRemoteUser()}"/>
<c:set var="path" value="${pageContext.request.getServletPath()}"/>
<div id="HeadTemplate">


    <div  class="HeadTemplate"><span>Интерактивное резюме на<br>позицию Java Junior Developer.<br><em>Панкратов Михаил</em></span></div>
    <div   class="HeadTemplate" ><img src="<c:url value="/resources/common_image/OCP_JavaSE7Programmer.gif"/>"  width="140" height="71" alt="OCP_JavaSE7Programmer.gif"></div>
    <div  class="HeadTemplate Navigation"><img class="ActiveNav" height='90' width='90' src='resources/common_image/icons/Home_active.png'><img class="UnActiveNav" height='90' width='90' src='resources/common_image/icons/Home.png'><br>Главная страница</div>
    <div  class="HeadTemplate Navigation" onmouseout="this.className-='Acive'" onmouseover="this.className+='Acive'"><img height='90' width='90' onmouseout="this.src = 'resources/common_image/icons/Sandbox.png'"   onmouseover="this.src = 'resources/common_image/icons/Sandbox_active.png'" src='resources/common_image/icons/Sandbox.png'><br>Приложение</div>
    <div id="right" class='HeadTemplate'>
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
</div>
    <script> 
        alert("inscript");
        $("div.Navigation").mouseout(function(){alert("leave");});
        $("div.Navigation.HeadTemplate").mouseover(function(){$(this).addClass("active");});
    </script>
    <script src='scripts/jquery-1.11.1.min.js'></script>    


