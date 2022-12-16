package com.meatchop.tmcpartner;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.meatchop.tmcpartner.R;

import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;

public class AlertDialogClass {
    String message ;





    public static void showDialog(Activity activity, String msg,int zero){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                final Dialog dialog = new Dialog(activity);

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.alert_dialog);

                TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
                text.setText(msg);

                Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

    }



    public static void showDialog(Activity activity, int msg){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                final Dialog dialog = new Dialog(activity);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.alert_dialog);

                TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
                text.setText(msg);

                Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
    }








    public static void showAlert(final Context activity, String message)
    {


        AlertDialog.Builder alertDialog=new AlertDialog.Builder(activity);


        alertDialog.setTitle(R.string.app_name);
        alertDialog.setMessage(message);

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {


            }
        });


        alertDialog.show();


    }
}
