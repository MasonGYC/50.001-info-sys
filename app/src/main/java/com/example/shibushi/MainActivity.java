package com.example.shibushi;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button bLogout, bChangePassword, bImportClothing, bTakePhoto;
    TextView tvWelcome;
    String welcome;
    private FirebaseAuth mAuth;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bLogout = findViewById(R.id.bLogout);
        bChangePassword = findViewById(R.id.bChangePassword);
        bImportClothing = findViewById(R.id.bImportClothing);

        bLogout.setOnClickListener(this);
        bChangePassword.setOnClickListener(this);
        bImportClothing.setOnClickListener(this);

        //Firebase authentication
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        //Welcome message
        //Obviously we don't have to include this but its an option to display the username somewhere
        //somehow
        String username = currentUser.getDisplayName();
        if (username != null){
            welcome = "Welcome, " + currentUser.getDisplayName();
        }
        else {
            welcome = ""; //not sure if it's possible to have a blank string
        }
        tvWelcome = findViewById(R.id.tvWelcome);
        tvWelcome.setText(welcome);

        //take photo
        bTakePhoto = findViewById(R.id.bTakePhoto);
        bTakePhoto.setOnClickListener(this);

    }
    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.bLogout:
                logOut();
                break;
            // Not high priority, may remove
            case R.id.bChangePassword:
                break;
            case R.id.bImportClothing:
                startActivity(new Intent(this, ImportClothing.class));
                break;
            case R.id.bTakePhoto:
                TakePhoto takePhoto = new TakePhoto();
                dispatchTakePictureIntent(takePhoto);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }

    //camera
    public void dispatchTakePictureIntent(TakePhoto takePhoto) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            Log.i("if2","resolveActivity");
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = takePhoto.createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                Log.i("dispatch","if (photoFile != null) ");
                startActivity(takePictureIntent);
                takePhoto.galleryAddPic();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap thumbnail = data.getParcelableExtra("data");
            // Do other work with full size photo saved in locationForPhotos
            //TODO: image label

        }
    }

    // TODO: 3/15/2022 refactor into a new java file 
    private void logOut() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
            mAuth.signOut();
            Toast.makeText(this, user.getEmail()+ "is logged out!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, Login.class));;
        }else{
            Toast.makeText(this, "You aren't logged in yet!", Toast.LENGTH_SHORT).show();
        }
    }







}


/*
* This is the Main screen that the user sees. From here, users can navigate to other activities.
* */
