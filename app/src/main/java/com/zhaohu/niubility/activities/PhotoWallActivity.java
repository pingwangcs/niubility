package com.zhaohu.niubility.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.MenuItem;
import android.widget.GridView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.photo_wall_layout);
        url = getIntent().getStringExtra("URL");

        photoGridView = (GridView) findViewById(R.id.photo_wall);

        adapter = new PhotoWallAdapter(this);

        ZhaoHuClient client = ZhaoHuClient.getInstance(this);

        client.fetchResult(url, new PhotoWallResultsResponseListener());

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
        }

        @Override
        public void whenNoMoreResults() {

        }
    }
}
