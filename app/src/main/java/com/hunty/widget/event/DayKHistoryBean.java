package com.hunty.widget.event;

import java.util.ArrayList;

/**
 * Created by lenovo23 on 2016/9/12.
 */
public class DayKHistoryBean extends BaseBean {
    private String resultCode;
    private String resultMsg;
    private ArrayList<DayKDatasBean> list;//数据列表


    public ArrayList<DayKDatasBean> getList() {
        return list;
    }

    public void setList(ArrayList<DayKDatasBean> list) {
        this.list = list;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public static class DayKDatasBean {

        private String code;//行情代码
        private String name;//证券简称
        private String close;//开盘价
        private String open;//收盘价
        private String high;//最高价格
        private String low;//最低价格
        private String date;//日期
        private String time;//时间
        //5日均值，10日均值，20日均值，30日均值
        private String ma5;
        private String ma10;
        private String ma20;
        private String ma30;

        public String getMa5() {
            return ma5;
        }

        public void setMa5(String ma5) {
            this.ma5 = ma5;
        }

        public String getMa10() {
            return ma10;
        }

        public void setMa10(String ma10) {
            this.ma10 = ma10;
        }

        public String getMa20() {
            return ma20;
        }

        public void setMa20(String ma20) {
            this.ma20 = ma20;
        }

        public String getMa30() {
            return ma30;
        }

        public void setMa30(String ma30) {
            this.ma30 = ma30;
        }

        public String getHigh() {
            return high;
        }

        public void setHigh(String high) {
            this.high = high;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getClose() {
            return close;
        }

        public void setClose(String close) {
            this.close = close;
        }

        public String getOpen() {
            return open;
        }

        public void setOpen(String open) {
            this.open = open;
        }

        public String getLow() {
            return low;
        }

        public void setLow(String low) {
            this.low = low;
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
}
