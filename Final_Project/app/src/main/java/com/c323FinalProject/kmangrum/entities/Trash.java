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

@Entity (tableName = "trash_table")
public class Trash {
    @PrimaryKey (autoGenerate = true)
    private int TrashID;
    private int CategoryID;
    private int FavoriteID;
    private int ReviewID;
    private int ReviewlistID;
    private String ReviewCategory;
    private String ReviewName;
    private String ReviewLocation;
    private String ReviewDetails;
    private String ReviewWebsite;
    private String ReviewLanguage;
    private String ReviewGenre;
    private String ReviewFeature;
    private String ReviewFamousFor;
    private String ReviewCuisine;
    private String Image;

    public int getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(int categoryID) {
        CategoryID = categoryID;
    }

    public String getReviewCategory() {
        return ReviewCategory;
    }

    public void setReviewCategory(String reviewCategory) {
        ReviewCategory = reviewCategory;
    }

    public String getReviewLocation() {
        return ReviewLocation;
    }

    public void setReviewLocation(String reviewLocation) {
        ReviewLocation = reviewLocation;
    }

    public String getReviewWebsite() {
        return ReviewWebsite;
    }

    public void setReviewWebsite(String reviewWebsite) {
        ReviewWebsite = reviewWebsite;
    }

    public String getReviewLanguage() {
        return ReviewLanguage;
    }

    public void setReviewLanguage(String reviewLanguage) {
        ReviewLanguage = reviewLanguage;
    }

    public String getReviewGenre() {
        return ReviewGenre;
    }

    public void setReviewGenre(String reviewGenre) {
        ReviewGenre = reviewGenre;
    }

    public String getReviewFeature() {
        return ReviewFeature;
    }

    public void setReviewFeature(String reviewFeature) {
        ReviewFeature = reviewFeature;
    }

    public String getReviewFamousFor() {
        return ReviewFamousFor;
    }

    public void setReviewFamousFor(String reviewFamousFor) {
        ReviewFamousFor = reviewFamousFor;
    }

    public String getReviewCuisine() {
        return ReviewCuisine;
    }

    public void setReviewCuisine(String reviewCuisine) {
        ReviewCuisine = reviewCuisine;
    }

    public int getTrashID() {
        return TrashID;
    }

    public void setTrashID(int trashID) {
        TrashID = trashID;
    }

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

    public static Trash[] PrepopulateTrash(Context context) {
        Trash trash1 = new Trash();
        trash1.setCategoryID(3);
        trash1.setReviewCategory("Restaurants");
        trash1.setReviewName("Taco Bell");
        trash1.setReviewDetails("Fast and cheap tacos");
        trash1.setReviewFamousFor("They're late night munchies");
        trash1.setReviewCuisine("Mexican");
        trash1.setReviewLocation("Bloomington, Indiana");
        Bitmap tacobell = BitmapFactory.decodeResource(context.getResources(), R.drawable.tacobell);
        trash1.setImage(BitMapToString(tacobell));

        Trash trash2 = new Trash();
        trash2.setCategoryID(3);
        trash2.setReviewCategory("Restaurants");
        trash2.setReviewName("KFC");
        trash2.setReviewDetails("Fast food, fried chicken, and sides");
        trash2.setReviewFamousFor("They're bucket of chicken!");
        trash2.setReviewCuisine("American");
        trash2.setReviewLocation("Cincinnati, Ohio");
        Bitmap kfc = BitmapFactory.decodeResource(context.getResources(), R.drawable.kfc);
        trash2.setImage(BitMapToString(kfc));

        Trash[] Trash = {
            trash1, trash2
        };
        return Trash;
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
