package com.apitechnosoft.ipad.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.model.Eventdata;
import com.apitechnosoft.ipad.model.EventotdataList;
import com.apitechnosoft.ipad.utils.FontManager;

import java.util.ArrayList;

public class ShowSameDateAllEvent extends RecyclerView.Adapter<ShowSameDateAllEvent.MyViewHolder> {

    private ArrayList<EventotdataList> eventdataList;
    Context mContext;
    Typeface materialdesignicons_font;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView eventname, eventdate;


        public MyViewHolder(View view) {
            super(view);
            eventname = view.findViewById(R.id.eventname);
            eventdate = view.findViewById(R.id.eventdate);
        }
    }


    public ShowSameDateAllEvent(Context mContext, ArrayList<EventotdataList> eventdataList) {
        this.eventdataList = eventdataList;
        this.mContext = mContext;
        materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(mContext, "fonts/materialdesignicons-webfont.otf");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.same_date_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.eventname.setText(eventdataList.get(position).getEventname());
        //holder.eventdate.setText("Date: "+eventdataList.get(position).getFromdate());

    }

    @Override
    public int getItemCount() {
        return eventdataList.size();
    }

}





