package com.thirdyearjavamaps.servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


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
				User user = new User();
				if (user.Login(email, password)) {
					json.put("result", "success");

					HttpSession httpsession = request.getSession();
					httpsession.setAttribute("User", user);

					json.put("session", user.getSession());
				} else {
					json.put("result", "error");
					json.put("message", "Incorrect email and/or password.");
				}

			}
			else if(action.equals("SearchAddress")){
				String search = request.getParameter("search");
				ArrayList<String> str = new ArrayList<String>();
				str.add(search);
				
				List<String> SearchAddress = (List<String>)db.searchAddress(str);
				if (SearchAddress != null && SearchAddress.size() > 0) {
					JSONArray jarr = ListToJSONArray(SearchAddress);
					json.put("result", "success");
					json.put("data", jarr);
				} else {
					json.put("result", "error");
					json.put("message", "No entries found.");
				}
			}
				
			/* st */
			else if (action.equals("Registration_Buyer")) {
				String[] strRB = { "name", "address", "latitude", "longitude" };
				boolean stop = false;
				ArrayList<String> ArrayListRB = new ArrayList<String>();
				for (int i = 0; i < strRB.length; i++) {
					String param = request.getParameter(strRB[i]);
					/*
					 * if (param == null) { json.put("result", "error");
					 * json.put("message", "Some fields are empty."); stop =
					 * true; break; }
					 */

					ArrayListRB.add(param);
				}
				if (!stop) {
					// db.addLocation(ArrayListRB);
					json.put("result", "success");
				}
			}
			/* st */
			else if (action.equals("ChangeUserPassword")) {
				String[] strCUP = { "password", "email" };
				boolean stop = false;
				ArrayList<String> CUP = new ArrayList<String>();
				for (int i = 0; i < strCUP.length; i++) {
					String param = request.getParameter(strCUP[i]);
					if (param == null) {
						json.put("result", "error");
						json.put("message", "Some fields are empty.");
						stop = true;
						break;
					}
					CUP.add(param);
				}

				if (!stop) {
					db.updateUserPassword(CUP);
					json.put("result", "success");
					json.put("message",
							"You have successfully changed your password ");

				}
			}
			/* st */
			else if (action.equals("GetUserUsingSession")) {
				ArrayList<String> GUUS = new ArrayList<String>();
				User GUUSUser;
				GUUS.add(request.getParameter("session"));
				GUUSUser = db.getUserBySession(GUUS);

				json.put("fname", GUUSUser.getFname());
				json.put("lname", GUUSUser.getLname());
				json.put("email", GUUSUser.getEmail());
				json.put("password", GUUSUser.getPassword());
				json.put("phone1", GUUSUser.getPhone1());
				json.put("phone2", GUUSUser.getPhone2());
				json.put("id", (GUUSUser.getID() + ""));
			}
			/* st */
			else if (action.equals("UpdateUserDetails")) {
				String[] str = { "fname", "lname", "phone1", "phone2", "email" };
				boolean stop = false;
				ArrayList<String> UUD = new ArrayList<String>();
				for (int i = 0; i < str.length; i++) {
					String param = request.getParameter(str[i]);
					if (param == null) {
						json.put("result", "error");
						json.put("message", "Some fields are empty.");
						stop = true;
						break;
					}
					UUD.add(param);
				}

				if (!stop) {
					db.updateUserDetails(UUD);
					json.put("result", "success");
					json.put("message",
							"You have successfully changed your details ");

				}
			}
			/* st */
			else if (action.equals("Registration")) {
				String[] str = { "fname", "lname", "email", "password",
						"phone1", "phone2" };

				boolean stop = false;
				ArrayList<String> RegTest = new ArrayList<String>();
				RegTest.add(request.getParameter("email"));
				if (db.userExists(RegTest)) {
					stop = true;
					json.put("result", "error");
					json.put("message",
							"A user with this Email is already registered");
				}
				List list = new ArrayList();
				if (!stop) {
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
				}
				if (!stop) {
					db.addUser((ArrayList) list);
					json.put("result", "success");
					json.put("message", "You've been registered");
				}
			} else { //USER HAVE SESSION? if so httpsession.getAttribute("User"); gets the user object with the data.
				HttpSession httpsession = request.getSession();
				checkSession(httpsession, request.getParameter("session"));
				User user = (User) httpsession.getAttribute("User");
				if (user == null || user.sessionExpired()) {
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
						List<String> history = user.getHistoryList();
						if (history != null && history.size() > 0) {
							JSONArray jarr = ListToJSONArray(history);
							json.put("result", "success");
							json.put("data", jarr);
						} else {
							json.put("result", "error");
							json.put("message", "No entries found.");
						}
					} else if (action.equals("removeHistory")) {
						int aid = 0;
						try {
							aid = Integer.parseInt(request.getParameter("apartment_id"));
						} catch (NumberFormatException e) {
							json.put("result", "error");
							json.put("message", "apartment_id invalid.");
						}
						System.out.println(user.getID() + " " + aid);
						user.removeHistory(aid);
						json.put("result", "success");

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

	private JSONArray ListToJSONArray(List history) throws JSONException {
		JSONArray jarr = new JSONArray();
		for (Object dict : history) {
			Iterator it = ((Map) dict).entrySet().iterator();
			JSONObject jobj = new JSONObject();
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry) it.next();
				jobj.put((String) pair.getKey(), pair.getValue());
				it.remove();
			}
			jarr.put(jobj);
		}
		return jarr;
	}

}