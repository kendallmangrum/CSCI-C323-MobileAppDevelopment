package com.example.project4_kmangrum;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PortraitFragment2 extends Fragment {

    RadioGroup radioGroup;
    View view;

//    Implement fragment interface
    public interface myFragmentInterface {
        public void priorityChange(String tasks);
    }

    myFragmentInterface activityCommunicator;

// Function to attach fragment to the main activity
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            activityCommunicator = (myFragmentInterface) getActivity();
        } catch (Exception e) {
            throw new ClassCastException("Activity must implement myFragmentInterface");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
// Inflate the view
        view = inflater.inflate(R.layout.fragment2_layout, container, false);

// Get the radiobutton group by their id
//        Add Listener to them so we can determine which button is selected
        radioGroup = view.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                switch (id) {
//                    If the high priority radiobutton is selected, send that information to the main activity using activityCommunicator
                    case R.id.radioButtonHighPriority:
                        activityCommunicator.priorityChange("High");
                        break;

//                    If the low priority radiobutton is selected, send that information to the main activity using activityCommunicator
                    case R.id.radioButtonLowPriority:
                        activityCommunicator.priorityChange("Low");
                        break;

//                    If the other priority radiobutton is selected, send that information to the main activity using activityCommunicator
                    case R.id.radioButtonOtherPriority:
                        activityCommunicator.priorityChange("Other");
                        break;
                }
            }
        });


        return view;
    }
}
