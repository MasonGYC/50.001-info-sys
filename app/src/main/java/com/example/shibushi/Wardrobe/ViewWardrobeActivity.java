package com.example.shibushi.Wardrobe;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.shibushi.Outfits.ViewOutfitsParentActivity;
import com.example.shibushi.R;
import com.example.shibushi.Utils.BottomNavigationViewHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.Serializable;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ViewWardrobeActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ViewWardrobe";
    private Context mContext = ViewWardrobeActivity.this;
    // Bottom navbar activity number
    private static final int b_menu_ACTIVTY_NUM = 2;

    // Left sidebar
    ImageView side_bar_clothes;
    ImageView side_bar_bottoms;
    ImageView side_bar_others;
    ImageView current_view;

    // Create outfit
    Button create_outfit_button;
    Button create_cancel_button;
    TextView create_outfit_name;
    TextView create_outfit_category;
    public static boolean isChoosing = false;
    ImageView basket;
    ImageView delete_cloth_button;


    // Fragment & ViewPage
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_wardrobe);

        isChoosing = false;
        // Set up bottom navigation bar
        setupBottomNavigationView();
        initDeleteClothFunction();
        initCreateOutfitFunction();
        initPage();
        initTabView();
    }

    private void initTabView(){
        side_bar_clothes = findViewById(R.id.id_wd_clothes);
        side_bar_clothes.setOnClickListener(this);
        side_bar_bottoms = findViewById(R.id.id_wd_bottoms);
        side_bar_bottoms.setOnClickListener(this);
        side_bar_others = findViewById(R.id.id_wd_others);
        side_bar_others.setOnClickListener(this);
        current_view = side_bar_clothes;
        changeTab(R.id.id_wd_clothes);
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
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
    }

    private void changeTab(int position) {
        current_view.setBackgroundColor(Color.parseColor("#eeeeee"));
//        current_view.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
//        current_view.setTextColor(Color.parseColor("#6d6d6d"));
        current_view.setSelected(false);
        switch (position){
            case R.id.id_wd_clothes:
                viewPager.setCurrentItem(0);
                current_view = side_bar_clothes;
                side_bar_clothes.setBackgroundColor(Color.parseColor("#FFFFFF"));
                side_bar_clothes.setSelected(true);
//                side_bar_clothes.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
//                side_bar_clothes.setTextColor(Color.parseColor("#292929"));

            case 0: // R.id.id_wd_clothes
                break;
            case R.id.id_wd_bottoms:
                viewPager.setCurrentItem(1);
                current_view = side_bar_bottoms;
                side_bar_bottoms.setBackgroundColor(Color.parseColor("#FFFFFF"));
                side_bar_bottoms.setSelected(true);
//                side_bar_bottoms.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
//                side_bar_bottoms.setTextColor(Color.parseColor("#292929"));

            case 1: // R.id.id_wd_clothes
                break;
            case R.id.id_wd_others:
                viewPager.setCurrentItem(2);
                current_view = side_bar_others;
                side_bar_others.setBackgroundColor(Color.parseColor("#FFFFFF"));
                side_bar_others.setSelected(true);
//                side_bar_others.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
//                side_bar_others.setTextColor(Color.parseColor("#292929"));
            case 2: // R.id.id_wd_clothes
                break;
        }
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

    @Override
    public void onClick(View view) {
        changeTab(view.getId());
    }

    // Whether it's choosing
    public void setState(boolean b) {
        isChoosing = b;
        if (b) {
            create_outfit_name.setVisibility(View.VISIBLE);
            create_outfit_category.setVisibility(View.VISIBLE);
            create_outfit_button.setVisibility(View.VISIBLE);
            create_cancel_button.setVisibility(View.VISIBLE);
        } else {
            create_outfit_name.setVisibility(View.GONE);
            create_outfit_category.setVisibility(View.GONE);
            create_outfit_button.setVisibility(View.GONE);
            create_cancel_button.setVisibility(View.GONE);
        }
    }


    public void initDeleteClothFunction(){
        delete_cloth_button = findViewById(R.id.wardrobe_delete);
        delete_cloth_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageAdapter.selectedItems.size()==0){
                    LayoutInflater inflater = LayoutInflater.from(getApplication());
                    View view_pop_out = inflater.inflate(R.layout.pop_out_delete_clothes, null);
                    AlertDialog.Builder builer = new AlertDialog.Builder(ViewWardrobeActivity.this);
                    builer.setView(view_pop_out);
                    AlertDialog dialog = builer.create();
                    dialog.show();
                }
            }
        }
        );
    }



    public void initCreateOutfitFunction(){
        // outfit button
        create_outfit_button = findViewById(R.id.buttonCreateOutfit);
        create_outfit_name = findViewById(R.id.create_outfit_name);
        create_outfit_category = findViewById(R.id.create_outfit_category);
        create_outfit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // init intent
                if (create_outfit_name.getText().toString().equals("")){
                    create_outfit_name.setError("outfit name cannot be empty!");
                    create_outfit_name.requestFocus();
                }
                else if (create_outfit_category.getText().toString().equals("")){
                    create_outfit_category.setError("outfit category cannot be empty!");
                    create_outfit_name.requestFocus();
                }
                else {
                    Intent intent = new Intent(mContext, ViewOutfitsParentActivity.class);
                    String category = create_outfit_category.getText().toString(); //TODO: create a new page top input category and name
                    String name = create_outfit_name.getText().toString();
                    isChoosing = false;
                    // process data
                    intent.putExtra(ViewOutfitsParentActivity.KEY_OUTFIT_CREATE, imageAdapter.selectedItems);
                    intent.putExtra(ViewOutfitsParentActivity.KEY_OUTFIT_CAT, category);
                    intent.putExtra(ViewOutfitsParentActivity.KEY_OUTFIT_NAME, name);
                    startActivity(intent);
                }
            }
        });

        // cancel button
        create_cancel_button = findViewById(R.id.buttonCancelCreate);
        create_cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isChoosing = false;
                setState(isChoosing);
            }
        });

        // basket icon
        basket = findViewById(R.id.wardrobe_basket);
        basket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isChoosing = true;
                setState(isChoosing);
            }
        });

        setState(isChoosing);
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
