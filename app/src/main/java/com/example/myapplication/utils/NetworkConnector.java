package com.example.myapplication.utils;


import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Base64;

import com.android.volley.toolbox.JsonObjectRequest;
import com.example.myapplication.dataBase.InfoCategory;
import com.example.myapplication.dataBase.InfoReceipt;

import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class NetworkConnector {

    //private final String HOST_URL = "http://10.0.2.2:8080/";
    //private  final String BASE_URL = HOST_URL + "projres";

    private  final String BASE_URL = "http://192.168.1.17:8080/projres";
    private List<NetworkResListener> listeners = Collections.synchronizedList(new ArrayList<NetworkResListener>());
    private Context ctx;
    private static NetworkConnector instance;

    public static final String GET_ALL_RECEIPTS_JSON_REQ = "0";
    //private static final int INSERT_FOLDER_REQ = 1;
    //private static final int DELETE_FOLDER_REQ = 2;
    public static final String INSERT_RECEIPT_REQ = "3";
    public static final String DELETE_RECEIPT_REQ = "4";
    public static final String GET_RECEIPT_IMAGE_REQ = "5";
    //private static final int GET_RECEIPTS_OF_USER_JSON_REQ = 6;
    private static final String GET_FILE_FROM_FILESYSTEM_REQ = "7";

    private static final String USER_ID = "u_id";
    private static final String USER_NAME = "u_name";

    public static final String RESOURCE_FAIL_TAG = "{\"result_code\":0}";
    public static final String RESOURCE_SUCCESS_TAG = "{\"result_code\":1}";

    public static final String RECEIPT_ID = "rec_id";
    public static final String RECEIPT_TITLE = "rec_title";
    public static final String RECEIPT_DESCRIPTION = "rec_desc";
    public static final String RECEIPT_USER_ID = "rec_uid";
    public static final String RECEIPT_IMAGE = "rec_img";
    public static final String REQ = "req";

   private final int RETRY_TIMES = 2;






    public void sendRequestToServer(String requestCode, InfoReceipt data) throws UnsupportedEncodingException {

        if(data==null){
            return;
        }

        NetworkTask networkTask = new NetworkTask();


        Uri.Builder builder = new Uri.Builder();

        switch (requestCode){
            case INSERT_RECEIPT_REQ:{
                builder.appendQueryParameter(REQ , requestCode);
                //builder.appendQueryParameter(RECEIPT_ID , String.valueOf(data.getId()));
                builder.appendQueryParameter(RECEIPT_ID , String.valueOf(0));
                builder.appendQueryParameter(RECEIPT_TITLE , data.getTitle());
                builder.appendQueryParameter(RECEIPT_DESCRIPTION , data.getDescription());

                String s = new String(InfoReceipt.getBitmapAsByteArray(data.getImage()),"UTF-8");
                builder.appendQueryParameter(RECEIPT_IMAGE,s);
                builder.appendQueryParameter(RECEIPT_USER_ID , String.valueOf(data.getUserId()));
                break;
            }
            case DELETE_RECEIPT_REQ:{
                builder.appendQueryParameter(REQ , requestCode);
                builder.appendQueryParameter(RECEIPT_ID , String.valueOf(data.getId()));
                break;
            }
            case GET_RECEIPT_IMAGE_REQ: {
                builder.appendQueryParameter(REQ , requestCode);
                builder.appendQueryParameter(RECEIPT_ID , String.valueOf(data.getId()));

                break;
            }
            case GET_ALL_RECEIPTS_JSON_REQ:{

                builder.appendQueryParameter(REQ , GET_ALL_RECEIPTS_JSON_REQ);
                String query = builder.build().getEncodedQuery();


            }
        }

        String query = builder.build().getEncodedQuery();

        networkTask.execute(query);

    }


    public void update(){
        NetworkTask networkTask = new NetworkTask();

        Uri.Builder builder = new Uri.Builder();
        builder.appendQueryParameter(REQ , GET_ALL_RECEIPTS_JSON_REQ);
        String query = builder.build().getEncodedQuery();

        networkTask.execute(query);
    }

    /*public void sendRequestToServer(String requestCode, InfoCategory data) {
        if(data==null){
            return;
        }

        NetworkTask networkTask = new NetworkTask();

        Uri.Builder builder = new Uri.Builder();

        switch (requestCode){
            case INSERT_FOLDER_REQ:{
                builder.appendQueryParameter(REQ , requestCode);
                builder.appendQueryParameter(FOLDER_ID ,data.getId());
                builder.appendQueryParameter(FOLDER_TITLE , data.getTitle());
                break;
            }
            case DELETE_FOLDER_REQ:{
                builder.appendQueryParameter(REQ , requestCode);
                builder.appendQueryParameter(FOLDER_ID , data.getId());
                break;
            }
        }
        String query = builder.build().getEncodedQuery();
        networkTask.execute(query);

    }*/


    private NetworkConnector()
    {
        super();
    }

    public static NetworkConnector getInstance(){
        if(instance==null){
            instance = new NetworkConnector();

        }
        return instance;
    }

    public void setContext(Context ctx){
        this.ctx = ctx;
    }





    public static void releaseInstance() {
        if (instance != null) {
            instance.clean();
            instance = null;
        }
    }

    private void clean() {
        listeners.clear();
    }

    public boolean unregisterListener(NetworkResListener listener){
        boolean result = false;
        if(listener!=null){
            if(listeners.contains(listener)){
                result= listeners.remove(listener);
            }
        }
        return result;
    }

    public void registerListener(NetworkResListener listener) {
        if(listener!=null){
            if(!listeners.contains(listener)){
                listeners.add(listener);
            }
        }
    }


    private  void notifyPostUpdateListeners(final byte[] res, final ResStatus status) {

        Handler handler = new Handler(ctx.getMainLooper());

        Runnable myRunnable = new Runnable() {

            @Override
            public void run() {
                try{
                    for (NetworkResListener listener : listeners) {
                        listener.onPostUpdate(res, status);
                    }
                }
                catch(Throwable t){
                    t.printStackTrace();
                }
            }
        };
        handler.post(myRunnable);

    }

    private  void notifyPreUpdateListeners() {


        Handler handler = new Handler(ctx.getMainLooper());

        Runnable myRunnable = new Runnable() {

            @Override
            public void run() {
                try{
                    for (NetworkResListener listener : listeners) {
                        listener.onPreUpdate();
                    }
                }
                catch(Throwable t){
                    t.printStackTrace();
                }
            }
        };
        handler.post(myRunnable);

    }


    private byte[] getResFromServer(String query, int retry){

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        InputStream in = null;

        try {

            URL url = new URL(BASE_URL);


            int timeoutConnection = 10000;

            int timeoutSocket = 10000;


            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(timeoutConnection);
            conn.setReadTimeout(timeoutSocket);

            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);


            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();

            in = new BufferedInputStream(conn.getInputStream());

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {

                byte[] buffer = new byte[4096];
                int n = -1;

                while ((n = in.read(buffer)) != -1) {
                    if (n > 0) {
                        out.write(buffer, 0, n);
                    }
                }
            }
            else{
                retry=0;
                return new byte[0];
            }


        } catch (Throwable e) {
            e.printStackTrace();
            if(retry==0){
                return new byte[0];
            }
            return getResFromServer(query, retry - 1);
        } finally {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
            } catch (Throwable e) {}
        }
        return   out.toByteArray();

    }


    private class NetworkTask extends AsyncTask<String, Void, byte[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            notifyPreUpdateListeners();

        }

        @Override
        protected byte[] doInBackground(String... params) {

            byte[] res= getResFromServer(params[0],RETRY_TIMES);
            return res;

        }

        @Override
        protected void onPostExecute(byte[] res) {

            super.onPostExecute(res);

            if(res!=null && res.length>0){
                String resp = new String(res);
                if(!resp.equals(RESOURCE_FAIL_TAG)) {
                    notifyPostUpdateListeners(res, ResStatus.SUCCESS);
                }
            }

            else{
                notifyPostUpdateListeners(res, ResStatus.FAIL);
            }

        }

    }



}

