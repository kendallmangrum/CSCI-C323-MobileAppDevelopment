package com.c323FinalProject.kmangrum.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "reviewlist_table")
public class ReviewList {
    @PrimaryKey (autoGenerate = true)
    int ReviewlistID;
    private int CategoryID;
    private String Category;
    private String ReviewName;
    private String Image;

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

    public String getReviewName() {
        return ReviewName;
    }

    public void setReviewName(String reviewName) {
        ReviewName = reviewName;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
