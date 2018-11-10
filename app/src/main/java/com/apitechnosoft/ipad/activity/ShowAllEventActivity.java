package com.apitechnosoft.ipad.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.adapter.EventDetailAdapter;
import com.apitechnosoft.ipad.adapter.OrganizerPagerAdapter;
import com.apitechnosoft.ipad.adapter.ShareRecivedCommentAdapter;
import com.apitechnosoft.ipad.calendarview.CalendarAdapter;
import com.apitechnosoft.ipad.calendarview.CalendarCollection;
import com.apitechnosoft.ipad.calendarview.CalenderActivity;
import com.apitechnosoft.ipad.component.ASTProgressBar;
import com.apitechnosoft.ipad.constants.Contants;
import com.apitechnosoft.ipad.framework.IAsyncWorkCompletedCallback;
import com.apitechnosoft.ipad.framework.ServiceCaller;
import com.apitechnosoft.ipad.model.Commentdata;
import com.apitechnosoft.ipad.model.ContentData;
import com.apitechnosoft.ipad.model.Eventdata;
import com.apitechnosoft.ipad.model.EventotdataList;
import com.apitechnosoft.ipad.utils.ASTUIUtil;
import com.apitechnosoft.ipad.utils.ASTUtil;
import com.apitechnosoft.ipad.utils.FontManager;
import com.apitechnosoft.ipad.utils.NoSSLv3Factory;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.function.Function;

import javax.net.ssl.HttpsURLConnection;

public class ShowAllEventActivity extends AppCompatActivity {
    private Toolbar toolbar;
    String UserId;
    RecyclerView eventrecycler_view;

