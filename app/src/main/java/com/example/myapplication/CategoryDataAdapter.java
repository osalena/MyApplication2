package com.example.myapplication;


import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class CategoryDataAdapter extends RecyclerView.Adapter<CategoryViewHolder>{

    private LayoutInflater mLayoutInflater;



    protected String[] mCategories;
    protected Context context;


    public CategoryDataAdapter(Context context, String[] mCategories) {
        mLayoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.mCategories=mCategories;

    }


    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new CategoryViewHolder(mLayoutInflater.inflate(R.layout.category_fragment_layout, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder viewHolder, final int position) {

        final String category = mCategories[position];
        viewHolder.setData(category);


        viewHolder.mCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(category.equals("categories")){
                    Intent intent = new Intent(context, Category_list.class);
                    context.startActivity(intent);
               }

            }
        });
    }

    @Override
    public int getItemCount() {
        return mCategories.length;
    }

}

class CategoryViewHolder extends RecyclerView.ViewHolder {

    // Views

    public Button mCategory;


    public CategoryViewHolder(View itemView) {
        super(itemView);

        // Get references to image and name.
        mCategory = (Button) itemView.findViewById(R.id.category_button);

    }

    public void setData(String category) {
        mCategory.setText(category);
    }

}
