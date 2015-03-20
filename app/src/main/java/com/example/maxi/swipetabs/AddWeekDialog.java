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
public class AddWeekDialog extends DialogFragment{

    public static final String WEEK = "WEEK";
    TextView txtDate;
    EditText etMornint, etNight;
    DataBaseHandler handler;
    Fragment_b fragmentB;

    public AddWeekDialog(Fragment_b fragmentB) {
        this.fragmentB = fragmentB;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.add_week_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        txtDate = (TextView) view.findViewById(R.id.editWeekDate);
        etMornint = (EditText) view.findViewById(R.id.editWeekMorning);
        etNight = (EditText) view.findViewById(R.id.editWeekNight);

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
        int week, month;
        Calendar calendar =  Calendar.getInstance();
        week = calendar.get(Calendar.WEEK_OF_MONTH);
        month = calendar.get(Calendar.MONTH)+1;
        String currentDate = week+"º week - "+month;
        return currentDate;
    }

    public void saveData(View v) {
        String getMorning = etMornint.getText().toString();
        String getNightl = etNight.getText().toString();
        String getDate = txtDate.getText().toString();

        handler = new DataBaseHandler(getActivity().getBaseContext());
        handler.open();
        long id = handler.insertData(getMorning, getNightl, getDate, WEEK);

        Toast.makeText(getActivity(), "Data inserted with ID= "+id, Toast.LENGTH_LONG).show();

        fragmentB.refresh();
        handler.close();
    }
}
