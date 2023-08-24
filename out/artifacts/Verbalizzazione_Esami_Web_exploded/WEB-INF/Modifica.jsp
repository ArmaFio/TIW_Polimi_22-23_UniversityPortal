<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Modifica</title>
</head>
<body>
Risultato:<br>
<table border="1">
<tr><td> MATRICOLA </td><td> NOME </td><td> COGNOME </td><td> CORSO DI LAUREA </td><td> E-MAIL STUDENTE </td><td> VOTO </td></tr>
<tr><td>${line.matr}</td>
	<td>${line.name}</td>
	<td>${line.surname}</td>
	<td>${line.gc}</td>
	<td>${line.email}</td>
    <td><form action="Insert" method="post">
	<input type="text" name="vote" required> <br>
	<input type="hidden" name="exc" value="${exc}"> <br>
	<input type="hidden" name="exd" value="${exd}"> <br>
	<input type="hidden" name="id" value="${matr}"> <br>
	<input type="submit" value="Insert"></form></td>
    </tr>
    </table>
     <br>
    <br>
    <a href="<c:url value="/Logout"/>"> LOGOUT</a>
</body>
</html>