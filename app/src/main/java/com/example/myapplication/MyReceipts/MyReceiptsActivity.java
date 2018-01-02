package com.example.myapplication.MyReceipts;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.dataBase.InfoUser;
import com.example.myapplication.dataBase.MyInfoManager;

/* MyReceiptsActivity class
* creates filtered list of my receipts*/

public class MyReceiptsActivity extends AppCompatActivity {

    private InfoUser curUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* to bind layout */
        setContentView(R.layout.activity_my_receipts);

        /* to display BACK button */
        android.app.ActionBar actionBar = getActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //getSupportActionBar().setTitle(curUser.getUsername());
        getSupportActionBar().setTitle("My Receipts");

        //to open DB
        MyInfoManager.getInstance().openDataBase(this);

        /* get current user info */
        Bundle b = getIntent().getExtras();
        curUser= MyInfoManager.getInstance().readUser(String.valueOf(b.getInt("user")));
        //Toast.makeText(MyReceiptsActivity.this, curUser.getUsername(), Toast.LENGTH_SHORT).show();

        /* to define fragment */
        Bundle bundle = new Bundle();
        bundle.putInt("user", curUser.getId());
        FragmentManager fm = getFragmentManager();
        MyReceiptsFragment fragment= new MyReceiptsFragment();
        fragment.setArguments(bundle);
        FragmentTransaction t= fm.beginTransaction();
        t.replace(R.id.my_receipts_layout, fragment, "fragment");
        t.addToBackStack(null);
        t.commit();
        /* end define fragment */
    }

    /* Listener for menu's items*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                super.onBackPressed();
                return true;
        }
        return true;
    }

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
