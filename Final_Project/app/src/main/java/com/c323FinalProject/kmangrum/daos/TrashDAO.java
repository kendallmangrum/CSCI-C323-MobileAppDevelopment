package com.c323FinalProject.kmangrum.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.c323FinalProject.kmangrum.entities.Trash;

import java.util.List;

@Dao
public interface TrashDAO {

    @Insert
    public void insert(Trash... trashes);

    @Delete
    public void delete(Trash... trashes);

    @Query("SELECT * FROM trash_table")
    List<Trash> getAllTrash();

    @Query("DELETE FROM trash_table")
    void deleteAllTrash();
}
