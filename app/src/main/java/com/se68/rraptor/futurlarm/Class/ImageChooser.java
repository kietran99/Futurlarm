package com.se68.rraptor.futurlarm.Class;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;

import com.se68.rraptor.futurlarm.R;
import com.squareup.picasso.Picasso;

import static android.app.Activity.RESULT_OK;

public class ImageChooser {

    public static void openFileChooser(Activity activity){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(intent, Constants.PICK_IMAGE_REQUEST);
    }

    public static String getFileExtension(Context context, Uri uri){
        ContentResolver contentResolver = context.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public static Uri loadChoosenImage(int requestCode, int resultCode, @Nullable Intent data, ImageView imgNewAvatar){
        if (requestCode == Constants.PICK_IMAGE_REQUEST &&
                resultCode == RESULT_OK &&
                data != null && data.getData() != null){
            Picasso.get().load(data.getData()).placeholder(R.drawable.ic_launcher_foreground).into(imgNewAvatar);
            return data.getData();
        }
        return null;
    }

}
