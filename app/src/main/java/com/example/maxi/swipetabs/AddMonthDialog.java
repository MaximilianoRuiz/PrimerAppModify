package com.example.maxi.swipetabs;

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
public class AddMonthDialog extends DialogFragment{

    public static final String MONTH = "MONTH";
    TextView txtDate;
    EditText etMornint, etNight;
    DataBaseHandler handler;
    Fragment_c fragmentC;

    public AddMonthDialog(Fragment_c fragmentC) {
        this.fragmentC = fragmentC;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.add_month_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        txtDate = (TextView) view.findViewById(R.id.editMonthDate);
        etMornint = (EditText) view.findViewById(R.id.editMonthMorning);
        etNight = (EditText) view.findViewById(R.id.editMonthNight);

        txtDate.setText(currentDate());
        builder.setView(view);
        builder.setTitle("Add New Element");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Cancel", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveData(view);
            }
        });
        return builder.create();
    }

    static String currentDate(){
        int month;
        Calendar calendar =  Calendar.getInstance();
        month = calendar.get(Calendar.MONTH)+1;
        String currentDate = obtainMonth(month);
        return currentDate;
    }

    public void saveData(View v) {
        String getMorning = etMornint.getText().toString();
        String getNightl = etNight.getText().toString();
        String getDate = txtDate.getText().toString();

        handler = new DataBaseHandler(getActivity().getBaseContext());
        handler.open();
        long id = handler.insertData(getMorning, getNightl, getDate, MONTH);

        Toast.makeText(getActivity(), "Data inserted with ID= "+id, Toast.LENGTH_LONG).show();

        fragmentC.refresh();
        handler.close();
    }

    private static String obtainMonth(int m) {
        switch (m) {
            case 1:
                return "JAN";
            case 2:
                return "FEB";
            case 3:
                return "MAR";
            case 4:
                return "APR";
            case 5:
                return "MAY";
            case 6:
                return "JUN";
            case 7:
                return "JUL";
            case 8:
                return "AUG";
            case 9:
                return "SEP";
            case 10:
                return "OCT";
            case 11:
                return "NOV";
            case 12:
                return "DEC";
        }
        return null;
    }

}
