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
import com.zhaohu.niubility.client.clients.ZhaoHuClient;
import com.zhaohu.niubility.client.listeners.ResultsListener;
import com.zhaohu.niubility.results.items.EventItem;
import com.zhaohu.niubility.results.adapters.EventResultsListAdapter;
import com.zhaohu.niubility.types.EventsType;

import java.util.List;

/**
 * Created by wen on 1/11/15.
 */
public class HotEventsFragment extends Fragment{
    private Context mContext;
    private ListView resultsListView;
    private EventResultsListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mContext = container.getContext();
        View view = inflater.inflate(R.layout.hot_page_layout, container, false);

        resultsListView = (ListView) view.findViewById(R.id.results_list);

        adapter = new EventResultsListAdapter(mContext, EventsType.HOT_EVENTS);

        ZhaoHuClient client = ZhaoHuClient.getInstance(mContext);

        client.fetchResult(EventsType.HOT_EVENTS, new HotResultsResponseListener());

        resultsListView.setOnItemClickListener(new HotResultsOnItemClickListener());
        return view;
    }

    private class HotResultsResponseListener implements ResultsListener<EventItem> {
        @Override
        public void update(List<EventItem> results) {
            adapter.setData(results);
            resultsListView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

        @Override
        public void whenNoMoreResults() {

        }
    }

    private class HotResultsOnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String url = adapter.getItem(position).webViewUrl;

            Intent intent = new Intent(mContext, WebViewActivity.class);
            intent.putExtra("URL", url);
            startActivity(intent);
        }
    }
}
