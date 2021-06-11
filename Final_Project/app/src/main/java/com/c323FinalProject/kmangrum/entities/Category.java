package com.c323FinalProject.kmangrum.entities;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Base64;

import androidx.annotation.DrawableRes;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.c323FinalProject.kmangrum.R;

import java.io.ByteArrayOutputStream;

@Entity (tableName = "category_table")
public class Category {
    @PrimaryKey (autoGenerate = true)
    private int CategoryID;
    private String CategoryName;
    private String Image;

    public Category(String categoryName, String image) {
        CategoryName = categoryName;
        Image = image;
    }

    public Category(){
        // basic generic constructor
    }

    public int getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(int categoryID) {
        CategoryID = categoryID;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public static Category[] PrepopulateCategories(Context context) {
        Bitmap movies = BitmapFactory.decodeResource(context.getResources(), R.drawable.moviesmall);
        Bitmap books = BitmapFactory.decodeResource(context.getResources(), R.drawable.booksmall);
        Bitmap restaurants = BitmapFactory.decodeResource(context.getResources(), R.drawable.restaurantsmall);
        Bitmap electronics = BitmapFactory.decodeResource(context.getResources(), R.drawable.electronicssmall);

        Category[] categories = {
                new Category("Movies", BitMapToString(movies)),
                new Category("Books", BitMapToString(books)),
                new Category("Restaurants", BitMapToString(restaurants)),
                new Category("Electronics", BitMapToString(electronics))
        };
        return categories;
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
