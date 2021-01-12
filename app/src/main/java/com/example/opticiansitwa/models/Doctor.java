package com.example.opticiansitwa.models;

import java.util.ArrayList;

public class Doctor {

    public String name;
    public  String email;
    public String profile_pic;
    public String location_x;
    public String location_y;
    public String address_google_map;
    public String ssn;
    public String age;
    public String id_proof_url_1;
    public String id_proof_url_2;
    public String status;
    public Long morning_start_epoch;
    public Long morning_end_epoch;
    public Long afternoon_start_epoch;
    public Long afternoon_end_epoch;
    public Long evening_start_epoch;
    public Long evening_end_epoch;
    public ArrayList<String> m_timing ;
    public ArrayList<String> a_timing;
    public ArrayList<String> e_timing;


    public Doctor(String name, String email, String profile_pic, String location_x, String id_proof_url_1, String id_proof_url_2, String location_y, String address_google_map, String ssn, String age, String status,Long morning_start_epoch, Long morning_end_epoch, Long afternoon_start_epoch, Long afternoon_end_epoch, Long evening_start_epoch, Long evening_end_epoch,ArrayList<String> m_timing,
    ArrayList<String> a_timing,ArrayList<String> e_timing) {
        this.name = name;
        this.email = email;
        this.profile_pic = profile_pic;
        this.location_x = location_x;
        this.location_y = location_y;
        this.address_google_map = address_google_map;
        this.id_proof_url_1=id_proof_url_1;
        this.id_proof_url_2=id_proof_url_2;
        this.status=status;
        this.ssn = ssn;
        this.age = age;
        this.morning_start_epoch = morning_start_epoch;
        this.morning_end_epoch = morning_end_epoch;
        this.afternoon_start_epoch = afternoon_start_epoch;
        this.afternoon_end_epoch = afternoon_end_epoch;
        this.evening_start_epoch = evening_start_epoch;
        this.evening_end_epoch = evening_end_epoch;
        this.m_timing = m_timing;
        this.a_timing = a_timing;
        this.e_timing = e_timing;


    }
}
