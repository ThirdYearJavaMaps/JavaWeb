package com.thirdyearjavamaps.servlets;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
			else if(action.equals("SearchAddress")){
				String search = request.getParameter("search");
				System.out.println(search);
				List<String> SearchAddress = (List<String>)db.searchAddress(search);
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
			//st
			else if(action.equals("ForgotPassword"))
			{
				
				ArrayList ru=new ArrayList();
				ru.add(request.getParameter("email"));
				User registeredUser=db.getUserByEmail(ru);
				if(registeredUser==null)
				{
					
					json.put("result", "error");
					json.put("message","No such user!");
				}
				else{
				registeredUser.sendEmail("forgotpassword");
				json.put("result", "success");
				json.put("message", "An email with your password was sent to "+ru.get(0));
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
					ArrayList ru=new ArrayList();
					ru.add(request.getParameter("email"));
					User registeredUser=db.getUserByEmail(ru);
					registeredUser.sendEmail("confirmation");
					json.put("result", "success");
					json.put("message", "You've been registered, an email was sent to "+ru.get(0));
				}
			} 
			/*Ariel search*/
			
			else if (action.equals("Search")) {
				String[] str = { "city", "rooms", "price1", "price2",
						"rooms1", "rooms2" };

				boolean stop = false;
				ArrayList<String> search = new ArrayList<String>();
				search.add(request.getParameter("city"));
				if (!db.cityExists(search)) {
					stop = true;
					json.put("result", "error");
					json.put("message",
							"There are no apartments for sale from this city");
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
					ArrayList<Apartment> apartments = new ArrayList<Apartment>();
					apartments = db.getSearchedApartments((ArrayList) list);
					
				}
				
			}
			
			else { //USER HAVE SESSION? if so httpsession.getAttribute("User"); gets the user object with the data.
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
					} 
					else if (action.equals("addHistory")){
						int apartment_id=Integer.parseInt(request.getParameter("apartment_id"));
						user.addHistory(apartment_id);
						json.put("result", "success");
					}
					else if (action.equals("removeHistory")) {
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
					}
					else {
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
		JSONObject json_out = new JSONObject();
		try {
			DB db = new DB();
			JSONObject json_in = new JSONObject();
			String base64encoded;
			base64encoded = json_in.getString(request.getParameter("file"));
			Base64 base64=new Base64();
			byte[] image=base64.decode(base64encoded);
			
			InputStream in = new ByteArrayInputStream(image);
			BufferedImage bImageFromConvert = ImageIO.read(in);
			
			ImageWriter writer = (ImageWriter) ImageIO.getImageWritersByFormatName("jpeg").next();

			ImageWriteParam param = writer.getDefaultWriteParam();
			param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			param.setCompressionQuality(0.2f);
			
			String root=null;
			String filename=User.md5(String.valueOf(User.epochNow()))+".jpg";
			String filepath=null;
			File file = new File(filepath);
			ArrayList<String> str=new ArrayList<String>();
			str.add(request.getParameter("apartment_id"));
			str.add(filename);
			db.addImagetoApartment(str);
			writer.setOutput(ImageIO.createImageOutputStream(file));
			writer.write(null, new IIOImage(bImageFromConvert, null, null), param);
			writer.dispose();
			json_out.append("result", "success");
			
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (SQLException e){
			try {
				json_out.append("result", "error");
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}
		response.getWriter().println(json_out.toString());
	}
	
	public BufferedImage scale(BufferedImage img, int targetWidth, int targetHeight) {

	    int type = (img.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
	    BufferedImage ret = img;
	    BufferedImage scratchImage = null;
	    Graphics2D g2 = null;

	    int w = img.getWidth();
	    int h = img.getHeight();

	    int prevW = w;
	    int prevH = h;

	    do {
	        if (w > targetWidth) {
	            w /= 2;
	            w = (w < targetWidth) ? targetWidth : w;
	        }

	        if (h > targetHeight) {
	            h /= 2;
	            h = (h < targetHeight) ? targetHeight : h;
	        }

	        if (scratchImage == null) {
	            scratchImage = new BufferedImage(w, h, type);
	            g2 = scratchImage.createGraphics();
	        }

	        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
	                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	        g2.drawImage(ret, 0, 0, w, h, 0, 0, prevW, prevH, null);

	        prevW = w;
	        prevH = h;
	        ret = scratchImage;
	    } while (w != targetWidth || h != targetHeight);

	    if (g2 != null) {
	        g2.dispose();
	    }

	    if (targetWidth != ret.getWidth() || targetHeight != ret.getHeight()) {
	        scratchImage = new BufferedImage(targetWidth, targetHeight, type);
	        g2 = scratchImage.createGraphics();
	        g2.drawImage(ret, 0, 0, null);
	        g2.dispose();
	        ret = scratchImage;
	    }

	    return ret;

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
