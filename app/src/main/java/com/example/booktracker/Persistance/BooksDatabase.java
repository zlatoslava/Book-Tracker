package com.example.booktracker.Persistance;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.booktracker.Models.Book;

@Database(entities = {Book.class}, version = 1, exportSchema = false)
public abstract class BooksDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "books_db";

    private static BooksDatabase instance;

    static BooksDatabase getInstance(final Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                                            BooksDatabase.class,
                                            DATABASE_NAME).build();
        }
        return instance;
    }

    public abstract BookDao getBookDao();
}
