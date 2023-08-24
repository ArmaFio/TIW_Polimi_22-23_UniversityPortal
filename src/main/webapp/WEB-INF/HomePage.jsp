<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title><c:out value="${user.getStatus()}"/>
</title></head>
<body>
<h1>Benvenuto <c:out value="${user.getName()}"/></h1>
<h2>Scegli un corso<br>
<c:choose>
<c:when test= "${Courses.size()>0}" >
<table border="1">
<tr><td>Codice Corso</td><td>Nome Corso</td></tr>
<c:forEach var="course" items="${Courses}">
<tr>
<c:url value="/GetExams" var="exURL" ><c:param name="corso" value="${course.courseId}"/>
<c:param name="ncorso" value="${course.courseName}"/></c:url>
<td><a href="${exURL}"> ${course.courseId}</a></td>
<td><a href="${exURL}"> ${course.courseName}</a></td>
</tr>
</c:forEach>
</table>
</c:when>
<c:otherwise> Nessun corso disponibile </c:otherwise>
</c:choose>
</h2>
<h3>
<c:if test="${Exams!=null}"><c:out value="${corso}"/>
<c:out value="${ncorso}"/>
<c:choose><c:when test="${Exams.size()>0}">
SELEZIONA UN APPELLO
<table border="1"><tr><td>DATA</td></tr>
<c:forEach var="ex" items="${Exams}">
<tr><c:url value="/GetResOrRegs" var="rrURL">
<c:param name="exc" value="${ex.courseCode}"/> <c:param name="exd" value="${ex.date}"/> <c:param name="o" value="1" /></c:url>
<td><a href="${rrURL}"> <c:out value="${ex.date}"/> </a></td></tr>
</c:forEach></table></c:when>
<c:otherwise> Nessun Appello disponibile per questo corso</c:otherwise>
</c:choose>
</c:if>
</h3>
 <br>
    <br>
    <a href="<c:url value="/Logout"/>"> LOGOUT </a>
</body>
</html> 