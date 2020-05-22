package com.se68.rraptor.futurlarm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.se68.rraptor.futurlarm.Class.Constants;
import com.se68.rraptor.futurlarm.Class.FunctionSwitcher;
import com.se68.rraptor.futurlarm.Class.Utilities;
import com.se68.rraptor.futurlarm.FuturlarmList.FuturlarmManagerActivity;
import com.se68.rraptor.futurlarm.Templates.FunctionSwitchFragment;

public class WelcomeActivity extends AppCompatActivity {

    private TextView txtWelcomeUser, txtWelcomeBack;
    private RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        FunctionSwitcher.setCurrent(0);
        mapping();
        darkModeManager.activateDarkMode();
        loadUsername();
        delayScreen();
    }

    @Override
    public void onBackPressed() { }

    public void mapping(){
        txtWelcomeBack = findViewById(R.id.welcomeLabel);
        txtWelcomeUser = findViewById(R.id.TextViewWelcomeUser);
        layout = findViewById(R.id.LayoutWelcome);
    }

    public void loadUsername(){
        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        txtWelcomeUser = findViewById(R.id.TextViewWelcomeUser);
        txtWelcomeUser.setText(sharedPreferences.getString("username", ""));
    }

    public void delayScreen(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(WelcomeActivity.this, FuturlarmManagerActivity.class);
                startActivity(intent);
                finish();
            }
        }, Constants.WELCOME_DELAY);
    }

    private Utilities.DarkModeManager darkModeManager = new Utilities.DarkModeManager() {
        @Override
        public void activateDarkMode() {
            if (Constants.DARK_MODE){
                Utilities.setDMText(WelcomeActivity.this, txtWelcomeBack);
                Utilities.setDMText(WelcomeActivity.this, txtWelcomeUser);
                Utilities.setDMBackground(WelcomeActivity.this, layout);
            }
        }
    };

}
