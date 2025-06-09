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

    // UI Components
    private ImageView menuIcon, profileIcon;
    private CardView tournamentCard1, tournamentCard2;
    private LinearLayout navHome, navCalendar, navTrophy;

    // Sidebar Components
    private DrawerLayout drawerLayout;
    private LinearLayout sidebarProfile;
    private LinearLayout sidebarDevInfo;
    private LinearLayout sidebarSettings;
    private LinearLayout sidebarLogout;
    private TextView sidebarUserName;
    private TextView sidebarUserRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        initializeViews();

        // Set click listeners
        setClickListeners();

        // Load user profile for sidebar
        loadUserProfile();
    }

    private void initializeViews() {
        // Header icons
        menuIcon = findViewById(R.id.menu_icon);
        profileIcon = findViewById(R.id.profile_icon);

        // Tournament cards
        tournamentCard1 = findViewById(R.id.card_tournament_1);
        tournamentCard2 = findViewById(R.id.card_tournament_2);

        // Bottom navigation
        navHome = findViewById(R.id.nav_home);
        navCalendar = findViewById(R.id.nav_calendar);
        navTrophy = findViewById(R.id.nav_trophy);

        // Sidebar components
        drawerLayout = findViewById(R.id.drawer_layout);
        sidebarProfile = findViewById(R.id.sidebar_profile);
        sidebarDevInfo = findViewById(R.id.sidebar_dev_info);
        sidebarSettings = findViewById(R.id.sidebar_settings);
        sidebarLogout = findViewById(R.id.sidebar_logout);
        sidebarUserName = findViewById(R.id.sidebar_user_name);
        sidebarUserRole = findViewById(R.id.sidebar_user_role);
    }

    private void setClickListeners() {
        // Header icons
        menuIcon.setOnClickListener(this);
        profileIcon.setOnClickListener(this);

        // Tournament cards
        tournamentCard1.setOnClickListener(this);
        tournamentCard2.setOnClickListener(this);

        // Bottom navigation
        navHome.setOnClickListener(this);
        navCalendar.setOnClickListener(this);
        navTrophy.setOnClickListener(this);

        // Sidebar options
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
        // TODO: Navigate to tournament details activity
    }

    private void handleNavigationClick(String section) {
        showToast(section + " selected");
        // TODO: Handle bottom navigation
        switch (section) {
            case "Home":
                // Already on home, maybe refresh or scroll to top
                break;
            case "Calendar":
                // Navigate to calendar/schedule activity
                break;
            case "Trophy":
                // Navigate to tournaments/results activity
                break;
        }
    }

    // Sidebar click handlers
    private void handleSidebarProfileClick() {
        drawerLayout.closeDrawer(GravityCompat.START);
        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(intent);
    }

    private void handleSidebarDevInfoClick() {
        drawerLayout.closeDrawer(GravityCompat.START);
        showDeveloperInfo();
    }

    private void handleSidebarSettingsClick() {
        drawerLayout.closeDrawer(GravityCompat.START);
        showToast("Opening Settings");
        // TODO: Navigate to settings activity
        // Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        // startActivity(intent);
    }

    private void handleSidebarLogoutClick() {
        drawerLayout.closeDrawer(GravityCompat.START);
        handleLogout();
    }

    private void loadUserProfile() {
        // Load user profile data from SharedPreferences, database, or API
        String userName = getUserName(); // Get from your data source
        String userRole = getUserRole(); // Get from your data source

        sidebarUserName.setText(userName);
        sidebarUserRole.setText(userRole);
    }

    private void showDeveloperInfo() {
        // Create and show developer information dialog
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
        // Show confirmation dialog
        new android.app.AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Sign out from Firebase
                    FirebaseAuth.getInstance().signOut();

                    // Clear local session data
                    clearUserSession();

                    // Navigate to LoginActivity
                    Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish(); // Close current activity
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void clearUserSession() {
        // Clear SharedPreferences
        getSharedPreferences("user_prefs", MODE_PRIVATE)
                .edit()
                .clear()
                .apply();

        // Clear any other user data
        // Clear authentication tokens
        // Clear cached data
    }

    private String getUserName() {
        // Get username from SharedPreferences or your data source
        return getSharedPreferences("user_prefs", MODE_PRIVATE)
                .getString("user_name", "John Developer");
    }

    private String getUserRole() {
        // Get user role from SharedPreferences or your data source
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

            // Reset the flag after 2 seconds
            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }
}