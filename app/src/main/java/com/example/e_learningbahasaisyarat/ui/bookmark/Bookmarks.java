package com.example.e_learningbahasaisyarat.ui.bookmark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.e_learningbahasaisyarat.DbQuery;
import com.example.e_learningbahasaisyarat.MainActivity;
import com.example.e_learningbahasaisyarat.R;
import com.example.e_learningbahasaisyarat.ui.MyCompleteListener;
import com.example.e_learningbahasaisyarat.ui.answer.AnswerAdapter;
import com.example.e_learningbahasaisyarat.ui.test.TestActivity;

public class Bookmarks extends AppCompatActivity {

    private RecyclerView questionView;
    private Dialog progressDialog;
    private TextView dialogText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);

        Toolbar toolbar = findViewById(R.id.ba_toolbar);
        questionView = findViewById(R.id.ba_recycler_view);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        // Set a custom up indicator (back button icon)
        toolbar.setNavigationIcon(R.drawable.ic_back);

        // Set an OnClickListener for the back button
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Bookmarks.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        getSupportActionBar().setTitle("SAVED QUESTION");
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        progressDialog = new Dialog(Bookmarks.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogText = progressDialog.findViewById(R.id.dialog_text);
        dialogText.setText("Loading...");

        progressDialog.show();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        questionView.setLayoutManager(linearLayoutManager);

        DbQuery.loadBookmarks(new MyCompleteListener() {
            @Override
            public void onSuccess() {
                BookmarksAdapter adapter = new BookmarksAdapter(DbQuery.g_bookmarksList);
                questionView.setAdapter(adapter);

                progressDialog.dismiss();
            }

            @Override
            public void onFailure() {
                progressDialog.dismiss();

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
        {
            Bookmarks.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
