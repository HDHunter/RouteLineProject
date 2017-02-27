package com.hunty.widget.charts;

/**
 * fenshi valid data throw ui thread
 * <p/>
 * Created by zhangjianqiu on 16-8-18.
 */
public class FenshiParamResult {

    private String high;//nearly high
    private String low;////nearly low
    private String close;//nearly close
    private String price;//nearly price
    private String date;//nearly date
    private String time;//nearly time
    private float[][] routeData;

    public float[][] getRouteData() {
        return routeData;
    }

    public void setRouteData(float[][] routeData) {
        this.routeData = routeData;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
