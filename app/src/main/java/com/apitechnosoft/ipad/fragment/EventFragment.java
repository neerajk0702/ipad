package com.apitechnosoft.ipad.fragment;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.component.ASTProgressBar;
import com.apitechnosoft.ipad.constants.Contants;
import com.apitechnosoft.ipad.framework.IAsyncWorkCompletedCallback;
import com.apitechnosoft.ipad.framework.ServiceCaller;
import com.apitechnosoft.ipad.model.ContentResponce;
import com.apitechnosoft.ipad.utils.ASTUIUtil;
import com.apitechnosoft.ipad.utils.ASTUtil;
import com.apitechnosoft.ipad.utils.FontManager;
import com.apitechnosoft.ipad.utils.NoSSLv3Factory;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import javax.net.ssl.HttpsURLConnection;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventFragment extends MainFragment {

    Typeface materialdesignicons_font;
    TextView fromcaladerDate, fromtimeView, totimeView, tocaladerDate;
    Button submit;
    Calendar myCalendar;
    EditText name, description;
    Switch reminder;
    TextView email;
    String nameStr, desStr;
    String UserId;
    String reminderStr = "off";
    String fromcaladerDateStr, fromtimeStr, totimeStr, tocaladerDateStr;
    DatePickerDialog todatepicker;
    TimePickerDialog totimmepicker;
    DatePickerDialog fromdatepicker;
    TimePickerDialog fromtimepicker;

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

        name = findViewById(R.id.name);
        reminder = findViewById(R.id.reminder);
        email = findViewById(R.id.email);
        description = findViewById(R.id.description);
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
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

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
        fromdatepicker = new DatePickerDialog(getContext(), date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));

        final SimpleDateFormat sdfTime = new SimpleDateFormat("hh:mm aa");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                myCalendar.set(Calendar.MINUTE, minute);
                fromtimeView.setText(sdfTime.format(myCalendar.getTime()));
            }
        };
        fromtimepicker = new TimePickerDialog(getContext(), time, myCalendar
                .get(Calendar.HOUR_OF_DAY), myCalendar
                .get(Calendar.MINUTE), true);
        DatePickerDialog.OnDateSetListener todate = new DatePickerDialog.OnDateSetListener() {

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
        todatepicker = new DatePickerDialog(getContext(), todate, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        TimePickerDialog.OnTimeSetListener totime = new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                myCalendar.set(Calendar.MINUTE, minute);
                totimeView.setText(sdfTime.format(myCalendar.getTime()));
            }
        };
        totimmepicker = new TimePickerDialog(getContext(), totime, myCalendar
                .get(Calendar.HOUR_OF_DAY), myCalendar
                .get(Calendar.MINUTE), true);
        SharedPreferences prefs = getActivity().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        if (prefs != null) {
            UserId = prefs.getString("UserId", "");
            email.setText(UserId);
        }

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.fromtimeView:
                fromtimepicker.show();
                break;
            case R.id.fromcaladerDate:
                fromdatepicker.show();
                break;
            case R.id.totimeView:
                totimmepicker.show();
                break;
            case R.id.tocaladerDate:
                todatepicker.show();
                break;
            case R.id.submit:
                if (isValidate()) {
                    saveEvent();
                }
                break;
        }
    }


    private void saveEvent() {
        long submitTime = System.currentTimeMillis();
        String currentDate = ASTUtil.formatDate(String.valueOf(submitTime));
        if (ASTUIUtil.isOnline(getContext())) {
            final ASTProgressBar dotDialog = new ASTProgressBar(getContext());
            dotDialog.show();
            ServiceCaller serviceCaller = new ServiceCaller(getContext());
            final String url = Contants.BASE_URL + Contants.SaveEventApi + "username=" + UserId + "&" + "fromdate=" + fromcaladerDateStr + "&" + "todate=" + tocaladerDateStr + "&" + "mysqldate=" + currentDate + "&" + "eventname=" + nameStr + "&" + "reminder=" + reminderStr + "&" + "eventdescription=" + desStr + "&" + "fromdatetime=" + fromtimeStr + "&" + "todatetime=" + totimeStr;
            serviceCaller.CallCommanServiceMethod(url, "saveEvent", new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String result, boolean isComplete) {
                    if (isComplete) {
                        ContentResponce data = new Gson().fromJson(result, ContentResponce.class);
                        if (data != null) {
                            if (data.isStatus()) {
                                Toast.makeText(getContext(), "Event saved Successfully", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getContext(), data.getError_msg(), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Event not saved Successfully!", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        showToast(Contants.Error);
                    }
                    if (dotDialog.isShowing()) {
                        dotDialog.dismiss();
                    }
                }
            });
        } else {
            showToast(Contants.OFFLINE_MESSAGE);
        }

    }

    // ----validation -----
    private boolean isValidate() {
        String emailRegex = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
        nameStr = name.getText().toString();
        desStr = description.getText().toString();
        fromcaladerDateStr = fromcaladerDate.getText().toString();
        fromtimeStr = fromtimeView.getText().toString();
        totimeStr = totimeView.getText().toString();
        tocaladerDateStr = tocaladerDate.getText().toString();
        if (reminder.isChecked()) {
            reminderStr = "on";
        } else {
            reminderStr = "off";
        }
        if (nameStr.length() == 0) {
            showToast("Please enter event name");
            return false;
        } else if (desStr.length() == 0) {
            showToast("Please enter description");
            return false;
        } else if (fromcaladerDateStr.length() == 0) {
            showToast("Please select from date");
            return false;
        } else if (fromtimeStr.length() == 0) {
            showToast("Please  select from time");
            return false;
        } else if (tocaladerDateStr.length() == 0) {
            showToast("Please select to date");
            return false;
        } else if (totimeStr.length() == 0) {
            showToast("Please select to time");
            return false;
        }
        return true;
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }
}
