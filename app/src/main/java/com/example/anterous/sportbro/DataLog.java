package com.example.anterous.sportbro;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.anterous.sportbro.Calcs.KcalCalc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import database.handler.DatabaseHandler;
import database.model.SportEvent;

public class DataLog extends Fragment {

    private int burn = 0;
    private DatabaseHandler db;
    private List<SportEvent> eventList = new ArrayList<>();
    ArrayList<HashMap<String,String>> data = new ArrayList<>();
    private ListView lv;

    private String TAG_TYPE = "";
    private String TAG_LENGTH = "";
    private String TAG_KCAL = "";
    private String TAG_TIMESTAMP = "";

    private int length = 0;
    private String sport_type = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_datalog, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        lv = getActivity().findViewById(R.id.event_list);
        db = new DatabaseHandler(this.getContext());
        eventList.addAll(db.getAllEvents());

        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddDialog();
            }
        });
    }

    private void openAddDialog() {

        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this.getContext());
        View view = layoutInflaterAndroid.inflate(R.layout.event_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this.getActivity());
        alertDialogBuilderUserInput.setView(view);

        final Spinner spinner = (Spinner) view.findViewById(R.id.type_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.planets_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        final EditText editText = view.findViewById(R.id.event_length);
        final TextView textVview = view.findViewById(R.id.burned);

        editText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                int type = (int) spinner.getSelectedItemId();
                String str = editText.getText().toString();
                //Log.e("TAGGERIINO", "spinner id: " + String.valueOf(type) + "event length: " + String.valueOf(length) );
                if(!TextUtils.isEmpty(str)) {
                    int length = Integer.parseInt(editText.getText().toString());
                    calculateKcal(type, length);
                    textVview.setText(String.valueOf(burn));
                }
                else{
                    textVview.setText("0");
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });


        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        sport_type = spinner.getSelectedItem().toString();
                        createNote(sport_type, length, burn);

                    }
                })
                .setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

    }

    private void calculateKcal(int type, int length) {
        KcalCalc kcalCalc = new KcalCalc();
        kcalCalc.params(type,length);
        int burn = kcalCalc.getBurn();
        Log.e("KCAL", "calculateKcal: " + String.valueOf(burn) );
        setKcal(burn);
    }

    private void setKcal(int burn) {
        this.burn = burn;
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Data Log");
    }

    private void createNote(String event, int length, int kcal) {
        long id = db.inserEvent(event, length, kcal);

        SportEvent e = db.getEvent(id);

        if (e != null) {
            eventList.add(0, e);
        }

        for (int i =0; i < eventList.size(); i++) {
            SportEvent event1 =eventList.get(i);
            HashMap<String,String> map = new HashMap<>();

            map.put(TAG_TYPE, event1.getType());
            map.put(TAG_LENGTH, String.valueOf(event1.getLength()));
            map.put(TAG_KCAL, String.valueOf(event1.getKcal()));
            map.put(TAG_TIMESTAMP, event1.getTimestamp());
            data.add(map);
        }
        ListAdapter adapter = new SimpleAdapter(this.getContext(),
                data,
                R.layout.event_list_item,
                new String[]{TAG_TYPE , TAG_LENGTH, TAG_KCAL},
                new int[]{R.id.type_header_item, R.id.length_list_item, R.id.kcal_list_item});
        lv.setAdapter(adapter);
    }



}
