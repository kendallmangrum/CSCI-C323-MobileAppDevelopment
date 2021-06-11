package com.example.project4_kmangrum;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PortraitFragment1 extends Fragment implements View.OnClickListener {
    Button editBtn;
    Button saveBtn;
    EditText textName;
    EditText textLocation;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//Inflate the view
        View myView = inflater.inflate(R.layout.fragment1_layout, container, false);

//        Insert the ImageView into the layout
        ImageView profileImage = (ImageView) myView.findViewById(R.id.profileIcon);
        profileImage.setImageResource(R.drawable.userimage);

//        Get the edit and save buttons and add onCLickListeners to them
//        Get the EditTexts for Name and Location
        editBtn = (Button) myView.findViewById(R.id.editButton);
        editBtn.setOnClickListener(this);
        textName = (EditText) myView.findViewById(R.id.editTextName);
        textLocation = (EditText) myView.findViewById(R.id.editTextLocation);
        saveBtn = (Button) myView.findViewById(R.id.saveButton);
        saveBtn.setOnClickListener(this);

        return myView;


    }

//    OnClick function that determines whether the save or edit button is clicked
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
//            If the edit button is clicked, allow the editTexts for name and location to be edited
            case R.id.editButton:
                textName.setFocusable(true);
                textName.setClickable(true);
                textName.setCursorVisible(true);
                textName.setFocusableInTouchMode(true);

                textLocation.setFocusable(true);
                textLocation.setClickable(true);
                textLocation.setCursorVisible(true);
                textLocation.setFocusableInTouchMode(true);

                break;

//            If the save button is clicked, disable the ability to edit the EditTexts of Name and Location
            case R.id.saveButton:
                textName.setFocusable(false);
                textName.setClickable(false);
                textName.setCursorVisible(false);
                textName.setFocusableInTouchMode(false);

                textLocation.setFocusable(false);
                textLocation.setClickable(false);
                textLocation.setCursorVisible(false);
                textLocation.setFocusableInTouchMode(false);
        }



    }

}
