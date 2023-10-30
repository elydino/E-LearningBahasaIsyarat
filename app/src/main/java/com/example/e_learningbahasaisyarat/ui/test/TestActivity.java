package com.example.e_learningbahasaisyarat.ui.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_learningbahasaisyarat.DbQuery;
import com.example.e_learningbahasaisyarat.MainActivity;
import com.example.e_learningbahasaisyarat.R;
import com.example.e_learningbahasaisyarat.ui.MyCompleteListener;


public class TestActivity extends AppCompatActivity {

    private RecyclerView testView;
    private Toolbar toolbar;
    private TestAdapter adapter;
    private Dialog progressDialog;
    private TextView dialogText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        // Set a custom up indicator (back button icon)
        toolbar.setNavigationIcon(R.drawable.ic_back);

        // Set an OnClickListener for the back button
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        getSupportActionBar().setTitle(DbQuery.g_catList.get(DbQuery.g_selected_cat_index).getName());
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        testView = findViewById(R.id.test_recycler_view);

        progressDialog = new Dialog(TestActivity.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogText = progressDialog.findViewById(R.id.dialog_text);
        dialogText.setText("Loading...");

        progressDialog.show();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        testView.setLayoutManager(linearLayoutManager);

        // Check if g_testList is empty, and if so, load the test data
        if (DbQuery.g_testList.isEmpty()) {
            // Show a loading dialog or handle the situation as needed
            progressDialog.show();

            DbQuery.loadTestData(new MyCompleteListener() {
            @Override
            public void onSuccess() {

                DbQuery.loadMyScores(new MyCompleteListener() {
                    @Override
                    public void onSuccess() {
                        if (DbQuery.g_testList != null && !DbQuery.g_testList.isEmpty()) {
                            adapter = new TestAdapter(DbQuery.g_testList);
                            testView.setAdapter(adapter);
                        } else {
                            // Handle the case when g_testList is empty, for example, by displaying a message to the user.
                            Toast.makeText(TestActivity.this, "No tests available.", Toast.LENGTH_SHORT).show();
                        }

                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure() {
                        progressDialog.dismiss();
                        // If sign in fails, display a message to the user.
                        Toast.makeText(TestActivity.this, "Something went wrong! Please Try Again Later!", Toast.LENGTH_SHORT).show();

                    }
                });
            }

            @Override
            public void onFailure() {
                progressDialog.dismiss();
                // If sign in fails, display a message to the user.
                Toast.makeText(TestActivity.this, "Something went wrong ! Please Try Again Later !", Toast.LENGTH_SHORT).show();


            }
        });
    }else {
            // If g_testList is already loaded, set up your RecyclerView adapter directly
            adapter = new TestAdapter(DbQuery.g_testList);
            testView.setAdapter(adapter);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
        {
            TestActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}