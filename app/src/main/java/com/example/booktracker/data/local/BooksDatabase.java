package com.example.booktracker.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.booktracker.data.models.Book;

@Database(entities = {Book.class}, version = 1, exportSchema = false)
public abstract class BooksDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "books_db";

    private static BooksDatabase instance;

    static public BooksDatabase getInstance(final Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                                            BooksDatabase.class,
                                            DATABASE_NAME).build();
        }
        return instance;
    }

    public abstract BookDao getBookDao();
}
