package com.lisa.carpartner.host.utils.voice.tts;

import android.speech.tts.TextToSpeech;

import com.lisa.carpartner.host.utils.AppUtils;
import com.lisa.carpartner.host.utils.LogUtils;

public class TTSUtils {
    private static final String TAG = TTSUtils.class.getSimpleName();
    private static final TextToSoundConfig TEXT_TO_SOUND_CONFIG = new TextToSoundConfig();
    private static TextToSpeech tts = new TextToSpeech(AppUtils.getContext(), new TextToSpeech.OnInitListener() {
        @Override
        public void onInit(int status) {
            if (status == TextToSpeech.SUCCESS) {
                int result = tts.setLanguage(TEXT_TO_SOUND_CONFIG.LAUNGUAGE);
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    LogUtils.d(TAG, "This Language is not supported");
                }
            } else {
                LogUtils.d(TAG, "Initialization Failed!");
            }
        }
    });

    public static void startTextToSound(String text) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }
}
