package com.example.opticiansitwa.models;

public class User {

    public String name;
    public  String email;
    public String profile_pic;
    public String location_x;
    public String location_y;
    public String address_google_map;
    public String ssn;
    public String age;
//    public String notif_id;


    public User(String name, String email, String profile_pic, String location_x, String location_y, String address_google_map, String ssn, String age) {
        this.name = name;
        this.email = email;
        this.profile_pic = profile_pic;
        this.location_x = location_x;
        this.location_y = location_y;
        this.address_google_map = address_google_map;
        this.ssn = ssn;
        this.age = age;
    }
}
