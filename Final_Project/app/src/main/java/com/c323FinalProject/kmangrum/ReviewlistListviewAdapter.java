package com.c323FinalProject.kmangrum;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.c323FinalProject.kmangrum.entities.Category;
import com.c323FinalProject.kmangrum.entities.Review;
import com.c323FinalProject.kmangrum.entities.ReviewList;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ReviewlistListviewAdapter extends RecyclerView.Adapter<ReviewlistListviewAdapter.ViewHolder> {

//    Initialize Variables
    private List<Review> reviewList;
    private Context context;
    public ReviewListListener reviewListListener;

    public ReviewlistListviewAdapter(List<Review> reviewListList, Context context, ReviewListListener reviewListListener) {
        this.reviewList = reviewListList;
        this.context = context;
        this.reviewListListener = reviewListListener;
    }


    @NonNull
    @Override
    public ReviewlistListviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        inflate the recyclerview layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reviewlist_listview_item, parent, false);
        return new ReviewlistListviewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // get the current review from the list
        Review curr = reviewList.get(position);
        // set the title and image of the review cardview with the curr's data
        holder.reviewName.setText(curr.getReviewName());
        // if the review has an image in the database we'll want to use it
        if (curr.getImage() != null) {
            holder.reviewImage.setImageBitmap(getBitmapFromString(curr.getImage()));
        } else {
            holder.reviewImage.setImageResource(R.drawable.placeholder_image);
        }

        // We'll want to set the click listeners for our image update button and entire view
        // onclick listener for the entire view, when clicked, should send to Review Activity
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ReviewActivity.class);
                i.putExtra("reviewName", curr.getReviewName());
                i.putExtra("reviewCategory", curr.getReviewCategory());
                i.putExtra("reviewID", curr.getReviewID());
                holder.view.getContext().startActivity(i);
            }
        });

        holder.updateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reviewListListener.createChooseImageAlertDialog(position);
                Toast.makeText(context, "Here's the Current ReviewID: " + String.valueOf(curr.getReviewID()), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView reviewName;
        ImageView reviewImage, updateImage;
        View view;
        ReviewListListener reviewListListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            reviewName = itemView.findViewById(R.id.tvReviewlistItem);
            reviewImage = itemView.findViewById(R.id.ivReviewlistItem);
            updateImage = itemView.findViewById(R.id.ivCameraIcon);
            view = itemView;
        }
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    private String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    // This Function converts the String back to Bitmap
    private Bitmap getBitmapFromString(String stringPicture) {
        byte[] decodedString = Base64.decode(stringPicture, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
}