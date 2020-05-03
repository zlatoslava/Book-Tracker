package com.example.booktracker.viewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.booktracker.data.DataBaseRepository;
import com.example.booktracker.data.models.Book;

import java.util.List;

public class BookListViewModel extends AndroidViewModel {

    private DataBaseRepository mRepository;

    private LiveData<List<Book>> mAllBooks;

    public BookListViewModel(Application application) {
        super(application);
        mRepository = new DataBaseRepository(application);
        mAllBooks = mRepository.getAllBooks();
    }

    public LiveData<List<Book>> getAllBooks() {
        return mAllBooks;
    }

//    public LiveData<List<Book>> getBooksByAuthor(String author) {
//        return mRepository.getBooksByAuthorTask(author);
//    }

    public LiveData<List<Book>> getBooksByStatus(String status) {
        return mRepository.getBooksByStatusTask(status);
    }
}
