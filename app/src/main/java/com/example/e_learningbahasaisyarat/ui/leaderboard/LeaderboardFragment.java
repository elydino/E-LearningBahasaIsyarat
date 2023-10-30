package com.example.e_learningbahasaisyarat.ui.leaderboard;

import static com.example.e_learningbahasaisyarat.DbQuery.g_usersCount;
import static com.example.e_learningbahasaisyarat.DbQuery.g_usersList;
import static com.example.e_learningbahasaisyarat.DbQuery.myPerformance;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_learningbahasaisyarat.DbQuery;
import com.example.e_learningbahasaisyarat.MainActivity;
import com.example.e_learningbahasaisyarat.R;
import com.example.e_learningbahasaisyarat.databinding.FragmentLeaderboardBinding;
import com.example.e_learningbahasaisyarat.ui.MyCompleteListener;
import com.example.e_learningbahasaisyarat.ui.rank.RankAdapter;

public class LeaderboardFragment extends Fragment {

    private TextView totalUsersTV, myImgTextTV, myScoreTV, myRankTV;
    private RecyclerView usersView;
    private RankAdapter adapter;
    private Dialog progressDialog;
    private TextView dialogText;

    public LeaderboardFragment() {
        //Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        ((MainActivity) getActivity()).getSupportActionBar().setTitle("LEADERBOARD");

        initViews(view);

        progressDialog = new Dialog(getContext());
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogText = progressDialog.findViewById(R.id.dialog_text);
        dialogText.setText("Loading...");

        progressDialog.show();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        usersView.setLayoutManager(layoutManager);

        adapter = new RankAdapter(g_usersList);
        usersView.setAdapter(adapter);

        DbQuery.getTopUsers(new MyCompleteListener(){
            @Override
            public void onSuccess() {

                adapter.notifyDataSetChanged();
                if (myPerformance.getScore() != 0)
                {
                    if ( ! DbQuery.isMeOnTopList)
                    {
                        calculateRank();
                    }

                    myScoreTV.setText("Score : " + myPerformance.getScore());
                    myRankTV.setText("Rank - " + myPerformance.getRank());
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure() {
                // If sign in fails, display a message to the user.
                Toast.makeText(getContext(), "Something went wrong ! Please Try Again Later !", Toast.LENGTH_SHORT).show();

                progressDialog.dismiss();
            }
        });

        totalUsersTV.setText("Total Users : " + g_usersCount);
        myImgTextTV.setText(myPerformance.getName().toUpperCase().substring(0,1));
        return view;
    }

    private void initViews(View view)
    {
        totalUsersTV = view.findViewById(R.id.total_users);
        myImgTextTV = view.findViewById(R.id.img_text);
        myScoreTV = view.findViewById(R.id.total_score);
        myRankTV = view.findViewById(R.id.rank);
        usersView = view.findViewById(R.id.users_view);
    }

    private void calculateRank()
    {
        int lowTopScore = g_usersList.get(g_usersList.size() - 1).getScore();
        int remaining_slots = g_usersCount - 20;
        int myslot = (myPerformance.getScore()*remaining_slots) / lowTopScore;
        int rank;

        if (lowTopScore != myPerformance.getScore())
        {
            rank = g_usersCount - myslot;
        }
        else{
            rank = 21;
        }

        myPerformance.setRank(rank);
    }
}