package com.example.myapplication;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
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
import com.example.myapplication.LogIn.activity_signIn;
import com.example.myapplication.MyReceipts.CreateReceiptActivity;
import com.example.myapplication.MyReceipts.MyReceiptsActivity;
import com.example.myapplication.ReceiptsList.ReceiptsListFragment;
import com.example.myapplication.dataBase.InfoUser;
import com.example.myapplication.dataBase.MyInfoManager;


public class activity_main_cookpad extends AppCompatActivity {

    private Button               trending;
    private SearchView           searchView;
    private FloatingActionButton actionButton;
    private ActionBar            actionBar;
    private InfoUser             curUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        trending        =   (Button)findViewById(R.id.btn2);
        searchView      =   (SearchView)findViewById(R.id.search);
        actionButton    =   (FloatingActionButton)findViewById(R.id.myFAB);

        //to open DB
        MyInfoManager.getInstance().openDataBase(this);

        /* get current user info */
        Bundle b = getIntent().getExtras();
        curUser= MyInfoManager.getInstance().readUser(b.getInt("user"));

        /* to display Category List */
        getCategories();
        /* to open Fragment2 on create */
        showFragment1OnClick(trending);

        searchView.onActionViewExpanded();
        searchView.clearFocus();


        getSupportActionBar().setDisplayShowTitleEnabled(false);




        //actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setHomeButtonEnabled(true);


        //hideKeyboard();


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
        Intent intent = new Intent(activity_main_cookpad.this, CreateReceiptActivity.class);
        intent.putExtra("id", -1);
        intent.putExtra("user", curUser.getId());
        activity_main_cookpad.this.startActivity(intent);
    }


    public void showFragment1OnClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putInt("flag", 2);
        bundle.putInt("user", curUser.getId());
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
        Bundle bundle = new Bundle();
        bundle.putInt("flag", 1);
        bundle.putInt("user", curUser.getId());
        FragmentManager fm = getFragmentManager();
        ReceiptsListFragment fragmet2= new ReceiptsListFragment();
        fragmet2.setArguments(bundle);
        FragmentTransaction t= fm.beginTransaction();
        t.replace(R.id.root_layout, fragmet2, "fragment");
        t.addToBackStack(null);
        t.commit();
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
        showFragment2OnClick(trending);
        super.onResume();

    }

    @Override
    protected void onPause() {
        MyInfoManager.getInstance().closeDataBase();
        super.onPause();
    }

}
