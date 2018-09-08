package com.apitechnosoft.ipad.calendarview;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.apitechnosoft.ipad.R;

public class ListViewActivity extends Activity implements OnClickListener {

    private ListView lv_android;
    private AndroidListAdapter list_adapter;
    private Button btn_calender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        CalendarCollection.date_collection_arr=new ArrayList<CalendarCollection>();
        CalendarCollection.date_collection_arr.add(new CalendarCollection("2018-04-01","John Birthday"));
        CalendarCollection.date_collection_arr.add(new CalendarCollection("2018-04-04","Client Meeting at 5 p.m."));
        CalendarCollection.date_collection_arr.add(new CalendarCollection("2018-04-06","A Small Party at my office"));
        CalendarCollection.date_collection_arr.add(new CalendarCollection("2018-05-02","Marriage Anniversary"));
        CalendarCollection.date_collection_arr.add(new CalendarCollection("2018-04-11","Live Event and Concert of sonu"));


        getWidget();
    }



    public void getWidget(){
        btn_calender = (Button) findViewById(R.id.btn_calender);
        btn_calender.setOnClickListener(this);

        lv_android = (ListView) findViewById(R.id.lv_android);
        list_adapter=new AndroidListAdapter(ListViewActivity.this,R.layout.list_item, CalendarCollection.date_collection_arr);
        lv_android.setAdapter(list_adapter);

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_calender:
                startActivity(new Intent(ListViewActivity.this,CalenderActivity.class));

                break;

            default:
                break;
        }

    }

}

