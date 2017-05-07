package com.hunty.widget.charts;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


import com.hunty.widget.R;
import com.hunty.widget.util.ViewUtils;

import java.text.DecimalFormat;
import java.util.Calendar;


/**
 * 烛光图
 * <p/>
 * <p/>
 * setDate();first
 * setData();
 *
 * @author ZhangJianqiu 2016-05-29周日
 */
public class CandleChart extends View {

    /**
     * 整个控件的高度和宽度
     */
    private float height = 230, width;// width = textSpace+rightwidth+剩下的
    /**
     * 每个蜡烛的宽度 ,每个蜡烛可以铺开的宽度
     **/
    private float candlewidth = 6, singlewidth;// dp
    /**
     * 底部和顶部的初始间隙
     **/
    private float TOP = 3, low;// dp

    // 常用控件的初始化
    private Paint mPaint;
    private Calendar mCal = Calendar.getInstance();
    private DashPathEffect bgDash = new DashPathEffect(new float[]{10, 10, 10, 10}, 0);
    private DashPathEffect crossDash = new DashPathEffect(new float[]{5, 5, 5, 5}, 0);
    private DecimalFormat nt = new DecimalFormat("####0.00");

    // 左边的文本
    private String[] leftText = new String[]{"1520.08", "1224.42", "1161.00"};
    private float[][] leftTextPosition;
    // 底部的文本
    private String[] bellowIndicator = new String[2];
    //左右小黑板
    private Bitmap textbox, mCorssDot;
    private float boxwidth = 85, xoffset = 5, yoffset = 5;//dp
    private RectF dst = new RectF();
    private String[] leftIndicatorInbox = new String[]{"日期", "开盘价", "收盘价", "最高", "最低"};
    private String[] rightIndicatorInbox = new String[]{"2016-08-05", "0000.00", "0000.00", "0000.00", "0000.00"};
    //底部文本的坐标
    private float[][] bellowTextPosition = new float[2][2];
    /**
     * 图片缓存机制
     **/
    private Bitmap mCacheImg;
    private Canvas mCacheCanvas;
    /**
     * 是否需要重绘 区别手势,默认true<br/>
     * 设置历史或者大小值改变时置为true
     **/
    private boolean isNewDrw = true, isTouch = false;
    private int touchChartx;//索引

    //背景线条的坐标
    private float[] bgLine;

    /**
     * 日K 图的数据
     * <p/>
     * [4][i],0代表top,1代表topvalue,2代表bottomvalue,3代表bottom，4代表日期,5代表5日均线，6代表10日均线，7代表20日均线。
     * 代表，最高，开盘，收盘，最低，时间(不在做显示使用)，*，*，*，
     **/
    private float[][] mData;
    //30个日期
    private String[] mDate;
    /**
     * 保存每个数据的 y 轴坐标点
     * <p/>
     * 表示y和x,<br/>
     * 0-top,1-open,2-bottom,3-close,<br/>,同时还有,4-ma5,5-ma10,6-ma20,7-x轴。共8个。
     * 代表坐标值，前六个是y值，然后一个是x值
     **/
    private float[][] mDataPosition;
    private float max, min;
    /**
     * 缓存使用
     **/
    private float candlewidthpercent, cachey;
    //图表线图部分的高度
    float invalidatey;

    public CandleChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public CandleChart(Context context, AttributeSet attrs) {
        this(context, attrs, 1);
    }

    public CandleChart(Context context) {
        this(context, null);
    }

    private void init(Context mContext) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStyle(Paint.Style.FILL);// 设置具有填充效果
        mPaint.setStrokeWidth(1.0f);
        //各种数据
        height = ViewUtils.dp2px(height);
        //图表线图部分的高度
        invalidatey = DrawChartUtils.getChartHeight(mPaint, height);//有效到下面的坐标
        TOP = ViewUtils.dp2px(TOP);
        candlewidth = ViewUtils.dp2px(candlewidth);
        boxwidth = ViewUtils.dp2px(boxwidth);

