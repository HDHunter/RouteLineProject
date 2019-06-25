package com.hunty.widget.charts;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.hunty.widget.R;
import com.hunty.widget.util.ViewUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;


/**
 * zhangjianqiu 2016-5-18
 * <p/>
 * perfect in 5-19<br/>
 * perfect fit in 5-26<br/>
 * ok in 5-27
 * <p/>
 * setType(TYPE);没有则按照上证的X.
 * //setXData(),x轴文本 。没有则按照默认的类型
 * setMaxAndMin();先
 * setData(float[][] data);绘制折线图。。 一维是任意随机数 二维是从0开始的序号
 * 加入图片缓存，减少手指触摸的计算量
 * Activity_onPause的时候，执行 recycle();回收静态Bitmap
 *
 * @author zhangJianQiu
 */
public class RouteLineView extends View {

    public enum TYPE {
        GOLD, SZHENG, EUROPE
    }

    /**
     * 每个数据的x轴长度，表示每一分钟的一个点
     **/
    private float eachUnitPixcels;
    private boolean iswilloverwrite = false;

    /**
     * 绘制的类型
     */
    private TYPE type = TYPE.SZHENG;

    // 纵横坐标的x轴y轴数据
    /**
     * 所有点的 五维数组 ,0维是值 value,1维-小时数，2维-分钟数,3维-比例小数,4维月份，5维日。
     */
    private float[][] mData;
    /**
     * 所有的点 一维是y坐标点 ,二维是x的坐标(方便触摸计算，用存储来优化计算)
     **/
    private float[][] mnData;

    private float width;
    private float height = 230;

    private Paint mPaint;
    private DashPathEffect bgDash = new DashPathEffect(new float[]{10, 10, 10, 10}, 0);
    private DashPathEffect crossDash = new DashPathEffect(new float[]{5, 5, 5, 5}, 0);
    private LinearGradient shader;
    private NumberFormat nt = NumberFormat.getPercentInstance();//百分比格式化
    private DecimalFormat df = new DecimalFormat("####0.00");//小数格式化
    /**
     * 图片缓存机制,缓存的画布
     **/
    private Bitmap mCacheImg, mCorssPoint;
    private Canvas mCacheCanvas;
    /**
     * 是否需要重绘 区别手势,默认true<br/>
     * 设置历史或者大小值改变时置为true
     **/
    private boolean isNewDrw = true;
    // 文本框
    private Bitmap textbox;
    private RectF dst;
    private float boxwidth = 80;// dp
    //上部下部图标空隙
    private float HTIGHT_TOP = 10f;// y值dp
    /**
     * 左边空隙
     **/
    private float xoffset = 10, yoffset = 10;//左边边框的边距。
    /**
     * 图表的高度(像素)
     **/
    private float invalidatey;
    // 最大最小值
    private float max = 1f, min = 100000f;
    private float close;
    // 备用缓存
    private float lastcachex;// 已经画到的x索引
    private int lastcachey;// 数据已经有的,长度！！！长度。已经画的电话。
    /**
     * 纵坐标上,一个百分比文本之间的距离.一共就3个。
     **/
    private float spilitheight = 0.0f;
    //x 上的文本
    private String[] xText;
    //y 上的文本
    private String[] yText_left, yText_right;
    //文本框中的文本
    private String[] leftIndicatorInbox = new String[]{"时间:", "指数:", "涨跌幅:"};
    private String[] rightIndicatorInbox = new String[]{"00:00", "0000.00", "-0.00%"};

    //x轴文本的坐标轴位置点,
    private float xposition[][];
    //y轴 文本的坐标轴位置点,
    private float yposition_left[][], yposition_right[][];
    //背景线条
    private float[] bglines;
    // 触摸的绘制标记
    private boolean isTouch = false;
    //触摸的x轴坐标,有x 则就有时间点,索引值
    private int touchChartx = 0;

    public void setType(TYPE tpe) {
        this.type = tpe;
        initDataArrayLength();
        positionXText();
    }

    public void recycle() {
        textbox.recycle();
        mCacheImg.recycle();
        mCorssPoint.recycle();
        mCacheImg = null;
    }

