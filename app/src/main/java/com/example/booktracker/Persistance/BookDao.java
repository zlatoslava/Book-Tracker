package com.example.booktracker.Persistance;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.booktracker.Models.Book;

import java.util.List;

@Dao
public interface BookDao {

    @Insert
    void insertBook(Book book);

    @Query("SELECT * FROM books")
    LiveData<List<Book>> getAllBooks();

    @Query("SELECT * FROM books WHERE author = :name")
    LiveData<List<Book>> getBooksByAuthor(String name);

    @Query("SELECT * FROM books WHERE status = :status")
    LiveData<List<Book>> getBooksByStatus(String status);

    @Delete
    int deleteBooks(Book... books);

    @Update
    int updateBooks(Book... books);
}
