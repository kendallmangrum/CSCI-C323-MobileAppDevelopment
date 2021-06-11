package com.c323FinalProject.kmangrum;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.room.Room;

import com.c323FinalProject.kmangrum.daos.ReviewDAO;
import com.c323FinalProject.kmangrum.daos.ReviewListDAO;
import com.c323FinalProject.kmangrum.entities.Category;
import com.c323FinalProject.kmangrum.entities.Review;
import com.c323FinalProject.kmangrum.entities.ReviewList;

import java.util.ArrayList;
import java.util.List;

public class AddNewRestaurantDialog extends AppCompatDialogFragment {

    private EditText etReviewItemName, etReviewItemCuisine, etReviewItemFamous, etReviewItemWebsite, etReviewItemLocation, etReviewItemDetails;

//    Initialize variables that will be used later
    ReviewListDAO reviewListDAO;
    ReviewDAO reviewDAO;
    AppDatabase database;

//    Create interface to know when dialog is no longer active
    public interface RestaurantDialogComplete{
        void RestaurantsFinished();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlerts);
//      Inflate layout for adding restaurant
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_reviewlist_restaurant, null);
//      Get instance of the database
        database = Room.databaseBuilder(getContext(), AppDatabase.class, "ReviewDatabase").allowMainThreadQueries().build();
        reviewListDAO = database.getReviewListDAO();
        reviewDAO = database.getReviewDAO();

        Spinner categorySelector = view.findViewById(R.id.categorySpinner);
        List<Category> allCategories = database.getCategoryDAO().getAllCategories();
        ArrayList<String> options = setSpinnerDropdown(allCategories);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, options){
            @Override
            public boolean isEnabled(int position) {
                if(position == 0){
                    return false;
                } else {
                    return true;
                }
            }
        };
        categorySelector.setAdapter(adapter);
//      Set up actions for when the Cancel and Save buttons are pressed
        builder.setView(view)
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        Get information of selected item
                        int position = categorySelector.getSelectedItemPosition() - 1;
                        int id = allCategories.get(position).getCategoryID();
                        String categoryName = categorySelector.getSelectedItem().toString();
//                      Create a new reviewlist entity
                        ReviewList newReviewList = new ReviewList();
                        newReviewList.setCategoryID(id);
                        newReviewList.setCategory(categoryName);
                        newReviewList.setReviewName(etReviewItemName.getText().toString());
//                      Create a new review entity
                        Review newReview = new Review();
                        newReview.setCategoryID(id);
                        newReview.setReviewCategory(categoryName);
                        newReview.setReviewName(etReviewItemName.getText().toString());
                        newReview.setReviewLocation(etReviewItemLocation.getText().toString());
                        newReview.setReviewDetails(etReviewItemDetails.getText().toString());
                        newReview.setReviewCuisine(etReviewItemCuisine.getText().toString());
                        newReview.setReviewFamousFor(etReviewItemFamous.getText().toString());
//                        Update the Reviewlist and Review entities with the new entity information
                        reviewListDAO.insert(newReviewList);
                        reviewDAO.insert(newReview);
//                      Notify that the dialog is no longer active
                        RestaurantDialogComplete restaurantDialogComplete = (RestaurantDialogComplete) getActivity();
                        restaurantDialogComplete.RestaurantsFinished();

                        Toast.makeText(getContext(), "Review Added!", Toast.LENGTH_SHORT).show();

                    }
                });
//        Get the edittexts to get the information the user inputs
        etReviewItemName = view.findViewById(R.id.etReviewItemName);
        etReviewItemFamous = view.findViewById(R.id.etReviewItemFamous);
        etReviewItemCuisine = view.findViewById(R.id.etReviewItemCuisine);
        etReviewItemDetails = view.findViewById(R.id.etReviewItemDetails);
        etReviewItemLocation = view.findViewById(R.id.etReviewItemLocation);

        return builder.create();
    }

//    Populate the spinner menu
    private ArrayList<String> setSpinnerDropdown(List<Category> allCategories) {
        ArrayList<String> allOptions = new ArrayList<>();
        allOptions.add("- Pick a Category -");
        for(Category c : allCategories){
            allOptions.add(c.getCategoryName());
        }
        return allOptions;
    }
}
