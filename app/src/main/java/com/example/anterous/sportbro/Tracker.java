package com.example.anterous.sportbro;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Tracker extends Fragment {

    Tracker context;
    private Handler handler;
    MyLocationListener myLocationListener;
    TextView speedText;
    LocationManager locationManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_tracker, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        context = this;

        myLocationListener = new MyLocationListener();
        speedText = getView().findViewById(R.id.cur_speed);

        handler = new Handler();

        Runnable r = new Runnable() {

            public void run() {
                StartSpeedMonitor();
                handler.postDelayed(this, 1000); //  delay one second before updating the number
            }
        };
        handler.postDelayed(r, 1000);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Tracker");
    }

    private void StartSpeedMonitor() {
        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, myLocationListener);
        double speed = myLocationListener.getSpeed();
        // Log.e("SPEEDTEST", "StartSpeedMonitor: " + String.valueOf(speed));
        speedText.setText(String.valueOf((float) Math.round(speed * 100) / 100) + " m/s");
    }

}
