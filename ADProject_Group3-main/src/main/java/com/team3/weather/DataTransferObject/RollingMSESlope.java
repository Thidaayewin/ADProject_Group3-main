package com.team3.weather.DataTransferObject;

public class RollingMSESlope {
    private String type;
    private int window;
    private int modelNum;
    private String station;
    private String modelType;
    private double rollingMSEslope;

    public RollingMSESlope(){
      
    }
    

    public RollingMSESlope(String type, int window, double rollingMSEslope){
        this.type=type;
        this.window=window;
        this.rollingMSEslope=rollingMSEslope;
    }
}
