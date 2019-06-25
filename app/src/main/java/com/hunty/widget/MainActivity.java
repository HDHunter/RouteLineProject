package com.hunty.widget;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.hunty.widget.charts.CandleChartView;
import com.hunty.widget.charts.CandleParamResult;
import com.hunty.widget.charts.ChartDataInitUtils;
import com.hunty.widget.charts.FenshiParamResult;
import com.hunty.widget.charts.RouteLineView;
import com.hunty.widget.event.DayKHistoryEvent;
import com.hunty.widget.event.TimeSharingHistoryEvent;

import java.text.NumberFormat;

/**
 * main home page
 *
 * @author Hunter
 * @Date 2019年06月25日15:27:15
 */
public class MainActivity extends AppCompatActivity implements ChartDataInitUtils.onRouteChartOKPostListener, ChartDataInitUtils.onCandleChartOKPostListener {

    RouteLineView routeline;
    CandleChartView chartView;
    private NumberFormat nt = NumberFormat.getPercentInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        routeline = findViewById(R.id.routeline);
        chartView = findViewById(R.id.candle);
        nt.setMinimumFractionDigits(2);
        nt.setMaximumFractionDigits(2);

        ChartDataInitUtils chartDataInitUtils = new ChartDataInitUtils();

        //折线图处理部分
        routeline.setType(RouteLineView.TYPE.SZHENG);
//        routeline.setMaxAndMin();
//        routeline.addData();

        //烛光图部分
        chartView.setType(2);

        TimeSharingHistoryEvent routeData = new TimeSharingHistoryEvent(this);
        chartDataInitUtils.initRouteChartData(routeData, this);
        DayKHistoryEvent candleData = new DayKHistoryEvent(this);
        chartDataInitUtils.initCandleChartData(candleData, this);
    }

    @Override
    public void onSuccess(FenshiParamResult data) {
        if (null == data) return;
        String shangIndex = data.getPrice();
        float[][] mRouteData = data.getRouteData();
        resetPercent(data.getClose(), data.getPrice());
        //最后一个显示配置max min。
        if (TextUtils.isEmpty(data.getHigh()) || TextUtils.isEmpty(data.getLow()) || TextUtils.isEmpty(data.getClose()))
            return;
        routeline.setMaxAndMin(data.getHigh(), data.getLow(), data.getClose());
        if (null == mRouteData) return;
        routeline.setData(mRouteData);
        if (TextUtils.isEmpty(data.getDate()) || TextUtils.isEmpty(data.getTime())) {
            return;
        }
//        tv_fenshi_shijian.setText(StringUtil.formatDate(data.getDate()) + " " + StringUtil.formatTime(data.getTime()));
//        tv_fenshi_zhishu.setText(data.getPrice());
        //历史数据确定获取不到，一样可以插入数据。。因为页面需要 price值，soNULL判断失效
//        if (mAddedData != null && !TextUtils.isEmpty(mAddedData.getaMOUNT())) {
            addData();
//        }
    }

    @Override
    public void onSuccess(CandleParamResult result) {

    }


    private void resetPercent(String open, String price) {
        if (TextUtils.isEmpty(open) || TextUtils.isEmpty(price)) {
            return;
        }
        float closep = Float.parseFloat(open);
        float pricef = Float.parseFloat(price);
        if (closep < pricef) {
//            tv_fenshi_percent.setTextColor(Color.parseColor("#F94747"));
//            tv_fenshi_percent.setText("+" + nt.format((pricef - closep) / closep));
        } else {
//            tv_fenshi_percent.setTextColor(Color.parseColor("#51C678"));
//            tv_fenshi_percent.setText("-" + nt.format((closep - pricef) / closep));
        }
    }

    /**
     * mqtt,或者短连接接收过来数据。及时刷新视图。
     */
    public void onEventMainThread(String base) {
//        mAddedData = JSON.parseObject(base.getMsgbody(), MQTimeModel.class);
//        if (mRouteData != null) {
            addData();
//        }
    }

    private void addData() {
        //添加数据
//        if (TextUtils.isEmpty(mAddedData.getpRICE()) || TextUtils.isEmpty(mAddedData.gettRADETIME()) || TextUtils.isEmpty(mAddedData.getdBFDATE()))
//            return;
//        if (TextUtils.isEmpty(mAddedData.gettRADETIME().substring(0, 2)) || TextUtils.isEmpty(mAddedData.getdBFDATE().substring(0, 2)))
//            return;
//        mRoutLine.setMaxAndMin(mAddedData.gethIGH(), mAddedData.getlOW(), mAddedData.getcLOSE());
//        mRoutLine.addData(mAddedData.getpRICE(), mAddedData.gettRADETIME().substring(0, 2), mAddedData.gettRADETIME().substring(2, 4), mAddedData.getdBFDATE().substring(4, 6), mAddedData.getdBFDATE().substring(6, 8));
        //设置文本
//        tv_fenshi_shijian.setText(StringUtil.formatDate(mAddedData.gettRADEDATE()) + " " + StringUtil.formatTime(mAddedData.gettRADETIME()));
//        tv_fenshi_zhishu.setText(mAddedData.getpRICE());
//        resetPercent(mAddedData.getcLOSE(), mAddedData.getpRICE());
    }
}
