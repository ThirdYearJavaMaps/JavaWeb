<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Admin login page</title>
</head>
<body>
<div style="width:50%;top:30%;left:25%;position:absolute;">
<div class="well bs-component">
<form action="checkLogin.jsp" method="post" class="form-horizontal">
  <fieldset>
    <legend>Admin login page</legend>
    <div class="form-group" style="width:60%; margin-left: auto; margin-right: auto;">
    <label for="username">Username</label>

<input class="form-control" id="username" type="text" name="username"/><br>

<label for="password">Password</label>
<input class="form-control" type="password" name="password"/><br>
<input class="btn btn-success" type="submit" value="Login" style="margin-left: auto; margin-right: auto;"/>
</div></div>
</fieldset>
</form>
</div>
</body>
</html>