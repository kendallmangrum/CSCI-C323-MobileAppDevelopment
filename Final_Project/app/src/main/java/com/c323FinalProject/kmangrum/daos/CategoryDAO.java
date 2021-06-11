package com.c323FinalProject.kmangrum.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.c323FinalProject.kmangrum.entities.Category;

import java.util.List;

@Dao
public interface CategoryDAO {

    @Insert
    public void insert(Category... categories);

    @Delete
    public void delete(Category... categories);

    @Query("SELECT * FROM category_table")
    List<Category> getAllCategories();
}
