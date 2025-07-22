<%-- 
    Document   : viewReservationDetail
    Created on : Jul 16, 2025, 11:24:11 PM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Reservation Details</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" />
    </head>
    <body>
<div class="container mt-5">
    <h2 class="mb-4">Reserved Books</h2>

    <c:if test="${not empty reservation && not empty reservation.details}">
        <div class="table-responsive">
            <table class="table table-sm table-striped align-middle">
                <thead class="table-dark">
                <tr>
                    <th>Book Title</th>
                    <th>Quantity</th>
                    <th>Status</th>
                    <th>Description</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="d" items="${reservation.details}">
                    <tr>
                        <td>${d.bookTitle}</td>
                        <td>${d.quantity}</td>
                        <td>${d.statusDisplayName}</td>
                        <td>${d.statusDescription}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>

        <c:if test="${sessionScope.user.role == 'member' && reservation.statusCode == 'Pending'}">
            <form method="post" action="ReservationController"
                  onsubmit="return confirm('Are you sure you want to cancel this reservation?');" class="mb-3">
                <input type="hidden" name="action" value="cancelReservation"/>
                <input type="hidden" name="reservationID" value="${reservation.reservationID}"/>
                <button type="submit" class="btn btn-danger">❌ Cancel Reservation</button>
            </form>
        </c:if>
    </c:if>

    <c:if test="${empty reservation || empty reservation.details}">
        <div class="alert alert-warning">No reserved books found.</div>
    </c:if>

    <c:if test="${not empty message}">
        <div class="alert alert-success">${message}</div>
    </c:if>

    <c:choose>
        <c:when test="${sessionScope.user.role == 'admin'}">
            <a href="MainController?action=adminViewAllReservations" class="btn btn-secondary">🏠 Back</a>
        </c:when>
        <c:otherwise>
            <a href="MainController?action=viewReservationHistory" class="btn btn-secondary">⬅ Back</a>
        </c:otherwise>
    </c:choose>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
