package com.fotnews.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private ImageView menuIcon, profileIcon;
    private CardView tournamentCard1, tournamentCard2;
    private LinearLayout navHome, navCalendar, navTrophy;


    private DrawerLayout drawerLayout;
    private LinearLayout sidebarProfile;
    private LinearLayout sidebarDevInfo;
    private LinearLayout sidebarSettings;
    private LinearLayout sidebarLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initializeViews();


        setClickListeners();


        loadUserProfile();
    }

    private void initializeViews() {

        menuIcon = findViewById(R.id.menu_icon);
        profileIcon = findViewById(R.id.profile_icon);


        tournamentCard1 = findViewById(R.id.card_tournament_1);
        tournamentCard2 = findViewById(R.id.card_tournament_2);


        navHome = findViewById(R.id.nav_home);
        navCalendar = findViewById(R.id.nav_calendar);
        navTrophy = findViewById(R.id.nav_trophy);


        drawerLayout = findViewById(R.id.drawer_layout);
        sidebarProfile = findViewById(R.id.sidebar_profile);
        sidebarDevInfo = findViewById(R.id.sidebar_dev_info);
        sidebarSettings = findViewById(R.id.sidebar_settings);
        sidebarLogout = findViewById(R.id.sidebar_logout);

    }

    private void setClickListeners() {

        menuIcon.setOnClickListener(this);
        profileIcon.setOnClickListener(this);


        tournamentCard1.setOnClickListener(this);
        tournamentCard2.setOnClickListener(this);


        navHome.setOnClickListener(this);
        navCalendar.setOnClickListener(this);
        navTrophy.setOnClickListener(this);


        sidebarProfile.setOnClickListener(this);
        sidebarDevInfo.setOnClickListener(this);
        sidebarSettings.setOnClickListener(this);
        sidebarLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.menu_icon) {
            handleMenuClick();
        } else if (id == R.id.profile_icon) {
            handleProfileClick();
        } else if (id == R.id.card_tournament_1) {
            handleTournamentClick("Tech Bash 2023 - Tournament 1");
        } else if (id == R.id.card_tournament_2) {
            handleTournamentClick("Tech Bash 2023 - Tournament 2");
        } else if (id == R.id.nav_home) {
            handleNavigationClick("Home");
        } else if (id == R.id.nav_calendar) {
            handleNavigationClick("Calendar");
        } else if (id == R.id.nav_trophy) {
            handleNavigationClick("Trophy");
        } else if (id == R.id.sidebar_profile) {
            handleSidebarProfileClick();
        } else if (id == R.id.sidebar_dev_info) {
            handleSidebarDevInfoClick();
        } else if (id == R.id.sidebar_settings) {
            handleSidebarSettingsClick();
        } else if (id == R.id.sidebar_logout) {
            handleSidebarLogoutClick();
        }
    }

    private void handleMenuClick() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    private void handleProfileClick() {
        drawerLayout.closeDrawer(GravityCompat.START);
        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(intent);
    }

    private void handleTournamentClick(String tournamentName) {
        showToast("Opening " + tournamentName);

    }

    private void handleNavigationClick(String section) {
        showToast(section + " selected");
        // TODO: Handle bottom navigation
        switch (section) {
            case "Home":

                break;
            case "Calendar":

                break;
            case "Trophy":
                Intent trophyIntent = new Intent(this, NewsActivity.class);
                startActivity(trophyIntent);
                break;
        }
    }


    private void handleSidebarProfileClick() {
        drawerLayout.closeDrawer(GravityCompat.START);
        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(intent);
    }

    private void handleSidebarDevInfoClick() {
        drawerLayout.closeDrawer(GravityCompat.START);
        Intent intent = new Intent(MainActivity.this, DeveloperInfoActivity.class);
        startActivity(intent);
    }

    private void handleSidebarSettingsClick() {
        drawerLayout.closeDrawer(GravityCompat.START);
        showToast("Opening Settings");


    }

    private void handleSidebarLogoutClick() {
        drawerLayout.closeDrawer(GravityCompat.START);
        handleLogout();
    }

    private void loadUserProfile() {

        String userName = getUserName();
        String userRole = getUserRole();


    }

    private void showDeveloperInfo() {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Developer Information")
                .setMessage("App Name: Tournament Manager\n" +
                        "Version: 1.0.0\n" +
                        "Developer: Your Name\n" +
                        "Contact: developer@example.com\n" +
                        "Built with Android SDK")
                .setPositiveButton("OK", null)
                .show();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private boolean doubleBackToExitPressedOnce = false;

    private void handleLogout() {

        new android.app.AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (dialog, which) -> {

                    FirebaseAuth.getInstance().signOut();


                    clearUserSession();

                    Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void clearUserSession() {

        getSharedPreferences("user_prefs", MODE_PRIVATE)
                .edit()
                .clear()
                .apply();


    }

    private String getUserName() {

        return getSharedPreferences("user_prefs", MODE_PRIVATE)
                .getString("user_name", "John Developer");
    }

    private String getUserRole() {

        return getSharedPreferences("user_prefs", MODE_PRIVATE)
                .getString("user_role", "Software Developer");
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        } else {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();


            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }
}