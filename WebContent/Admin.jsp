<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="com.thirdyearjavamaps.classes.DB"
	import="java.util.*" import="com.thirdyearjavamaps.classes.Admin"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Admin control panel</title>
</head>
<body>
	<%
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		DB db = new DB();
		List<String> str = new ArrayList<String>();
		str.add(username);
		str.add(password);
		Admin admin = null;
		admin = db.getAdmin((ArrayList<String>) str);
		if (admin != null) {
			out.println("Login success.");
		} else {
			out.println("Login failed.");
		}
	%>
</body>
</html>