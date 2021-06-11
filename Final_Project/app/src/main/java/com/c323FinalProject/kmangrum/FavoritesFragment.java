package com.c323FinalProject.kmangrum;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.c323FinalProject.kmangrum.entities.Favorites;

import java.util.List;

public class FavoritesFragment extends Fragment {

    RecyclerView listView;
    AppDatabase database;
    List<Favorites> favoritesList;
    FavoritesFragmentAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        // reference our Recyclerview and database
        listView = view.findViewById(R.id.favoritesListview);
        new ItemTouchHelper(itemtouchHelperCallback).attachToRecyclerView(listView);
        database = AppDatabase.getDBInstance(getContext());
        // use our get all favorites to crate the list
        favoritesList = database.getFavoritesDAO().getAllFavorites();
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        listView.setHasFixedSize(true);
        adapter = new FavoritesFragmentAdapter(getContext(), favoritesList);
        listView.setAdapter(adapter);

        return view;
    }

    ItemTouchHelper.SimpleCallback itemtouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.CustomAlerts);
            final View customLayout = LayoutInflater.from(getContext()).inflate(R.layout.deletion_prompt, null);
            TextView message = customLayout.findViewById(R.id.tvDeletionMessage);
            TextView title = customLayout.findViewById(R.id.swipePrompt);
            message.setText("Hitting confirm will remove this review from your favorites. Is this what" +
                    "you intended to do?");
            title.setText("Remove Favorite");


            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // we'll reload the list to bring back our view from off the page!
                    favoritesList = database.getFavoritesDAO().getAllFavorites();
                    adapter = new FavoritesFragmentAdapter(getContext(), favoritesList);
                    listView.setAdapter(adapter);
                }
            });

            builder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // we'll use the viewholder to get the position of the current favorite that we want to remove
                    int position = viewHolder.getAdapterPosition();
                    Favorites toRemove = favoritesList.get(position);
                    database.getFavoritesDAO().delete(toRemove);

                    // At this point we'll reload the new list with the removed favorite
                    favoritesList = database.getFavoritesDAO().getAllFavorites();
                    adapter = new FavoritesFragmentAdapter(getContext(), favoritesList);
                    listView.setAdapter(adapter);
                }
            });
            builder.setView(customLayout);
            builder.show();

        }
    };
}