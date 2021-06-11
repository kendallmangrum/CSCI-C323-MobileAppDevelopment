package com.c323proj9.kmangrum;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Collections;
import java.util.zip.CheckedOutputStream;

// Create table and Expense class with all the getters and setters
@Entity(tableName = "expenses")
public class Expense implements Comparable<Expense> {
    @PrimaryKey (autoGenerate = true)
    @NonNull private int id;
    private String name;
    private String cost;
    private String date;
    private String category;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public int compareTo(Expense o) {
        return getDate().compareTo(o.getDate());
    }

}
