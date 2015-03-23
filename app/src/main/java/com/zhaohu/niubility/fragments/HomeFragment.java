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
import android.widget.Toast;

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
    private final static EventsType EVENT_TYPE = EventsType.HOME_EVENTS;

    private Context mContext;
    private ListView mResultsListView;
    private EventResultsListAdapter mAdapter;

    private int visibleFirstIndex = 0;

    private int visibleLastIndex = 0;   //最后的可视项索引
    private int visibleItemCount;       // 当前窗口可见项总数

    private View mMainLoadingSpinner;
    private View mBottomLoadMoreSpinner;

    private View mFootView;

    private ZhaoHuClient client;

    private boolean hasMoreResults = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mContext = container.getContext();

        View homeView = inflater.inflate(R.layout.home_page_layout, container, false);
        mMainLoadingSpinner = homeView.findViewById(R.id.spinner);
        mMainLoadingSpinner.setVisibility(View.VISIBLE);

        mFootView = inflater.inflate(R.layout.spinner_layout, null);
        mBottomLoadMoreSpinner = mFootView.findViewById(R.id.spinner);
        mBottomLoadMoreSpinner.setVisibility(View.VISIBLE);

        mResultsListView = (ListView) homeView.findViewById(R.id.results_list);
        mAdapter = new EventResultsListAdapter(mContext, EVENT_TYPE);

        mResultsListView.setOnItemClickListener(new HomeResultsOnItemClickListener());
        mResultsListView.setOnScrollListener(new HomeResultsOnScrollListener());
        mResultsListView.addFooterView(mFootView);

        client = ZhaoHuClient.getInstance(mContext);
        client.fetchResult(EVENT_TYPE, new HomeResultsResponseListener());
        return homeView;
    }

    private class HomeResultsResponseListener implements ResultsListener<EventItem> {
        @Override
        public void update(List<EventItem> results) {

            int index = mResultsListView.getFirstVisiblePosition();
            View v = mResultsListView.getChildAt(0);
            int top = (v == null) ? 0 : (v.getTop() - mResultsListView.getPaddingTop());

            mAdapter.setData(results);
            mResultsListView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();

            mResultsListView.setSelectionFromTop(index, top);

            mMainLoadingSpinner.setVisibility(View.GONE);
        }

        @Override
        public void whenNoMoreResults() {
            mResultsListView.removeFooterView(mFootView);
            hasMoreResults = false;
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
            visibleFirstIndex = firstVisibleItem;
            visibleItemCount = visibleCount;
            visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            int itemsLastIndex = mAdapter.getCount() - 1;    //数据集最后一项的索引
            int lastIndex = itemsLastIndex + 1;             //加上底部的loadMoreView项
            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && visibleLastIndex == lastIndex - 1) {
                //如果是自动加载,可以在这里放置异步加载数据的代码
                if(hasMoreResults) {
                    client.loadMore(EVENT_TYPE, mAdapter.getCount());
                } else if (mResultsListView.getChildAt(mResultsListView.getChildCount() - 1).getBottom() <= mResultsListView.getHeight()) {
                    Toast.makeText(mContext, "数据全部加载完成，没有更多数据！", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }
}
