package com.c323FinalProject.kmangrum;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.c323FinalProject.kmangrum.entities.Favorites;
import com.c323FinalProject.kmangrum.entities.ReviewList;

import java.util.List;

public class FavoritesFragmentAdapter extends RecyclerView.Adapter<FavoritesFragmentAdapter.ViewHolder> {

//    Initialize variables
    private List<Favorites> favoritesList;
    private Context context;

    public FavoritesFragmentAdapter(Context context, List<Favorites> favoritesList) {
        this.context = context;
        this.favoritesList = favoritesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item_imageview_textview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // get the current Favorite from the list
        Favorites curr = favoritesList.get(position);
        // set the title for each favorite cardview
        holder.favName.setText(curr.getReviewName());
        holder.favImage.setImageBitmap(getBitmapFromString(curr.getImage()));
    }

    @Override
    public int getItemCount() {
        return favoritesList.size();
    }

    // This Function converts the String back to Bitmap
    private Bitmap getBitmapFromString(String stringPicture) {
        byte[] decodedString = Base64.decode(stringPicture, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView favName;
        ImageView favImage;
        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            favName = itemView.findViewById(R.id.tvListviewItem);
            favImage = itemView.findViewById(R.id.ivListviewItem);
            view = itemView;
        }
    }


//    public FavoritesFragmentAdapter(Context context, List<Favorites> favoritesList) {
//        super(context, R.layout.listview_item_imageview_textview, favoritesList);
//        this.context = context;
//        this.favoritesList = favoritesList;
//    }
//
//    @Override
//    public int getCount() {
//        return favoritesList.size();
//    }
//
//    @Nullable
//    @Override
//    public Favorites getItem(int position) {
//        return favoritesList.get(position);
//    }
//
//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//
//        if (convertView == null) {
//            convertView = LayoutInflater.from(context).inflate(R.layout.listview_item_imageview_textview, parent, false);
//        }
//
////        Collections.reverse(reviewListList);
//
//        TextView tvFavoriteItem = convertView.findViewById(R.id.tvListviewItem);
//
//        tvFavoriteItem.setText(favoritesList.get(position).getReviewName());
//
//        ImageView ivDefaultImage = convertView.findViewById(R.id.ivListviewItem);
//
//        if (favoritesList.get(position).getImage() != null) {
//            ivDefaultImage.setImageBitmap(getBitmapFromString(favoritesList.get(position).getImage()));
//        }else {
//            ivDefaultImage.setImageResource(R.drawable.placeholder_image);
//        }
//
//        return convertView;
//    }
//
//
//
//    //    This Function converts the String back to Bitmap
//    private Bitmap getBitmapFromString(String stringPicture) {
//        byte[] decodedString = Base64.decode(stringPicture, Base64.DEFAULT);
//        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//    }

}
