package com.debin.textspeech.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

/**
 * 网络状态监听
 */
public class NetworkChangeReceiver extends BroadcastReceiver {
    public final static String ACTION_NETWORK_CHANGED = "android.net.conn.CONNECTIVITY_CHANGE";

    private NetworkChangeReceiver() {

    }

    public static NetworkChangeReceiver getInstance() {
        return NetworkChangeReceiverUtil.mInstance;
    }

    private static class NetworkChangeReceiverUtil {
        public static NetworkChangeReceiver mInstance = new NetworkChangeReceiver();
    }

    private List<NetworkStateListener> mListeners = new ArrayList<>();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ACTION_NETWORK_CHANGED.equals(intent.getAction())) {
            if (mListeners != null && mListeners.size() > 0) {
                for (NetworkStateListener listener : mListeners) {
                    listener.onNetworkStateChange();
                }
            }
        }
    }

    public void registerNetworkStateListener(NetworkStateListener listener) {
        mListeners.add(listener);
    }

    public void unRegisterNetworkStateListener(NetworkStateListener listener) {
        mListeners.remove(listener);
    }

    public interface NetworkStateListener {
        void onNetworkStateChange();
    }
}

