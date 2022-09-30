<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Quản lý tài khoản</title>
</head>
<body>
<h4>
    <a href="/users?action=create">Tạo tài khoản mới</a>
    <div align="center">
        <table border="1" cellpadding="5">
            <caption><h2>Danh sách tài khoản</h2></caption>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Email</th>
                <th>Country</th>
                <th>Actions</th>
            </tr>
            <c:forEach var="user" items="${listUser}">
                <tr>
                    <td><c:out value="${user.id}"/></td>
                    <td><c:out value="${user.name}"/></td>
                    <td><c:out value="${user.email}"/></td>
                    <td><c:out value="${user.country}"/></td>
                    <td>
                        <a href="/users?action=edit&id=${user.id}">Edit</a>
                        <a href="/users?action=delete&id=${user.id}">Delete</a>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </div>
</h4>
</body>
</html>
