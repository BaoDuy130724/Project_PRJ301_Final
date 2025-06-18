<%-- 
    Document   : welcome
    Created on : Jun 11, 2025, 10:57:46 AM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Welcome Page</title>
    </head>
    <body>
        <c:choose>

            <c:when test="${not empty sessionScope.user}">
                <h1>Welcome ${sessionScope.user.fullName}</h1>
                <a href="MainController?action=logout">Logout</a>

                <form action="MainController" method="get">
                    <input type="hidden" name="action" value="search">
                    <input type="text" name="txtSearch" placeholder="Enter title book....."
                           value="${searchTitle != null ? searchTitle : ''}"/>
                    <input type="submit" value="Search"/>
                </form>
                <c:if test="${sessionScope.user.role eq 'admin'}"> 
                    <a href="MainController?isAdd=true&action=addBook">Add Book</a>
                </c:if>
                <c:if test="${listBooks != null and fn:length(listBooks) == 0}">
                    <p>${message}</p>
                </c:if>
                <c:if test="${not empty listBooks}">
                    <table border="1">
                        <thead>
                            <tr>
                                <th>Book ID</th>
                                <th>Title</th>
                                <th>Author</th>
                                <th>Publisher</th>
                                <th>Year Published</th>
                                <th>ISBN</th>
                                <th>Category ID</th>
                                <th>Quantity</th>
                                <th>Available</th>
                                    <c:if test="${sessionScope.user.role eq 'admin'}">
                                    <th>Action</th>
                                    </c:if>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="book" items="${listBooks}">
                                <tr>
                                    <td>${book.bookId}</td>
                                    <td>${book.title}</td>
                                    <td>${book.author}</td>
                                    <td>${book.publisher}</td>
                                    <td>${book.year}</td>
                                    <td>${book.ISBN}</td>
                                    <td>
                                        <c:forEach var="category" items="${listCategories}">
                                            <c:if test="${book.categoryId == category.categoryId}">
                                                ${category.name}
                                            </c:if>
                                        </c:forEach>
                                    </td>
                                    <td>${book.quantity}</td>
                                    <td>${book.available}</td>
                                    <c:if test="${sessionScope.user.role eq 'admin'}">
                                        <td>
                                            <a href="#">EDIT</a>
                                            <a href="#">DELETE</a>
                                        </td>
                                    </c:if>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:if>
            </c:when>
            <c:otherwise>
                <p>You do not have permission to access. Please login to access!!!
                    <a href="MainController">Login</a>
                </p>
            </c:otherwise>
        </c:choose>
    </body>
</html>