    public GregorianCalendar cal_month, cal_month_copy;
    private CalendarAdapter cal_adapter;
    private TextView tv_month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_event);
        if (ASTUtil.isTablet(ShowAllEventActivity.this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init();
    }

    private void init() {

        SharedPreferences prefs = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        if (prefs != null) {
            UserId = prefs.getString("UserId", "");
        }
        Typeface materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(this, "fonts/materialdesignicons-webfont.otf");
        TextView back = toolbar.findViewById(R.id.back);
        back.setTypeface(materialdesignicons_font);
        back.setText(Html.fromHtml("&#xf30d;"));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        eventrecycler_view = findViewById(R.id.eventrecycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ShowAllEventActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        eventrecycler_view.setLayoutManager(linearLayoutManager);
//--------------calendar data-------------------
        CalendarCollection.date_collection_arr = new ArrayList<CalendarCollection>();
        CalendarCollection.date_collection_arr.add(new CalendarCollection("1910-07-02", "ipad"));

        cal_month = (GregorianCalendar) GregorianCalendar.getInstance();
        cal_month_copy = (GregorianCalendar) cal_month.clone();
        cal_adapter = new CalendarAdapter(this, cal_month, CalendarCollection.date_collection_arr);


        tv_month = findViewById(R.id.tv_month);
        tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy", cal_month));

        ImageButton previous = findViewById(R.id.ib_prev);

        previous.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setPreviousMonth();
                refreshCalendar();
            }
        });

        ImageButton next = findViewById(R.id.Ib_next);
        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setNextMonth();
                refreshCalendar();

            }
        });

        GridView gridview = findViewById(R.id.gv_calendar);
        gridview.setAdapter(cal_adapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                ((CalendarAdapter) parent.getAdapter()).setSelected(v, position);
                String selectedGridDate = CalendarAdapter.day_string
                        .get(position);

                String[] separatedTime = selectedGridDate.split("-");
                String gridvalueString = separatedTime[2].replaceFirst("^0*", "");
                int gridvalue = Integer.parseInt(gridvalueString);
                GetEventDetails(selectedGridDate);//get event detail
                if ((gridvalue > 10) && (position < 8)) {
                    setPreviousMonth();
                    refreshCalendar();
                } else if ((gridvalue < 7) && (position > 28)) {
                    setNextMonth();
                    refreshCalendar();
                }
                ((CalendarAdapter) parent.getAdapter()).setSelected(v, position);

                // ((CalendarAdapter) parent.getAdapter()).getPositionList(selectedGridDate, ShowAllEventActivity.this);
            }

        });

        getAllEvents();
    }

    protected void setNextMonth() {
        if (cal_month.get(GregorianCalendar.MONTH) == cal_month
                .getActualMaximum(GregorianCalendar.MONTH)) {
            cal_month.set((cal_month.get(GregorianCalendar.YEAR) + 1),
                    cal_month.getActualMinimum(GregorianCalendar.MONTH), 1);
        } else {
            cal_month.set(GregorianCalendar.MONTH,
                    cal_month.get(GregorianCalendar.MONTH) + 1);
        }

    }

    protected void setPreviousMonth() {
        if (cal_month.get(GregorianCalendar.MONTH) == cal_month
                .getActualMinimum(GregorianCalendar.MONTH)) {
            cal_month.set((cal_month.get(GregorianCalendar.YEAR) - 1),
                    cal_month.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            cal_month.set(GregorianCalendar.MONTH,
                    cal_month.get(GregorianCalendar.MONTH) - 1);
        }

    }

    public void refreshCalendar() {
        cal_adapter.refreshDays();
        cal_adapter.notifyDataSetChanged();
        tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy", cal_month));
    }

    //get all events
    private void getAllEvents() {

        if (ASTUIUtil.isOnline(ShowAllEventActivity.this)) {
            final ASTProgressBar dotDialog = new ASTProgressBar(ShowAllEventActivity.this);
            dotDialog.show();
            ServiceCaller serviceCaller = new ServiceCaller(ShowAllEventActivity.this);
            final String url = Contants.BASE_URL + Contants.GetAllEventList + "username=" + UserId;
            serviceCaller.CallCommanServiceMethod(url, "getAllEvents", new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String result, boolean isComplete) {
                    Log.d(Contants.LOG_TAG, "getAllEvents**" + result);
                    if (isComplete) {
                        ContentData data = new Gson().fromJson(result, ContentData.class);
                        if (data != null) {
                            if (data.getEventotdataList() != null) {
                                for (EventotdataList eventotdataList : data.getEventotdataList()) {
                                    if (eventotdataList.getFromdate() != null && !eventotdataList.getFromdate().equals("")) {
                                        if (ASTUtil.isDateValid(eventotdataList.getFromdate())) {
                                            if (convertDateIntoLong(eventotdataList.getFromdate()) != 0) {
                                                CalendarCollection.date_collection_arr.add(new CalendarCollection(eventotdataList.getFromdate(), eventotdataList.getEventname()));
                                            }
                                        }
                                    }
                                }
                                refreshCalendar();
                            } else {
                                Toast.makeText(ShowAllEventActivity.this, "Event List Data found!", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(ShowAllEventActivity.this, "Event List Data found!", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        ASTUIUtil.showToast(Contants.Error);
                    }
                    if (dotDialog.isShowing()) {
                        dotDialog.dismiss();
                    }
                }
            });
        } else {
            ASTUIUtil.showToast(Contants.OFFLINE_MESSAGE);
        }
    }

    private long convertDateIntoLong(String eventdate) {
        long timeInMilliseconds = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date mDate = sdf.parse(eventdate);
            timeInMilliseconds = mDate.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeInMilliseconds;
    }

    //get GetEventDetails
    private void GetEventDetails(String eventdate) {
        if (ASTUIUtil.isOnline(ShowAllEventActivity.this)) {
            final ASTProgressBar dotDialog = new ASTProgressBar(ShowAllEventActivity.this);
            dotDialog.show();
            ServiceCaller serviceCaller = new ServiceCaller(ShowAllEventActivity.this);
            final String url = Contants.BASE_URL + Contants.GetEventDetails + "username=" + UserId + "&" + "id=" + eventdate;
            serviceCaller.CallCommanServiceMethod(url, "GetEventDetails", new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String result, boolean isComplete) {
                    Log.d(Contants.LOG_TAG, "GetEventDetails**" + result);
                    ArrayList<Eventdata> eventdataList = new ArrayList<Eventdata>();
                    if (isComplete) {
                        ContentData data = new Gson().fromJson(result, ContentData.class);
                        if (data != null) {
                            if (data.getEventdata() != null) {
                                for (Eventdata eventdata : data.getEventdata()) {
                                    eventdataList.add(eventdata);
                                }

                            } else {
                                Toast.makeText(ShowAllEventActivity.this, "Event detail found!", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(ShowAllEventActivity.this, "Event detail found!", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        ASTUIUtil.showToast(Contants.Error);
                    }
                    if (dotDialog.isShowing()) {
                        dotDialog.dismiss();
                    }
                    EventDetailAdapter adapter = new EventDetailAdapter(ShowAllEventActivity.this, eventdataList);
                    eventrecycler_view.setAdapter(adapter);
                }
            });
        } else {
            ASTUIUtil.showToast(Contants.OFFLINE_MESSAGE);
        }
    }
}
