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
import android.util.Log;
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

public class AddNewBookActivity extends AppCompatActivity {


    private static final int REQUEST_CAMERA_IMAGE = 1;
    private static final int REQUEST_GALLERY_IMAGE = 2;
    private static final int STORAGE_PERMISSION_CODE = 10;

    private BookViewModel mBookViewModel;
    private Toolbar mToolbar;
    private EditText mEditTextTitle;
    private EditText mEditTextAuthor;
    private ImageButton mImageButton;
    private RatingBar mRatingBar;
    private Spinner mSpinner;

    private Book mBook;

    private String cameraFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_book);

        initializeViews();

        mBookViewModel = new ViewModelProvider(this).get(BookViewModel.class);
        mBook = new Book();

    }

    private void initializeViews() {
        mToolbar = findViewById(R.id.toolbar);
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
        getMenuInflater().inflate(R.menu.add_new_book_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_new_book:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

        mBook.setName(title);
        mBook.setAuthor(author);
        mBook.setRating(rating);
        mBook.setStatus(status);

        mBookViewModel.insert(mBook);

        finish();
    }

    public void takePhoto(View view) {

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_IMAGE_CAPTURE);
            }
        }*/

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose book cover");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (options[which].toString()) {
                    case "Take Photo":
                        if(ContextCompat.checkSelfPermission(AddNewBookActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
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
                    AddNewBookActivity.this, BuildConfig.APPLICATION_ID + ".provider", createImageFile()
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
                            ActivityCompat.requestPermissions(AddNewBookActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
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
                    Uri imageUriCamera = Uri.parse(cameraFilePath);

                    Glide.with(this)
                            .load(imageUriCamera)
                            .centerCrop()
                            .into(mImageButton);

                    mBook.setImageUrl(imageUriCamera.toString());

                    break;

                case REQUEST_GALLERY_IMAGE:
                    Uri imageUriGallery = data.getData();

                    Glide.with(this)
                            .load(imageUriGallery)
                            .centerCrop()
                            .into(mImageButton);

                    mBook.setImageUrl(imageUriGallery.toString());
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
    }

