<%-- 
    Document   : createFine
    Created on : Jul 18, 2025, 10:00:27 AM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title>Create Fine</title>

    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet" />
    </head>
    <body>
        <div class="container mt-5">

            <!-- Page Header with Back Buttons -->
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h2 class="mb-0">Create Fine</h2>
                <div>
                    <a href="MainController?action=listFine" class="btn btn-outline-secondary me-2">📋 Fine List</a>
                    <a href="MainController?action=viewAllBorrows" class="btn btn-outline-secondary">📚 All Borrows</a>
                </div>
            </div>

            <!-- Form -->
            <form action="MainController" method="post" class="needs-validation" novalidate>
                <input type="hidden" name="action" value="createFine"/>

                <div class="mb-3">
                    <label class="form-label">Borrow ID:</label>
                    <input type="text" class="form-control" value="${borrowId}" readonly />
                    <input type="hidden" name="borrowId" value="${borrowId}" />
                </div>

                <div class="mb-3">
                    <label for="reason" class="form-label">Reason:</label>
                    <select class="form-select" id="reason" name="reason" required>
                        <option value="" disabled selected>-- Select reason --</option>
                        <c:forEach var="reason" items="${reasonList}">
                            <option value="${reason.reasonCode}">${reason.displayName}</option>
                        </c:forEach>
                    </select>
                    <div class="invalid-feedback">Please select a reason.</div>
                </div>

                <div class="mb-3">
                    <label for="amount" class="form-label">Amount:</label>
                    <input type="number" class="form-control" id="amount" name="amount" step="0.01" min="0" required />
                    <div class="invalid-feedback">Please enter a valid amount.</div>
                </div>

                <button type="submit" class="btn btn-primary w-100">✅ Create Fine</button>
            </form>

            <!-- Message Alert -->
            <c:if test="${not empty message}">
                <div class="alert alert-success mt-4" role="alert">
                    ${message}
                </div>
            </c:if>

        </div>

        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

        <!-- Bootstrap validation script -->
        <script>
        (() => {
          'use strict'
          const forms = document.querySelectorAll('.needs-validation')
          Array.from(forms).forEach(form => {
            form.addEventListener('submit', event => {
              if (!form.checkValidity()) {
                event.preventDefault()
                event.stopPropagation()
              }
              form.classList.add('was-validated')
            }, false)
          })
        })()
        </script>
    </body>
</html>
