package com.example.shibushi.Feed;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.shibushi.Feed.Profile.Profile;
import com.example.shibushi.MainActivity;
import com.example.shibushi.R;
import com.example.shibushi.TagIt;
import com.example.shibushi.Utils.BottomNavigationViewHelper;
import com.example.shibushi.Utils.UniversalImageLoader;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.annotations.Nullable;
import com.nostra13.universalimageloader.core.ImageLoader;

public class FeedActivity extends AppCompatActivity {

    private static final String TAG = "CommunityFeed";
    private Context mContext = FeedActivity.this;
    // Bottom navbar activity number
    private static final int b_menu_ACTIVTY_NUM = 1;

    public static final String KEY_FEED_PHOTO = "KEY_FEED_PHOTO";
    Uri imageUri;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_feed);
        Log.d(TAG, "onCreate: started");

        initImageLoader(); //Init Image Loader
        setupToolBar(); //Setup top toolbar
        setupBottomNavigationView(); //Setup bottom navigation bar
        setup_FAB(); //Setup floating action button

    }


    /**
     * Initialise ImageLoader
     * Quick Setup Src- https://github.com/nostra13/Android-Universal-Image-Loader/wiki/Quick-Setup
     */
    private void initImageLoader() {
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(mContext);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }

    // Floating action button
    private void setup_FAB() {
        Log.d(TAG, "setupFloatingActionButton: Setting up FAB menu");

        FloatingActionButton fab_outfit = findViewById(R.id.community_feed_fab_outfit);
        FloatingActionButton fab_hanger = findViewById(R.id.community_feed_fab_hanger);
        FloatingActionButton fab_edit = findViewById(R.id.community_feed_fab_edit);

        fab_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Add new post page
                Toast.makeText(mContext, "Creating new post...", Toast.LENGTH_SHORT).show();
                // Intent intent = new Intent(mContext, NewPost.class);
                // startActivity(intent);

            }
        });

        fab_hanger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Adding new clothes...", Toast.LENGTH_SHORT).show();
                importClothing(MainActivity.PICK_IMAGE_REQUEST);
            }
        });

        fab_outfit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Add new outfit page
                Toast.makeText(mContext, "Making new outfit...", Toast.LENGTH_SHORT).show();
                // Intent intent = new Intent(mContext, NewOutfit.class);
                // startActivity(intent);
            }
        });
    }

    // Top toolbar setup
    private void setupToolBar() {
        Log.d(TAG, "setupBottomNavigationView: Setting up top Toolbar");
        Toolbar toolbar = findViewById(R.id.community_feed_top_toolbar);
        setSupportActionBar(toolbar);

        ImageView myprofile = findViewById(R.id.community_feed_top_toolbar_profile);
        ImageView searchBar = findViewById(R.id.community_feed_top_toolbar_search);

        myprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Navigating to my profile");
                Intent intent = new Intent(mContext, Profile.class);
                startActivity(intent);
            }
        });

        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Navigating to search");
                Intent intent = new Intent(mContext, SearchActivity.class);
                startActivity(intent);
            }
        });
    }

    // BottomNavigationView setup
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

    // Select Image method
    public void importClothing(int PICK_IMAGE_REQUEST) {
        // Defining Implicit Intent to mobile gallery
        Intent selectIntent = new Intent();
        selectIntent.setType("image/*");
        selectIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(selectIntent, "Select Image from here..."), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == MainActivity.PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            if (intent.getData() != null) {
                imageUri = intent.getData();
                Intent tagItIntent = new Intent(mContext, TagIt.class);
                tagItIntent.putExtra(KEY_FEED_PHOTO, imageUri.toString());
                startActivity(tagItIntent);

            }

        }

        }

}
