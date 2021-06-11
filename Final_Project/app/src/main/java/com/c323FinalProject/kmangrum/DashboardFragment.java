package com.c323FinalProject.kmangrum;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.c323FinalProject.kmangrum.daos.CategoryDAO;
import com.c323FinalProject.kmangrum.entities.Category;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;


public class DashboardFragment extends Fragment implements CustomChooseDialog.CustomChooseDialogListener {

//    Initialize variables that will be used later
    FloatingActionButton fab;
    AppDatabase database;
    RecyclerView listView;
    DashboardFragmentAdapter adapter;
    List<Category> categoryList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        // reference to our ListView
        listView = view.findViewById(R.id.categoryListview);
        // Creating an instance of our DB
        database = AppDatabase.getDBInstance(getContext());
        // create a list of all the categories within our Room database
        categoryList = database.getCategoryDAO().getAllCategories();
        // Create the listAdapter and pass it the new categoryList and then set the adapter
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        listView.setHasFixedSize(true);
        adapter = new DashboardFragmentAdapter(categoryList, getContext());
        listView.setAdapter(adapter);

        fab = view.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        return view;
    }

//    Method that shows a custom dialog that the user can use to add new Categories
    public void openDialog() {
//        CustomChooseDialog chooseDialog = new CustomChooseDialog();
//        chooseDialog.show(getFragmentManager(), "Create New Category");
        FragmentManager fm = getFragmentManager();
        CustomChooseDialog customChooseDialog = new CustomChooseDialog();
        customChooseDialog.setTargetFragment(DashboardFragment.this, 300);
        customChooseDialog.show(fm, "fragment_custom_choose");
    }

//  Method that is called once a new category has been added to update the listview
    @Override
    public void onDialogComplete() {
        categoryList = database.getCategoryDAO().getAllCategories();
        adapter = new DashboardFragmentAdapter(categoryList, getContext());
        listView.setAdapter(adapter);
    }
}