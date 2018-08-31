package com.apitechnosoft.ipad.fragment;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.utils.FontManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 */
public class BirthdayFragment extends MainFragment {


    Typeface materialdesignicons_font;
    TextView caladerDate, timeView;
    Button submit;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    TimePickerDialog.OnTimeSetListener time;

    @Override
    protected int fragmentLayout() {
        return R.layout.fragment_birthday;
    }


    @Override
    protected void loadView() {
        materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(getContext(), "fonts/materialdesignicons-webfont.otf");
        timeView = findViewById(R.id.timeView);
        caladerDate = findViewById(R.id.caladerDate);
        submit = findViewById(R.id.submit);
    }


    @Override
    protected void setClickListeners() {
        caladerDate.setOnClickListener(this);
        timeView.setOnClickListener(this);
        submit.setOnClickListener(this);
    }

    @Override
    protected void setAccessibility() {

    }

    @Override
    protected void dataToView() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        final SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                caladerDate.setText(sdf.format(myCalendar.getTime()));
            }
        };
        final SimpleDateFormat sdfTime = new SimpleDateFormat("HH.mm");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
     time = new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                myCalendar.set(Calendar.MINUTE, minute);
                timeView.setText(sdfTime.format(myCalendar.getTime()));
            }
        };
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.timeView:
                new TimePickerDialog(getContext(), time, myCalendar
                        .get(Calendar.HOUR_OF_DAY), myCalendar
                        .get(Calendar.MINUTE), true).show();
                break;
            case R.id.caladerDate:
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.submit:
                break;
        }
    }
}
