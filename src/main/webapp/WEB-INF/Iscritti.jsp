<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<title>Iscritti</title></head>
<body>
<h1>ISCRITTI APPELLO<br></h1>
<table border=1>
<tr><td><a href="<c:url value="/GetResOrRegs?exc=${cc}&&exd=${date}&&o=${order==1? 2:1}"/>"> MATRICOLA </a></td>
<td><a href="<c:url value="/GetResOrRegs?exc=${cc}&&exd=${date}&&o=${order==5? 6:5}"/>"> NOME </a></td>
<td><a href="<c:url value="/GetResOrRegs?exc=${cc}&&exd=${date}&&o=${order==3? 4:3}"/>"> COGNOME </a></td>
<td><a href="<c:url value="/GetResOrRegs?exc=${cc}&&exd=${date}&&o=${order==9? 10:9}"/>"> CORSO DI LAUREA </a></td>
<td><a href="<c:url value="/GetResOrRegs?exc=${cc}&&exd=${date}&&o=${order==7? 8:7}"/>"> E-MAIL STUDENTE </a></td>
<td><a href="<c:url value="/GetResOrRegs?exc=${cc}&&exd=${date}&&o=${order==11? 12:11}"/>"> VOTO </a></td>
<td><a href="<c:url value="/GetResOrRegs?exc=${cc}&&exd=${date}&&o=${order==13? 14:13}"/>"> STATO VALUTAZIONE </a></td>
<td> MODIFICABILE </td>
</tr>
<c:forEach var="iscr" items="${regs}">
<tr><td>${iscr.matr}</td>
	<td>${iscr.name}</td>
	<td>${iscr.surname}</td>
	<td>${iscr.gc}</td>
	<td>${iscr.email}</td>
    <td>${iscr.vote}</td>
    <td>${iscr.status}</td>
    <td><c:if test="${iscr.status eq 'NON INSERITO'||iscr.status eq 'INSERITO'}"><a href="<c:url value="/Modify?exc=${cc}&&exd=${date}&&matr=${iscr.matr}&&o=0"/>"> MODIFICA </a></c:if></td>
    </tr>
    </c:forEach>
    </table>
    <form action="Publish" method="POST">
    <input type="hidden" name="exc" value="${cc}">
    <input type="hidden" name="exd" value="${date}">
    <input type="hidden" name="o" value="${order}">
    <input type="submit" value="PUBLISH">
    </form>
    <form action="Verbalize" method="POST">
    <input type="hidden" name="exc" value="${cc}">
    <input type="hidden" name="exd" value="${date}">
    <input type="hidden" name="o" value="${order}">
    <input type="submit" value="VERBALIZE">
    </form>
     <br>
    <br>
    <a href="<c:url value="/Logout"/>"> LOGOUT </a>
    </body>
</html>