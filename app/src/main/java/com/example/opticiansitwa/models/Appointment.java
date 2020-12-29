package com.example.opticiansitwa.models;

import java.util.Map;

public class Appointment {

    public String user_id;
    public String doctor_id;
    public String appointment_id;
    public String epoch;
    public String approve_status;
    public String test_status;
    public String rating;
    public String user_complaint;
    public String video_url;
    public Map<String,String>test_report;
    public String cost;
    public String invoice_url;
    public String note_from_doctor;
    public String cancel_status;
    public String review;

    public Appointment(String user_id, String doctor_id, String appointment_id, String epoch, String approve_status, String test_status, String rating, String user_complaint, String video_url, Map<String, String> test_report, String cost, String invoice_url, String note_from_doctor, String cancel_status, String review) {
        this.user_id = user_id;
        this.doctor_id = doctor_id;
        this.appointment_id = appointment_id;
        this.epoch = epoch;
        this.approve_status = approve_status;
        this.test_status = test_status;
        this.rating = rating;
        this.user_complaint = user_complaint;
        this.video_url = video_url;
        this.test_report = test_report;
        this.cost = cost;
        this.invoice_url = invoice_url;
        this.note_from_doctor = note_from_doctor;
        this.cancel_status = cancel_status;
        this.review = review;
    }


}
