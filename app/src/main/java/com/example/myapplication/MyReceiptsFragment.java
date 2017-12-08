package com.example.myapplication;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.myapplication.dataBase.InfoReceipt;
import com.example.myapplication.dataBase.InfoUser;
import com.example.myapplication.dataBase.MyInfoManager;
import java.util.List;



public class MyReceiptsFragment     extends Fragment {
    private ListView                receiptsList;
    private MyReceiptsDataAdapter   adapter;
    private Context                 context = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.my_rec_list2, container, false);

        context = rootView.getContext();




        receiptsList = (ListView) rootView.findViewById(R.id.receipt_recycle);

        MyInfoManager.getInstance().openDataBase(context);
        InfoUser u = MyInfoManager.getInstance().readUserByUserName("Test");
        List<InfoReceipt> list = MyInfoManager.getInstance().getUserReceipt(u);
        Toast.makeText(context, u.getUsername(), Toast.LENGTH_LONG).show();
        //List<InfoReceipt> list = MyInfoManager.getInstance().getAllReceipts();
        adapter = new MyReceiptsDataAdapter(context, R.layout.my_receipts_layout, list);
        receiptsList.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onResume() {
        MyInfoManager.getInstance().openDataBase(context);
        super.onResume();

    }

    @Override
    public void onPause() {
        MyInfoManager.getInstance().closeDataBase();
        super.onPause();
    }


}

