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

import com.zhaohu.niubility.R;

import org.apache.http.cookie.Cookie;

/**
 * Created by wen on 1/31/15.
 */
public class WebViewActivity extends Activity {
    private final static String DOMAIN = "http://51zhaohu.com";
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
//        getActionBar().setIcon(android.R.color.transparent);
        setContentView(R.layout.web_view_layout);
        url = getIntent().getStringExtra("URL");

        WebView webView = (WebView) findViewById(R.id.web_view);

        final CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(this);
        final CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();

        webView.loadUrl(url);
        cookieManager.setCookie(DOMAIN, "51zhaohu_app=123");
        cookieSyncManager.sync();


        System.out.println("Cookie:"+cookieManager.getCookie(DOMAIN));

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
