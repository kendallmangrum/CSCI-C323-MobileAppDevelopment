package com.c323proj11.kmangrum;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.room.Database;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class FavoritesFragment extends Fragment {

//    Create variables that will be used later
    AppDatabase database;
    List<Recipe> recipeList;
    ListView listView;
    FavoriteFragmentAdapter adapter;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

//        Get listview reference
        listView = view.findViewById(R.id.listview);

//        Get database instance
        database = AppDatabase.getDBInstance(getContext());

//        Get list of recipes in favorites database
        recipeList = database.getRecipeDao().getAllRecipes();

//        Populate the listview
        adapter = new FavoriteFragmentAdapter(getContext(), recipeList);
        listView.setAdapter(adapter);

        return view;
    }
}