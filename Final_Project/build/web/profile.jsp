<%-- 
    Document   : profile
    Created on : Jul 7, 2025, 7:09:33 PM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>PROFILE Page</title>
        <style>
            .tab-button {
                padding: 10px 20px;
                cursor: pointer;
                margin-right: 10px;
                background-color: lightgray;
                border: none;
            }
            .active-tab {
                background-color: #007bff;
                color: white;
            }
            .tab-content {
                display: none;
                margin-top: 20px;
            }
            #detailOverlay {
                display: none;
                position: fixed;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                background: rgba(0,0,0,0.5);
                z-index: 999;
            }
            #detailPopup {
                display: none;
                position: fixed;
                top: 50%;
                left: 50%;
                transform: translate(-50%, -50%);
                background: white;
                padding: 20px;
                border-radius: 8px;
                box-shadow: 0 0 10px rgba(0,0,0,0.3);
                z-index: 1000;
                min-width: 300px;
                max-height: 80vh;
                overflow: auto;
            }
        </style>
    </head>
    <body>
        <c:choose>
            <c:when test="${not empty sessionScope.user}">

                <!-- Tab Buttons -->
                <div>
                    <button class="tab-button active-tab" onclick="showTab('profileTab', this)">Profile</button>
                    <button class="tab-button" onclick="showTab('borrowTab', this)">My Borrows</button>
                </div>

                <!-- Profile Tab -->
                <div id="profileTab" class="tab-content" style="display:block;">
                    <h2>Profile Information</h2>
                    <form action="MainController" method="post">
                        <input type="hidden" name="action" value="updateProfile" />
                        <label>Username: </label>
                        <input type="text" name="username" value="${sessionScope.user.userName}" readonly /><br/>
                        <label>Full Name: </label>
                        <input type="text" name="fullName" value="${sessionScope.user.fullName}" required/><br/>
                        <label>Email: </label>
                        <input type="email" name="email" value="${sessionScope.user.email}" required/><br/>
                        <c:if test="${sessionScope.user.role eq 'admin'}">
                            <label>Role: </label>
                            <input type="text" value="${sessionScope.user.role}" readonly/><br/>
                        </c:if>
                        <input type="submit" value="Update Profile" />
                    </form>

                    <c:if test="${not empty message}">
                        <p style="color:green;">${message}</p>
                    </c:if>
                </div>

                <!-- Borrow Tab -->
                <div id="borrowTab" class="tab-content">
                    <h2>My Borrow Orders</h2>

                    <form action="BorrowController" method="get">
                        <input type="hidden" name="action" value="searchMyBorrows"/>
                        From: <input type="date" name="fromDate" value="${fromDate}" />
                        To:   <input type="date" name="toDate" value="${toDate}" />
                        <input type="submit" value="Search" />
                    </form>

                    <c:if test="${not empty myBorrows}">
                        <table border="1" cellpadding="5" style="margin-top:10px;">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Borrow Date</th>
                                    <th>Return Date</th>
                                    <th>Status</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="b" items="${myBorrows}">
                                    <tr>
                                        <td>${b.borrowId}</td>
                                        <td>${b.borrowDate}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${empty b.returnDate}">
                                                    <span style="color:red;">Not Returned</span>
                                                </c:when>
                                                <c:otherwise>
                                                    ${b.returnDate}
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>${b.status}</td>
                                        <td>
                                            <button onclick="viewDetail(${b.borrowId})">View Detail</button>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </c:if>
                    <c:if test="${not empty error}">
                        <p style="color:red;">${error}</p>
                    </c:if>

                    <c:if test="${empty myBorrows}">
                        <p>No borrow records found.</p>
                    </c:if>
                </div>
                <div id="detailOverlay" onclick="closeDetail()"></div>
                <div id="detailPopup">
                    <button onclick="closeDetail()">X</button>
                    <h3>Borrow Details</h3>
                    <div id="detailContent">Loading...</div>
                </div>

                <script>
                    function showTab(tabId, button) {
                        document.getElementById("profileTab").style.display = "none";
                        document.getElementById("borrowTab").style.display = "none";
                        document.getElementById(tabId).style.display = "block";

                        // Update button style
                        let buttons = document.getElementsByClassName("tab-button");
                        for (let btn of buttons) {
                            btn.classList.remove("active-tab");
                        }
                        if (button) {
                            button.classList.add("active-tab");
                        }
                    }

                    window.onload = function () {
                        const tab = "${activeTab}";
                        const btns = document.getElementsByClassName("tab-button");
                        if (tab === "borrows") {
                            showTab("borrowTab", btns[1]);
                        } else {
                            showTab("profileTab", btns[0]);
                        }
                    };
                    function viewDetail(borrowId) {
                        document.getElementById("detailOverlay").style.display = "block";
                        document.getElementById("detailPopup").style.display = "block";
                        document.getElementById("detailContent").innerHTML = "Loading...";

                        fetch(`BorrowController?action=viewBorrowDetailAjax&borrowId=` + borrowId)
                                .then(response => {
                                    if (!response.ok)
                                        throw new Error("Failed to load borrow details.");
                                    return response.text();
                                })
                                .then(html => {
                                    document.getElementById("detailContent").innerHTML = html;
                                })
                                .catch(err => {
                                    document.getElementById("detailContent").innerHTML = `<p style="color:red;">${err.message}</p>`;
                                });
                    }

                    function closeDetail() {
                        document.getElementById("detailOverlay").style.display = "none";
                        document.getElementById("detailPopup").style.display = "none";
                    }
                </script>
            </c:when>
            <c:otherwise>
                <p>You must <a href="MainController">login</a> to view your profile.</p>
            </c:otherwise>
        </c:choose>
    </body>
</html>
