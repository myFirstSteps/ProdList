


<!DOCTYPE html>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<html> 
    <head>
        <title>Start Page</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="mainCSS.css" type="text/css" rel="stylesheet">
    </head>
    <body>
        <c:import url="headtemplate.jsp" charEncoding="UTF-8"/>
        <div>
            <form method="POST" action="j_security_check">
                <input type="text" name="j_username">
                <input type="password" name="j_password">  
                <input type="submit" value="Войти">
            </form> 
        </div>      
    </body>
</html>






