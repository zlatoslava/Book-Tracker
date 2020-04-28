package com.example.booktracker.data;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.booktracker.data.remote.RetrofitApi;
import com.example.booktracker.data.remote.RetrofitInstance;
import com.example.booktracker.models.Book;
import com.example.booktracker.models.Result;
import com.example.booktracker.models.Results;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WebServiceRepository {

    private static WebServiceRepository sWebServiceRepository;
    private RetrofitApi mRetrofitApi;

    public static WebServiceRepository getInstance() {
        if(sWebServiceRepository == null){
            sWebServiceRepository = new WebServiceRepository();
        }
        return sWebServiceRepository;
    }

    public WebServiceRepository(){
        mRetrofitApi = RetrofitInstance.getInstance().create(RetrofitApi.class);
    }

    public LiveData<List<Book>> getBooks(String search){
        MutableLiveData<List<Book>> books = new MutableLiveData<>();

        mRetrofitApi.getBooks(search).enqueue(new Callback<Results>() {
            @Override
            public void onResponse(Call<Results> call, Response<Results> response) {
                if(response.isSuccessful()){
                    List<Result> results = response.body().getItems();
                    List<Book> bookList = new ArrayList<>();
                    for(Result result: results){
                        bookList.add(result.getBook());
                    }
                    books.setValue(bookList);
                } else {
                    books.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<Results> call, Throwable t) {
                books.setValue(null);
            }
        });

        return books;
    }


}
