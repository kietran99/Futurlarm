package com.se68.rraptor.futurlarm;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.se68.rraptor.futurlarm.Class.Constants;
import com.se68.rraptor.futurlarm.Class.FirebaseHelper;
import com.se68.rraptor.futurlarm.Class.FuturlarmList;
import com.se68.rraptor.futurlarm.Class.ImageChooser;
import com.se68.rraptor.futurlarm.Class.User;
import com.se68.rraptor.futurlarm.Class.Utilities;

public class SignUpActivity extends AppCompatActivity {

    private EditText edtEmail, edtUsername, edtPassword, edtVerify;
    private Button btnCreateAccount, btnCancel, btnChooseNewAvatar;
    private ImageView imgNewAvatar;
    private ProgressBar progressBar;
    private LinearLayout layout;

    private Uri uri;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mapping();
        darkModeManager.activateDarkMode();
        btnChooseNewAvatar_click();
        btnCreateAccount_click();
        btnCancel_click();
    }

    @Override
    public void onBackPressed() {
        btnCancel_click();
    }

    public void mapping(){
        layout = findViewById(R.id.LayoutSignUp);
        edtEmail = findViewById(R.id.EditTextEmail);
        edtUsername = findViewById(R.id.EditTextUsername);
        edtPassword = findViewById(R.id.EditTextPassword);
        edtVerify = findViewById(R.id.EditTextVerify);
        btnCreateAccount = findViewById(R.id.ButtonCreateAccount);
        btnCancel = findViewById(R.id.ButtonCancelSignUp);
        btnChooseNewAvatar = findViewById(R.id.ButtonChooseNewAvatar);
        imgNewAvatar = findViewById(R.id.ImageViewNewAvatar);
        progressBar = findViewById(R.id.ProgressBarCreateAccount);
    }

    private Utilities.ProgressBarManager progressBarManager = new Utilities.ProgressBarManager() {
        @Override
        public void inProgress(boolean success) {
            if (success){
                progressBar.setVisibility(View.VISIBLE);
                edtEmail.setEnabled(false);
                edtUsername.setEnabled(false);
                edtPassword.setEnabled(false);
                edtVerify.setEnabled(false);
                btnChooseNewAvatar.setEnabled(false);
                btnCreateAccount.setEnabled(false);
                btnCancel.setEnabled(false);
            }
            else {
                progressBar.setVisibility(View.GONE);
                edtEmail.setEnabled(true);
                edtUsername.setEnabled(true);
                edtPassword.setEnabled(true);
                edtVerify.setEnabled(true);
                btnChooseNewAvatar.setEnabled(true);
                btnCreateAccount.setEnabled(true);
                btnCancel.setEnabled(true);
            }
        }
    };

    private Utilities.DarkModeManager darkModeManager = new Utilities.DarkModeManager() {
        @Override
        public void activateDarkMode() {
            if (Constants.DARK_MODE){
                Utilities.setDMBackground(SignUpActivity.this, layout);
                edtEmail.setHintTextColor(getColor(R.color.DMTextColor));
                edtUsername.setHintTextColor(getColor(R.color.DMTextColor));
                edtPassword.setHintTextColor(getColor(R.color.DMTextColor));
                edtVerify.setHintTextColor(getColor(R.color.DMTextColor));
                edtEmail.setTextColor(getColor(R.color.DMTextColor));
                edtUsername.setTextColor(getColor(R.color.DMTextColor));
                edtPassword.setTextColor(getColor(R.color.DMTextColor));
                edtVerify.setTextColor(getColor(R.color.DMTextColor));
            }
        }
    };

    public void btnChooseNewAvatar_click(){
        btnChooseNewAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageChooser.openFileChooser(SignUpActivity.this);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uri = ImageChooser.loadChoosenImage(requestCode, resultCode, data, imgNewAvatar);
    }

    public void btnCreateAccount_click(){
        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!edtPassword.getText().toString().trim().equals(edtVerify.getText().toString().trim())){
                    edtVerify.setError("different from password.");
                    return;
                }

                //Confirmation dialog
                AlertDialog.Builder dialog = new AlertDialog.Builder(SignUpActivity.this);
                dialog.setTitle("CREATE ACCOUNT?");

                dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        createAccount();
                    }
                });

                dialog.show();


    }

    private void createAccount(){
        //Create user with email and password
        progressBarManager.inProgress(true);
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(edtEmail.getText().toString().trim(), edtPassword.getText().toString().trim())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        User user = new User(
                                edtUsername.getText().toString().trim(),
                                edtEmail.getText().toString().trim()
                        );

                        //Add remaining data to created user's data
                        new FirebaseHelper().createUser(SignUpActivity.this, uri, user,
                                mAuth.getCurrentUser().getUid(), new FirebaseHelper.DataStatus() {
                                    @Override
                                    public void dataLoaded(User userData) {

                                    }

                                    @Override
                                    public void dataLoaded(FuturlarmList futurlarms){

                                    }

                                    @Override
                                    public void dataInserted() {
                                        progressBarManager.inProgress(false);
                                        Toast.makeText(SignUpActivity.this, "Account created successfully.", Toast.LENGTH_SHORT).show();
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                                                finish();
                                            }
                                        }, Constants.SIGN_UP_DELAY);
                                    }

                                    @Override
                                    public void dataUpdated() {

                                    }

                                    @Override
                                    public void dataDeleted() {

                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
        });
    }

    public void btnCancel_click(){
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}
