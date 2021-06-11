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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.c323FinalProject.kmangrum.entities.Review;
import com.c323FinalProject.kmangrum.entities.Trash;

import java.util.List;

public class TrashFragment extends Fragment  implements TrashToReview {

    RecyclerView listView;
    AppDatabase database;
    List<Trash> trashList;
    TrashFragmentAdapter adapter;
    Button bttnEmptyTrash;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trash, container, false);
        // reference our Recyclerview and database
        listView = view.findViewById(R.id.trashListview);
        // create and attach our itemtouch helper to our recyclerview
        new ItemTouchHelper(itemtouchHelperCallback).attachToRecyclerView(listView);
        database = AppDatabase.getDBInstance(getContext());
        // use our get all trash to crate the list
        trashList = database.getTrashDAO().getAllTrash();
        // set the layout manager and fized size
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        listView.setHasFixedSize(true);
        // create our adapter and attach it
        adapter = new TrashFragmentAdapter(getContext(), trashList, TrashFragment.this);
        listView.setAdapter(adapter);

        bttnEmptyTrash = view.findViewById(R.id.bttnEmptyTrash);
        bttnEmptyTrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.getTrashDAO().deleteAllTrash();
                Toast.makeText(getContext(), "Trash has been cleared!", Toast.LENGTH_SHORT).show();
                trashList = database.getTrashDAO().getAllTrash();
                adapter = new TrashFragmentAdapter(getContext(), trashList, TrashFragment.this);
                listView.setAdapter(adapter);
            }
        });

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
            message.setText("Hitting confirm will permanently delete this review from the application. Is that " +
                    "what you intend to happen?");
            title.setText("Remove Trash");


            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // we'll reload the list to bring back our view from off the page!
                    trashList = database.getTrashDAO().getAllTrash();
                    adapter = new TrashFragmentAdapter(getActivity(), trashList, TrashFragment.this);
                    listView.setAdapter(adapter);
                }
            });

            builder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // we'll use the viewholder to get the position of the current favorite that we want to remove
                    int position = viewHolder.getAdapterPosition();
                    Trash toRemove = trashList.get(position);
                    database.getTrashDAO().delete(toRemove);

                    // At this point we'll reload the new list with the removed favorite
                    trashList = database.getTrashDAO().getAllTrash();
                    adapter = new TrashFragmentAdapter(getContext(), trashList, TrashFragment.this);
                    listView.setAdapter(adapter);
                }
            });
            builder.setView(customLayout);
            builder.show();

        }
    };

    @Override
    public void ReinstallTrash(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.CustomAlerts);
        final View customLayout = LayoutInflater.from(getContext()).inflate(R.layout.deletion_prompt, null);
        TextView message = customLayout.findViewById(R.id.tvDeletionMessage);
        TextView title = customLayout.findViewById(R.id.swipePrompt);
        message.setText("Hitting confirm will add this review back into your review lists. Is that what you " +
                "intend to happen?");
        title.setText("Add Review Back");


        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // we'll reload the list to bring back our view from off the page!
                trashList = database.getTrashDAO().getAllTrash();
                adapter = new TrashFragmentAdapter(getActivity(), trashList, TrashFragment.this);
                listView.setAdapter(adapter);
            }
        });

        builder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // we'll use the position to identify the current trash review
                Trash toDelete = trashList.get(position);
                // using the current trash review we can create and add back a review object
                Review toAdd = new Review();
                toAdd.setCategoryID(toDelete.getCategoryID());
                toAdd.setReviewCategory(toDelete.getReviewCategory());
                toAdd.setReviewName(toDelete.getReviewName());
                toAdd.setReviewFeature(toDelete.getReviewFeature());
                toAdd.setReviewWebsite(toDelete.getReviewWebsite());
                toAdd.setReviewLocation(toDelete.getReviewLocation());
                toAdd.setReviewDetails(toDelete.getReviewDetails());
                toAdd.setImage(toDelete.getImage());
                toAdd.setReviewGenre(toDelete.getReviewGenre());
                toAdd.setReviewFamousFor(toDelete.getReviewFamousFor());
                toAdd.setReviewCuisine(toDelete.getReviewCuisine());
                toAdd.setReviewLanguage(toDelete.getReviewLanguage());
                // we can now add our review and remove our trash
                database.getReviewDAO().insert(toAdd);
                database.getTrashDAO().delete(toDelete);
                // At this point we'll reload the new list with the removed trash
                trashList = database.getTrashDAO().getAllTrash();
                adapter = new TrashFragmentAdapter(getContext(), trashList, TrashFragment.this);
                listView.setAdapter(adapter);
            }
        });
        builder.setView(customLayout);
        builder.show();

    }
}