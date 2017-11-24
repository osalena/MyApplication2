package com.example.myapplication;

import android.app.Activity;
import android.app.Fragment;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.AnimalsDataAdapter;
import com.example.myapplication.R;


public class AnimalsListFragment extends Fragment {

    private int[] mImageResIds;
    private String[] mNames;
    private String[] mDescriptions;
    private String[] mPosters;
    private String[] mDates;




    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);

        // Get  names and descriptions.
        final Resources resources = context.getResources();
        mNames = resources.getStringArray(R.array.names);
        mDescriptions = resources.getStringArray(R.array.descriptions);
        mPosters = resources.getStringArray(R.array.posters);
        mDates = resources.getStringArray(R.array.dates);

        // Get images.
        final TypedArray typedArray = resources.obtainTypedArray(R.array.images);
        final int imageCount = mNames.length;
        mImageResIds = new int[imageCount];
        for (int i = 0; i < imageCount; i++) {
            mImageResIds[i] = typedArray.getResourceId(i, 0);
        }
        typedArray.recycle();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_animals_list, container, false);
        Activity activity = getActivity();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(activity, 1));
        recyclerView.setAdapter(new AnimalsDataAdapter(activity, mImageResIds, mNames, mDescriptions, mPosters, mDates));
        return view;
    }


}
