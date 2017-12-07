package com.example.myapplication;


import android.app.Activity;
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

import com.example.myapplication.ReceiptsList.ReceiptsListFragment;
import com.example.myapplication.dataBase.MyInfoManager;


public class activity_main_cookpad extends AppCompatActivity {

    private Button               trending;
    private SearchView           searchView;
    private FloatingActionButton actionButton;
    private ActionBar            actionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        trending        =   (Button)findViewById(R.id.btn2);
        searchView      =   (SearchView)findViewById(R.id.search);
        actionButton    =   (FloatingActionButton)findViewById(R.id.myFAB);

        /* to display Category List */
        getCategories();
        trending.callOnClick();
        //searchView.setIconified(false);
        searchView.onActionViewExpanded();
        searchView.clearFocus();

        //to get DB
        MyInfoManager.getInstance().openDataBase(this);

        //actionBar.setTitle(null);
        //actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setHomeButtonEnabled(true);
        //actionBar.setIcon(null)


        //hideKeyboard();


    }



    private void getCategories() {

        FragmentManager categoryManager = getFragmentManager();
        //Bundle argsCat = new Bundle();
        //argsCat.putInt("no", 555);
        //argsCat.putStringArray("category", getResources().getStringArray(R.array.categories));
        //argsCat.putString("name", "Israel Israel");
        CategoryFragment fragmentCat= new CategoryFragment();
       // fragmentCat.setArguments(argsCat);
        FragmentTransaction tCat= categoryManager.beginTransaction();
        tCat.add(R.id.category_layoutt, fragmentCat);
        tCat.addToBackStack(null);
        tCat.commit();

        FragmentManager fm = getFragmentManager();
        ReceiptsListFragment fragmet2= new ReceiptsListFragment();
        FragmentTransaction t= fm.beginTransaction();
        t.replace(R.id.root_layout, fragmet2);
        t.addToBackStack(null);
        t.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
      /*  switch (item.getItemId()) {
            case R.id.menuitem_search: {
                FindFragment findFragment = new FindFragment();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.rootview, findFragment);
                ft.commit();
                Toast.makeText(this, getString(R.string.ui_menu_search),
                        Toast.LENGTH_SHORT).show();

                return true;
            }
            case R.id.menuitem_send:
                Toast.makeText(this, getString(R.string.ui_menu_send),
                        Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menuitem_add:
                Toast.makeText(this, getString(R.string.ui_menu_add),
                        Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menuitem_share:
                Toast.makeText(this, getString(R.string.ui_menu_share),
                        Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menuitem_feedback: {
                ChatFragment chatFragment = new ChatFragment();
                FragmentTransaction ftransaction = fm.beginTransaction();
                ftransaction.replace(R.id.rootview, chatFragment);
                ftransaction.commit();

                Toast.makeText(this, getString(R.string.ui_menu_feedback),
                        Toast.LENGTH_SHORT).show();
                return true;
            }
            case R.id.menuitem_about:
                Intent intent = new Intent(this,AboutActivity.class);
                startActivity(intent);
                Toast.makeText(this, getString(R.string.ui_menu_about),
                        Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menuitem_quit:
                Toast.makeText(this, getString(R.string.ui_menu_quit),
                        Toast.LENGTH_SHORT).show();
                finish(); // close the activity
                return true;
        } */
        return false;
    }

    public void createNewReceiptOnClick (View view) {
        //Toast.makeText(getApplicationContext(), "dfdf", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(activity_main_cookpad.this, CreateReceiptActivity.class);
        activity_main_cookpad.this.startActivity(intent);
    }


    public void showFragment1OnClick(View view) {

        // send data to fragment
        FragmentManager fm = getFragmentManager();
        Bundle args = new Bundle();
        args.putInt("no", 123);
        args.putString("name", "Israel Israel");
        FirstFragment fragmet1= new FirstFragment();
        fragmet1.setArguments(args);
        FragmentTransaction t= fm.beginTransaction();
        t.replace(R.id.root_layout, fragmet1);
        t.addToBackStack(null);
        t.commit();

    }

    public void showFragment2OnClick(View view) {
        FragmentManager fm = getFragmentManager();
        ReceiptsListFragment fragmet2= new ReceiptsListFragment();
        FragmentTransaction t= fm.beginTransaction();
        t.replace(R.id.root_layout, fragmet2, "fragment");
        t.addToBackStack(null);
        t.commit();
    }

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
        MyInfoManager.getInstance().openDataBase(this);
        super.onResume();

    }

    @Override
    protected void onPause() {
        MyInfoManager.getInstance().closeDataBase();
        super.onPause();
    }

}
