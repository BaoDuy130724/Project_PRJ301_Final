<%-- 
    Document   : reservationSelect
    Created on : Jul 16, 2025, 10:07:14 PM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Reserve Unavailable Books</title>
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet" />
    <style>
        img {
            width: 60px;
            height: auto;
        }

        input[type="number"] {
            width: 70px;
        }
    </style>
    </head>
    <body>
<div class="container mt-5">

    <!-- Tiêu đề và nút Back to Home -->
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h2 class="mb-0">Reserve Unavailable Books</h2>
        <a href="MainController?action=home" class="btn btn-outline-dark">🏠 Back to Home</a>
    </div>

    <!-- Nút View Cart + View History nằm dưới tiêu đề, bên trái -->
    <div class="mb-3">
        <a href="MainController?action=viewReservationCart" class="btn btn-outline-info me-2">🛒 View Cart</a>
        <a href="MainController?action=viewReservationHistory" class="btn btn-outline-secondary">📖 View History</a>
    </div>

    <!-- Table và phần còn lại giữ nguyên -->
    <c:if test="${not empty message}">
        <div class="alert alert-success">${message}</div>
    </c:if>

    <c:if test="${empty unavailableBooks}">
        <div class="alert alert-warning">No unavailable books found.</div>
    </c:if>

    <c:if test="${not empty unavailableBooks}">
        <div class="table-responsive">
            <table class="table table-bordered align-middle text-center">
                <thead class="table-dark">
                <tr>
                    <th>Image</th>
                    <th>Title</th>
                    <th>Author</th>
                    <th>Publisher</th>
                    <th>Year</th>
                    <th>Quantity</th>
                    <th>Action</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="book" items="${unavailableBooks}">
                    <tr>
                        <form action="MainController" method="post">
                            <td>
                                <c:if test="${not empty book.image}">
                                    <img src="${pageContext.request.contextPath}/assets/book_images/${book.image}" alt="${book.title}" />
                                </c:if>
                            </td>
                            <td>${book.title}</td>
                            <td>${book.author}</td>
                            <td>${book.publisher}</td>
                            <td>${book.yearPublished}</td>
                            <td>
                                <input type="number" class="form-control" name="quantity" value="1" min="1" required />
                                <input type="hidden" name="bookId" value="${book.bookId}" />
                                <input type="hidden" name="action" value="addToReservationCart" />
                            </td>
                            <td>
                                <button type="submit" class="btn btn-primary btn-sm">Add to Cart</button>
                            </td>
                        </form>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </c:if>
</div>

<!-- Bootstrap JS Bundle -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
