package com.debin.textspeech.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.debin.textspeech.MyApplication;
import com.debin.textspeech.R;
import com.debin.textspeech.service.CaptionService;
import com.debin.textspeech.util.CommonUtil;
import com.debin.textspeech.util.DateUtils;
import com.debin.textspeech.util.PrefUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.Nullable;

public class CaptionActivity extends BaseActivity implements View.OnClickListener, MyApplication.CaptionResultListener, CompoundButton.OnCheckedChangeListener {
    private static final String TAG = CaptionActivity.class.getSimpleName();
    private static final int FILE_PICK_REQUEST_CODE = 100;
    private static final String TIME_FORMAT = "HH:mm:ss";
    private CheckBox cbZH;
    private CheckBox cbEN;
    private CheckBox cbJP;
    private CheckBox cbKR;
    private CheckBox cbRU;
    private CheckBox cbFR;
    private RadioGroup rgInputType;
    private TextView tvFileSelect;
    private ViewGroup llFile;
    private TextView tvFile;
    private CheckBox cbTranslate;
    private TextView tvCaption;
    private ScrollView svTranslate;
    private TextView tvTranslate;
    private ScrollView svResult;
    private TextView tvResult;
    private int inputType;
    private String filePath;
    private String lastCaptionMsg;
    private String lastTranslateMsg;
    private String speechResult;
    private String translateResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caption);

        cbZH = findViewById(R.id.cb_zh);
        cbEN = findViewById(R.id.cb_en);
        cbJP = findViewById(R.id.cb_jp);
        cbKR = findViewById(R.id.cb_kr);
        cbRU = findViewById(R.id.cb_ru);
        cbFR = findViewById(R.id.cb_fr);
        llFile = findViewById(R.id.ll_file);
        tvFile = findViewById(R.id.tv_file);
        cbTranslate = findViewById(R.id.cb_translate);
        tvCaption = findViewById(R.id.tv_caption);
        svTranslate = findViewById(R.id.sv_translate);
        tvTranslate = findViewById(R.id.tv_translate);
        svResult = findViewById(R.id.sv_result);
        tvResult = findViewById(R.id.tv_result);

        cbZH.setOnCheckedChangeListener(this);
        cbEN.setOnCheckedChangeListener(this);
        cbJP.setOnCheckedChangeListener(this);
        cbKR.setOnCheckedChangeListener(this);
        cbRU.setOnCheckedChangeListener(this);
        cbFR.setOnCheckedChangeListener(this);
        rgInputType = findViewById(R.id.rg_input_type);
        rgInputType.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb_microphone) {
                inputType = CaptionService.INTENT_INPUT_TYPE_MICROPHONE;
                tvFileSelect.setVisibility(View.GONE);
                llFile.setVisibility(View.GONE);
            } else if (checkedId == R.id.rb_file) {
                inputType = CaptionService.INTENT_INPUT_TYPE_FILE;
                tvFileSelect.setVisibility(View.VISIBLE);
                llFile.setVisibility(View.VISIBLE);
            }
            MyApplication.getInstance().getPrefUtil().setInt(PrefUtil.INPUT_TYPE, inputType);
        });
        tvCaption.setOnClickListener(this);
        tvFileSelect = findViewById(R.id.tv_file_select);
        tvFileSelect.setOnClickListener(this);
        findViewById(R.id.tv_save).setOnClickListener(this);

        setUpSplash();
    }

    @Override
    public void init() {
        MyApplication.getInstance().setCaptionResultListener(this);

        initSelect();
        //test
        filePath = "/sdcard/Android/data/com.debin.textspeech/files/test.mp4";
//        fileSelect();
//        inputType = CaptionService.INTENT_INPUT_TYPE_FILE;
//        rgInputType.check(R.id.rb_file);
//        startCaptionService();
//        tvFile.postDelayed(this::stopCaptionService, 10000);
//        tvFile.postDelayed(this::startCaptionService, 15000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_file_select:
                openFileSelect();
                break;
            case R.id.tv_caption:
                if (MyApplication.getInstance().isCaptioning()) {
                    stopCaptionService();
                } else {
                    startCaptionService();
                }
                break;
            case R.id.tv_save:
                saveToFile();
                break;
            default:
                break;
        }
    }

    private void initSelect() {
        PrefUtil prefUtil = MyApplication.getInstance().getPrefUtil();
        cbZH.setChecked(prefUtil.getBoolean(PrefUtil.LAUNCHER_ZH, false));
        cbEN.setChecked(prefUtil.getBoolean(PrefUtil.LAUNCHER_EN, false));
        cbJP.setChecked(prefUtil.getBoolean(PrefUtil.LAUNCHER_JP, false));
        cbKR.setChecked(prefUtil.getBoolean(PrefUtil.LAUNCHER_KR, false));
        cbRU.setChecked(prefUtil.getBoolean(PrefUtil.LAUNCHER_RU, false));
        cbFR.setChecked(prefUtil.getBoolean(PrefUtil.LAUNCHER_FR, false));
        boolean translate = prefUtil.getBoolean(PrefUtil.TRANSLATE, false);
        cbTranslate.setChecked(translate);
        translateSelectChange(translate);
        inputType = prefUtil.getInt(PrefUtil.INPUT_TYPE, CaptionService.INTENT_INPUT_TYPE_MICROPHONE);
        rgInputType.check(inputType == 1 ? R.id.rb_microphone : R.id.rb_file);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        PrefUtil prefUtil = MyApplication.getInstance().getPrefUtil();
        switch (buttonView.getId()) {
            case R.id.cb_zh:
                prefUtil.setBoolean(PrefUtil.LAUNCHER_ZH, isChecked);
                break;
            case R.id.cb_en:
                prefUtil.setBoolean(PrefUtil.LAUNCHER_EN, isChecked);
                break;
            case R.id.cb_jp:
                prefUtil.setBoolean(PrefUtil.LAUNCHER_JP, isChecked);
                break;
            case R.id.cb_kr:
                prefUtil.setBoolean(PrefUtil.LAUNCHER_KR, isChecked);
                break;
            case R.id.cb_ru:
                prefUtil.setBoolean(PrefUtil.LAUNCHER_RU, isChecked);
                break;
            case R.id.cb_fr:
                prefUtil.setBoolean(PrefUtil.LAUNCHER_FR, isChecked);
                break;
            case R.id.cb_translate:
                prefUtil.setBoolean(PrefUtil.TRANSLATE, isChecked);
                translateSelectChange(isChecked);
                break;
            default:
                break;
        }
    }

    private void translateSelectChange(boolean isChecked) {
        svTranslate.setVisibility(isChecked ? View.VISIBLE : View.GONE);
    }

    private void openFileSelect() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"audio/*", "video/*"});
        startActivityForResult(intent, FILE_PICK_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != FILE_PICK_REQUEST_CODE) {
            return;
        }
        if (resultCode != RESULT_OK) {
            return;
        }
        filePath = data.getData().getPath();
        fileSelect();
    }

    private void fileSelect() {
        tvFile.setText(filePath);
    }

    private void startCaptionService() {
        String subscriptionKey = MyApplication.getInstance().getSubscriptionKey();
        String subscriptionRegion = MyApplication.getInstance().getSubscriptionRegion();
        if (TextUtils.isEmpty(subscriptionKey) || TextUtils.isEmpty(subscriptionRegion)) {
            MyApplication.getInstance().getToastUtil().show("订阅信息未配置");
            return;
        }

        lastCaptionMsg = null;
        lastTranslateMsg = null;

        ArrayList<String> launcherList = new ArrayList<>();
        if (cbZH.isChecked()) {
            launcherList.add(PrefUtil.LAUNCHER_ZH);
        }
        if (cbEN.isChecked()) {
            launcherList.add(PrefUtil.LAUNCHER_EN);
        }
        if (cbJP.isChecked()) {
            launcherList.add(PrefUtil.LAUNCHER_JP);
        }
        if (cbKR.isChecked()) {
            launcherList.add(PrefUtil.LAUNCHER_KR);
        }
        if (cbRU.isChecked()) {
            launcherList.add(PrefUtil.LAUNCHER_RU);
        }
        if (cbFR.isChecked()) {
            launcherList.add(PrefUtil.LAUNCHER_FR);
        }

        Intent captionServiceIntent = new Intent(getApplicationContext(), CaptionService.class);
        captionServiceIntent.putExtra(CaptionService.INTENT_ACTION, CaptionService.INTENT_ACTION_START);
        captionServiceIntent.putExtra(CaptionService.INTENT_INPUT_TYPE, inputType);
        if (inputType == CaptionService.INTENT_INPUT_TYPE_FILE && TextUtils.isEmpty(filePath)) {
            MyApplication.getInstance().getToastUtil().show("请选择文件");
            return;
        }
        captionServiceIntent.putExtra(CaptionService.INTENT_FILE_PATH, filePath);
        captionServiceIntent.putStringArrayListExtra(CaptionService.INTENT_LAUNCHER_LIST, launcherList);
        captionServiceIntent.putExtra(CaptionService.INTENT_TRANSLATE, cbTranslate.isChecked());
        startService(captionServiceIntent);
    }

    private void stopCaptionService() {
        Intent captionServiceIntent = new Intent(getApplicationContext(), CaptionService.class);
        captionServiceIntent.putExtra(CaptionService.INTENT_ACTION, CaptionService.INTENT_ACTION_STOP);
        startService(captionServiceIntent);
    }

    @Override
    public void captionStart() {
        tvCaption.setText("运行中...");
        tvResult.setText(DateUtils.dateFormat(new Date(), TIME_FORMAT) + " 开始运行\n");
        svResult.setVisibility(View.VISIBLE);
        tvTranslate.setText("");
        speechResult = null;
        translateResult = null;
    }

    @Override
    public void captionResult(String result, boolean finallyResult) {
        if (!TextUtils.isEmpty(lastCaptionMsg)) {
            tvResult.setText(tvResult.getText().toString().replaceFirst(lastCaptionMsg, result));
        } else {
            tvResult.setText(result + "\n\n" + tvResult.getText().toString());
        }
        if (finallyResult) {
            lastCaptionMsg = null;
        } else {
            lastCaptionMsg = result;
        }
        if (MyApplication.PRINT_DEBUG_LOG) {
            Log.i(CaptionActivity.TAG, tvResult.getText().toString());
        }
    }

    @Override
    public void captionTranslateResult(String result, boolean finallyResult) {
        if (!TextUtils.isEmpty(lastTranslateMsg)) {
            tvTranslate.setText(tvTranslate.getText().toString().replaceFirst(lastTranslateMsg, result));
        } else {
            tvTranslate.setText(result + "\n\n" + tvTranslate.getText().toString());
        }
        if (finallyResult) {
            lastTranslateMsg = null;
        } else {
            lastTranslateMsg = result;
        }
        if (MyApplication.PRINT_DEBUG_LOG) {
            Log.i(CaptionActivity.TAG, tvTranslate.getText().toString());
        }
    }

    @Override
    public void captionStop(boolean succeed, String reason, String translateResult) {
        tvCaption.setText("转文字");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(DateUtils.dateFormat(new Date(), TIME_FORMAT)).append(" ");
        stringBuilder.append(succeed ? "完成" : "失败：");
        if (!succeed) {
            stringBuilder.append(reason).append("\n");
        } else {
            stringBuilder.append("\n").append(reason);
            speechResult = reason;
            this.translateResult = translateResult;
        }
        tvResult.setText(stringBuilder.toString());
        svResult.fullScroll(View.FOCUS_DOWN);
        if (MyApplication.PRINT_DEBUG_LOG) {
            Log.i(TAG, "captionStop " + stringBuilder.toString());
        }

        if (!TextUtils.isEmpty(translateResult)) {
            tvTranslate.setText(translateResult);
            svTranslate.fullScroll(View.FOCUS_DOWN);
            if (MyApplication.PRINT_DEBUG_LOG) {
                Log.i(TAG, "captionStop " + translateResult);
            }
        }
    }

    private void saveToFile() {
        if (MyApplication.getInstance().isCaptioning()) {
            MyApplication.getInstance().getToastUtil().show("正在执行转语言任务");
            return;
        }

        if (TextUtils.isEmpty(speechResult)) {
            MyApplication.getInstance().getToastUtil().show("无可保存内容");
            return;
        }

        boolean succeed = false;
        if (saveResult(speechResult, "startCaptionService.txt")) {
            if (!TextUtils.isEmpty(translateResult) && saveResult(translateResult, "translate.txt")) {
                succeed = true;
            } else {
                succeed = true;
            }
        }
        MyApplication.getInstance().getToastUtil().show("保存文件" + (succeed ? "成功" : "失败"));
    }

    private boolean saveResult(String msg, String fileName) {
        boolean result = false;
        FileOutputStream fileOutputStream = null;
        try {
            String filePath = CommonUtil.getTempFileDir() + File.separator + fileName;
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(msg.getBytes());
            fileOutputStream.flush();
            result = true;
            if (MyApplication.PRINT_DEBUG_LOG) {
                Log.i(TAG, "save to file succeed: " + filePath);
            }
        } catch (Exception e) {
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (Exception e) {
                }
            }
        }
        return result;
    }
}
