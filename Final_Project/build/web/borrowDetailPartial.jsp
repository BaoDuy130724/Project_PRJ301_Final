<%-- 
    Document   : borrowDetailPartial
    Created on : Jul 7, 2025, 9:22:49 PM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Borrow Detail</title>
    </head>
    <body>
        <table border="1" cellpadding="6" cellspacing="0" width="100%">
            <thead>
                <tr>
                    <th>Book ID</th>
                    <th>Title</th>
                    <th>Quantity</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="d" items="${details}">
                    <tr>
                        <td>${d.bookId}</td>
                        <td>${d.bookTitle}</td>
                        <td>${d.quantity}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </body>
</html>
