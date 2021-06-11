package com.c323FinalProject.kmangrum;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.c323FinalProject.kmangrum.entities.Category;

import java.util.List;

public class DashboardFragmentAdapter extends RecyclerView.Adapter<DashboardFragmentAdapter.ViewHolder> {

//    Initialize variables that will be used later
    List<Category> categoryList;
    Context context;

    public DashboardFragmentAdapter(List<Category> categoryList, Context context){
        this.categoryList = categoryList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        Inflate the layout of the recyclerview
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item_imageview_textview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // get the current Category from the list
        Category curr = categoryList.get(position);
        // set the title and image for the category
        holder.categoryName.setText(curr.getCategoryName());
        holder.categoryImage.setImageBitmap(getBitmapFromString(curr.getImage()));
        // set the onclick listener for the category card
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Pass the information to the next activity so it knows what category to display
                Category selectedCategory = categoryList.get(position);
                Intent i = new Intent(view.getContext(), ReviewListActivity.class);
                i.putExtra("categoryID", curr.getCategoryID());
                i.putExtra("categoryTitle", selectedCategory.getCategoryName());
                view.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    // This Function converts the String back to Bitmap
    private Bitmap getBitmapFromString(String stringPicture) {
        byte[] decodedString = Base64.decode(stringPicture, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        ImageView categoryImage;
        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryImage = itemView.findViewById(R.id.ivListviewItem);
            categoryName = itemView.findViewById(R.id.tvListviewItem);
            view = itemView;
        }
    }
}
