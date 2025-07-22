<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Login - Library System</title>

        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">

        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/login.css">
    </head>
    <body class="bg-light d-flex align-items-center justify-content-center vh-100">

        <div class="card shadow-sm p-4" style="width: 100%; max-width: 400px;">
            <h3 class="text-center mb-3">📚 Library Login</h3>

            <c:choose>
                <c:when test="${sessionScope.user != null}">
                    <c:redirect url="welcome.jsp" />
                </c:when>
                <c:otherwise>
                    <form action="MainController" method="post">
                        <input type="hidden" name="action" value="login"/>

                        <div class="mb-3">
                            <label for="name" class="form-label">User Name</label>
                            <input type="text" id="name" name="name" class="form-control" placeholder="Enter user name..." required>
                        </div>

                        <div class="mb-3">
                            <label for="password" class="form-label">Password</label>
                            <input type="password" id="password" name="password" class="form-control" placeholder="Enter password..." required>
                        </div>

                        <button type="submit" class="btn btn-primary w-100">Login</button>
                    </form>

                    <!-- Messages -->
                    <c:if test="${not empty message}">
                        <div class="alert alert-warning mt-3">${message}</div>
                    </c:if>
                    <c:if test="${not empty success}">
                        <div class="alert alert-success mt-3">${success}</div>
                    </c:if>
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger mt-3">${error}</div>
                    </c:if>

                    <p class="text-center mt-3">
                        Don't have an account? <a href="register.jsp">Register here</a>
                    </p>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
