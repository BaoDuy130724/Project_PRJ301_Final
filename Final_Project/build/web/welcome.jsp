<%-- 
    Document   : welcome
    Created on : Jun 11, 2025, 10:57:46 AM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.UserDTO"%>
<%@page import="model.BookDTO"%>
<%@page import="model.CategoryDAO"%>
<%@page import="utils.GeneralMethod"%>
<%@page import="java.util.List" %>
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
            String searchTitle = (String) request.getAttribute("searchTitle");
        %>
        <h1>Welcome <%= user.getFullName()%></h1>
        <a href="MainController?action=logout">Logout</a>
        <form action="MainController" method="get">
            <input type="hidden" name="action" value="search">
            <input type="text" name="txtSearch" placeholder="Enter title book....." value="<%= searchTitle!=null?searchTitle:""%>"/>
            <input type="submit" value="Search"/>
        </form>
        <%
            List<BookDTO> listBooks = (List<BookDTO>) request.getAttribute("listBooks");
            if(listBooks!=null && listBooks.isEmpty()) {
                String message = (String) request.getAttribute("message");
        %>
        <p><%= message != null ? message : "" %></p>
        <%} else if (listBooks!=null && !listBooks.isEmpty()) {%>
            <table>
                <thead>
                <th>Book ID</th>
                <th>Title</th>
                <th>Author</th>
                <th>Publisher</th>
                <th>Year Published</th>
                <th>ISBN</th>
                <th>Category ID</th>
                <th>Quantity</th>
                <th>Available</th>
                    <% if(GeneralMethod.isAdmin(request)){ %>
                <th>Action</th>
                    <%}%>
            </thead>
            <tbody>
                <%for(BookDTO book : listBooks){%>
                <tr>
                    <td><%=book.getBookId()%></td>
                    <td><%=book.getTitle()%></td>
                    <td><%=book.getAuthor()%></td>
                    <td><%=book.getPublisher()%></td>
                    <td><%=book.getYear()%></td>
                    <td><%=book.getISBN()%></td>
                    <td><%=book.getCategoryId()%></td>
                    <td><%=book.getQuantity()%></td>
                    <td><%=book.getAvailable()%></td>
                    <% if(GeneralMethod.isAdmin(request)){ %>
                    <td>
                        <a href="#">EDIT</a>
                        <a href="#">DELETE</a>
                    </td>
                    <%}%>
                </tr>
                <%}%>
            </tbody>
        </table>
        <%}%>
    <%} else {%>
    <p>You do not have permission to access. Please login to access!!! <a href="MainContdoller">Login</a></p>
    <%}%>
</body>
</html>
