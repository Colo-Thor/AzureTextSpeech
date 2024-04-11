package com.debin.textspeech.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefUtil {
    private Context context;
    public static final String PREF_NAME = "config";
    public static final String SUBSCRIPTION_KEY = "subscription_key";
    public static final String SUBSCRIPTION_REGION = "subscription_region";
    public static final String LANGUAGE_INDEX = "language_index";
    public static final String VOICE_INDEX = "voice_index";
    public static final String VOICE_NAME = "voice_name";
    public static final String STYLE_INDEX = "style_index";
    public static final String STYLE_NAME = "style_name";
    public static final String ROLE_INDEX = "role_index";
    public static final String ROLE_NAME = "role_name";
    public static final String VOLUME_INDEX = "volume_index";
    public static final String RATE_INDEX = "rate_index";
    public static final String PITCH_INDEX = "pitch_index";
    public static final String LAUNCHER_ZH = "zh-CN";
    public static final String LAUNCHER_EN = "en-US";
    public static final String LAUNCHER_JP = "ja-JP";
    public static final String LAUNCHER_KR = "ko-KR";
    public static final String LAUNCHER_RU = "ru-RU";
    public static final String LAUNCHER_FR = "fr-FR";
    public static final String TRANSLATE = "translate";
    public static final String INPUT_TYPE = "input_type";

    public PrefUtil(Context ctx) {
        context = ctx.getApplicationContext();
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        return sp.getBoolean(key, defaultValue);
    }

    public void setBoolean(String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).apply();
    }

    public String getString(String key, String defaultValue) {
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        return sp.getString(key, defaultValue);
    }

    public void setString(String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        sp.edit().putString(key, value).apply();
    }

    public int getInt(String key, int defaultValue) {
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        return sp.getInt(key, defaultValue);
    }

    public void setInt(String key, int value) {
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        sp.edit().putInt(key, value).apply();
    }
}
