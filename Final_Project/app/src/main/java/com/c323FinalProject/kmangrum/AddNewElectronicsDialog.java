package com.c323FinalProject.kmangrum;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;
import androidx.room.Room;

import com.c323FinalProject.kmangrum.daos.ReviewDAO;
import com.c323FinalProject.kmangrum.daos.ReviewListDAO;
import com.c323FinalProject.kmangrum.entities.Category;
import com.c323FinalProject.kmangrum.entities.Review;
import com.c323FinalProject.kmangrum.entities.ReviewList;

import java.util.ArrayList;
import java.util.List;

public class AddNewElectronicsDialog extends DialogFragment {

//    Initialize variables
    private EditText etReviewItemName, etReviewItemFeature, etReviewItemDetails, etReviewItemLocation, etReviewItemWebsite;
    ReviewListDAO reviewListDAO;
    ReviewDAO reviewDAO;
    AppDatabase database;

//    interface to notify when the dialog is completed
    public interface ElectronicsDialogComplete{
        void ElectronicsFinished();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.CustomAlerts);

//        Inflate layout for dialog box
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_reviewlist_electronics, null);

//        Get database reference
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

//        Set up actions for when the cancel and save buttons are pressed
        builder.setView(view)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        get selected item and its information
                        int position = categorySelector.getSelectedItemPosition() - 1;
                        int id = allCategories.get(position).getCategoryID();
                        String categoryName = categorySelector.getSelectedItem().toString();

//                        Create a new reviewlist item
                        ReviewList newReviewList = new ReviewList();
                        newReviewList.setCategoryID(id);
                        newReviewList.setCategory(categoryName);
                        newReviewList.setReviewName(etReviewItemName.getText().toString());
//                        Create a new review item
                        Review newReview = new Review();
                        newReview.setCategoryID(id);
                        newReview.setReviewCategory(categoryName);
                        newReview.setReviewName(etReviewItemName.getText().toString());
                        newReview.setReviewLocation(etReviewItemLocation.getText().toString());
                        newReview.setReviewDetails(etReviewItemDetails.getText().toString());
                        newReview.setReviewWebsite(etReviewItemWebsite.getText().toString());
                        newReview.setReviewFeature(etReviewItemFeature.getText().toString());
//                        Use the two new items to update the old ones
                        reviewListDAO.insert(newReviewList);
                        reviewDAO.insert(newReview);
//                        Notify that the dialog box is finished being used
                        ElectronicsDialogComplete electronicsDialogComplete = (ElectronicsDialogComplete) getActivity();
                        electronicsDialogComplete.ElectronicsFinished();
                        Toast.makeText(getContext(), "Review Added!", Toast.LENGTH_SHORT).show();

                    }
                });

//        Get references for the editTexts
        etReviewItemName = view.findViewById(R.id.etReviewItemName);
        etReviewItemWebsite = view.findViewById(R.id.etReviewItemCuisine);
        etReviewItemFeature = view.findViewById(R.id.etReviewItemFamous);
        etReviewItemDetails = view.findViewById(R.id.etReviewItemDetails);
        etReviewItemLocation = view.findViewById(R.id.etReviewItemLocation);

        return builder.create();
    }

//    Populate the spinner dropdown menu
    private ArrayList<String> setSpinnerDropdown(List<Category> allCategories) {
        ArrayList<String> allOptions = new ArrayList<>();
        allOptions.add("- Pick a Category -");
        for(Category c : allCategories){
            allOptions.add(c.getCategoryName());
        }
        return allOptions;
    }
}
