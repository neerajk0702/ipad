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
public class EventFragment extends MainFragment {

    Typeface materialdesignicons_font;
    TextView fromcaladerDate, fromtimeView, totimeView, tocaladerDate;
    Button submit;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    TimePickerDialog.OnTimeSetListener time;
    TimePickerDialog.OnTimeSetListener totime;
    DatePickerDialog.OnDateSetListener todate;

    @Override
    protected int fragmentLayout() {
        return R.layout.fragment_event;
    }

    @Override
    protected void loadView() {
        materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(getContext(), "fonts/materialdesignicons-webfont.otf");
        fromcaladerDate = findViewById(R.id.fromcaladerDate);
        fromtimeView = findViewById(R.id.fromtimeView);
        totimeView = findViewById(R.id.totimeView);
        tocaladerDate = findViewById(R.id.tocaladerDate);
        submit = findViewById(R.id.submit);
    }

    @Override
    protected void setClickListeners() {
        tocaladerDate.setOnClickListener(this);
        totimeView.setOnClickListener(this);
        fromtimeView.setOnClickListener(this);
        fromcaladerDate.setOnClickListener(this);
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
                fromcaladerDate.setText(sdf.format(myCalendar.getTime()));
            }
        };
        final SimpleDateFormat sdfTime = new SimpleDateFormat("HH.mm");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        time = new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                myCalendar.set(Calendar.MINUTE, minute);
                fromtimeView.setText(sdfTime.format(myCalendar.getTime()));
            }
        };


        todate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                tocaladerDate.setText(sdf.format(myCalendar.getTime()));
            }
        };
        totime = new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                myCalendar.set(Calendar.MINUTE, minute);
                totimeView.setText(sdfTime.format(myCalendar.getTime()));
            }
        };
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.fromtimeView:
                new TimePickerDialog(getContext(), time, myCalendar
                        .get(Calendar.HOUR_OF_DAY), myCalendar
                        .get(Calendar.MINUTE), true).show();
                break;
            case R.id.fromcaladerDate:
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;

            case R.id.totimeView:
                new TimePickerDialog(getContext(), totime, myCalendar
                        .get(Calendar.HOUR_OF_DAY), myCalendar
                        .get(Calendar.MINUTE), true).show();
                break;
            case R.id.tocaladerDate:
                new DatePickerDialog(getContext(), todate, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.submit:
                break;
        }
    }
}
