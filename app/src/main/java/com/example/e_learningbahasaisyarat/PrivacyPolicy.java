package com.example.e_learningbahasaisyarat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class PrivacyPolicy extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privacypolicy); // Pastikan layout yang benar

        Toolbar toolbar = findViewById(R.id.p_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        // Set a custom up indicator (back button icon)
        toolbar.setNavigationIcon(R.drawable.ic_back);

        // Set an OnClickListener for the back button
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrivacyPolicy.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        getSupportActionBar().setTitle("PRIVACY POLICY");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }
}
