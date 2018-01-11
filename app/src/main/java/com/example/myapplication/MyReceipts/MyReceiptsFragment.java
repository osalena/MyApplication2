package com.example.myapplication.MyReceipts;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.myapplication.Interface.LoadListContainer;
import com.example.myapplication.Interface.LoadListener;
import com.example.myapplication.MyReceipts.MyReceiptsDataAdapter;
import com.example.myapplication.R;
import com.example.myapplication.dataBase.InfoReceipt;
import com.example.myapplication.dataBase.InfoUser;
import com.example.myapplication.dataBase.MyInfoManager;
import com.example.myapplication.utils.NetworkConnector;
import com.example.myapplication.utils.NetworkResListener;
import com.example.myapplication.utils.ResStatus;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;



public class MyReceiptsFragment     extends Fragment implements LoadListener, NetworkResListener {
    private ListView                receiptsList;
    private MyReceiptsDataAdapter   adapter;
    private Context                 context = null;
    private InfoUser                curUser;
    private List<InfoReceipt>       list;

    private List<LoadListContainer> listC ;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView     = inflater.inflate(R.layout.my_rec_list2, container, false);
        context                 = rootView.getContext();
        /* to open DB */
        MyInfoManager.getInstance().openDataBase(context);
        /* get user ID from parent Activity */
        Bundle bundle           = this.getArguments();
        curUser                 = MyInfoManager.getInstance().readUser(String.valueOf(bundle.getInt("user")));
        receiptsList            = (ListView) rootView.findViewById(R.id.receipt_recycle);
        list                    = MyInfoManager.getInstance().getUserReceipt(curUser);
        //List<InfoReceipt> list  = MyInfoManager.getInstance().getAllReceipts();
        adapter                 = new MyReceiptsDataAdapter(context, R.layout.my_receipts_layout, list);
        receiptsList.setAdapter(adapter);
        receiptsList.setOnItemClickListener(receiptClickListener);

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NetworkConnector.getInstance().setContext(getActivity());
        NetworkConnector.getInstance().registerListener(MyReceiptsFragment.this);
        try {
            NetworkConnector.getInstance().sendRequestToServer(NetworkConnector.GET_ALL_RECEIPTS_JSON_REQ, new InfoReceipt());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

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


    @Override
    public void onPostLoad(List<LoadListContainer> list) {



        //LoadListContainer.unregisterLoadListener(this);

        List<InfoReceipt> r = new ArrayList<>();
        for (LoadListContainer l: list){
            r.add((InfoReceipt)l);
     //       System.out.println(((InfoReceipt) l).getTitle());
        }




        adapter = new MyReceiptsDataAdapter(context, R.layout.my_receipts_layout, r);

        receiptsList.setAdapter(adapter);
        //receiptsList.setOnItemClickListener(receiptClickListener);

    }

    @Override
    public void onPreUpdate() {
        Toast.makeText(this.context,"getting data from server",Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onPostUpdate(byte[] res, ResStatus status) {

        NetworkConnector.getInstance().unregisterListener(this);
        //to open DB

        String content = null;
        try{
            content = new String(res, "UTF-8");
        }
        catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        LoadListContainer.registerLoadListener(this);
        List<LoadListContainer> listOfReceipts = InfoReceipt.parseJson(content);
        //System.out.println("List of receipts" + listOfReceipts.size());
        if(listOfReceipts.isEmpty()){
            LoadListContainer.unregisterLoadListener(this);
        }

    }


}

