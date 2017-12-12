package com.example.myapplication.Category;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.dataBase.InfoCategory;
import com.example.myapplication.dataBase.InfoReceipt;
import com.example.myapplication.dataBase.MyInfoManager;

import java.util.List;

/* DataAdapter for Category list
* receive:
*   list of categories
* */
public class CategoryListDataAdapter extends ArrayAdapter {

    protected Context            context;
    private List<InfoCategory>   categories;


    public CategoryListDataAdapter(Context context, int resource, List<InfoCategory> objects) {
        super(context, resource, objects);
        this.context = context;
        categories = objects;
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View rootView = mInflater.inflate(R.layout.category_list_details_layout, null);
        final InfoCategory currentCategory = getItem(position);
        TextView title = (TextView) rootView.findViewById(R.id.category_title);
        title.setText(currentCategory.getTitle());
        ImageView Image = (ImageView) rootView.findViewById(R.id.category_pic);
        Image.setImageBitmap(currentCategory.getImage());


        return rootView;
    }




    @Override
    public InfoCategory getItem(int position) {
        return categories.get(position);
    }

}