    public RouteLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public RouteLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RouteLineView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        boxwidth = ViewUtils.dp2px(boxwidth);
        height = ViewUtils.dp2px(height);

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        nt.setMinimumFractionDigits(2);
        nt.setMaximumFractionDigits(2);

        invalidatey = DrawChartUtils.getChartHeight(mPaint, height);
        shader = new LinearGradient(0, 0, 0, invalidatey, new int[]{Color.parseColor("#0f5CAFF4"), Color.BLACK}, null, Shader.TileMode.REPEAT);
        // 没有数据时的初始显示
        yText_right = new String[]{"1.26%", "0.00%", "0.63%",};
        yText_left = new String[]{"2890.92", "2887.21", "2877.50"};
        // 文本框
        textbox = BitmapFactory.decodeResource(context.getResources(), R.drawable.board_bg);
        mCorssPoint = BitmapFactory.decodeResource(context.getResources(), R.drawable.corsspoint);

        // 用作画文本框
        dst = new RectF();
    }

    /**
     * 设置历史文本 然后遍历。setData之前要set一次最大最小值<br/>
     * 一维 值, 二维 小时 095030，三维   分钟
     *
     * @param data 接口请求的坐标需要的数据
     */
    public synchronized void setData(float[][] data) {
        initData(data);
        isNewDrw = true;
        postInvalidate();
    }

    /**
     * 添加.添加之前，set一次最大值最小值。在 set 最大值最小值的时候进行其他数据的计算
     *
     * @param value 值
     * @param hours 时间
     */
    public void addData(String value, String hours, String minutes, String month, String day) {
        float fv = Float.parseFloat(value);
        float ft = Float.parseFloat(hours);
        float fs = Float.parseFloat(minutes);
        float fm = Float.parseFloat(month);
        float fd = Float.parseFloat(day);
        addData(fv, ft, fs, fm, fd);
    }

    /**
     * 添加.添加之前，set一次最大值最小值。在 set 最大值最小值的时候进行其他数据的计算
     *
     * @param value 值
     * @param hours 时间
     */
    public void addData(float value, float hours, float minutes, float month, float day) {
        if (type == TYPE.SZHENG && beyoudValueReplace(value, hours, minutes)) {
            //上证的替换，替换文本并且重绘。时间点越界，不论250，也废弃
            postInvalidate();
            return;
        }
        // 保存时间和值
        if (lastcachey == mData[0].length) {
            LogUtil.ee("error", ">>>>>DrawError>>>>>>超过250个值。无法储存和绘制");
            return;
        }
        mData[0][lastcachey] = value;
        mData[1][lastcachey] = hours;
        mData[2][lastcachey] = minutes;
        mData[4][lastcachey] = month;
        mData[5][lastcachey] = day;
        initaddData(false);
        //上证的开盘文本
        initStartTimeIfSZ();
        // 刷新界面
        isNewDrw = true;
        if (getVisibility() == View.VISIBLE) {
            postInvalidate();
        }
    }

    /**
     * 越界数据的替换。。只在上证里。。。并且判断是否越界
     */
    private boolean beyoudValueReplace(float value, float hours, float minutes) {
        if (lastcachey == 250) {
            mData[0][lastcachey - 1] = value;
            mData[1][lastcachey - 1] = hours;
            mData[2][lastcachey - 1] = minutes;
            return true;
        }
        return false;
    }

    /**
     * 新添加的数据，刷新到视图中。包含重新计算之前的值，和计算新加入的值。
     *
     * @param isneedCalOthers 是否需要计算其他值
     */
    private void initaddData(boolean isneedCalOthers) {
        if (mData == null && mData[0][0] == 0) return;//无历史数据，直接不添加
        //收盘价,距离最高和最低的差距
        float value = (Math.abs(close - max) > Math.abs(close - min)) ? Math.abs(close - max) : Math.abs(close - min);
        float cacheValue = (invalidatey - HTIGHT_TOP) / 2;

        LogUtil.ee("ADD Data To Chart:", "max:" + max + "  min:" + min + " close:" + close);
        // y轴数据的初始化.减去最小数。平均分剩余空间
        if (isneedCalOthers) {
            for (int i = 0; i < lastcachey; i++) {
                mData[3][i] = (mData[0][i] - close) / close;//算出比例，保留正负号
                mnData[0][i] = ((close - mData[0][i]) * cacheValue) / value + cacheValue + HTIGHT_TOP;//算出的坐标
            }
        } else {
            mData[3][lastcachey] = (mData[0][lastcachey] - close) / close;//比例,计算正确
            mnData[0][lastcachey] = ((close - mData[0][lastcachey]) * cacheValue) / value + cacheValue + HTIGHT_TOP;//坐标
            lastcachey++;
        }
    }

    /**
     * 设置最大值,最小值,改变整个视图的显示比例和y轴文本。默认情况，必须先有设置一对
     * 获取历史数据时，先进行大小比较.
     * <strong>每次必须set一次。</strong><br/>
     * 记住
     */
    public void setMaxAndMin(String mx, String mn, String closePrice) {
        if (TextUtils.isEmpty(mx) || TextUtils.isEmpty(mn)) {
            return;
        }
        float mmax = Float.parseFloat(mx);
        float mmin = Float.parseFloat(mn);
        float close = Float.parseFloat(closePrice);
        setMaxAndMin(mmax, mmin, close);
    }

    //setData时的。addData时的。
    private void setMaxAndMin(float mmax, float mmin, float closePrice) {
        if (mmax < 1 || mmin < 0 || mmax < mmin) {
            LogUtil.ee("RouteLine error:", "max and min value is wrong .check fist");
            return;
        }
        float cache2 = (Math.abs(mmax - close) > Math.abs(mmin - close)) ? Math.abs(mmax - close) : Math.abs(mmin - close);//新的差距
        float cache1 = (Math.abs(max - close) > Math.abs(min - close)) ? Math.abs(max - close) : Math.abs(min - close);//老的差距
        boolean cache3 = (closePrice != close);
        close = closePrice;
        max = (max < mmax) ? mmax : max;
        min = (min > mmin) ? mmin : min;
        if (cache3 || cache1 != cache2) {
            // 计算文本和文本坐标
            measureYText();//用新的，老的会引用0和10000。
            initaddData(true);
            isNewDrw = true;
            postInvalidate();
        }
    }

    /**
     * y 轴文本测量和位置
     */
    private void measureYText() {
        /** 文本数据计算，百分比____每个点代表的百分值 **/
        float cacheValue = spilitheight / 2;
        //每个点代表的百分数,纵坐标上的
        float singlePercent;
        if (Math.abs(close - max) > Math.abs(min - close)) {
            singlePercent = (Math.abs(close - max) / close) / cacheValue;
        } else {
            singlePercent = (Math.abs(close - min) / close) / cacheValue;
        }
        //右边的百分比
        yText_right[0] = nt.format(cacheValue * singlePercent);
        yText_right[1] = "0.00%";
        yText_right[2] = yText_right[0];
        //左边的value
        //保留4位小数
        cacheValue = cacheValue * singlePercent * close;//增长值,全半边百分比占close才是需要的值
        if (type == TYPE.EUROPE) {
            df.applyPattern("#0.0000");
        }
        yText_left[0] = df.format(close + cacheValue);
        yText_left[1] = df.format(close);
        yText_left[2] = df.format(close - cacheValue);
    }


    /**
     * 历史数据的计算
     */
    private synchronized void initData(float[][] freshData) {
        if (max == 0 || min == 0)
            LogUtil.e("RouteLine", "call setMaxAndMin method before setData method.");

        // y轴数据的初始化.减去最小数。平均分剩余空间
        if (mnData == null) LogUtil.e("RouteLine", "mnData null-----------------");

        lastcachey = mData[0].length;//总个数
        float value = (Math.abs(close - max) > Math.abs(close - min)) ? Math.abs(close - max) : Math.abs(close - min);
        float cacheValue = spilitheight / 2;
        for (int i = 0; i < freshData[0].length; i++) {
            if (i >= lastcachey) {//不数组越界就可。
                break;
            }
            mnData[0][i] = ((close - freshData[0][i]) * cacheValue) / value + cacheValue + HTIGHT_TOP;//y坐标
            mData[0][i] = freshData[0][i];//value
            mData[1][i] = freshData[1][i];//hours
            mData[2][i] = freshData[2][i];//minutes
            mData[3][i] = (mData[0][i] - close) / close;//比例
            mData[4][i] = freshData[3][i];//month
            mData[5][i] = freshData[4][i];//day
        }
        lastcachey = (freshData[0].length >= mData[0].length ? mData[0].length : freshData[0].length);//初始化已经画的点数
        freshData = null;
        //上证时间的话，开盘时间按照
        initStartTimeIfSZ();
    }

    /**
     * 上证时间的话，开盘时间按照。不需要重新测量。
     */
    private void initStartTimeIfSZ() {
        if (type == TYPE.SZHENG && lastcachey > 0) {
            LogUtil.e("RouteLine:", "重新设置，上证，左端时间");
            xText = new String[]{DrawChartUtils.formatTime(mData[1][0]) + ":" + DrawChartUtils.formatTime(mData[2][0]), "11:30/13:00", "15:00"};
        } else if (type == TYPE.GOLD && lastcachey > 0) {
            LogUtil.e("RouteLine:", "重新设置，黄金，左端时间");
            xText = new String[]{DrawChartUtils.formatTime(mData[1][0]) + ":" + DrawChartUtils.formatTime(mData[2][0]), "15:30"};
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        isNewDrw = true;
        normal();
    }


    @Override
    public void onDraw(Canvas canvas) {
        mPaint.setShader(null);
        if (!isNewDrw) {
            // 不是第一次，直接画缓存的
            canvas.drawBitmap(mCacheImg, 0, 0, null);
        } else {
            // 第一次，或者新数据来的时候.画到缓存上
            initBitmap();
            // 注释掉，方便 eclipse 调试。
            canvas = mCacheCanvas;
            LogUtil.ee("attation:", "第一次绘制drawBitmap到缓存");
        }

        // 绘制触摸的竖线
        if (isTouch & !isNewDrw) {
            //没有数据,则不绘制触摸和文本
            if (mnData[0][0] == 0) {
                return;
            }
            calcEnsureX();
            //绘制交叉点&Line
            drawCorss(canvas);
            // 绘制圆点
            canvas.drawBitmap(mCorssPoint, mnData[1][touchChartx] - 13f, mnData[0][touchChartx] - 13f, mPaint);
            // 绘制触摸出来的文本框&ScrollBlock
            drawTextBox(canvas);
        }

        //图片已经包括的内容
        if (!isNewDrw) {
            return;
        }

        LogUtil.ee("attation:", "绘制各种线条。");
        mPaint.setColor(Color.parseColor("#E1C6B1"));
        // 绘制背景实线和虚线
        mPaint.setStrokeWidth(1.0f);
        mPaint.setPathEffect(bgDash);
        for (int i = 1; i < 4; i++) {
            canvas.drawLine(0, bglines[i], width, bglines[i], mPaint);
        }
        mPaint.setPathEffect(null);
        canvas.drawLine(0, 1, width, 1, mPaint);
        canvas.drawLine(0, bglines[4], width, bglines[4], mPaint);

        // x轴文本
        mPaint.setTextSize(DrawChartUtils.xSize);
        mPaint.setColor(Color.GRAY);
        for (int j = 0; j < xText.length; j++) {
            canvas.drawText(xText[j], xposition[0][j], xposition[1][j], mPaint);
        }

        if (mData != null && mData[0][0] != 0) {
            // 绘制折线部分
            mnData[1][0] = 1;
            Path mPath = new Path();
            mPath.moveTo(0, invalidatey);
            mPath.lineTo(mnData[1][0], mnData[0][0]);
            mPaint.setColor(Color.parseColor("#5CAFF4"));
            mPaint.setStrokeWidth(2.0f);
            for (int i = 1; i < lastcachey; i++) {
                mnData[1][i] = eachUnitPixcels * i;//序号 * 个数 -> x轴坐标
                // 防止点重复
                if (iswilloverwrite && i != 1 && mnData[1][i] == mnData[1][i - 1]) {
                    continue;
                }
                lastcachex = mnData[1][i];
                canvas.drawLine(mnData[1][i - 1], mnData[0][i - 1], mnData[1][i], mnData[0][i], mPaint);
                mPath.lineTo(mnData[1][i], mnData[0][i]);
            }
            mPath.lineTo(mnData[1][lastcachey - 1], invalidatey);
            mPaint.setShader(shader);
            canvas.drawPath(mPath, mPaint);
        }

        // y轴文本,y2文本
        mPaint.setShader(null);
        mPaint.setTextSize(DrawChartUtils.ySize);
        mPaint.setColor(Color.RED);
        canvas.drawText(yText_left[0], yposition_left[0][0], yposition_left[1][0], mPaint);
        canvas.drawText(yText_right[0], yposition_right[0][0], yposition_right[1][0], mPaint);
        mPaint.setColor(Color.WHITE);
        canvas.drawText(yText_left[1], yposition_left[0][1], yposition_left[1][1], mPaint);
        canvas.drawText(yText_right[1], yposition_right[0][1], yposition_right[1][1], mPaint);
        mPaint.setColor(Color.parseColor("#50C577"));
        canvas.drawText(yText_left[2], yposition_left[0][2], yposition_left[1][2], mPaint);
        canvas.drawText(yText_right[2], yposition_right[0][2], yposition_right[1][2], mPaint);

        // 新的一次画图，是画在Bitmap上的。所以刷新。
        if (isNewDrw) {
            isNewDrw = false;
            postInvalidate();
        }
    }

    private void drawCorss(Canvas canvas) {
        //竖直线
        mPaint.setColor(Color.parseColor("#5CAFF4"));
        mPaint.setStrokeWidth(2.0f);
        mPaint.setPathEffect(crossDash);
        canvas.drawLine(mnData[1][touchChartx], 0, mnData[1][touchChartx], invalidatey, mPaint);
        //水平线
        canvas.drawLine(0, mnData[0][touchChartx], width, mnData[0][touchChartx], mPaint);
    }


    private void drawTextBox(Canvas canvas) {
        float[][] floats;
        // 测量和绘制文本框// 计算文本的位置
        if (x > (boxwidth + xoffset * 2)) {// 文本框在左边,常态。左上角
            floats = DrawChartUtils.measureBoxTextAndBox(mPaint, leftIndicatorInbox, rightIndicatorInbox, boxwidth, xoffset, yoffset);
            dst.set(xoffset, yoffset, floats[0][0], floats[1][0]);
        } else {// 文本框在右边，过于靠左边时
            float temp = width - boxwidth - xoffset * 2;
            floats = DrawChartUtils.measureBoxTextAndBox(mPaint, leftIndicatorInbox, rightIndicatorInbox, boxwidth, temp, yoffset);
            dst.set(temp, yoffset, floats[0][0], floats[1][0]);
        }

        int cache = rightIndicatorInbox.length;
        String date = DrawChartUtils.formatTime(mData[1][touchChartx]) + ":" + DrawChartUtils.formatTime(mData[2][touchChartx]);
        String value = df.format(mData[0][touchChartx]);
        String percent = nt.format(mData[3][touchChartx]);

        //绘制滑块
        DrawChartUtils.drawBlock(canvas, mPaint, value, mnData[0][touchChartx], height, 0);
        DrawChartUtils.drawBlock(canvas, mPaint, percent, mnData[0][touchChartx], height, width);
        DrawChartUtils.drawBellowTime(canvas, mPaint, DrawChartUtils.formatTime(mData[4][touchChartx]) + "-" + DrawChartUtils.formatTime(mData[5][touchChartx]) + " " + date, mnData[1][touchChartx], width, height);
        //文本框
        canvas.drawBitmap(textbox, null, dst, null);
        // 绘制框内的文本文本
        mPaint.setColor(Color.parseColor("#a9a9a9"));
        mPaint.setTextSize(DrawChartUtils.boxtextSize);
        mPaint.setPathEffect(null);
        canvas.drawText(leftIndicatorInbox[0], floats[0][1], floats[1][1], mPaint);
        canvas.drawText(date, floats[0][1 + cache], floats[1][1 + cache], mPaint);
        canvas.drawText(leftIndicatorInbox[1], floats[0][2], floats[1][2], mPaint);
        canvas.drawText(value, floats[0][2 + cache], floats[1][2 + cache], mPaint);
        for (int i = 1; i < cache; i++) {
            if (mData[3][touchChartx] <= 0) {
                mPaint.setColor(Color.parseColor("#50c577"));
            } else {
                mPaint.setColor(Color.parseColor("#F94747"));
            }
            if (i != 1) {
                canvas.drawText(percent, floats[0][i + 1 + cache], floats[1][i + 1 + cache], mPaint);
                canvas.drawText(leftIndicatorInbox[i], floats[0][i + 1], floats[1][i + 1], mPaint);
            }

//            if (i == 1) {
//
//            } else {
//
//            }
        }
    }

    /**
     * 确定触摸的有数据的x 轴坐标
     */
    private void calcEnsureX() {
        // 数组里面都没有坐标值,用得到的x坐标，来类比数组的时间，或者y值，都是可以的
        // 因为类比时间需要计算，类比y是直接已经做得的。
        for (int i = 0; i < mData[0].length; i++) {
            if (mnData[1][i] > x) {
                touchChartx = i;
                return;
            }
        }
    }


    /**
     * 当前触摸坐标
     */
    static float x, y, srcy;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        x = event.getX();
        y = event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (x < lastcachex && y < invalidatey) {
                    isTouch = true;
                    srcy = event.getY();
                    //作图区域点击，则不要父类拦截
                    this.getParent().requestDisallowInterceptTouchEvent(true);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (x < lastcachex && y < invalidatey) {
                    isTouch = true;
                    if (Math.abs(srcy - event.getY()) > 50) {
                        //作图区域拖动，则父类拦截。子类cancel。
                        this.getParent().requestDisallowInterceptTouchEvent(false);
                    }
                    postInvalidate();
                } else {
                    isTouch = false;
                }
                break;
            // 解决事件冲突
            default:
                isTouch = false;
                postInvalidate();
        }
        return true;
    }

    private void normal() {
        //必须要加，会多次调用的。
        spilitheight = invalidatey - HTIGHT_TOP * 2;
        //X 有初始文本直接测量
        positionXText();
        //Y 有初始文本直接测量
        yposition_left = DrawChartUtils.measureYText(mPaint, yText_left, height, true);
        yposition_right = DrawChartUtils.measureYText(mPaint, yText_right, height, false);
        //背景线条
        bglines = DrawChartUtils.measureBgLine(mPaint, height);
    }

    private void initBitmap() {
        if (mCacheImg != null || mCacheCanvas != null) {
            mCacheCanvas = null;
            mCacheImg.recycle();
            mCacheImg = null;
        }
        mCacheImg = Bitmap.createBitmap((int) width, (int) height, Bitmap.Config.ARGB_8888);
        mCacheCanvas = new Canvas(mCacheImg);
    }


    /**
     * 初始化数组长度 ---> 一个像素一个值
     **/
    private void initDataArrayLength() {
        //黄金大约244个
        if (type == TYPE.GOLD) {
            mData = new float[6][246];
            mnData = new float[2][246];
            eachUnitPixcels = width / 246;
            //上证大约240个
        } else if (type == TYPE.SZHENG) {
            mData = new float[6][242];
            mnData = new float[2][242];
            eachUnitPixcels = width / 242;
        } else {
            //欧元大约240个
            mData = new float[6][242];
            mnData = new float[2][242];
            eachUnitPixcels = width / 242;
        }
        iswilloverwrite = (eachUnitPixcels < 1);
    }

    private void positionXText() {
        // 没有网络获取到时的界面。set->测量
        if (type == TYPE.GOLD) {
            xText = new String[]{"20:00", "15:30"};
        } else if (type == TYPE.SZHENG) {
            xText = new String[]{"09:30", "11:30/13:00", "15:00"};
        } else {
            xText = new String[]{"05:00", "04:30"};
        }
        xposition = DrawChartUtils.measureXText(mPaint, xText, yText_left[0], yText_right[0], height);//x 底部坐标。
    }

}
