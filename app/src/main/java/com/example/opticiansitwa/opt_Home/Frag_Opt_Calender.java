package com.example.opticiansitwa.opt_Home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.opticiansitwa.R;
import com.example.opticiansitwa.databinding.FragOptAppointmentBinding;
import com.example.opticiansitwa.databinding.FragOptCalenderBinding;
import com.github.jhonnyx2012.horizontalpicker.DatePickerListener;
import com.github.jhonnyx2012.horizontalpicker.HorizontalPicker;

import org.joda.time.DateTime;

public class Frag_Opt_Calender extends Fragment implements DatePickerListener {

    FragOptCalenderBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragOptCalenderBinding.inflate(inflater,container,false);
        binding.datePicker.setListener(this)
                .setDays(120)
                .setOffset(7)
                .setDateSelectedTextColor(Color.WHITE)
                .setTodayButtonTextColor(getResources().getColor(R.color.blue))
                .showTodayButton(false)
                .init();
        binding.datePicker.setBackgroundColor(getResources().getColor(R.color.blue1));
        binding.datePicker.setDate(new DateTime());


        return binding.getRoot();
    }

    @Override
    public void onDateSelected(DateTime dateSelected) {

    }
}