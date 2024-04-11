package com.debin.textspeech.util;

import android.content.Intent;
import com.debin.textspeech.MyApplication;
import com.debin.textspeech.ui.MainActivity;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 定时任务工具类
 */
public class ScheduledExecutorUtil {
    public static final long RETRY_SPACE_MILLIS = 5 * 60 * 1000;
    private static ScheduledExecutorService executorService;
    private volatile static boolean stop = false;
    private static ScheduledFuture<?> appForegroundFuture;
    private static ScheduledFuture<?> airDroidRunningFuture;
    private static ScheduledFuture<?> pingGatewayFuture;

    public static void start() {
        stop = false;
        if (executorService == null) {
            executorService = Executors.newScheduledThreadPool(1);
        }

        addAppForegroundMonitorScheduled();
        if (CommonUtil.isRoot()) {
            addAirDroidRunningScheduled();
        } else {
            LogFileUtil.e(CommonUtil.TAG, "device is not root");
        }
    }

    public static void destroy() {
        stop = true;
        if (executorService != null) {
            try {
                executorService.shutdown();
                executorService.awaitTermination(3, TimeUnit.SECONDS);
                executorService.shutdownNow();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 添加应用前台检查服务
     */
    public static void addAppForegroundMonitorScheduled() {
        removeAppForegroundMonitorScheduled();

        appForegroundFuture = executorService.scheduleWithFixedDelay(() -> {
            if (stop) {
                return;
            }

            //未在前台运行
            if (!CommonUtil.isApplicationForeground(MyApplication.getInstance())) {
                // 切换app在前台运行
                if (!CommonUtil.startApplicationToForeground(MyApplication.getInstance())) {
                    // 不存在前台Activity
                    Intent mainActivityIntent = new Intent(MyApplication.getInstance(),
                            MainActivity.class);
                    mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    MyApplication.getInstance().startActivity(mainActivityIntent);
                }
            }
        }, 60, 60, TimeUnit.SECONDS);
    }

    /**
     * 删除应用前台检查服务
     */
    public static void removeAppForegroundMonitorScheduled() {
        if (appForegroundFuture != null && !appForegroundFuture.isCancelled()) {
            appForegroundFuture.cancel(false);
            appForegroundFuture = null;
        }
    }

    /**
     * 添加AirDroid运行检查
     */
    public static void addAirDroidRunningScheduled() {
        removeAirDroidRunningScheduled();

        airDroidRunningFuture = executorService.scheduleWithFixedDelay(() -> {
            if (stop) {
                return;
            }

            if (!CommonUtil.isAirDroidInstalled(MyApplication.getInstance())) {
                // AirDroid未安装
                removeAirDroidRunningScheduled();
                return;
            }

            if (!CommonUtil.isAirDroidRunning()) {
                CommonUtil.startAirDroid();
            }
        }, 0, 2, TimeUnit.MINUTES);
    }

    /**
     * 删除AirDroid运行检查
     */
    public static void removeAirDroidRunningScheduled() {
        if (airDroidRunningFuture != null && !airDroidRunningFuture.isCancelled()) {
            airDroidRunningFuture.cancel(false);
            airDroidRunningFuture = null;
        }
    }

    /**
     * 添加ping网关任务
     */
    public static void addPingGatewayScheduled() {
        removePingGatewayScheduled();

        pingGatewayFuture = executorService.scheduleWithFixedDelay(() -> {
            if (stop) {
                return;
            }

            LogFileUtil.e(CommonUtil.TAG, "pingGatewayFuture start");

            if (PingUtil.getPacketLossRate(CommonUtil.GATEWAY_IP, 4) == 0) {
                CommonUtil.addDockerIpRoute();
                removePingGatewayScheduled();
            }
        }, 0, 15, TimeUnit.SECONDS);
    }

    /**
     * 删除ping网关任务
     */
    public static void removePingGatewayScheduled() {
        if (pingGatewayFuture != null && !pingGatewayFuture.isCancelled()) {
            pingGatewayFuture.cancel(false);
            pingGatewayFuture = null;
        }
    }

}
