package com.hunty.widget;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hunty.widget.charts.RouteLine;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RouteLine routeline = (RouteLine) findViewById(R.id.routeline);
    }
}
