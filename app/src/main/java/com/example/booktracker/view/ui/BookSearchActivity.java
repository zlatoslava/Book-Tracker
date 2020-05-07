package com.example.booktracker.view.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;

import com.example.booktracker.databinding.ActivityBookSearchBinding;
import com.example.booktracker.view.adapter.MyRecyclerAdapter;
import com.example.booktracker.data.models.Book;
import com.example.booktracker.viewModels.BookSearchViewModel;

import java.util.ArrayList;
import java.util.List;

public class BookSearchActivity extends AppCompatActivity implements MyRecyclerAdapter.OnBookListener, SearchView.OnQueryTextListener {

    private static final String TAG = "mtag";

    ActivityBookSearchBinding binding;
    private MyRecyclerAdapter mAdapter;
    private BookSearchViewModel mBookSearchViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookSearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mBookSearchViewModel = new BookSearchViewModel();

        initializeViews();
    }
    private boolean haveNetwork(){
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return  networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    private void initializeViews(){
        setSupportActionBar(binding.toolbarSearchActivity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.recyclerViewSearchActivity.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MyRecyclerAdapter(new ArrayList<>(), this, this);
        binding.recyclerViewSearchActivity.setAdapter(mAdapter);

        binding.searchViewSearchActivity.onActionViewExpanded();
        binding.searchViewSearchActivity.setOnQueryTextListener(this);

        if(!haveNetwork()){
            setNoInternetVisibility();
        }
    }

    @Override
    public void onBookClicked(int position) {
            Intent intent = new Intent(this, BookActivity.class);
            Book selectedBook = mAdapter.getBookAt(position);
            selectedBook.setStatus("READ");
            intent.putExtra("selected_book", selectedBook);
            intent.putExtra("isFromWeb", true);
            startActivity(intent);
            finish();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if(haveNetwork()) {
            setHasInternetVisibility();
            mBookSearchViewModel.getBooksFromWeb(query).observe(this, new Observer<List<Book>>() {
                @Override
                public void onChanged(List<Book> books) {
                    mAdapter.setData(books);
                    mAdapter.notifyDataSetChanged();
                }
            });
        } else {
            setNoInternetVisibility();
        }
        return true;
    }

    private void setNoInternetVisibility(){
        binding.noInternetConnectionLayout.setVisibility(View.VISIBLE);
        binding.recyclerViewSearchActivity.setVisibility(View.INVISIBLE);
    }

    private void setHasInternetVisibility(){
        binding.noInternetConnectionLayout.setVisibility(View.GONE);
        binding.recyclerViewSearchActivity.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
