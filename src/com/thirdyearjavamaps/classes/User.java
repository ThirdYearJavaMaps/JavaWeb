package com.thirdyearjavamaps.classes;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

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

	public User(int id, String fname, String lname, String email,
			String password, String phone1, String phone2, String session,
			String session_exp) {
		this.id = id;
		this.fname = fname;
		this.email = email;
		this.password = password;
		this.phone1 = phone1;
		this.phone2 = phone2;
		this.session = session;
		this.session_exp = session_exp;
	}

	public User() {
		// TODO Auto-generated constructor stub
	}

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

	public boolean Login(String email, String password) throws SQLException {
		DB db = new DB();
		ArrayList<String> str = new ArrayList<String>();
		str.add(email);
		str.add(password);
		if (db.getUser(this, str)) {
			setSession(Generate_Session());
			updateSession();
			return true;
		}
		return false;
	}

	public void updateSession() throws SQLException {
		DB db = new DB();
		ArrayList<String> str1 = new ArrayList<String>();
		str1.add(session);
		str1.add(String.format("%d", epochNow() + 604800)); // one
															// week
															// session
		str1.add(email);
		db.updateSession(str1);
	}

	public static long epochNow() {
		return System.currentTimeMillis() / 1000;
	}

	private String Generate_Session() {
		long epoch = epochNow();
		String plaintext = email + password + epoch;
		System.out.println(plaintext);
		return md5(plaintext);
	}

	public static String md5(String plaintext){
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
	
	public boolean sessionExpired() {
		if (session_exp.isEmpty())
			return epochNow() - Long.parseLong(session_exp) >= 0;
		return false;
	}

	public List<String> getHistoryList() throws SQLException {
		DB db = new DB();
		ArrayList<String> str = new ArrayList<String>();
		str.add(String.valueOf(id));
		return (List<String>) db.getHistory(str);
	}
	
	public void addHistory(int apartment_id) throws SQLException {
		DB db = new DB();
		db.addHistory(id, apartment_id, Long.toString(epochNow()));
	}
	
	public void removeHistory(int apartment_id) throws SQLException {
		DB db = new DB();
		db.removeHistory(id, apartment_id);
	}
	
	//updated by st stas and dj araksha
	public void sendEmail(String what_action){
			String from="gymprogramerssce@gmail.com";
			String subject="";
			String pass="gymprogramers1234";
			String body="";
			if(what_action=="confirmation")
			{
				subject="Weepi Registration confirmation";
			 body=String.format("Thank you for registering to Weepi.\n\n Email: %s\nPassword: %s",email,password);
			}
			if(what_action=="forgotpassword")
			{
				subject="Weepi brings your forgotten password";
				 body=String.format("Hi it's Weepi.\n\n Your password is: %s",password);
			}
			
			Properties props = System.getProperties();
	        String host = "smtp.gmail.com";
	        props.put("mail.smtp.starttls.enable", "true");
	        props.put("mail.smtp.host", host);
	        
	        props.put("mail.smtp.user", "gymprogramerssce");
	        props.put("mail.smtp.password", pass);
	        props.put("mail.smtp.port", "587");
	        props.put("mail.smtp.auth", "true");

	        Session session = Session.getDefaultInstance(props);
	        MimeMessage message = new MimeMessage(session);

	        try {
	            message.setFrom(new InternetAddress(from));
	            InternetAddress toAddress = new InternetAddress(this.email);
	            message.addRecipient(Message.RecipientType.TO, toAddress);
	            

	            message.setSubject(subject);
	            message.setText(body);
	            Transport transport = session.getTransport("smtp");
	            transport.connect(host, from, pass);
	            transport.sendMessage(message, message.getAllRecipients());
	            transport.close();
	        }
	        catch (AddressException ae) {
	            ae.printStackTrace();
	        }
	        catch (MessagingException me) {
	            me.printStackTrace();
	        }
	    }
	}
