<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<!DOCTYPE html>
<html>
    
    <head>
        <title>Авторизация</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
    </head>
    <body>
       
        <c:import url="headtemplate.jsp" charEncoding="UTF-8" />
        
        <div>
            <form method="POST" action="j_security_check">
                <input type="text" name="j_username">
                <div>
                <input type="password" name="j_password">
                </div>
                <input type="submit" value="ok">
                <div> <input type="file" name="file"></div>
            </form> 
        </div>
<img src="OCP_JavaSE7Programmer.gif" alt="ytnQ">        
    </body>
</html>