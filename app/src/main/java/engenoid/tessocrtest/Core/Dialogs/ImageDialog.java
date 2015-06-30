package engenoid.tessocrtest.Core.Dialogs;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cchuang.diabetetracker.R;

/**
 * Created by Fadi on 5/11/2014.
 */
public class ImageDialog extends DialogFragment implements View.OnClickListener {

    private Bitmap bmp;

    private String title;

    public ImageDialog(){}
    Button yesButton;
    Button noButton;

    public static ImageDialog New(){
        return new ImageDialog();
    }

    public ImageDialog addBitmap(Bitmap bmp) {
        if (bmp != null)
            this.bmp = bmp;
        return this;
    }

    public ImageDialog addTitle(String title) {
        if (title != null)
            this.title = title;
        return this;
    }

    @Override
    public void onClick(View v){
        if(v == yesButton){
            Intent value = new Intent();
            value.putExtra("DETECT_VALUE", Integer.parseInt(this.title));
            getActivity().setResult(Activity.RESULT_OK, value);
            getActivity().finish();}

        if(v == noButton){
            onDismiss(getDialog());
            getActivity().recreate();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image_dialog, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.image_dialog_imageView);
        TextView textView = (TextView) view.findViewById(R.id.image_dialog_textView);
        yesButton = (Button) view.findViewById(R.id.yes_button);
        noButton = (Button) view.findViewById(R.id.no_button);

        yesButton.setOnClickListener(this);
        noButton.setOnClickListener(this);
        if (bmp != null)
            imageView.setImageBitmap(bmp);

        if(title!=null) {
            //textView.setText(this.title);
            textView.setText(this.title +"\n" + "Is the value correct");

        }
        return view;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if(bmp!=null)
            bmp.recycle();
        bmp = null;
        System.gc();
        super.onDismiss(dialog);
        Log.d("ImageDialog", "OnDismiss");
    }
}