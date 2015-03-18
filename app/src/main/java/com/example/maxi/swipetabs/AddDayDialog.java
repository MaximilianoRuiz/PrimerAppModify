package com.example.maxi.swipetabs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by Maxi on 14/03/2015.
 */
public class AddDayDialog extends DialogFragment{

    TextView textView;
    EditText etMornint, etNight;
    DataBaseHandler handler;
    Fragment_a fragmentA;

    public AddDayDialog(Fragment_a fragmentA) {
        this.fragmentA = fragmentA;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.add_day_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        textView = (TextView) view.findViewById(R.id.editDayDate);
        etMornint = (EditText) view.findViewById(R.id.editDayMorning);
        etNight = (EditText) view.findViewById(R.id.editDayNight);

        textView.setText(currentDate());
        builder.setView(view);
        builder.setTitle("Add New List");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Cancel", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Add", Toast.LENGTH_SHORT).show();
                saveData(view);
            }
        });
        return builder.create();
    }

    private String currentDate(){
        int day, month, year;
        Calendar calendar =  Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH)+1;
        year = calendar.get(Calendar.YEAR);
        String currentDate = day+"/"+month+"/"+year;
        return currentDate;
    }

    public void saveData(View v) {
        String getMorning = etMornint.getText().toString();
        String getNightl = etNight.getText().toString();
        String getDate = textView.getText().toString();

        handler = new DataBaseHandler(getActivity().getBaseContext());
        handler.open();
        long id = handler.insertData(getMorning, getNightl, getDate);

        Toast.makeText(getActivity(), "Data inserted with ID= "+id, Toast.LENGTH_LONG).show();

        fragmentA.refresh();
        handler.close();
    }

}
