package com.c323proj11.kmangrum;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

//  Create the database object
@Database(entities = {Recipe.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract RecipeDao getRecipeDao();

    public static AppDatabase INSTANCE;

    private static String DATABASE_NAME = "favoriteRecipes";

    public static AppDatabase getDBInstance(Context context) {
//        Check condition
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME).allowMainThreadQueries().build();
        }
        return INSTANCE;
    }
}
