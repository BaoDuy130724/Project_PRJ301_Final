<%-- 
    Document   : borrowList
    Created on : Jul 7, 2025, 8:26:52 PM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Borrow Orders</title>
    </head>
    <body>
        <c:choose>
            <c:when test="${sessionScope.user.role eq 'admin'}">
                <h1>Borrow Orders (Admin)</h1>
                <form action="MainController" method="get">
                    <input type="hidden" name="action" value="viewAllBorrows" />
                    <input type="text" name="txtSearch" placeholder="Search by user name"
                           value="${searchName != null ? searchName : ''}" />
                    <input type="submit" value="Search" />
                </form>
                <br/>
                <table border="1">
                    <thead>
                        <tr>
                            <th>Borrow ID</th>
                            <th>User</th>
                            <th>Borrow Date</th>
                            <th>Return Date</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>

                    <tbody>
                        <c:forEach var="borrow" items="${listBorrows}">
                            <tr>
                                <td>${borrow.borrowId}</td>
                                <td>${borrow.fullName}</td>
                                <td>${borrow.borrowDate}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${empty borrow.returnDate}">
                                            <span style="color:red;">Not returned</span>
                                        </c:when>
                                        <c:otherwise>
                                            ${borrow.returnDate}
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>${borrow.status}</td>
                                <td>
                                    <a href="javascript:void(0);" onclick="loadDetail(${borrow.borrowId})">View Detail</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                <div id="borrowDetailPopup" style="display:none; position:fixed; top:10%; left:20%; width:60%; background:#fff; border:1px solid #aaa; padding:20px; box-shadow: 0px 0px 10px #000; z-index:999;">
                    <h2>Borrow Detail</h2>
                    <div id="popupContent">
                        Loading...
                    </div>
                    <button onclick="closePopup()">Close</button>
                </div>
                <script>
                    function loadDetail(borrowId) {
                        fetch('MainController?action=viewBorrowDetailAjax&borrowId=' + borrowId)
                                .then(response => response.text())
                                .then(html => {
                                    document.getElementById("popupContent").innerHTML = html;
                                    document.getElementById("borrowDetailPopup").style.display = "block";
                                })
                                .catch(error => {
                                    document.getElementById("popupContent").innerHTML = "Failed to load detail.";
                                });
                    }

                    function closePopup() {
                        document.getElementById("borrowDetailPopup").style.display = "none";
                    }
                </script>
            </c:when>
            <c:otherwise>
                <p>You do not have permission to access. Please login to access!!!
                    <a href="MainController">Login</a>
                </p>
            </c:otherwise>
        </c:choose>
    </body>
</html>
