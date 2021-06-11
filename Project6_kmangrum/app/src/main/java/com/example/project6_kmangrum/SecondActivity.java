package com.example.project6_kmangrum;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SecondActivity extends AppCompatActivity {

//  Create variables for that will be used later
    ListView listView;
    String[] separateEvents;
    List<String> myEvents = new ArrayList<>();
    EditText etDateFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

//        Get the listview from the layout
        listView = findViewById(R.id.listViewcustom);

//      Read in the myEvents file using readFromFile method
        String events = readFromFile(this);

//        Split the string at newlines
        separateEvents = events.split("\n");

//      Loop through string array and remove coordinates so that they are not displayed in the list view
        for (int i = 0; i < separateEvents.length; i++) {
            int index = separateEvents[i].indexOf("^");
            separateEvents[i] = separateEvents[i].substring(index+1);
        }
//        Convert the string array into a list so that it is usable for the listadapter
        Collections.addAll(myEvents, separateEvents);

//        Create the custom listadapter and provide it the events list
        MyListAdapter myAdapter = new MyListAdapter(this, myEvents);
        ListView listView = findViewById(R.id.listViewcustom);
        listView.setAdapter(myAdapter);
    }


//    OnClick function that takes user to maps activity
    public void displayMarkersInMaps(View view) {
        startActivity(new Intent(SecondActivity.this, MapsActivity.class));
    }

// Function that reads the information stored in myEvents file
    private String readFromFile(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("myEvents.txt");

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append("\n").append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }


//    Onclick function that filters the events in the listview by the data
    public void filterByDate(View view) {
        etDateFilter = findViewById(R.id.etFilterDate);

//        Get the date to filter by from editText
        String filterDate = etDateFilter.getText().toString();

//        Create arraylist to store events with matching date in
        List<String> myEventsFiltered = new ArrayList<>();

//        Loop through events in event list to compare date
        for (int i = 0; i < myEvents.size(); i++) {
            if(myEvents.get(i).contains(filterDate)) {
                myEventsFiltered.add(myEvents.get(i));
            }
        }

//        If there are no matching dates, display toast saying "No matching events
        if (myEventsFiltered.size() == 0) {
            Toast.makeText(this, "No matching Events", Toast.LENGTH_SHORT).show();
        }

//        Display filtered events in listview
        MyListAdapter myAdapter = new MyListAdapter(this, myEventsFiltered);
        ListView listView = findViewById(R.id.listViewcustom);
        listView.setAdapter(null);
        listView.setAdapter(myAdapter);
    }
}


