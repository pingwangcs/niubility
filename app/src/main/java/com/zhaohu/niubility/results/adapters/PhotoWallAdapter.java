package com.zhaohu.niubility.results.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.android.volley.toolbox.NetworkImageView;
import com.zhaohu.niubility.R;
import com.zhaohu.niubility.activities.ImageDetailsActivity;
import com.zhaohu.niubility.client.clients.ZhaoHuClient;
import com.zhaohu.niubility.results.items.PhotoItem;

import java.util.List;

/**
 * Created by wen on 1/14/15.
 */
public class PhotoWallAdapter extends BaseAdapter{
    Context context;
    LayoutInflater mInflater;
    List<PhotoItem> events;

    public PhotoWallAdapter(Context context) {
        this.context = context;
        mInflater = ((Activity)context).getLayoutInflater();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public PhotoItem getItem(int position) {
        return events.get(position);
    }

    @Override
    public int getCount() {
        return events.size();
    }

    public void setData(List<PhotoItem> events) {
        this.events = events;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        PhotoItemHolder holder = null;

        if(row == null)
        {
            row = mInflater.inflate(R.layout.photo_layout, parent, false);

            holder = new PhotoItemHolder();
            holder.image = (NetworkImageView) row.findViewById(R.id.image);

            row.setTag(holder);
        }
        else
        {
            holder = (PhotoItemHolder)row.getTag();
        }

        PhotoItem photoItem = events.get(position);

        ZhaoHuClient client = ZhaoHuClient.getInstance(context);
        client.setNetworkImage(photoItem.imageUrl, holder.image);

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ImageDetailsActivity.class);
                intent.putExtra("INDEX", position);
                intent.putExtra("IMAGE_ARRAY", getImageDetailUrlArray());
                context.startActivity(intent);
            }
        });


        return row;
    }


    static class PhotoItemHolder {
        NetworkImageView image;
    }

    private String[] getImageDetailUrlArray() {
        String[] result = new String[getCount()];
        for(int i=0; i<getCount(); i++) {
            result[i] = events.get(i).imageDetailUrl;
        }
        return result;
    }
}

