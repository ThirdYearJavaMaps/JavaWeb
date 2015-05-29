<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="com.thirdyearjavamaps.classes.DB"
	import="java.util.*"
	%>
<%
int apartment_id=Integer.parseInt(request.getParameterNames().nextElement());
DB db=new DB();
db.deleteApartment(apartment_id);
response.setStatus(response.SC_MOVED_TEMPORARILY);
response.setHeader("Location", "Apartments.jsp"); 

%>