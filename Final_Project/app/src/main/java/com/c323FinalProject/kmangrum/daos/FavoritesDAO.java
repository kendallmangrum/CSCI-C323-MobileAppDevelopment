package com.c323FinalProject.kmangrum.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.c323FinalProject.kmangrum.entities.Favorites;

import java.util.List;

@Dao
public interface FavoritesDAO {

    @Insert
    public void insert(Favorites... favorites);

    @Delete
    public void delete(Favorites favorites);

    @Query("SELECT * FROM favorites_table")
    List<Favorites> getAllFavorites();

    @Query("DELETE FROM favorites_table WHERE ReviewID = :reviewID")
    void getReviewIDSpecificFavorite(String reviewID);
}
