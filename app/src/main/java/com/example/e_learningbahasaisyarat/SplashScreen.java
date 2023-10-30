package com.example.e_learningbahasaisyarat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.e_learningbahasaisyarat.ui.MyCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashScreen extends AppCompatActivity implements Animation.AnimationListener {

    ImageView imageView;
    Animation animation;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        imageView = findViewById(R.id.splash_logo);
        animation = AnimationUtils.loadAnimation(this, R.anim.splash_anim);
        animation.setAnimationListener(this);
        imageView.startAnimation(animation);

        auth = FirebaseAuth.getInstance();

        // Access a Cloud Firestore instance from your Activity
        DbQuery.g_firestore = FirebaseFirestore.getInstance();


        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(3000); // Sleep for 3 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(auth.getCurrentUser() != null){

                DbQuery.loadData(new MyCompleteListener() {
                    @Override
                    public void onSuccess() {
                        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    }

                    @Override
                    public void onFailure() {
                        Toast.makeText(SplashScreen.this, "Something went wrong ! Please Try Again Later!", Toast.LENGTH_SHORT).show();

                    }
                });
            }
            else {
                Intent intent = new Intent(SplashScreen.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

        thread.start();
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
