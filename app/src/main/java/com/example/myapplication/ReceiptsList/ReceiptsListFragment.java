package com.example.myapplication.ReceiptsList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;


import com.example.myapplication.R;
import com.example.myapplication.ReceiptsList.ReceiptsDataAdapter;
import com.example.myapplication.dataBase.InfoReceipt;
import com.example.myapplication.dataBase.MyInfoManager;

import java.util.ArrayList;
import java.util.List;


public class ReceiptsListFragment extends Fragment {

    private ListView receiptsList;
    private ReceiptsDataAdapter adapter;
    private Context context = null;
    private Activity ctx;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_animals_list, container, false);

        context = rootView.getContext();

        ctx = getActivity();
        /*if (ctx != null) {
            ctx.setTitle(ctx.getResources().getString(R.string.myinfo));
        }*/

        receiptsList = (ListView) rootView.findViewById(R.id.folderList);

        receiptsList.setOnItemClickListener(receiptClickListener);

        //newFolderBtn = (Button) rootView.findViewById(R.id.new_folder_btn);
        //newFolderBtn.setVisibility(View.VISIBLE);


        //newFolderBtn.setOnClickListener(newFloderListener);

        //InfoReceipt r1 = new InfoReceipt("DDDDD", "DDDDDDDDDDDDD", null);
        //MyInfoManager.getInstance().createReceipt(r1);
        //List<InfoReceipt> list = new ArrayList<InfoReceipt>();
        //list.add(r1);

        List<InfoReceipt> list = MyInfoManager.getInstance().getAllReceipts();
        adapter = new ReceiptsDataAdapter(context, R.layout.recycler_item, list);
        receiptsList.setAdapter(adapter);

        return rootView;
    }





/*
    private View.OnClickListener newFloderListener = new View.OnClickListener() {

        @Override
        public void onClick(View arg0) {
            final String title = "New Folder";
            final String msg = "Enter a new name for this folder";

            final EditText answerText = new EditText(context);
            AlertDialog.Builder  builder=  new AlertDialog.Builder(context);
            builder.setTitle(title);
            builder.setMessage(msg);
            builder.setView(answerText);

            builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String proptAnswer = answerText.getText().toString();
                    InfoFolder folder = new InfoFolder(proptAnswer);
                    MyInfoManager.getInstance().createFolder(folder);
                    List<InfoFolder> list = MyInfoManager.getInstance().getAllFolders();
                    adapter = new InfoFolderListAdapter(context, R.layout.folder_list_item, list);
                    foldersList.setAdapter(adapter);
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            });


            AlertDialog alertDialog = builder.create();

            alertDialog .show();

        }
    }; */


    // display details
    private AdapterView.OnItemClickListener receiptClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
          /*  InfoReceipt receipt = (InfoReceipt) parent.getItemAtPosition(position);
            if (folder != null) {

                MyInfoManager.getInstance().setSelectedReceipt(receipt);
                ReceiptsListFragment itemlistfragment = new ReceiptsListFragment();
                FragmentManager fManager =ctx.getFragmentManager();
                FragmentTransaction ft = fManager.beginTransaction();
                ft.replace(R.id.content_frame, itemlistfragment);
                ft.addToBackStack(null);
                ft.commit();


            } */
        }
    };

    //private int[] mImageResIds;
    //private String[] mNames;
    //private String[] mDescriptions;
    //private String[] mPosters;
    //private String[] mDates;
    //private String[] mLiked;
    //private String[] mCooked;




   /* @Override
    public void onAttach(Activity context) {
        super.onAttach(context);

        // Get  names and descriptions.
        final Resources resources = context.getResources();
        mNames = resources.getStringArray(R.array.names);
        mDescriptions = resources.getStringArray(R.array.descriptions);
        mPosters = resources.getStringArray(R.array.posters);
        mDates = resources.getStringArray(R.array.dates);
        mLiked = resources.getStringArray(R.array.liked);
        mCooked = resources.getStringArray(R.array.cooked);

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
        recyclerView.setAdapter(new ReceiptsDataAdapter(activity, mImageResIds, mNames, mDescriptions, mPosters, mDates, mLiked, mCooked));
        return view;
    } */


}
