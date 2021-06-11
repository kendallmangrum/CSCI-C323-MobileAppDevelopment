package com.c323proj7.kmangrum;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

// Create a custom adapter for the listview in second activity
public class MyListAdapter extends ArrayAdapter<movieItem> {

    ArrayList<movieItem> myMovies;
    Context context;

    public MyListAdapter(Context context, ArrayList<movieItem> myMovies) {
        super(context, R.layout.movie_item, myMovies);

        this.context = context;
        this.myMovies = myMovies;

    }

    @Override
    public int getCount() {
        return myMovies.size();
    }


    @Override
    public movieItem getItem(int position) {
        return myMovies.get(position);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.movie_item, parent, false);
        }



//        Get the layout elements imageview and textview
        TextView tvMovieTitle = view.findViewById(R.id.tvMovieTitle);
        ImageView ivMovieImage = view.findViewById(R.id.ivMovieImage);

        String startImageURL = "https://image.tmdb.org/t/p/w500";

        tvMovieTitle.setText(myMovies.get(position).getTitle());

//        set the imageview to display the movie poster image
        Picasso.get().load(startImageURL + myMovies.get(position).getImagePath()).into(ivMovieImage);

        return view;
    }


}
