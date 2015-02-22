package com.zhaohu.niubility.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.toolbox.JsonObjectRequest;
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
public class HomeFragment extends Fragment{
    private Context mContext;
    private ListView mResultsListView;
    private EventResultsListAdapter mAdapter;

    private int visibleLastIndex = 0;   //最后的可视项索引
    private int visibleItemCount;       // 当前窗口可见项总数

    private View mLoadingSpinner;
    private View mLoadMoreSpinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mContext = container.getContext();

        View homeView = inflater.inflate(R.layout.home_page_layout, container, false);
        mLoadingSpinner = homeView.findViewById(R.id.spinner);
        mLoadingSpinner.setVisibility(View.VISIBLE);

        View footerView = inflater.inflate(R.layout.spinner_layout, null);
        mLoadMoreSpinner = footerView.findViewById(R.id.spinner);
        mLoadMoreSpinner.setVisibility(View.VISIBLE);

        mResultsListView = (ListView) homeView.findViewById(R.id.results_list);
        mAdapter = new EventResultsListAdapter(mContext, EventsType.HOME_EVENTS);

        mResultsListView.setOnItemClickListener(new HomeResultsOnItemClickListener());
        mResultsListView.setOnScrollListener(new HomeResultsOnScrollListener());
        mResultsListView.addFooterView(footerView);

        ZhaoHuClient client = ZhaoHuClient.getInstance(mContext);
        client.fetchResult(EventsType.HOME_EVENTS, new HomeResultsResponseListener());

        return homeView;
    }

    private class HomeResultsResponseListener implements ResultsListener<EventItem> {
        @Override
        public void update(List<EventItem> results) {
            mAdapter.setData(results);
            mResultsListView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
            mLoadingSpinner.setVisibility(View.GONE);
        }
    }

    private class HomeResultsOnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String url = mAdapter.getItem(position).webViewUrl;

            Intent intent = new Intent(mContext, WebViewActivity.class);
            intent.putExtra("URL", url);
            startActivity(intent);
        }
    }

    private class HomeResultsOnScrollListener implements AbsListView.OnScrollListener {
        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleCount, int totalItemCount) {
            visibleItemCount = visibleCount;
            visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
            Log.w("wztw", "onScroll: visibleItemCount:"+visibleItemCount+" visibleLastIndex:"+visibleLastIndex);
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            int itemsLastIndex = mAdapter.getCount() - 1;    //数据集最后一项的索引
            int lastIndex = itemsLastIndex + 1;             //加上底部的loadMoreView项
            Log.w("wztw", "onScrollStateChanged:lastIndex:"+lastIndex);
            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && visibleLastIndex == lastIndex) {
                //如果是自动加载,可以在这里放置异步加载数据的代码
                Log.w("wztw", "should update listview here");
            }
        }
    }
}
