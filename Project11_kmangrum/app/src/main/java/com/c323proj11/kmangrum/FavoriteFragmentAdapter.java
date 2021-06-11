package com.c323proj11.kmangrum;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FavoriteFragmentAdapter extends ArrayAdapter<Recipe> {

    //    Initialize variables
    private List<Recipe> recipeList;
    private Context context;

    //    Create constructor
    public FavoriteFragmentAdapter(Context context, List<Recipe> recipeList) {
        super(context, R.layout.favorite_recipe_layout, recipeList);
        this.context = context;
        this.recipeList = recipeList;
    }

    @Override
    public int getCount() {
        return recipeList.size();
    }

    @Nullable
    @Override
    public Recipe getItem(int position) {
        return recipeList.get(position);
    }

//    Method that sets up all of the views in the layout
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.favorite_recipe_layout, parent, false);
        }
        TextView tvRName = convertView.findViewById(R.id.tvRName);
        TextView tvRCategory = convertView.findViewById(R.id.tvRCategory);
        TextView tvRArea = convertView.findViewById(R.id.tvRArea);
        TextView tvRInstructions = convertView.findViewById(R.id.tvRInstructions);

        ImageView ivRecipe = convertView.findViewById(R.id.ivRecipe);

        tvRName.setText(recipeList.get(position).getName());
        tvRCategory.setText(recipeList.get(position).getCategory());
        tvRArea.setText(recipeList.get(position).getArea());
        tvRInstructions.setText(recipeList.get(position).getInstructions());
        Picasso.get().load(recipeList.get(position).getImagePath()).into(ivRecipe);

        return convertView;
    }
}
