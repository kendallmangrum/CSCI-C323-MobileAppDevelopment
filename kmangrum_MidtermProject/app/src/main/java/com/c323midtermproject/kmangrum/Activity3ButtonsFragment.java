package com.c323midtermproject.kmangrum;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import static com.c323midtermproject.kmangrum.ThirdActivity.isListview;


public class Activity3ButtonsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Activity3ButtonsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

// Create variables for the two buttons in the fragment
    Button displayListViewButton;
    Button displayCardViewButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_activity3_buttons, container, false);

//        Get the list view button and set an onclickListener
        displayListViewButton = v.findViewById(R.id.listViewButton);
        displayListViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isListview = true;
                /* In this on click method, I would have the boolean isListview, and send it to the fragment where I
                 * display either the listview or the cardview for the cities, monuments, or camping locations.
                 * In this case, isListview is set to true because it is the button that is for list view
                 */

            }
        });

//      Get the card view button and set an onClickListener
        displayCardViewButton = v.findViewById(R.id.cardViewButton);
        displayCardViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isListview = false;
                /* In this on click method, I would have the boolean isListview, and send it to the fragment where I
                 * display either the listview or the cardview for the cities, monuments, or camping locations.
                 * In this case, isListView is set to false because this button is for cardview
                 */
            }
        });

        return v;
    }
}