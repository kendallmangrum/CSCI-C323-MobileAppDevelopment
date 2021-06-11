package com.c323proj9.kmangrum;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ExpenseDao {

    @Insert
    public void insert(Expense... expenses);

    @Update
    public void update(Expense... expenses);

    @Delete
    public void delete(Expense... expenses);

//  Queries that get the information that is needed throughout the app
//    Get everything in the database
    @Query("SELECT * FROM expenses")
    List<Expense> getAllExpenses();

    @Query("UPDATE expenses SET name = :newName WHERE name = :eName")
    void updateName(String eName, String newName);

    @Query("UPDATE expenses SET cost = :newCost WHERE cost = :eCost AND name = :eName")
    void updateCost(String eName, String eCost, String newCost);

    @Query("UPDATE expenses SET category = :newCategory WHERE category = :eCategory AND name = :eName")
    void updateCategory(String eName, String eCategory, String newCategory);

    @Query("UPDATE expenses SET date = :newDate WHERE date = :eDate AND name = :eName")
    void updateDate(String eName, String eDate, String newDate);

//    Get all of one category from the database
    @Query("SELECT * FROM expenses WHERE category = :category")
    List<Expense> getSearchCategory(String category);

//    Get all of the items that match the category and cost
    @Query("SELECT * FROM expenses WHERE cost = :cost AND category = :category")
    List<Expense> getSearchCost(String cost, String category);

//    Get all of the items that match the category and date
    @Query("SELECT * FROM expenses WHERE date = :date AND category = :category")
    List<Expense> getSearchDate(String date, String category);

    //    Get all of the items that match the category and name
    @Query("SELECT * FROM expenses WHERE name = :name AND category = :category")
    List<Expense> getSearchName(String name, String category);
}
