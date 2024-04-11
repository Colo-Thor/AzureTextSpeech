package com.debin.textspeech.service;

import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.debin.textspeech.MyApplication;
import com.debin.textspeech.R;
import com.debin.textspeech.util.BinaryAudioStreamReader;
import com.debin.textspeech.util.ThreadUtils;
import com.microsoft.cognitiveservices.speech.AutoDetectSourceLanguageConfig;
import com.microsoft.cognitiveservices.speech.NoMatchDetails;
import com.microsoft.cognitiveservices.speech.ProfanityOption;
import com.microsoft.cognitiveservices.speech.PropertyId;
import com.microsoft.cognitiveservices.speech.RecognitionResult;
import com.microsoft.cognitiveservices.speech.Recognizer;
import com.microsoft.cognitiveservices.speech.ResultReason;
import com.microsoft.cognitiveservices.speech.SpeechConfig;
import com.microsoft.cognitiveservices.speech.SpeechRecognizer;
import com.microsoft.cognitiveservices.speech.audio.AudioConfig;
import com.microsoft.cognitiveservices.speech.audio.AudioInputStream;
import com.microsoft.cognitiveservices.speech.audio.AudioStreamContainerFormat;
import com.microsoft.cognitiveservices.speech.audio.AudioStreamFormat;
import com.microsoft.cognitiveservices.speech.audio.PullAudioInputStream;
import com.microsoft.cognitiveservices.speech.translation.SpeechTranslationConfig;
import com.microsoft.cognitiveservices.speech.translation.TranslationRecognitionResult;
import com.microsoft.cognitiveservices.speech.translation.TranslationRecognizer;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import androidx.annotation.Nullable;

