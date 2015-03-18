package com.example.maxi.swipetabs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Maxi on 15/03/2015.
 */
public class MyViewDayListAdapter extends ArrayAdapter<String> {
    Context context;
    List<String> morningArray;
    List<String> nightArray;
    List<String> dateArray;

    MyViewDayListAdapter(Context c, List<String> morning, List<String> night, List<String> date) {
        super(c, R.layout.single_row, R.id.txtDayMorning, morning);
        this.context = c;
        this.morningArray = morning;
        this.nightArray = night;
        this.dateArray = date;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.single_row, parent, false);

        TextView txtMorning = (TextView) row.findViewById(R.id.txtDayMorning);
        TextView txtNight = (TextView) row.findViewById(R.id.txtDayNight);
        TextView txtDate = (TextView) row.findViewById(R.id.txtDayDate);

        txtMorning.setText(morningArray.get(position) + " Kg");
        txtNight.setText(nightArray.get(position) + " Kg");
        txtDate.setText(dateArray.get(position));

        return row;
    }
}
