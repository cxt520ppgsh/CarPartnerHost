package com.lisa.carpartner.host.utils.voice.stt;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.lisa.carpartner.host.utils.AppUtils;
import com.lisa.carpartner.host.utils.JsonParser;
import com.lisa.carpartner.host.utils.XmlParser;
import com.lisa.carpartner.host.utils.voice.LogUtils;

public class STSUtils {
    private static final String TAG = STSUtils.class.getSimpleName();
    private static final SoundToTextConfig SOUND_TO_TEXT_CONFIG = new SoundToTextConfig();

    public interface OnlineSoundToTextCallback {
        void onStart();

        void onSoundToText(String text, boolean isLast);

        void onError(SpeechError speechError);
    }

    public static void startOnlineSoundToText(OnlineSoundToTextCallback onlineSoundToTextCallback) {
        SpeechRecognizer mIat = SpeechRecognizer.createRecognizer(AppUtils.getContext(),
                i -> LogUtils.d(TAG, "SpeechRecognizer init status " + i));
        mIat.setParameter(SpeechConstant.CLOUD_GRAMMAR, SOUND_TO_TEXT_CONFIG.CLOUD_GRAMMAR);
        mIat.setParameter(SpeechConstant.SUBJECT, SOUND_TO_TEXT_CONFIG.SUBJECT);
        mIat.setParameter(SpeechConstant.RESULT_TYPE, SOUND_TO_TEXT_CONFIG.RESULT_TYPE);
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, SOUND_TO_TEXT_CONFIG.ENGINE_TYPE);
        mIat.setParameter(SpeechConstant.LANGUAGE, SOUND_TO_TEXT_CONFIG.LANGUAGE);
        mIat.setParameter(SpeechConstant.ACCENT, SOUND_TO_TEXT_CONFIG.ACCENT);
        mIat.setParameter(SpeechConstant.VAD_BOS, SOUND_TO_TEXT_CONFIG.VAD_BOS);
        mIat.setParameter(SpeechConstant.VAD_EOS, SOUND_TO_TEXT_CONFIG.VAD_EOS);
        mIat.setParameter(SpeechConstant.ASR_PTT, SOUND_TO_TEXT_CONFIG.ASR_PTT);
        mIat.startListening(new RecognizerListener() {
            private String resultText = "";

            @Override
            public void onVolumeChanged(int volume, byte[] bytes) {
                LogUtils.d(TAG, "onVolumeChanged " + volume);
            }

            @Override
            public void onBeginOfSpeech() {
                LogUtils.d(TAG, "onBeginOfSpeech ");
                if (onlineSoundToTextCallback != null) {
                    onlineSoundToTextCallback.onStart();
                }
            }

            @Override
            public void onEndOfSpeech() {
                LogUtils.d(TAG, "onEndOfSpeech");
            }

            @Override
            public void onResult(RecognizerResult result, boolean isLast) {
                if (null == result || TextUtils.isEmpty(result.getResultString())) {
                    LogUtils.d(TAG, "RecognizerResult null");
                    return;
                }
                Log.d(TAG, "recognizer result：" + result.getResultString());
                String text = "";
                if (SOUND_TO_TEXT_CONFIG.RESULT_TYPE.equals("json")) {
                    text = JsonParser.parseGrammarResult(result.getResultString(), SOUND_TO_TEXT_CONFIG.ENGINE_TYPE);
                } else if (SOUND_TO_TEXT_CONFIG.RESULT_TYPE.equals("xml")) {
                    text = XmlParser.parseNluResult(result.getResultString());
                } else {
                    text = result.getResultString();
                }
                resultText += text;
                if (onlineSoundToTextCallback != null) {
                    onlineSoundToTextCallback.onSoundToText(text, isLast);
                }
            }

            @Override
            public void onError(SpeechError speechError) {
                LogUtils.d(TAG, "SpeechError " + speechError.getErrorDescription());
                if (onlineSoundToTextCallback != null) {
                    onlineSoundToTextCallback.onError(speechError);
                }
            }

            @Override
            public void onEvent(int i, int i1, int i2, Bundle bundle) {

            }
        });
    }

}
