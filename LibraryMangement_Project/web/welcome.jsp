
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ include file="partials/header.jsp" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/welcome.css">

<div class="container mt-4">
    <c:choose>
        <c:when test="${not empty sessionScope.user}">
            <div class="d-flex flex-column flex-md-row justify-content-between align-items-md-center mb-4">
                <h2 class="mb-3 mb-md-0">Welcome, ${sessionScope.user.fullName}!</h2>
                <div class="d-flex">
                    <c:if test="${sessionScope.user.role eq 'admin'}">
                        <a href="MainController?isAdd=true&action=addBook" class="btn btn-success me-2">
                            <i class="bi bi-plus-circle-fill me-1"></i> Add New Book
                        </a>
                        <a href="MainController?action=adminViewAllReservations" class="btn btn-outline-primary me-2">
                            <i class="bi bi-journal-text me-1"></i> View All Reservations
                        </a>    
                    </c:if>

                    <c:if test="${sessionScope.user.role eq 'member'}">
                        <button class="btn btn-secondary" data-bs-toggle="modal" data-bs-target="#borrowCartPopup">
                            <i class="bi bi-cart-fill me-1"></i> View Borrow Cart
                            <c:if test="${not empty sessionScope.borrowCart && fn:length(sessionScope.borrowCart) > 0}">
                                <span class="badge bg-danger ms-1">${fn:length(sessionScope.borrowCart)}</span>
                            </c:if>
                        </button>
                        <a href="MainController?action=viewReservations" class="btn btn-outline-info ms-2">
                            <i class="bi bi-bookmark-check me-1"></i> Reservation
                        </a>
                    </c:if>

                    <a href="MainController?action=listFine" class="btn btn-outline-warning ms-2">
                        <i class="bi bi-exclamation-circle me-1"></i> View List Fine
                    </a>
                </div>
            </div>

            <form action="MainController" method="get" class="input-group mb-4" style="max-width: 500px;">
                <input type="hidden" name="action" value="search">
                <input type="text" name="txtSearch" class="form-control" placeholder="Search by book title..." value="${searchTitle != null ? searchTitle : ''}" aria-label="Book Title Search">
                <button class="btn btn-outline-primary" type="submit">
                    <i class="bi bi-search me-1"></i> Search
                </button>
            </form>

            <c:if test="${not empty message}">
                <div class="alert alert-info mt-3" role="alert">${message}</div>
            </c:if>

            <c:if test="${not empty listBooks}">
                <div class="table-responsive mt-3">
                    <table class="table table-bordered table-striped table-hover align-middle">
                        <thead class="table-dark">
                            <tr>
                                <c:if test="${sessionScope.user.role eq 'admin'}">
                                    <th>Book ID</th>
                                    </c:if>
                                <th>Image</th>
                                <th>Title</th>
                                <th>Author</th>
                                <th>Publisher</th>
                                <th>Year</th>
                                    <c:if test="${sessionScope.user.role eq 'admin'}">
                                    <th>ISBN</th>
                                    <th>Category</th>
                                    <th>Quantity</th>
                                    <th>Status</th>
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
                                    <td>
                                        <img src="${pageContext.request.contextPath}/assets/book_images/${book.image != null ? book.image : 'default.jpg'}"
                                             alt="${book.title}"
                                             class="img-thumbnail"
                                             style="width: 60px; height: auto;" />
                                    </td>
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
                                                    <span class="badge bg-danger">Discontinued</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge bg-success">Active</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                    </c:if>
                                    <td>
                                        <c:choose>
                                            <c:when test="${book.available == 0}">
                                                <span class="text-danger fw-bold">Out of Stock</span>
                                            </c:when>
                                            <c:otherwise>
                                                ${book.available}
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${sessionScope.user.role eq 'admin'}">
                                                <div class="d-flex flex-column gap-2">
                                                    <a href="MainController?action=editBook&bookId=${book.bookId}" class="btn btn-sm btn-outline-primary">
                                                        <i class="bi bi-pencil-square"></i> Edit
                                                    </a>
                                                    <a href="MainController?action=deleteBook&bookId=${book.bookId}" class="btn btn-sm btn-outline-danger" onclick="return confirm('Are you sure you want to delete this book? This action cannot be undone.')">
                                                        <i class="bi bi-trash"></i> Delete
                                                    </a>
                                                </div>
                                            </c:when>
                                            <c:otherwise>
                                                <c:if test="${book.available > 0}">
                                                    <a href="MainController?action=addToCart&bookId=${book.bookId}" class="btn btn-sm btn-outline-success">
                                                        <i class="bi bi-cart-plus"></i> Add to Cart
                                                    </a>
                                                </c:if>
                                                <c:if test="${book.available == 0}">
                                                    <button class="btn btn-sm btn-outline-secondary" disabled>
                                                        <i class="bi bi-x-circle"></i> Unavailable
                                                    </button>
                                                </c:if>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:if>
            <c:if test="${empty listBooks}">
                <p class="alert alert-info">No books found matching your search criteria.</p>
            </c:if>

            <div class="modal fade" id="borrowCartPopup" tabindex="-1" aria-labelledby="borrowCartPopupLabel" aria-hidden="true">
                <div class="modal-dialog modal-lg modal-dialog-centered modal-dialog-scrollable">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="borrowCartPopupLabel">Your Borrow Cart</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <c:choose>
                                <c:when test="${empty sessionScope.borrowCart || fn:length(sessionScope.borrowCart) == 0}">
                                    <p class="text-center text-muted">Your borrow cart is currently empty. Start adding books!</p>
                                </c:when>
                                <c:otherwise>
                                    <form action="MainController" method="post" id="borrowCartForm">
                                        <input type="hidden" name="action" value="confirmBorrow" />
                                        <div class="table-responsive">
                                            <table class="table table-bordered table-striped align-middle">
                                                <thead>
                                                    <tr>
                                                        <th>Book Title</th>
                                                        <th class="text-center">Quantity</th>
                                                        <th class="text-center">Action</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach var="item" items="${sessionScope.borrowCart}" varStatus="loop">
                                                        <tr>
                                                            <td>${item.bookTitle}</td>
                                                            <td class="text-center">${item.quantity}</td>
                                                            <td class="text-center">
                                                                <button type="button" class="btn btn-sm btn-danger" onclick="submitRemoveForm(${loop.index})">
                                                                    <i class="bi bi-x-circle me-1"></i> Remove
                                                                </button>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                        <div class="d-flex justify-content-end mt-3">
                                            <button type="submit" class="btn btn-primary">
                                                <i class="bi bi-check-circle-fill me-1"></i> Confirm Borrow
                                            </button>
                                        </div>
                                    </form>
                                    <form id="removeForm" action="MainController" method="post" style="display: none;">
                                        <input type="hidden" name="action" value="removeFromCart">
                                        <input type="hidden" name="index" id="removeIndex">
                                        <input type="hidden" name="showCart" value="true">
                                    </form>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>

            <c:if test="${showCartPopup == true}">
                <script>
                    document.addEventListener('DOMContentLoaded', function () {
                        var cartModal = new bootstrap.Modal(document.getElementById('borrowCartPopup'));
                        cartModal.show();
                    });
                </script>
            </c:if>
            <script>
                function submitRemoveForm(index) {
                    document.getElementById("removeIndex").value = index;
                    document.getElementById("removeForm").submit();
                }
            </script>

        </c:when>

        <c:otherwise>
            <div class="alert alert-danger mt-5 text-center" role="alert">
                You do not have permission to access this page. Please <a href="MainController" class="alert-link">login</a> first!
            </div>
        </c:otherwise>
    </c:choose>
</div>

<%@ include file="partials/footer.jsp" %>