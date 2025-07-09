<%--
    Document   : productForm
    Created on : Jun 18, 2025, 9:33:54 AM
    Author     : Admin
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%-- Include the header --%>
<%@include file="partials/header.jsp" %>

<c:choose>
    <c:when test="${sessionScope.user != null && sessionScope.user.role eq 'admin'}">
        <div class="container mt-5">
            <h1 class="mb-4 text-center">${isAdd ? "ADD BOOK" : "EDIT BOOK"}</h1>
            <form action="MainController" method="post" enctype="multipart/form-data" class="needs-validation" novalidate>
                <input type="hidden" name="action" value="bookSubmitting"/>
                <input type="hidden" name="bookId" value="${book != null ? book.bookId : 0}"/>
                <input type="hidden" name="isAdd" value="${isAdd}"/>

                <div class="mb-3">
                    <label for="title" class="form-label">Title*</label>
                    <input type="text" name="title" id="title" class="form-control" value="${book.title}" required/>
                    <div class="invalid-feedback">
                        Please provide a title.
                    </div>
                </div>

                <div class="mb-3">
                    <label for="author" class="form-label">Author*</label>
                    <input type="text" name="author" id="author" class="form-control" value="${book.author}" required/>
                    <div class="invalid-feedback">
                        Please provide an author.
                    </div>
                </div>

                <div class="mb-3">
                    <label for="publisher" class="form-label">Publisher*</label>
                    <input type="text" name="publisher" id="publisher" class="form-control" value="${book.publisher}" required/>
                    <div class="invalid-feedback">
                        Please provide a publisher.
                    </div>
                </div>

                <div class="mb-3">
                    <label for="year" class="form-label">Year Published</label>
                    <input type="number" name="year" id="year" class="form-control" value="${book.yearPublished}" min="1000" max="2099" pattern="[0-9]{4}"/>
                    <div class="form-text">Enter a 4-digit year.</div>
                </div>

                <div class="mb-3">
                    <label for="ISBN" class="form-label">ISBN*</label>
                    <input type="text" name="ISBN" id="ISBN" class="form-control" value="${book.ISBN}" required/>
                    <div class="invalid-feedback">
                        Please provide an ISBN.
                    </div>
                </div>

                <div class="mb-3">
                    <label for="category" class="form-label">Category*</label>
                    <select name="categoryId" id="category" class="form-select" required>
                        <option value="" disabled ${book == null || book.categoryId == 0 ? "selected" : ""}>--- Select Category ---</option>
                        <c:forEach var="category" items="${listCategories}">
                            <option value="${category.categoryId}"
                                    ${book != null && book.categoryId == category.categoryId ? "selected" : ""}>
                                ${category.name}
                            </option>
                        </c:forEach>
                    </select>
                    <div class="invalid-feedback">
                        Please select a category.
                    </div>
                </div>

                <div class="mb-3">
                    <label for="quantity" class="form-label">Quantity*</label>
                    <input type="number" name="quantity" id="quantity" class="form-control" value="${book.quantity}" required min="0"/>
                    <div class="invalid-feedback">
                        Please provide a valid quantity.
                    </div>
                </div>

                <div class="mb-3">
                    <label for="available" class="form-label">Available</label>
                    <input type="number" name="available" id="available" class="form-control" value="${book.available}" min="0"/>
                    <div class="form-text">Automatically updated on borrow/return.</div>
                </div>

                <div class="mb-3">
                    <label for="image" class="form-label">Image</label>
                    <input type="file" name="image" class="form-control" />
                </div>

                <button type="submit" class="btn btn-primary mt-3">${isAdd?"Create":"Update"}</button>
            </form>
            <c:if test="${not empty message}">
                <div class="alert alert-info mt-3" role="alert">
                    ${message}
                </div>
            </c:if>
            <c:if test="${not empty accessDenied}">
                <div class="alert alert-info mt-3" role="alert">
                    ${accessDenied}
                </div>
            </c:if>
        </div>
    </c:when>
    <c:otherwise>
        <div class="container text-center mt-5">
            <p class="alert alert-danger">You do not have permission to access this page. Please <a href="MainController" class="alert-link">login</a> as an administrator to proceed.</p>
        </div>
    </c:otherwise>
</c:choose>

<script>
// Bootstrap form validation
    (function () {
        'use strict';

        // Fetch all the forms we want to apply custom Bootstrap validation styles to
        var forms = document.querySelectorAll('.needs-validation');

        // Loop over them and prevent submission
        Array.prototype.slice.call(forms)
                .forEach(function (form) {
                    form.addEventListener('submit', function (event) {
                        if (!form.checkValidity()) {
                            event.preventDefault();
                            event.stopPropagation();
                        }

                        form.classList.add('was-validated');
                    }, false);
                });
    })();
</script>

<%-- Include the footer --%>
<%@include file="partials/footer.jsp" %>