package com.c323FinalProject.kmangrum.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.c323FinalProject.kmangrum.entities.User;

import java.util.List;

@Dao
public interface UserDAO {

    @Insert
    public void insert(User... users);

    @Delete
    public void delete(User... users);

    @Query("SELECT * FROM user_table")
    List<User> getAllUsers();

}
