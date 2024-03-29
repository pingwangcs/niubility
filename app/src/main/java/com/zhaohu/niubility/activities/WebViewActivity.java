package com.zhaohu.niubility.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

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

//        this.getWindow().requestFeature(Window.FEATURE_PROGRESS);
//        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
//        requestWindowFeature(Window.FEATURE_PROGRESS);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.web_view_layout);

        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressbar);

        url = getIntent().getStringExtra("URL");

        WebView webView = (WebView) findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);

//        getWindow().setFeatureInt( Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON);

//        setProgressBarIndeterminateVisibility(true);
//        setProgressBarVisibility(true);


        final CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(this);
        final CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();


        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress)
            {
                //Make the bar disappear after URL is loaded, and changes string to Loading...
//                setTitle("Loading...");
//                setProgress(progress * 100); //Make the bar disappear after URL is loaded

                progressBar.setProgress(progress);

                // Return the app name after finish loading
                if(progress == 100) {
//                    setProgressBarIndeterminateVisibility(false);
//                    setProgressBarVisibility(false);

//                    setTitle(R.string.app_name);
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        webView.loadUrl(url);
        cookieManager.setCookie(DOMAIN, "51zhaohu_app=123");
        cookieSyncManager.sync();

        progressBar.setProgress(0);
        progressBar.setVisibility(View.VISIBLE);



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
