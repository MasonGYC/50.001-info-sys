package com.example.shibushi.Feed.Profile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shibushi.Models.cUsers;
import com.example.shibushi.R;
import com.example.shibushi.Utils.UniversalImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {
    private static final String TAG = "EditProfileActivity";
    private final Context mContext = EditProfileActivity.this;

    private CircleImageView profile_photo_CIV;
    private TextView emailTV, usernameTV;
    private Button saveChangesBUTTON;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: starting");
        setContentView(R.layout.activity_edit_profile);
        this.setTitle("Edit Profile");

        // Firebase
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        // Widgets setup
        profile_photo_CIV = findViewById(R.id.snippet_edit_profile_center_pp);
        emailTV = findViewById(R.id.snippet_edit_profile_center_tvEmail);
        usernameTV = findViewById(R.id.snippet_edit_profile_center_tvUsername);
        saveChangesBUTTON = findViewById(R.id.activity_edit_profile_save_changes_Button);

        // Set textViews
        emailTV.setText(currentUser.getEmail());
        usernameTV.setText(currentUser.getDisplayName());

        // setup profile image
        initImageLoader();
        initProfileImage();

        // TODO: setup Save Changes to firestore
        setupSaveChangesBUTTON();
    }

    // For setting profile image
    private void initProfileImage() {
        Log.d(TAG, "setProfileImage: setting profile image");
        String current_UserID = currentUser.getUid();
        DocumentReference docRefUser = FirebaseFirestore.getInstance().collection("cUsers").document(current_UserID);
        docRefUser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        cUsers user = document.toObject(cUsers.class);
                        String profile_photo = user.getProfile_photo();

                        StorageReference mStorageReference = FirebaseStorage.getInstance().getReference();
                        mStorageReference.child("images").child(profile_photo).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // Got the download URL for 'users/me/profile.png'
                                // Glide.with(context).load(uri.toString()).into(holder.clothingIV);
                                UniversalImageLoader.setImage(uri.toString(), profile_photo_CIV, null, "");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                            }
                        });

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }

    // TODO: To update firestore with changes
    private void setupSaveChangesBUTTON() {
        EditText new_bio = findViewById(R.id.snippet_edit_profile_center_etBio);
        new_bio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                saveChangesBUTTON.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (editable.toString().isEmpty()) {
                            Toast.makeText(mContext, "Bio cannot be empty!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, "Bio updated!", Toast.LENGTH_SHORT).show();
                            String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            FirebaseFirestore mFirestoreDB = FirebaseFirestore.getInstance();

                            Log.d(TAG, "afterTextChanged: ablabalb");
                            // Update bio in firestore
                            Map<String, Object> bio_map = new HashMap<>();
                            bio_map.put("bio", editable.toString());
                            mFirestoreDB.collection("cUsers").document(userID).update(bio_map);

                            Intent intent = new Intent(mContext, Profile.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP); // Prevents going back to prev page when back button is pressed
                            startActivity(intent);
                        }
                    }
                });

            }
        });

    }

    /**
     * Initialise ImageLoader
     * Quick Setup Src- https://github.com/nostra13/Android-Universal-Image-Loader/wiki/Quick-Setup
     */
    private void initImageLoader() {
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(mContext);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }
}
