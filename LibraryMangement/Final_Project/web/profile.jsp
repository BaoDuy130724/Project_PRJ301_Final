<%--
    Document   : profile
    Created on : Jul 7, 2025, 7:09:33 PM
    Author     : Admin
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%-- Include the header --%>
<%@include file="partials/header.jsp" %>

<c:choose>
    <c:when test="${not empty sessionScope.user}">
        <div class="container mt-5">
            <ul class="nav nav-pills mb-3" id="profileTabs" role="tablist">
                <li class="nav-item" role="presentation">
                    <button class="nav-link active" id="profile-tab" data-bs-toggle="tab" data-bs-target="#profileContent" type="button" role="tab" aria-controls="profileContent" aria-selected="true">Profile</button>
                </li>
                <li class="nav-item" role="presentation">
                    <button class="nav-link" id="borrows-tab" data-bs-toggle="tab" data-bs-target="#borrowContent" type="button" role="tab" aria-controls="borrowContent" aria-selected="false">My Borrows</button>
                </li>
            </ul>

            <div class="tab-content" id="profileTabContent">
                <div class="tab-pane fade show active" id="profileContent" role="tabpanel" aria-labelledby="profile-tab">
                    <h2 class="mb-4">Profile Information</h2>
                    <form action="MainController" method="post">
                        <input type="hidden" name="action" value="updateProfile" />
                        <div class="mb-3">
                            <label for="username" class="form-label">Username:</label>
                            <input type="text" class="form-control" id="username" name="username" value="${sessionScope.user.userName}" readonly />
                        </div>
                        <div class="mb-3">
                            <label for="fullName" class="form-label">Full Name:</label>
                            <input type="text" class="form-control" id="fullName" name="fullName" value="${sessionScope.user.fullName}" required/>
                        </div>
                        <div class="mb-3">
                            <label for="email" class="form-label">Email:</label>
                            <input type="email" class="form-control" id="email" name="email" value="${sessionScope.user.email}" required/>
                        </div>
                        <c:if test="${sessionScope.user.role eq 'admin'}">
                            <div class="mb-3">
                                <label class="form-label">Role:</label>
                                <input type="text" class="form-control" value="${sessionScope.user.role}" readonly/>
                            </div>
                        </c:if>
                        <button type="submit" class="btn btn-primary">Update Profile</button>
                    </form>

                    <c:if test="${not empty message}">
                        <div class="alert alert-success mt-3" role="alert">
                            ${message}
                        </div>
                    </c:if>
                </div>

                <div class="tab-pane fade" id="borrowContent" role="tabpanel" aria-labelledby="borrows-tab">
                    <h2 class="mb-4">My Borrow Orders</h2>

                    <form action="BorrowController" method="get" class="row g-3 align-items-end mb-4">
                        <input type="hidden" name="action" value="searchMyBorrows"/>
                        <div class="col-md-auto">
                            <label for="fromDate" class="form-label">From:</label>
                            <input type="date" class="form-control" id="fromDate" name="fromDate" value="${fromDate}" />
                        </div>
                        <div class="col-md-auto">
                            <label for="toDate" class="form-label">To:</label>
                            <input type="date" class="form-control" id="toDate" name="toDate" value="${toDate}" />
                        </div>
                        <div class="col-md-auto">
                            <button type="submit" class="btn btn-primary">Search</button>
                        </div>
                    </form>

                    <c:if test="${not empty myBorrows}">
                        <div class="table-responsive">
                            <table class="table table-striped table-bordered table-hover">
                                <thead class="table-dark">
                                    <tr>
                                        <th>No</th>
                                        <th>Borrow Date</th>
                                        <th>Return Date</th>
                                        <th>Status</th>
                                        <th>Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="b" items="${myBorrows}" varStatus="status">
                                        <tr>
                                            <td>${status.count}</td>
                                            <td>${b.borrowDate}</td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${empty b.returnDate}">
                                                        <span class="text-danger">Not Returned</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        ${b.returnDate}
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>${b.status}</td>
                                            <td>
                                                <button type="button" class="btn btn-info btn-sm" onclick="viewDetail(${b.borrowId})">View Detail</button>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:if>
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger mt-3" role="alert">
                            ${error}
                        </div>
                    </c:if>

                    <c:if test="${empty myBorrows}">
                        <p class="mt-3">No borrow records found.</p>
                    </c:if>
                </div>
            </div>
        </div>

        <div class="modal fade" id="borrowDetailModal" tabindex="-1" aria-labelledby="borrowDetailModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg modal-dialog-scrollable">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="borrowDetailModalLabel">Borrow Details</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body" id="detailContent">
                        Loading...
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>

        <script>
            // Function to activate a specific Bootstrap tab programmatically on page load
            window.onload = function () {
                const activeTabName = "${activeTab}"; // Get the active tab name from server-side variable

                // Find the corresponding Bootstrap tab button and activate it
                if (activeTabName === "borrows") {
                    const borrowsTab = document.getElementById('borrows-tab');
                    if (borrowsTab) {
                        const bsTab = new bootstrap.Tab(borrowsTab);
                        bsTab.show();
                    }
                } else {
                    const profileTab = document.getElementById('profile-tab');
                    if (profileTab) {
                        const bsTab = new bootstrap.Tab(profileTab);
                        bsTab.show();
                    }
                }
            };

            // Function to display borrow details in a Bootstrap modal
            function viewDetail(borrowId) {
                const detailContent = document.getElementById("detailContent");
                detailContent.innerHTML = "<p>Loading borrow details...</p>"; // Initial loading message

                // Get the Bootstrap modal instance
                const borrowDetailModal = new bootstrap.Modal(document.getElementById('borrowDetailModal'));
                borrowDetailModal.show(); // Show the modal

                // Fetch borrow details via AJAX
                fetch(`BorrowController?action=viewBorrowDetailAjax&borrowId=` + borrowId)
                        .then(response => {
                            if (!response.ok) {
                                throw new Error("Failed to load borrow details. Status: " + response.status);
                            }
                            return response.text();
                        })
                        .then(html => {
                            detailContent.innerHTML = html; // Populate modal body with fetched HTML
                        })
                        .catch(err => {
                            detailContent.innerHTML = `<p class="text-danger">Error: ${err.message}</p>`;
                        });
            }
        </script>
    </c:when>
    <c:otherwise>
        <div class="container text-center mt-5">
            <p class="alert alert-warning">You must <a href="MainController">login</a> to view your profile.</p>
        </div>
    </c:otherwise>
</c:choose>

<%-- Include the footer --%>
<%@include file="partials/footer.jsp" %>