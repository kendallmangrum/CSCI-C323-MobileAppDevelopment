package com.c323FinalProject.kmangrum;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.c323FinalProject.kmangrum.daos.CategoryDAO;
import com.c323FinalProject.kmangrum.daos.FavoritesDAO;
import com.c323FinalProject.kmangrum.daos.ReviewDAO;
import com.c323FinalProject.kmangrum.daos.ReviewListDAO;
import com.c323FinalProject.kmangrum.daos.TrashDAO;
import com.c323FinalProject.kmangrum.daos.UserDAO;
import com.c323FinalProject.kmangrum.entities.Category;
import com.c323FinalProject.kmangrum.entities.Favorites;
import com.c323FinalProject.kmangrum.entities.Review;
import com.c323FinalProject.kmangrum.entities.ReviewList;
import com.c323FinalProject.kmangrum.entities.Trash;
import com.c323FinalProject.kmangrum.entities.User;

import java.util.concurrent.Executors;

@Database(entities = {User.class, Category.class, ReviewList.class, Review.class, Favorites.class, Trash.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {

    public abstract CategoryDAO getCategoryDAO();

    public abstract ReviewListDAO getReviewListDAO();

    public abstract ReviewDAO getReviewDAO();

    public abstract FavoritesDAO getFavoritesDAO();

    public abstract TrashDAO getTrashDAO();

    public static AppDatabase INSTANCE;

    private static String DATABASE_NAME = "ReviewDatabase";

    public static AppDatabase getDBInstance(Context context) {
//        Check condition
        if (INSTANCE == null) {
//            If database hasn't been created before, prepopulate it
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME).fallbackToDestructiveMigration().allowMainThreadQueries().addCallback(new Callback() {
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);
                    Executors.newSingleThreadExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            getDBInstance(context).getCategoryDAO().insert(Category.PrepopulateCategories(context));
                            getDBInstance(context).getReviewDAO().insert(Review.PrepopulateReviews(context));
                            getDBInstance(context).getFavoritesDAO().insert(Favorites.PrepopulateFavorites(context));
                            getDBInstance(context).getTrashDAO().insert(Trash.PrepopulateTrash(context));
                        }
                    });
                }
            }).build();
        }
        return INSTANCE;
    }
}
