package com.airtennis.dhisat.fostracking.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by naveen on 28/5/16.
 */
public class UICommon {

    public static void ShowDialog(Context context, String title, String message) {

        try {
            if (!((Activity) context).isFinishing()) {
                final AlertDialog.Builder confirm = new AlertDialog.Builder(context);
                confirm.setTitle(title);
                confirm.setMessage(message);
                confirm.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                confirm.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
