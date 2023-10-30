package com.example.e_learningbahasaisyarat.ui.category;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.e_learningbahasaisyarat.DbQuery;
import com.example.e_learningbahasaisyarat.MainActivity;
import com.example.e_learningbahasaisyarat.R;

public class CategoryFragment extends Fragment {

    public CategoryFragment() {
        //Required empty public constructor
    }
    private GridView catView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment);
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("CATEGORIES");

        catView = view.findViewById(R.id.cat_grid);

        CategoryAdapter adapter = new CategoryAdapter(DbQuery.g_catList);
        catView.setAdapter(adapter);

        return view;
    }
}