package com.fotnews.myapplication;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DeveloperInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer_info);

        // Initialize TextViews
        TextView nameValue = findViewById(R.id.nameValue);
        TextView idValue = findViewById(R.id.idValue);
        TextView statementValue = findViewById(R.id.statementValue);
        TextView versionValue = findViewById(R.id.versionValue);
        TextView emailValue = findViewById(R.id.emailValue);

        // Set values from your info
        nameValue.setText("Uditha Indunil");
        idValue.setText("2020T00898");
        statementValue.setText("Web Developer");
        versionValue.setText("V1.0.0");
        emailValue.setText("udithaindunil5@Gmail.Com");
    }
}