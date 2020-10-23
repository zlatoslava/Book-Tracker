package com.example.booktracker.view.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.booktracker.BuildConfig;
import com.example.booktracker.databinding.ActivityBookBinding;
import com.example.booktracker.data.models.Book;
import com.example.booktracker.R;
import com.example.booktracker.viewModels.SingleBookViewModel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BookActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA_IMAGE = 1;
    private static final int REQUEST_GALLERY_IMAGE = 2;
    private static final int STORAGE_PERMISSION_CODE = 10;

    private SingleBookViewModel mSingleBookViewModel;
    private ActivityBookBinding binding;
    private String cameraFilePath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_book);

        mSingleBookViewModel = new ViewModelProvider(this).get(SingleBookViewModel.class);
        if(savedInstanceState == null) {
            mSingleBookViewModel.setNewBook(getBookFromIntent());
            mSingleBookViewModel.setMode(getModeFromIntent());
        }
        initializeViews();
        binding.setBook(mSingleBookViewModel);

        showToast();
    }

    private Book getBookFromIntent(){
        Intent incomingIntent = getIntent();

        if (incomingIntent.hasExtra("selected_book")) {
            return incomingIntent.getParcelableExtra("selected_book");
        }
        return new Book();
    }

    private int getModeFromIntent(){
        Intent incomingIntent = getIntent();

        if (incomingIntent.hasExtra("selected_book")) {
            if (incomingIntent.getBooleanExtra("isFromWeb", false)) {
                return SingleBookViewModel.WEB_MODE;
            } else return SingleBookViewModel.INFO_MODE;
        }
        return SingleBookViewModel.NEW_BOOK_MODE;
    }

    private void initializeViews() {
        binding.toolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_choices, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinner.setAdapter(adapter);
    }

    private void disableInteractions() {
        binding.editText.setInputType(InputType.TYPE_NULL);
        binding.editText2.setInputType(InputType.TYPE_NULL);
        binding.imageViewBook.setEnabled(false);
        binding.ratingBar.setIsIndicator(true);
        binding.spinner.setEnabled(false);
    }

    private void enableInteractions() {
        binding.editText.setInputType(InputType.TYPE_CLASS_TEXT);
        binding.editText2.setInputType(InputType.TYPE_CLASS_TEXT);
        binding.imageViewBook.setEnabled(true);
        binding.ratingBar.setIsIndicator(false);
        binding.spinner.setEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_book_menu, menu);
        MenuItem editItem = menu.findItem(R.id.edit);
        MenuItem deleteItem = menu.findItem(R.id.delete);
        MenuItem saveItem = menu.findItem(R.id.save);

        mSingleBookViewModel.getMode().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer mode) {
                switch (mode){
                    case SingleBookViewModel.WEB_MODE:
                    case SingleBookViewModel.NEW_BOOK_MODE:
                        editItem.setVisible(false);
                        deleteItem.setVisible(false);
                        break;
                    case SingleBookViewModel.INFO_MODE:
                        saveItem.setVisible(false);
                        deleteItem.setVisible(true);
                        editItem.setVisible(true);
                        disableInteractions();
                        break;
                    case SingleBookViewModel.EDIT_MODE:
                        deleteItem.setVisible(false);
                        editItem.setVisible(false);
                        break;
                }
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                saveBook();
                return true;
            case R.id.edit:
                editBook();
                return true;
            case R.id.delete:
                deleteBook();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void editBook() {
        binding.toolbar.setTitle(getString(R.string.edit_book_title));
        enableInteractions();
        mSingleBookViewModel.editBook();
        invalidateOptionsMenu();
    }

    private void deleteBook(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage(R.string.dialog_delete_book)
                .setPositiveButton(R.string.dialog_delete_option, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSingleBookViewModel.deleteBook();
                        finish();
                    }
                })
                .setNegativeButton(R.string.dialog_negative_option, null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void saveBook(){
        mSingleBookViewModel.saveBook();
    }

    private void showToast(){
        mSingleBookViewModel.getToastMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String toast) {
                Toast.makeText(BookActivity.this, toast, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void takePhoto(View view) {
        final String takePhoto = getString(R.string.dialog_take_photo);
        final String chooseGallery = getString(R.string.dialog_choose_gallery);
        final String cancel = getString(R.string.dialog_negative_option);
        final CharSequence[] options = {takePhoto, chooseGallery, cancel};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.dialog_choose_book_cover));
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String option = options[which].toString();
                if(option.equals(takePhoto)){
                    if (ContextCompat.checkSelfPermission(BookActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                            startCameraIntent();
                        } else {
                            requestStoragePermission();
                        }
                } else if(option.equals(chooseGallery)){
                    startGalleryIntent();
                } else {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void startCameraIntent() {
        try {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(
                    BookActivity.this, BuildConfig.APPLICATION_ID + ".provider", createImageFile()
            ));
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_CAMERA_IMAGE);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void startGalleryIntent() {
        Intent pickPhotoIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        pickPhotoIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

        startActivityForResult(pickPhotoIntent, REQUEST_GALLERY_IMAGE);
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            new AlertDialog.Builder(this)
                    .setTitle(R.string.permission_needed)
                    .setMessage(R.string.permission_explanation)
                    .setPositiveButton(R.string.dialog_positive_option, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(BookActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton(R.string.dialog_negative_option, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, R.string.permission_granted, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri imageUri = Uri.parse("");
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CAMERA_IMAGE:
                    imageUri = Uri.parse(cameraFilePath);
                    break;

                case REQUEST_GALLERY_IMAGE:
                    imageUri = data.getData();
                    break;
            }
        }
        loadImage(imageUri);
        mSingleBookViewModel.setImageUri(imageUri.toString());
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        cameraFilePath = "file://" + image.getAbsolutePath();
        return image;
    }

    private void loadImage(Uri imageUri){
        Glide.with(this)
                .load(imageUri)
                .centerCrop()
                .into(binding.imageViewBook);
    }
}
