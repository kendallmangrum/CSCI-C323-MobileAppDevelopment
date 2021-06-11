package com.c323proj11.kmangrum;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainActivityAdapter extends ArrayAdapter<FoodCategory> {
//    Initialize variables
    private List<FoodCategory> categoryList;
    private Context context;

//    Create constructor
    public MainActivityAdapter(Context context, ArrayList<FoodCategory> categoryList) {
        super(context, R.layout.activity_onetwo_cardview, categoryList);
        this.context = context;
        this.categoryList = categoryList;
    }

    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Nullable
    @Override
    public FoodCategory getItem(int position) {
        return categoryList.get(position);
    }

//    Method that sets up the views in the layout
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_onetwo_cardview, parent, false);
        }
        TextView tvTitle = convertView.findViewById(R.id.tvTitle);
        ImageView ivFood = convertView.findViewById(R.id.ivFood);

        tvTitle.setText(categoryList.get(position).getTitle());
        Picasso.get().load(categoryList.get(position).getImagePath()).into(ivFood);

        return convertView;

    }

}
