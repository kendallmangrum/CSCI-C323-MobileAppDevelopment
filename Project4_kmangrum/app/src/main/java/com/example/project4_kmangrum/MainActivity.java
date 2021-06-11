package com.example.project4_kmangrum;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements PortraitFragment2.myFragmentInterface{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

// In office hours, the AI had me implement my fragments not using fragment transaction.

//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        Configuration configuration = getResources().getConfiguration();
//        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            BlankFragment1 blankFragment1 = new BlankFragment1();
//            fragmentTransaction.replace(R.id.layout_main, blankFragment1);
//        }
//        fragmentTransaction.commit();

    }

//    Function that adds the task information to the main Activity depending on which priority of task is selected
    @Override
    public void priorityChange(String tasks) {
//        Get all of the textviews in teh activity
        TextView textViewHeading = findViewById(R.id.textViewHeading);
        TextView listItem1 = findViewById(R.id.textViewItem1);
        TextView listItem2 = findViewById(R.id.textViewItem2);
        TextView listItem3 = findViewById(R.id.textViewItem3);
//        Get the window so that we can change the background color depending on what priority is selected
        View view = this.getWindow().getDecorView();

//        If the high priority is selected, change the heading, list items, and background color to reflect that
        if (tasks.equals("High")) {
            textViewHeading.setText("High Priority Tasks");
            listItem1.setText("1) Complete Assignment");
            listItem2.setText("2) Laundry");
            listItem3.setText("3) Complete the test");
            view.setBackgroundColor(Color.RED);

//        If the low priority is selected, change the heading, list items, and background color to reflect that
        } else if (tasks.equals("Low")) {
            textViewHeading.setText("Low Priority Tasks");
            listItem1.setText("1) Shopping");
            listItem2.setText("2) Movie");
            listItem3.setText("");
            view.setBackgroundColor(Color.YELLOW);

//        If the other priority is selected, change the heading, list items, and background color to reflect that
        } else {
            textViewHeading.setText("Other Tasks");
            listItem1.setText("1) Visit parents");
            listItem2.setText("2) Watch Netflix");
            listItem3.setText("3) Cook Brownies");
            view.setBackgroundColor(Color.GREEN);
        }
    }
}