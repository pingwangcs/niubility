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
import com.zhaohu.niubility.client.clients.ZhaoHuClient;
import com.zhaohu.niubility.client.listeners.ResultsListener;
import com.zhaohu.niubility.results.adapters.AlbumAdapter;
import com.zhaohu.niubility.results.items.AlbumItem;
import com.zhaohu.niubility.types.EventsType;

import java.util.List;


public class AlbumFragment extends Fragment{
    private Context mContext;
    private ListView resultsListView;
    private AlbumAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mContext = container.getContext();
        View view = inflater.inflate(R.layout.album_layout, container, false);

        resultsListView = (ListView) view.findViewById(R.id.album_list);

        adapter = new AlbumAdapter(mContext);

        ZhaoHuClient client = ZhaoHuClient.getInstance(mContext);

        client.fetchResult(EventsType.ALBUM_EVENTS, new AlbumResultsResponseListener());

        resultsListView.setOnItemClickListener(new AlbumResultsOnItemClickListener());

        return view;
    }

    private class AlbumResultsResponseListener implements ResultsListener<AlbumItem> {
        @Override
        public void update(List<AlbumItem> results) {
            adapter.setData(results);
            resultsListView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

        @Override
        public void whenNoMoreResults() {

        }
    }

    private class AlbumResultsOnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String url = adapter.getItem(position).url;

            Intent intent = new Intent(mContext, PhotoWallActivity.class);
            intent.putExtra("URL", url);
            startActivity(intent);
        }
    }
}
