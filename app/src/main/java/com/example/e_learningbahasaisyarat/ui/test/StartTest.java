package com.example.e_learningbahasaisyarat.ui.test;

import static com.example.e_learningbahasaisyarat.DbQuery.g_catList;
import static com.example.e_learningbahasaisyarat.DbQuery.loadquestions;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_learningbahasaisyarat.DbQuery;
import com.example.e_learningbahasaisyarat.R;
import com.example.e_learningbahasaisyarat.ui.MyCompleteListener;
import com.example.e_learningbahasaisyarat.ui.question.Questions;

public class StartTest extends AppCompatActivity {

    private TextView catName, testNo, totalQ, bestScore, time;
    private Button startTestB;
    private ImageView backB;
    private Dialog progressDialog;
    private TextView dialogText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_test);

        init();

        progressDialog = new Dialog(StartTest.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogText = progressDialog.findViewById(R.id.dialog_text);
        dialogText.setText("Loading...");

        progressDialog.show();

        loadquestions(new MyCompleteListener(){
            @Override
            public void onSuccess() {
                setData();

                progressDialog.dismiss();
            }

            @Override
            public void onFailure() {
                progressDialog.dismiss();
                // If sign in fails, display a message to the user.
                Toast.makeText(StartTest.this, "Something went wrong ! Please Try Again Later !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init()
    {
        catName = findViewById(R.id.st_cat_name);
        testNo = findViewById(R.id.st_test_num);
        totalQ = findViewById(R.id.st_total_ques);
        bestScore = findViewById(R.id.st_best_score);
        time = findViewById(R.id.st_time);
        startTestB = findViewById(R.id.start_testB);
        backB = findViewById(R.id.st_backB);

        backB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartTest.this.finish();
            }
        });

        startTestB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartTest.this, Questions.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setData()
    {
        catName.setText(g_catList.get(DbQuery.g_selected_cat_index).getName());
        testNo.setText("Test No. " + String.valueOf(DbQuery.g_selected_test_index + 1));
        totalQ.setText(String.valueOf(DbQuery.g_quesList.size()));
        bestScore.setText(String.valueOf(DbQuery.g_testList.get(DbQuery.g_selected_test_index).getTopScore()));
        time.setText(String.valueOf(DbQuery.g_testList.get(DbQuery.g_selected_test_index).getTime()));
    }
}