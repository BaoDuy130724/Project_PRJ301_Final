<%-- 
    Document   : fineList
    Created on : Jul 18, 2025, 9:57:22 AM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title>Fine List</title>

    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet" />
    </head>
    <body>
        <div class="container mt-5">
            <!-- Header and Back Button -->
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h1 class="mb-0">Fine List</h1>
                <a href="MainController?action=home" class="btn btn-outline-secondary">
                    🏠 Back to Home
                </a>
            </div>

            <!-- Search Form -->
            <form action="MainController" method="get" class="row g-3 mb-4">
                <input type="hidden" name="action" value="searchFine" />

                <div class="col-md-3">
                    <label class="form-label">Reason</label>
                    <input type="text" name="reason" class="form-control" placeholder="Enter reason"
                           value="${param.reason != null ? param.reason : ''}" />
                </div>

                <div class="col-md-3">
                    <label class="form-label">Status</label>
                    <select name="status" class="form-select">
                        <option value="">-- All --</option>
                        <option value="Unpaid" ${param.status == 'Unpaid' ? 'selected' : ''}>Unpaid</option>
                        <option value="Paid" ${param.status == 'Paid' ? 'selected' : ''}>Paid</option>
                        <option value="Waived" ${param.status == 'Waived' ? 'selected' : ''}>Waived</option>
                    </select>
                </div>

                <div class="col-md-3 d-flex align-items-end">
                    <button type="submit" class="btn btn-primary w-100">Search</button>
                </div>
            </form>

            <c:if test="${not empty message}">
                <div class="alert alert-info">${message}</div>
            </c:if>

            <!-- Fine List Table -->
            <c:if test="${not empty fineList}">
                <div class="table-responsive">
                    <table class="table table-bordered table-striped align-middle">
                        <thead class="table-dark">
                            <tr>
                                <th>No</th>
                                <th>Borrow ID</th>
                                <th>Reason</th>
                                <th>Description</th>
                                <th>Amount</th>
                                <th>Status</th>
                                <th>Status Description</th>
                                <th>Created At</th>
                                <c:if test="${sessionScope.user.role eq 'admin'}">
                                    <th>Name</th>
                                    <th class="text-center">Action</th>
                                </c:if>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="fine" items="${fineList}" varStatus="status">
                                <tr>
                                    <td>${status.count}</td>
                                    <td>${fine.borrowID}</td>
                                    <td>${fine.reasonDisplayName}</td>
                                    <td>${fine.reasonDescription}</td>
                                    <td>${fine.amount}</td>
                                    <td>${fine.statusDisplayName}</td>
                                    <td>${fine.statusDescription}</td>
                                    <td>${fine.createdAt}</td>
                                    <c:if test="${sessionScope.user.role eq 'admin'}">
                                        <td>${fine.fullName}</td>
                                        <td class="text-center">
                                            <c:choose>
                                                <c:when test="${fine.statusCode eq 'Unpaid'}">
                                                    <div class="d-flex justify-content-center">
                                                        <!-- Mark as Paid -->
                                                        <form action="MainController" method="post" class="d-inline">
                                                            <input type="hidden" name="action" value="updateStatus" />
                                                            <input type="hidden" name="fineId" value="${fine.fineID}" />
                                                            <input type="hidden" name="status" value="Paid" />
                                                            <button type="submit" class="btn btn-success btn-sm me-1"
                                                                    onclick="return confirm('Confirm mark as Paid?')">
                                                                Paid
                                                            </button>
                                                        </form>
                                                        <!-- Waive -->
                                                        <form action="MainController" method="post" class="d-inline">
                                                            <input type="hidden" name="action" value="updateStatus" />
                                                            <input type="hidden" name="fineId" value="${fine.fineID}" />
                                                            <input type="hidden" name="status" value="Waived" />
                                                            <button type="submit" class="btn btn-warning btn-sm"
                                                                    onclick="return confirm('Confirm waive this fine?')">
                                                                Waive
                                                            </button>
                                                        </form>
                                                    </div>
                                                </c:when>
                                                <c:when test="${fine.statusCode eq 'Paid'}">
                                                    <span class="badge bg-success">Paid</span>
                                                </c:when>
                                                <c:when test="${fine.statusCode eq 'Waived'}">
                                                    <span class="badge bg-warning text-dark">Waived</span>
                                                </c:when>
                                            </c:choose>
                                        </td>
                                    </c:if>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:if>

            <c:if test="${empty fineList}">
                <p>No fines found.</p>
            </c:if>
        </div>

        <!-- Bootstrap JS Bundle -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
