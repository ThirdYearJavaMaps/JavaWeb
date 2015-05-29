<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="com.thirdyearjavamaps.classes.DB"
	import="java.util.*" import="com.thirdyearjavamaps.classes.Admin"
	import="com.thirdyearjavamaps.classes.User"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Admin control panel</title>
</head>
<body>
<nav class="navbar navbar-default">
  <div class="container-fluid">
    <div class="navbar-header">
      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      <a class="navbar-brand" href="#">Weepi</a>
    </div>

    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
      <ul class="nav navbar-nav">
        <li><a href="${pageContext.request.contextPath}/admin/Admin.jsp">Users<span class="sr-only">(current)</span></a></li>
        <li><a href="${pageContext.request.contextPath}/admin/Apartments.jsp">Apartments<span class="sr-only">(current)</span></a></li>
      </ul>
    </div>
  </div>
</nav>
<div class="page-header">
<h1 style="text-align: center;">Users control panel</h1><br>
</div>
<form action="${pageContext.request.contextPath}/admin/EditUser.jsp" method="post" style="width:80%;  margin-left:auto; margin-right:auto;">
<table class="table table-striped table-hover">
<thead>
<th>Edit</th>
<th>ID</th>
<th>First name</th>
<th>Last name</th>
<th>Email</th>
<th>Password</th>
<th>Phone1</th>
<th>Phone2</th>
<th>Session</th>
<th>Session Expiration</th>
</thead>
<%
DB db=new DB();
ArrayList<User> users=db.getUsers();
for(User user:users){
%>
<tr>
	<td><input class="btn btn-info" type="submit" name="<%=user.getID()%>" value="Edit" /></td>
	<td><%=user.getID()%></td>
	<td><%=user.getFname() %></td>
	<td><%=user.getLname()%></td>
	<td><%=user.getEmail()%></td>
	<td><%=user.getPassword()%></td>
	<td><%=user.getPhone1()%></td>
	<td><%=user.getPhone2()%></td>
	<td><%=user.getSession()%></td>
	<td><%=user.getSessionExp()%></td>
</tr>
<% } %>
</table>
</form>
</body>
</html>