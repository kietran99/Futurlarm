package com.se68.rraptor.futurlarm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.se68.rraptor.futurlarm.Class.Constants;
import com.se68.rraptor.futurlarm.Class.FirebaseHelper;
import com.se68.rraptor.futurlarm.Class.FuturlarmList;
import com.se68.rraptor.futurlarm.Class.User;
import com.se68.rraptor.futurlarm.Class.Utilities;

public class MainActivity extends AppCompatActivity {

    private Button btnForgetPassword, btnLogin, btnSignUp;
    private EditText edtEmail, edtPassword;
    private CheckBox cbRememberMe;
    private Switch DARK_MODESwitch;
    private ImageView imgAvatar;
    private LinearLayout layout;
    private ProgressBar progressBar;

    private SharedPreferences sharedPreferences;
    private User user;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapping();
        mAuth = FirebaseAuth.getInstance();
        clearFields();
        loadRememberMe();
        removeRememberMe();
        btnLogin_click();
        btnForgotPassword_click();
        activateDarkMode();
        cbRememberMe_CheckChanged();
        btnSignUp_click();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        returnWaitingImages();
        removeRememberMe();
        loadRememberMe();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void mapping(){
        btnLogin = findViewById(R.id.ButtonLogin);
        btnForgetPassword = findViewById(R.id.ButtonForgotPassword);
        btnSignUp = findViewById(R.id.ButtonSignUp);
        edtEmail = findViewById(R.id.EditTextEmail);
        edtPassword = findViewById(R.id.EditTextPassword);
        cbRememberMe = findViewById(R.id.CheckBoxRememberMe);
        DARK_MODESwitch = findViewById(R.id.SwitchDarkMode);
        imgAvatar = findViewById(R.id.ImageViewAvatar);
        layout = findViewById(R.id.LayoutMain);
        progressBar = findViewById(R.id.ProgressBarLogin);
    }

