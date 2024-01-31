<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
<title>
Esito</title></head>
<body>
<c:choose>
<c:when test="${esito!=null}">
Risultato:<br>
<table>
<tr><td> MATRICOLA </td><td> NOME </td><td> COGNOME </td><td> CORSO DI LAUREA </td><td> E-MAIL STUDENTE </td>
<td> CORSO </td><td> DOCENTE </td><td> DATA APPELLO</td><td> STATO VALUTAZIONE </td><td> VOTO </td></tr>
<tr><td>${user.matricola}</td>
	<td>${user.name}</td>
	<td>${user.surname}</td>
	<td>${user.corsoLaurea}</td>
	<td>${user.email}</td>
	<td>${esito.course}</td>
	<td>${esito.teacherName} ${esito.teacherSurname}</td>
    <td>${date}</td>
    <td>${esito.status}</td>
    <td>${esito.vote}</td>
    </tr>
    </table>
    <c:if test="${esito.status eq 'RIFIUTATO'}"> <br>Il voto è stato rifiutato </c:if>
    <c:if test="${esito.status eq 'PUBBLICATO'}"><br><form action="Refuse" method="post"> <input type="hidden" name="exc" value="${exc}">
    <input type="hidden" name="exd" value="${esito.date}"><input type="hidden" name="o" value='0'> <input type="submit" value="RIFIUTA VALUTAZIONE"></form></c:if>
    </c:when><c:otherwise>VOTO NON ANCORA DEFINITO</c:otherwise></c:choose>
    <br>
    <br>
    <a href="<c:url value="/Logout"/>"> LOGOUT </a></body></html>