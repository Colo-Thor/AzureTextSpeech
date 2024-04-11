package com.debin.textspeech.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 判断当前网络是否可达
 */
public class NetworkAvailableUtil {

    public interface NetworkAvailableListener {
        /**
         * 网络可用
         */
        void onAvail();

        /**
         * 网络不可用
         */
        void onUnAvail();
    }

    /**
     * 异步检测网络是否可用
     */
    public static void available(NetworkAvailableListener listener) {
        HttpThread httpThread = new HttpThread(listener);
        httpThread.start();
    }

    /**
     * 同步检测网络是否可用
     */
    public static boolean available() {
        HttpThread httpThread = new HttpThread(null);
        httpThread.start();
        while (httpThread.isAlive()) {
            try {
                Thread.sleep(100);
            } catch (Exception e) {
            }
        }
        return httpThread.succeed;
    }

    private static class HttpThread extends Thread {

        private boolean succeed;
        private NetworkAvailableUtil.NetworkAvailableListener listener;

        public HttpThread(NetworkAvailableUtil.NetworkAvailableListener listener) {
            this.listener = listener;
        }

        @Override
        public void run() {
            succeed = false;
            HttpURLConnection urlConnection = null;
            try {
                //准备连接的uri
                URL url = new URL("http", "www.qualcomm.cn", "/generate_204");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setInstanceFollowRedirects(false);
                urlConnection.setConnectTimeout(10000);
                urlConnection.setReadTimeout(10000);
                urlConnection.setUseCaches(false);
                urlConnection.getInputStream();
                //获取服务器回应
                int httpResponseCode = urlConnection.getResponseCode();
                //拿到回应
                if (httpResponseCode == 204) {
                    succeed = true;
                } else if (httpResponseCode == 200 && urlConnection.getContentLength() == 0) {
                    succeed = true;
                }

                if (listener != null) {
                    if (succeed) {
                        listener.onAvail();
                    } else {
                        listener.onUnAvail();
                    }
                }
            } catch (IOException e) {
            } finally {
                if (urlConnection != null) {
                    try {
                        urlConnection.disconnect();
                    } catch (Exception e) {
                    }
                }
            }
        }
    }
}
