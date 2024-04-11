package com.debin.textspeech;

import android.app.Application;

import com.debin.textspeech.util.LogFileUtil;
import com.debin.textspeech.util.PrefUtil;
import com.debin.textspeech.util.ThreadUtils;
import com.debin.textspeech.util.ToastUtil;

public class MyApplication extends Application {
    private static final String TAG = MyApplication.class.getSimpleName();

    //是否是调试模式
    public static final boolean debug = true;

    //是否打印调试日志
    public static final boolean PRINT_DEBUG_LOG = true;

    //是否给予了权限
    private static boolean permissionGranted;

    //Application实例
    private static MyApplication mInstance;

    private PrefUtil prefUtil;

    private ToastUtil toastUtil;

    private boolean captioning;

    private long fileLength;

    private long readLength;

    private CaptionResultListener captionResultListener;

    public static MyApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        prefUtil = new PrefUtil(this);
        toastUtil = new ToastUtil(this);
        LogFileUtil.setDebug(debug);
    }

    public void onPermissionGranted() {
        permissionGranted = true;
    }

    public boolean permissionGranted() {
        return permissionGranted;
    }

    public PrefUtil getPrefUtil() {
        return prefUtil;
    }

    public ToastUtil getToastUtil() {
        return toastUtil;
    }

    public String getSubscriptionKey() {
        return prefUtil.getString(PrefUtil.SUBSCRIPTION_KEY, "");
    }

    public String getSubscriptionRegion() {
        return prefUtil.getString(PrefUtil.SUBSCRIPTION_REGION, "southeastasia");
    }

    public boolean isCaptioning() {
        return captioning;
    }

    public void setCaptioning(boolean captioning, boolean succeed, String reason, String translateResult) {
        this.captioning = captioning;
        if (captionResultListener != null) {
            ThreadUtils.runOnUiThread(() -> {
                if (captioning) {
                    captionResultListener.captionStart();
                } else {
                    captionResultListener.captionStop(succeed, reason, translateResult);
                }
            });
        }
    }

    public long getFileLength() {
        return fileLength;
    }

    public void setFileLength(long fileLength) {
        this.fileLength = fileLength;
    }

    public long getReadLength() {
        return readLength;
    }

    public void setReadLength(long readLength) {
        this.readLength = readLength;
    }

    public CaptionResultListener getCaptionResultListener() {
        return captionResultListener;
    }

    public void setCaptionResultListener(CaptionResultListener captionResultListener) {
        this.captionResultListener = captionResultListener;
    }

    public interface CaptionResultListener {
        void captionStart();

        void captionResult(String result, boolean finallyResult);

        void captionTranslateResult(String result, boolean finallyResult);

        void captionStop(boolean succeed, String reason, String translateResult);
    }
}
