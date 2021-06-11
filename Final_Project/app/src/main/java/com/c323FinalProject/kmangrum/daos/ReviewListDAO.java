package com.c323FinalProject.kmangrum.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.c323FinalProject.kmangrum.entities.ReviewList;

import java.util.List;

@Dao
public interface ReviewListDAO {

    @Insert
    public void insert(ReviewList... reviewLists);

    @Delete
    public void delete(ReviewList... reviewLists);

    @Query("SELECT * FROM reviewlist_table")
    List<ReviewList> getAllReviewlists();

    @Query("SELECT * FROM reviewlist_table WHERE Category == 'Restaurants'")
    List<ReviewList> getAllRestaurants();

    @Query("SELECT * FROM reviewlist_table WHERE Category == 'Electronics'")
    List<ReviewList> getAllElectronics();

    @Query("SELECT * FROM reviewlist_table WHERE Category == 'Books'")
    List<ReviewList> getAllBooks();

    @Query("SELECT * FROM reviewlist_table WHERE Category == 'Movies'")
    List<ReviewList> getAllMovies();

    @Query("SELECT * FROM reviewlist_table WHERE Category == :categoryName AND ReviewName == :reviewName")
    ReviewList getCurrentReviewListItem(String categoryName, String reviewName);

    @Query("SELECT * FROM reviewlist_table WHERE Category == :categoryID")
    ReviewList getCategorizedReviewList(String categoryID);

    @Query("UPDATE reviewlist_table SET Image = :image WHERE ReviewName == :reviewName")
    void updateReviewListImage(String image, String reviewName);

    @Update
    void updateReviewList(ReviewList reviewList);

}
