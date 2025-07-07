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
                <a href="MainController?action=logout">Logout</a><br/>
                <a href="MainController?action=viewProfile">My Profile</a><br/>
                
                <form action="MainController" method="get">
                    <input type="hidden" name="action" value="search">
                    <input type="text" name="txtSearch" placeholder="Enter title book....."
                           value="${searchTitle != null ? searchTitle : ''}"/>
                    <input type="submit" value="Search"/>
                </form>
                    
                <c:if test="${sessionScope.user.role eq 'admin'}"> 
                    <a href="MainController?isAdd=true&action=addBook">Add Book</a><br/>
                    <a href="MainController?action=viewAllBorrows">View Borrow Orders</a><br/>
                </c:if>     
                <c:if test="${not empty message}">
                    ${message}
                </c:if>
                    
                <c:if test="${listBooks != null and fn:length(listBooks) == 0}">
                    <p>${message}</p>
                </c:if>
                <c:if test="${not empty listBooks}">
                    <table border="1">
                        <thead>
                            <tr>
                                <c:if test="${sessionScope.user.role eq 'admin'}">
                                    <th>Book ID</th>
                                    </c:if>

                                <th>Title</th>
                                <th>Author</th>
                                <th>Publisher</th>
                                <th>Year Published</th>

                                <c:if test="${sessionScope.user.role eq 'admin'}">
                                    <th>ISBN</th>
                                    <th>Category</th>
                                    <th>Quantity</th>
                                    <th>Is Deleted</th>
                                    </c:if>

                                <th>Available</th>
                                <th>Action</th> 
                            </tr>
                        </thead>

                        <tbody>
                            <c:forEach var="book" items="${listBooks}">
                                <tr>
                                    <c:if test="${sessionScope.user.role eq 'admin'}">
                                        <td>${book.bookId}</td>
                                    </c:if>

                                    <td>${book.title}</td>
                                    <td>${book.author}</td>
                                    <td>${book.publisher}</td>
                                    <td>${book.yearPublished}</td>

                                    <c:if test="${sessionScope.user.role eq 'admin'}">
                                        <td>${book.ISBN}</td>
                                        <td>
                                            <c:forEach var="category" items="${listCategories}">
                                                <c:if test="${book.categoryId == category.categoryId}">
                                                    ${category.name}
                                                </c:if>
                                            </c:forEach>
                                        </td>
                                        <td>${book.quantity}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${book.isdeleted}">
                                                    <span style="color:red; font-weight: bold;">Discontinued</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span style="color:green;">Sale</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                    </c:if>

                                    <td>
                                        <c:choose>
                                            <c:when test="${book.available == 0}">
                                                <span style="color: red; font-weight: bold;">Out of Stock</span>
                                            </c:when>
                                            <c:otherwise>
                                                ${book.available}
                                            </c:otherwise>
                                        </c:choose>
                                    </td>

                                    <c:if test="${sessionScope.user.role eq 'admin'}">
                                        <td>
                                            <a href="MainController?action=editBook&bookId=${book.bookId}">Edit</a>
                                            <a href="MainController?action=deleteBook&bookId=${book.bookId}"
                                               onclick="return confirm('Are you sure you want to delete this book?');">Delete</a>
                                        </td>
                                    </c:if>

                                    <c:if test="${sessionScope.user.role eq 'member'}"> 
                                        <td>
                                            <a href="MainController?action=viewDetailBook&bookId=${book.bookId}">#</a>
                                        </td>
                                    </c:if>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    <c:if test="${not empty accessDenied}">
                        ${accessDenied}
                    </c:if>
                    <c:if test="${not empty messDelete}">
                        ${messDelete}
                    </c:if>
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

