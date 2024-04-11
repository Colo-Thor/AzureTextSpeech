package com.debin.textspeech.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.debin.textspeech.MyApplication;

import java.util.List;

import io.microshow.rxffmpeg.RxFFmpegInvoke;
import io.microshow.rxffmpeg.RxFFmpegSubscriber;

import static android.content.Context.ACTIVITY_SERVICE;

public class CommonUtil {
    public static final String TAG = "TextSpeech";
    public static final String GATEWAY_IP = "192.168.1.1";

    /**
     * 应用是否在前台运行
     */
    public static boolean isApplicationForeground(Context context) {
        ActivityManager activityManager =
                (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcessInfoList =
                activityManager.getRunningAppProcesses();
        if (appProcessInfoList != null) {
            for (ActivityManager.RunningAppProcessInfo appProcessInfo : appProcessInfoList) {
                if (appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    if (appProcessInfo.processName.equals(context.getApplicationInfo().processName)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 将应用调到前台运行
     *
     * @param context Context
     * @return 是否在前台显示
     */
    public static boolean startApplicationToForeground(Context context) {
        if (context == null) {
            return false;
        }

        String packageName = context.getPackageName();
        ActivityManager activityManager =
                (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = activityManager.getRunningTasks(100);
        if (list != null) {
            for (ActivityManager.RunningTaskInfo runningTaskInfo : list) {
                if (runningTaskInfo.topActivity.getPackageName().equals(packageName)) {
                    activityManager.moveTaskToFront(runningTaskInfo.id, ActivityManager.MOVE_TASK_WITH_HOME);
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 判断AirDroid是否运行
     */
    public static boolean isAirDroidRunning() {
        if (!isRoot()) {
            return false;
        }

        ShellUtils.CommandResult commandResult = ShellUtils.execCmd("ps | grep com.sand.airdroid"
                , true, true);
        if (commandResult.result == 0 && !TextUtils.isEmpty(commandResult.successMsg)) {
            return true;
        }

        LogFileUtil.e(TAG, "AirDroid is not running");
        return false;
    }

    /**
     * 判断AirDroid是否安装
     */
    public static boolean isAirDroidInstalled(Context context) {
        PackageManager localPackageManager = context.getPackageManager();
        List installedPackages = localPackageManager.getInstalledPackages(0);
        for (int i = 0; i < installedPackages.size(); i++) {
            PackageInfo packageInfo = (PackageInfo) installedPackages.get(i);
            String packageName = packageInfo.packageName.split(":")[0];
            if ("com.sand.airdroid".equals(packageName)) {
                return true;
            }
        }
        LogFileUtil.e(TAG, "AirDroid is not installed");
        return false;
    }

    /**
     * 启动AirDroid
     */
    public static boolean startAirDroid() {
        if (!isRoot()) {
            return false;
        }

        ShellUtils.CommandResult commandResult = ShellUtils.execCmd("am start -n com.sand" +
                ".airdroid/com.sand.airdroid.ui.splash.SplashActivity_", true, true);
        if (commandResult.result == 0) {
            LogFileUtil.i(TAG, "AirDroid start succeed");
            return true;
        }

        LogFileUtil.e(TAG, "AirDroid start failed");
        return false;
    }

    public static boolean isRoot() {
        return RootUtil.checkRoot() && RootUtil.requestRootPermission();
    }

    public static void addDockerIpRoute() {
        String[] commands = {"ip rule add pref 1 from all lookup main",
                "ip rule add pref 2 from all lookup default",
                "ip route add default via " + GATEWAY_IP + " dev wlan0",
                "ip rule add from all lookup main pref 30000"};
        execCommand(commands, true);
    }

    public static void removeDockerIpRoute() {
        String[] commands = {"ip rule del pref 1 from all lookup main",
                "ip rule del pref 2 from all lookup default",
                "ip route del default via " + GATEWAY_IP + " dev wlan0",
                "ip rule del from all lookup main pref 30000"};
        execCommand(commands, true);
    }

    public static void screenLock() {
        String[] commands = {"input keyevent 26"};
        execCommand(commands, false);
    }

    private static void execCommand(String[] commands, boolean printLog) {
        for (String command : commands) {
            ShellUtils.CommandResult result = ShellUtils.execCmd(command, true, true);
            if (printLog) {
                LogFileUtil.e(TAG, command + ", result: " + result.result
                        + ", successMsg: " + result.successMsg
                        + ", errorMsg: " + result.errorMsg);
            }
        }
    }

    public static String getTempFileDir() {
        return MyApplication.getInstance().getExternalFilesDir(null).getPath();
    }

    public static void wav2Amr(String inFile, String outFile, RxFFmpegResultListener resultListener) throws Exception {
//        String command = "ffmpeg -i " + inFile + " -ar 8000 -ab 12.2k " + outFile;
        String command = "ffmpeg -y -i " + inFile + " -ar 8000 -ab 12.2k -ac 1 -f amr " + outFile;
        //开始执行FFmpeg命令
//        RxFFmpegInvoke.getInstance().setDebug(true);
        RxFFmpegInvoke.getInstance()
                .runCommandRxJava(command.split(" "))
                .subscribe(new RxFFmpegSubscriber() {
                    @Override
                    public void onFinish() {
                        if (resultListener != null) {
                            resultListener.onSucceed();
                        }
                    }

                    @Override
                    public void onProgress(int progress, long progressTime) {

                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(String message) {
                        if (resultListener != null) {
                            resultListener.onError(message);
                        }
                    }
                });
    }

    public static interface RxFFmpegResultListener {
        void onSucceed();

        void onError(String message);
    }
}
