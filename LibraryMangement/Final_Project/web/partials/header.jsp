<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- Bootstrap -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">

<!-- Bắt đầu cấu trúc HTML đầy đủ -->
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Library System</title>
</head>
<body class="d-flex flex-column min-vh-100"> <!-- ✨ Đây là phần quan trọng nhất -->

<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container-fluid">
        <a class="navbar-brand" href="MainController?action=home">
            <i class="bi bi-book"></i> Library System
        </a>

        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav me-auto">
                <c:if test="${sessionScope.user != null && sessionScope.user.role eq 'admin'}">
                    <li class="nav-item">
                        <a class="nav-link" href="MainController?action=viewAllBorrows">Borrow Orders</a>
                    </li>
                </c:if>
            </ul>

            <ul class="navbar-nav ms-auto">
                <c:choose>
                    <c:when test="${empty sessionScope.user}">
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/login.jsp">
                                <i class="bi bi-box-arrow-in-right"></i> Login
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/register.jsp">
                                <i class="bi bi-person-plus"></i> Register
                            </a>
                        </li>
                    </c:when>

                    <c:otherwise>
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown">
                                <i class="bi bi-person-circle"></i>
                                ${sessionScope.user.fullName}
                            </a>
                            <ul class="dropdown-menu dropdown-menu-end">
                                <li>
                                    <a class="dropdown-item" href="${pageContext.request.contextPath}/MainController?action=viewProfile">
                                        <i class="bi bi-person-lines-fill"></i> My Profile
                                    </a>
                                </li>
                                <li><hr class="dropdown-divider"></li>
                                <li>
                                    <a class="dropdown-item text-danger" href="${pageContext.request.contextPath}/MainController?action=logout">
                                        <i class="bi bi-box-arrow-right"></i> Logout
                                    </a>
                                </li>
                            </ul>
                        </li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</nav>
