package com.debin.textspeech.util;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;


public class ToastUtil {
    private Context mContext;
    private Toast toast = null;

    public ToastUtil(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public void show(String text) {
        if (text != null && !text.trim().equals("")) {
            if (this.toast == null) {
                if (this.mContext == null || this.mContext instanceof Activity && ((Activity) this.mContext).isFinishing()) {
                    Log.i("show", "isFinishing");
                    return;
                }

                this.toast = Toast.makeText(this.mContext, text, Toast.LENGTH_SHORT);
            } else {
                this.toast.setText(text);
                this.toast.setDuration(Toast.LENGTH_SHORT);
            }

            this.toast.show();
        }
    }

    public void show(String text, int duration) {
        if (text != null && !text.trim().equals("")) {
            if (this.toast == null) {
                if (this.mContext == null || this.mContext instanceof Activity && ((Activity) this.mContext).isFinishing()) {
                    return;
                }

                this.toast = Toast.makeText(this.mContext, text, duration);
            } else {
                this.toast.setText(text);
                this.toast.setDuration(duration);
            }

            this.toast.show();
        }
    }

    public void show(int resTextId) {
        String text = this.mContext.getResources().getString(resTextId);
        if (!text.isEmpty()) {
            if (this.toast == null) {
                if (this.mContext == null || this.mContext instanceof Activity && ((Activity) this.mContext).isFinishing()) {
                    return;
                }

                this.toast = Toast.makeText(this.mContext, text, Toast.LENGTH_SHORT);
            } else {
                this.toast.setText(text);
                this.toast.setDuration(Toast.LENGTH_SHORT);
            }

            this.toast.show();
        }
    }
}
