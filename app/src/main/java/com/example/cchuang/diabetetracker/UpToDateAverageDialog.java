package com.example.cchuang.diabetetracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;


/**
 * Created by cchuang on 6/28/2015.
 */
public class UpToDateAverageDialog extends DialogFragment {

    private static final String average = "Average_value";
    private static int mAverage;
    public UpToDateAverageDialog(){};


    @Override
    public Dialog onCreateDialog(Bundle saveInstanceState){
        View v = getActivity().getLayoutInflater()
                .inflate(R.layout.average_dialog,null);
        String temp = Integer.toString(mAverage);
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(temp)
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }

    public static UpToDateAverageDialog newInstance(int val){
        Bundle args = new Bundle();
        args.putInt(average, val);

        UpToDateAverageDialog dialog = new UpToDateAverageDialog();
        dialog.setArguments(args);
        mAverage = val;
        return dialog;
    }
/*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.average_dialog, null);

        TextView textView = (TextView) view.findViewById(R.id.image_dialog_textView);

        textView.setText( "Is the value correct");
        return view;
    }
*/


}
