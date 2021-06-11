package com.c323midtermproject.kmangrum;

import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

public class PopUp {

    //PopupWindow display method

    public void showPopupWindow(final View view) {


        //Create a View object yourself through inflater
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.city_cardview_layout, null);

        //Specify the length and width through constants
        int width = ConstraintLayout.LayoutParams.MATCH_PARENT;
        int height = ConstraintLayout.LayoutParams.MATCH_PARENT;

        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = true;

        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        //Initialize the elements of our window, install the handler

        TextView textView1 = popupView.findViewById(R.id.tv1);
        textView1.setText("Test");
        TextView textView2 = popupView.findViewById(R.id.tv3);
        textView2.setText("Test");
        TextView textView3 = popupView.findViewById(R.id.tv2);
        textView3.setText("Test");
        TextView textView4 = popupView.findViewById(R.id.tv2);
        textView4.setText("Test");


        Button addFavorites = popupView.findViewById(R.id.favoriteButton);
        addFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //As an example, display the message
                Toast.makeText(view.getContext(), "Wow, add favorites action button", Toast.LENGTH_SHORT).show();



            }
        });

        Button mapDisplay = popupView.findViewById(R.id.displayMapButton);
        mapDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //As an example, display the message
                Toast.makeText(view.getContext(), "Wow, display maps action button", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(v.getContext(), MapsActivity.class);
                v.getContext().startActivity(i);
            }
        });



        //Handler for clicking on the inactive zone of the window
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                //Close the window when clicked
                popupWindow.dismiss();
                return true;
            }
        });
    }

}
