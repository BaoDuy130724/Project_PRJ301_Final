<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login Page</title>
    </head>
    <body>
        <div>
            <h1>Login Form</h1>

            <c:choose>
                <c:when test="${sessionScope.user != null}">
                    <c:redirect url="welcom.jsp" />
                </c:when>
                <c:otherwise>
                    <form action="MainController" method="post">
                        <input type="hidden" name="action" value="login"/>

                        <label for="name">User Name</label>
                        <input type="text" id="name" name="name" placeholder="Enter user name...">

                        <label for="password">Password</label>
                        <input type="password" id="password" name="password" placeholder="Enter password...">

                        <input type="submit" value="Login"/>
                    </form>
                    <c:if test="${not empty message}">
                        <span style="color: red">${message}</span>
                    </c:if>
                </c:otherwise>
            </c:choose>
        </div>
    </body>
</html>
