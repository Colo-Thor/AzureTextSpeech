package com.debin.textspeech.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaMetadataRetriever;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.debin.textspeech.MyApplication;
import com.debin.textspeech.R;
import com.debin.textspeech.bean.SpeekItem;
import com.debin.textspeech.receiver.NetworkChangeReceiver;
import com.debin.textspeech.util.CommonUtil;
import com.debin.textspeech.util.DateUtils;
import com.debin.textspeech.util.LogFileUtil;
import com.debin.textspeech.util.PrefUtil;
import com.debin.textspeech.util.ShellUtils;
import com.debin.textspeech.util.SpeekUtil;
import com.debin.textspeech.util.ThreadUtils;
import com.microsoft.cognitiveservices.speech.CancellationReason;
import com.microsoft.cognitiveservices.speech.ResultReason;
import com.microsoft.cognitiveservices.speech.SpeechConfig;
import com.microsoft.cognitiveservices.speech.SpeechSynthesisCancellationDetails;
import com.microsoft.cognitiveservices.speech.SpeechSynthesisResult;
import com.microsoft.cognitiveservices.speech.SpeechSynthesizer;
import com.microsoft.cognitiveservices.speech.audio.AudioConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends BaseActivity implements NetworkChangeReceiver.NetworkStateListener, View.OnClickListener {
    // 网络状态监听
    private NetworkChangeReceiver networkChangeReceiver;

    private static final String[] STYLE_NAME_ARR = {"affectionate", "angry", "assistant", "calm", "chat", "cheerful", "customerservice", "depressed", "disgruntled", "embarrassed", "empathetic", "envious", "fearful", "gentle", "lyrical", "narration-professional", "narration-relaxed", "newscast", "newscast-casual", "newscast-formal", "sad", "serious", "shouting", "advertisement-upbeat", "sports-commentary", "sports-commentary-excited", "whispering", "terrified", "unfriendly"};
    private static final String[] STYLE_DESC_ARR = {"以较高的音调和音量表达温暖而亲切的语气", "表达生气和厌恶的语气", "热情而轻松的语气", "以沉着冷静的态度说话", "表达轻松随意的语气", "表达积极愉快的语气", "友好热情的语气", "调低音调和音量来表达忧郁、沮丧的语气", "表达轻蔑和抱怨的语气", "在说话者感到不舒适时表达不确定、犹豫的语气", "表达关心和理解", "当你渴望别人拥有的东西时，表达一种钦佩的语气", "以较高的音调、较高的音量和较快的语速来表达恐惧、紧张的语气", "以较低的音调和音量表达温和、礼貌和愉快的语气", "以优美又带感伤的方式表达情感", "以专业、客观的语气朗读内容", "为内容阅读表达一种舒缓而悦耳的语气", "以正式专业的语气叙述新闻", "以通用、随意的语气发布一般新闻", "以正式、自信和权威的语气发布新闻", "表达悲伤语气", "表达严肃和命令的语气", "就像从遥远的地方说话或在外面说话，但能让自己清楚地听到", "用兴奋和精力充沛的语气推广产品或服务", "用轻松有趣的语气播报体育赛事", "用快速且充满活力的语气播报体育赛事精彩瞬间", "说话非常柔和，发出的声音小且温柔", "表达一种非常害怕的语气，语速快且声音颤抖。 听起来说话人处于不稳定的疯狂状态", "表达一种冷淡无情的语气"};
    private static final String[] ROLE_NAME_ARR = {"Girl", "Boy", "YoungAdultFemale", "YoungAdultMale", "OlderAdultFemale", "OlderAdultMale", "SeniorFemale", "SeniorMale"};
    private static final String[] ROLE_DESC_ARR = {"模拟女孩", "模拟男孩", "模拟年轻成年女性", "模拟年轻成年男性", "模拟年长的成年女性", "模拟年长的成年男性", "模拟老年女性", "模拟老年男性"};
    private String language;
    private String speechSynthesisVoiceName;
    private String styleName;
    private String styleDegree;
    private String roleName;
    private String volume;
    private String rate;
    private String pitch;

    private EditText etText;
    private TextView tvSpeech;
    private TextView tvWav;
    private TextView tvAmr;
    private Spinner spLanguage;
    private Spinner spVoice;
    private Spinner spStyle;
    private Spinner spStyleDegree;
    private Spinner spRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.tv_jump_caption).setOnClickListener(this);
        findViewById(R.id.tv_settings).setOnClickListener(this);
        etText = findViewById(R.id.et_text);
        tvSpeech = findViewById(R.id.tv_to_speech);
        tvSpeech.setOnClickListener(this);
        tvWav = findViewById(R.id.tv_to_wav);
        tvWav.setOnClickListener(this);
        tvAmr = findViewById(R.id.tv_to_amr);
        tvAmr.setOnClickListener(this);

        spLanguage = findViewById(R.id.sp_language);
        spVoice = findViewById(R.id.sp_voice);
        spStyle = findViewById(R.id.sp_style);
        spStyleDegree = findViewById(R.id.sp_styledegree);
        spRole = findViewById(R.id.sp_role);

        etText.setText("今天是" + DateUtils.dateFormat(new Date(), "yyyy年MM月dd日"));
        etText.setSelection(etText.getText().length());

        List<String> volumeDescList = Arrays.asList("默认", "超弱", "弱", "强", "超强");
        Spinner spVolume = findViewById(R.id.sp_volume);
        spVolume.setAdapter(new ArrayAdapter<>(MainActivity.this, R.layout.spinner_item, volumeDescList));
        spVolume.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1:
                        volume = "x-soft";
                        break;
                    case 2:
                        volume = "soft";
                        break;
                    case 3:
                        volume = "loud";
                        break;
                    case 4:
                        volume = "x-loud";
                        break;
                    default:
                        volume = "default";
                        break;
                }
                MyApplication.getInstance().getPrefUtil().setInt(PrefUtil.VOLUME_INDEX, position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spVolume.setSelection(MyApplication.getInstance().getPrefUtil().getInt(PrefUtil.VOLUME_INDEX, 0));

        List<String> rateDescList = Arrays.asList("默认", "超慢", "慢", "快", "超快");
        Spinner spRate = (Spinner) findViewById(R.id.sp_rate);
        spRate.setAdapter(new ArrayAdapter<>(MainActivity.this, R.layout.spinner_item, rateDescList));
        spRate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1:
                        rate = "x-slow";
                        break;
                    case 2:
                        rate = "slow";
                        break;
                    case 3:
                        rate = "fast";
                        break;
                    case 4:
                        rate = "x-fast";
                        break;
                    default:
                        rate = "default";
                        break;
                }
                MyApplication.getInstance().getPrefUtil().setInt(PrefUtil.RATE_INDEX, position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spRate.setSelection(MyApplication.getInstance().getPrefUtil().getInt(PrefUtil.RATE_INDEX, 0));

        List<String> pitchDescList = Arrays.asList("默认", "超慢", "慢", "快", "超快");
        Spinner spPitch = (Spinner) findViewById(R.id.sp_pitch);
        spPitch.setAdapter(new ArrayAdapter<>(MainActivity.this, R.layout.spinner_item, pitchDescList));
        spPitch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1:
                        pitch = "x-low";
                        break;
                    case 2:
                        pitch = "low";
                        break;
                    case 3:
                        pitch = "high";
                        break;
                    case 4:
                        pitch = "x-high";
                        break;
                    default:
                        pitch = "default";
                        break;
                }
                MyApplication.getInstance().getPrefUtil().setInt(PrefUtil.PITCH_INDEX, position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spPitch.setSelection(MyApplication.getInstance().getPrefUtil().getInt(PrefUtil.PITCH_INDEX, 0));

        setUpSplash();
    }

    @Override
    public void init() {
        MyApplication.getInstance().onPermissionGranted();
        registerNetworkStateListener(true);

        boolean parseLanguageList = false;
        try {
            List<String> languageList = SpeekUtil.getLanguageList();
            if (languageList != null && languageList.size() > 0) {
                parseLanguageList(languageList);
                parseLanguageList = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogFileUtil.e(CommonUtil.TAG, "parseLanguageList error", e);
            MyApplication.getInstance().getToastUtil().show("解析语音配置错误");
        }

        if (!parseLanguageList) {
            speechSynthesisVoiceName = MyApplication.getInstance().getPrefUtil().getString(PrefUtil.VOICE_NAME, "zh-CN-YunxiNeural");
        }

        //test
//        tvSpeech.postDelayed(() -> tvAmr.performClick(), 3000);
//        jumpToCaption();
//        tvWav.postDelayed(() -> findViewById(R.id.tv_settings).performClick(), 2000);
    }

    private void parseLanguageList(List<String> languageList) {
        spLanguage.setAdapter(new ArrayAdapter<>(MainActivity.this, R.layout.spinner_item, languageList));
        spLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                language = languageList.get(position);
                languageChange(language);
                MyApplication.getInstance().getPrefUtil().setInt(PrefUtil.LANGUAGE_INDEX, position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        int languageIndex = MyApplication.getInstance().getPrefUtil().getInt(PrefUtil.LANGUAGE_INDEX, 2);
        if (languageList.size() - 1 < languageIndex) {
            languageIndex = 0;
        }
        spLanguage.setSelection(languageIndex);
    }

    private void languageChange(String language) {
        SpeekItem speekItem = null;
        try {
            speekItem = SpeekUtil.getSpeekItem(language);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (speekItem == null) {
            MyApplication.getInstance().getToastUtil().show("语音信息解析失败");
            spVoice.setAdapter(new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, Collections.singletonList("")));
            spVoice.setSelection(0);
            spVoice.setEnabled(false);
            speechSynthesisVoiceName = MyApplication.getInstance().getPrefUtil().getString(PrefUtil.VOICE_NAME, "zh-CN-YunxiNeural");
            return;
        }

        final SpeekItem speekItemTemp = speekItem;
        spVoice.setAdapter(new ArrayAdapter<>(MainActivity.this, R.layout.spinner_item, speekItem.getLocalNameList()));
        spVoice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                speechSynthesisVoiceName = speekItemTemp.getShortNameList().get(position);
                voiceIndexChange();
                MyApplication.getInstance().getPrefUtil().setInt(PrefUtil.VOICE_INDEX, position);
                MyApplication.getInstance().getPrefUtil().setString(PrefUtil.VOICE_NAME, speechSynthesisVoiceName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spVoice.setEnabled(true);

        int index = MyApplication.getInstance().getPrefUtil().getInt(PrefUtil.VOICE_INDEX, 11);
        String name = MyApplication.getInstance().getPrefUtil().getString(PrefUtil.VOICE_NAME, "zh-CN-YunxiNeural");
        if (speekItem.getShortNameList().contains(name) && speekItem.getShortNameList().indexOf(name) == index) {
            spVoice.setSelection(index);
        } else {
            spVoice.setSelection(0);
        }
    }

    private void voiceIndexChange() {
        String[] styleNameArr = {}, roleNameArr = {};
        if (speechSynthesisVoiceName.equals("zh-CN-XiaoxiaoMultilingualNeural")) {
            styleNameArr = new String[]{"", "calm", "fearful", "cheerful", "disgruntled", "serious", "angry", "sad", "embarrassed", "affectionate", "gentle", "serious"};
        } else if (speechSynthesisVoiceName.equals("zh-CN-XiaohanNeural")) {
            styleNameArr = new String[]{"", "calm", "fearful", "cheerful", "disgruntled", "serious", "angry", "sad", "embarrassed", "affectionate", "gentle", "serious"};
        } else if (speechSynthesisVoiceName.equals("zh-CN-XiaomengNeural")) {
            styleNameArr = new String[]{"", "chat"};
        } else if (speechSynthesisVoiceName.equals("zh-CN-XiaomoNeural")) {
            styleNameArr = new String[]{"", "embarrassed", "calm", "fearful", "cheerful", "disgruntled", "serious", "angry", "sad", "depressed", "affectionate", "gentle", "envious"};
            roleNameArr = new String[]{"", "YoungAdultFemale", "YoungAdultMale", "OlderAdultFemale", "OlderAdultMale", "SeniorFemale", "SeniorMale", "Girl", "Boy"};
        } else if (speechSynthesisVoiceName.equals("zh-CN-XiaoruiNeural")) {
            styleNameArr = new String[]{"", "calm", "fearful", "angry", "sad"};
        } else if (speechSynthesisVoiceName.equals("zh-CN-XiaoshuangNeural")) {
            styleNameArr = new String[]{"", "chat"};
        } else if (speechSynthesisVoiceName.equals("zh-CN-XiaoxiaoNeural")) {
            styleNameArr = new String[]{"", "assistant", "chat", "customerservice", "newscast", "affectionate", "angry", "calm", "cheerful", "disgruntled", "fearful", "gentle", "lyrical", "sad", "serious", "friendly", "poetry-reading"};
        } else if (speechSynthesisVoiceName.equals("zh-CN-XiaoxuanNeural")) {
            styleNameArr = new String[]{"", "calm", "fearful", "cheerful", "disgruntled", "serious", "angry", "gentle", "depressed"};
            roleNameArr = new String[]{"", "YoungAdultFemale", "YoungAdultMale", "OlderAdultFemale", "OlderAdultMale", "SeniorFemale", "SeniorMale", "Girl", "Boy"};
        } else if (speechSynthesisVoiceName.equals("zh-CN-XiaoyiNeural")) {
            styleNameArr = new String[]{"", "angry", "disgruntled", "affectionate", "cheerful", "fearful", "sad", "embarrassed", "serious", "gentle"};
        } else if (speechSynthesisVoiceName.equals("zh-CN-XiaozhenNeural")) {
            styleNameArr = new String[]{"", "angry", "disgruntled", "cheerful", "fearful", "sad", "serious"};
        } else if (speechSynthesisVoiceName.equals("zh-CN-YunfengNeural")) {
            styleNameArr = new String[]{"", "angry", "cheerful", "depressed", "disgruntled", "fearful", "sad", "serious"};
        } else if (speechSynthesisVoiceName.equals("zh-CN-YunhaoNeural")) {
            styleNameArr = new String[]{"", "advertisement-upbeat"};
        } else if (speechSynthesisVoiceName.equals("zh-CN-YunjianNeural")) {
            styleNameArr = new String[]{"", "narration-relaxed", "sports-commentary", "sports-commentary-excited"};
        } else if (speechSynthesisVoiceName.equals("zh-CN-YunxiaNeural")) {
            styleNameArr = new String[]{"", "calm", "fearful", "cheerful", "angry", "sad"};
        } else if (speechSynthesisVoiceName.equals("zh-CN-YunxiNeural")) {
            styleNameArr = new String[]{"", "narration-relaxed", "embarrassed", "fearful", "cheerful", "disgruntled", "serious", "angry", "sad", "depressed", "chat", "assistant", "newscast"};
            roleNameArr = new String[]{"", "Narrator", "YoungAdultMale", "Boy"};
        } else if (speechSynthesisVoiceName.equals("zh-CN-YunyangNeural")) {
            styleNameArr = new String[]{"", "customerservice", "narration-professional", "newscast-casual"};
        } else if (speechSynthesisVoiceName.equals("zh-CN-YunyeNeural")) {
            styleNameArr = new String[]{"", "embarrassed", "calm", "fearful", "cheerful", "disgruntled", "serious", "angry", "sad"};
            roleNameArr = new String[]{"", "YoungAdultFemale", "YoungAdultMale", "OlderAdultFemale", "OlderAdultMale", "SeniorFemale", "SeniorMale", "Girl", "Boy"};
        } else if (speechSynthesisVoiceName.equals("zh-CN-YunzeNeural")) {
            styleNameArr = new String[]{"", "calm", "fearful", "cheerful", "disgruntled", "serious", "angry", "sad", "depressed", "documentary-narration"};
            roleNameArr = new String[]{"", "OlderAdultMale", "SeniorMale"};
        } else if (language.contains("中文")) {
            styleNameArr = new String[]{};
            roleNameArr = new String[]{};
        } else {
            styleNameArr = new String[]{"", "affectionate", "angry", "assistant", "calm", "chat", "cheerful", "customerservice", "depressed", "disgruntled", "embarrassed", "empathetic", "envious", "fearful", "gentle", "lyrical", "narration-professional", "narration-relaxed", "newscast", "newscast-casual", "newscast-formal", "sad", "serious", "shouting", "advertisement-upbeat", "gentle", "sports-commentary", "sports-commentary-excited", "whispering", "terrified", "unfriendly"};
            roleNameArr = new String[]{"", "YoungAdultFemale", "YoungAdultMale", "OlderAdultFemale", "OlderAdultMale", "SeniorFemale", "SeniorMale", "Girl", "Boy"};
        }

        if (styleNameArr.length <= 0) {
            spStyle.setAdapter(new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, Collections.singletonList("")));
            spStyle.setSelection(0);
            spStyle.setEnabled(false);
            styleName = "";

            spStyleDegree.setAdapter(new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, Collections.singletonList("")));
            spStyleDegree.setSelection(0);
            spStyleDegree.setEnabled(false);
            styleDegree = "1";
        } else {
            List<String> styleNameList = Arrays.asList(styleNameArr);
            List<String> styleDescList = new ArrayList<>();
            for (int i = 0; i < styleNameArr.length; i++) {
                if (i == 0) {
                    styleDescList.add("无");
                    continue;
                }
                for (int j = 0; j < STYLE_NAME_ARR.length; j++) {
                    if (styleNameArr[i].equals(STYLE_NAME_ARR[j])) {
                        styleDescList.add(STYLE_DESC_ARR[j]);
                        break;
                    }
                }
            }

            spStyle.setAdapter(new ArrayAdapter<>(MainActivity.this, R.layout.spinner_item, styleDescList));
            spStyle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    styleName = styleNameList.get(position);
                    if (position > 0) {
                        spStyleDegree.setEnabled(true);
                    }
                    MyApplication.getInstance().getPrefUtil().setInt(PrefUtil.STYLE_INDEX, position);
                    MyApplication.getInstance().getPrefUtil().setString(PrefUtil.STYLE_NAME, styleName);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            spStyle.setEnabled(true);

            int styleIndex = MyApplication.getInstance().getPrefUtil().getInt(PrefUtil.STYLE_INDEX, 0);
            String styleName = MyApplication.getInstance().getPrefUtil().getString(PrefUtil.STYLE_NAME, "");
            if (styleNameList.size() - 1 > styleIndex && styleNameList.get(styleIndex).equals(styleName)) {
                MainActivity.this.styleName = styleName;
                spStyle.setSelection(styleIndex);
            } else {
                spStyle.setSelection(0);
            }

            List<String> styleDegreeList = Arrays.asList("默认", "弱", "强", "超强");
            spStyleDegree.setAdapter(new ArrayAdapter<>(MainActivity.this, R.layout.spinner_item, styleDegreeList));
            spStyleDegree.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 1:
                            styleDegree = "0.5";
                            break;
                        case 2:
                            styleDegree = "1.5";
                            break;
                        case 3:
                            styleDegree = "2";
                            break;
                        default:
                            styleDegree = "1";
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            spStyleDegree.setSelection(0);
            spStyleDegree.setEnabled(true);
        }

        if (roleNameArr.length <= 0) {
            spRole.setAdapter(new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, Collections.singletonList("")));
            spRole.setSelection(0);
            spRole.setEnabled(false);
            roleName = "";
        } else {
            List<String> roleNameList = Arrays.asList(roleNameArr);
            List<String> roleDescList = new ArrayList<>();
            for (int i = 0; i < roleNameArr.length; i++) {
                if (i == 0) {
                    roleDescList.add("无");
                    continue;
                }
                for (int j = 0; j < ROLE_NAME_ARR.length; j++) {
                    if (roleNameArr[i].equals(ROLE_NAME_ARR[j])) {
                        roleDescList.add(ROLE_DESC_ARR[j]);
                        break;
                    }
                }
            }

            spRole.setAdapter(new ArrayAdapter<>(MainActivity.this, R.layout.spinner_item, roleDescList));
            spRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    roleName = roleNameList.get(position);
                    MyApplication.getInstance().getPrefUtil().setInt(PrefUtil.ROLE_INDEX, position);
                    MyApplication.getInstance().getPrefUtil().setString(PrefUtil.ROLE_NAME, roleName);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            spRole.setEnabled(true);

            int roleIndex = MyApplication.getInstance().getPrefUtil().getInt(PrefUtil.ROLE_INDEX, 0);
            String roleName = MyApplication.getInstance().getPrefUtil().getString(PrefUtil.ROLE_NAME, "");
            if (roleNameList.size() - 1 > roleIndex && roleNameList.get(roleIndex).equals(roleName)) {
                MainActivity.this.roleName = roleName;
                spRole.setSelection(roleIndex);
            } else {
                spRole.setSelection(0);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_jump_caption:
                jumpToCaption();
                break;
            case R.id.tv_settings:
                jumpToSettings();
                break;
            case R.id.tv_to_speech:
                toSpeech();
                break;
            case R.id.tv_to_wav:
                toWav();
                break;
            case R.id.tv_to_amr:
                toAmr();
                break;
            default:
                break;
        }
    }

    private void jumpToCaption() {
        startActivity(new Intent(MainActivity.this, CaptionActivity.class));
    }

    private void jumpToSettings() {
        startActivity(new Intent(MainActivity.this, SettingsActivity.class));
    }

    private void toSpeech() {
        AudioConfig audioConfig = AudioConfig.fromDefaultMicrophoneInput();
        startSpeech(tvSpeech, audioConfig, new TextSpeechResultListener() {
            @Override
            public void onSuccess() {
                MyApplication.getInstance().getToastUtil().show("播放成功");
            }

            @Override
            public void onError(String message) {
                MyApplication.getInstance().getToastUtil().show("播放失败: " + message);
            }

            @Override
            public void onCancel(String reason) {
                MyApplication.getInstance().getToastUtil().show("播放取消: " + reason);
            }
        });
    }

    private void toWav() {
        String filePath = CommonUtil.getTempFileDir() + File.separator + "output.wav";
        AudioConfig audioConfig = AudioConfig.fromWavFileInput(filePath);
        startSpeech(tvWav, audioConfig, new TextSpeechResultListener() {
            @Override
            public void onSuccess() {
                Log.i(CommonUtil.TAG, "toWav: " + filePath);
                MyApplication.getInstance().getToastUtil().show("生成成功: " + filePath);
            }

            @Override
            public void onError(String message) {
                MyApplication.getInstance().getToastUtil().show("生成失败: " + message);
            }

            @Override
            public void onCancel(String reason) {
                MyApplication.getInstance().getToastUtil().show("生成取消: " + reason);
            }
        });
    }

    private void toAmr() {
        String wavFilePath = CommonUtil.getTempFileDir() + File.separator + "output.wav";
        AudioConfig audioConfig = AudioConfig.fromWavFileInput(wavFilePath);
        startSpeech(tvAmr, audioConfig, new TextSpeechResultListener() {
            @Override
            public void onSuccess() {
                File file = new File(wavFilePath);
                if (file.exists()) {
                    wavToAmr(wavFilePath);
                } else {
                    Log.i(CommonUtil.TAG, "file not exists: " + wavFilePath);
                }

            }

            @Override
            public void onError(String message) {
                MyApplication.getInstance().getToastUtil().show("生成失败: " + message);
            }

            @Override
            public void onCancel(String reason) {
                MyApplication.getInstance().getToastUtil().show("生成取消: " + reason);
            }
        });
    }

    private void wavToAmr(String wavFilePath) {
        try {
            String amrFileName;
            String weChatFilePath = getWeChatFilePath();
            if (!TextUtils.isEmpty(weChatFilePath)) {
                amrFileName = weChatFilePath.substring(weChatFilePath.lastIndexOf(File.separator) + 1);
            } else {
                amrFileName = "output.amr";
            }

            String outfilePath = CommonUtil.getTempFileDir() + File.separator + amrFileName;
            File file = new File(outfilePath);
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();

            CommonUtil.wav2Amr(wavFilePath, outfilePath, new CommonUtil.RxFFmpegResultListener() {
                @Override
                public void onSucceed() {
                    try {
                        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                        retriever.setDataSource(outfilePath);
                        String durationStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                        long durationMs = Long.parseLong(durationStr);
                        MyApplication.getInstance().getToastUtil().show("保存arm成功，时长: " + durationMs);
                        LogFileUtil.i(CommonUtil.TAG, "wavToAmr: " + outfilePath + ", duration: " + durationMs);
                    } catch (Exception e) {
                        MyApplication.getInstance().getToastUtil().show("保存arm成功");
                        LogFileUtil.i(CommonUtil.TAG, "wavToAmr: " + outfilePath);
                    }
                    if (!TextUtils.isEmpty(weChatFilePath)) {
                        copyToWeChat(outfilePath, weChatFilePath);
                    }
                }

                @Override
                public void onError(String message) {
                    MyApplication.getInstance().getToastUtil().show("arm转码失败: " + message);
                    LogFileUtil.e(CommonUtil.TAG, "arm转码失败: " + message);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            LogFileUtil.e(CommonUtil.TAG, "arm转码错误: " + e.getMessage());
        }
    }

    private void copyToWeChat(String amrFilePath, String weChatFilePath) {
        String weChatFileDir = weChatFilePath.substring(0, weChatFilePath.lastIndexOf(File.separator)) + File.separator;

        String fileGid = null;
        String getGidCommand = "stat -c %g " + weChatFileDir;
        ShellUtils.CommandResult getGidCommandResult = ShellUtils.execCmd(getGidCommand, true);
        if (!TextUtils.isEmpty(getGidCommandResult.errorMsg)) {
            LogFileUtil.e(CommonUtil.TAG, "获取文件所有者错误: " + getGidCommandResult.errorMsg);
        } else {
            fileGid = getGidCommandResult.successMsg;
        }

        List<String> commandList = new ArrayList<>();
        commandList.add("mv -f " + amrFilePath + " " + weChatFileDir);
        if (!TextUtils.isEmpty(fileGid)) {
            commandList.add("chown " + fileGid + " " + weChatFilePath);
            commandList.add("chgrp " + fileGid + " " + weChatFilePath);
            commandList.add("chmod 600 " + weChatFilePath);
        } else {
            commandList.add("chmod 777 " + weChatFilePath);
        }
        ShellUtils.CommandResult commandResult = ShellUtils.execCmd(commandList, true);
        if (!TextUtils.isEmpty(commandResult.errorMsg)) {
            MyApplication.getInstance().getToastUtil().show("文件移动错误: " + commandResult.errorMsg);
            LogFileUtil.e(CommonUtil.TAG, "copyToWeChat error: " + commandResult.errorMsg);
            return;
        }

        LogFileUtil.i(CommonUtil.TAG, "copyToWeChat succeed: " + weChatFilePath);
    }

    private String getWeChatFilePath() {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (!clipboardManager.hasPrimaryClip()) {
            return null;
        }

        ClipData clipData = clipboardManager.getPrimaryClip();
        if (clipData == null || clipData.getItemCount() <= 0) {
            return null;
        }

        CharSequence charSequence = clipData.getItemAt(0).getText();
        if (TextUtils.isEmpty(charSequence)) {
            return null;
        }

        String text = charSequence.toString();
        if (!text.contains("com.tencent.mm") || !text.endsWith(".amr")) {
            return null;
        }

        return text;
    }

    private boolean speeching = false;

    private void startSpeech(View view, AudioConfig audioConfig, TextSpeechResultListener textSpeechResultListener) {
        String subscriptionKey = MyApplication.getInstance().getSubscriptionKey();
        String subscriptionRegion = MyApplication.getInstance().getSubscriptionRegion();
        if (TextUtils.isEmpty(subscriptionKey) || TextUtils.isEmpty(subscriptionRegion)) {
            MyApplication.getInstance().getToastUtil().show("订阅信息未配置");
            return;
        }

        if (speeching) {
            MyApplication.getInstance().getToastUtil().show("有正在进行的任务");
            return;
        }

        String text = etText.getText().toString().trim();
        if (TextUtils.isEmpty(text)) {
            MyApplication.getInstance().getToastUtil().show("请输入文本");
            return;
        }
        view.setEnabled(false);

        speeching = true;
        SpeechConfig config = SpeechConfig.fromSubscription(subscriptionKey, subscriptionRegion);

        String ssml = null;
        if (!TextUtils.isEmpty(styleName) || !TextUtils.isEmpty(roleName)
                || (!TextUtils.isEmpty(volume) && !"default".equals(volume))
                || (!TextUtils.isEmpty(rate) && !"default".equals(rate))
                || (!TextUtils.isEmpty(pitch) && !"default".equals(pitch))) {
            //https://learn.microsoft.com/zh-cn/azure/ai-services/speech-service/speech-synthesis-markup-structure
            String mstts;
            if (!TextUtils.isEmpty(styleName) || !TextUtils.isEmpty(roleName)) {
                mstts = "<mstts:express-as ";
                if (!TextUtils.isEmpty(styleName)) {
                    mstts += "style=\"" + styleName + "\" ";
                }
                if (!TextUtils.isEmpty(styleDegree)) {
                    mstts += "styledegree=\"" + styleDegree + "\" ";
                }
                if (!TextUtils.isEmpty(roleName)) {
                    mstts += "role=\"" + roleName + "\" ";
                }
                mstts += "></mstts:express-as>";
            } else {
                mstts = "";
            }

            String prosody;
            if ((!TextUtils.isEmpty(volume) && !"default".equals(volume))
                    || (!TextUtils.isEmpty(rate) && !"default".equals(rate))
                    || (!TextUtils.isEmpty(pitch) && !"default".equals(pitch))) {
                prosody = " <prosody ";
                if (!TextUtils.isEmpty(volume) && !"default".equals(volume)) {
                    prosody += "volume=\"" + volume + "\" ";
                }
                if (!TextUtils.isEmpty(rate) && !"default".equals(rate)) {
                    prosody += "rate=\"" + rate + "\" ";
                }
                if (!TextUtils.isEmpty(pitch) && !"default".equals(pitch)) {
                    prosody += "pitch=\"" + pitch + "\" ";
                }
                prosody += ">";
            } else {
                prosody = "";
            }

            text = text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
            String lang = speechSynthesisVoiceName.substring(0, speechSynthesisVoiceName.lastIndexOf("-"));
            ssml = String.format("<speak version='1.0' xmlns='http://www.w3.org/2001/10/synthesis' xmlns:mstts='http://www.w3.org/2001/mstts'  xmlns:emo='http://www.w3.org/2009/10/emotionml' xml:lang='" + lang + "'>"
                    .concat(String.format("<voice name='%s'>", speechSynthesisVoiceName))
                    .concat(mstts)
                    .concat(prosody)
                    .concat(text)
                    .concat(TextUtils.isEmpty(prosody) ? "" : "</prosody>")
                    .concat("</voice>")
                    .concat("</speak>"));
            Log.i(CommonUtil.TAG, "ssml: " + ssml);
        } else {
            // Note: the voice setting will not overwrite the voice element in input SSML.
            config.setSpeechSynthesisVoiceName(speechSynthesisVoiceName);
            Log.i(CommonUtil.TAG, "speechSynthesisVoiceName: " + speechSynthesisVoiceName);
        }
        final String textTemp = text;
        final String ssmlTemp = ssml;

        SpeechSynthesizer synthesizer = new SpeechSynthesizer(config, audioConfig);
        ThreadUtils.runOnSubThread(() -> {
            try {
                SpeechSynthesisResult result = null;
                if (!TextUtils.isEmpty(ssmlTemp)) {
                    result = synthesizer.SpeakSsmlAsync(ssmlTemp).get();
                } else {
                    result = synthesizer.SpeakTextAsync(textTemp).get();
                }

                if (result.getReason() == ResultReason.SynthesizingAudioCompleted) {
                    LogFileUtil.i(CommonUtil.TAG, "Speech synthesized for text: " + textTemp);
                    if (textSpeechResultListener != null) {
                        ThreadUtils.runOnUiThread(() -> {
                            textSpeechResultListener.onSuccess();
                        });
                    }
                } else if (result.getReason() == ResultReason.Canceled) {
                    SpeechSynthesisCancellationDetails cancellation = SpeechSynthesisCancellationDetails.fromResult(result);
                    LogFileUtil.e(CommonUtil.TAG, "CANCELED: Reason=" + cancellation.getReason());
                    if (cancellation.getReason() == CancellationReason.Error) {
                        LogFileUtil.e(CommonUtil.TAG, "CANCELED: ErrorCode=" + cancellation.getErrorCode());
                        LogFileUtil.e(CommonUtil.TAG, "CANCELED: ErrorDetails=" + cancellation.getErrorDetails());
                        LogFileUtil.e(CommonUtil.TAG, "CANCELED: Did you update the subscription info?");

                        if (textSpeechResultListener != null) {
                            ThreadUtils.runOnUiThread(() -> {
                                textSpeechResultListener.onError(cancellation.getErrorDetails());
                            });
                        }
                    } else if (cancellation.getReason() == CancellationReason.EndOfStream) {
                        if (textSpeechResultListener != null) {
                            ThreadUtils.runOnUiThread(() -> {
                                textSpeechResultListener.onCancel("流结束");
                            });
                        }
                    } else {
                        if (textSpeechResultListener != null) {
                            ThreadUtils.runOnUiThread(() -> {
                                textSpeechResultListener.onCancel("用户取消");
                            });
                        }
                    }
                }
            } catch (Exception e) {
                if (textSpeechResultListener != null) {
                    ThreadUtils.runOnUiThread(() -> {
                        textSpeechResultListener.onError(e.getMessage());
                    });
                }
            } finally {
                speeching = false;
                ThreadUtils.runOnUiThread(() -> view.setEnabled(true));
            }
        });
    }

    private interface TextSpeechResultListener {
        void onSuccess();

        void onError(String message);

        void onCancel(String reason);
    }

    @Override
    protected void onDestroy() {
        registerNetworkStateListener(false);
        super.onDestroy();
    }

    /**
     * 注册网络状态广播
     */
    private void registerNetworkStateListener(boolean register) {
        if (!permissionGranted) {
            return;
        }

        if (register && networkChangeReceiver == null) {
            networkChangeReceiver = NetworkChangeReceiver.getInstance();
            networkChangeReceiver.registerNetworkStateListener(this);
            IntentFilter intentFilter =
                    new IntentFilter(NetworkChangeReceiver.ACTION_NETWORK_CHANGED);
            registerReceiver(networkChangeReceiver, intentFilter);
        } else if (!register && networkChangeReceiver != null) {
            networkChangeReceiver.unRegisterNetworkStateListener(this);
            unregisterReceiver(networkChangeReceiver);
            networkChangeReceiver = null;
        }
    }

    @Override
    public void onNetworkStateChange() {
        boolean connectWifi = false;
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                WifiManager wifiManager =
                        (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                if (wifiInfo != null) {
                    connectWifi = true;
                }
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByDoubleClick();
        }
        return false;
    }

    private static Boolean isExit = false;

    private void exitByDoubleClick() {
        Timer tExit = null;
        if (!isExit) {
            isExit = true;
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false;//取消退出
                }
            }, 2000);// 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
        } else {
            try {
                finish();
                System.exit(0);
            } catch (Exception e) {
            }
        }
    }
}
