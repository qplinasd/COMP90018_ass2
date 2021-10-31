package com.example.recommend.application;

import android.app.Application;

public class MyApplication extends Application {
    private Boolean IsShakeUndoOn;

    @Override
    public void onCreate() {
        super.onCreate();
        setShakeUndoOn(false);
    }
    public Boolean getShakeUndoOn() {
        return IsShakeUndoOn;
    }

    public void setShakeUndoOn(Boolean shakeUndoOn) {
        IsShakeUndoOn = shakeUndoOn;
    }
}
