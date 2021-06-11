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

public class SecondActivityAdapter extends ArrayAdapter<Meal> {
    //    Initialize variables
    private List<Meal> mealsList;
    private Context context;

    //    Create constructor
    public SecondActivityAdapter(Context context, ArrayList<Meal> mealsList) {
        super(context, R.layout.activity_onetwo_cardview, mealsList);
        this.context = context;
        this.mealsList = mealsList;
    }

    @Override
    public int getCount() {
        return mealsList.size();
    }

    @Nullable
    @Override
    public Meal getItem(int position) {
        return mealsList.get(position);
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

        tvTitle.setText(mealsList.get(position).getTitle());
        Picasso.get().load(mealsList.get(position).getImagePath()).into(ivFood);

        return convertView;
    }
}
