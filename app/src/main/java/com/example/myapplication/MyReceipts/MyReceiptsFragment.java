package com.example.myapplication.MyReceipts;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.myapplication.MyReceipts.MyReceiptsDataAdapter;
import com.example.myapplication.R;
import com.example.myapplication.dataBase.InfoReceipt;
import com.example.myapplication.dataBase.InfoUser;
import com.example.myapplication.dataBase.MyInfoManager;
import java.util.List;



public class MyReceiptsFragment     extends Fragment {
    private ListView                receiptsList;
    private MyReceiptsDataAdapter   adapter;
    private Context                 context = null;
    private InfoUser                curUser;
    private List<InfoReceipt>       list;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView     = inflater.inflate(R.layout.my_rec_list2, container, false);
        context                 = rootView.getContext();
        /* to open DB */
        MyInfoManager.getInstance().openDataBase(context);
        /* get user ID from parent Activity */
        Bundle bundle           = this.getArguments();
        curUser                 = MyInfoManager.getInstance().readUser(bundle.getInt("user"));
        receiptsList            = (ListView) rootView.findViewById(R.id.receipt_recycle);
        list                    = MyInfoManager.getInstance().getUserReceipt(curUser);
        //List<InfoReceipt> list  = MyInfoManager.getInstance().getAllReceipts();
        adapter                 = new MyReceiptsDataAdapter(context, R.layout.my_receipts_layout, list);
        receiptsList.setAdapter(adapter);
        receiptsList.setOnItemClickListener(receiptClickListener);

        return rootView;
    }

    /*to display details
   * editable mode
   * param receipt ID*/
    private AdapterView.OnItemClickListener receiptClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            InfoReceipt receipt = (InfoReceipt) parent.getItemAtPosition(position);
            if (receipt != null) {

                Intent intent = new Intent(context, CreateReceiptActivity.class);
                intent.putExtra("id", receipt.getId());
                context.startActivity(intent);
            }
        }
    };

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

