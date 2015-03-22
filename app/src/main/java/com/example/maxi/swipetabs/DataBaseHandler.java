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

    public static final String ID= "id";
    public static final String MORNING_WEIGHT= "morning_weight";
    public static final String NIGHT_WEIGHT= "night_weight";
    public static final String DATE= "date";

    public static final String DATA_BASE_NAME= "myDataBase";

    public static final String TABLE_DAY = "myTable";
    public static final String TABLE_WEEK = "myTableWeek";
    public static final String TABLE_MONTH = "myTableMonth";

    public static final int DATABASE_VERSION= 1;

    public static final String DATABASE_CREATE_TABLE_DAY = "create table myTable (id integer primary key autoincrement, morning_weight text, night_weight text, date text);";
    public static final String DATABASE_CREATE_TABLE_WEEK = "create table myTableWeek (id integer primary key autoincrement, morning_weight text, night_weight text, date text);";
    public static final String DATABASE_CREATE_TABLE_MONTH = "create table myTableMonth (id integer primary key autoincrement, morning_weight text, night_weight text, date text);";

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
                db.execSQL(DATABASE_CREATE_TABLE_DAY);
                db.execSQL(DATABASE_CREATE_TABLE_WEEK);
                db.execSQL(DATABASE_CREATE_TABLE_MONTH);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXIST "+ TABLE_DAY);
            onCreate(db);

            db.execSQL("DROP TABLE IF EXIST "+ TABLE_WEEK);
            onCreate(db);

            db.execSQL("DROP TABLE IF EXIST "+ TABLE_MONTH);
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

    public long insertData(String morning, String night, String date, String tabSelected) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(MORNING_WEIGHT, morning);
        contentValues.put(NIGHT_WEIGHT, night);
        contentValues.put(DATE, date);

        switch (tabSelected){
            case "DAY":
                return db.insert(TABLE_DAY, null, contentValues);
            case "WEEK":
                return db.insert(TABLE_WEEK, null, contentValues);
            case "MONTH":
                return db.insert(TABLE_MONTH, null, contentValues);
            default:
                return 0;
        }
    }

    public Cursor returnData(String tabSelected) {
        try {
            switch (tabSelected){
                case "DAY":
                    return db.query(TABLE_DAY, new String[]{ID, MORNING_WEIGHT, NIGHT_WEIGHT, DATE}, null, null, null, null, ID + " DESC");
                case "WEEK":
                    return db.query(TABLE_WEEK, new String[]{ID, MORNING_WEIGHT, NIGHT_WEIGHT, DATE}, null, null, null, null, ID + " DESC");
                case "MONTH":
                    return db.query(TABLE_MONTH, new String[]{ID, MORNING_WEIGHT, NIGHT_WEIGHT, DATE}, null, null, null, null, ID + " DESC");
                default:
                    return null;
            }
        }catch (Exception e){
            Log.d("POTTER",e.getMessage());
        }
        return null;
    }

    public int deleteData(String paramName, String tabSelected) {
        String where;
        String[] args = {paramName};
        switch (tabSelected){
            case "DAY":
                where = "myTable.date LIKE ?";
                return db.delete(TABLE_DAY, where, args);
            case "WEEK":
                where = "myTableWeek.date LIKE ?";
                return db.delete(TABLE_WEEK, where, args);
            case "MONTH":
                where = "myTableMonth.date LIKE ?";
                return db.delete(TABLE_MONTH, where, args);
            default:
                return 0;
        }

    }

    public int upDate(String morning, String night, String date, String tabSelected){
        ContentValues contentValues = new ContentValues();
        contentValues.put(MORNING_WEIGHT, morning);
        contentValues.put(NIGHT_WEIGHT, night);
        String[] whereArgs = {date};
        String where;

        switch (tabSelected){
            case "DAY":
                where = "myTable.date LIKE ?";
                return db.update(TABLE_DAY, contentValues, where, whereArgs);
            case "WEEK":
                where = "myTableWeek.date LIKE ?";
                return db.update(TABLE_WEEK, contentValues, where, whereArgs);
            case "MONTH":
                where = "myTableMonth.date LIKE ?";
                return db.update(TABLE_MONTH, contentValues, where, whereArgs);
            default:
                return 0;
        }
    }

    public List<WeightVO> returnWeightVO(String tabSelected) {
        String morning, night, date;
        WeightVO weightVO;
        Cursor c = returnData(tabSelected);

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
