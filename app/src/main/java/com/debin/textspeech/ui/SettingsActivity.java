package com.debin.textspeech.ui;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.debin.textspeech.R;
import com.debin.textspeech.util.PrefUtil;

import androidx.annotation.Nullable;

public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceManager().setSharedPreferencesName(PrefUtil.PREF_NAME);
        addPreferencesFromResource(R.xml.settings_preferences);
    }
}
