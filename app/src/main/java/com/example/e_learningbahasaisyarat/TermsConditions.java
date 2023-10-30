package com.example.e_learningbahasaisyarat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class TermsConditions extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.termsconditions); // Menggunakan layout XML yang telah Anda buat

        Toolbar toolbar = findViewById(R.id.t_toolbar);

        // Set a custom up indicator (back button icon)
        toolbar.setNavigationIcon(R.drawable.ic_back);

        // Set an OnClickListener for the back button
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TermsConditions.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        TextView termsAndConditionsTextView = findViewById(R.id.termsAndConditionsTextView); // Ganti dengan ID yang sesuai
        // Set teks "Terms and Conditions" sesuai dengan teks yang Anda inginkan
        termsAndConditionsTextView.setText("This is Terms and Conditions.");
    }
}



