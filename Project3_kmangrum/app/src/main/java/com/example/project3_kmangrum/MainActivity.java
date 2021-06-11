package com.example.project3_kmangrum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Notes> myNotes = new ArrayList<Notes>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        populateMyNotes();
        populateMainActivity();


    }

    private void populateMainActivity() {
        ArrayAdapter<Notes> myAdapter = new myCustomListAdapter();
    }

    private void populateMyNotes() {

    }

    private class myCustomListAdapter extends ArrayAdapter<Notes> {

        public myCustomListAdapter() {
            super(MainActivity.this, resource, myNotes);
        }
    }
}