        // 文本框
        textbox = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.board_bg);
        mCorssDot = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.corsspoint);

        // 底部日期初始化  默认，不设置的情况下，有文本可以显示
        bellowIndicator[1] = (mCal.get(Calendar.MONTH) + 1) + "."
                + mCal.get(Calendar.DATE);
        mCal.add(Calendar.DATE, -30);
        bellowIndicator[0] = (mCal.get(Calendar.MONTH) + 1) + "."
                + mCal.get(Calendar.DATE);
    }

    /**
     * 设置烛光图的数据。设置底部x 轴上的文本。
     *
     * @param values [4][i],0代表top,1代表topvalue,2代表bottomvalue,3代表bottom。
     * @param mm     最大值
     * @param nn     最小值
     */
    public void setData(float[][] values, float mm, float nn, String lefttext, String righttext) {
        mData = values;
        max = mm;
        min = nn;
        initRightText();
        bellowIndicator[0] = lefttext;
        bellowIndicator[1] = righttext;
        mDataPosition = new float[8][values[0].length];//初始化坐标数组
        initData();
        if (getVisibility() == View.VISIBLE) {
            isNewDrw = true;
            postInvalidate();
        }
    }

    public void setDate(String[] date) {
        mDate = date;
    }

    /**
     * 按照类型的特殊处理
     *
     * @param type 2 四位小数
     */
    public void setType(int type) {
        if (type == 2) {
            nt = new DecimalFormat("#0.0000");
        } else {

        }
    }

    private void initRightText() {
        float cache = (max - min) / 2;
        leftText[0] = nt.format(max);
        leftText[1] = nt.format(min + cache);
        leftText[2] = nt.format(min);
    }

    /**
     * 数值，转化成纵坐标点
     * <p/>
     * 大量的运算都在这里。只计算y值
     */
    private void initData() {
        low = invalidatey - TOP;//整个的长度，最低点y
        float height = low - TOP;
        candlewidthpercent = max - min;
        if (mData == null) {
            LogUtil.ee(this, "the mData is empty,Please call setData to initalise the values first.");
        }

        //计算纵坐标点
        for (int j = 0; j < mData[0].length; j++) {
            mDataPosition[0][j] = low - ((mData[0][j] - min) / candlewidthpercent) * height;
            mDataPosition[1][j] = low - ((mData[1][j] - min) / candlewidthpercent) * height;
            mDataPosition[2][j] = low - ((mData[2][j] - min) / candlewidthpercent) * height;
            mDataPosition[3][j] = low - ((mData[3][j] - min) / candlewidthpercent) * height;
            //均线的坐标
            if (mData[5][j] != 0) {
                mDataPosition[4][j] = low - ((mData[5][j] - min) / candlewidthpercent) * height;
            } else {
                mDataPosition[4][j] = low - ((mData[5][j - 1] - min) / candlewidthpercent) * height;
            }
            if (mData[6][j] != 0) {
                mDataPosition[5][j] = low - ((mData[6][j] - min) / candlewidthpercent) * height;
            } else {
                mDataPosition[5][j] = low - ((mData[6][j - 1] - min) / candlewidthpercent) * height;
            }
            if (mData[7][j] != 0) {
                mDataPosition[6][j] = low - ((mData[7][j] - min) / candlewidthpercent) * height;
            } else {
                mDataPosition[6][j] = low - ((mData[7][j - 1] - min) / candlewidthpercent) * height;
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        isNewDrw = true;//重新作图
        normal();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!isNewDrw) {
            // 不是第一次，直接画缓存的
            canvas.drawBitmap(mCacheImg, 0, 0, null);
        } else {
            // 第一次，或者新数据来的时候.画到缓存上
            initBitmap();
            canvas = mCacheCanvas;
        }
        // 绘制触摸的竖线
        if (isTouch & !isNewDrw) {
            //没有数据,则不绘制触摸和文本
            if (mData == null || mData[0][0] == 0) {
                return;
            }
            calcEnsureX();
            drawCorssLines(canvas);
            // 绘制圆点
            canvas.drawBitmap(mCorssDot, mDataPosition[7][touchChartx] - 13f, cachey - 13f, mPaint);
            // 绘制触摸出来的文本框
            drawTextBox(canvas);
        }
        //图片已经包括的内容
        if (!isNewDrw) {
            return;
        }
        // 画背景直线
        mPaint.setStrokeWidth(1.0f);
        mPaint.setColor(Color.parseColor("#E1C6B1"));
        for (int i = 0; i < bgLine.length; i++) {
            if (i == 0 || i == 4) {
                mPaint.setPathEffect(null);
                canvas.drawLine(0, bgLine[i], width, bgLine[i], mPaint);
            } else {
                mPaint.setPathEffect(bgDash);
                canvas.drawLine(0, bgLine[i], width, bgLine[i], mPaint);
            }
        }
        // 画左边的文本
        mPaint.setTextSize(DrawChartUtils.ySize);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        for (int i = 0; i < 3; i++) {
            canvas.drawText(leftText[i], leftTextPosition[0][i], leftTextPosition[1][i], mPaint);
        }
        // 画底部文本
        mPaint.setTextSize(DrawChartUtils.xSize);
        mPaint.setColor(Color.GRAY);
        for (int i = 0; i < 2; i++) {
            //初始化的是昨天和30前
            canvas.drawText(bellowIndicator[i], bellowTextPosition[0][i], bellowTextPosition[1][i], mPaint);
        }
        //无数据，直接退出
        if (mData == null || mDataPosition == null || getVisibility() != View.VISIBLE) {
            isNewDrw = false;
            postInvalidate();
            return;
        }
        // 开始画蜡烛    candlewidthpercent = candlewidth / singlewidth;
        candlewidthpercent = candlewidth / 2;
        cachey = singlewidth / 2;
        LogUtil.ee("摆放::", "  candlewidth:" + candlewidth + " singlewidth:" + singlewidth);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        for (int k = 0; k < mData[0].length; k++) {
            changeColor(k, mPaint);
            //确定x轴中心位置
            mDataPosition[7][k] = singlewidth * k + cachey;
            //画灯芯
            canvas.drawLine(mDataPosition[7][k], mDataPosition[0][k], mDataPosition[7][k], mDataPosition[3][k], mPaint);
            //蜡烛的矩形框,style类型空心效果
            canvas.drawRect(mDataPosition[7][k] - candlewidthpercent, mDataPosition[1][k], mDataPosition[7][k] + candlewidthpercent, mDataPosition[2][k], mPaint);
            if (mDataPosition[1][k] > mDataPosition[2][k]) {
                //封闭的小矩形
                mPaint.setColor(Color.BLACK);
                canvas.drawRect(mDataPosition[7][k] - candlewidthpercent + 1, mDataPosition[1][k] - 1, mDataPosition[7][k] + candlewidthpercent - 1, mDataPosition[2][k] + 1, mPaint);
            }
        }

        //画5日均线，10日均线，20日均线
        mPaint.setStrokeWidth(2.0f);
        mPaint.setColor(Color.parseColor("#FCC031"));
        for (int j = 1; j < mData[0].length; j++) {
            //不画出最高点。三条线相同
            if (mDataPosition[4][j - 1] < TOP || mDataPosition[4][j - 1] > low) {
                continue;
            }
            canvas.drawLine(mDataPosition[7][j - 1], mDataPosition[4][j - 1], mDataPosition[7][j], mDataPosition[4][j], mPaint);
        }
        mPaint.setColor(Color.parseColor("#9A73EC"));
        for (int j = 1; j < mData[0].length; j++) {
            if (mDataPosition[5][j - 1] < TOP || mDataPosition[5][j - 1] > low) {
                continue;
            }
            canvas.drawLine(mDataPosition[7][j - 1], mDataPosition[5][j - 1], mDataPosition[7][j], mDataPosition[5][j], mPaint);
        }
        mPaint.setColor(Color.parseColor("#F47431"));
        for (int j = 1; j < mData[0].length; j++) {
            if (mDataPosition[6][j - 1] < TOP || mDataPosition[6][j - 1] > low) {
                continue;
            }
            canvas.drawLine(mDataPosition[7][j - 1], mDataPosition[6][j - 1], mDataPosition[7][j], mDataPosition[6][j], mPaint);
        }

        // 新的一次画图，是画在Bitmap上的。所以刷新。
        if (isNewDrw) {
            isNewDrw = false;
            postInvalidate();
        }
    }

    //绘制交叉线
    private void drawCorssLines(Canvas canvas) {
        //竖直线
        mPaint.setStrokeWidth(2.0f);
        mPaint.setColor(Color.parseColor("#5CAFF4"));
        mPaint.setPathEffect(crossDash);
        canvas.drawLine(mDataPosition[7][touchChartx], 0, mDataPosition[7][touchChartx], invalidatey, mPaint);
        //水平线
        cachey = (mDataPosition[1][touchChartx] + mDataPosition[2][touchChartx]) / 2;
        canvas.drawLine(0, cachey, width, cachey, mPaint);
    }

    private void drawTextBox(Canvas canvas) {
        float[][] floats;
        // 测量和绘制文本框
        // 计算文本的位置
        String value = nt.format(mData[2][touchChartx]);
        String date = formatdate();
        // 绘制框内的文本文本
        mPaint.setColor(Color.WHITE);
        mPaint.setPathEffect(null);
        if (x > (boxwidth + xoffset * 2)) {// 文本框在左边,常态
            floats = DrawChartUtils.measureBoxTextAndBox(mPaint, leftIndicatorInbox, rightIndicatorInbox, boxwidth, xoffset, yoffset);
            dst.set(xoffset, yoffset, floats[0][0], floats[1][0]);
            //绘制滑块,如果文本框在左边，则滑块就在右边
            DrawChartUtils.drawBlock(canvas, mPaint, value, cachey, height, width);
            DrawChartUtils.drawBellowTime(canvas, mPaint, date, mDataPosition[7][touchChartx], width, height);
        } else {// 文本框在右边，过于靠左边时
            float temp = width - boxwidth - xoffset * 2;
            floats = DrawChartUtils.measureBoxTextAndBox(mPaint, leftIndicatorInbox, rightIndicatorInbox, boxwidth, temp, yoffset);
            dst.set(temp, yoffset, floats[0][0], floats[1][0]);
            //绘制滑块，如果文本框在右边，则滑块就在左边
            DrawChartUtils.drawBlock(canvas, mPaint, value, cachey, height, 0);
            DrawChartUtils.drawBellowTime(canvas, mPaint, date, mDataPosition[7][touchChartx], width, height);
        }
        //文本框
        canvas.drawBitmap(textbox, null, dst, null);
        //画左边的指示标语
        mPaint.setTextSize(DrawChartUtils.boxtextSize);
        for (int i = 0; i < leftIndicatorInbox.length; i++) {
            canvas.drawText(leftIndicatorInbox[i], floats[0][i + 1], floats[1][i + 1], mPaint);
        }
        int count = leftIndicatorInbox.length + 1;
        canvas.drawText(mDate[touchChartx].substring(0, 4) + "-" + date, floats[0][count], floats[1][count], mPaint);
        canvas.drawText(nt.format(mData[1][touchChartx]), floats[0][count + 1], floats[1][count + 1], mPaint);
        canvas.drawText(value, floats[0][count + 2], floats[1][count + 2], mPaint);
        canvas.drawText(nt.format(mData[0][touchChartx]), floats[0][count + 3], floats[1][count + 3], mPaint);
        canvas.drawText(nt.format(mData[3][touchChartx]), floats[0][count + 4], floats[1][count + 4], mPaint);
    }

    private String formatdate() {
        return mDate[touchChartx].substring(4, 6) + "-" + mDate[touchChartx].substring(6);
    }

    private void calcEnsureX() {
        for (int i = 0; i < mData[0].length; i++) {
            if (mDataPosition[7][i] > x) {
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
        x = event.getX();
        y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (y < invalidatey && y > TOP) {
                    isTouch = true;
                    srcy = event.getY();
                    //作图区域点击，则不要父类拦截
                    this.getParent().requestDisallowInterceptTouchEvent(true);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (/*x > cachey && */y < invalidatey && y > TOP) {
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

    public void changeColor(int k, Paint mPaint) {
        // 值大，则坐标小
        if (mData[1][k] > mData[2][k]) {//开盘大于收盘..对应坐标左小又大
            mPaint.setColor(Color.parseColor("#50c577"));
        } else if (mData[1][k] < mData[2][k]) {//收盘大于开盘。位置更换
            mPaint.setColor(Color.RED);
        } else if (mData[1][k] == mData[2][k]) {//相等，灰色
            mPaint.setColor(Color.GRAY);
            mPaint.setColor(Color.GRAY);
        }
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
     * 测量完毕，即可获得底纹所有的坐标
     * --重新初始化坐标系，和初始化坐标体系
     */
    private void normal() {
        // 计算出底部线的y
        bgLine = DrawChartUtils.measureBgLine(mPaint, height);
        // 计算左边问本地额坐标
        leftTextPosition = DrawChartUtils.measureYText(mPaint, leftText, height, true);
        //计算每一个宽度
        singlewidth = (width - candlewidth) / 30;//计算每个蜡烛，可以占据的位置     (mData[0].length)
        //计算底部日期的坐标
        bellowTextPosition = DrawChartUtils.measureXText(mPaint, bellowIndicator, leftText[0], "", height);
        candlewidthpercent = candlewidth / singlewidth;
    }

}
