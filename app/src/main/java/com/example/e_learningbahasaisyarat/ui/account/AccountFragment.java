package com.example.e_learningbahasaisyarat.ui.account;

import static com.example.e_learningbahasaisyarat.DbQuery.g_usersCount;
import static com.example.e_learningbahasaisyarat.DbQuery.g_usersList;
import static com.example.e_learningbahasaisyarat.DbQuery.myPerformance;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.e_learningbahasaisyarat.DbQuery;
import com.example.e_learningbahasaisyarat.Login;
import com.example.e_learningbahasaisyarat.MainActivity;
import com.example.e_learningbahasaisyarat.ui.bookmark.Bookmarks;
import com.example.e_learningbahasaisyarat.ui.profile.MyProfileActivity;
import com.example.e_learningbahasaisyarat.R;
import com.example.e_learningbahasaisyarat.ui.MyCompleteListener;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class AccountFragment extends Fragment {

    private LinearLayout logoutB;
    private TextView profile_img_text, name, score, rank;
    private LinearLayout leaderB, profileB, bookmarksB;
    private BottomNavigationView bottomNavigationView;

    private Dialog progressDialog;
    private TextView dialogText;
    public AccountFragment(){
        //Required empty public constructor
        Log.d("ddf", "df");
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        initViews(view);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("MY ACCOUNT");

        progressDialog = new Dialog(getContext());
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogText = progressDialog.findViewById(R.id.dialog_text);
        dialogText.setText("Loading...");

        String userName = DbQuery.myProfile.getName();
        profile_img_text.setText(userName.toUpperCase().substring(0,1));

        name.setText(userName);

        score.setText(String.valueOf(DbQuery.myPerformance.getScore()));

        if (g_usersList.size() == 0)
        {
            progressDialog.show();
            DbQuery.getTopUsers(new MyCompleteListener(){
                @Override
                public void onSuccess() {

                    if (myPerformance.getScore() != 0)
                    {
                        if ( ! DbQuery.isMeOnTopList)
                        {
                            calculateRank();
                        }

                        score.setText("Score : " + myPerformance.getScore());
                        rank.setText("Rank - " + myPerformance.getRank());
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

        }
        else {
            score.setText("Score : " + myPerformance.getScore());
            if (myPerformance.getScore() != 0)
                rank.setText("Rank - " + myPerformance.getRank());
        }
        logoutB.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                FirebaseAuth.getInstance().signOut();

                //Configure Google Sign In
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)

                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();

                GoogleSignInClient GoogleClient = GoogleSignIn.getClient(getContext(), gso);
                GoogleClient.signOut().addOnCompleteListener(task -> {

                    Intent intent = new Intent(getContext(), Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                    getActivity().finish();
                });

            }
        });

        bookmarksB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), Bookmarks.class);
                startActivity(intent);

            }
        });

        profileB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), MyProfileActivity.class);
                startActivity(intent);

            }
        });

        leaderB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottomNavigationView.setSelectedItemId(R.id.navigation_leaderboard);
            }
        });
        return view;
    }

    private void initViews(View view)
    {
        logoutB = view.findViewById(R.id.logoutB);
        profile_img_text = view.findViewById(R.id.profile_img_text);
        name = view.findViewById(R.id.name);
        score = view.findViewById(R.id.total_score);
        rank = view.findViewById(R.id.rank);
        leaderB = view.findViewById(R.id.leaderB);
        bookmarksB = view.findViewById(R.id.bookmarkB);
        profileB= view.findViewById(R.id.profileB);
        bottomNavigationView = getActivity().findViewById(R.id.bottom_nav_bar);

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