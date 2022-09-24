<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
     pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Java Error</title>
</head>
<body>
	<h1>Java Error</h1>
	<p>Excepción de Java</p>
	<p>Para continuar, haga click en el botón de atrás
	
	<h2>Detalles del error:</h2>
	<p>Tipo: {pageContext.exception["class"]</p>
	<p>Mensaje: {pageContext.exception.message}</p>
</body>
</html>