    public void btnForgotPassword_click() {
        btnForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnForgetPassword.setTextColor(Color.CYAN);
                btnForgetPassword.setTextColor(Color.parseColor("#025C77"));

                if (edtEmail.getText().toString().equals("")){
                    edtEmail.setError("REQUIRED");
                    return;
                }

                mAuth.sendPasswordResetEmail(edtEmail.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "Reset-password-email sent.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private Utilities.ProgressBarManager progressBarManager = new Utilities.ProgressBarManager() {
        @Override
        public void inProgress(boolean success) {
            if (success){
                progressBar.setVisibility(View.VISIBLE);
                btnLogin.setEnabled(false);
                btnSignUp.setEnabled(false);
                btnForgetPassword.setEnabled(false);
            } else {
                progressBar.setVisibility(View.GONE);
                btnLogin.setEnabled(true);
                btnSignUp.setEnabled(true);
                btnForgetPassword.setEnabled(true);
            }
        }
    };

    public void activateDarkMode(){
        DARK_MODESwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    setDMColor(true);
                } else {
                    setDMColor(false);
                }
            }
        });
    }

    public void cbRememberMe_CheckChanged(){
        cbRememberMe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    sharedPreferences.edit().putBoolean("checked", true).apply();
                else sharedPreferences.edit().putBoolean("checked", false).apply();
            }
        });
    }

    public void setDMColor(boolean dm){
        if (dm) {
            Constants.DARK_MODE = true;
            layout.setBackgroundResource(R.color.colorPrimaryDark);
            btnForgetPassword.setTextColor(getColor(R.color.DMTextColor));
            DARK_MODESwitch.setTextColor(getColor(R.color.DMTextColor));
            edtEmail.setTextColor(getColor(R.color.DMTextColor));
            edtPassword.setTextColor(getColor(R.color.DMTextColor));
            edtEmail.setHintTextColor(getColor(R.color.DMTextColor));
            edtPassword.setHintTextColor(getColor(R.color.DMTextColor));
            cbRememberMe.setTextColor(getColor(R.color.DMTextColor));
            sharedPreferences.edit().putBoolean("DARK_MODE", true).apply();
        } else {
            Constants.DARK_MODE = false;
            layout.setBackgroundResource(R.color.backgroundColor);
            btnForgetPassword.setTextColor(getColor(R.color.textColor));
            DARK_MODESwitch.setTextColor(getColor(R.color.textColor));
            edtEmail.setTextColor(getColor(R.color.textColor));
            edtPassword.setTextColor(getColor(R.color.textColor));
            edtEmail.setHintTextColor(getColor(R.color.textColor));
            edtPassword.setHintTextColor(getColor(R.color.textColor));
            cbRememberMe.setTextColor(getColor(R.color.textColor));
            sharedPreferences.edit().putBoolean("DARK_MODE", false).apply();
        }
    }

    public void clearFields(){
        edtEmail.setText("");
        edtPassword.setText("");
    }

    public void saveRememberMe(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (user != null){
            editor.putString("username", user.getUsername());
            editor.putString("password", edtPassword.getText().toString().trim());
            editor.putString("email", user.getEmail());
            editor.putString("avatar", user.getAvatar());
        }
        editor.apply();
    }

    public void loadRememberMe(){
        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        cbRememberMe.setChecked(sharedPreferences.getBoolean("checked", false));
        DARK_MODESwitch.setChecked(sharedPreferences.getBoolean("DARK_MODE", false));

        if (DARK_MODESwitch.isChecked()) setDMColor(true);

        if (cbRememberMe.isChecked()) {
            edtEmail.setText(sharedPreferences.getString("email", ""));
            edtPassword.setText(sharedPreferences.getString("password", ""));
            String avatar = sharedPreferences.getString("avatar", "");
            if (!avatar.equals(""))
                Utilities.loadAvatar(imgAvatar, avatar);
        } else {
            clearFields();
        }
    }

    public void removeRememberMe(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putBoolean("DARK_MODE", DARK_MODESwitch.isChecked());
        editor.putBoolean("checked", cbRememberMe.isChecked());
        editor.apply();
    }

    public void btnLogin_click() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmpty()) return;
                progressBarManager.inProgress(true);
                mAuth.signInWithEmailAndPassword(edtEmail.getText().toString().trim(), edtPassword.getText().toString().trim())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                final FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                Toast.makeText(MainActivity.this, "Signed in as " + firebaseUser.getEmail(), Toast.LENGTH_SHORT).show();

                                new FirebaseHelper().getUserData(edtEmail.getText().toString().trim(), new FirebaseHelper.DataStatus() {
                                    @Override
                                    public void dataLoaded(User userData) {
                                        progressBarManager.inProgress(false);
                                        user = new User();
                                        user = userData;
                                        welcomeScreen(true);
                                    }

                                    @Override
                                    public void dataLoaded(FuturlarmList futurlarms){

                                    }

                                    @Override
                                    public void dataInserted() {

                                    }

                                    @Override
                                    public void dataUpdated() {

                                    }

                                    @Override
                                    public void dataDeleted() {

                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBarManager.inProgress(false);
                        Toast.makeText(MainActivity.this, "Sign in failed.", Toast.LENGTH_SHORT).show();
                        welcomeScreen(false);
                    }
                });
            }
        });
    }

    public boolean isEmpty(){
        if (edtEmail.getText().toString().trim().equals("")){
            edtEmail.setError("REQUIRED");
            return true;
        }

        if (edtPassword.getText().toString().trim().equals("")){
            edtPassword.setError("REQUIRED");
            return true;
        }

        return false;
    }

    public void welcomeScreen(boolean success){
        if (!success){
            //Create fail-login images
            edtEmail.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,
                    getDrawable(R.drawable.invalid_user), null);
            edtPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                    getDrawable(R.drawable.incorrect_password), null);

            //Return waiting images
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    returnWaitingImages();
                }
            }, Constants.INPUT_DELAY);

            return;
        }

        Utilities.loadAvatar(imgAvatar, user.getAvatar());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Create success-login images
                edtEmail.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                        getDrawable(R.drawable.valid_user), null);
                edtPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                        getDrawable(R.drawable.correct_password), null);

                saveRememberMe();

                Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                startActivity(intent);
                finish();
            }
            }, Constants.INPUT_DELAY * 2);
    }

    public void returnWaitingImages(){
        edtEmail.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,
                getDrawable(R.drawable.user_icon), null);
        edtPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                getDrawable(R.drawable.password_icon), null);
    }

    public void btnSignUp_click(){
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SignUpActivity.class));
                finish();
            }
        });
    }

}
