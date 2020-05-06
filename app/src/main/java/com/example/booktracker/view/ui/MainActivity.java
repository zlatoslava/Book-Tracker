package com.example.booktracker.view.ui;

import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.booktracker.R;
import com.example.booktracker.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;


public class MainActivity extends AppCompatActivity implements  SpeedDialView.OnActionSelectedListener {

    ActivityMainBinding binding;
    private int mLastDayNightMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initializeViews();
        setInitialFragment();

        if (savedInstanceState != null) {
            mLastDayNightMode = AppCompatDelegate.getDefaultNightMode();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (AppCompatDelegate.getDefaultNightMode() != mLastDayNightMode) {
            recreate();
        }
    }

    public void initializeViews() {
        binding.fabMenu.addActionItem( new SpeedDialActionItem.Builder(R.id.fab_search, R.drawable.search)
                .setFabBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.color_secondary_variant, getTheme()))
                .setFabImageTintColor(ResourcesCompat.getColor(getResources(), R.color.color_on_primary, getTheme()))
                .setLabel(getString(R.string.fab_search))
                .setFabSize(FloatingActionButton.SIZE_NORMAL)
                .create());
        binding.fabMenu.addActionItem(new SpeedDialActionItem.Builder(R.id.fab_enter_manually, R.drawable.edit)
                .setFabBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.color_secondary_variant, getTheme()))
                .setFabImageTintColor(ResourcesCompat.getColor(getResources(), R.color.color_on_primary, getTheme()))
                .setLabel(getString(R.string.fab_enter))
                .setFabSize(FloatingActionButton.SIZE_NORMAL)
                .create());
        binding.fabMenu.setOnActionSelectedListener(this);

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
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mNavListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            binding.fabMenu.close();
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


    @Override
    public boolean onActionSelected(SpeedDialActionItem actionItem) {
        switch(actionItem.getId()){
            case R.id.fab_enter_manually:
                Intent intent = new Intent(this, BookActivity.class);
                startActivity(intent);
                binding.fabMenu.close();
                return true;
            case R.id.fab_search:
                Intent searchIntent = new Intent(this, BookSearchActivity.class);
                startActivity(searchIntent);
                binding.fabMenu.close();
                return true;
        }
        return false;
    }
}
