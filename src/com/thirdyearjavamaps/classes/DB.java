package com.thirdyearjavamaps.classes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
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
		stmt = open().prepareStatement(query);
		for (int i = 1; i < str.size() + 1; i++)
			stmt.setString(i, str.get(i - 1));

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
	
	private void uiquery(String query, int [] ints)
			throws SQLException {
		c = open();
		c.setAutoCommit(true);
		stmt = c.prepareStatement(query);
		for (int i = 1; i < ints.length + 1; i++)
			stmt.setInt(i, ints[i-1]);
		stmt.executeUpdate();
	}
	/*st*/
	public boolean userExists(ArrayList<String> str) throws SQLException 
	{
		res = query("SELECT * FROM Users WHERE email=?", str);
		if (!res.isBeforeFirst())
		{
			return false;
		}
		return true;
	}
	public User getUser(ArrayList<String> str) throws SQLException {
		res = query("SELECT * FROM Users WHERE email=? AND password=?", str);
		if (!res.isBeforeFirst())
			return null;
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
	public Admin getAdmin(ArrayList<String> str) throws SQLException{
		res = query("SELECT * FROM Admin WHERE username=? AND password=?", str);
		if (!res.isBeforeFirst())
			return null;
		Object o = new Admin();
		while (res.next()) {
			((Admin) o).setID(res.getInt("id"));
			((Admin) o).setUsername(res.getString("username"));
			((Admin) o).setPassword(res.getString("password"));
		}
		close();
		return (Admin) o;
	}
	
	public User getUserBySession(ArrayList<String> str) throws SQLException {
		res = query("SELECT * FROM Users WHERE session=?", str);
		if (!res.isBeforeFirst())
			return null;
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
	/*st*/
	public void updateUserDetails(ArrayList<String> str) throws SQLException
	{
		uquery("UPDATE Users SET fname=?,lname=?,phone1=?,phone2=? WHERE email=?", str);
		close();
	}
	/*st*/
	public void updateUserPassword(ArrayList<String> str) throws SQLException
	{
		uquery("UPDATE Users SET password=? WHERE email=?", str);
	close();
		
	}
	public void updateSession(ArrayList<String> str) throws SQLException {
		uquery("UPDATE Users SET session=?,session_exp=? WHERE email=?", str);
		close();
	}
	/*st*/
	public void addUsersLocations(ArrayList<String> str) throws SQLException
	{
		uquery("INSERT INTO Users_Locations (name) VALUES (?)",
				str);
		close();
	}
	public void addUser(ArrayList<String> str) throws SQLException {
		uquery("INSERT INTO Users (fname,lname,email,password,phone1,phone2) VALUES (?,?,?,?,?,?)",
				str);
		close();
	}

	public List getHistory(ArrayList<String> str) throws SQLException {
		res = query(
				"SELECT id,city,address,rooms,price,filename FROM Apartments,History,(select apartment_id,filename from Apartment_Picture GROUP BY apartment_id order by filename) pic WHERE deleted=0 AND id=History.apartment_id AND id=pic.apartment_id AND Apartments.user_id=?",
				str);
		Object o = new ArrayList();
		o = resultSetToArrayList(res);
		close();
		return (List) o;
	}

	public void removeHistory(int user_id,int apartment_id) throws SQLException{
		uiquery("UPDATE History SET deleted=1 WHERE user_id=? AND apartment_id=?",new int[]{user_id,apartment_id});
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
}
