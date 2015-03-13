package com.zhaohu.niubility.results.adapters;

/**
 * Created by wen on 2/11/15.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.zhaohu.niubility.R;
import com.zhaohu.niubility.client.clients.ZhaoHuClient;
import com.zhaohu.niubility.results.items.AlbumItem;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by wen on 1/14/15.
 */
public class AlbumAdapter extends BaseAdapter {
    Context context;
    LayoutInflater mInflater;
    List<AlbumItem> albums;

    ArrayList<Integer> tempImages = new ArrayList<Integer>();

    public AlbumAdapter(Context context) {
        this.context = context;
        mInflater = ((Activity)context).getLayoutInflater();

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public AlbumItem getItem(int position) {
        return albums.get(position);
    }

    @Override
    public int getCount() {
        return albums.size();
    }

    public void setData(List<AlbumItem> albums) {
        this.albums = albums;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        AlbumItemHolder holder = null;

        if(row == null)
        {
            row = mInflater.inflate(R.layout.album_item_layout, parent, false);

            holder = new AlbumItemHolder();
            holder.image = (NetworkImageView) row.findViewById(R.id.image);
//            holder.image = (ImageView) row.findViewById(R.id.image);
            holder.title = (TextView)row.findViewById(R.id.title);
            holder.time = (TextView) row.findViewById(R.id.time);
//            holder.startTime = (TextView)row.findViewById(R.id.start_time);
//            holder.endTime = (TextView)row.findViewById(R.id.end_time);
//            holder.address = (TextView)row.findViewById(R.id.address);

            row.setTag(holder);
        }
        else
        {
            holder = (AlbumItemHolder)row.getTag();
        }

        AlbumItem album = albums.get(position);
        holder.title.setText(album.title);
//        holder.title.setText(StringEscapeUtils.unescapeHtml(event.title));

        ZhaoHuClient client = ZhaoHuClient.getInstance(context);
        client.setNetworkImage(album.cover_url, holder.image);

        return row;
    }


    static class AlbumItemHolder {
        NetworkImageView image;
        TextView title;
        TextView time;
    }
}

