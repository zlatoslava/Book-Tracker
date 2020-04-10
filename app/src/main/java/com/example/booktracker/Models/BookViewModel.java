package com.example.booktracker.Models;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.booktracker.Persistance.Repository;

import java.util.List;

public class BookViewModel extends AndroidViewModel {

    private Repository mRepository;

    private LiveData<List<Book>> mAllBooks;

    public BookViewModel(Application application) {
        super(application);
        mRepository = new Repository(application);
        mAllBooks = mRepository.getAllBooks();
    }

    public LiveData<List<Book>> getAllBooks() {
        return mAllBooks;
    }

    public LiveData<List<Book>> getBooksByAuthor(String author) {
        return mRepository.getBooksByAuthorTask(author);
    }

    public LiveData<List<Book>> getBooksByStatus(String status) {
        return mRepository.getBooksByStatusTask(status);
    }

    public void insert(Book book) {
        mRepository.insertBookTask(book);
    }

    public void delete(Book... books) {
        mRepository.deleteBooksTask(books);
    }

    public void update(Book... books) {
        mRepository.updateBooksTask(books);
    }
}
