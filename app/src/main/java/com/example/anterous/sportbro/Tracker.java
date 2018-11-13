package com.example.anterous.sportbro;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

public class Tracker extends Fragment {

    Tracker context;
    private Handler handler;
    Runnable r;
    MyLocationListener myLocationListener;
    TextView speedText;
    TextView distanceText;
    TextView timeText;
    Button collectdata_button;
    LocationManager locationManager;
    private int MY_ACCES_LOCATION_ACCES_GRANTED = 0;
    ArrayList<String> speedList = new ArrayList<>();
    float distance = 0;
    float totalvalue = 0;
    boolean isRunning = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tracker,
                container, false);
        Button button = (Button) view.findViewById(R.id.collectdata_button);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                handler = new Handler();

                if (isRunning == false) {
                    collectdata_button.setText(R.string.end_workout);

                    isRunning = true;

                    r = new Runnable() {

                        public void run() {

                            if (isRunning == true) {
                                StartSpeedMonitor();
                                handler.postDelayed(this, 1000); //  delay one second before updating the number
                            }
                            else{
                                handler.removeCallbacks(r);
                            }
                        }
                    };

                    handler.postDelayed(r, 1000);
                }
                else{
                    collectdata_button.setText(R.string.start_workout);
                    Log.e("CLICKED", "onClick: ");
                    isRunning = false;
                    handler.removeCallbacks(r);
                    openAddDialog();
                }
            }

        });
        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        context = this;

        myLocationListener = new MyLocationListener();
        speedText = getView().findViewById(R.id.cur_speed);
        distanceText = getView().findViewById(R.id.cur_distance);
        timeText = getView().findViewById(R.id.cur_length);
        collectdata_button = getView().findViewById(R.id.collectdata_button);


    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Tracker");
    }

    private void StartSpeedMonitor() {
        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.getActivity(),
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_ACCES_LOCATION_ACCES_GRANTED);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, myLocationListener);
        double speed = myLocationListener.getSpeed();
        // Log.e("SPEEDTEST", "StartSpeedMonitor: " + String.valueOf(speed));
        String current_speed = String.valueOf((float) Math.round(speed * 100) / 100);
        speedList.add(current_speed);
        for (int i = 0; i > speedList.size(); i++){
            float val = Float.parseFloat(speedList.get(i));
            totalvalue = totalvalue + val;
        }
        Log.e("SPEEDLIST", "StartSpeedMonitor: " + speedList.toString() );
        speedText.setText( current_speed + " m/s");
        distanceText.setText(String.valueOf(totalvalue ) + " M");
        timeText.setText(String.valueOf(speedList.size()) + " Sec");
    }

    private void openAddDialog() {

        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this.getContext());
        View view = layoutInflaterAndroid.inflate(R.layout.tracker_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this.getActivity());
        alertDialogBuilderUserInput.setView(view);


        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })
                .setNegativeButton("resume",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();
    }

}
