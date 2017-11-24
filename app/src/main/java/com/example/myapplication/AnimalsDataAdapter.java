package com.example.myapplication;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by admin on 11/22/2016.
 */

public  class AnimalsDataAdapter extends RecyclerView.Adapter<ViewHolder> {

    private LayoutInflater mLayoutInflater;


    protected int[] mImageResIds;
    protected String[] mNames;
    protected String[] mDescriptions;
    protected String[] mPosters;
    protected String[] mDates;
    protected String[] mLiked;
    protected String[] mCooked;
    protected Context context;


    public AnimalsDataAdapter(Context context, int[] mImageResIds, String[] mNames, String[] mDescriptions,
                              String[] mPosters, String[] mDates, String[] mLiked, String[] mCooked) {
        mLayoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.mImageResIds=mImageResIds;
        this.mNames = mNames;
        this.mPosters = mPosters;
        this.mDescriptions = mDescriptions;
        this.mDates = mDates;
        this.mLiked = mLiked;
        this.mCooked = mCooked;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.recycler_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        final int imageResId = mImageResIds[position];
        final String name = mNames[position];
        final String description = mDescriptions[position];
        final String poster = mPosters[position];
        final String date = mDates[position];
        final String liked = mLiked[position];
        final String cooked = mCooked[position];
        viewHolder.setData(imageResId, name, poster, date, liked, cooked);

        viewHolder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AnimalDetailsFragment detailsFragment = new AnimalDetailsFragment();
                // send data to fragment
                Bundle args = new Bundle();
                args.putInt(AnimalDetailsFragment.ARGUMENT_IMAGE_RES_ID, imageResId);
                args.putString(AnimalDetailsFragment.ARGUMENT_NAME, name);
                args.putString(AnimalDetailsFragment.ARGUMENT_DESCRIPTION, description);
                args.putString(AnimalDetailsFragment.ARGUMENT_POSTER, poster);
                args.putString(AnimalDetailsFragment.ARGUMENT_DATE, date);
                detailsFragment.setArguments(args);
                // open fragment
                FragmentManager fm = ((Activity)context).getFragmentManager();
                FragmentTransaction tr= fm.beginTransaction();
                tr.replace(R.id.root_layout, detailsFragment);
                tr.addToBackStack(null);
                tr .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNames.length;
    }

}

class ViewHolder extends RecyclerView.ViewHolder {
    // Views
    public ImageView mImageView;
    public TextView mNameTextView;
    public TextView mPosterTextView;
    public TextView mDateTextView;
    public TextView mByCookTextView;
    public ImageView mLogoImageView;
    public TextView mLikedTextView;
    public TextView mCookedTextView;


    public ViewHolder(View itemView) {
        super(itemView);

        // Get references to image and name.
        mImageView = (ImageView) itemView.findViewById(R.id.animal_image);
        mNameTextView = (TextView) itemView.findViewById(R.id.name);
        mPosterTextView = (TextView) itemView.findViewById(R.id.poster);
        mDateTextView = (TextView) itemView.findViewById(R.id.date);
        mByCookTextView = (TextView) itemView.findViewById(R.id.by_cook);
        mLogoImageView = (ImageView) itemView.findViewById(R.id.user_image);
        mLikedTextView = (TextView) itemView.findViewById(R.id.liked);
        mCookedTextView = (TextView) itemView.findViewById(R.id.cooked);

    }

    public void setData(int imageResId, String name, String poster, String date, String liked, String cooked) {
        mImageView.setImageResource(imageResId);
        mNameTextView.setText(name);
        mPosterTextView.setText(poster);
        mDateTextView.setText(date);
        mByCookTextView.setText(poster);
        mLogoImageView.setImageResource(imageResId);
        mLikedTextView.setText(liked);
        mCookedTextView.setText(cooked);



    }
}



