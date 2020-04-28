package com.example.booktracker.viewModels;

import androidx.lifecycle.LiveData;

import com.example.booktracker.data.WebServiceRepository;
import com.example.booktracker.models.Book;

import java.util.List;

public class BookSearchViewModel {

    private WebServiceRepository mWebServiceRepository;
    //private LiveData<List<Book>> mBooks;

    public BookSearchViewModel(){
        mWebServiceRepository = WebServiceRepository.getInstance();
    }

    public LiveData<List<Book>> getBooksFromWeb(String search){
        return mWebServiceRepository.getBooks(search);
    }
}
