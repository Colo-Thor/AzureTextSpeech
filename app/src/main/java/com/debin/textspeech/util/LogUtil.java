package com.debin.textspeech.util;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogUtil {
    public static final String TAG = LogUtil.class.getSimpleName();
    private static volatile boolean mDebug = true;
    private static final boolean writeToFile = false;
    private static final String DATA_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 设置日志状态
     *
     * @param debug 当debug为ture时显示日志，false时不显示日志
     */
    public static void setDebug(boolean debug) {
        android.util.Log.d(TAG, "loggable:" + debug);
        mDebug = debug;
    }

    /**
     * Send a DEBUG log message.
     *
     * @param tag Used to identify the source of a log message. It usually identifies the class or
     *            activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void d(String tag, String msg) {
        if (mDebug) {
            android.util.Log.d(tag, msg);
        }
    }

    /**
     * Send a DEBUG log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies the class or
     *            activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static void d(String tag, String msg, Throwable tr) {
        if (mDebug) {
            android.util.Log.d(tag, msg, tr);
        }
    }

    /**
     * Send an ERROR log message.
     *
     * @param tag Used to identify the source of a log message. It usually identifies the class or
     *            activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void e(String tag, String msg) {
        if (mDebug) {
            android.util.Log.e(tag, msg);
        }
        writeToFile(tag, msg);
    }

    /**
     * Send a ERROR log message and log the exception.
     *
     * @param tag Used to identify the source of a log message. It usually identifies the class or
     *            activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static void e(String tag, String msg, Throwable tr) {
        if (mDebug) {
            android.util.Log.e(tag, msg, tr);
        }
        writeToFile(tag, msg);
    }

    /**
     * Send a VERBOSE log message.
     *
     * @param tag Used to identify the source of a log message. It usually identifies the class or
     *            activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void v(String tag, String msg) {
        if (mDebug) {
            android.util.Log.v(tag, msg);
        }
    }

    /**
     * Send a WARN log message.
     *
     * @param tag Used to identify the source of a log message. It usually identifies the class or
     *            activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void w(String tag, String msg) {
        if (mDebug) {
            android.util.Log.w(tag, msg);
            writeToFile(tag, msg);
        }
    }

    /**
     * Send a INFO log message.
     *
     * @param tag Used to identify the source of a log message. It usually identifies the class or
     *            activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void i(String tag, String msg) {
        if (mDebug) {
            android.util.Log.i(tag, msg);
        }
    }

    private static void writeToFile(String tag, String msg) {
        if (!writeToFile) {
            return;
        }

        FileOutputStream outputStream = null;
        try {
            File file = new File(Environment.getExternalStorageDirectory() + "/"
                    + Environment.DIRECTORY_DOWNLOADS + "/" + tag + ".txt");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            outputStream = new FileOutputStream(file, true);
            String timeStr = new SimpleDateFormat(DATA_FORMAT_PATTERN).format(new Date());
            String msgStr = timeStr + " " + tag + "---" + msg + "\n";
            outputStream.write(msgStr.getBytes());
        } catch (Exception e) {
            if (mDebug) {
                android.util.Log.e(TAG, Log.getStackTraceString(e));
            }
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (Exception e) {
                }
            }
        }
    }
}
