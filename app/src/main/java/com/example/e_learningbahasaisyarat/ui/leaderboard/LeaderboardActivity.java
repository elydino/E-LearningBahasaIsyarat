package com.example.e_learningbahasaisyarat.ui.leaderboard;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_learningbahasaisyarat.DbQuery;
import com.example.e_learningbahasaisyarat.R;
import com.example.e_learningbahasaisyarat.ui.MyCompleteListener;
import com.example.e_learningbahasaisyarat.ui.rank.RankAdapter;

public class LeaderboardActivity extends AppCompatActivity {

    private TextView totalUsersTV, myImgTextTV, myScoreTV, myRankTV;
    private RecyclerView usersView;
    private RankAdapter adapter;
    private Dialog progressDialog;
    private TextView dialogText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_leaderboard); // Set your activity's layout

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("LEADERBOARD");
        }

        initViews();

        progressDialog = new Dialog(this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogText = progressDialog.findViewById(R.id.dialog_text);
        dialogText.setText("Loading...");

        progressDialog.show();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        usersView.setLayoutManager(layoutManager);

        adapter = new RankAdapter(DbQuery.g_usersList);
        usersView.setAdapter(adapter);

        DbQuery.getTopUsers(new MyCompleteListener(){
            @Override
            public void onSuccess() {
                adapter.notifyDataSetChanged();
                if (DbQuery.myPerformance.getScore() != 0) {
                    if (!DbQuery.isMeOnTopList) {
                        calculateRank();
                    }

                    myScoreTV.setText("Score : " + DbQuery.myPerformance.getScore());
                    myRankTV.setText("Rank - " + DbQuery.myPerformance.getRank());
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure() {
                // If there's an error, display a message to the user.
                Toast.makeText(LeaderboardActivity.this, "Something went wrong! Please Try Again Later!", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

        totalUsersTV.setText("Total Users : " + DbQuery.g_usersCount);
        myImgTextTV.setText(DbQuery.myPerformance.getName().toUpperCase().substring(0, 1));
    }

    private void initViews() {
        totalUsersTV = findViewById(R.id.total_users);
        myImgTextTV = findViewById(R.id.img_text);
        myScoreTV = findViewById(R.id.total_score);
        myRankTV = findViewById(R.id.rank);
        usersView = findViewById(R.id.users_view);
    }

    private void calculateRank() {
        int lowTopScore = DbQuery.g_usersList.get(DbQuery.g_usersList.size() - 1).getScore();
        int remaining_slots = DbQuery.g_usersCount - 20;
        int myslot = (DbQuery.myPerformance.getScore() * remaining_slots) / lowTopScore;
        int rank;

        if (lowTopScore != DbQuery.myPerformance.getScore()) {
            rank = DbQuery.g_usersCount - myslot;
        } else {
            rank = 21;
        }

        DbQuery.myPerformance.setRank(rank);
    }
}
