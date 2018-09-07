package com.apitechnosoft.ipad.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.model.Commentdata;
import com.apitechnosoft.ipad.model.Eventdata;
import com.apitechnosoft.ipad.utils.FontManager;

import java.util.ArrayList;

public class EventDetailAdapter extends RecyclerView.Adapter<EventDetailAdapter.MyViewHolder> {

    private ArrayList<Eventdata> eventdataList;
    Context mContext;
    Typeface materialdesignicons_font;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView eventname, eventtype, fromdate, todate, eventdes;


        public MyViewHolder(View view) {
            super(view);
            eventname = view.findViewById(R.id.eventname);
            eventtype = view.findViewById(R.id.eventtype);
            fromdate = view.findViewById(R.id.fromdate);
            todate = view.findViewById(R.id.todate);
            eventdes = view.findViewById(R.id.eventdes);
        }
    }


    public EventDetailAdapter(Context mContext, ArrayList<Eventdata> eventdataList) {
        this.eventdataList = eventdataList;
        this.mContext = mContext;
        materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(mContext, "fonts/materialdesignicons-webfont.otf");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_detail_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.eventname.setText(eventdataList.get(position).getEventname());
        holder.eventtype.setText(eventdataList.get(position).getType());
        holder.fromdate.setText("From: " + eventdataList.get(position).getFromdate() + " " + eventdataList.get(position).getFromDateTime());
        holder.todate.setText("To: " + eventdataList.get(position).getTodate() + " " + eventdataList.get(position).getToDateTime());
        holder.eventdes.setText(eventdataList.get(position).getEventDescription());
    }

    @Override
    public int getItemCount() {
        return eventdataList.size();
    }

}




