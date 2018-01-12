package com.example.myapplication;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.Category.CategoryFragment;
import com.example.myapplication.Interface.LoadListContainer;
import com.example.myapplication.Interface.LoadListener;
import com.example.myapplication.LogIn.activity_signIn;
import com.example.myapplication.MyReceipts.CreateReceiptActivity;
import com.example.myapplication.MyReceipts.MyReceiptsActivity;
import com.example.myapplication.ReceiptsList.ReceiptsListFragment;
import com.example.myapplication.dataBase.InfoReceipt;
import com.example.myapplication.dataBase.InfoUser;
import com.example.myapplication.dataBase.MyInfoManager;
import com.example.myapplication.utils.NetworkConnector;
import com.example.myapplication.utils.NetworkResListener;
import com.example.myapplication.utils.ResStatus;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;


public class activity_main_cookpad extends AppCompatActivity implements LoadListener, NetworkResListener {

    private Button               trending;
    private SearchView           searchView;
    private FloatingActionButton actionButton;
    private ActionBar            actionBar;
    private InfoUser             curUser;
    private ProgressDialog       progressDialog = null;

    /*
    Notification
     */
    // Notification ID to allow for future updates
    private static final int MY_NOTIFICATION_ID = 1;
    // Notification Count
    private int mNotificationCount;
    // Notification Action Elements
    private Intent mNotificationIntent;
    private PendingIntent mContentIntent;

    private List<LoadListContainer> listC ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* to define layout */
        setContentView(R.layout.activity_my);

        //to open DB
        MyInfoManager.getInstance().openDataBase(activity_main_cookpad.this);

        progressDialog = new ProgressDialog(this);



        trending        =   (Button)findViewById(R.id.btn2);
        searchView      =   (SearchView)findViewById(R.id.search);
        actionButton    =   (FloatingActionButton)findViewById(R.id.myFAB);

        /* get current user info */
        Bundle b = getIntent().getExtras();
        curUser= MyInfoManager.getInstance().readUser(String.valueOf(b.getInt("user")));

        /* to display Category List */
        getCategories();
        /* to open Fragment2 on create */
        //showFragment1OnClick(trending);

        searchView.onActionViewExpanded();
        searchView.clearFocus();


        getSupportActionBar().setDisplayShowTitleEnabled(false);

        /*Notification intent */
        mNotificationIntent = new Intent(getApplicationContext(), MyNotificationsActivity.class);
        mContentIntent = PendingIntent.getActivity(getApplicationContext(), 0, mNotificationIntent, Intent.FILL_IN_ACTION);




        //actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setHomeButtonEnabled(true);


        //hideKeyboard();


