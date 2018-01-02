package com.example.myapplication.dataBase;


import android.content.Context;
import android.widget.Toast;

import com.example.myapplication.utils.NetworkResListener;
import com.example.myapplication.utils.ResStatus;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MyInfoManager implements NetworkResListener {
    private static MyInfoManager instance = null;
    private Context context = null;
    private MyDataBase db = null;
    private InfoUser selectedUser = null;
    private InfoReceipt selectedReceipt = null;


    public static MyInfoManager getInstance() {
        if (instance == null) {
            instance = new MyInfoManager();
        }
        return instance;
    }

    public MyDataBase getDB(){
        return db;
    }

    public static void releaseInstance() {
        if (instance != null) {
            instance.clean();
            instance = null;
        }
    }

    private void clean() {

    }


    public Context getContext() {
        return context;

    }

    public void openDataBase(Context context) {
        this.context = context;
        if (context != null) {
            db = new MyDataBase(context);
            db.open();
        }
    }
    public void closeDataBase() {
        if(db!=null){
            db.close();
        }
    }

    public void createReceipt(InfoUser user, InfoReceipt receipt) {
        /*if (db != null) {
            db.createReceipt(getSelectedUser(), receipt);
        } */
        if (db != null){
            db.createReceipt(user, receipt);
        }
    }

    public InfoUser readUserByUserName(String userName) {
        if (db != null) {
            InfoUser u = db.readUserByUserName(userName);
            return u;
        }
        return null;
    }


    public void createUser(InfoUser user) {
        if (db != null) {
            db.createUser(user);
        }
    }

    public void deleteTable(String tableName){
        db.deleteTable(tableName);
    }

    /*public void createTable(String t){
        db.createTable(t);
    }*/

    public InfoReceipt readReceipt(String id) {
        InfoReceipt result = null;
        if (db != null) {
            result = db.readReceipt(id);
        }
        return result;
    }

    public InfoUser readUser(String id) {
        InfoUser result = null;
        if (db != null) {
            result = db.readUser(id);
        }
        return result;
    }

    public List<InfoReceipt> getAllReceipts() {
        List<InfoReceipt> result = new ArrayList<InfoReceipt>();
        if (db != null) {
            result = db.getAllReceipts();
        }
        return result;
    }

    public List<InfoUser> getAllUsers() {
        List<InfoUser> result = new ArrayList<InfoUser>();
        if (db != null) {
            result = db.getAllUsers();
        }
        return result;
    }

    public void updateReceipt(InfoReceipt receipt) {
        if (db != null && receipt != null) {
            db.updateReceipt(receipt);
        }
    }

    public void updateUser(InfoUser user) {
        if (db != null && user != null) {
            db.updateUser(user);
        }
    }

    public void deleteReceipt(InfoReceipt receipt) {
        if (db != null) {
            db.deleteReceipt(receipt);
        }
    }

    public void deleteUser(InfoUser user) {
        if (db != null) {
            db.deleteUser(user);
        }
    }

    public List<InfoReceipt> getUserReceipt(InfoUser user) {
        List<InfoReceipt> result = new ArrayList<InfoReceipt>();
        if (db != null && user != null) {
            result = db.getAllReceiptsOfUser(user);
        }
        return result;
    }

    public InfoUser getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(InfoUser selectedUser) {
        this.selectedUser = selectedUser;
    }

    public InfoReceipt getSelectedReceipt() {
        return selectedReceipt;
    }

    public void setSelectedReceipt(InfoReceipt selectedReceipt) {
        this.selectedReceipt = selectedReceipt;
    }


    public void deleteSelectedUser(){
        deleteUser(selectedUser);
    }

    @Override
    public void onPreUpdate() {
        Toast.makeText(context,"Sync stated...",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPostUpdate(byte[] res, ResStatus status) {
        Toast.makeText(context,"Sync finished...status " + status.toString(),Toast.LENGTH_SHORT).show();
    }


    public void updateResources(byte[] res) {
        if(res==null){
            return;
        }
        try {
            String content = new String(res, "UTF-8");
            List<InfoReceipt> list = InfoReceipt.parseJson(content);
            if(list!=null && list.size()>0) {
                for(InfoReceipt r: list) {
                    syncCreateReceipt(r);
                }

            }
            } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        catch(Throwable t){
            t.printStackTrace();
        }

    }


    private boolean syncUpdateReceipt(InfoReceipt r) {
        boolean res = false;
        if (db != null) {
            if(!db.createReceipt(r)){
                db.updateReceipt(r);
                res = true;
            }
            else{
                res = true;
            }
        }
        return res;
    }


    private boolean syncCreateReceipt(InfoReceipt r) {
        boolean res = false;
        if (db != null) {
            if(!db.createReceipt(r)){
                db.updateReceipt(r);
                res = true;
            }
            else{
                res = true;
            }
        }
        return res;
    }
}