public class CaptionService extends ForegroundService {
    private static final String TAG = CaptionService.class.getSimpleName();
    public static final String INTENT_ACTION = "intent_action";
    public static final int INTENT_ACTION_START = 1;
    public static final int INTENT_ACTION_STOP = 2;
    public static final String INTENT_INPUT_TYPE = "intent_input_type";
    public static final int INTENT_INPUT_TYPE_MICROPHONE = 1;
    public static final int INTENT_INPUT_TYPE_FILE = 2;
    public static final String INTENT_FILE_PATH = "intent_file_path";
    public static final String INTENT_LAUNCHER_LIST = "intent_launcher_list";
    public static final String INTENT_TRANSLATE = "intent_translate";
    private boolean needStop;
    private boolean hadReqStop;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            int action = intent.getIntExtra(INTENT_ACTION, INTENT_ACTION_START);
            if (action == INTENT_ACTION_START) {
                stop = false;
                needStop = false;
                int inputType = intent.getIntExtra(INTENT_INPUT_TYPE, INTENT_INPUT_TYPE_MICROPHONE);
                String filePath = null;
                if (inputType == INTENT_INPUT_TYPE_FILE) {
                    filePath = intent.getStringExtra(INTENT_FILE_PATH);
                    if (!fileExists(filePath)) {
                        MyApplication.getInstance().setCaptioning(false, false, "文件不存在", null);
                        return super.onStartCommand(intent, flags, startId);
                    }
                }

                List<String> launcherList = intent.getStringArrayListExtra(INTENT_LAUNCHER_LIST);
                boolean translate = intent.getBooleanExtra(INTENT_TRANSLATE, false);
                startCaption(inputType, filePath, launcherList, translate);
            } else if (action == INTENT_ACTION_STOP) {
                stop();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void startCaption(int inputType, String filePath, List<String> launcherList, boolean translate) {
        showNotification(getString(R.string.app_name), "正在运行中...");

        SpeechConfig speechConfig = null;
        AudioConfig audioConfig = null;
        Recognizer recognizer = null;
        try {
            MyApplication.getInstance().setCaptioning(true, false, null, null);
            speechStringBuilder = new StringBuilder();
            translateStringBuilder = new StringBuilder();

            if (MyApplication.PRINT_DEBUG_LOG) {
                Log.w(TAG, translate ? "start translate" : "start");
            }

            speechConfig = getSpeechConfig(inputType, launcherList, translate);
            audioConfig = getAudioConfig(inputType, filePath);
            if (translate) {
                TranslationRecognizer translationRecognizer = new TranslationRecognizer((SpeechTranslationConfig) speechConfig, audioConfig);
                translationRecognizer.recognizing.addEventListener((sender, eventArgs) -> {
                    onRecognitionEventArgs(eventArgs.getResult());
                });
                translationRecognizer.recognized.addEventListener((sender, eventArgs) -> {
                    onRecognitionEventArgs(eventArgs.getResult());
                });
//                translationRecognizer.canceled.addEventListener((sender, eventArgs) -> {
//                    onRecognitionEventArgs(eventArgs.getResult());
//                });
                recognizer = translationRecognizer;
            } else {
                //https://learn.microsoft.com/zh-cn/azure/ai-services/speech-service/language-support?tabs=language-identification
                AutoDetectSourceLanguageConfig detectLanguageConfig = AutoDetectSourceLanguageConfig.fromLanguages(launcherList);
                SpeechRecognizer speechRecognizer = new SpeechRecognizer(speechConfig, detectLanguageConfig, audioConfig);
                speechRecognizer.recognizing.addEventListener((sender, eventArgs) -> {
                    onRecognitionEventArgs(eventArgs.getResult());
                });
                speechRecognizer.recognized.addEventListener((sender, eventArgs) -> {
                    onRecognitionEventArgs(eventArgs.getResult());
                });
//                speechRecognizer.canceled.addEventListener((sender, eventArgs) -> {
//                    onRecognitionEventArgs(eventArgs.getResult());
//                });
                recognizer = speechRecognizer;
            }

            subThreadRun(inputType, recognizer, translate, speechConfig, audioConfig);
        } catch (Exception e) {
            e.printStackTrace();
            MyApplication.getInstance().setCaptioning(false, false, e.getMessage(), null);
            super.onDestroy();
        }
    }

    private boolean stop = false;
    private volatile long lastReceiveMillis;
    private StringBuilder speechStringBuilder;
    private StringBuilder translateStringBuilder;

    private void onRecognitionEventArgs(RecognitionResult result) {
        if (stop) {
            return;
        }

        if (result.getReason() == ResultReason.RecognizingSpeech
                || result.getReason() == ResultReason.RecognizedSpeech) {
            String text = result.getText();
            if (MyApplication.PRINT_DEBUG_LOG) {
                Log.i(TAG, text);
            }
            if (result.getReason() == ResultReason.RecognizingSpeech) {
                lastReceiveMillis = System.currentTimeMillis();
            } else {
                speechStringBuilder.append(text).append("\n");
            }
            ThreadUtils.runOnUiThread(() -> MyApplication.getInstance().getCaptionResultListener().captionResult(text, result.getReason() == ResultReason.RecognizedSpeech));
        } else if (result.getReason() == ResultReason.TranslatingSpeech
                || result.getReason() == ResultReason.TranslatedSpeech) {
            TranslationRecognitionResult translateResult = (TranslationRecognitionResult) result;
            String text = translateResult.getText();
            if (result.getReason() == ResultReason.TranslatingSpeech) {
                if (MyApplication.PRINT_DEBUG_LOG) {
                    Log.i(TAG, "translating: " + text);
                }
            } else {
                if (MyApplication.PRINT_DEBUG_LOG) {
                    Log.i(TAG, "translate: " + text);
                }
                speechStringBuilder.append(text).append("\n");
            }
            ThreadUtils.runOnUiThread(() -> MyApplication.getInstance().getCaptionResultListener().captionResult(text, result.getReason() == ResultReason.TranslatedSpeech));

            for (Map.Entry<String, String> pair : translateResult.getTranslations().entrySet()) {
                String value = pair.getValue();
                if (result.getReason() == ResultReason.TranslatingSpeech) {
                    if (MyApplication.PRINT_DEBUG_LOG) {
                        Log.i(TAG, "intoing: " + value);
                    }
                    lastReceiveMillis = System.currentTimeMillis();
                } else {
                    if (MyApplication.PRINT_DEBUG_LOG) {
                        Log.i(TAG, "into: " + value);
                    }
                    translateStringBuilder.append(value).append("\n");
                }
                ThreadUtils.runOnUiThread(() -> MyApplication.getInstance().getCaptionResultListener().captionTranslateResult(value, result.getReason() == ResultReason.TranslatedSpeech));
            }
        } else {
            if (result.getReason() == ResultReason.NoMatch) {
                NoMatchDetails noMatchDetails = NoMatchDetails.fromResult(result);
                Log.e(TAG, "failed: " + result.getReason() + ", detail: " + noMatchDetails.toString());
                MyApplication.getInstance().setCaptioning(false, false, noMatchDetails.toString(), null);
            } else if (result.getReason() == ResultReason.Canceled) {
                Log.e(TAG, "cancel: " + result.getReason());
                MyApplication.getInstance().setCaptioning(false, false, result.getReason().name(), null);
            } else {
                Log.e(TAG, "failed: " + result.getReason());
                MyApplication.getInstance().setCaptioning(false, false, result.getReason().name(), null);
            }
            stop = true;
        }
    }

    private void subThreadRun(int inputType, Recognizer recognizer, boolean translate, SpeechConfig speechConfig, AudioConfig audioConfig) {
        ThreadUtils.runOnSubThread(() -> {
            try {
                Future<Void> future;
                if (translate) {
                    future = ((TranslationRecognizer) recognizer).startContinuousRecognitionAsync();
                } else {
                    future = ((SpeechRecognizer) recognizer).startContinuousRecognitionAsync();
                }
                lastReceiveMillis = System.currentTimeMillis();
                MyApplication instance = MyApplication.getInstance();
                long waitTimeOut = inputType == INTENT_INPUT_TYPE_MICROPHONE ? 30000 : 5000;
                do {
                    if (hadReqStop) {
                        hadReqStop = false;
                        stop = true;
                    }

                    if (stop) {
                        break;
                    }

                    if (needStop) {
                        needStop = false;
                        if (translate) {
                            ((TranslationRecognizer) recognizer).stopContinuousRecognitionAsync();
                        } else {
                            ((SpeechRecognizer) recognizer).stopContinuousRecognitionAsync();
                        }
                        hadReqStop = true;
                    }

                    Thread.sleep(1000);
                } while ((inputType == INTENT_INPUT_TYPE_FILE && instance.getReadLength() < instance.getFileLength())
                        || System.currentTimeMillis() - lastReceiveMillis < waitTimeOut);
                if (MyApplication.PRINT_DEBUG_LOG) {
                    Log.w(TAG, "stop");
                }
            } catch (Exception e) {
                e.printStackTrace();
                MyApplication.getInstance().setCaptioning(false, false, e.getMessage(), null);
            } finally {
                if (MyApplication.getInstance().isCaptioning()) {
                    MyApplication.getInstance().setCaptioning(false, true, speechStringBuilder.toString(), translateStringBuilder.toString());
                }

                if (recognizer != null) {
                    try {
                        recognizer.close();
                    } catch (Exception e) {
                    }
                }
                if (speechConfig != null) {
                    try {
                        speechConfig.close();
                    } catch (Exception e) {
                    }
                }
                if (audioConfig != null) {
                    try {
                        audioConfig.close();
                    } catch (Exception e) {
                    }
                }

                super.onDestroy();
            }
        });
    }

    private AudioConfig getAudioConfig(int inputType, String filePath) throws Exception {
        if (inputType == INTENT_INPUT_TYPE_MICROPHONE) {
            return AudioConfig.fromDefaultMicrophoneInput();
        }
        AudioStreamFormat format = AudioStreamFormat.getCompressedFormat(AudioStreamContainerFormat.ANY);
        PullAudioInputStream pullAudio = AudioInputStream.createPullStream(new BinaryAudioStreamReader(filePath), format);
        return AudioConfig.fromStreamInput(pullAudio);
    }

    private SpeechConfig getSpeechConfig(int inputType, List<String> launcherList, boolean translate) {
        String subscriptionKey = MyApplication.getInstance().getSubscriptionKey();
        String subscriptionRegion = MyApplication.getInstance().getSubscriptionRegion();
        SpeechConfig speechConfig = null;
        if (translate) {
            SpeechTranslationConfig speechTranslationConfig = SpeechTranslationConfig.fromSubscription(subscriptionKey, subscriptionRegion);
            speechTranslationConfig.setSpeechRecognitionLanguage(launcherList.get(0));
            //https://learn.microsoft.com/zh-cn/azure/ai-services/speech-service/language-support?tabs=speech-translation#supported-languages
            speechTranslationConfig.addTargetLanguage("zh-Hans");
            speechConfig = speechTranslationConfig;
        } else {
            speechConfig = SpeechConfig.fromSubscription(subscriptionKey, subscriptionRegion);
        }
        speechConfig.setProperty(PropertyId.SpeechServiceConnection_InitialSilenceTimeoutMs, 3600 * 1000 + "");
        //https://learn.microsoft.com/zh-cn/azure/ai-services/speech-service/language-identification?tabs=continuous&pivots=programming-language-java
        speechConfig.setProperty(PropertyId.SpeechServiceConnection_LanguageIdMode, "Continuous");
        //https://learn.microsoft.com/zh-cn/azure/ai-services/speech-service/get-speech-recognition-results?pivots=programming-language-java
        speechConfig.requestWordLevelTimestamps();
        //https://learn.microsoft.com/zh-cn/azure/ai-services/speech-service/captioning-concepts?pivots=programming-language-java
        speechConfig.setProperty(PropertyId.SpeechServiceResponse_StablePartialResultThreshold, "5");
        if (inputType == CaptionService.INTENT_INPUT_TYPE_FILE) {
            speechConfig.setProperty(PropertyId.SpeechServiceResponse_RecognitionBackend, "offline");
        }
        speechConfig.setProfanity(ProfanityOption.Raw);
        return speechConfig;
    }

    private boolean fileExists(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            Log.e(TAG, "file not exists: " + filePath);
            MyApplication.getInstance().setCaptioning(false, false, "文件不存在", null);
            return false;
        }
        return true;
    }

    private void stop() {
        if (!MyApplication.getInstance().isCaptioning()) {
            return;
        }

        needStop = true;
        hadReqStop = false;
    }
}