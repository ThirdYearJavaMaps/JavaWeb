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
import javax.servlet.http.HttpSession;

import java.security.*;

import org.apache.tomcat.jdbc.pool.DataSource;
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
		if (action.equals("Login")) {
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			ArrayList<String> str = new ArrayList<String>();
			str.add(email);
			str.add(password);
			json = new JSONObject();
			try {
				User user = (User) select_query("getUser", str);
				if (user != null) {
					json.put("result", "success");
					String session_str = Generate_Session(email, password);
					user.setSession(session_str);
					HttpSession session = request.getSession();
					session.setAttribute("User", user);

					json.put("session", session_str);
				} else {
					json.put("result", "error");
					json.put("message", "Incorrect email and/or password.");
				}
			} catch (Exception e) {
				System.err.println(e.getClass().getName() + ": "
						+ e.getMessage());
			}
			response.getWriter().println(json.toString());
		} else if (action.equals("Main")) {
			json = new JSONObject();
			HttpSession session = request.getSession();
			User user = (User) session.getAttribute("User");
			try {
				if (user != null) {

					/*
					 * Apartments=Algorithm();
					 * data=Get_Apartment_Data(Apartments); 
					 * json.put(data);
					 */
					json.put("result", "success");
				} else {
					json.put("result", "error");
					json.put("message", "Not logged in.");
				}
			} catch (Exception e) {
				System.err.println(e.getClass().getName() + ": "
						+ e.getMessage());
			}
			response.getWriter().println(json.toString());
		} else if (action.equals("History")) {
			json = new JSONObject();
			HttpSession session = request.getSession();
			User user = (User) session.getAttribute("User");
			try {
				if (user != null) {
					ArrayList<String> str = new ArrayList<String>();
					str.add(String.valueOf(user.getID()));
					History history=(History) select_query("getHistory",str);
					if(history!=null){
						
					}
					json.put("result", "success");
				} else {
					json.put("result", "error");
					json.put("message", "Not logged in.");
					
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

		while (hashtext.length() < 32) {
			hashtext = "0" + hashtext;
		}
		return hashtext;
	}

	public Object select_query(String q, ArrayList<String> str)
			throws Exception {
		Connection c = null;
		PreparedStatement stmt = null;
		String query = null;

		Class.forName("org.sqlite.JDBC");
		Context ctx = new InitialContext();
		DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/sqlite");
		c = ds.getConnection();
		c.setAutoCommit(false);
		System.out.println("Opened database successfully");

		if (q.equals("getUser"))
			query = "SELECT * FROM Users WHERE email=? AND password=?";
		else if(q.equals("getHistory"))
			query = "SELECT city,address,rooms,price FROM Apartments,History WHERE deleted=0 AND id=apartment_id AND user_id=?"; //add 1st picture to query
		stmt = c.prepareStatement(query);
		for (int i = 1; i < str.size() + 1; i++)
			stmt.setString(i, str.get(i - 1));
		ResultSet res = stmt.executeQuery();
		if (!res.isBeforeFirst()) {
			return null;
		}
		User user = new User();
		while (res.next()) {
			user.setID(res.getInt("id"));
			user.setFname(res.getString("fname"));
			user.setLname(res.getString("lname"));
			user.setEmail(res.getString("email"));
			user.setPassword(res.getString("password"));
			user.setPhone1(res.getString("phone1"));
			user.setPhone2(res.getString("phone2"));
			user.setSession(res.getString("session"));
		}
		res.close();
		stmt.close();
		c.close();
		System.out.println("Operation done successfully");
		return user;
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
