package com.example.project6_kmangrum;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MyListAdapter extends ArrayAdapter<String> {

//    Create variables that I will need later
    private final Activity context;
    private List<String> myEvents;

    public MyListAdapter(Activity context, List<String> myEvents) {
        super(context, R.layout.custom_event_listview, myEvents);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.myEvents = myEvents;

    }


    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        view = inflater.inflate(R.layout.custom_event_listview, parent, false);     // Inflate the customListview

//        Get teh textview and deletebutton from layout
        TextView tvEvent = view.findViewById(R.id.tvEvent);
        tvEvent.setText(myEvents.get(position));
        Button deleteButton = view.findViewById(R.id.bRemove);

//      Set an onclick listener on delete button to remove listview item
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
//                When clicked, remove item at that position
                myEvents.remove(position);
                notifyDataSetChanged();

                if (myEvents.size() == 0) {
                    clearFile();
                } else {
                    writeToFile(myEvents, context);
                }
            }
        });
        return view;
    }

//  Update the myEvents file so that deleted events no longer are stored
    private void writeToFile(List data, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("myEvents.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(String.valueOf(data));
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

//    fucntion clear file to try and completely clear file
    private void clearFile() {
        String string1 = "";
        FileOutputStream fos;
        try {
            fos = new FileOutputStream("data/user/0/com.example.project6_kmangrum/files/myEvents.txt", false);
            FileWriter fWriter;

            try {
                fWriter = new FileWriter(fos.getFD());
                fWriter.write(string1);
                fWriter.flush();
                fWriter.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                fos.getFD().sync();
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

