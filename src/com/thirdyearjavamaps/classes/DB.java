

package com.thirdyearjavamaps.classes;

import java.beans.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DB {
	Connection c = null;
	PreparedStatement stmt = null;
	String query = null;
	Context ctx;
	DataSource ds;
	ResultSet res = null;

	public DB() {
		try {
			Class.forName("org.sqlite.JDBC");
			ctx = new InitialContext();
			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/sqlite");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	public Connection open() throws SQLException {
		return ds.getConnection();
	}

	public void close() throws SQLException {
		if (res != null)
			res.close();
		if (stmt != null)
			stmt.close();
		if (c != null)
			c.close();
		System.out.println("Operation done successfully");
	}

	private ResultSet query(String query, ArrayList<String> str)
			throws SQLException {
		c = open();
		c.setAutoCommit(false);
		System.out.println("Opened database successfully");
		stmt = c.prepareStatement(query);
		for (int i = 1; i < str.size() + 1; i++)
			stmt.setString(i, str.get(i - 1));

		res = stmt.executeQuery();
		return res;
	}

	private ResultSet query1(String query) throws SQLException {
		c = open();
		c.setAutoCommit(false);
		System.out.println("Opened database successfully");
		stmt = c.prepareStatement(query);
		res = stmt.executeQuery();
		return res;
	}

	private void uquery(String query, ArrayList<String> str)
			throws SQLException {
		c = open();
		c.setAutoCommit(true);
		stmt = c.prepareStatement(query);
		for (int i = 1; i < str.size() + 1; i++)
			stmt.setString(i, str.get(i - 1));
		stmt.executeUpdate();
	}

	private void uiquery(String query, int[] ints) throws SQLException {
		c = open();
		c.setAutoCommit(true);
		stmt = c.prepareStatement(query);
		for (int i = 1; i < ints.length + 1; i++)
			stmt.setInt(i, ints[i - 1]);
		stmt.executeUpdate();
	}

	/* st */
	public boolean userExists(ArrayList<String> str) throws SQLException {
		res = query("SELECT * FROM Users WHERE email=?", str);
		if (!res.isBeforeFirst()) {
			close();
			return false;
		}
		close();
		return true;
	}

	/* ar city exists? */
	public boolean cityExists(ArrayList<String> str) throws SQLException {
		res = query("SELECT * FROM Apartments WHERE city=?", str);
		if (!res.isBeforeFirst()) {
			close();
			return false;
		}
		close();
		return true;
	}

	/* st and ar */
	public User getUserByEmail(ArrayList<String> str) throws SQLException {
		res = query("SELECT * FROM Users WHERE email=?", str);
		if (res.isBeforeFirst()) {
			User o = new User();
			while (res.next()) {
				(o).setID(res.getInt("id"));
				(o).setFname(res.getString("fname"));
				(o).setLname(res.getString("lname"));
				(o).setEmail(res.getString("email"));
				(o).setPassword(res.getString("password"));
				(o).setPhone1(res.getString("phone1"));
				(o).setPhone2(res.getString("phone2"));
				(o).setSession(res.getString("session"));
				(o).setSessionExp(res.getString("session_exp"));
			}
			close();
			return o;
		}
		close();
		return null;
	}

	public boolean getUser(Object o, ArrayList<String> str) throws SQLException {
		res = query("SELECT * FROM Users WHERE email=? AND password=?", str);
		if (!res.isBeforeFirst()) {
			close();
			return false;
		}
		while (res.next()) {
			((User) o).setID(res.getInt("id"));
			((User) o).setFname(res.getString("fname"));
			((User) o).setLname(res.getString("lname"));
			((User) o).setEmail(res.getString("email"));
			((User) o).setPassword(res.getString("password"));
			((User) o).setPhone1(res.getString("phone1"));
			((User) o).setPhone2(res.getString("phone2"));
			((User) o).setSession(res.getString("session"));
			((User) o).setSessionExp(res.getString("session_exp"));
		}
		close();
		return true;
	}

	public User getUserBySession(ArrayList<String> str) throws SQLException {
		res = query("SELECT * FROM Users WHERE session=?", str);
		if (!res.isBeforeFirst()) {
			close();
			return null;
		}
		Object o = new User();
		while (res.next()) {
			((User) o).setID(res.getInt("id"));
			((User) o).setFname(res.getString("fname"));
			((User) o).setLname(res.getString("lname"));
			((User) o).setEmail(res.getString("email"));
			((User) o).setPassword(res.getString("password"));
			((User) o).setPhone1(res.getString("phone1"));
			((User) o).setPhone2(res.getString("phone2"));
			((User) o).setSession(res.getString("session"));
			((User) o).setSessionExp(res.getString("session_exp"));
		}
		close();
		return (User) o;
	}

	public ArrayList<User> getUsers() throws SQLException {
		res = query1("SELECT * FROM Users");
		ArrayList<User> users = new ArrayList<User>();
		for (int i = 0; res.next(); i++) {
			User user = new User(res.getInt("id"), res.getString("fname"),
					res.getString("lname"), res.getString("email"),
					res.getString("password"), res.getString("phone1"),
					res.getString("phone2"), res.getString("session"),
					res.getString("session_exp"));
			users.add(user);
		}
		close();
		return users;
	}

	public ArrayList<Apartment> getApartments() throws SQLException {
		res = query1("SELECT id,city,address,rooms,sizem2,price FROM Apartments");
		ArrayList<Apartment> apartments = new ArrayList<Apartment>();
		for (int i = 0; res.next(); i++) {
			Apartment apartment = new Apartment(res.getInt("id"),
					res.getString("city"), res.getString("address"),
					res.getFloat("rooms"), res.getFloat("sizem2"),
					res.getInt("price"));
			apartments.add(apartment);
		}
		close();
		return apartments;
	}

/*Ariel, function for search*/
	
	public List getSearchedApartments(ArrayList<String> str) throws SQLException {
		System.out.println(str.toString());
		res = query("SELECT * FROM Apartments"
				+ " WHERE lower(city)=lower(?)"
				+ " AND (rooms=?"
				+ " OR (price>=? AND price<=?)"
				+ " OR (floor>=? AND floor<=?))",str);
		if (!res.isBeforeFirst()){
			
			close();
			return null;
		}
//		ArrayList<Apartment> apartments = new ArrayList<Apartment>();
//		for (int i = 0; res.next(); i++) {
//			Apartment apartment = new Apartment(res.getInt("id"),
//					res.getString("city"), res.getString("address"),
//					res.getFloat("rooms"), res.getInt("floor"),
//					res.getInt("price"),res.getBoolean("aircondition"),
//					res.getBoolean("elevator"),res.getBoolean("balcony"),
//					res.getBoolean("isolated_room"),res.getBoolean("parking"),
//					res.getBoolean("handicap_access"),res.getBoolean("storage"),
//					res.getBoolean("sun_balcony"));
//			apartments.add(apartment);
//		}

		Object o = new ArrayList();
		o = resultSetToArrayList(res);
		close();
		return (List) o;
	}

	/* st */

	public void deleteUser(int id) throws SQLException {
		ArrayList<String> str = new ArrayList<String>();
		str.add(Integer.toString(id));
		res = query("SELECT id FROM Apartments WHERE user_id=?", str);
		for (int i = 0; res.next(); i++)
			deleteApartment(res.getInt("id"));
		uquery("DELETE FROM Users WHERE id=?", str);
		close();
	}

	public void deleteApartment(int id) throws SQLException {
		ArrayList<String> str = new ArrayList<String>();
		str.add(Integer.toString(id));
		uquery("DELETE FROM Apartment_Picture WHERE apartment_id=?", str);
		close();
		uquery("DELETE FROM Apartments WHERE id=?", str);
		close();
	}

	public User getUserByID(ArrayList<String> str) throws SQLException {
		res = query("SELECT * FROM Users WHERE id=?", str);
		User user = null;
		for (int i = 0; res.next(); i++) {
			user = new User(res.getInt("id"), res.getString("fname"),
					res.getString("lname"), res.getString("email"),
					res.getString("password"), res.getString("phone1"),
					res.getString("phone2"), res.getString("session"),
					res.getString("session_exp"));
		}
		close();
		return user;
	}

	public void updateUserDetails(ArrayList<String> str) throws SQLException {
		uquery("UPDATE Users SET fname=?,lname=?,phone1=?,phone2=? WHERE email=?",
				str);
		close();
	}

	public void updateUser(ArrayList<String> str) throws SQLException {
		uquery("UPDATE Users SET fname=?,lname=?,email=?,password=?,phone1=?,phone2=?,session=?,session_exp=? WHERE id=?",
				str);
		close();
	}

	/* st */
	public void updateUserPassword(ArrayList<String> str) throws SQLException {
		uquery("UPDATE Users SET password=? WHERE email=?", str);
		close();

	}

	public void updateSession(ArrayList<String> str) throws SQLException {
		uquery("UPDATE Users SET session=?,session_exp=? WHERE email=?", str);
		close();
	}

	/* st */

	public void addUsersLocations(ArrayList<String> str) throws SQLException {

		uquery("INSERT INTO Users_Locations (user_id,latitude,longitude,address,name) VALUES (?,?,?,?,?)",
				str);
		close();
	}

	public void addUser(ArrayList<String> str) throws SQLException {
		uquery("INSERT INTO Users (fname,lname,email,password,phone1,phone2) VALUES (?,?,?,?,?,?)",
				str);
		close();
	}

	public void addHistory(int user_id, int apartment_id, String date)
			throws SQLException {
		c = open();
		c.setAutoCommit(true);
		System.out.println("Opened database successfully");
		stmt = c.prepareStatement("INSERT INTO History VALUES (?,?,?,?)");
		stmt.setInt(1, user_id);
		stmt.setInt(2, apartment_id);
		stmt.setInt(3, 0);
		stmt.setString(4, date);
		stmt.executeUpdate();
		close();
	}

	public List getHistory(ArrayList<String> str) throws SQLException {
		res = query(
				"SELECT id,city,address,rooms,price,filename FROM Apartments,History,(select apartment_id,filename from Apartment_Picture GROUP BY apartment_id order by filename) pic WHERE id=History.apartment_id AND id=pic.apartment_id AND History.user_id=?",
				str);
		Object o = new ArrayList();
		o = resultSetToArrayList(res);
		close();
		return (List) o;
	}

	public List searchAddress(String str) throws SQLException {

		String[] words = str.split("\\s+");
		for (int i = 0; i < words.length; i++) {
			// You may want to check for a non-word character before blindly
			// performing a replacement
			// It may also be necessary to adjust the character class
			words[i] = words[i].replaceAll("[^\\w]", "");
		}

		StringBuilder sb = new StringBuilder();
		String or = "";
		boolean first = true;
		for (int i = 0; i < words.length; i++) {

			sb.append(" address LIKE '%" + words[i] + "%' ").append(or);

			if (first) {
				or = " OR ";
				first = false;
			}
		}

		c = open();
		c.setAutoCommit(false);
		System.out.println("Opened database successfully");
		query = "SELECT * FROM Apartments WHERE " + sb.toString();
		stmt = c.prepareStatement(query);
		res = stmt.executeQuery();
		Object o = new ArrayList();
		o = resultSetToArrayList(res);
		close();
		return (List) o;
	}
	public List<String> getHistoryLiked(ArrayList<String> str) throws SQLException{
		res = query(
				"SELECT id,city,address,rooms,price,filename FROM Apartments,History,(select apartment_id,filename from Apartment_Picture GROUP BY apartment_id order by filename) pic WHERE deleted=0 AND id=History.apartment_id AND id=pic.apartment_id AND History.user_id=?",
				str);
		Object o = new ArrayList();
		o = resultSetToArrayList(res);
		close();
		return (List) o;
	}
	public void removeHistory(int user_id, int apartment_id)
			throws SQLException {
		uiquery("UPDATE History SET deleted=1 WHERE user_id=? AND apartment_id=?",
				new int[] { user_id, apartment_id });
		close();
	}

	public List resultSetToArrayList(ResultSet rs) throws SQLException {
		ResultSetMetaData md = rs.getMetaData();
		int columns = md.getColumnCount();
		List list = new ArrayList();
		while (rs.next()) {
			HashMap row = new HashMap(columns);
			for (int i = 1; i <= columns; ++i) {
				row.put(md.getColumnName(i), rs.getObject(i));
			}
			list.add(row);
		}

		return list;
	}

	public HashMap getApartmentByID(int apartment_id) throws SQLException {
		c = open();
		c.setAutoCommit(false);
		System.out.println("Opened database successfully");
		stmt = c.prepareStatement("SELECT * FROM Apartments WHERE id=?");
		stmt.setInt(1, apartment_id);
		res = stmt.executeQuery();
		Apartment apartment = null;
		HashMap row = null;
		ResultSetMetaData md = res.getMetaData();
		int columns = md.getColumnCount();
		while (res.next()) {
			row = new HashMap(columns);
			for (int i = 1; i <= columns; ++i) {
				row.put(md.getColumnName(i), res.getObject(i));
			}
		}
		close();
		return row;
	}

	public HashMap getUserInfo(int user_id) throws SQLException {
		c = open();
		c.setAutoCommit(false);
		System.out.println("Opened database successfully");
		stmt = c.prepareStatement("SELECT phone1,email FROM Users WHERE id=?");
		stmt.setInt(1, user_id);
		res = stmt.executeQuery();
		HashMap row = new HashMap();
		while (res.next()) {
			row.put("phone1", res.getString("phone1"));
			row.put("email", res.getString("email"));
		}
		close();
		return row;
	}

	// /////////// DB.java ///////////////////

	private void uisquery(String query, ArrayList<String> str)
			throws SQLException {

		c = open();
		c.setAutoCommit(true);
		

		try {
			stmt = c.prepareStatement(query);
			stmt.executeUpdate();
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			close();

		}
		// stmt = c.prepareStatement(query);

		// stmt.setInt(1, Integer.parseInt(str.get(0)));// user_id
		// stmt.setInt(2, Integer.parseInt(str.get(1)));// type_id
		// for (int i = 3; i < 6; i++)
		// stmt.setString(i, str.get(i - 1));// city, territory, address
		//
		// stmt.setInt(6, Integer.parseInt(str.get(5)));// rooms
		// stmt.setInt(7, Integer.parseInt(str.get(6)));// floor
		//
		// stmt.setFloat(8, Float.parseFloat(str.get(7)));// lat
		// stmt.setFloat(9, Float.parseFloat(str.get(8)));// lng
		// stmt.setFloat(10, Float.parseFloat(str.get(9)));// sizem2
		//
		// stmt.setString(11, str.get(10));// desc
		// stmt.setInt(12, Integer.parseInt(str.get(11)));// price
		//
		// for (int i = 13; i < 26; i++)
		// //
		// ac,elevator,balcony,MAMAD,parking,handicap,storage,bars,sunBlacony,renovated,furnished,unit,pandoor
		// stmt.setBoolean(i, (str.get(i - 1) == "1") ? true : false);

		// stmt.executeUpdate();
	}

	// public void addApt(ArrayList<String> str) throws SQLException {
	// uquery("INSERT INTO Users "
	// + "(user_id, type_id, territory, city, address, rooms, floor,"
	// + "latitude, longitude, sizem2, comment, price, add_date,"
	// + "aircondition, elevator, balcony, isolated_room, parking,"
	// + "handicap_access, storage, bars, sun_balcony, renovated,"
	// + "furnished,unit, pandoor)"
	// + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
	// str);
	// close();
	// }

	public void addApt(java.util.Map<String, String> str,String filename) throws SQLException {
		String params = "";
		String values = "'";
		String delimp = "";
		String delimv = "";
		boolean first = true;
		for (String item : str.keySet()) {
			if(!item.equals("image") && !item.equals("action")){
				String val = str.get(item);
				if (val == null || val.isEmpty())
					continue;
				params += delimp + item;
				values += delimv + str.get(item);
				if (first) {
					delimp = ", ";
					delimv = "', '";
					first = false;
				}
			}
		}
		params += "";
		values += "'";
		uisquery("INSERT INTO Apartments (" + params + ") VALUES (" + values + ")", null);
		
		res=query1("SELECT last_insert_rowid();");
		int id=res.getInt(1);
		close();
		
		c = open();
		c.setAutoCommit(true);
		System.out.println("Opened database successfully");
		stmt = c.prepareStatement("INSERT INTO Apartment_Picture VALUES(?,?)");
		stmt.setInt(1, id);
		stmt.setString(2, filename);
		stmt.executeUpdate();
		close();
	}
	


//	private void uisquery(String query, ArrayList<String> str)
//			throws SQLException {
//
//		c = open();
//		c.setAutoCommit(true);
//
//		try {
//			stmt = c.prepareStatement(query);
//			stmt.executeUpdate();
//
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//		} finally {
//			close();
//
//		}
//	}

//	public void addApt(java.util.Map<String, String> str) throws SQLException {
//		String params = "";
//		String values = "'";
//		String delimp = "";
//		String delimv = "";
//		boolean first = true;
//		for (String item : str.keySet()) {
//			String val = str.get(item);
//			if (val == null || val.isEmpty())
//				continue;
//			params += delimp + item;
//			values += delimv + str.get(item);
//			if (first) {
//				delimp = ", ";
//				delimv = "', '";
//				first = false;
//			}
//		}
//		params += "";
//		values += "'";
//		uisquery("INSERT INTO Apartments (" + params + ") VALUES (" + values
//				+ ")", null);
//		close();
//	}

	// //////////////////////Api.java /////////////////////////////

	public List searchByAddress(String address) throws SQLException {
		String[] words = address.replace("%20", " ").split("\\s+");
		for (int i = 0; i < words.length; i++) {
			// You may want to check for a non-word character before blindly
			// performing a replacement
			// It may also be necessary to adjust the character class
			words[i] = words[i].replaceAll("[^\\w]|[0-9]", "");

		}

		StringBuilder sb = new StringBuilder();
		String or = "";
		boolean first = true;
		for (int i = 0; i < words.length; i++) {
			if (words[i].isEmpty()) {
				continue;
			}
			sb.append(or).append(" address LIKE '%" + words[i] + "%' ");

			if (first) {
				or = " OR ";
				first = false;
			}
		}

		c = open();
		c.setAutoCommit(false);
		System.out.println("Opened database successfully");

		query = "SELECT * FROM Apartments WHERE " + sb.toString();
		System.out.println(query);
		Object o = null;
		try {
			stmt = c.prepareStatement(query);
			res = stmt.executeQuery();
			// stmt.setString(1, sb.toString());
			o = new ArrayList();
			o = resultSetToArrayList(res);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			close();

		}

		return (List) o;
	}

	public List<String> searchByDistance(float lat, float lng, float dst)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public void updateApartment(int id, float lat, float lng, String address,
			String city) throws SQLException {
		c = open();
		try {
			c.setAutoCommit(true);
			System.out.println("Opened database successfully A");
			query = "UPDATE Apartments SET city='" + city + "', address='"
					+ address + "', latitude='" + lat + "', longitude='" + lng
					+ "' WHERE id='" + id + "'";
			System.out.println(query);
			stmt = c.prepareStatement(query);

			stmt.executeUpdate();
		} catch (Exception e) {
		} finally {
			close();
		}
	}
}
