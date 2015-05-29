<%@ page language="java" contentType="text/html; charset=windows-1255"
	pageEncoding="windows-1255"%>
<%
response.setStatus(response.SC_MOVED_TEMPORARILY);
response.setHeader("Location", "Admin.jsp"); 
%>