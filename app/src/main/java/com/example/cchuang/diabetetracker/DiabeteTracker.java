package com.example.cchuang.diabetetracker;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class DiabeteTracker extends Activity {

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    private static final int TEXT_DETECT_NUM_REQUEST_CODE = 300;
    private Uri fileUri;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    public static final int DETECT_TYPE_NUM = 3;
    static final String TAG = "DBG_" + DiabeteTracker.class.getName();
    private static int bloodSugarVal = 0;
    public BloodSugarDbHelper mDbHelper;

    LocalService mService;
    boolean mBound = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diabete_tracker);
       // mDbHelper = new BloodSugarDbHelper(this);

        // create Intent to take a picture and return control to the calling application
       // Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

       //fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
       //intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

        // start the image capture Intent
       // startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        Log.d(TAG, "DiabeteTracker Oncreate");
    }

    @Override
    protected void onStart(){
        super.onStart();
        mDbHelper = new BloodSugarDbHelper(this);

        // Bind to LocalService
        Intent intent = new Intent(this, LocalService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        //mDbHelper.deleteRecordAll();
        /*
        Cursor cur = mDbHelper.queryRecord();

        if (cur!=null) {
            ArrayList<String> results = mService.populateDataBase(cur);
            ListView myListView = (ListView) findViewById(R.id.myListView);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_2, results);
            myListView.setAdapter(adapter);
        }
        */

    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == TEXT_DETECT_NUM_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                bloodSugarVal = data.getIntExtra("DETECT_VALUE",bloodSugarVal);
                Calendar c = Calendar.getInstance();
                Log.d(TAG,"current time: " + c.getTime());
               // SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                SimpleDateFormat df = new SimpleDateFormat("HH:mm dd-MMM-yyyy ");
                String formattedDate = df.format(c.getTime());
                mDbHelper.insertRecord(data.getIntExtra("DETECT_VALUE", bloodSugarVal), formattedDate);
               // ListView mListView = (ListView) findViewById(R.id.expandableListView);

                Cursor cur = mDbHelper.queryRecord();
                if (cur!=null) {
                    ArrayList<String> results = mService.populateDataBase(cur);
                    ListView myListView = (ListView) findViewById(R.id.myListView);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                            android.R.layout.simple_list_item_1, results);
                    myListView.setAdapter(adapter);
                }
                /*
                                cur.moveToFirst();
                                Log.d(TAG, cur.getString(cur.getColumnIndex("value")));
                                Log.d(TAG, cur.getString(cur.getColumnIndex("date")));
                                while(cur.moveToNext()) {
                                    Log.d(TAG, Integer.toString(cur.getCount()));
                                    Log.d(TAG, cur.getString(cur.getColumnIndex("value")));
                                    Log.d(TAG, cur.getString(cur.getColumnIndex("date")));
                }
                */
               // if (mRecord.isExternalStorageWritable()) {
               //     File mFile = mRecord.createRecordFileDir(this);
               //     Log.d("DBG_path", mFile.getPath());
                //}

                //myTextView.setText(buffer);




            }
        }

        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Image captured and saved to fileUri specified in the Intent
                Toast.makeText(this, "Image saved to:\n" +
                        data.getData(), Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the image capture
            } else {
                // Image capture failed, advise user
            }
        }

        if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Video captured and saved to fileUri specified in the Intent
                Toast.makeText(this, "Video saved to:\n" +
                        data.getData(), Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the video capture
            } else {
                // Video capture failed, advise user
            }
        }
    }


    public void takePic(View view){
        // create Intent to take a picture and return control to the calling application
       // Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Intent intent = new Intent(this, TakePicActivity.class);
        intent.putExtra("CAPTURE_VALUE",bloodSugarVal);
        // start the image capture Intent
        startActivityForResult(intent, TEXT_DETECT_NUM_REQUEST_CODE);
    }

    public void aveToDate(View view){
       // int temp = mService.getAverageBloodSugarValue();
       // Log.d(TAG + "returned average: ", Integer.toString(temp));
        UpToDateAverageDialog dialog = UpToDateAverageDialog.newInstance(mService.getAverageBloodSugarValue());
               dialog.show(this.getFragmentManager(), TAG);


    }

    /** Create a file Uri for saving an image or video */
    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            LocalService.LocalBinder binder = (LocalService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
}
