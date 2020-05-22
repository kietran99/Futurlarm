package com.se68.rraptor.futurlarm.FuturlarmList;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.se68.rraptor.futurlarm.Adapter.FuturlarmAdapter;
import com.se68.rraptor.futurlarm.Class.ConfirmationDialog;
import com.se68.rraptor.futurlarm.Class.Futurlarm;
import com.se68.rraptor.futurlarm.R;

public class FuturlarmDetailActivity extends AppCompatActivity {

    private EditText edtName, edtDate, edtTime;
    private TextView txtSender;
    private Spinner spinner;
    private RadioButton btnImportant;
    private Button btnApply, btnCancel;

    private Futurlarm futurlarm;
    public static String FUTURLARM_RESULT = "FUTURLARM_RESULT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_futurlarm_detail);
        futurlarm = (Futurlarm) getIntent().getSerializableExtra(FuturlarmManagerActivity.FUTURLARM);
        bindView();
        loadData();
        setBtnApply_click();
        setBtnCancel_click();
    }

    public void bindView(){
        edtName = findViewById(R.id.EditTextDetailName);
        txtSender = findViewById(R.id.TextViewDetailSender);
        edtDate = findViewById(R.id.EditTextDetailDate);
        edtTime = findViewById(R.id.EditTextDetailTime);
        spinner = findViewById(R.id.SpinnerType);
        btnImportant = findViewById(R.id.ButtonDetailImportant);
        btnApply = findViewById(R.id.ButtonFuturlarmDetailApply);
        btnCancel = findViewById(R.id.ButtonFuturlarmDetailCancel);
    }

    public void loadData(){
        edtName.setText(futurlarm.getName());
        txtSender.setText(futurlarm.getSender());
        edtDate.setText(futurlarm.getDate());
        edtTime.setText(futurlarm.getTime());
        btnImportant.setChecked(futurlarm.isImportant());
    }

    public void setBtnApply_click(){
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmationDialog.create(FuturlarmDetailActivity.this, new ConfirmationDialog.Confirmation() {
                    @Override
                    public void confirmed() {
                        Intent returnIntent = new Intent(FuturlarmDetailActivity.this, FuturlarmManagerActivity.class);
                        returnIntent.putExtra(FUTURLARM_RESULT, futurlarm);
                        setResult(RESULT_OK, returnIntent);
                        finish();
                    }
                });
            }
        });
    }

    public void setBtnCancel_click(){
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(FuturlarmDetailActivity.this, FuturlarmManagerActivity.class));
            }
        });
    }
}
