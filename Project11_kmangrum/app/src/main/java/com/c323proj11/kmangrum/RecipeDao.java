package com.c323proj11.kmangrum;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface RecipeDao {


    @Insert
    public void insert(Recipe... recipes);

    @Update
    public void update(Recipe... recipes);

    @Delete
    public void delete(Recipe... recipes);

    @Query("SELECT * FROM favoriteRecipes")
    List<Recipe> getAllRecipes();
}
