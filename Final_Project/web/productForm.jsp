<%-- 
    Document   : productForm
    Created on : Jun 18, 2025, 9:33:54 AM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Product Page</title>
    </head>
    <body>
        <c:choose>
            <c:when test="${sessionScope.user != null && sessionScope.user.role eq 'admin'}">
                <h1>${isAdd ? "ADD BOOK" : "EDIT BOOK"}</h1>
                <form action="MainController" method="post">
                    <input type="hidden" name="action" value="${isAdd?"submitCreateBook":"editBook"}"/>

                    <label for="title">Title*</label>
                    <input type="text" name="title" id="title" value="${book.title}" required=""/> <br/>

                    <label for="author">Author</label>
                    <input type="text" name="author" id="auhtor" value="${book.author}" required=""/><br/>

                    <label for="publisher">Publisher</label>
                    <input type="text" name="publisher" id="publisher" value="${book.publisher}" required=""/><br/>

                    <label for="year">Year Published</label>
                    <input type="text" name="year" id="year" value="${book.yearPublished}"/><br/>

                    <label for="ISBN">ISBN*</label>
                    <input type="text" name="ISBN" id="ISBN" value="${book.ISBN}" required=""/><br/>

                    <label for="category">Category</label>
                    <select name="categoryId" id="category">
                        <c:forEach var="category" items="${listCategories}">
                            <option value="${category.categoryId}">${category.name}</option>
                        </c:forEach>
                    </select><br/>

                    <label for="quantity">Quantity*</label>
                    <input type="text" name="quantity" id="quantity" value="${book.quantity}"/><br/>

                    <label for="available">Available</label>
                    <input type="text" name="available" id="available" value="${book.available}"/><br/>

                    <input type="submit" value="${isAdd?"Create":"Update"}">
                </form>
                <c:if test="${not empty message}">
                    ${message}
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
