package com.zhaohu.niubility.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.Toast;

import com.zhaohu.niubility.R;
import com.zhaohu.niubility.client.clients.ZhaoHuClient;
import com.zhaohu.niubility.client.listeners.ResultsListener;
import com.zhaohu.niubility.results.items.PhotoItem;
import com.zhaohu.niubility.results.adapters.PhotoWallAdapter;
import com.zhaohu.niubility.types.EventsType;

import java.util.List;

/**
 * Created by wen on 2/13/15.
 */
public class PhotoWallActivity extends Activity {
    private String url;
    private GridView photoGridView;
    private PhotoWallAdapter adapter;
    private ZhaoHuClient client;

    private int visibleFirstIndex = 0;
    private int visibleLastIndex = 0;   //最后的可视项索引
    private int visibleItemCount;       // 当前窗口可见项总数

    private Boolean hasMoreResults = true;

    private final static EventsType EVENT_TYPE = EventsType.PHOTO_WALL_EVENTS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.photo_wall_layout);
        url = getIntent().getStringExtra("URL");

        photoGridView = (GridView) findViewById(R.id.photo_wall);
        photoGridView.setOnScrollListener(new PhotoWallResultsOnScrollListener());

        adapter = new PhotoWallAdapter(this);

        client = ZhaoHuClient.getInstance(this);

        client.fetchResult(url, new PhotoWallResultsResponseListener());

        Log.w("wztw", "url:"+url);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    TaskStackBuilder.create(this)
                            .addNextIntentWithParentStack(upIntent)
                            .startActivities();
                } else {
                    upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }
        return true;
    }

    private class PhotoWallResultsResponseListener implements ResultsListener<PhotoItem> {
        @Override
        public void update(List<PhotoItem> results) {

            adapter.setData(results);
            photoGridView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            synchronized (hasMoreResults) {
                if (hasMoreResults) {
                    client.loadMore(EVENT_TYPE, adapter.getCount());
                }
            }


        }

        @Override
        public void whenNoMoreResults() {
            synchronized (hasMoreResults) {
                hasMoreResults = false;
            }
        }
    }

    private class PhotoWallResultsOnScrollListener implements AbsListView.OnScrollListener {
        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleCount, int totalItemCount) {
            visibleFirstIndex = firstVisibleItem;
            visibleItemCount = visibleCount;
            visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            int itemsLastIndex = adapter.getCount() - 1;    //数据集最后一项的索引
            int lastIndex = itemsLastIndex + 1;             //加上底部的loadMoreView项
            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && visibleLastIndex == lastIndex - 1) {
                //如果是自动加载,可以在这里放置异步加载数据的代码
                Log.w("wztw", "visibleLastIndex:" + visibleLastIndex + " visibleItemCount:" + visibleItemCount + " visibleFirstIndex:" + visibleFirstIndex);
                if(hasMoreResults) {
                    Log.w("wztw", "has more will load");

                } else if (photoGridView.getChildAt(photoGridView.getChildCount() - 1).getBottom() <= photoGridView.getHeight()) {
                    Log.w("wztw", "Bottom");
                    Toast.makeText(PhotoWallActivity.this, "数据全部加载完成，没有更多数据！", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }
}
