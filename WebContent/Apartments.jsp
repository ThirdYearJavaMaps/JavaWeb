<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="com.thirdyearjavamaps.classes.DB"
	import="java.util.*" import="com.thirdyearjavamaps.classes.Admin"
	import="com.thirdyearjavamaps.classes.Apartment"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="../JavaWeb/css/bootstrap.min.css">
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
        <li><a href="${pageContext.request.contextPath}/Admin.jsp">Users<span class="sr-only">(current)</span></a></li>
        <li><a href="${pageContext.request.contextPath}/Apartments.jsp">Apartments<span class="sr-only">(current)</span></a></li>
      </ul>
    </div>
  </div>
</nav>
<div class="page-header">
<h1 style="text-align: center;">Apartments control panel</h1><br>
</div>
<form action="${pageContext.request.contextPath}/DeleteApartment.jsp" method="post"  style="width:80%;  margin-left:auto; margin-right:auto;">
<table class="table table-striped table-hover">
<thead>
<th>Delete</th>
<th>ID</th>
<th>Address</th>
<th>City</th>
<th>Rooms</th>
<th>Size (M^2)</th>
<th>Price</th>

</thead>
<%
DB db=new DB();
ArrayList<Apartment> apartments=db.getApartments();
for(Apartment apartment:apartments){
%>
<tr>
	<td><input class="btn btn-danger" type="submit" name="<%=apartment.getId()%>" value="Delete" /></td>
	<td><%=apartment.getId() %></td>
	<td><%=apartment.getAddress() %></td>
	<td><%=apartment.getCity() %></td>
	<td><%=apartment.getAddress() %></td>
	<td><%=apartment.getRooms() %></td>
	<td><%=apartment.getSizem2() %></td>
	<td><%=apartment.getPrice() %></td>
</tr>
<% } %>
</table>
</form>
</body>
</html>