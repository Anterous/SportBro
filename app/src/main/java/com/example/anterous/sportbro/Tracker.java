package com.example.anterous.sportbro;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Tracker extends Fragment {

    private Handler handler;
    MyLocationListener myLocationListener;
    TextView speedText;

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

        myLocationListener= new MyLocationListener();
        speedText = getView().findViewById(R.id.cur_speed);

        handler=new Handler();

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
        double speed = myLocationListener.getSpeed();
        // Log.e("SPEEDTEST", "StartSpeedMonitor: " + String.valueOf(speed));
        speedText.setText(String.valueOf(speed));
    }

}
