package com.team3.weather.DataTransferObject;

import java.util.List;

public class DataPointsWrapper {
    private List<DataPoint> data;

    public List<DataPoint> getData() {
        return data;
    }

    public void setData(List<DataPoint> data) {
        this.data = data;
    }
}
