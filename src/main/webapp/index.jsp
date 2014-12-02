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
        <title>Главная страница.</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href='CSSdoc/mainCSS.css' rel='stylesheet' type="text/css">
    </head>
    <body>
        <c:import url="/WEB-INF/template/headtemplate.jsp" charEncoding="UTF-8"/> 
        <div id="about">
            <div id='aboutme' class="panel">
                <h1>Обо мне</h1>
                <img height="200" src='resources/common_image/me.png'>
                <p> Меня зовут <em>Михаил Панкратов</em>. Я родился в Москве в 1984 году. Закончив среднюю школу,
                    я поступил в <em>МГТУ «МАМИ»</em> на специальноcть <em>«Автоматизация технологических процессов
                        и производств»</em>. После окончания университета в 2006 году я устроился работать по
                    полученной специальности на должность инженера в подразделение автоматизации <em>ГНЦ РФ ВНИИМЕТМАШ</em>.
                    С тех пор я занимаюсь автоматизацией поставляемых  ВНИИМЕТМАШ прокатных станов, металлургических машин и агрегатов в качестве
                    программиста АСУ.
                    На данный момент я работаю в должности ведущего научного сотрудника и в мои обязанности входит полный цикл
                    разработки управляющей программы объекта автоматизации, включающий:</p>
                <ul>
                    <li>согласование т.з.;</li>
                    <li>выбор  программируемого логического контроллера  (ПЛК) и написание управляющей программы  на языках стандарта IEC 61131-3;</li>
                    <li>выбор средства визуализации технологического процесса (HMI панели или ПЭВМ), создание проекта визуализации, как при помощи
                        стандартных SCADA систем, так и при помощи написания собственных компонент для IDE Delphi  и построения на их основе визуализации;</li>
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
                </p> 
                <p>
                    Сейчас я владею <em>Java SE7</em> на уровне сертификации <em>Oracle Certified Professional Java SE 7 Programmer</em>.
                </p>
                <p><em>Java EE, SQL, HTML/CSS, JavaScript</em> я владею на уровне
                    необходимом для создания этого ресурса. 
                </p>
                <p>Теперь о главном.  Я ищу работу Java разработчика. Беря во внимание, что у меня нет коммерческого опыта разработки на Java,
                    я рассчитываю на должность <em>junior</em>.
                    На потенциальном месте работы мне бы хотелось иметь возможность писать Java  код. Причем, не важно на каком уровне. Я
                    рад буду начать с решения модульных задач уровня SE,
                    постепенно продвигаясь к EE проектам. Главным для меня является наличие возможности написания собственного кода,
                    а не только рефакторинга или поддержания чужого.
                    Я не ищу работу связанную с АСУТП, программированием не на Java (Scala, JavaScript или что-то ещё), верстальщика,
                    системного администратора и т.д. 
                    Надеюсь, Вас заинтересует моя кандидатура. Связаться со мной можно по электронной почте <a href="mailto:resumeapp@mail.ru">
                        resumeapp@mail.ru</a>, другие контакты можно найти в моем резюме на <a href="http://hh.ru" target="_blank">hh.ru</a>. </p>
            </div>
            <div id='aboutproj' class="panel">
                <h1>O ресурсе</h1>
                <p>Основной предпосылкой к созданию этого ресурса послужило моё желание попрактиковаться в <em>Java SE</em>  программировании,
                получить начальный опыт  <em>Java EE</em>, а также познакомится и приобрести опыт работы с такими вещами как: <em>SQL, HTML/CSS,
                JavaScript/jQuery</em>. Помимо описательной части, ресурс содержит простенький сервис, позволяющий создавать списки покупок
                и добавлять описания продуктов в базу данных. Надеюсь, что этот ресурс поможет мне в поисках работы и сэкономит моё и Ваше время,
                сформировав у Вас более полное представление обо мне и моих навыках, чем это возможно в формате обычного резюме. Этот ресурс является
                неформальным дополнением к резюме, поэтому оформлен в свободном стиле.</p>  
                <h2>Используемые средства и технологии</h2>
                <img id='springimg' height="120"  src='resources/common_image/Spring.png'>
                <p>
                В роли веб-сервера и контейнера сервлетов выступает <em>Apache Tomcat</em>. В качестве базы данных, ожидаемо, выбрана <em>MySQL</em>.
                В проекте я не использую
                Spring и Hibernate, никаких фреймворков и CMS. Как уже сказано, главной целью была практика в языке, поэтому и было принято решение
                работать с более низкоуровневыми вещами и стандартной библиотекой. Исключение составляет <em>JavaScript</em>, с которым я до начала этого проекта не работал.
                Обойтись без JavaScript было бы крайне сложно, поэтому для <em>AJAX/JSON</em> запросов и работы с <em>DOM</em> используется <em>jQuery</em>.
                Немного используется
                <em>jQueryUI</em> для автодополнения в форме редактора продуктов, перемещения позиций списка и т.д. Вообще, хотя JavaScript показался во многом
                интуитивным, на его освоение требуется достаточно много времени и это не является приоритетной задачей. Поэтому JavaScript код получился
                избыточным, пестрит антипаттернами и присутствует исключительно ради обеспечения функционала.</p><p>  Помимо <em>Servlets & JSP</em> используется
                <em>JSTL</em>,
                организована отправка почты при помощи <em>Apache Commons Email</em> и загрузка на сервер файлов <em>Apache ServletFileUpload</em>.
                Для доступа к БД
                используется <em>JDBC</em>, написан класс, реализующий настраиваемый пул соединений к БД, и классы, позволяющие работать с записями продуктов,
                учетными записями пользователей и списками. Разработка велась в <em>NetBeans</em>, который показался весьма удобным и «из коробки» поддерживает
                <em>Maven,</em> Tomcat (и много чего ещё), собирает и деплоит проекты без каких либо дополнительных усилий. В качестве <em>VCS</em> использовался
                <em>GIT</em>,
                но его задача сводилась к обеспечению синхронизации работы с разных устройств.</p>
                <p>
                Сайт построен в расчете на современный браузер с включенным JavaScript и, опционально, с включенными cookie
                (для возобновления сессии незарегистрированных пользователей).  Поддержка старых браузеров, мобильных устройств и Internet Explorer
                весьма трудоемкий процесс и не входит в ближайшие планы. Функционал протестирован на  <em>?Чём?</em>, на другом может не работать.</p> 

                <h2>Изобретая квадратные велосипеды...</h2>
                <img height="200"  src='resources/common_image/Bycicle.jpg'>
                <p>Название этого параграфа родилось из двух антипаттернов «Изобретение велосипеда»
                и «Изобретение квадратного колеса». И прежде чем дать ссылку на исходный код я хочу пояснить почему вместо стандартных
                решений я использую свои. Конечно, с  организацией пула соединений к БД прекрасно справится Tomcat, а для обеспечения записи
                и чтения объектов гораздо лучше подойдет Hibernate. Но собственноручная реализация позволила поработать с многопоточностью,
                JDBC, SQL и вволю походить по граблям. Это дало начальный опыт и было интересно. Кроме того, о Hibernate и Spring я знаю пока
                только тезисно. Cо временем я  реализую ветку на Spring. Кое-где присутствует избыточная сложность. Например, создание фабрики
                DAO объектов, которая реализует только один тип DAO. Это связано с моим желанием на практике понять, как строить расширяемую
                архитектуру и лучше усвоить паттерны проектирования. Разработка велась без какого бы то ни было т.з., и в процессе рождался
                новый функционал, приходили новые знания, подрывающие заложенные изначально решения, поэтому хороший, связный код не мог
                получится даже случайно.  Кроме того, обстоятельства складываются так, что меня сильно поджимает время и я вынужден выкладывать
                код, который меня не во всем устраивает.  Самой приоритетной моей задачей станет рефакторинг Java кода и тщательное его
                комментирование. Ну, а пока, то что <a target="_blank" href="https://github.com/myFirstSteps/ProdList">есть</a>. Около полугода назад я выполнял тестовое задание,
                тогда мои знания были значительно ниже, но, думаю, это тоже может быть полезно, поэтому привожу
                <a target="_blank" href="https://github.com/myFirstSteps/ProdList">ссылку</a> (в readme.md пояснения). На этом всё.</p>
                <div>
                <a href="<c:url value="editProduct.jsp"/>">Учебный проект</a>
                &nbsp&nbsp&nbsp
                <a href="<c:url value="valuation.jsp"/>">Оставить отзыв</a>.
                </div>
            </div>
        </div>
    </body>
</html>
