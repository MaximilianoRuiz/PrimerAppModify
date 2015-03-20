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
import android.widget.Toast;

/**
 * Created by Maxi on 14/03/2015.
 */
public class ModifyWeekDialog extends DialogFragment{

    public static final String WEEK = "WEEK";
    EditText etMorning, etNight;
    DataBaseHandler handler;
    Fragment_b fragmentB;
    String morning, night, date;

    public ModifyWeekDialog(Fragment_b fragmentB, String morning, String night, String date) {
        this.fragmentB = fragmentB;
        this.morning = morning;
        this.night = night;
        this.date = date;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.modify_day_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        etMorning = (EditText) view.findViewById(R.id.editMDayMorning);
        etNight = (EditText) view.findViewById(R.id.editMDayNight);

        etMorning.setText(morning);
        etNight.setText(night);


        builder.setView(view);
        builder.setTitle("Update Element");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Cancel", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setPositiveButton("UpDate", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Update", Toast.LENGTH_SHORT).show();

                upData(view);
            }
        });
        return builder.create();
    }

    public void upData(View v) {
        morning = etMorning.getText().toString();
        night = etNight.getText().toString();

        handler = new DataBaseHandler(getActivity().getBaseContext());
        handler.open();

        long id = handler.upDate(morning, night, date, WEEK);

        Toast.makeText(getActivity(), id+": "+morning+"  "+night+"   "+date, Toast.LENGTH_LONG).show();

        fragmentB.refresh();
        handler.close();
    }

}