        /*to define neetwork connection and listener */
        NetworkConnector.getInstance().initialize(this);
        NetworkConnector.getInstance().update(this);
    }

    private void createNotification (){
        // Define the Notification's expanded message and Intent:
        // Notification Sound and Vibration on Arrival
        Uri soundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        long[] mVibratePattern = { 0, 200, 200, 300 };

        // Notification Text Elements
        String contentTitle = "Notification";
        String contentText = "You have a new message!";

        Notification.Builder notificationBuilder = new Notification.Builder(getApplicationContext());

        notificationBuilder.setSmallIcon(android.R.drawable.stat_sys_warning);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setContentTitle(contentTitle);
        notificationBuilder.setContentText(contentText + " (" + ++mNotificationCount + ")");
        notificationBuilder.setContentIntent(mContentIntent).setSound(soundURI);
        notificationBuilder.setVibrate(mVibratePattern);

        // Pass the Notification to the NotificationManager:
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(MY_NOTIFICATION_ID, notificationBuilder.build());
    }



    private void getCategories() {

        FragmentManager categoryManager = getFragmentManager();
        CategoryFragment fragmentCat= new CategoryFragment();
        FragmentTransaction tCat= categoryManager.beginTransaction();
        tCat.add(R.id.category_layoutt, fragmentCat);
        tCat.addToBackStack(null);
        tCat.commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.cook_pad_icon:{
                Intent intent = new Intent(activity_main_cookpad.this, MyReceiptsActivity.class);
                intent.putExtra("user", curUser.getId());
                activity_main_cookpad.this.startActivity(intent);
                return true;
            }
            case R.id.cook_pad_notification: {
                Intent intent = new Intent(activity_main_cookpad.this, MyNotificationsActivity.class);
                intent.putExtra("user", curUser.getId());
                activity_main_cookpad.this.startActivity(intent);
                return true;
            }
            case R.id.cook_pad_conversation: {
                Intent intent = new Intent(activity_main_cookpad.this, MyConversationsActivity.class);
                intent.putExtra("user", curUser.getId());
                activity_main_cookpad.this.startActivity(intent);
                return true;
            }
        }
        return false;
    }

    public void createNewReceiptOnClick (View view) {
        createNotification();
        Intent intent = new Intent(activity_main_cookpad.this, CreateReceiptActivity.class);
        intent.putExtra("id", "-1");
        intent.putExtra("user", curUser.getId());
        activity_main_cookpad.this.startActivity(intent);
    }


    public void showFragment1OnClick(String cont) {
        Bundle bundle = new Bundle();
        bundle.putInt("flag", 2);
        bundle.putInt("user", curUser.getId());
        bundle.putString("list", cont);
        FragmentManager fm = getFragmentManager();
        ReceiptsListFragment fragmet2= new ReceiptsListFragment();
        fragmet2.setArguments(bundle);
        FragmentTransaction t= fm.beginTransaction();
        t.replace(R.id.root_layout, fragmet2, "fragment");
        t.addToBackStack(null);
        t.commit();
        // send data to fragment
       /* FragmentManager fm = getFragmentManager();
        Bundle args = new Bundle();
        args.putInt("no", 123);
        args.putString("name", "Israel Israel");
        FirstFragment fragmet1= new FirstFragment();
        fragmet1.setArguments(args);
        FragmentTransaction t= fm.beginTransaction();
        t.replace(R.id.root_layout, fragmet1);
        t.addToBackStack(null);
        t.commit(); */

    }

    public void showFragment2OnClick(View view) {
        /*Bundle bundle = new Bundle();
        bundle.putInt("flag", 1);
        bundle.putInt("user", curUser.getId());
        FragmentManager fm = getFragmentManager();
        ReceiptsListFragment fragmet2= new ReceiptsListFragment();
        fragmet2.setArguments(bundle);
        FragmentTransaction t= fm.beginTransaction();
        t.replace(R.id.root_layout, fragmet2, "fragment");
        t.addToBackStack(null);
        t.commit();*/
    }

    public void showNearbyFragmentOnClick(View view){
        FragmentManager fm = getFragmentManager();
        Fragment f = new Fragment();
        FragmentTransaction t= fm.beginTransaction();
        t.replace(R.id.root_layout, f, "fragment");
        t.addToBackStack(null);
        t.commit();


    }


  /*  private void showFragment2onCreate(){
        Bundle bundle = new Bundle();
        bundle.putInt("flag", 1);
        FragmentManager fm = getFragmentManager();
        ReceiptsListFragment fragmet2= new ReceiptsListFragment();
        fragmet2.setArguments(bundle);
        FragmentTransaction t= fm.beginTransaction();
        t.replace(R.id.root_layout, fragmet2);
        t.addToBackStack(null);
        t.commit();
    } */

    //doesn't work
    /*private void hideKeyboard() {
        try {
            View view = this.getCurrentFocus();
            if (view != null) {
                final InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }*/


    @Override
    protected void onResume() {
        /*MyInfoManager.getInstance().openDataBase(this);
        FragmentManager fm = getFragmentManager();
        ReceiptsListFragment fragmet2= new ReceiptsListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("flag", 1);
        fragmet2.setArguments(bundle);
        FragmentTransaction t= fm.beginTransaction();
        t.replace(R.id.root_layout, fragmet2, "fragment");
        t.addToBackStack(null);
        t.commit();*/
        NetworkConnector.getInstance().update(this);
        super.onResume();

    }

    @Override
    protected void onPause() {

        MyInfoManager.getInstance().closeDataBase();
        super.onPause();
    }

//    @Override
//    public void onPostLoad(List<LoadListContainer> list) {
//
//        LoadListContainer.unregisterLoadListener(this);
//    }

    @Override
    public void onPostLoad(List<LoadListContainer> list) {

    }

    @Override
    public void onPreUpdate() {

        progressDialog.setTitle("Updating resources");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    @Override
    public void onPostUpdate(byte[] res, ResStatus status) {
        //to open DB
        progressDialog.dismiss();
        String content = null;
        try{
            content = new String(res, "UTF-8");
            System.out.println(content);
        }
        catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }

        LoadListContainer.registerLoadListener(this);
        List<LoadListContainer> listOfReceipts = InfoReceipt.parseJson(content);
        if(listOfReceipts.isEmpty()){
            LoadListContainer.unregisterLoadListener(this);
        }
        if(status == ResStatus.SUCCESS){



            //  MyInfoManager.getInstance().updateResources(res);
            Toast.makeText(this, "download ok...", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this,"download failed...", Toast.LENGTH_LONG).show();
        }

        showFragment1OnClick(content);


        // hideKeyboard();




    }

    @Override
    public void onPostUpdate(JSONObject res, ResStatus status) {

    }

    @Override
    public void onPostUpdate(Bitmap res, ResStatus status) {
        //to open DB

    }


}
