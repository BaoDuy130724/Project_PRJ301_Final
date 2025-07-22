<%-- 
    Document   : reservationHistory
    Created on : Jul 16, 2025, 11:48:37 PM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Reservation History</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" />
    </head>
    <body>
<div class="container mt-5">
    <h2 class="mb-4">
        <c:choose>
            <c:when test="${sessionScope.user.role == 'admin'}">All Book Reservations</c:when>
            <c:otherwise>Your Reservation History</c:otherwise>
        </c:choose>
    </h2>
    <c:if test="${sessionScope.user.role == 'admin'}">
        <form method="get" action="ReservationController" class="row g-3 mb-4">
            <input type="hidden" name="action" value="searchReservationsForAdmin"/>

            <div class="col-md-3">
                <label for="fromDate" class="form-label">From Date</label>
                <input type="date" id="fromDate" name="fromDate" class="form-control" value="${fromDate}" />
            </div>

            <div class="col-md-3">
                <label for="toDate" class="form-label">To Date</label>
                <input type="date" id="toDate" name="toDate" class="form-control" value="${toDate}" />
            </div>

            <div class="col-md-3">
                <label for="fullName" class="form-label">Full Name</label>
                <input type="text" id="fullName" name="fullName" class="form-control" value="${fullName}" placeholder="Search by name" />
            </div>

            <div class="col-md-2">
                <label for="status" class="form-label">Status</label>
                <select id="status" name="status" class="form-select">
                    <option value="" ${empty status ? 'selected' : ''}>All</option>
                    <option value="Pending" ${status == 'Pending' ? 'selected' : ''}>Pending</option>
                    <option value="ReadyToPickup" ${status == 'ReadyToPickup' ? 'selected' : ''}>Ready to Pickup</option>
                    <option value="Fulfilled" ${status == 'Fulfilled' ? 'selected' : ''}>Fulfilled</option>
                    <option value="Expired" ${status == 'Expired' ? 'selected' : ''}>Expired</option>
                </select>
            </div>

            <div class="col-md-1 d-flex align-items-end">
                <button type="submit" class="btn btn-primary w-100">Search</button>
            </div>
        </form>
    </c:if>
    <c:if test="${not empty message}">
        <div class="alert alert-info">${message}</div>
    </c:if>

    <c:if test="${empty reservationList}">
        <p>No reservations found.</p>
    </c:if>

    <c:if test="${not empty reservationList}">
        <div class="table-responsive">
            <table class="table table-bordered table-striped align-middle">
                <thead class="table-dark">
                <tr>
                    <th>Reservation ID</th>
                    <th>Reservation Date</th>
                    <th>Pickup Deadline</th>
                    <c:if test="${sessionScope.user.role == 'admin'}"><th>Name</th></c:if>
                    <th>Status</th>
                    <th>Details</th>
                    <th>Action</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="r" items="${reservationList}">
                    <tr>
                        <td>${r.reservationID}</td>
                        <td>${r.reserveDate}</td>
                        <td>${r.deadlineToPickup}</td>
                        <c:if test="${sessionScope.user.role == 'admin'}"><td>${r.fullName}</td></c:if>
                        <td>${r.statusDisplayName}</td>
                        <td>
                            <form method="get" action="ReservationController">
                                <input type="hidden" name="action" value="viewReservationDetail"/>
                                <input type="hidden" name="reservationID" value="${r.reservationID}"/>
                                <button type="submit" class="btn btn-sm btn-info">View</button>
                            </form>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${sessionScope.user.role == 'admin' && r.statusCode == 'ReadyToPickup'}">
                                    <form method="post" action="ReservationController">
                                        <input type="hidden" name="action" value="fulfillReservation"/>
                                        <input type="hidden" name="reservationID" value="${r.reservationID}"/>
                                        <button type="submit" class="btn btn-sm btn-success"
                                                onclick="return confirm('Confirm fulfill and create loan?');">Fulfill</button>
                                    </form>
                                </c:when>
                                <c:when test="${r.statusCode == 'Pending'}">
                                    <form method="post" action="ReservationController">
                                        <input type="hidden" name="action" value="cancelReservation"/>
                                        <input type="hidden" name="reservationID" value="${r.reservationID}"/>
                                        <button type="submit" class="btn btn-sm btn-danger"
                                                onclick="return confirm('Are you sure you want to cancel this reservation?');">Cancel</button>
                                    </form>
                                </c:when>
                                <c:otherwise>
                                    <span class="text-muted">Unavailable</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </c:if>

    <div class="mt-3">
        <c:choose>
            <c:when test="${sessionScope.user.role == 'admin'}">
                <a href="MainController?action=home" class="btn btn-secondary">🏠 Back to Admin Home</a>
            </c:when>
            <c:otherwise>
                <a href="MainController?action=viewReservations" class="btn btn-secondary">⬅ Back to Reservations</a>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
