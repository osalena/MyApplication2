package com.example.myapplication.ReceiptsList;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import com.example.myapplication.Interface.LoadListContainer;
import com.example.myapplication.MyReceipts.CreateReceiptActivity;
import com.example.myapplication.MyReceipts.MyReceiptsDataAdapter;
import com.example.myapplication.R;
import com.example.myapplication.dataBase.InfoReceipt;
import com.example.myapplication.dataBase.InfoUser;
import com.example.myapplication.dataBase.MyInfoManager;
import com.example.myapplication.utils.NetworkConnector;
import com.example.myapplication.utils.NetworkResListener;
import com.example.myapplication.utils.ResStatus;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class ReceiptsListFragment extends Fragment{

    private ListView            receiptsList;
    private ReceiptsDataAdapter adapter;
    private Context             context = null;
    private List<InfoReceipt>   list;
    private InfoUser            curUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_animals_list, container, false);
        Bundle bundle    = this.getArguments();
        context          = rootView.getContext();
        receiptsList     = (ListView)rootView.findViewById(R.id.folderList);
        curUser          = MyInfoManager.getInstance().readUser(String.valueOf(bundle.getInt("user")));


        if(bundle != null) {
            if (bundle.getInt("flag") == 1) {
                list = MyInfoManager.getInstance().getAllReceipts();
            }
            else if (bundle.getInt("flag") == 2){
                list = MyInfoManager.getInstance().getUserReceipt(curUser);
            }
            List<LoadListContainer> listOfReceipts = InfoReceipt.parseJson(bundle.getString("list"));
            List<InfoReceipt> r = new ArrayList<>();
            for (LoadListContainer l: listOfReceipts){
                r.add((InfoReceipt)l);
            }
            adapter = new ReceiptsDataAdapter(context, R.layout.recycler_item, r);

            receiptsList.setAdapter(adapter);
        }


        //adapter = new ReceiptsDataAdapter(context, R.layout.recycler_item, list);
        //receiptsList.setAdapter(adapter);
        receiptsList.setOnItemClickListener(receiptClickListener);



        return rootView;
    }



    /*to display details without editing
    * param receipt ID
    * param edit flag = 1 non editable mode*/
    private AdapterView.OnItemClickListener receiptClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            InfoReceipt receipt = (InfoReceipt) parent.getItemAtPosition(position);
            if (receipt != null) {

                Intent intent = new Intent(context, CreateReceiptActivity.class);
                intent.putExtra("id", receipt.getId());
                intent.putExtra("edit_flag", 1);
                context.startActivity(intent);


            }
        }
    };

}
