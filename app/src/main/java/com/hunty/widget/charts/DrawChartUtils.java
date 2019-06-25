package com.hunty.widget.charts;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.hunty.widget.HlApplication;
import com.hunty.widget.util.ViewUtils;


/**
 * 折线图
 * Created by zhangjianqiu on 2016/8/3 0003.
 */
public class DrawChartUtils {

    /**
     * x 文本大小
     */
    public static float xSize = ViewUtils.sp2px(13);// sp
    /**
     * y 文本大小
     */
    public static float ySize = ViewUtils.sp2px(13);// sp
    /**
     * 文本框中的文字大小
     */
    public static float boxtextSize = ViewUtils.sp2px(10);
    /**
     * x文本间隙，左侧左右，右侧左右。
     */
    private static float leftText_paddingLeft = ViewUtils.dp2px(2);
    private static float leftText_paddingright = ViewUtils.dp2px(2);
    private static float rightText_paddingLeft = leftText_paddingright;
    private static float rightText_paddingright = leftText_paddingLeft;
    /**
     * y文本的上下间隙。所有的间隙。
     */
    public static float yText_paddingtop = 4;
    public static float yText_paddingbottom = 4;
    private static float xText_paddingtop = ViewUtils.dip2px(HlApplication.getContext(), 2);
    private static float xText_paddingbottom = xText_paddingtop;
    /**
     * 分时日K，下划线的宽度保存。
     */
    public static float tabIndicatorbang, x;

    /**
     * x 轴文本测量和位置。
     * 按照整个屏幕宽度计算。。。。。。注释部分，因为x文本左对齐则不要lefet.。
     **/
    public static float[][] measureXText(Paint mPaint, String[] xText, String yleftSample, String yrightSample, float height) {
        float[][] xposition = new float[2][xText.length];
        float left = 0;//getTextWidthAndLength(mPaint, yleftSample, ySize)[0];
        float right = getTextWidthAndLength(mPaint, xText[xText.length - 1], ySize)[0];
        float width = ViewUtils.getScreenWidth() - left - right - leftText_paddingLeft - leftText_paddingright - rightText_paddingLeft - rightText_paddingright;
        float spilitwidth = width / (xText.length - 1);
        for (int i = 0; i < xText.length; i++) {
            if (i == 0) {
                xposition[0][i] = leftText_paddingLeft;//+ leftText_paddingright + left;//第一个文本
            } else if (i == (xText.length - 1)) {
                xposition[0][i] = ViewUtils.getScreenWidth() - right - rightText_paddingright;//最后一个文本
            } else {
                xposition[0][i] = xposition[0][0] + i * spilitwidth - getTextWidthAndLength(mPaint, xText[i], xSize)[0] / 2;//中间的文本
            }
            xposition[1][i] = height - xText_paddingbottom - mPaint.descent();
        }
        return xposition;
    }

    /**
     * y 轴文本测量和位置。
     */
    public static float[][] measureYText(Paint mPaint, String[] yText, float height, boolean isLeft) {
        float[][] yposition = new float[2][yText.length];
        mPaint.setTextSize(ySize);
        yposition[1][0] = yText_paddingtop - mPaint.ascent();
        float heightBelow = xText_paddingtop + xText_paddingbottom + getTextWidthAndLength(mPaint, "0", xSize)[1];
        mPaint.setTextSize(ySize);
        yposition[1][1] = (height - heightBelow) / 2 - mPaint.descent();
        yposition[1][2] = height - heightBelow - yText_paddingbottom - mPaint.descent();
        if (isLeft) {
            yposition[0][0] = leftText_paddingLeft;
            yposition[0][1] = leftText_paddingLeft;
            yposition[0][2] = leftText_paddingLeft;
        } else {
            yposition[0][0] = ViewUtils.getScreenWidth() - rightText_paddingright - getTextWidthAndLength(mPaint, yText[0], ySize)[0];
            yposition[0][1] = yposition[0][0];
            yposition[0][2] = yposition[0][0];
        }
        return yposition;
    }

