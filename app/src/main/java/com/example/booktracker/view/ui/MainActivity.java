package com.example.booktracker.view.ui;

import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.booktracker.R;
import com.example.booktracker.databinding.ActivityMainBinding;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initializeViews();
        setInitialFragment();
    }

    public void initializeViews() {
        binding.fabSearch.setOnClickListener(this);
        binding.fabEnterManually.setOnClickListener(this);

        setSupportActionBar(binding.toolbar);

        binding.bottomNavigation.setOnNavigationItemSelectedListener(mNavListener);
    }

    private void setInitialFragment() {
        BooksListFragment fragment = new BooksListFragment();
        fragment.setTypeOfBooks("READ");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
        binding.toolbar.setTitle(R.string.read_books);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_menu_option:

                return true;
            case R.id.toolbar_settings:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_search:
                binding.fabMenu.collapse();
                Intent searchIntent = new Intent(this, BookSearchActivity.class);
                startActivity(searchIntent);
                break;

            case R.id.fab_enter_manually:
                binding.fabMenu.collapse();
                Intent intent = new Intent(this, BookActivity.class);
                startActivity(intent);
                break;
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mNavListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            BooksListFragment bookFragment = new BooksListFragment();
            switch (item.getItemId()) {
                case R.id.read_books:
                    bookFragment.setTypeOfBooks("READ");
                    binding.toolbar.setTitle(R.string.read_books);
                    break;
                case R.id.unfinished_books:
                    bookFragment.setTypeOfBooks("UNFINISHED");
                    binding.toolbar.setTitle(R.string.unfinished_books);
                    break;
                case R.id.books_to_read:
                    bookFragment.setTypeOfBooks("TOREAD");
                    binding.toolbar.setTitle(R.string.books_to_read);
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, bookFragment)
                    .commit();

            return true;
        }
    };


}
