<%-- 
    Document   : markLostForm.jsp
    Created on : Jul 22, 2025, 7:06:39 AM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>


<html>
<head>
    <meta charset="UTF-8">
    <title>Mark Lost Books</title>
    <!-- Bootstrap CSS (online link) -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container py-5">
    <div class="card shadow rounded-4">
        <div class="card-body">
            <h2 class="card-title text-danger mb-4">Mark Lost Books</h2>

            <!-- Display error or message -->
            <c:if test="${not empty message}">
                <div class="alert alert-info">${message}</div>
            </c:if>
            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>

            <form action="BorrowController" method="post" onsubmit="return validateLostQuantities();">
                <input type="hidden" name="action" value="confirmMarkLost">
                <input type="hidden" name="borrowId" value="${borrowId}">
                
                <div class="table-responsive">
                    <table class="table table-hover align-middle text-center">
                        <thead class="table-light">
                            <tr>
                                <th scope="col">Book Title</th>
                                <th scope="col">Borrowed Quantity</th>
                                <th scope="col">Lost Quantity</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="item" items="${borrowDetails}">
                                <tr>
                                    <td>${item.bookTitle}</td>
                                    <td>${item.quantity}</td>
                                    <td>
                                        <input type="number"
                                               name="lostQty_${item.bookId}" 
                                               min="0"
                                               max="${item.quantity}" 
                                               value="0"
                                               class="form-control form-control-sm w-50 mx-auto text-center" />
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>

                <div class="d-flex justify-content-end gap-2 mt-4">
                    <button type="submit" class="btn btn-danger px-4">Confirm Lost</button>
                    <a href="MainController?action=viewAllBorrows" class="btn btn-secondary">Cancel</a>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- JavaScript: Validate lost quantities -->
<script>
function validateLostQuantities() {
    const inputs = document.querySelectorAll("input[name^='lostQty_']");
    for (let input of inputs) {
        const max = parseInt(input.getAttribute('max'));
        const min = parseInt(input.getAttribute('min'));
        const value = parseInt(input.value) || 0;

        if (value < min || value > max) {
            alert(`Lost quantity must be between ${min} and ${max}.`);
            input.focus();
            return false;
        }
    }
    return confirm('Are you sure you want to mark these books as lost?');
}
</script>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