    /**
     * [0,1][0]框的宽高,。。。0是x,1是y。
     * 标示的坐标
     * 数值的坐标
     */
    public static float[][] measureBoxTextAndBox(Paint mPaint, String[] leftTagSample, String[] rightValueSample, float boxWidth, float xoffset, float yoffset) {
        float[][] position = new float[2][leftTagSample.length + rightValueSample.length + 1];
        int cache = leftTagSample.length;//行数
        float leftpadding = ViewUtils.dp2px(3);//左侧图文间隙
        float rightpadding = ViewUtils.dp2px(4);//右侧图文间隙
        float divideheight = ViewUtils.dp2px(5);//行间距
        float textHeight = getTextWidthAndLength(mPaint, "0", boxtextSize)[1];//只需要知道高度就行。
        for (int i = 0; i < cache; i++) {
            //左侧的坐标
            position[0][i + 1] = leftpadding + xoffset;//x
            position[1][i + 1] = (i + 1) * (divideheight + textHeight) + yoffset;//y
        }
        for (int j = 0; j < rightValueSample.length; j++) {
            //右侧的坐标
            float[] cacheposition = getTextWidthAndLength(mPaint, rightValueSample[j], boxtextSize);//右侧文本的长宽
            position[0][j + cache + 1] = boxWidth - rightpadding - cacheposition[0] + xoffset;//x
            position[1][j + cache + 1] = (j + 1) * (divideheight + textHeight) + yoffset;//y
        }
        position[0][0] = boxWidth + xoffset;
        //长度做索引,-1
        position[1][0] = position[1][cache + rightValueSample.length] + divideheight + yoffset;
        return position;
    }

    /**
     * 背景虚线的x坐标
     *
     * @param mPaint
     * @param height
     * @return
     */
    public static float[] measureBgLine(Paint mPaint, float height) {
        float[] lines = new float[5];
        float heightBelow = xText_paddingtop + xText_paddingbottom + getTextWidthAndLength(mPaint, "0", xSize)[1];
        float cache = (height - heightBelow) / (lines.length - 1);
        lines[0] = 1;
        lines[1] = cache;
        lines[2] = cache * 2;
        lines[3] = cache * 3;
        lines[4] = cache * 4;
        return lines;
    }

    /**
     * float[0] 宽度 float[1] 高度
     *
     * @param mPaint
     * @param text
     * @param textSize
     * @return
     */
    public static float[] getTextWidthAndLength(Paint mPaint, String text, float textSize) {
        float[] values = new float[2];
        mPaint.setTextSize(textSize);
        Paint.FontMetrics fm = mPaint.getFontMetrics();
        values[0] = mPaint.measureText(text);
        values[1] = fm.descent - fm.ascent;
        return values;
    }

    /**
     * 获得图标框的高度。减去底部的高度
     *
     * @param mPaint
     * @param height
     * @return
     */
    public static float getChartHeight(Paint mPaint, float height) {
        return height - getTextWidthAndLength(mPaint, "0", xSize)[1] - xText_paddingtop - xText_paddingbottom;
    }

    /**
     * 获得图标框的宽度。没有右边，则不需要rightSample。
     *
     * @param mPaint
     * @param width
     * @param leftSample
     * @param rightSample
     * @param isHasRight
     * @return
     */
    public static float getChartWidth(Paint mPaint, float width, String leftSample, String rightSample, boolean isHasRight) {
        if (isHasRight) {
            return width - leftText_paddingLeft - leftText_paddingright - getTextWidthAndLength(mPaint, leftSample, ySize)[0] - rightText_paddingLeft - rightText_paddingright - getTextWidthAndLength(mPaint, rightSample, ySize)[0];
        } else {
            return width - leftText_paddingLeft - leftText_paddingright - getTextWidthAndLength(mPaint, leftSample, ySize)[0];
        }
    }

    /**
     * 使用从左往右密集排列战略
     *
     * @param cachehour
     * @param cacheminites
     * @return
     */
    private float initXorder(RouteLineView.TYPE type, float cachehour, float cacheminites) {
        float time = cachehour * 60 + cacheminites;
        if (type == RouteLineView.TYPE.SZHENG) {
            if (time >= 780 && time <= 900) {//上证13:00开始
                return time - 780 + 120;
            } else if (cachehour <= 690 && time >= 570) {
                return time - 570;
            }
        } else if (type == RouteLineView.TYPE.GOLD) {
            if (time >= 1250 && time < 1440) {
                return time - 1250;
            } else if (time >= 540 && time <= 690) {
                return time - 540 + 190;
            } else if (time >= 810 && time >= 930) {
                return time - 810 + 340;
            } else if (time >= 0 && time >= 150) {
                return time + 460;
            }
        } else if (type == RouteLineView.TYPE.EUROPE) {
            if (time >= 300 && time < 1440) {
                return time - 300;
            } else if (time <= 270) {
                return time + 1140;
            }
        }
        return 0.0f;
    }

