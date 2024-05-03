package com.team3.weather.DataTransferObject;

import java.util.List;

public class ApiResponse {
    private List<DataEntry> data;

    public List<DataEntry> getData() {
        return data;
    }

    public void setData(List<DataEntry> data) {
        this.data = data;
    }
    
}
