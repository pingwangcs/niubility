package com.zhaohu.niubility.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.widget.GridView;

import com.zhaohu.niubility.R;
import com.zhaohu.niubility.client.PhotoWallListener;
import com.zhaohu.niubility.client.ZhaoHuClient;
import com.zhaohu.niubility.results.PhotoItem;
import com.zhaohu.niubility.results.PhotoWallAdapter;

import java.util.List;

/**
 * Created by wen on 2/13/15.
 */
public class PhotoWallActivity extends Activity {
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.photo_wall_layout);
        url = getIntent().getStringExtra("URL");

        final GridView photoGridView = (GridView) findViewById(R.id.photo_wall);

        final PhotoWallAdapter adapter = new PhotoWallAdapter(this);

        ZhaoHuClient client = ZhaoHuClient.getInstance(this);

        client.addPhotoWallListener(new PhotoWallListener() {
            @Override
            public void update(List<PhotoItem> results) {
                adapter.setData(results);
                photoGridView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });

        client.fetchPhotoWall(url);

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
}
