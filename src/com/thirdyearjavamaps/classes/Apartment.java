package com.thirdyearjavamaps.classes;


public class Apartment {
    int id;
    String city;
    int price;
    String territory;
    String address;
    boolean aircondition;
    boolean elevator;
    boolean balcony;
    boolean isolated_room;
    boolean parking;
    boolean handicap_access;
    boolean storage;
    boolean bars;
    boolean sun_balcony;
    boolean renovated;
    boolean furnished;
    boolean unit;
    boolean pandoor;
    float rooms;
    int floor;
    float longitude;
    float latitude;
    float sizem2;
    String desc;

    public Apartment(int id, String city, int price, String territory, String address, boolean aircondition, boolean elevator, boolean balcony, boolean isolated_room, boolean parking, boolean handicap_access, boolean storage, boolean bars, boolean sun_balcony, boolean renovated, boolean furnished, boolean unit, boolean pandoor, float rooms, int floor, float longitude, float latitude, float sizem2,String desc) {
        this.id = id;
        this.city = city;
        this.price = price;
        this.territory = territory;
        this.address = address;
        this.aircondition = aircondition;
        this.elevator = elevator;
        this.balcony = balcony;
        this.isolated_room = isolated_room;
        this.parking = parking;
        this.handicap_access = handicap_access;
        this.storage = storage;
        this.bars = bars;
        this.sun_balcony = sun_balcony;
        this.renovated = renovated;
        this.furnished = furnished;
        this.unit = unit;
        this.pandoor = pandoor;
        this.rooms = rooms;
        this.floor = floor;
        this.longitude = longitude;
        this.latitude = latitude;
        this.sizem2 = sizem2;
        this.desc=desc;
    }
    public Apartment(int id,String city,String address,float rooms,float sizem2,int price){
    	this.id=id;
    	this.city=city;
    	this.address=address;
    	this.rooms=rooms;
    	this.sizem2=sizem2;
    	this.price=price;
    }
    /*Ariel another constructor*/
    public Apartment(int id,String city,String address,float rooms,int floor,int price,boolean aircondition, boolean elevator, boolean balcony, boolean isolated_room, boolean parking, boolean handicap_access, boolean storage, boolean sun_balcony){
    	this.id=id;
    	this.city=city;
    	this.address=address;
    	this.rooms=rooms;
    	this.floor=floor;
    	this.price=price;
    	this.aircondition=aircondition;
    	this.elevator = elevator;
        this.balcony = balcony;
        this.isolated_room = isolated_room;
        this.parking = parking;
        this.handicap_access = handicap_access;
        this.storage = storage;
        this.sun_balcony = sun_balcony;
    	
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getTerritory() {
        return territory;
    }

    public void setTerritory(String territory) {
        this.territory = territory;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isAircondition() {
        return aircondition;
    }

    public void setAircondition(boolean aircondition) {
        this.aircondition = aircondition;
    }

    public boolean isElevator() {
        return elevator;
    }

    public void setElevator(boolean elevator) {
        this.elevator = elevator;
    }

    public boolean isBalcony() {
        return balcony;
    }

    public void setBalcony(boolean balcony) {
        this.balcony = balcony;
    }

    public boolean isIsolated_room() {
        return isolated_room;
    }

    public void setIsolated_room(boolean isolated_room) {
        this.isolated_room = isolated_room;
    }

    public boolean isParking() {
        return parking;
    }

    public void setParking(boolean parking) {
        this.parking = parking;
    }

    public boolean isHandicap_access() {
        return handicap_access;
    }

    public void setHandicap_access(boolean handicap_access) {
        this.handicap_access = handicap_access;
    }

    public boolean isStorage() {
        return storage;
    }

    public void setStorage(boolean storage) {
        this.storage = storage;
    }

    public boolean isBars() {
        return bars;
    }

    public void setBars(boolean bars) {
        this.bars = bars;
    }

    public boolean isSun_balcony() {
        return sun_balcony;
    }

    public void setSun_balcony(boolean sun_balcony) {
        this.sun_balcony = sun_balcony;
    }

    public boolean isRenovated() {
        return renovated;
    }

    public void setRenovated(boolean renovated) {
        this.renovated = renovated;
    }

    public boolean isFurnished() {
        return furnished;
    }

    public void setFurnished(boolean furnished) {
        this.furnished = furnished;
    }

    public boolean isUnit() {
        return unit;
    }

    public void setUnit(boolean unit) {
        this.unit = unit;
    }

    public boolean isPandoor() {
        return pandoor;
    }

    public void setPandoor(boolean pandoor) {
        this.pandoor = pandoor;
    }

    public float getRooms() {
        return rooms;
    }

    public void setRooms(float rooms) {
        this.rooms = rooms;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getSizem2() {
        return sizem2;
    }

    public void setSizem2(float sizem2) {
        this.sizem2 = sizem2;
    }


}
