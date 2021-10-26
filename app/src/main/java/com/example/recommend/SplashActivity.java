package com.example.recommend;

/**
 * Created by Haoran Lin on 2021/10/26.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class SplashActivity extends AppCompatActivity {

    /**
     * 1.delay2000ms
     * 2.if first use?
     */

    private TextView tv_splash;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1001:
//                    //if first use this app
//                    if (isFirst()) {
                        startActivity(new Intent(SplashActivity.this, GuideActivity.class));
//                    } else {
//                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
//                    }
                    finish();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initView();
    }

    private void initView() {
        //delay2000ms
        handler.sendEmptyMessageDelayed(1001, 2000);

    }

    //if first
//    private boolean isFirst() {
//        boolean isFirst = getBoolean(this,"isFirst",true);
//        if(isFirst){
//            putBoolean(this,"isFirst",false);
//            //first
//            return true;
//        }else {
//            return false;
//        }
//
//    }

    public static void putBoolean(Context mContext,String key,boolean value){
        SharedPreferences sp = mContext.getSharedPreferences("config",Context.MODE_PRIVATE);
        sp.edit().putBoolean(key,value).commit();
    }

    public static boolean getBoolean(Context mContext, String key, boolean defValue){
        SharedPreferences sp = mContext.getSharedPreferences("config",Context.MODE_PRIVATE);
        return sp.getBoolean(key,defValue);
    }

}
