package com.example.recommend.application;

import android.app.Application;
import android.location.Address;

public class MyApplication extends Application {
    private Boolean IsShakeUndoOn;
    private Address currentLocation;

    @Override
    public void onCreate() {
        super.onCreate();
        setShakeUndoOn(false);
        currentLocation = null;
    }
    public Boolean getShakeUndoOn() {
        return IsShakeUndoOn;
    }

    public void setShakeUndoOn(Boolean shakeUndoOn) {
        IsShakeUndoOn = shakeUndoOn;
    }

    public Address getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Address currentLocation) {
        this.currentLocation = currentLocation;
    }
}
