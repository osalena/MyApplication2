package com.example.myapplication.Category;

import android.app.Activity;
import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.Category.CategoryDataAdapter;
import com.example.myapplication.R;


public class CategoryFragment extends Fragment{

    private String[] mCategories;


    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);

        // Get  names and descriptions.
        final Resources resources = context.getResources();
        mCategories = resources.getStringArray(R.array.categories);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_list, container, false);
        Activity activity = getActivity();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.category_recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(new CategoryDataAdapter(activity, mCategories));
        return view;
    }

/*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.category_fragment_layout, container, false);

        TextView noTextView = (TextView) view.findViewById(R.id.categories);

        Bundle args = getArguments();
        if(args!=null) {
            String no = String.valueOf(args.getString("category"));
            noTextView.setText(no);
        }
        return view;
    } */
}
