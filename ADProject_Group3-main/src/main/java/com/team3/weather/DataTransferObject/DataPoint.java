package com.team3.weather.DataTransferObject;

import com.alibaba.fastjson.JSONObject;

public class DataPoint {
    private String ds;
    private double y;

    // Default constructor required by Jackson
    public DataPoint() {
    }

    public DataPoint(String ds, double y) {
        this.ds = ds;
        this.y = y;
    }

    public String getDs() {
        return ds;
    }

    public double getY() {
        return y;
    }

    public JSONObject toJsonObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ds", ds);
        jsonObject.put("y", y);
        return jsonObject;
    }
}

