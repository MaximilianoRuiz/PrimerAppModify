package com.example.maxi.swipetabs;

/**
 * Created by Maxi on 15/03/2015.
 */
public class WeightVO {
    private String morning_weight;
    private String night_weight;
    private String date;

    public WeightVO(String morning, String night, String date) {
        this.morning_weight = morning;
        this.night_weight = night;
        this.date = date;
    }

    public String getMorning_weight() {
        return morning_weight;
    }

    public void setMorning_weight(String morning_weight) {
        this.morning_weight = morning_weight;
    }

    public String getNight_weight() {
        return night_weight;
    }

    public void setNight_weight(String night_weight) {
        this.night_weight = night_weight;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}


