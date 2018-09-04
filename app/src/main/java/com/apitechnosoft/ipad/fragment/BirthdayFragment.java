package com.apitechnosoft.ipad.fragment;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
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
import com.apitechnosoft.ipad.activity.LoginActivity;
import com.apitechnosoft.ipad.activity.MainActivity;
import com.apitechnosoft.ipad.component.ASTProgressBar;
import com.apitechnosoft.ipad.constants.Contants;
import com.apitechnosoft.ipad.framework.IAsyncWorkCompletedCallback;
import com.apitechnosoft.ipad.framework.ServiceCaller;
import com.apitechnosoft.ipad.model.ContentResponce;
import com.apitechnosoft.ipad.utils.ASTUIUtil;
import com.apitechnosoft.ipad.utils.ASTUtil;
import com.apitechnosoft.ipad.utils.FontManager;
import com.google.gson.Gson;

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
    EditText name, description;
    Switch reminder;
    TextView email;
    String nameStr, desStr, dateStr, timeStr;
    String UserId;
    String reminderStr = "off";

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
        name = findViewById(R.id.name);
        reminder = findViewById(R.id.reminder);
        email = findViewById(R.id.email);
        description = findViewById(R.id.description);
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
        String myFormat = "yyyy-mm-dd"; //In which you need put here
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
        final SimpleDateFormat sdfTime = new SimpleDateFormat("hh:mm aa");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        time = new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                myCalendar.set(Calendar.MINUTE, minute);
                timeView.setText(sdfTime.format(myCalendar.getTime()));
            }
        };
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
                if (isValidate()) {
                    saveBdEvent();
                }
                break;
        }
    }

    private void saveBdEvent() {
        long submitTime = System.currentTimeMillis();
        String currentDate = ASTUtil.formatDate(String.valueOf(submitTime));
        if (ASTUIUtil.isOnline(getContext())) {
            final ASTProgressBar dotDialog = new ASTProgressBar(getContext());
            dotDialog.show();
            ServiceCaller serviceCaller = new ServiceCaller(getContext());
            final String url = Contants.BASE_URL + Contants.SaveBdaysApi + "username=" + UserId + "&" + "fromdate=" + dateStr + "&" + "todate=" + timeStr + "&" + "mysqldate=" + currentDate + "&" + "eventname=" + nameStr + "&" + "reminder=" + reminderStr + "&" + "eventdescription=" + desStr;
            serviceCaller.CallCommanServiceMethod(url, "saveBdEvent", new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String result, boolean isComplete) {
                    if (isComplete) {
                        ContentResponce data = new Gson().fromJson(result, ContentResponce.class);
                        if (data != null) {
                            if (data.isStatus()) {
                                Toast.makeText(getContext(), data.getError_msg(), Toast.LENGTH_LONG).show();
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
        dateStr = caladerDate.getText().toString();
        timeStr = timeView.getText().toString();
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
        } else if (dateStr.length() == 0) {
            showToast("Please Selete date");
            return false;
        } else if (timeStr.length() == 0) {
            showToast("Please Selete time");
            return false;
        }
        return true;
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

}
