package com.example.e_learningbahasaisyarat;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Menu;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.example.e_learningbahasaisyarat.ui.account.AccountFragment;
import com.example.e_learningbahasaisyarat.ui.bookmark.Bookmarks;
import com.example.e_learningbahasaisyarat.ui.category.CategoryFragment;
import com.example.e_learningbahasaisyarat.ui.leaderboard.LeaderboardFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.collection.BuildConfig;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;
    private FrameLayout main_frame;
    private TextView drawerProfileName, drawerProfileText;
    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener =
            menuItem -> {

                int selectedItemId = menuItem.getItemId();

                if (selectedItemId == R.id.navigation_home) {
                    setFragment(new CategoryFragment());
                    return true;
                } else if (selectedItemId == R.id.navigation_leaderboard) {
                    setFragment(new LeaderboardFragment());
                    return true;
                } else if (selectedItemId == R.id.navigation_account) {
                    setFragment(new AccountFragment());
                    return true;
                }
                return false;
            };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("CATEGORIES");

        bottomNavigationView = findViewById(R.id.bottom_nav_bar);
        main_frame = findViewById(R.id.main_frame);

        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        drawerProfileName = navigationView.getHeaderView(0).findViewById(R.id.nav_drawer_name);
        drawerProfileText = navigationView.getHeaderView(0).findViewById(R.id.nav_drawer_text_img);

        String name = DbQuery.myProfile.getName();
        drawerProfileName.setText(name);

        drawerProfileText.setText(name.toUpperCase().substring(0, 1));

        setFragment(new CategoryFragment());
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.website) {
            Log.d("MainActivity", "Website item selected");
            showEmailDialog();
        } else if (id == R.id.instagram) {
            Log.d("MainActivity", "Instagram item selected");
            Intent instagram = new Intent(Intent.ACTION_VIEW, Uri.parse("https://instagram.com/e_syarat?igshid=OGQ5ZDc2ODk2ZA=="));
            startActivity(instagram);
        } else if (id == R.id.nav_bookmark) {
            Intent intent = new Intent(MainActivity.this, Bookmarks.class);
            startActivity(intent);
        } else if (id == R.id.rating) {
            Log.d("MainActivity", "Rate Our App item selected");
            showRatingDialog();
        } else if (id == R.id.sharing) {
            Log.d("MainActivity", "Share item selected");
            shareApp();
        } else if (id == R.id.privacyPolicy) {
            Log.d("MainActivity", "Privacy Policy item selected");
            Intent privacyPolicyIntent = new Intent(MainActivity.this, PrivacyPolicy.class);
            startActivity(privacyPolicyIntent);
        } else if (id == R.id.termsCon) {
            Log.d("MainActivity", "Terms & Conditions item selected");
            Intent termsConditionsIntent = new Intent(MainActivity.this, TermsConditions.class);
            startActivity(termsConditionsIntent);
        } else if (id == R.id.more) {
        } else if (id == R.id.logOut) {

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
        return false;
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(main_frame.getId(), fragment);
        transaction.commit();
    }

    private void showEmailDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Contact Us");
        builder.setMessage("For any inquiries or assistance, please contact us at:\n\nEmail: signlangtech@gmail.com");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    private void showRatingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Rate Our App");
        builder.setMessage("How many stars would you like to give us?");

        final RatingBar ratingBar = new RatingBar(MainActivity.this);
        ratingBar.setNumStars(5);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        ratingBar.setLayoutParams(layoutParams);
        builder.setView(ratingBar);

        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                float rating = ratingBar.getRating();
                // Implement logic to send the rating to your server or take other actions as needed.
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    @SuppressLint("RestrictedApi")
    private void shareApp() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "E-Learning Bahasa Isyarat App");
        String shareMessage = "This is the best application for E-Learning Bahasa Isyarat.\n";
        shareMessage = shareMessage + "Download it here: http://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID;
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
        startActivity(Intent.createChooser(shareIntent, "Choose one"));
    }



}
