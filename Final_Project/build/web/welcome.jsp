<%-- 
    Document   : welcome
    Created on : Jun 11, 2025, 10:57:46 AM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.UserDTO"%>
<%@page import="utils.GeneralMethod"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Welcome Page</title>
    </head>
    <body>
        <%
            UserDTO user = (UserDTO) GeneralMethod.getCurrentUser(request);
            if(user!=null){
        %>
        <h1>Welcome <%= user.getFullName()%></h1>
        <a href="MainController?action=logout">Logout</a>
        
        <%} else {%>
        <p>You do not have permission to access. Please login to access!!! <a href="MainController">Login</a></p>
        <%}%>
    </body>
</html>
