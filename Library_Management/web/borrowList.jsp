<%--
    Document   : borrowList
    Created on : Jul 7, 2025, 8:26:52 PM
    Author     : Admin
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%-- Include the header --%>
<%@ include file="partials/header.jsp" %>

<c:choose>
    <c:when test="${sessionScope.user != null && sessionScope.user.role eq 'admin'}">
        <div class="container mt-5">
            <h1 class="mb-4">Borrow Orders (Admin)</h1>

            <form action="MainController" method="get" class="row g-3 align-items-end mb-4">
                <input type="hidden" name="action" value="viewAllBorrows" />
                <div class="col-md-auto">
                    <label for="txtSearch" class="visually-hidden">Search by user name</label>
                    <input type="text" name="txtSearch" id="txtSearch" class="form-control" placeholder="Search by user name"
                           value="${searchName != null ? searchName : ''}" />
                </div>
                <div class="col-md-auto">
                    <button type="submit" class="btn btn-primary">Search</button>
                </div>
            </form>
            <c:if test="${not empty listBorrows}">
                <div class="table-responsive">
                    <table class="table table-striped table-bordered table-hover">
                        <thead class="table-dark">
                            <tr>
                                <th>No</th>
                                <th>User</th>
                                <th>Borrow Date</th>
                                <th>Expected Return</th>
                                <th>Return Date</th>
                                <th>Status</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="borrow" items="${listBorrows}" varStatus="status">
                                <tr>
                                    <td>${status.count}</td>
                                    <td>${borrow.fullName}</td>
                                    <td>${borrow.borrowDate}</td>
                                    <td>${borrow.expectedReturnDate}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${empty borrow.returnDate}">
                                                <span class="text-danger">Not returned</span>
                                            </c:when>
                                            <c:otherwise>
                                                ${borrow.returnDate}
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>${borrow.status}</td>
                                    <td>
                                        <button type="button" class="btn btn-info btn-sm" onclick="loadDetail(${borrow.borrowId})">View Detail</button>
                                        <c:if test="${empty borrow.returnDate and borrow.status ne 'Lost' and borrow.status ne 'Partially Lost'}">
                                            <form action="MainController" method="post" style="display:inline;">
                                                <input type="hidden" name="action" value="markReturned">
                                                <input type="hidden" name="borrowId" value="${borrow.borrowId}">
                                                <button type="submit" class="btn btn-success btn-sm"
                                                        onclick="return confirm('Mark this borrow as returned?')">
                                                    Mark as Returned
                                                </button>
                                            </form>
                                        </c:if>
                                        <!-- phong them -->
                                        <c:if test="${borrow.status ne 'Returned' and borrow.status ne 'Lost' and borrow.status ne 'Partially Lost'}">
                                            <form action="MainController" method="get" style="display:inline;">
                                                <input type="hidden" name="action" value="markLostForm">
                                                <input type="hidden" name="borrowId" value="${borrow.borrowId}">
                                                <button type="submit" class="btn btn-danger btn-sm"
                                                        onclick="return confirm('Mark this borrow as lost?')">
                                                    Mark as Lost
                                                </button>
                                            </form>
                                        </c:if>
                                        <c:if test="${borrow.status eq 'Returned'}">
                                            <form action="FineController" method="get" style="display:inline;">
                                                <input type="hidden" name="action" value="createForm" />
                                                <input type="hidden" name="borrowId" value="${borrow.borrowId}" />
                                                <button type="submit" class="btn btn-warning btn-sm">
                                                    Create Fine
                                                </button>
                                            </form>
                                        </c:if>

                                        <!-- phong them toi day-->
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:if>
            <c:if test="${empty listBorrows}">
                <p class="alert alert-info">No borrow orders found for the current search criteria.</p>
            </c:if>
            <c:if test="${not empty accessDenied}">
                <p class="alert alert-info">${accessDenied}</p>
            </c:if>
        </div>

        <div class="modal fade" id="borrowDetailModal" tabindex="-1" aria-labelledby="borrowDetailModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg modal-dialog-scrollable">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="borrowDetailModalLabel">Borrow Detail</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body" id="popupContent">
                        Loading...
                    </div>
                </div>
            </div>
        </div>

        <script>
            function loadDetail(borrowId) {
                const popupContent = document.getElementById("popupContent");
                popupContent.innerHTML = "<p>Loading borrow details...</p>"; // Initial loading message

                // Get the Bootstrap modal instance
                const borrowDetailModal = new bootstrap.Modal(document.getElementById('borrowDetailModal'));
                borrowDetailModal.show(); // Show the modal

                // Fetch borrow details via AJAX
                fetch(`MainController?action=viewBorrowDetailAjax&borrowId=` + borrowId)
                        .then(response => {
                            if (!response.ok) {
                                throw new Error("Failed to load borrow detail. Status: " + response.status);
                            }
                            return response.text();
                        })
                        .then(html => {
                            popupContent.innerHTML = html; // Populate modal body with fetched HTML
                        })
                        .catch(error => {
                            popupContent.innerHTML = `<p class="text-danger">Error: ${error.message}</p>`;
                        });
            }
            // The closePopup function is no longer needed, as Bootstrap handles closing via data-bs-dismiss="modal"
        </script>
    </c:when>
    <c:otherwise>
        <div class="container text-center mt-5">
            <p class="alert alert-danger">You do not have permission to access this page. Please <a href="MainController" class="alert-link">login</a> as an administrator to proceed.</p>
        </div>
    </c:otherwise>
</c:choose>

<%-- Include the footer --%>
<%@include file="partials/footer.jsp" %>