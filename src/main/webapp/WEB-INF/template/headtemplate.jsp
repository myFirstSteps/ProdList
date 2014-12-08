<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<c:set var="path" value="${pageContext.request.getServletPath()}"/>
<c:set scope="request" var="icons" value='${pageContext.servletContext.getInitParameter("icons")}'/>
<c:set var="ico1img" value='${icons}Home.png'/>
<c:set var="ico2img" value='${icons}Sandbox.png'/>
<c:set var="ico3img" value='${icons}Editor.png'/>
<c:set var="ico4img" value='${icons}New_list.png'/>
<c:set var="ico5img" value='${icons}View_list.png'/>
<c:choose>
    <c:when test="${path eq '/index.jsp'}"> <c:set var="ico1img" value='${icons}Home_active.png'/> </c:when>
    <c:when test="${path eq '/editProduct.jsp'}"><c:set var="ico3img" value='${icons}Editor_active.png'/></c:when>
    <c:when test="${path eq '/newList.jsp'}"><c:set var="ico4img" value='${icons}New_list_active.png'/></c:when>
    <c:when test="${path eq '/viewList.jsp'}"><c:set var="ico5img" value='${icons}View_list_active.png'/></c:when>    
</c:choose>
<c:if test="${path eq '/editProduct.jsp' or path eq '/newList.jsp' or path eq '/viewList.jsp'}">
    <c:set var="ico2img" value='${icons}Sandbox_active.png'/>
</c:if>
<div id="HeadTemplate">
    <div><span>Интерактивное резюме на<br>позицию Java Junior Developer.<br><em>Панкратов Михаил</em></span></div>
    <div><img  src="<c:url value="/resources/common_image/OCP_JavaSE7Programmer.gif"/>" title="Да. У меня есть этот сертификат — отличный способ систематизировать базовые знания по Java SE."  
               width="140" height="71" alt="OCP_JavaSE7Programmer.gif"></div>
    <div  class="Navigation"><a title="Главная" href="<c:url value="index.jsp"/>" ><img class="UnActiveNavIco" alt="Главная" height="80" width="80" src="${ico1img}"> 
            <img class="ActiveNavIco" height="80" width="80" src="${icons}Home_active.png">
        </a>
    </div>
    <div  class="Navigation" >
        <a title="Учебный проект" href="<c:url value="editProduct.jsp"/>" ><img class="UnActiveNavIco" alt="Учебный проект" height="100" width="100" src='${ico2img}' >
            <img class="ActiveNavIco" height="100" width="100" src="${icons}Sandbox_active.png">
        </a>
    </div>
    <c:if test="${path eq '/editProduct.jsp' or path eq '/newList.jsp' or path eq '/viewList.jsp'}">
        <div  class="Navigation" >
            <a title="Редактор продуктов" href="<c:url value="editProduct.jsp"/>" ><img class="UnActiveNavIco" alt="Учебный проект" height="100" width="100" src='${ico3img}' >
                <img class="ActiveNavIco" height="100" width="100" src="${icons}Editor_active.png">
            </a>
        </div>
        <div  class="Navigation" >
            <a title="Создать список покупок" href="<c:url value="newList.jsp"/>" ><img class="UnActiveNavIco" alt="Создать список" height="100" width="100" src='${ico4img}' >
                <img class="ActiveNavIco" height="100" width="100" src="${icons}New_list_active.png">
            </a>
        </div>
        <div  class="Navigation" >
            <a title="Просмотреть списки покупок" href="<c:url value="viewList.jsp"/>" ><img class="UnActiveNavIco" alt="Просмотреть списки" height="100" width="100" src='${ico5img}' >
                <img class="ActiveNavIco" height="100" width="100" src="${icons}View_list_active.png">
            </a>
        </div>
    </c:if>
    <div id="UserForm"> 
        <c:choose>
            <c:when test="${pageContext.request.getRemoteUser()==null}">

                <c:if test="${path ne'/loginPage.jsp' and path ne '/loginError.jsp'  and path ne '/registration.jsp' }">

                    Вход не выполнен.<br>
                    <a  href='<c:url value="loginPage.jsp"/>'>вход</a><br>
                    <a href='<c:url value="registration.jsp"/>'>регистрация</a>
                    <c:if test="${path ne '/errorPage.jsp'}">
                        <c:set var="lastPage" scope="session" value="${path}"/>
                    </c:if>
                </c:if> 

            </c:when>
            <c:otherwise> 
                Пользователь: <em> ${client}</em><br> <a href='<c:url value="LogOut.do?path=${path}"/>'>выход</a>      
            </c:otherwise>
        </c:choose>
    </div> 
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
    function LoadingSplash() {
        this.html = "<div id='processing' style='position: absolute; z-index:2; background-color: white; text-align: center; display:none; '>\n\
<img style=' position: fixed;' src='${icons}ajax-loader.gif'></div>";
        this.calcSizePos = function() {
            $('div#processing').width(window.innerWidth).height(document.body.scrollHeight); //- $("#HeadTemplate").height());
            var img = $('div#processing').children("img");
            var y = (window.innerHeight - $(img).height()) / 2;
            $(img).css('top', y);
        };
        this.show = function() {
            splash.calcSizePos();
            $(this.splash).fadeTo(100, 0.8);
        };
        this.hide = function() {
            $(this.splash).hide();
        };
        if ($('div#processing').length === 0)
            $("body").append(this.html);
        this.splash = $('div#processing');
        $(window).resize(this.calcSizePos);
    }
    ;

    function Banner(id, html) {
        this.left = -440;
        this.id = id !== undefined && (typeof (id) === 'string') ? id : 'banner';
        this.html = "<div id='" + this.id + "' style='position: fixed; clear:none; top:150px; left: " + this.left + "px;  text-align: center; border: solid 2px black;' class='panel'>\n\
<img  src='resources/common_image/Before_leaving.png'></div>";
        this.calcSizePos = function() {
        };

        this.show = function() {
            $('div#' + this.id).animate({left: 0}, 300);
        };
        this.hide = function() {
            $('div#' + this.id).stop(true, true);
            $('div#' + this.id).animate({left: -$(this).width() + 25}, 500);
        };
        if ($('div#' + this.id).length === 0)
            $("body").append(this.html);
        this.banner = $('div#' + this.id);
        $(this.banner).mouseover(this.show);
        $(this.banner).mouseleave(this.hide);
        $(this.banner).click(function() {
            location.href = '<c:url value="Valuation.do"/>';
        });

    }
    ;
    var banner = new Banner("valuation");
    var splash = new LoadingSplash();

</script>




