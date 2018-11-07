package com.example.anterous.sportbro;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import java.security.PrivateKey;

import javax.crypto.spec.GCMParameterSpec;

public class MyLocationListener implements LocationListener {
    private Location mLastLocation;
    private double speed = 0;



    @Override
    public void onLocationChanged(Location pCurrentLocation) {
        //calcul manually speed
        double speed = 0;
        if (this.mLastLocation != null)
            speed = Math.sqrt(
                    Math.pow(pCurrentLocation.getLongitude() - mLastLocation.getLongitude(), 2)
                            + Math.pow(pCurrentLocation.getLatitude() - mLastLocation.getLatitude(), 2)
            ) / (pCurrentLocation.getTime() - this.mLastLocation.getTime());
        //if there is speed from location
        if (pCurrentLocation.hasSpeed())
            //get location speed
            speed = pCurrentLocation.getSpeed();
        this.mLastLocation = pCurrentLocation;
        setSpeed(speed);
    }

    private void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getSpeed(){
        return speed;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
