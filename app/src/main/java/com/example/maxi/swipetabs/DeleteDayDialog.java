package com.example.maxi.swipetabs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Maxi on 14/03/2015.
 */
public class DeleteDayDialog extends DialogFragment{

    DataBaseHandler handler;
    Fragment_a fragmentA;
    String date;

    public DeleteDayDialog(Fragment_a fragmentA,String date) {
        this.fragmentA = fragmentA;
        this.date = date;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.delete_day_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setView(view);
        builder.setTitle("Delete Element");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Cancel", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteData(view, date);
            }
        });
        return builder.create();
    }

    public void deleteData(View v, String dateToDelete) {
        handler = new DataBaseHandler(getActivity().getBaseContext());
        handler.open();

        Toast.makeText(getActivity().getBaseContext(), "Deleted: "+dateToDelete, Toast.LENGTH_LONG).show();
        handler.deleteData(dateToDelete);

        fragmentA.refresh();
        handler.close();
    }
}
