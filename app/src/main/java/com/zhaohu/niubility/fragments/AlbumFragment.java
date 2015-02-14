package com.zhaohu.niubility.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zhaohu.niubility.R;
import com.zhaohu.niubility.activities.PhotoWallActivity;
import com.zhaohu.niubility.activities.WebViewActivity;
import com.zhaohu.niubility.client.AlbumListener;
import com.zhaohu.niubility.client.HotResultsListener;
import com.zhaohu.niubility.client.ZhaoHuClient;
import com.zhaohu.niubility.results.AlbumAdapter;
import com.zhaohu.niubility.results.AlbumItem;
import com.zhaohu.niubility.results.EventItem;
import com.zhaohu.niubility.results.EventResultsListAdapter;

import java.util.List;


public class AlbumFragment extends Fragment{
    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mContext = container.getContext();
        View view = inflater.inflate(R.layout.album_layout, container, false);

        final ListView resultsListView = (ListView) view.findViewById(R.id.album_list);

        final AlbumAdapter adapter = new AlbumAdapter(mContext);


        ZhaoHuClient client = ZhaoHuClient.getInstance(mContext);

        client.addAlbumListener(new AlbumListener() {
            @Override
            public void update(List<AlbumItem> results) {
                adapter.setData(results);
                resultsListView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });

        client.fetchAlbums();

        resultsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = adapter.getItem(position).url;

                Intent intent = new Intent(mContext, PhotoWallActivity.class);
                intent.putExtra("URL", url);
                startActivity(intent);
            }
        });
        return view;
    }
}
