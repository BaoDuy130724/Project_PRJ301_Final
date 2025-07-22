<%-- 
    Document   : lostBookForm
    Created on : Jul 22, 2025, 6:24:02 PM
    Author     : Admin
--%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ include file="partials/header.jsp" %>

<div class="container mt-5">
    <h2 class="mb-4">Report Lost Books</h2>

    <form action="MainController" method="post">
        <input type="hidden" name="action" value="confirmMarkLost" />
        <input type="hidden" name="borrowId" value="${borrowId}" />

        <table class="table table-bordered">
            <thead>
                <tr>
                    <th>Book Title</th>
                    <th>Borrowed Quantity</th>
                    <th>Lost Quantity</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="item" items="${borrowDetails}">
                    <tr>
                        <td>${item.bookTitle}</td>
                        <td>${item.quantity}</td>
                        <td>
                            <input type="number" name="lost_${item.bookId}" min="0" max="${item.quantity}" value="0" class="form-control" />
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <button type="submit" class="btn btn-danger">Confirm Lost</button>
        <a href="MainController?action=viewAllBorrows" class="btn btn-secondary">Cancel</a>
    </form>
</div>

<%@ include file="partials/footer.jsp" %>
