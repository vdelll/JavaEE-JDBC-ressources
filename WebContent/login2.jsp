<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://ldevapps.fr/tld/extratags" prefix="tlog" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Login screen</title>
		<link rel="stylesheet" type="text/css" href="styles.css" />
	</head>
	<body>
        <h1>Login screen</h1>
	
	    <tlog:LoginForm action="login" login="${login}" password="${password}" />
	
        <br/>
        <div class="errorMessage">${errorMessage}</div>
	
	</body>
</html>