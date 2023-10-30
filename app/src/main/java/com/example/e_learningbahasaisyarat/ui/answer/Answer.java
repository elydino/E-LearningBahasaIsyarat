package com.example.e_learningbahasaisyarat.ui.answer;


import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.e_learningbahasaisyarat.DbQuery;
import com.example.e_learningbahasaisyarat.R;


public class Answer extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView answersView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        toolbar = findViewById(R.id.aa_toolbar);
        answersView = findViewById(R.id.aa_recycler_view);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("ANSWERS");
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        answersView.setLayoutManager(linearLayoutManager);

        AnswerAdapter adapter = new AnswerAdapter(DbQuery.g_quesList);
        answersView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
        {
            Answer.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}