package com.example.shibushi.Wardrobe;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.shibushi.R;
import com.example.shibushi.Utils.BottomNavigationViewHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class ViewWardrobeActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ViewWardrobe";
    private Context mContext = ViewWardrobeActivity.this;
    // Bottom navbar activity number
    private static final int b_menu_ACTIVTY_NUM = 2;

    // Fragment & ViewPage
    private ViewPager2 viewPager;
//    private imageAdapter imageAdapter;
//    private Model.DataSource dataSource;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_wardrobe);

        // Set up bottom navigation bar
        setupBottomNavigationView();

//        List<Model.Img> imgs = new ArrayList<>();
//        imgs.add(new Model.Img("https://i.pinimg.com/474x/4b/8a/e4/4b8ae452fe3d785f3d15b1fa5b201af3.jpg"));
//        dataSource = new Model.DataSource(imgs);
//        fragRecyclerView = findViewById(R.id.id_wd_fragment);
//        render();
        initPage();
        initTabView();
    }

    private void initTabView(){
        TextView side_bar_clothes = findViewById(R.id.id_wd_clothes);
        side_bar_clothes.setOnClickListener(this);
        TextView side_bar_bottoms = findViewById(R.id.id_wd_bottoms);
        side_bar_bottoms.setOnClickListener(this);
        TextView side_bar_others = findViewById(R.id.id_wd_others);
        side_bar_others.setOnClickListener(this);
    }


    private void initPage() {
        viewPager = findViewById(R.id.vp_wardrobe);
        viewPager.setUserInputEnabled(false);

        // todo: set to fetch from firebase
        ArrayList<String> clothes = new ArrayList<>();
        ArrayList<String> bottoms = new ArrayList<>();
        ArrayList<String> accessories = new ArrayList<>();

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(WardrobeFragment.newInstance(clothes));    // clothes
        fragments.add(WardrobeFragment.newInstance(bottoms));    // bottoms
        fragments.add(WardrobeFragment.newInstance(accessories));    // accessories
        FragmentPageAdapter pageAdapter = new FragmentPageAdapter(getSupportFragmentManager(), getLifecycle(), fragments);
        viewPager.setAdapter(pageAdapter);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                changeTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
    }

    private void changeTab(int position) {
        switch (position){
            case R.id.id_wd_clothes:
                viewPager.setCurrentItem(0);
            case 0: // R.id.id_wd_clothes
                break;
            case R.id.id_wd_bottoms:
                viewPager.setCurrentItem(1);
            case 1: // R.id.id_wd_clothes
                break;
            case R.id.id_wd_others:
                viewPager.setCurrentItem(2);
            case 2: // R.id.id_wd_clothes
                break;


        }
    }

//    private void render() {
//
//        imageAdapter = new imageAdapter(, dataSource);
//        fragRecyclerView.setAdapter(imageAdapter);
//        fragRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
//    }




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

    @Override
    public void onClick(View view) {
        changeTab(view.getId());
    }
}


/**
 * According to this link:
 * https://stackoverflow.com/questions/58590666/firebase-use-metadata-to-sort-which-files-get-displayed-when-use-list
 * It's good to use Firebase storage for image storage
 * in conjunction with RTDB or Cloud Firestore for file metadata.
 * This allows us to easily store, query and update the metadata
 * We may not want to separate the uploads into different folders for each user as
 * we want to be able to view others by tags
 *
 * According to this link:
 * https://firebase.google.com/docs/firestore/query-data/queries#java_5
 * We can do complex queries in Cloud Firestore
 * E.g. we can structure each document in Cloud Firestore as
 *
 * Documetn structure
 * { username: michael
 *   imageurl: url(truncated)
 *   color : color
 *   etc...
 * }
 *
 * Common operations:
 *
 *  Query/search:
 *      All yellow shirts (color, type) -> Query chainedQuery1 = images.whereEqualTo(color , "yellow").whereEqualTo(username, "Joshua)
 *      All formal pants (occasion, type)
 *      All white cotton (color, material)
 *
 *      -> Query chainedQuery1 = images.whereEqualTo(color , "yellow")
 *      .whereEqualTo(username, "Michael")
 *      .whereEqualTo(type, "Shirt")
 *      Query chainedQuery2 = images.whereEqualTo(color, "yellow").whereEqualTo(type, "Shirt").whereEqualTo(privacy, "public")
 *
 *      We may not search by privacy attribute
 *
 *  Add:
 *
 *  Delete Image, if user match/authorized:
 *
 *  Tree structure - flat hierarchy:
 *
 *  Images
 *      -Public
 *          -Image1
 *          -Image2
 *          -Image3
 *      -Private
 *          -User
 *              -Image1
 *
 *         -OR-
 *   Images
 *      -Image1
 *      -Image2
 *      -Image3
 *
 *
 *  Don't bother splitting between private and public, just use a != filter for privacy
 *
 *
 *
 *
 * */
