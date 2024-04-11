package com.debin.textspeech.util;

import android.util.Log;

import com.debin.textspeech.MyApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

public class LogFileUtil {
    public static final String TAG = LogFileUtil.class.getSimpleName();
    public static final int FILE_TYPE = 16;
    private static boolean mDebug = true;
    private static final boolean WRITE_TO_FILE = true;
    private static final String DATE_PATTERN = "yyyyMMdd";
    private static String todayDateStr;
    private static String baseFilePath;
    private static String baseErrorFilePath;
    private static String logFilePath;
    private static String errorFilePath;

    public LogFileUtil() {
    }

    public static void setDebug(boolean debug) {
        mDebug = debug;
    }

    public static void d(String tag, String msg) {
        if (mDebug) {
            Log.d(tag, msg);
            writeToFile(tag, msg, false);
        }
    }

    public static void d(String tag, String msg, Throwable tr) {
        if (mDebug) {
            Log.d(tag, msg, tr);
            writeToFile(tag, msg, false);
        }
    }

    public static void e(String tag, String msg) {
        if (mDebug) {
            Log.e(tag, msg);
            writeToFile(tag, msg, true);
        }
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (mDebug) {
            Log.e(tag, msg, tr);
            writeToFile(tag, msg, true);
        }
    }

    public static void v(String tag, String msg) {
        if (mDebug) {
            Log.v(tag, msg);
            writeToFile(tag, msg, false);
        }
    }

    public static void w(String tag, String msg) {
        if (mDebug) {
            Log.w(tag, msg);
            writeToFile(tag, msg, false);
        }
    }

    public static void i(String tag, String msg) {
        if (mDebug) {
            Log.i(tag, msg);
            writeToFile(tag, msg, false);
        }
    }

    public static void i(String tag, String msg, boolean writeToFile) {
        if (mDebug) {
            Log.i(tag, msg);
            if (writeToFile) {
                writeToFile(tag, msg, false);
            }
        }
    }

    private static void writeToFile(String tag, String msg, boolean error) {
        if (!WRITE_TO_FILE) {
            return;
        }

        if (!MyApplication.getInstance().permissionGranted()) {
            return;
        }

        initLogFile();
        writeToLogFile(tag, msg);
        if (error) {
            writeToErrorFile(tag, msg);
        }
    }

    private static void writeToLogFile(String tag, String msg) {
        try {
            File file = new File(logFilePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream outputStream = new FileOutputStream(file, true);
            String timeStr = DateUtils.dateFormat(new Date(), DateUtils.DATE_TIME_PATTERN);
            String msgStr = timeStr + " " + tag + "---" + msg + "\n";
            outputStream.write(msgStr.getBytes());
        } catch (Exception e) {
            if (mDebug) {
                android.util.Log.e(TAG, msg);
            }
        }
    }

    private static void writeToErrorFile(String tag, String msg) {
        try {
            File file = new File(errorFilePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream outputStream = new FileOutputStream(file, true);
            String timeStr = DateUtils.dateFormat(new Date(), DateUtils.DATE_TIME_PATTERN);
            String msgStr = timeStr + " " + tag + "---" + msg + "\n";
            outputStream.write(msgStr.getBytes());
        } catch (Exception e) {
            if (mDebug) {
                android.util.Log.e(TAG, msg);
            }
        }
    }

    public static String getLogFilePath() {
        return logFilePath;
    }

    public static String getLogFilePathByDate(Date date) {
        return getLogFilePathByDate(DateUtils.dateFormat(date, DATE_PATTERN));
    }

    public static String getLogFilePathByDate(String lodDate) {
        return baseFilePath + File.separator + lodDate + ".log";
    }

    public static String getBaseFilePath() {
        return baseFilePath;
    }

    public static String getBaseErrorFilePath() {
        return baseErrorFilePath;
    }

    public static void initLogFile() {
        if (todayDateStr != null && DateUtils.dateFormat(new Date(), DATE_PATTERN).equals(todayDateStr) && logFilePath != null && errorFilePath != null) {
            return;
        }
        try {
            baseFilePath = MyApplication.getInstance().getExternalFilesDir(null).getPath() + File.separator + "log";
            baseErrorFilePath = baseFilePath + File.separator + "error";
            todayDateStr = DateUtils.dateFormat(new Date(), DATE_PATTERN);
            logFilePath = baseFilePath + File.separator + todayDateStr + ".log";
            errorFilePath = baseErrorFilePath + File.separator + todayDateStr + ".log";

            File logFile = new File(logFilePath);
            if (!logFile.getParentFile().exists()) {
                logFile.getParentFile().mkdirs();
            }

            File errorFile = new File(errorFilePath);
            if (!errorFile.getParentFile().exists()) {
                errorFile.getParentFile().mkdirs();
            }

            if (logFile.exists()) {
                writeToFile(TAG, "\n\n\ninit log file", true);
            } else {
                writeToFile(TAG, "init log file", true);
            }
        } catch (Exception e) {
            if (mDebug) {
                android.util.Log.e(TAG, Log.getStackTraceString(e));
            }
        }
    }
}
