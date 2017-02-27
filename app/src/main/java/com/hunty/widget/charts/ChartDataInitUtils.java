package com.hunty.widget.charts;

import android.os.AsyncTask;
import android.text.TextUtils;


import com.hunty.widget.event.DayKHistoryBean;
import com.hunty.widget.event.DayKHistoryEvent;
import com.hunty.widget.event.TimeSharingHistoryBean;
import com.hunty.widget.event.TimeSharingHistoryEvent;

import java.util.List;

/**
 * ChartData init Task.
 * include RouteLine And Candle.
 * Created by zhangjianqiu on 16-8-18.
 */
public class ChartDataInitUtils {

    private onRouteChartOKPostListener routelistner;
    private onCandleChartOKPostListener candlelistner;
    /**
     * RouteLine Task run.
     */
    private AsyncTask<TimeSharingHistoryEvent, Void, FenshiParamResult> routeasyncTask = new AsyncTask<TimeSharingHistoryEvent, Void, FenshiParamResult>() {
        /**
         * 初始化折线图数据
         */
        @Override
        protected FenshiParamResult doInBackground(TimeSharingHistoryEvent... base) {
            FenshiParamResult valid = new FenshiParamResult();
            float[][] mRouteData;
//            TimeSharingHistoryModel data = (TimeSharingHistoryModel) AbJsonUtil.fromJson((String) base[0].getObject(), TimeSharingHistoryModel.class);
            TimeSharingHistoryBean data = (TimeSharingHistoryBean) base[0].getObject();
            base = null;//event
            if (data != null && data.getList().size() > 1) {
                //  直接取值，就不需要 初始化一个model了。
                List<TimeSharingHistoryBean.FenShihistoryDatesBean> daydata = data.getList();
                valid.setHigh(daydata.get(daydata.size() - 1).getHigh());
                valid.setLow(daydata.get(daydata.size() - 1).getLow());
                valid.setClose(daydata.get(daydata.size() - 1).getClose());
                valid.setPrice(daydata.get(daydata.size() - 1).getPrice());
                valid.setDate(daydata.get(daydata.size() - 1).getTradedate());
                valid.setTime(daydata.get(daydata.size() - 1).getTradetime());
                mRouteData = new float[5][daydata.size()];
                for (int i = 0; i < daydata.size(); i++) {
                    mRouteData[0][i] = Float.parseFloat(daydata.get(i).getPrice());//价格
                    mRouteData[1][i] = Float.parseFloat(daydata.get(i).getTradetime().substring(0, 2));//小时
                    mRouteData[2][i] = Float.parseFloat(daydata.get(i).getTradetime().substring(2, 4));//分钟
                    mRouteData[3][i] = Float.parseFloat(daydata.get(i).getTradedate().substring(4, 6));//month
                    mRouteData[4][i] = Float.parseFloat(daydata.get(i).getTradedate().substring(6, 8));//day
                }
                data = null;//json
            } else {
                LogUtil.e("error", "分时数据解析失败");
                mRouteData = new float[5][1];
            }
            valid.setRouteData(mRouteData);
            return valid;
        }

        @Override
        protected void onPostExecute(FenshiParamResult data) {
            if (routelistner != null) {
                routelistner.onSuccess(data);
            }
        }
    };

    /**
     * RouteLine Task run.
     */
    private AsyncTask<DayKHistoryEvent, Void, CandleParamResult> candleasycTask = new AsyncTask<DayKHistoryEvent, Void, CandleParamResult>() {
        /**
         * 初始化烛光图数据，日K
         */
        @Override
        protected CandleParamResult doInBackground(DayKHistoryEvent... base) {
            CandleParamResult candleResult = null;
//            DayKInformationModel date = (DayKInformationModel) AbJsonUtil.fromJson((String) base[0].getObject(), DayKInformationModel.class);
            DayKHistoryBean date = (DayKHistoryBean) base[0].getObject();
            base = null;
            if (date != null && date.getList().size() > 0) {
                candleResult = new CandleParamResult();
                float[][] mCandleData = new float[8][30];
                String[] mDate = new String[30];
                float max = 0, min = 100000f;
                List<DayKHistoryBean.DayKDatasBean> item = date.getList();
                int maxcount = item.size();
                int initcount = maxcount - 30;
                for (int j = initcount; j < maxcount; j++) {
                    float high = Float.parseFloat(item.get(j).getHigh());
                    float low = Float.parseFloat(item.get(j).getLow());
                    if (high > max) {
                        max = high;
                    }
                    if (low < min) {
                        min = low;
                    }
                    mCandleData[0][j - initcount] = high;
                    mCandleData[1][j - initcount] = Float.parseFloat(item.get(j).getOpen());
                    mCandleData[2][j - initcount] = Float.parseFloat(item.get(j).getClose());
                    mCandleData[3][j - initcount] = low;
                    mCandleData[4][j - initcount] = Float.parseFloat(item.get(j).getDate());//不在用作显示
                    mDate[j - initcount] = item.get(j).getDate();
                    //位空的时候，默认就是 0 。
                    if (!TextUtils.isEmpty(item.get(j).getMa5())) {
                        mCandleData[5][j - initcount] = Float.parseFloat(item.get(j).getMa5());
                    }
                    if (!TextUtils.isEmpty(item.get(j).getMa5())) {
                        mCandleData[6][j - initcount] = Float.parseFloat(item.get(j).getMa10());
                    }
                    if (!TextUtils.isEmpty(item.get(j).getMa5())) {
                        mCandleData[7][j - initcount] = Float.parseFloat(item.get(j).getMa20());
                    }
                }
                candleResult.setmDates(mDate);
                candleResult.setmData(mCandleData);
                candleResult.setMax(max);
                candleResult.setMin(min);
                candleResult.setFirstCandleDate(item.get(initcount).getDate());
                candleResult.setLastcandleDate(item.get(item.size() - 1).getDate());
            } else {
                LogUtil.ee("error", "日K数据解析失败");
            }
            return candleResult;
        }

        @Override
        protected void onPostExecute(CandleParamResult data) {
            if (data == null) return;
            if (candlelistner != null) {
                candlelistner.onSuccess(data);
            }
        }
    };

    public void initRouteChartData(TimeSharingHistoryEvent event, onRouteChartOKPostListener listner) {
        routelistner = listner;
        if (routeasyncTask.getStatus() == AsyncTask.Status.RUNNING) {
            return;
        }
        routeasyncTask.execute(event);
    }

    public void initCandleChartData(DayKHistoryEvent event, onCandleChartOKPostListener listner) {
        candlelistner = listner;
        if (candleasycTask.getStatus() == AsyncTask.Status.RUNNING) {
            return;
        }
        candleasycTask.execute(event);
    }

    /**
     * Cancel All Task.
     */
    public void cancelAllTaskAndClean() {
        routeasyncTask.cancel(true);
        candleasycTask.cancel(true);
        routeasyncTask=null;
        candleasycTask=null;
    }

    public interface onRouteChartOKPostListener {
        void onSuccess(FenshiParamResult result);
    }

    public interface onCandleChartOKPostListener {
        void onSuccess(CandleParamResult result);
    }

}
