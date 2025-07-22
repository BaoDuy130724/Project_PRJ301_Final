<%-- 
    Document   : reservationCart
    Created on : Jul 16, 2025, 10:09:50 PM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body class="bg-light">
        <div class="container mt-5">
            <div class="d-flex justify-content-between align-items-center mb-4">
            <h2 class="mb-0">Reservation Cart</h2>
            <a href="MainController?action=viewReservations" class="btn btn-outline-primary">
                📚 Back to Book List
            </a>
        </div>

            <c:if test="${empty sessionScope.reservationCart}">
                <div class="alert alert-info">Your reservation cart is empty.</div>
            </c:if>

            <c:if test="${not empty sessionScope.reservationCart}">
                <table class="table table-bordered table-hover align-middle">
                    <thead class="table-primary">
                        <tr>
                            <th>Image</th>
                            <th>Title</th>
                            <th>Author</th>
                            <th>Quantity</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="item" items="${sessionScope.reservationCart}">
                            <tr>
                                <td style="width: 80px;">
                                    <img src="${pageContext.request.contextPath}/assets/book_images/${item.book.image}" 
                                         alt="${item.book.title}" class="img-fluid rounded" />
                                </td>
                                <td>${item.book.title}</td>
                                <td>${item.book.author}</td>
                                <td style="width: 180px;">
                                    <form action="MainController" method="post" class="d-flex align-items-center">
                                        <input type="hidden" name="action" value="updateQuantity" />
                                        <input type="hidden" name="bookId" value="${item.book.bookId}" />
                                        <input type="number" name="quantity" value="${item.quantity}" min="1" 
                                               class="form-control form-control-sm me-2" style="width: 70px;">
                                        <button class="btn btn-sm btn-outline-secondary" type="submit">Update</button>
                                    </form>
                                </td>
                                <td>
                                    <form action="MainController" method="post">
                                        <input type="hidden" name="action" value="removeFromReservationCart" />
                                        <input type="hidden" name="bookId" value="${item.book.bookId}" />
                                        <button type="button" class="btn btn-sm btn-danger" 
                                                data-bs-toggle="modal" data-bs-target="#confirmRemoveModal${item.book.bookId}">
                                            Remove
                                        </button>

                                        <!-- Modal -->
                                        <div class="modal fade" id="confirmRemoveModal${item.book.bookId}" tabindex="-1">
                                            <div class="modal-dialog modal-dialog-centered">
                                                <div class="modal-content">
                                                    <div class="modal-header">
                                                        <h5 class="modal-title">Confirm Removal</h5>
                                                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                                    </div>
                                                    <div class="modal-body">
                                                        Are you sure you want to remove <strong>${item.book.title}</strong> from the cart?
                                                    </div>
                                                    <div class="modal-footer">
                                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                                        <button type="submit" class="btn btn-danger">Remove</button>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>

                <!-- Cart Actions -->
                <div class="d-flex justify-content-between mt-4">
                    <!-- Clear Cart -->
                    <form action="MainController" method="post">
                        <input type="hidden" name="action" value="clearReservationCart" />
                        <button type="button" class="btn btn-outline-danger" data-bs-toggle="modal" data-bs-target="#confirmClearModal">
                            Clear Cart
                        </button>

                        <!-- Modal -->
                        <div class="modal fade" id="confirmClearModal" tabindex="-1">
                            <div class="modal-dialog modal-dialog-centered">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h5 class="modal-title">Confirm Clear Cart</h5>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                    </div>
                                    <div class="modal-body">
                                        Are you sure you want to clear your entire reservation cart?
                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                        <button type="submit" class="btn btn-danger">Clear All</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form>

                    <!-- Confirm Reservation -->
                    <form action="MainController" method="post">
                        <input type="hidden" name="action" value="confirmReservation" />
                        <button type="button" class="btn btn-success" data-bs-toggle="modal" data-bs-target="#confirmReservationModal">
                            Confirm Reservation
                        </button>

                        <!-- Modal -->
                        <div class="modal fade" id="confirmReservationModal" tabindex="-1">
                            <div class="modal-dialog modal-dialog-centered">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h5 class="modal-title">Confirm Reservation</h5>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                    </div>
                                    <div class="modal-body">
                                        Are you sure you want to confirm your reservation?
                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                        <button type="submit" class="btn btn-success">Confirm</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </c:if>
        </div>

        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
