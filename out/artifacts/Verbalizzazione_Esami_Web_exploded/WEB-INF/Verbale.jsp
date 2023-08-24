<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<title>
Verbale</title></head>
<body>
<h1>Verbale creato</h1>
<br>
<h2>
ID Verbale: ${verbal.id}<br>
Data e Ora: ${verbal.dateTime}<br><br>
DATI CORSO<br>
${idcorso} - ${verbal.registrations.get(0).course}<br>
Docente: ${user.surname} ${user.name}<br>
<br>
DATA APPELLO: ${verbal.registrations.get(0).date}<br>
</h2>
<h3>
RISULTATI<br><br>
<c:forEach var="vv" items="${verbal.registrations}">
${vv.matr}<br>
${vv.gc}<br>
${vv.surname} ${vv.name}<br>
${vv.vote}<br><br>
</c:forEach>
 <br>
    <br>
    <a href="/Verbalizzazione-Esami/Logout"> LOGOUT </a>
</h3>
</body>
</html>

