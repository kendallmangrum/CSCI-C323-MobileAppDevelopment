package com.c323proj9.kmangrum;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

//  Create the database object
@Database(entities = {Expense.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract ExpenseDao getExpenseDao();

    public static AppDatabase INSTANCE;

    private static String DATABASE_NAME = "expenses";

    public static AppDatabase getDBInstance(Context context) {
//        check condition
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME).allowMainThreadQueries().build();
        }
        return INSTANCE;
    }

}
