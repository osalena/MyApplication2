package com.example.myapplication.Category;


import android.app.Fragment;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.myapplication.R;
import com.example.myapplication.dataBase.InfoCategory;

import java.util.List;


public class CategoryListFragment extends Fragment {
    private ListView                categoryList;
    private CategoryListDataAdapter adapter;
    private Context                 context = null;
    private List<InfoCategory>      list;
    //private InfoUser                user;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.category_details_layout, container, false);

        context = rootView.getContext();




        categoryList = (ListView) rootView.findViewById(R.id.category_details_list);

        //MyInfoManager.getInstance().openDataBase(context);
        //user = MyInfoManager.getInstance().getAllUsers().get(1);
        //InfoUser u = MyInfoManager.getInstance().readUserByUserName("Test");
        getCategoryList();
        //Toast.makeText(context, u.getUsername(), Toast.LENGTH_LONG).show();
        //List<InfoReceipt> list = MyInfoManager.getInstance().getAllReceipts();
        adapter = new CategoryListDataAdapter(context, R.layout.category_details_layout, list);
        categoryList.setAdapter(adapter);

        return rootView;
    }

    private void getCategoryList() {
        String[] mCategories = context.getResources().getStringArray(R.array.categories);
        // Get images.
        //String[] typedArray = context.getResources().getStringArray(R.array.categories_image);
        //InfoCategory c1 = new InfoCategory(mCategories[0], BitmapFactory.decodeResource(context.getResources(), R.drawable.dessert));
        //InfoCategory c2 = new InfoCategory(mCategories[1], BitmapFactory.decodeResource(context.getResources(), R.drawable.breakfast));
        //InfoCategory c3 = new InfoCategory(mCategories[2], BitmapFactory.decodeResource(context.getResources(), R.drawable.lunch));
        InfoCategory c1 = new InfoCategory("11",null);
        InfoCategory c2 = new InfoCategory("11",null);
        InfoCategory c3 = new InfoCategory("11",null);
        list.add(c1);
        list.add(c2);
        list.add(c3);
    }

}

