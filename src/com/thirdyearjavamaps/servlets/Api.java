package com.thirdyearjavamaps.servlets;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.thirdyearjavamaps.classes.*;

/**
 * Servlet implementation class Api
 */
@WebServlet("/api")
@MultipartConfig
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

			else if (action.equals("AddApt")) {
				// String[] strApt = { "city","price","territory","street",
				// "house_num","apt_num","rooms","floor",
				// "sizem2","desc","aircondition","elevator", "balcony",
				// "isolated_room","parking", "handicap_access",
				// "storage","bars","sun_balcony",
				// "renovated","furnished","unit","pandoor"};

				Map<String, String> dict = new HashMap<>();
				dict.put("user_id", "");
				dict.put("type_id", "");
				dict.put("territory", "");
				dict.put("city", "");
				dict.put("address", "");
				dict.put("rooms", "");
				dict.put("floor", "");
				dict.put("latitude", "");
				dict.put("longitude", "");
				dict.put("sizem2", "");
				dict.put("comment", "");
				dict.put("price", "");
				dict.put("add_date", "");
				dict.put("aircondition", "");
				dict.put("elevator", "");
				dict.put("balcony", "");
				dict.put("isolated_room", "");
				dict.put("parking", "");
				dict.put("handicap_access", "");
				dict.put("storage", "");
				dict.put("bars", "");
				dict.put("sun_balcony", "");
				dict.put("renovated", "");
				dict.put("furnished", "");
				dict.put("unit", "");
				dict.put("pandoor", "");

				for (String item : dict.keySet()) {
					String param = request.getParameter(item);
					dict.put(item, param);
				}

				//
				// String[] strApt = { "user_id", "type_id", "territory",
				// "city",
				// "address", "rooms", "floor", "latitude", "longitude",
				// "sizem2", "comment", "price", "aircondition",
				// "elevator", "balcony", "isolated_room", "parking",
				// "handicap_access", "storage", "bars", "sun_balcony",
				// "renovated", "furnished", "unit", "pandoor" };

				boolean stop = false;
				// ArrayList<String> AddApt = new ArrayList<String>();
				for (String item : dict.keySet()) {
					// String param = request.getParameter(strApt[i]);
					if (item == null) {
						json.put("result", "error");
						json.put("message", "Some fields are empty.");
						stop = true;
						break;
					}
					// AddApt.add(param);
				}

				if (!stop) {
					db.addApt(dict);
					json.put("result", "success");
					json.put("message",
							"You have registered an apartment. ");

				}
			}
			else if (action.equals("SearchAddress")) {
				json = UtilityTools.SearchApartment(request, json, db);
			} else if (action.equals("getApartment")) {
				int apartment_id = Integer.parseInt(request
						.getParameter("apartment_id"));
				HashMap dict = db.getApartmentByID(apartment_id);
				JSONObject jobj = new JSONObject();
				if (dict != null) {
					Iterator it = ((Map) dict).entrySet().iterator();
					jobj = new JSONObject();
					while (it.hasNext()) {
						Map.Entry pair = (Map.Entry) it.next();
						jobj.put((String) pair.getKey(), pair.getValue());
						it.remove();
					}
					json.put("result", "success");
					json.put("data", jobj);
				} else {
					json.put("result", "error");
				}
			} else if (action.equals("getUserInfo")) {
				int user_id = Integer.parseInt(request.getParameter("user_id"));
				HashMap dict = db.getUserInfo(user_id);
				JSONObject jobj = new JSONObject();
				if (dict != null) {
					Iterator it = ((Map) dict).entrySet().iterator();
					jobj = new JSONObject();
					while (it.hasNext()) {
						Map.Entry pair = (Map.Entry) it.next();
						jobj.put((String) pair.getKey(), pair.getValue());
						it.remove();
					}
					json.put("result", "success");
					json.put("data", jobj);
				} else {
					json.put("result", "error");
				}
			}

			/* st */
			else if (action.equals("Registration_Location")) {

				String[] strRB = { "userid", "lati", "lngi", "addresstosend",
						"name" };

				boolean stop = false;
				ArrayList<String> ArrayListRB = new ArrayList<String>();
				for (int i = 0; i < strRB.length; i++) {

					String param = request.getParameter(strRB[i]);
					if (param == null) {
						json.put("result", "error");
						json.put("message", "Some fields are empty.");
						stop = true;
						break;
					}
					ArrayListRB.add(param);
				}
				if (!stop) {

					db.addUsersLocations(ArrayListRB);

					json.put("result", "Location was written");
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
			// st
			else if (action.equals("ForgotPassword")) {

				ArrayList ru = new ArrayList();
				ru.add(request.getParameter("email"));
				User registeredUser = db.getUserByEmail(ru);
				if (registeredUser == null) {

					json.put("result", "error");
					json.put("message", "No such user!");
				} else {
					registeredUser.sendEmail("forgotpassword");
					json.put("result", "success");
					json.put(
							"message",
							"An email with your password was sent to "
									+ ru.get(0));
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
					ArrayList ru = new ArrayList();
					ru.add(request.getParameter("email"));
					User registeredUser = db.getUserByEmail(ru);
					registeredUser.sendEmail("confirmation");
					json.put("result", "success");
					json.put("message",
							"You've been registered, an email was sent to "
									+ ru.get(0));
				}
			}
			/*Ariel search*/
			
			else if (action.equals("Search")) {
				String[] str = { "city", "rooms", "price1", "price2",
						"floor1", "floor2" };

				boolean stop = false;
				ArrayList<String> search = new ArrayList<String>();
				search.add(request.getParameter("city"));
//				if (!db.cityExists(search)) {
//					stop = true;
//					json.put("result", "error");
//					json.put("message",
//							"There are no apartments for sale from this city");
//				}
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
					//ArrayList<Apartment> apartments = new ArrayList<Apartment>();
					//apartments = db.getSearchedApartments((ArrayList) list);
					
					List<String> result = (List<String>)db.getSearchedApartments((ArrayList) list);
					if (result != null && result.size() > 0) {
						JSONArray jarr = ListToJSONArray(result);
						json.put("result", "success");
						json.put("data", jarr);
					}
					else {
						json.put("result", "error");
						json.put("message", "No entries found.");
					}
					
				}
				
			}
			else { // USER HAVE SESSION? if so
						// httpsession.getAttribute("User");
						// gets the user object with the data.
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
					} else if (action.equals("Logout")) {
							db.deleteSession(user.getID());
							user.setSession("");
							json.put("result", "success");

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
					} else if (action.equals("HistoryLiked")) {
						List<String> history = user.getHistoryListLiked();
						if (history != null && history.size() > 0) {
							JSONArray jarr = ListToJSONArray(history);
							json.put("result", "success");
							json.put("data", jarr);
						} else {
							json.put("result", "error");
							json.put("message", "No entries found.");
						}
					} else if (action.equals("addHistory")) {
						int apartment_id = Integer.parseInt(request
								.getParameter("apartment_id"));
						user.addHistory(apartment_id);
						json.put("result", "success");
					} else if (action.equals("removeHistory")) {
						int aid = 0;
						try {
							aid = Integer.parseInt(request
									.getParameter("apartment_id"));
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
					json.put("message", "Duplicate entry.");
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
		/*
		JSONObject json = new JSONObject();
		JSONObject json_in = new JSONObject();
		DB db = new DB();
		try {
			
			StringBuffer jb = new StringBuffer();
			String line = null;
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null)
			   jb.append(line);
			
			json_in = org.json.HTTP.toJSONObject(jb.toString());
			
			JSONObject js=new JSONObject((String)json_in.get("Method"));
			
			String base64encoded;
			Map<String, String> dict = new HashMap<>();
			Iterator it=js.keys();
	        while(it.hasNext()){
	             String key=(String)it.next();
	             dict.put(key,(String) js.get(key));
	        }
	        
	        System.out.println(dict);
	        base64encoded = dict.get("image");
	        
			Base64 base64 = new Base64();
			byte[] image = base64.decode(base64encoded);
			
	
			String root = "/home/guy/git/JavaWeb14/";
			String filename = User.md5(String.valueOf(User.epochNow())) + ".jpg";
			String filepath = root + "images/" + filename;
			System.out.println(filepath);
			File file = new File(filepath);
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(image);
			fos.flush();
			fos.close();
			
	        boolean stop = false;

			for (String item : dict.keySet()) {
				if (item == null) {
					json.put("result", "error");
					json.put("message", "Some fields are empty.");
					stop = true;
					break;
				}

			}

			if (!stop) {
					db.addApt(dict,filename);
					json.put("result", "success");
					json.put("message","You have successfully added an apartment.");

			}

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			try {
				json.put("result", "error");
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}
		response.getWriter().println(json.toString());
		*/
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
