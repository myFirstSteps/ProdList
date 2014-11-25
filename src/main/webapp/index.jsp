<%-- 
    Document   : index
    Created on : 29.07.2014, 15:57:04
    Author     : pankratov
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>

<!DOCTYPE html>
<html> 
    <head>
        <title>index</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href='CSSdoc/mainCSS.css' rel='stylesheet' type="text/css">
    </head>
    <body>
        <c:import url="/WEB-INF/template/headtemplate.jsp" charEncoding="UTF-8"/> 
        <div style="width: 90%; margin: 0 auto 0 auto;">
            <div id='aboutme' class="panel">
               <h2>Обо мне:</h2>
               <img style='float:left; border-radius: 10px; margin: 10px;' height="200" src='resources/common_image/me.png'>
               <p> Меня зовут <em>Михаил Панкратов</em>. Я родился в Москве в 1984 году. Закончив среднюю школу,
                   я поступил в <em>МГТУ «МАМИ»</em> на специальноcть <em>«Автоматизация технологических процессов
                   и производств»</em>. После окончания университета в 2006 году я устроился работать по
                   полученной специальности на должность инженера в подразделение автоматизации <em>ГНЦ ОАО АХК ВНИИМЕТМАШ</em>.
                   С тех пор я занимаюсь автоматизацией поставляемых ОАО АХК ВНИИМЕТМАШ прокатных станов, металлургических машин и агрегатов в качестве программиста АСУ.
                На данный момент я работаю в должности ведущего научного сотрудника и в мои обязанности входит полный цикл
                разработки управляющей программы объекта автоматизации, включающий:</p>
               <ul>
                <li>согласование т.з.;</li>
                <li>выбор  программируемого логического контроллера  (ПЛК) и написание управляющей программы  на языках стандарта IEC 61131-3;</li>
                <li>выбор средства визуализации технологического процесса (HMI панели или ПЭВМ), создание проекта визуализации, как при помощи стандартных SCADA систем, так и при помощи написания собственных компонент для IDE Delphi  и построения на их основе визуализации;</li>
                <li>участие в выборе и конфигурирование промышленной сети (Profibus-DP, Modbus и т.д.);</li>
                <li>разработка проектной документации (инструкция оператора);</li>
                <li>пусконаладка на территории заказчика;</li>
                <li>техподдержка и расширение функционала ПО после ввода в эксплуатацию (при необходимости).</li>
                </ul>
               <p>
                Хотя, безусловно, наиболее любимая часть моей работы  - программирование. И здесь, в основном, приходится иметь дело с низким уровнем
                абстракции, прямым доступом к памяти, работой со структурами данных уровня битовых полей  и реализациями языка ST(по сути своей процедурный язык)
                у тех производителей ПЛК, которые его поддерживают.  Однако имеется у меня и некоторый опыт работы с языками высокого уровня, который включает в
                себя разработку системы визуализации тех. процесса на Delphi 7. Именно с Delphi  началось мое знакомство с языками высокого уровня и <em>ООП</em>.
                Постепенно круг моих интересов сместился в сторону технологии <em>Java</em> и всего, что с ней связано.
                Сейчас я владею <em>Java SE7</em> на уровне сертификации <em>Oracle Certified Professional Java SE 7 Programmer</em>.  <em>Java EE, MySQL, HTML/CSS, JavaScript</em> я владею на уровне
                необходимом для создания этого ресурса. </p>
                <p>Теперь о главном.  Я ищу работу Java разработчика. Беря во внимание, что у меня нет коммерческого опыта разработки на Java, я рассчитываю на должность junior.
                На потенциальном месте работы мне бы хотелось иметь возможность писать Java  код. Причем, не важно на каком уровне. Я  рад буду начать с решения модульных задач уровня SE,
                постепенно продвигаясь к EE проектам. Главным для меня является наличие возможности написания собственного кода, а не только рефакторинга или поддержания чужого.
                Я не ищу работу связанную с АСУТП, программированием не на Java (Scala, JavaScript или что-то ещё), верстальщика, системного администратора и т.д. 
                Надеюсь, Вас заинтересует моя кандидатура. Связаться со мной можно по электронной почте <a>resumeapp@mail.ru</a>, другие контакты можно найти в моем резюме на <a href="http://hh.ru" target="_blank">hh.ru</a>. </p>
            </div>
            <div id='aboutproj' class="panel">

            </div>
        </div>
    </body>
</html>
