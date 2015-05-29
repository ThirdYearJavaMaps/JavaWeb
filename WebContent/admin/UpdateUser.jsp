<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="com.thirdyearjavamaps.classes.DB"
	import="java.util.*" 
	import="com.thirdyearjavamaps.classes.User"%>
<%
DB db=new DB();
String action=request.getParameter("action");
if(action.compareTo("Edit")==0){
	ArrayList<String> str=new ArrayList<String>();
	str.add(request.getParameter("fname"));
	str.add(request.getParameter("lname"));
	str.add(request.getParameter("email"));
	str.add(request.getParameter("password"));
	str.add(request.getParameter("phone1"));
	str.add(request.getParameter("phone2"));
	str.add(request.getParameter("session"));
	str.add(request.getParameter("session_exp"));
	str.add(request.getParameter("id"));
	db.updateUser(str);
}
else {
	int user_id=Integer.parseInt(request.getParameter("id"));
	db.deleteUser(user_id);
}

response.setStatus(response.SC_MOVED_TEMPORARILY);
response.setHeader("Location", "Admin.jsp");
%>