package com.example.booktracker.view.ui;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

import com.example.booktracker.R;
import com.example.booktracker.databinding.SettingsActivityBinding;
import com.example.booktracker.viewModels.BookListViewModel;

public class SettingsActivity extends AppCompatActivity {

    SettingsActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SettingsActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();

        setSupportActionBar(binding.toolbarSettingsActivity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        SwitchPreference switchPreference;
        Preference infoPreference;
        Preference deletePreference;
        SharedPreferences preferences;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            switchPreference = getPreferenceManager().findPreference("theme");
            infoPreference = getPreferenceManager().findPreference("info");
            deletePreference = getPreferenceManager().findPreference("clear");
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

            setChangeThemePreference();
            setInfoPreference();
            setDeleteDataPreference();

            return super.onCreateView(inflater, container, savedInstanceState);
        }

        private void setInfoPreference() {
            infoPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setView(R.layout.about_an_app_dialog);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    return true;
                }
            });
        }

        private void setChangeThemePreference() {
            boolean useDarkTheme = preferences.getBoolean("theme", false);

            if (useDarkTheme) switchPreference.setChecked(true);
            else switchPreference.setChecked(false);

            switchPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {

                    if (switchPreference.isChecked()) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    }
                    return true;
                }
            });
        }

        private void setDeleteDataPreference(){
            deletePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    BookListViewModel bookListViewModel = new ViewModelProvider(getActivity()).get(BookListViewModel.class);

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                            .setMessage(R.string.dialog_delete_all_books)
                            .setPositiveButton(R.string.dialog_delete_option, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    bookListViewModel.deleteAllBooks();
                                    Toast.makeText(getContext(), R.string.toast_all_books_deleted, Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton(R.string.dialog_negative_option, null);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    return true;
                }
            });

        }
    }
}