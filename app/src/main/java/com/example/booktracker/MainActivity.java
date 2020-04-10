package com.example.booktracker;

import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.booktracker.Fragments.BooksToReadFragment;
import com.example.booktracker.Fragments.ReadBooksFragment;
import com.example.booktracker.Fragments.UnfinishedBooksFragment;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    private FloatingActionsMenu mFloatingActionsMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
    }

    public void initializeViews(){
        mFloatingActionsMenu = findViewById(R.id.fab_menu);
        FloatingActionButton fabSearch = findViewById(R.id.fab_search);
        fabSearch.setOnClickListener(this);
        FloatingActionButton fabEnterManually = findViewById(R.id.fab_enter_manually);
        fabEnterManually.setOnClickListener(this);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(mNavListener);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new ReadBooksFragment())
                .commit();
        mToolbar.setTitle(R.string.read_books);
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
                mFloatingActionsMenu.collapse();
                //search book via Google !!! Make !!!
                break;
            case R.id.fab_enter_manually:
                mFloatingActionsMenu.collapse();
                Intent intent = new Intent(this, AddNewBookActivity.class);
                startActivity(intent);
                break;
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mNavListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.read_books:
                    selectedFragment = new ReadBooksFragment();
                    mToolbar.setTitle(R.string.read_books);
                    break;
                case R.id.unfinished_books:
                    selectedFragment = new UnfinishedBooksFragment();
                    mToolbar.setTitle(R.string.unfinished_books);
                    break;
                case R.id.books_to_read:
                    selectedFragment = new BooksToReadFragment();
                    mToolbar.setTitle(R.string.books_to_read);
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment)
                    .commit();

            return true;
        }
    };


}