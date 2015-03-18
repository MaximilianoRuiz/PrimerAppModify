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

    TextView textView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.add_week_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        textView = (TextView) view.findViewById(R.id.editWeekDate);
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
            }
        });
        return builder.create();
    }

    private String currentDate(){
        int week, month;
        Calendar calendar =  Calendar.getInstance();
        week = calendar.get(Calendar.WEEK_OF_MONTH);
        month = calendar.get(Calendar.MONTH)+1;
        String currentDate = week+"º week of month "+month;
        return currentDate;
    }

}
