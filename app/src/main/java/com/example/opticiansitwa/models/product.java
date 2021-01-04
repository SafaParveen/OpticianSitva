package com.example.opticiansitwa.models;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class product {

    public String title;
    public String subtitle;
    public String price;
    public Long store_id;
    public String stock_left;
    public String description;
    public ArrayList<String> pic_list;

    public product(String title, String subtitle, String price, Long store_id, String stock_left, String description, ArrayList<String> pic_list) {
        this.title = title;
        this.subtitle = subtitle;
        this.price = price;
        this.store_id = store_id;
        this.stock_left = stock_left;
        this.description = description;
        this.pic_list = pic_list;
    }


}
