package com.se68.rraptor.futurlarm.Class;

import android.content.Context;
import android.text.Layout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.se68.rraptor.futurlarm.R;
import com.squareup.picasso.Picasso;


public class Utilities {

    public interface ProgressBarManager{
        void inProgress(boolean success);
    }

    public interface DarkModeManager{
        void activateDarkMode();
    }

    public static void setDMText(Context context, TextView textView){
        textView.setTextColor(context.getColor(R.color.DMTextColor));
    }

    public static void setDMBackground(Context context, RelativeLayout layout){
        layout.setBackgroundColor(context.getColor(R.color.colorPrimaryDark));
    }

    public static void setDMBackground(Context context, LinearLayout layout){
        layout.setBackgroundColor(context.getColor(R.color.colorPrimaryDark));
    }

    public static void loadAvatar(ImageView imageView, String avatar){
        Picasso.get().load(avatar).placeholder(R.drawable.ic_launcher_foreground).fit().into(imageView);
    }

    public static String typeToString(FuturlarmType type){
        switch (type){
            case BIRTHDAY:
                return "BIRTHDAY";
            case DRINK:
                return "DRINK";
            case HANGOUT:
                return "HANGOUT";
            case SPORT:
                return "SPORT";
            case PAYMENT:
                return "PAYMENT";
            case MISC:
                return "MISC";
                default:
                    return "OTHERS";
        }
    }
}
