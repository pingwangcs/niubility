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
import com.zhaohu.niubility.activities.WebViewActivity;
import com.zhaohu.niubility.client.HotResultsListener;
import com.zhaohu.niubility.client.ZhaoHuClient;
import com.zhaohu.niubility.results.EventItem;
import com.zhaohu.niubility.results.EventResultsListAdapter;

import java.util.List;

/**
 * Created by wen on 1/11/15.
 */
public class HotEventsFragment extends Fragment{
    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mContext = container.getContext();
        View view = inflater.inflate(R.layout.hot_page_layout, container, false);

        final ListView resultsListView = (ListView) view.findViewById(R.id.results_list);

        final EventResultsListAdapter adapter = new EventResultsListAdapter(mContext, EventsFragment.HOT_EVENTS_FRAGMENT);

        ZhaoHuClient client = ZhaoHuClient.getInstance(mContext);

        client.addHotResultsListener(new HotResultsListener() {
            @Override
            public void update(List<EventItem> results) {
                adapter.setData(results);
                resultsListView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });

        client.fetchHotResults();

        resultsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = adapter.getItem(position).webViewUrl;

                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra("URL", url);
                startActivity(intent);


            }
        });
        return view;
    }
}
