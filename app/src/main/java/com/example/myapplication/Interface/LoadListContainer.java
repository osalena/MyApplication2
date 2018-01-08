package com.example.myapplication.Interface;

import android.content.Context;
import android.os.Handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public  abstract class LoadListContainer {

    private static List<LoadListener> listeners = Collections.synchronizedList(new ArrayList<LoadListener>());
    private static Context ctx;


    public static void setCtx(Context ctx1){
        ctx = ctx1;
    }

    /**registers listeners to the load list**/

    public static  void registerLoadListener(LoadListener l){
        if(!listeners.contains(l)){
            listeners.add(l);
        }
    }
    /**unregisters listeners to the load list**/
    public static void unregisterLoadListener(LoadListener l){
        if(listeners.contains(l)){
            listeners.remove(l);
        }
    }
    /**notifies listeners about loading- finished**/
    public static void notifyLsteners(final List<LoadListContainer> list){
        Handler handler = new Handler(ctx.getMainLooper());

        Runnable myRunnable = new Runnable() {

            @Override
            public void run() {
                try{
                    for (LoadListener listener : listeners) {
                        listener.onPostLoad(list);
                    }
                }
                catch(Throwable t){
                    t.printStackTrace();
                }
            }
        };
        handler.post(myRunnable);

    }

}
