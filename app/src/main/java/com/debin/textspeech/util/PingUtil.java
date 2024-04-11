package com.debin.textspeech.util;

import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PingUtil {
    private static final String TAG = PingUtil.class.getSimpleName();
    private static final String ERROR_RESULT = "ERROR_PING";

    public interface PingFloatResultListener {
        void onResult(int packetLossRate);
    }

    /**
     * 获取ping domain的丢包率，浮点型
     *
     * @param domain 需要ping的domain
     * @param count  需要ping的次数
     * @return 丢包率 如50%可得 50，返回-1表示获取失败
     */
    public static int getPacketLossRate(String domain, int count) {
        int result = -1;
        String packetLossInfo = getPacketLoss(domain, count);
        if (null != packetLossInfo) {
            try {
                Matcher matcher = Pattern.compile("\\d+").matcher(packetLossInfo);
                result = matcher.find() ? Integer.parseInt(matcher.group()) : -1;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 获取ping domain的丢包率，浮点型
     *
     * @param domain 需要ping的domain
     * @param count  需要ping的次数
     * @return 丢包率 如50%可得 50，返回-1表示获取失败
     */
    public static void getPacketLossRate(String domain, int count,
                                         PingFloatResultListener listener) {
        new Thread(() -> {
            int result = -1;
            String packetLossInfo = getPacketLoss(domain, count);
            if (null != packetLossInfo) {
                try {
                    Matcher matcher = Pattern.compile("\\d+").matcher(packetLossInfo);
                    result = matcher.find() ? Integer.parseInt(matcher.group()) : -1;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (listener != null) {
                listener.onResult(result);
            }
        }).start();
    }

    /**
     * 获取ping domain的丢包率
     *
     * @param domain 需要ping的domain
     * @param count  需要ping的次数
     * @return 丢包率 x%
     */
    public static String getPacketLoss(String domain, int count) {
        String pingString = ping(createSimplePingCommand(count, domain));
        if (!TextUtils.isEmpty(pingString)) {
            try {
                String result = null;
                if (ERROR_RESULT.equals(pingString)) {
                    result = "100";
                } else if (pingString.contains("errors")) {
                    String tempInfo = pingString.substring(pingString.indexOf("errors,"));
                    result = tempInfo.substring(7, tempInfo.indexOf("packet"));
                } else {
                    String tempInfo = pingString.substring(pingString.indexOf("received,"));
                    result = tempInfo.substring(9, tempInfo.indexOf("packet"));
                }
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static String ping(String command) {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command);

            InputStream errorStream = process.getErrorStream();
            BufferedReader errorBufferedReader =
                    new BufferedReader(new InputStreamReader(errorStream));
            String errorResult = errorBufferedReader.readLine();
            errorBufferedReader.close();
            errorStream.close();
            if (!TextUtils.isEmpty(errorResult)) {
                LogFileUtil.e(TAG, command + " -- error -- " + errorResult);
                return ERROR_RESULT;
            }

            InputStream is = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String result = null;
            String line;
            while (null != (line = reader.readLine())) {
                if (line.contains("packet loss")) {
                    result = line;
                    break;
                }
            }
            reader.close();
            is.close();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != process) {
                process.destroy();
            }
        }
        return null;
    }

    private static String createSimplePingCommand(int count, String domain) {
        return "/system/bin/ping -c " + count + " " + domain;
    }
}
