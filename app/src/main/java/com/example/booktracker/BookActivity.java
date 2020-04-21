package com.example.booktracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.booktracker.Models.Book;
import com.example.booktracker.Models.BookViewModel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BookActivity extends AppCompatActivity {


    private static final int REQUEST_CAMERA_IMAGE = 1;
    private static final int REQUEST_GALLERY_IMAGE = 2;
    private static final int STORAGE_PERMISSION_CODE = 10;

    public static final int EDIT_MODE = 0;
    private static final int INFO_MODE = 1;
    public static final int  NEW_BOOK_MODE = 2;

    private BookViewModel mBookViewModel;
    private Toolbar mToolbar;
    private EditText mEditTextTitle;
    private EditText mEditTextAuthor;
    private ImageButton mImageButton;
    private RatingBar mRatingBar;
    private Spinner mSpinner;

    private Book mBook;
    private boolean mIsNewBook;
    private int mMode;

    private String cameraFilePath;
    private Uri mImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        initializeViews();

        mBookViewModel = new ViewModelProvider(this).get(BookViewModel.class);
        mBook = new Book();

        if(isNewBook()){
            mToolbar.setTitle("Add new book");
            mMode = NEW_BOOK_MODE;
        } else {
            setBookInformation();
            disableInteractions();
            mMode = INFO_MODE;
        }
    }

    private void setBookInformation() {
        mEditTextTitle.setText(mBook.getName());
        mEditTextAuthor.setText(mBook.getAuthor());
        mRatingBar.setRating(mBook.getRating());
        switch(mBook.getStatus()){
            case "READ":
                mSpinner.setSelection(0);
                break;
            case "TOREAD":
                mSpinner.setSelection(2);
                break;
            case "UNFINISHED":
                mSpinner.setSelection(1);
                break;
        }

        Uri imageUri = Uri.parse("some_uri");

        try{
            imageUri = Uri.parse(mBook.getImageUrl());
        } catch (NullPointerException ex){
            Toast.makeText(this, "NullPointerException", Toast.LENGTH_SHORT).show(); //TODO change
        }

        Glide.with(this)
                .load(imageUri)
                .centerCrop()
                .error(R.drawable.book_cover_placeholder)
                .into(mImageButton);
    }

    private void disableInteractions(){
        mEditTextTitle.setInputType(InputType.TYPE_NULL);
        mEditTextAuthor.setInputType(InputType.TYPE_NULL);
        mImageButton.setEnabled(false);
        mRatingBar.setIsIndicator(true);
        mSpinner.setEnabled(false);
    }

    private void enableInteractions(){
        mEditTextTitle.setInputType(InputType.TYPE_CLASS_TEXT);
        mEditTextAuthor.setInputType(InputType.TYPE_CLASS_TEXT);
        mImageButton.setEnabled(true);
        mRatingBar.setIsIndicator(false);
        mSpinner.setEnabled(true);
    }

    private boolean isNewBook(){
        if(getIntent().hasExtra("selected_book")){
            mBook = getIntent().getParcelableExtra("selected_book");

            mIsNewBook = false;
            return false;
        }

        mIsNewBook = true;
        return true;
    }

    private void initializeViews() {
        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSpinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_choices, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);

        mEditTextTitle = findViewById(R.id.editText);
        mEditTextAuthor = findViewById(R.id.editText2);
        mImageButton = findViewById(R.id.imageButton);
        mRatingBar = findViewById(R.id.ratingBar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_book_menu, menu);
        MenuItem editItem = menu.findItem(R.id.edit);
        MenuItem deleteItem = menu.findItem(R.id.delete);
        MenuItem saveItem = menu.findItem(R.id.save);

        switch (mMode){
            case NEW_BOOK_MODE:
                editItem.setVisible(false);
                deleteItem.setVisible(false);
                break;
            case INFO_MODE:
                saveItem.setVisible(false);
                break;
            case EDIT_MODE:
                deleteItem.setVisible(false);
                editItem.setVisible(false);
                break;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                saveNote();
                return true;
            case R.id.edit:
                editNote();
                return true;
            case R.id.delete:
                deleteBook();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void editNote(){
        mToolbar.setTitle("Edit book");
        enableInteractions();
        mMode = EDIT_MODE;
        invalidateOptionsMenu();
    }

    private void deleteBook(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage(R.string.dialog_delete_book)
                .setPositiveButton(R.string.dialog_positive_option, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mBookViewModel.delete(mBook);
                        finish();
                    }
                })
                .setNegativeButton(R.string.dialog_negative_option, null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void saveNote() {
        String title = mEditTextTitle.getText().toString();
        String author = mEditTextAuthor.getText().toString();
        int rating = (int) mRatingBar.getRating();
        String status = "";

        switch (mSpinner.getSelectedItem().toString()) {
            case "Finished book":
                status = "READ";
                break;
            case "Unfinished book":
                status = "UNFINISHED";
                break;
            case "Book to read":
                status = "TOREAD";
                break;
        }

        if (title.trim().isEmpty() || author.trim().isEmpty()) {
            Toast.makeText(this, R.string.toast_input_data, Toast.LENGTH_SHORT).show();
            return;
        }

        if(rating == 0){
            Toast.makeText(this, R.string.toast_input_rating, Toast.LENGTH_SHORT).show();
            return;
        }

        mBook.setName(title);
        mBook.setAuthor(author);
        mBook.setRating(rating);
        mBook.setStatus(status);
        //mBook.setImageUrl(mImageUri.toString());

        if(mIsNewBook){
            mBookViewModel.insert(mBook);
        } else {
            mBookViewModel.update(mBook);
        }

        finish();

    }

    public void takePhoto(View view) {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose book cover");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (options[which].toString()) {
                    case "Take Photo":
                        if(ContextCompat.checkSelfPermission(BookActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                            startCameraIntent();
                        } else {
                            requestStoragePermission();
                        }
                        break;

                    case "Choose from Gallery":
                        Intent pickPhotoIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        pickPhotoIntent.setType("image/*");
                        startActivityForResult(pickPhotoIntent, REQUEST_GALLERY_IMAGE);
                        break;

                    case "Cancel":
                        dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void startCameraIntent(){
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

    private void requestStoragePermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed for saving images taken by camera")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(BookActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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
        if(requestCode == STORAGE_PERMISSION_CODE ){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CAMERA_IMAGE:
                    //Uri imageUriCamera = Uri.parse(cameraFilePath);

                    mImageUri = Uri.parse(cameraFilePath);
                    Glide.with(this)
                            .load(mImageUri)
                            .centerCrop()
                            .into(mImageButton);

                    mBook.setImageUrl(mImageUri.toString());
                    //TODO: save Image Uri to global variable

                    break;

                case REQUEST_GALLERY_IMAGE:
                    //Uri imageUriGallery = data.getData();

                    mImageUri = data.getData();
                    Glide.with(this)
                            .load(mImageUri)
                            .centerCrop()
                            .into(mImageButton);

                    mBook.setImageUrl(mImageUri.toString());
                    break;
            }


        }
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

    @Override
    public void onBackPressed() {
        if(mMode == EDIT_MODE){
            mMode = INFO_MODE;
            invalidateOptionsMenu();
            setBookInformation();
            disableInteractions();
        }
        else super.onBackPressed();
    }
}


