package com.example.cchuang.diabetetracker;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;


public class LocalService extends Service {
    // Binder given to clients
    private final IBinder mBinder = (IBinder) new LocalBinder();
    private static int mAverageBloodSugarValue=0;
    static final String TAG = "DBG_" + LocalService.class.getName();

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        LocalService getService() {
            // Return this instance of LocalService so clients can call public methods
            return LocalService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public ArrayList populateDataBase(Cursor c) {
        ArrayList<String> results = new ArrayList();
        int temp = 0;
        if (c != null) {
            if (c.moveToFirst()) {
                do {

                    String date = c.getString(c.getColumnIndex("date"));
                    int value = c.getInt(c.getColumnIndex("value"));
                    results.add( date + " Blood Sugar Value: " + Integer.toString(value));
                    temp += value;
                } while (c.moveToNext());
            }

        }

        mAverageBloodSugarValue = temp/c.getCount();
        Log.d(TAG + "counts", Integer.toString(c.getCount()));
        Log.d(TAG + "ave", Integer.toString(mAverageBloodSugarValue));
        return results;
    }

    public int getAverageBloodSugarValue(){
        return mAverageBloodSugarValue;}

}
