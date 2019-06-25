package com.hunty.widget.charts;

import android.util.Log;

import com.hunty.widget.BuildConfig;

/**
 * 日志工具
 */
public class LogUtil {

    private static final long EXPIRED_MILS = 30 * 24 * 60 * 60 * 1000L; // 30天
    public static final String PRE_LOG = "-->";
    public static final String PRE_EXCEPTION_LOG = "--E->";
    public static boolean isDebug = BuildConfig.DEBUG;

    /**
     * 写日志到控制台
     */
    public static void ii(Object tag, String msg) {
        if (isDebug) {
            if (tag instanceof String) {
                android.util.Log.i((String) tag, PRE_LOG + msg);
            } else {
                android.util.Log.i(tag.getClass().getSimpleName(), PRE_LOG + msg);
            }
        }
    }

    public static void ee(Object tag, String msg) {
        if (isDebug) {
            if (tag instanceof String) {
                android.util.Log.e((String) tag, PRE_EXCEPTION_LOG + msg);
            } else {
                android.util.Log.e(tag.getClass().getSimpleName(), PRE_EXCEPTION_LOG + msg);
            }
        }
    }

    public static void ee(Object tag, String msg, Throwable tr) {
        if (isDebug) {
            if (tag instanceof String) {
                android.util.Log.e((String) tag, PRE_EXCEPTION_LOG + msg, tr);
            } else {
                android.util.Log.e(tag.getClass().getSimpleName(), PRE_EXCEPTION_LOG + msg, tr);
            }
        }

    }

    /**
     * 写日志到文件和控制台
     */
    public static void i(Object tag, String msg) {
        if (isDebug) {
            if (tag instanceof String) {
                Log.i((String) tag, PRE_LOG + msg);
            } else {
                Log.i(tag.getClass().getSimpleName(), PRE_LOG + msg);
            }
        }
    }

    public static void i(Object tag, String msg, Throwable tr) {
        if (isDebug) {
            if (tag instanceof String) {
                Log.i((String) tag, PRE_LOG + msg, tr);
            } else {
                Log.i(tag.getClass().getSimpleName(), PRE_LOG + msg, tr);
            }
        }
    }

    public static void e(Object tag, String msg) {
        if (isDebug) {
            if (tag instanceof String) {
                Log.e((String) tag, PRE_EXCEPTION_LOG + msg);
            } else {
                Log.e(tag.getClass().getSimpleName(), PRE_EXCEPTION_LOG + msg);
            }
        }
    }

    public static void e(Object tag, String msg, Throwable tr) {
        if (isDebug) {
            if (tag instanceof String) {
                Log.e((String) tag, PRE_LOG + msg, tr);
            } else {
                Log.e(tag.getClass().getSimpleName(), PRE_LOG + msg, tr);
            }
        }
    }
}
