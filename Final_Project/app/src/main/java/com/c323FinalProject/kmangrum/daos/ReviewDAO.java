package com.c323FinalProject.kmangrum.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.c323FinalProject.kmangrum.entities.Review;
import com.c323FinalProject.kmangrum.entities.ReviewList;

import java.util.List;

@Dao
public interface ReviewDAO {

    @Insert
    public void insert(Review... reviews);

    @Delete
    public void delete(Review... reviews);

    @Query("SELECT * FROM review_table")
    List<Review> getAllReviews();

    @Query("SELECT * FROM review_table WHERE ReviewCategory == :categoryName AND ReviewName == :reviewName")
    Review getCurrentReviewItem(String categoryName, String reviewName);

    @Query("SELECT * FROM review_table WHERE CategoryID == :categoryID")
    List<Review> getCategoriedReviews(String categoryID);

    @Query("SELECT * FROM review_table WHERE ReviewID == :reviewID")
    Review getSpecificReview(String reviewID);

    @Query("UPDATE review_table SET Image = :image WHERE ReviewName == :reviewName")
    void updateReviewListImage(String image, String reviewName);

    @Query("UPDATE review_table SET Image = :image WHERE ReviewID == :reviewID")
    void updateReviewImage(String image, String reviewID);

    @Update
    void updateReview(Review... reviews);
}
