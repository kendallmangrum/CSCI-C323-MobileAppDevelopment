package com.c323FinalProject.kmangrum.entities;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.c323FinalProject.kmangrum.R;

import java.io.ByteArrayOutputStream;

@Entity (tableName = "favorites_table")
public class Favorites {
    @PrimaryKey (autoGenerate = true)
    private int FavoriteID;
    private int ReviewID;
    private int ReviewlistID;
    private String ReviewName;
    private String ReviewDetails;
    private String Image;

    public int getFavoriteID() {
        return FavoriteID;
    }

    public void setFavoriteID(int favoriteID) {
        FavoriteID = favoriteID;
    }

    public int getReviewID() {
        return ReviewID;
    }

    public void setReviewID(int reviewID) {
        ReviewID = reviewID;
    }

    public int getReviewlistID() {
        return ReviewlistID;
    }

    public void setReviewlistID(int reviewlistID) {
        ReviewlistID = reviewlistID;
    }

    public String getReviewName() {
        return ReviewName;
    }

    public void setReviewName(String reviewName) {
        ReviewName = reviewName;
    }

    public String getReviewDetails() {
        return ReviewDetails;
    }

    public void setReviewDetails(String reviewDetails) {
        ReviewDetails = reviewDetails;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public static Favorites[] PrepopulateFavorites(Context context) {

        Favorites favorites1 = new Favorites();
        favorites1.setReviewID(6);
        favorites1.setReviewName("Wendy's");
        favorites1.setReviewDetails("Quick and cheap food. Great 4 for $4 deal");
        Bitmap wendys = BitmapFactory.decodeResource(context.getResources(), R.drawable.wendys);
        favorites1.setImage(BitMapToString(wendys));

        Favorites favorites2 = new Favorites();
        favorites2.setReviewID(8);
        favorites2.setReviewName("iPhone11");
        favorites2.setReviewDetails("Expensive, no green text bubbles");
        Bitmap iphone = BitmapFactory.decodeResource(context.getResources(), R.drawable.iphone11);
        favorites2.setImage(BitMapToString(iphone));

        Favorites favorites3 = new Favorites();
        favorites3.setReviewID(2);
        favorites3.setReviewName("Hoosiers");
        favorites3.setReviewDetails("A rural basketball team earns its way to the championship game.");
        Bitmap hoosiers = BitmapFactory.decodeResource(context.getResources(), R.drawable.hoosiers);
        favorites3.setImage(BitMapToString(hoosiers));

        Favorites favorites4 = new Favorites();
        favorites4.setReviewID(3);
        favorites4.setReviewName("The Iliad");
        favorites4.setReviewDetails("A poem written by Homer that is about Achilles");
        Bitmap iliad = BitmapFactory.decodeResource(context.getResources(), R.drawable.iliad);
        favorites4.setImage(BitMapToString(iliad));

        Favorites[] favorites = {
                favorites1, favorites2, favorites3, favorites4
        };
        return favorites;
    }

    // Function that converts Bitmap into a string
    private static String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos = new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
}
