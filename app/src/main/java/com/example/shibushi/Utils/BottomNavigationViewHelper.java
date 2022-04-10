package com.example.shibushi.Utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.example.shibushi.Feed.FeedActivity;
import com.example.shibushi.MainActivity;
import com.example.shibushi.R;
import com.example.shibushi.ViewOutfitsActivity;
import com.example.shibushi.Wardrobe.ViewWardrobeActivity;
import com.google.android.material.navigation.NavigationBarView;

// A Helper class to organise implementation of Bottom Navigation Bar which applies to all screens
public class BottomNavigationViewHelper {

    private static final String TAG = "BottomNavViewHelper";

    public static void setupBottomNavigationView(NavigationBarView bottomNavigationView) {
        Log.d(TAG, "setupBottomNavigationView: Setting up BottomNavigationView");
    }

    // setup navigation between activities
    public static void enableNavigation(final Context context, NavigationBarView view) {

        view.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    // TODO: Remove and change home to feed
                    case R.id.ic_home: // b_menu_ACTIVTY_NUM = 0
                        if (view.getSelectedItemId()!=R.id.ic_home) {
                            Intent intent_activity_home = new Intent(context, MainActivity.class);
                            context.startActivity(intent_activity_home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                        }
                        break;

                    case R.id.ic_community_feed: // b_menu_ACTIVTY_NUM = 1
                        Intent intent_community_feed = new Intent(context, FeedActivity.class);
                        context.startActivity(intent_community_feed.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                        break;

                    case R.id.ic_wardrobe: // b_menu_ACTIVTY_NUM = 2
                        if (view.getSelectedItemId()!=R.id.ic_wardrobe) {
                            Intent intent_wardrobe = new Intent(context, ViewWardrobeActivity.class);
                            context.startActivity(intent_wardrobe.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                        }
                        break;

                    case R.id.ic_outfits: // b_menu_ACTIVTY_NUM = 3
                        if (view.getSelectedItemId()!=R.id.ic_outfits) {
                            Intent intent_outfits = new Intent(context, ViewOutfitsActivity.class);
                            context.startActivity(intent_outfits.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                        }
                        break;
                }

                return false;
            }
        });
    }
}
