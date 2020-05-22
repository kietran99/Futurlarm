package com.se68.rraptor.futurlarm.Class;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class ConfirmationDialog {

    public interface Confirmation{
        void confirmed();
    }

    public static void create(Context context, final Confirmation confirmation){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("APPLY CHANGES?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        confirmation.confirmed();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();
    }
}
