package com.se68.rraptor.futurlarm.Class;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.se68.rraptor.futurlarm.R;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

public class FuturlarmFilterDialog extends AppCompatDialogFragment {

    private EditText edtDate, edtTime;
    private RadioButton btnImportant;
    private FuturlarmDialogListener listener;

    public interface FuturlarmDialogListener{
        void applyFilter(String date, String time, boolean important);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_filter, null);
        viewBind(view);
        edtDate_click();

        builder.setView(view).setTitle("LET'S FILTER")
                .setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String date = edtDate.getText().toString().trim();
                        String time = edtTime.getText().toString().trim();
                        boolean important = btnImportant.isChecked();

                        if (!time.equals("") && (Integer.valueOf(time) < 0 || Integer.valueOf(time) > 24)){
                            Toast.makeText(getContext(), "Please choose a valid hour", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        listener.applyFilter(date, time, important);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (FuturlarmDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement FuturlarmDialogListener");
        }
    }

    public void viewBind(View view){
        edtDate = view.findViewById(R.id.EditTextFilterDate);
        edtTime = view.findViewById(R.id.EditTextFilterTime);
        btnImportant = view.findViewById(R.id.ButtonFilterImportant);
    }

    public void edtDate_click(){
        edtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDate();
            }
        });
    }

    public void pickDate(){
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                int currentMonth = month+ 1;
                String displayDate = dayOfMonth + "/" + currentMonth + "/" + year;
                edtDate.setText(displayDate);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public void edtTime_click(){
        edtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickTime();
            }
        });
    }

    public void pickTime(){
        final Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(0,0, 1, hourOfDay, minute);
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.US);
                edtTime.setText(formatter.format(calendar));
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), 0, true);
        timePickerDialog.show();
    }
}
