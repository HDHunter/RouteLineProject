package com.hunty.widget.event;

import java.util.ArrayList;

/**
 * Created by lenovo23 on 2016/9/12.
 */
public class TimeSharingHistoryBean extends BaseBean {

    private String resultCode;
    private String resultMsg;
    private ArrayList<FenShihistoryDatesBean> list;

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

    public ArrayList<FenShihistoryDatesBean> getList() {
        return list;
    }

    public void setList(ArrayList<FenShihistoryDatesBean> list) {
        this.list = list;
    }

    public static class FenShihistoryDatesBean {

        private String code;    //行情代码
        private String name;//  证券简称
        private String close;// 昨日收盘价
        private String open;//  今日开盘价
        private String amount;//成交金额
        private String high;//最高价格
        private String low;//最低价格
        private String price;//最新价格
        private String tradedate;//最新日期
        private String tradetime;//最新时间
        private String vol;//行情成交量
        private String oi;//持仓量
        private String settle;//结算价

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getSettle() {
            return settle;
        }

        public void setSettle(String settle) {
            this.settle = settle;
        }

        public String getOi() {
            return oi;
        }

        public void setOi(String oi) {
            this.oi = oi;
        }

        public String getVol() {
            return vol;
        }

        public void setVol(String vol) {
            this.vol = vol;
        }

        public String getTradetime() {
            return tradetime;
        }

        public void setTradetime(String tradetime) {
            this.tradetime = tradetime;
        }

        public String getTradedate() {
            return tradedate;
        }

        public void setTradedate(String tradedate) {
            this.tradedate = tradedate;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getLow() {
            return low;
        }

        public void setLow(String low) {
            this.low = low;
        }

        public String getHigh() {
            return high;
        }

        public void setHigh(String high) {
            this.high = high;
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

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
