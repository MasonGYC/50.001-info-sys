package com.example.shibushi.Feed.Profile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shibushi.Feed.FeedActivity;
import com.example.shibushi.Models.cOutfits;
import com.example.shibushi.Models.cUsers;
import com.example.shibushi.R;
import com.example.shibushi.Utils.BottomNavigationViewHelper;
//import com.example.shibushi.Utils.GridImageAdapter;
import com.example.shibushi.Utils.ProfileParentAdapter;
import com.example.shibushi.Utils.UniversalImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class Profile extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";
    private Context mContext = Profile.this;
    private static final int b_menu_ACTIVTY_NUM = 0; // Bottom navbar activity number
    private TextView mFollowers;
    private TextView mFollowing;
    private TextView mUsername;
    private TextView mOutfits;
    private TextView mBio;
    private Button mEditProfile;
    private ProgressBar mProgressBar;
    private Toolbar toolbar;
    private ImageView profilePhoto;
    // RCViews
    private RecyclerView parentRecyclerView;
    private ProfileParentAdapter profileParentAdapter;

    // Firestore
    StorageReference mStorageReference = FirebaseStorage.getInstance().getReference();
    private FirebaseFirestore mDatabase;
    private String current_UserID;

    // Model
    private cUsers user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Log.d(TAG, "onCreate: started");

        setupActivityWidgets();
        setupToolBar();
        initImageLoader();
        setupBottomNavigationView();

        // Firestore
        mDatabase = FirebaseFirestore.getInstance();
        current_UserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference docRefUser = mDatabase.collection("cUsers").document(current_UserID);
        docRefUser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        user = document.toObject(cUsers.class);
                        setupUserDetails(user);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        setupRecyclerViews(current_UserID);
    }

    /**
     * To populate the nested recycler view of current user
     * @param current_UserID UserID of the profile to display
     */
    private void setupRecyclerViews(String current_UserID) {
        mDatabase.collection("cOutfits")
                .orderBy("timeStamp", Query.Direction.ASCENDING)
                .whereEqualTo("userID", current_UserID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // set outfit count textview
                            mOutfits.setText(String.valueOf(task.getResult().size()));
                            ArrayList<cOutfits> cOutfitsArrayList = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                // Change document into class
                                cOutfits outfit = document.toObject(cOutfits.class);
                                Log.e(TAG, "WORKINGIGNIGN "+ outfit);
                                cOutfitsArrayList.add(outfit);
                            }

                            // Recycler Views and Adapters
                            parentRecyclerView = findViewById(R.id.profile_outfit_RV);
                            parentRecyclerView.setHasFixedSize(true);
                            parentRecyclerView.setLayoutManager(new LinearLayoutManager(Profile.this));
                            profileParentAdapter = new ProfileParentAdapter(cOutfitsArrayList);
                            parentRecyclerView.setAdapter(profileParentAdapter);

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    /**
     * Method to make it cleaner in onCreate
     */
    private void setupActivityWidgets() {
        Log.d(TAG, "setupActivityWidgets: started");
        // Progress bar
        mProgressBar = findViewById(R.id.profile_progress_bar);
        mProgressBar.setVisibility(View.GONE);

        // Profile photo
        profilePhoto = findViewById(R.id.snippet_centre_profile_photo);

        // TextViews
        mOutfits = findViewById(R.id.tvOutfits);
        mFollowers = findViewById(R.id.tvFollowers);
        mFollowing = findViewById(R.id.tvFollowing);
        mUsername = findViewById(R.id.snippet_profile_top_toolbar_username);
        mBio = findViewById(R.id.snippet_centre_profile_bio);
        mEditProfile = findViewById(R.id.editProfileBUTTON);

        mEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, EditProfileActivity.class);
                startActivity(intent);
            }
        });

        // Toolbar
        toolbar = findViewById(R.id.snippet_profile_toolbar);
    }

    /**
     * Setup user details: followStatus, OutfitsCount, Following, Followers
     * and update the UI respectively
     */
    private void setupUserDetails(cUsers user) {
        Log.d(TAG, "setupUserDetails: setting user details");

        setProfileImage(user);

        mFollowers.setText(String.valueOf(user.getFollowers().size()));
        mFollowing.setText(String.valueOf(user.getFollowing().size()));
        mBio.setText(user.getBio());

    }

    /**
     * Profile Image setup
     */
    private void setProfileImage(cUsers user) {
        Log.d(TAG, "setProfileImage: setting profile image");
        String profile_photo = user.getProfile_photo();

        StorageReference mStorageReference = FirebaseStorage.getInstance().getReference();
        mStorageReference.child("images").child(profile_photo).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                // Glide.with(context).load(uri.toString()).into(holder.clothingIV);
                UniversalImageLoader.setImage(uri.toString(), profilePhoto, mProgressBar, "");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

    /**
     * Top toolbar setup
     */
    private void setupToolBar() {
        Log.d(TAG, "setupToolBar: Setting up toolbar");

        Toolbar toolbar = findViewById(R.id.snippet_profile_toolbar);
        setSupportActionBar(toolbar);

        ImageView mysettings = findViewById(R.id.snippet_profile_top_toolbar_settings);
        ImageView back = findViewById(R.id.snippet_profile_top_toolbar_back);
        TextView tvUsername = findViewById(R.id.snippet_profile_top_toolbar_username);

        // Firebase authentication
        FirebaseAuth mAuth= FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // Set username on toolbar
        String username = currentUser.getDisplayName();
        if (username != null) {
            tvUsername.setText(username);}

        mysettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Navigating to account settings");
                Intent intent = new Intent(mContext, AccountSettingsActivity.class);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Navigating back to feed");
                Intent intent = new Intent(mContext, FeedActivity.class);
                startActivity(intent);
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

    /**
     * Bottom navigation bar setup
     */
    private void setupBottomNavigationView() {
        Log.d(TAG, "setupBottomNavigationView: Setting up BottomNavigationView");
        BottomNavigationView bottom_navbar_view = findViewById(R.id.bottom_navbar_view);
        BottomNavigationViewHelper.setupBottomNavigationView(bottom_navbar_view);
        BottomNavigationViewHelper.enableNavigation(mContext, bottom_navbar_view);

        // To highlight the correct icon when on correct page
        Menu menu = bottom_navbar_view.getMenu();
        MenuItem menuItem = menu.getItem(b_menu_ACTIVTY_NUM);
        menuItem.setChecked(true);
    }
}
