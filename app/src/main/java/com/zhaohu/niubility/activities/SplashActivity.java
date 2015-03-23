package com.zhaohu.niubility.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.zhaohu.niubility.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wen on 3/14/15.
 */
public class SplashActivity extends Activity
{
    private static final long DELAY = 3000;
    private boolean scheduled = false;
    private Timer splashTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        splashTimer = new Timer();
        splashTimer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                SplashActivity.this.finish();
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
        }, DELAY);
        scheduled = true;
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (scheduled)
            splashTimer.cancel();
        splashTimer.purge();
    }
}