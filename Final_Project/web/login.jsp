<%-- 
    Document   : login
    Created on : Jun 11, 2025, 8:50:49 AM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="utils.GeneralMethod"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login Page</title>
    </head>
    <body>
        <div>
            <h1>Login Form</h1>
            <%
                if(GeneralMethod.isLoggedIn(request)){
                    response.sendRedirect("welcom.jsp");
                }else{
                    String mess = (String) request.getAttribute("message");
            %>
            <form action="MainController" method="post">
                <input type="hidden" name="action" value="login"/>
                
                <label for="name">User Name</label>
                <input type="text" id="name" name="name" placeholder="Enter user name...">
                
                <label for="password">Password</label>
                <input type="password" id="password" name="password" placeholder="Enter password...">
                
                <input type="submit" value="Login"/>
            </form>
            <span style="color: red"> <%= mess!=null?mess:"" %> </span>
            <%}%>
        </div>
    </body>
</html>
