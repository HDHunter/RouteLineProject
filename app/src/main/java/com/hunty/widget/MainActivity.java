package com.hunty.widget;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hunty.widget.charts.CandleChartView;
import com.hunty.widget.charts.CandleParamResult;
import com.hunty.widget.charts.ChartDataInitUtils;
import com.hunty.widget.charts.FenshiParamResult;
import com.hunty.widget.charts.RouteLineView;
import com.hunty.widget.event.DayKHistoryEvent;
import com.hunty.widget.event.TimeSharingHistoryEvent;

/**
 * main home page
 *
 * @author Hunter
 * @Date 2019年06月25日15:27:15
 */
public class MainActivity extends AppCompatActivity implements ChartDataInitUtils.onRouteChartOKPostListener, ChartDataInitUtils.onCandleChartOKPostListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RouteLineView routeline = findViewById(R.id.routeline);
        CandleChartView chart = findViewById(R.id.candle);

        ChartDataInitUtils chartDataInitUtils = new ChartDataInitUtils();

        //折线图处理部分
        routeline.setType(RouteLineView.TYPE.SZHENG);
//        routeline.setMaxAndMin();
//        routeline.addData();

        //烛光图部分
        chart.setType(2);

        TimeSharingHistoryEvent routeData = new TimeSharingHistoryEvent(this);
        chartDataInitUtils.initRouteChartData(routeData, this);
        DayKHistoryEvent candleData = new DayKHistoryEvent(this);
        chartDataInitUtils.initCandleChartData(candleData, this);
    }

    @Override
    public void onSuccess(FenshiParamResult result) {

    }

    @Override
    public void onSuccess(CandleParamResult result) {

    }
}
