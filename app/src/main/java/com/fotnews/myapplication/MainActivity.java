package com.fotnews.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // UI Components
    private ImageView menuIcon, profileIcon;
    private CardView tournamentCard1, tournamentCard2;
    private LinearLayout navHome, navCalendar, navTrophy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        initializeViews();

        // Set click listeners
        setClickListeners();
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
        }
    }

    private void handleMenuClick() {
        showToast("Menu clicked");
        // TODO: Implement drawer navigation or menu functionality
    }

    private void handleProfileClick() {
        showToast("Profile clicked");
        // TODO: Navigate to profile activity
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

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

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