package org.thirdyearjava.api;

public class User {
 int id;
 String fname;
 String lname;
 String email;
 String password;
 String phone1;
 String phone2;
 String session;
 
 void setID(int id){
	 this.id=id;
 }
 int getID(){
	 return id;
 }
 void setSession(String session){
	this.session=session;
 }
 String getSession(){
	 return session;
 }
 
 void setFname(String fname){
	this.fname=fname;
 }
 String getFname(){
	 return fname;
 }
 void setLname(String lname){
	this.lname=lname;
 }
 String getLname(){
	 return lname;
 }
 void setEmail(String email){
	this.email=email;
 }
 String getEmail(){
	 return email;
 }
 void setPassword(String password){
	this.password=password;
 }
 String getPassword(){
	 return password;
 }
 void setPhone1(String phone1){
	this.phone1=phone1;
 }
 String getPhone1(){
	 return phone1;
 }
 void setPhone2(String phone2){
	this.phone2=phone2;
 }
 String getPhone2(){
	 return phone2;
 }
}
