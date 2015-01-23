package com.zhaohu.niubility.results;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhaohu.niubility.R;
import com.zhaohu.niubility.client.ZhaoHuClient;

import java.util.Enumeration;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;

/**
 * Created by wen on 1/14/15.
 */
public class EventResultsListAdapter extends BaseAdapter{
    Context context;
    LayoutInflater mInflater;
    List<EventItem> events;

    public EventResultsListAdapter(Context context) {
        this.context = context;
        mInflater = ((Activity)context).getLayoutInflater();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public EventItem getItem(int position) {
        return events.get(position);
    }

    @Override
    public int getCount() {
        return events.size();
    }

    public void setData(List<EventItem> events) {
        this.events = events;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        EventItemHolder holder = null;

        if(row == null)
        {
            row = mInflater.inflate(R.layout.results_item_layout, parent, false);

            holder = new EventItemHolder();
            holder.image = (ImageView) row.findViewById(R.id.image);
            holder.title = (TextView)row.findViewById(R.id.title);
            holder.owner = (TextView) row.findViewById(R.id.owner);
            holder.startTime = (TextView)row.findViewById(R.id.start_time);
            holder.endTime = (TextView)row.findViewById(R.id.end_time);
            holder.address = (TextView)row.findViewById(R.id.address);

            row.setTag(holder);
        }
        else
        {
            holder = (EventItemHolder)row.getTag();
        }

        EventItem event = events.get(position);
        holder.title.setText(StringEscapeUtils.unescapeHtml(event.title));
        holder.owner.setText("发起人: "+event.owner);
        holder.startTime.setText("开始时间: "+event.startTime);
        holder.endTime.setText("结束时间: "+event.endTime);
        holder.address.setText("地点: "+event.address);

        ZhaoHuClient client = ZhaoHuClient.getInstance(context);
        client.loadImage(event.imageUrl, holder.image);

        return row;
    }


    static class EventItemHolder {
        ImageView image;
        TextView title;
        TextView owner;
        TextView startTime;
        TextView endTime;
        TextView address;
    }
}

