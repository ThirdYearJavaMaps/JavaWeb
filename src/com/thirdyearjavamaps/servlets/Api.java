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
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import javax.sql.DataSource;

import org.json.JSONArray;
import org.json.JSONException;
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
		JSONObject json = new JSONObject();
		DB db = new DB();
		try {
			if (action.equals("Login")) {
				String email = request.getParameter("email");
				String password = request.getParameter("password");
				ArrayList<String> str = new ArrayList<String>();
				str.add(email);
				str.add(password);
				User user = db.getUser(str);
				if (user != null) {
					json.put("result", "success");
					String session_str = Generate_Session(email, password);
					user.setSession(session_str);

					// bad coding - to fix
					ArrayList<String> str1 = new ArrayList<String>();
					str1.add(session_str);
					str1.add(String.format("%d", epochNow() + 604800)); // one
																		// week
																		// session
					str1.add(email);
					db.updateSession(str1);

					HttpSession httpsession = request.getSession();
					httpsession.setAttribute("User", user);

					json.put("session", session_str);
				} else {
					json.put("result", "error");
					json.put("message", "Incorrect email and/or password.");
				}
			} else if (action.equals("Registration")) {
				String[] str = { "fname", "lname", "email", "password",
						"phone1", "phone2" };
				List list = new ArrayList();
				boolean stop = false;
				for (int i = 0; i < str.length; i++) {
					String param = request.getParameter(str[i]);
					if (param == null) {
						json.put("result", "error");
						json.put("message", "Some fields are empty.");
						stop = true;
						break;
					}
					list.add(param);
				}
				if (!stop) {
					db.addUser((ArrayList)list);
					json.put("result", "success");
				}
			} else {
				HttpSession httpsession = request.getSession();
				checkSession(httpsession, request.getParameter("session"));
				User user = (User) httpsession.getAttribute("User");
				if (user == null || sessionExpired(user)) {
					json.put("result", "error");
					json.put("message", "Not logged in.");
				} else {
					if (action.equals("Main")) {
						json.put("result", "success");
						/*
						 * Apartments=Algorithm();
						 * data=Get_Apartment_Data(Apartments); json.put(data);
						 */
					} else if (action.equals("History")) {
						json.put("result", "success");
						ArrayList<String> str = new ArrayList<String>();
						str.add(String.valueOf(user.getID()));
						List history = (List) db.getHistory(str);
						if (history != null) {
							JSONArray jarr = new JSONArray();
							for (Object dict : history) {
								Iterator it = ((Map) dict).entrySet()
										.iterator();
								JSONObject jobj = new JSONObject();
								while (it.hasNext()) {
									Map.Entry pair = (Map.Entry) it.next();
									jobj.put((String) pair.getKey(),
											pair.getValue());
									it.remove();
								}
								jarr.put(jobj);
							}
							json.put("data", jarr);
						}
					} else {
						throw new Exception();
					}
				}

			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			if (e.getErrorCode() == 19) {
				try {
					json.put("result", "error");
					json.put("message", "Email exists.");
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			e.printStackTrace();
		} catch (Exception e) {
			try {
				json = new JSONObject();
				json.put("result", "error");
				json.put("message", "Unknown error");
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try {
				db.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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

	private long epochNow() {
		return System.currentTimeMillis() / 1000;
	}

	private void checkSession(HttpSession httpsession, String session_str) {
		DB db = new DB();
		if (httpsession.getAttribute("User") == null) {
			ArrayList<String> str = new ArrayList<String>();
			str.add(session_str);
			User user = null;
			try {
				user = db.getUserBySession(str);
				if (user != null)
					httpsession.setAttribute("User", user);
				else
					System.out.println("session not found");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private boolean sessionExpired(Object o) {
		User user = (User) o;
		String SessionExp = user.getSessionExp();
		if (!SessionExp.isEmpty())
			return epochNow() - Long.parseLong(SessionExp) >= 0;
		return false;
	}

	private String Generate_Session(String email, String password) {
		long epoch = epochNow();
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

}