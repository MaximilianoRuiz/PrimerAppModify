package com.example.maxi.swipetabs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHandler {

    public static final String MORNING_WEIGHT= "morning_weight";
    public static final String NIGHT_WEIGHT= "night_weight";
    public static final String DATE= "date";
    public static final String ID= "id";
    public static final String TABLE_NAME= "myTable";
    public static final String DATA_BASE_NAME= "myDataBase";
    public static final int DATABASE_VERSION= 1;
    public static final String DATABASE_CREATE= "create table myTable (id integer primary key autoincrement, morning_weight text, night_weight text, date text);";

    DataBaseHelper baseHelper;
    Context cxt;
    SQLiteDatabase db;

    public DataBaseHandler(Context cxt) {
        this.cxt = cxt;
        baseHelper = new DataBaseHelper(cxt);
    }

    private static class DataBaseHelper extends SQLiteOpenHelper {

        public DataBaseHelper(Context context) {
            super(context, DATA_BASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(DATABASE_CREATE);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXIST "+TABLE_NAME);
            onCreate(db);
        }
    }

    public DataBaseHandler open(){
        this.db = baseHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        baseHelper.close();
    }

    public long insertData(String morning, String night, String date) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MORNING_WEIGHT, morning);
        contentValues.put(NIGHT_WEIGHT, night);
        contentValues.put(DATE, date);
        return db.insert(TABLE_NAME, null, contentValues);
    }

    public Cursor returnData() {
        try {
            return db.query(TABLE_NAME, new String[]{ID, MORNING_WEIGHT, NIGHT_WEIGHT, DATE}, null, null, null, null, ID + " DESC");
        }catch (Exception e){
            Log.d("POTTER",e.getMessage());
        }

        return db.query(TABLE_NAME, new String[]{ID, MORNING_WEIGHT, NIGHT_WEIGHT, DATE}, null, null, null, null, ID + " DESC");
    }

    public int deleteData(String paramName) {
        String where = "myTable.date LIKE ?";
        String[] args = {paramName};
        return db.delete(TABLE_NAME, where, args);
    }

    public int upDate(String morning, String night, String date){
        String where = "myTable.date LIKE ?";

        ContentValues contentValues = new ContentValues();
        contentValues.put(MORNING_WEIGHT, morning);
        contentValues.put(NIGHT_WEIGHT, night);
        String[] whereArgs = {date};
        return db.update(TABLE_NAME, contentValues, where, whereArgs);
    }

    public List<WeightVO> returnWeightVO() {
        String morning, night, date;
        WeightVO weightVO;
        Cursor c = returnData();

        List<WeightVO> weightVOs = new ArrayList<WeightVO>();

        if (c.moveToFirst()){
            do{
                morning = c.getString(1).toString();
                night = c.getString(2).toString();
                date = c.getString(3).toString();
                weightVO = new WeightVO(morning, night, date);
                weightVOs.add(weightVO);
            }while(c.moveToNext());
        }
        return weightVOs;
    }

}
