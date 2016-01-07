package org.mf.startupadpage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends Activity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isFirst();

            }
        }, 1000);
    }

    private void init() {
        setContentView(R.layout.activity_appstart);
    }

    private void isFirst() {
        if (!AdPreference.getInstance().getStartupAdPage().getPicUrl().equals("")) {
            Intent intent = new Intent(SplashActivity.this,
                    StartupAdPageActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(SplashActivity.this,
                    MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

}