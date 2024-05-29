package com.example.savingsapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class PhotoActivity extends BaseActivity implements View.OnClickListener{
    String currentPhotoPath;
    String dbPhotoPath;
    int REQUEST_IMAGE_CAPTURE = 1;

    CircleImageView mImageView;
    Button captureButton;
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        //init views
        initViews();
        //initialize database
        initAppDb();
    }

    @SuppressLint("SimpleDateFormat")
    private File createImageFile() throws IOException {
        // Create an image file name

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @SuppressLint("QueryPermissionsNeeded")
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                //takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Image captured and saved to fileUri specified in the Intent
            // Show the image in the ImageView
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageBitmap = cropAndScale(imageBitmap, 300);
            mImageView.setImageBitmap(imageBitmap);
            savePhoto(imageBitmap);
        }
    }

    void savePhoto(Bitmap bitmap){
        File dir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "ginafi_photos");
        if(!dir.exists()){
            dir.mkdir();
        }

        //String path = Environment.getExternalStorageDirectory().toString() + "/profile_photo.png";
        dbPhotoPath = dir.getAbsolutePath() + "/profile_photo.png";
        File file = new File(dir, "profile_photo-" + UUID.randomUUID() + ".png");

        try {
            getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    MediaStore.Images.Media.DATA + "=?", new String[]{file.getAbsolutePath()});
            //Files.deleteIfExists(file.toPath());
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
            out.flush();
            MediaStore.Images.Media.insertImage(getContentResolver(),file.getAbsolutePath(),file.getName(),file.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initViews(){
        mImageView = findViewById(R.id.photo);

        captureButton = findViewById(R.id.capture_btn);
        captureButton.setOnClickListener(this);

        saveButton = findViewById(R.id.save_btn);
        saveButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.capture_btn){
            if(!isPermissionGranted("android.permission.CAMERA")){
                requestPermissions(new String[]{
                        "android.permission.CAMERA",
                        "android.permission.WRITE_EXTERNAL_STORAGE",
                        "android.permission.READ_EXTERNAL_STORAGE"
                }, 1);
            }else{
                dispatchTakePictureIntent();
            }
        }
        else if(v.getId() == R.id.save_btn){
            if(dbPhotoPath == null){
                showToast("Please take a photo first");
                return;
            }
            //save the image to the database
            //get the user id
            Intent intent = getIntent();
            long userId = intent.getLongExtra("userId", 0);
            //save the image to the database
            runInBackground(() -> {
                appDatabase.userDao().updateUser((int) userId, dbPhotoPath);
                runOnUiThread(() -> {
                    showToast("Photo saved successfully");
                    gotoActivity(this, MainActivity.class);
                });
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            }
        }
    }

    public static  Bitmap cropAndScale (Bitmap source, int scale){
        int factor = source.getHeight() <= source.getWidth() ? source.getHeight(): source.getWidth();
        int longer = source.getHeight() >= source.getWidth() ? source.getHeight(): source.getWidth();
        int x = source.getHeight() >= source.getWidth() ?0:(longer-factor)/2;
        int y = source.getHeight() <= source.getWidth() ?0:(longer-factor)/2;
        source = Bitmap.createBitmap(source, x, y, factor, factor);
        source = Bitmap.createScaledBitmap(source, scale, scale, false);
        return source;
    }
}