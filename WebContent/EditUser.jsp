<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="com.thirdyearjavamaps.classes.DB"
	import="java.util.*" import="com.thirdyearjavamaps.classes.Admin"
	import="com.thirdyearjavamaps.classes.User"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="../JavaWeb/css/bootstrap.min.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Admin control panel</title>
</head>
<body>
<div class="page-header">
<h1 style="text-align: center;">Edit User</h1>
</div>
<form class="form-horizontal" action="${pageContext.request.contextPath}/UpdateUser.jsp" method="post" style=" width: 20%; margin-left:auto; margin-right:auto;">
<%
DB db=new DB();
String user_id=request.getParameterNames().nextElement();
ArrayList<String> str=new ArrayList<String>();
str.add(user_id);
User user=db.getUserByID(str);
%>
  <fieldset>
	<div class="form-group">
	<label for="id">ID</label>
	<input id="id" class="form-control" type="text" name="id" value="<%=user.getID() %>" readonly>
</div>
<div class="form-group">
	<label for="fname">First name</label>
	<input id="fname" class="form-control" type="text" name="fname" value="<%=user.getFname() %>">
</div>
<div class="form-group">
	<label for="lname">Last name</label>
	<input id="lname" class="form-control" type="text" name="lname" value="<%=user.getLname() %>">
</div>
<div class="form-group">
	<label for="email">Email</label>
	<input id="email" class="form-control" type="text" name="email" value="<%=user.getEmail() %>"></div>
<div class="form-group">
	<label for="password">Password</label>
	<input id="password" class="form-control" type="text" name="password" value="<%=user.getPassword() %>"></div>
<div class="form-group">
	<label for="phone1">Phone1</label>
	<input id="phone1" class="form-control" type="text" name="phone1" value="<%=user.getPhone1() %>"></div>
<div class="form-group">
	<label for="phone2">Phone2</label>
	<input id="phone2" class="form-control" type="text" name="phone2" value="<%=user.getPhone2() %>"></div>
<div class="form-group">
	<label for="session">Session</label>
	<input id="session" class="form-control" type="text" name="session" value="<%=user.getSession() %>"></div>
<div class="form-group">
	<label for="session_exp">Session Expiration</label>
	<input id="session_exp" class="form-control" type="text" name="session_exp" value="<%=user.getSessionExp() %>"></div>

<br>
<div id="buttons" style="width:200px; margin-left: auto; margin-right: auto;">
<input class="btn btn-primary" type="submit" value="Edit">
<input class="btn btn-danger" type="submit" value="Delete">
</div>
</form>
</body>
</html>