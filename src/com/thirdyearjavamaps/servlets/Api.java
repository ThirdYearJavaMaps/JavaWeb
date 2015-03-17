package com.thirdyearjavamaps.servlets;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import javax.sql.DataSource;

import org.json.JSONArray;
import org.json.JSONObject;

import com.thirdyearjavamaps.classes.*;

/**
 * Servlet implementation class Api
 */
@WebServlet("/api")
public class Api extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Api() {
		super();

	}

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
					 * data=Get_Apartment_Data(Apartments); json.put(data);
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
					List history = (List) select_query("getHistory", str);
					if (history != null) {
						JSONArray jarr=new JSONArray();
						for(Object dict: history){
						    Iterator it = ((Map)dict).entrySet().iterator();
						    JSONObject jobj=new JSONObject();
						    while (it.hasNext()) {
						        Map.Entry pair = (Map.Entry)it.next();
						        jobj.put((String) pair.getKey(),pair.getValue());
						        it.remove(); // avoids a ConcurrentModificationException
						    }
						    jarr.put(jobj);
						}
						json.put("data",jarr);
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

	}

	public String Generate_Session(String email, String password) {
		long epoch = System.currentTimeMillis() / 1000;
		String plaintext = email + password + epoch;
		System.out.println(plaintext);
		MessageDigest m = null;
		try {
			m = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
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
		else if (q.equals("getHistory"))
			query = "SELECT city,address,rooms,price,filename FROM Apartments,History,(select apartment_id,filename from Apartment_Picture GROUP BY apartment_id order by filename) pic WHERE deleted=0 AND id=History.apartment_id AND id=pic.apartment_id AND Apartments.user_id=?";

		stmt = c.prepareStatement(query);
		for (int i = 1; i < str.size() + 1; i++)
			stmt.setString(i, str.get(i - 1));
		ResultSet res = stmt.executeQuery();
		if (!res.isBeforeFirst()) {
			return null;
		}
		Object o = null;
		if (q.equals("getUser")) {
			o=new User();
			while (res.next()) {
				((User)o).setID(res.getInt("id"));
				((User)o).setFname(res.getString("fname"));
				((User)o).setLname(res.getString("lname"));
				((User)o).setEmail(res.getString("email"));
				((User)o).setPassword(res.getString("password"));
				((User)o).setPhone1(res.getString("phone1"));
				((User)o).setPhone2(res.getString("phone2"));
				((User)o).setSession(res.getString("session"));
			}
		} else if (q.equals("getHistory")) {
			o=new ArrayList();
			o=resultSetToArrayList(res);
		}
		res.close();
		stmt.close();
		c.close();
		System.out.println("Operation done successfully");
		return (Object)o;
	}


public List resultSetToArrayList(ResultSet rs) throws SQLException{
	  ResultSetMetaData md = rs.getMetaData();
	  int columns = md.getColumnCount();
	  ArrayList list = new ArrayList();
	  while (rs.next()){
	     HashMap row = new HashMap(columns);
	     for(int i=1; i<=columns; ++i){           
	      row.put(md.getColumnName(i),rs.getObject(i));
	     }
	      list.add(row);
	  }

	 return list;
}

}