    //备用，不可删除
    //获得最近的 日期序列不为0，并且和当前日期序列少于7的坐标值
    private float getNearNoZero(int type, int i, float[][] mData, float[][] mnData) {
        int cache = i;
        if (type == 1) {
            while (true) {
                if (mData[4][cache - 1] != 0 && (mData[4][i] - mData[4][cache]) < 7) {
                    return mnData[1][cache - 1];//x
                } else {
                    cache--;
                    if (cache == 0) {
                        return mnData[1][cache];
                    }
                }
            }
        } else {
            while (true) {
                if (mData[4][cache - 1] != 0 && (mData[4][i] - mData[4][cache]) < 7) {
                    return mnData[0][cache - 1];//y
                } else {
                    cache--;
                    if (cache == 0) {
                        return mnData[1][cache];
                    }
                }
            }
        }
    }

    //画左边的 offset 0 or width
    public static void drawBlock(Canvas canvas, Paint mPaint, String value, float position, float height, float xoffset) {
        mPaint.setColor(Color.parseColor("#5CAFF4"));
        float textborderpadding = 3;
        float[] text = getTextWidthAndLength(mPaint, value, ySize);
        float blockheight = textborderpadding * 2 + text[1];
        float low = 0, cache = blockheight / 2, blockwidth;
        blockwidth = leftText_paddingLeft + leftText_paddingright + text[0];
        if (xoffset != 0) {
            xoffset = xoffset - blockwidth;
        }
        if (position < blockheight) {
            //上部
            canvas.drawRect(xoffset, 0, blockwidth + xoffset, blockheight, mPaint);
            mPaint.setColor(Color.WHITE);
            canvas.drawText(value, xoffset + leftText_paddingLeft, blockheight - textborderpadding - mPaint.descent(), mPaint);
        } else if (position > (low = (getChartHeight(mPaint, height) - cache))) {
            //底部
            canvas.drawRect(xoffset, low - cache, blockwidth + xoffset, low + cache, mPaint);
            mPaint.setColor(Color.WHITE);
            canvas.drawText(value, xoffset + leftText_paddingLeft, low + cache - textborderpadding - mPaint.descent(), mPaint);
        } else {
            //中部
            canvas.drawRect(xoffset, position - cache, blockwidth + xoffset, position + cache, mPaint);
            mPaint.setColor(Color.WHITE);
            canvas.drawText(value, xoffset + leftText_paddingLeft, position + cache - textborderpadding - mPaint.descent(), mPaint);
        }
    }


    //画下边
    public static void drawBellowTime(Canvas canvas, Paint mPaint, String datetime, float position, float width, float height) {
        mPaint.setColor(Color.parseColor("#5CAFF4"));
        float textborderpadding = 3;
        float[] time = getTextWidthAndLength(mPaint, datetime, xSize);
        float cache = leftText_paddingLeft + time[0] / 2;
        float top = getChartHeight(mPaint, height);
        float below = top + textborderpadding * 2 + time[1];
        if (position < cache) {//左侧
            canvas.drawRect(0, top, cache * 2, below, mPaint);
            mPaint.setColor(Color.WHITE);
            canvas.drawText(datetime, leftText_paddingLeft, top + textborderpadding - mPaint.ascent(), mPaint);
        } else if ((position + cache) > width) {//右侧
            cache *= 2;
            canvas.drawRect(width - cache, top, width, below, mPaint);
            mPaint.setColor(Color.WHITE);
            canvas.drawText(datetime, width - cache + leftText_paddingLeft, top + textborderpadding - mPaint.ascent(), mPaint);
        } else {//其他
            canvas.drawRect(position - cache, top, position + cache, below, mPaint);
            mPaint.setColor(Color.WHITE);
            canvas.drawText(datetime, position - cache + leftText_paddingLeft, top + textborderpadding - mPaint.ascent(), mPaint);
        }
    }

    //分钟的加0操作
    public static String formatTime(float x) {
        if (x < 10) {
            return "0" + (int) x;
        } else {
            return "" + (int) x;
        }
    }
}
