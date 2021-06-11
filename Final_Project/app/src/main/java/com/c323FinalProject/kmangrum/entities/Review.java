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

@Entity (tableName = "review_table")
public class Review {
    @PrimaryKey (autoGenerate = true)
    private int ReviewID;
    private int ReviewlistID;
    private int CategoryID;
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

    public int getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(int categoryID) {
        CategoryID = categoryID;
    }

    public String getReviewDetails() {
        return ReviewDetails;
    }

    public void setReviewDetails(String reviewDetails) {
        ReviewDetails = reviewDetails;
    }

    public String getReviewCategory() {
        return ReviewCategory;
    }

    public void setReviewCategory(String reviewCategory) {
        ReviewCategory = reviewCategory;
    }

    public String getReviewName() {
        return ReviewName;
    }

    public void setReviewName(String reviewName) {
        ReviewName = reviewName;
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

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public static Review[] PrepopulateReviews(Context context) {

        // Movies
        Review review1 = new Review();
        review1.setCategoryID(1);
        review1.setReviewCategory("Movies");
        review1.setReviewName("Harry Potter and the Goblet of Fire");
        review1.setReviewGenre("Sci-Fi");
        review1.setReviewLanguage("English");
        review1.setReviewWebsite("www.Netflix.com");
        review1.setReviewLocation("Bloomington, Indiana");
        review1.setReviewDetails("A young wizard is placed into a wizarding tournament.");
        Bitmap harrypotter = BitmapFactory.decodeResource(context.getResources(), R.drawable.harrypotter);
        review1.setImage(BitMapToString(harrypotter));

        Review review2 = new Review();
        review2.setCategoryID(1);
        review2.setReviewCategory("Movies");
        review2.setReviewName("Hoosiers");
        review2.setReviewGenre("Drama");
        review2.setReviewLanguage("English");
        review2.setReviewWebsite("www.Netflix.com");
        review2.setReviewLocation("Milan, Indiana");
        review2.setReviewDetails("A rural basketball team earns its way to the championship game.");
        Bitmap hoosiers = BitmapFactory.decodeResource(context.getResources(), R.drawable.hoosiers);
        review2.setImage(BitMapToString(hoosiers));

//        Books
        Review review3 = new Review();
        review3.setCategoryID(2);
        review3.setReviewCategory("Books");
        review3.setReviewName("The Iliad");
        review3.setReviewGenre("Mythology");
        review3.setReviewLanguage("Greek");
        review3.setReviewWebsite("www.theIliad.com");
        review3.setReviewLocation("New York, New York");
        review3.setReviewDetails("A poem written by Homer that is about Achilles");
        Bitmap iliad = BitmapFactory.decodeResource(context.getResources(), R.drawable.iliad);
        review3.setImage(BitMapToString(iliad));

        Review review4 = new Review();
        review4.setCategoryID(2);
        review4.setReviewCategory("Books");
        review4.setReviewName("The Odyssey");
        review4.setReviewGenre("Mythology");
        review4.setReviewLanguage("Ancient Greek");
        review4.setReviewWebsite("www.theOdyssey.com");
        review4.setReviewLocation("Tampa, Florida");
        review4.setReviewDetails("An ancient poem written by Homer that is among the oldest writings still read.");
        Bitmap odyssey = BitmapFactory.decodeResource(context.getResources(), R.drawable.odyssey);
        review4.setImage(BitMapToString(odyssey));

//        Restaurants
        Review review5 = new Review();
        review5.setCategoryID(3);
        review5.setReviewCategory("Restaurants");
        review5.setReviewName("McDonald's");
        review5.setReviewFamousFor("The Big Mac");
        review5.setReviewCuisine("Burgers, Fries, Chicken Nuggets");
        review5.setReviewLocation("Bloomington, Indiana");
        review5.setReviewDetails("One of the largest fast food chains. Quick and cheap food");
        Bitmap mcdonalds = BitmapFactory.decodeResource(context.getResources(), R.drawable.mcdonalds);
        review5.setImage(BitMapToString(mcdonalds));

        Review review6 = new Review();
        review6.setCategoryID(3);
        review6.setReviewCategory("Restaurants");
        review6.setReviewName("Wendy's");
        review6.setReviewFamousFor("Their fresh, never frozen beef");
        review6.setReviewCuisine("Burgers, Fries, Chicken Nuggets");
        review6.setReviewLocation("Dublin, Ohio");
        review6.setReviewDetails("Quick and cheap food. Great 4 for $4 deal");
        Bitmap wendys = BitmapFactory.decodeResource(context.getResources(), R.drawable.wendys);
        review6.setImage(BitMapToString(wendys));

//        Electronics
        Review review7 = new Review();
        review7.setCategoryID(4);
        review7.setReviewCategory("Electronics");
        review7.setReviewName("Airpods Pro");
        review7.setReviewFeature("Noise cancelling");
        review7.setReviewWebsite("www.apple.com");
        review7.setReviewLocation("San Francisco, California");
        review7.setReviewDetails("Nice earbuds, somewhat expensive");
        Bitmap airpods = BitmapFactory.decodeResource(context.getResources(), R.drawable.airpodsmax);
        review7.setImage(BitMapToString(airpods));

        Review review8 = new Review();
        review8.setCategoryID(4);
        review8.setReviewCategory("Electronics");
        review8.setReviewName("iPhone11");
        review8.setReviewFeature("Three rear facing cameras");
        review8.setReviewWebsite("www.apple.com");
        review8.setReviewLocation("San Francisco, California");
        review8.setReviewDetails("Expensive, no green text bubbles");
        Bitmap iphone = BitmapFactory.decodeResource(context.getResources(), R.drawable.iphone11);
        review8.setImage(BitMapToString(iphone));

        Review[] reviews = {
                review1, review2, review3, review4, review5, review6,review7, review8
        };
        return reviews;
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
