package com.c323midtermproject.kmangrum;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

public class ThirdActivity extends AppCompatActivity {

//    Instantiate variables for FragmentTransaction, Fragments
    FragmentTransaction fragmentTransaction;
    private Activity3ButtonsFragment activity3ButtonsFragment;
    private ListCardViewFragment listCardViewFragment;
    static Boolean isListview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

//        Get the intent passed from the corresponding fragment and place in bundle to send it to fragment
        String s = getIntent().getStringExtra("FRAGMENT");
        Bundle bundle = new Bundle();
        bundle.putString("FRAGMENT", s);



        fragmentTransaction = getSupportFragmentManager().beginTransaction();

//        New instances of fragments to be displayed
        activity3ButtonsFragment = new Activity3ButtonsFragment();
        listCardViewFragment = new ListCardViewFragment();

//        Attach the bundle to fragment it needs sent to
        listCardViewFragment.setArguments(bundle);

//        Replace the fragments so that the proper ones are displayed
        fragmentTransaction.replace(R.id.listCardViewButtonFragment, activity3ButtonsFragment);
        fragmentTransaction.replace(R.id.listCardViewDisplayFragment, listCardViewFragment);

        fragmentTransaction.commit();
    }
}