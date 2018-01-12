package com.example.myapplication.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Base64;
import android.util.LruCache;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.dataBase.InfoCategory;
import com.example.myapplication.dataBase.InfoReceipt;

import org.json.JSONException;
import org.json.JSONObject;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NetworkConnector {

    private static NetworkConnector mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static Context mCtx;


    private final String HOST_URL = "http://192.168.1.14:8080/";
    private  final String BASE_URL = HOST_URL + "projres";

    //private  final String BASE_URL = "http://192.168.1.14:8080/projres";
    private List<NetworkResListener> listeners = Collections.synchronizedList(new ArrayList<NetworkResListener>());
    private Context ctx;
    private static NetworkConnector instance;

    public static final String GET_ALL_RECEIPTS_JSON_REQ = "0";
    //private static final int INSERT_FOLDER_REQ = 1;
    //private static final int DELETE_FOLDER_REQ = 2;
    public static final String INSERT_RECEIPT_REQ = "3";
    public static final String DELETE_RECEIPT_REQ = "4";
    public static final String GET_RECEIPT_IMAGE_REQ = "5";
    public static final String GET_RECEIPT_REQ = "8";
    public static final String INSERT_RECEIPT_WITH_IMG_REQ = "6";

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
    private NetworkConnector() {

    }

    public static synchronized NetworkConnector getInstance() {
        if (mInstance == null) {
            mInstance = new NetworkConnector();
        }
        return mInstance;
    }

    public void initialize(Context context){
        mCtx = context;
        mRequestQueue = getRequestQueue();

        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    private RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    private void addToRequestQueue(String query, final NetworkResListener listener) {

        String reqUrl = BASE_URL + "?" + query;
        notifyPreUpdateListeners(listener);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, reqUrl, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        notifyPostUpdateListeners(response, ResStatus.SUCCESS, listener);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();

                        JSONObject err = null;
                        try {
                            err = new JSONObject(RESOURCE_FAIL_TAG);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        finally {
                            notifyPostUpdateListeners(err, ResStatus.FAIL, listener);
                        }

                    }
                });

        getRequestQueue().add(jsObjRequest);
    }

    private void addImageRequestToQueue(String query, final NetworkResListener listener){

        String reqUrl = BASE_URL + "?" + query;

        notifyPreUpdateListeners(listener);

        getImageLoader().get(reqUrl, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                Bitmap bm = response.getBitmap();
                notifyPostBitmapUpdateListeners(bm, ResStatus.SUCCESS, listener);
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                notifyPostBitmapUpdateListeners(null, ResStatus.FAIL, listener);
            }
        });
    }

    private ImageLoader getImageLoader() {
        return mImageLoader;
    }


    private void uploadItemImage(final InfoReceipt item, final NetworkResListener listener) {

        String reqUrl = HOST_URL + "images?";
        notifyPreUpdateListeners(listener);


        //our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, reqUrl,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            Toast.makeText(mCtx, obj.getString("message"), Toast.LENGTH_SHORT).show();
                            notifyPostUpdateListeners(obj, ResStatus.SUCCESS, listener);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mCtx, error.getMessage(), Toast.LENGTH_SHORT).show();
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(RESOURCE_FAIL_TAG );
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        finally {
                            notifyPostUpdateListeners(obj, ResStatus.FAIL, listener);
                        }

                    }
                }) {

            /*
            * If you want to add more parameters with the image
            * you can do it here
            * here we have only one parameter with the image
            * which is tags
            * */
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(RECEIPT_ID, item.getId());
                params.put(RECEIPT_TITLE, item.getTitle());
                params.put(RECEIPT_DESCRIPTION,  item.getDescription());
                params.put(RECEIPT_USER_ID, item.getUserId());
                return params;
            }

            /*
            * Here we are passing image by renaming it with a unique name
            * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                byte[] pic = InfoReceipt.getBitmapAsByteArray(item.getImage());
                params.put("fileField", new DataPart(imagename + ".png", pic));
                return params;
            }
        };

        //adding the request to volley
        getRequestQueue().add(volleyMultipartRequest);
    }


    public void sendRequestToServer(String requestCode, InfoReceipt data, NetworkResListener listener){
        if(data==null){
            return;
        }

        Uri.Builder builder = new Uri.Builder();

        switch (requestCode){
            case INSERT_RECEIPT_WITH_IMG_REQ:{

                uploadItemImage(data, listener);

                break;
            }
            case GET_RECEIPT_REQ:{

                builder.appendQueryParameter(REQ , requestCode);
                builder.appendQueryParameter(RECEIPT_ID , data.getId());
                String query = builder.build().getEncodedQuery();

                addToRequestQueue(query, listener);
            }
            case INSERT_RECEIPT_REQ:{
                builder.appendQueryParameter(REQ , requestCode);
                builder.appendQueryParameter(RECEIPT_ID , data.getId());
                builder.appendQueryParameter(RECEIPT_TITLE , data.getTitle());
                builder.appendQueryParameter(RECEIPT_DESCRIPTION , data.getDescription());

                String query = builder.build().getEncodedQuery();
                addToRequestQueue(query, listener);

                break;
            }
            case DELETE_RECEIPT_REQ:{
                builder.appendQueryParameter(REQ , requestCode);
                builder.appendQueryParameter(RECEIPT_ID , data.getId());

                String query = builder.build().getEncodedQuery();
                addToRequestQueue(query, listener);

                break;
            }
            case GET_RECEIPT_IMAGE_REQ: {
                builder.appendQueryParameter(REQ , requestCode);
                builder.appendQueryParameter(RECEIPT_ID , data.getId());

                String query = builder.build().getEncodedQuery();
                addImageRequestToQueue(query, listener);

                break;
            }
            case GET_ALL_RECEIPTS_JSON_REQ: {

            }
        }



    }


    public void update(NetworkResListener listener){

        Uri.Builder builder = new Uri.Builder();
        builder.appendQueryParameter(REQ , GET_ALL_RECEIPTS_JSON_REQ);
        String query = builder.build().getEncodedQuery();

        addToRequestQueue(query, listener);
    }

    /*public void sendRequestToServer(String requestCode, InfoFolder data, NetworkResListener listener) {


        if(data==null){
            return;
        }

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

        addToRequestQueue(query, listener);

    }*/

    private void clean() {

    }


    private  void notifyPostBitmapUpdateListeners(final Bitmap res, final ResStatus status, final NetworkResListener listener) {

        Handler handler = new Handler(mCtx.getMainLooper());

        Runnable myRunnable = new Runnable() {

            @Override
            public void run() {
                try{
                    listener.onPostUpdate(res, status);
                }
                catch(Throwable t){
                    t.printStackTrace();
                }
            }
        };
        handler.post(myRunnable);

    }

    private  void notifyPostUpdateListeners(final JSONObject res, final ResStatus status, final NetworkResListener listener) {

        Handler handler = new Handler(mCtx.getMainLooper());

        Runnable myRunnable = new Runnable() {

            @Override
            public void run() {
                try{
                    listener.onPostUpdate(res.toString().getBytes(), status);
                }
                catch(Throwable t){
                    t.printStackTrace();
                }
            }
        };
        handler.post(myRunnable);

    }

    private  void notifyPreUpdateListeners(final NetworkResListener listener) {


        Handler handler = new Handler(mCtx.getMainLooper());

        Runnable myRunnable = new Runnable() {

            @Override
            public void run() {
                try{
                    listener.onPreUpdate();
                }
                catch(Throwable t){
                    t.printStackTrace();
                }
            }
        };
        handler.post(myRunnable);

    }
}

