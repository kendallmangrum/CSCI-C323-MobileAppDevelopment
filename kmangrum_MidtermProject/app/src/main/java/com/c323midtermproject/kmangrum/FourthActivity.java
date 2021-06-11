package com.c323midtermproject.kmangrum;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class FourthActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth);

        ArrayList<DestinationItem> destinationList = new ArrayList<>();
//        destinationList.add(new DestinationItem("Indianapolis", "Summer", "Indianapolis, Indiana"));
        try {
            JSONArray data = new JSONArray(readJSONDataFromFile("favorites"));
            for (int i = 0; i < data.length(); i++) {
                JSONObject obj = data.getJSONObject(i);
                destinationList.add(new DestinationItem(obj.getString("Name"), obj.getString("Time"), obj.getString("Location")));
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }


        RecyclerView recyclerList = (RecyclerView) findViewById(R.id.recyclerCardList);
        recyclerList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
//        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerList.setLayoutManager(llm);

        mAdapter = new DestinationAdapter(destinationList);
        recyclerList.setAdapter(mAdapter);


    }

    public String readJSONDataFromFile(String fileName) throws IOException, JSONException {
        FileInputStream fis = openFileInput(fileName);
        BufferedInputStream bis = new BufferedInputStream(fis);
        StringBuilder b = new StringBuilder();
        while (bis.available() != 0) {
            b.append((char) bis.read());
        }
        bis.close();
        bis.close();

        return b.toString();
    }
}