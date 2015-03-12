package org.thirdyearjava.api;

import java.io.IOException;
import java.math.BigInteger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.security.*;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.*;
import java.util.ArrayList;

/**
 * Servlet implementation class JavaWeb
 */
@WebServlet(description = "API", urlPatterns = { "/api" })
public class JavaWeb extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		JSONObject json;
		if(action.compareTo("Login")==0){
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			ArrayList<String> str = new ArrayList<String>();
			str.add(email);
			str.add(password);
			json = new JSONObject();
			try {
				if (query(
						"SELECT email,password FROM Users WHERE email=? AND password=?",
						str) == 0) {
					json.put("result", "success");
					String session = Generate_Session(email, password);
					json.put("session", session);
				} else {
					json.put("result", "error");
					json.put("message", "Incorrect email and/or password.");
				}
			} catch (Exception e) {
				System.err.println(e.getClass().getName() + ": "
						+ e.getMessage());
			}
			response.getWriter().println(json.toString());
		}
			else if(action.compareTo("Main")==0){
			json = new JSONObject();
			ArrayList<String> session = new ArrayList<String>();
			session.add(request.getParameter("session"));
			try {
				if (query("SELECT session FROM Users WHERE session=?", session) == 0) {
					/*
					 * Apartments=Algorithm();
					 * data=Get_Apartment_Data(Apartments); json.put(data);
					 */
					json.put("result", "success");
				} else {
					json.put("result", "error");
				}
			} catch (Exception e) {
				System.err.println(e.getClass().getName() + ": "
						+ e.getMessage());
			}
			response.getWriter().println(json.toString());
		}
	}

	public String Generate_Session(String email, String password) {
		long epoch = System.currentTimeMillis() / 1000;
		String plaintext = email + password + epoch;
		System.out.println(plaintext);
		MessageDigest m = null;
		try {
			m = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		m.reset();
		m.update(plaintext.getBytes());
		byte[] digest = m.digest();
		BigInteger bigInt = new BigInteger(1, digest);
		String hashtext = bigInt.toString(16);
		// Now we need to zero pad it if you actually want the full 32 chars.
		while (hashtext.length() < 32) {
			hashtext = "0" + hashtext;
		}
		return hashtext;
	}

	public int query(String query, ArrayList<String> str) throws Exception {
		Connection c = null;
		PreparedStatement stmt = null;

		Class.forName("org.sqlite.JDBC");
		Context ctx = new InitialContext();
		DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/sqlite");
		c = ds.getConnection();
		c.setAutoCommit(false);
		System.out.println("Opened database successfully");

		stmt = c.prepareStatement(query);
		for (int i = 1; i < str.size() + 1; i++)
			stmt.setString(i, str.get(i - 1));
		ResultSet res = stmt.executeQuery();
		if (!res.isBeforeFirst()) {
			return 1;
		}
		res.close();
		stmt.close();
		c.close();
		System.out.println("Operation done successfully");
		return 0;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
