package com.thirdyearjavamaps.classes;

public class User {
	private int id;
	private String fname;
	private String lname;
	private String email;
	private String password;
	private String phone1;
	private String phone2;
	private String session;
	private String session_exp;

	public void setID(int id) {
		this.id = id;
	}

	public int getID() {
		return id;
	}

	public void setSession(String session) {
		this.session = session;
	}

	public String getSession() {
		return session;
	}
	public void setSessionExp(String session_exp) {
		this.session_exp = session_exp;
	}

	public String getSessionExp() {
		return session_exp;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getFname() {
		return fname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getLname() {
		return lname;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}

	public String getPhone1() {
		return phone1;
	}

	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}

	public String getPhone2() {
		return phone2;
	}
	
}
