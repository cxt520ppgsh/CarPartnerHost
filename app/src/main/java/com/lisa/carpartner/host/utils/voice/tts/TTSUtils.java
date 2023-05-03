package com.lisa.carpartner.host.utils.voice.tts;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechEvent;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.util.ResourceUtil;
import com.lisa.carpartner.host.utils.AppUtils;
import com.lisa.carpartner.host.utils.LogUtils;

public class TTSUtils {
    private static final String TAG = TTSUtils.class.getSimpleName();
    private static SpeechSynthesizer mTts = SpeechSynthesizer.createSynthesizer(AppUtils.getContext(), code -> Log.d(TAG, "InitListener init() code = " + code));
    private static final TextToSoundConfig TEXT_TO_SOUND_CONFIG = new TextToSoundConfig();

    public interface TTSCallback {
        void onStart();

        void onCompelete();
    }

    public static void startTextToSound(String text, TTSCallback ttsCallback) {
        setupParams();
        int code = mTts.startSpeaking(text, new SynthesizerListener() {
            @Override
            public void onSpeakBegin() {
                Log.d(TAG, "开始播放：" + System.currentTimeMillis());
                if (ttsCallback != null) ttsCallback.onStart();
            }

            @Override
            public void onSpeakPaused() {
                Log.d(TAG, "暂停播放：" + System.currentTimeMillis());
            }

            @Override
            public void onSpeakResumed() {
                Log.d(TAG, "继续播放：" + System.currentTimeMillis());
            }

            @Override
            public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
                Log.d(TAG, "onBufferProgress：" + percent);

            }

            @Override
            public void onSpeakProgress(int percent, int beginPos, int endPos) {
                Log.d(TAG, "onSpeakProgress：" + percent);
            }

            @Override
            public void onCompleted(SpeechError error) {
                if (ttsCallback != null) ttsCallback.onCompelete();
                if (error == null) {
                    Log.d(TAG, "onCompleted");
                } else {
                    Log.d(TAG, "onSpeechError：" + error.getErrorDescription());
                }
            }

            @Override
            public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
                // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
                // 若使用本地能力，会话id为null
                if (SpeechEvent.EVENT_SESSION_ID == eventType) {
                    String sid = obj.getString(SpeechEvent.KEY_EVENT_AUDIO_URL);
                    Log.d(TAG, "session id =" + sid);
                }

                //实时音频流输出参考
			/*if (SpeechEvent.EVENT_TTS_BUFFER == eventType) {
				byte[] buf = obj.getByteArray(SpeechEvent.KEY_EVENT_TTS_BUFFER);
				Log.e("MscSpeechLog", "buf is =" + buf);
			}*/
            }
        });
        if (code != ErrorCode.SUCCESS) {
            if (ttsCallback != null) ttsCallback.onCompelete();
            LogUtils.d(TAG, "startTextToSound error " + code);
        }
    }

    public static void stopSpeaking() {
        mTts.stopSpeaking();
    }

    public static void pauseSpeaking() {
        mTts.pauseSpeaking();
    }

    public static void resumeSpeaking() {
        mTts.resumeSpeaking();
    }

    private static void setupParams() {
        mTts.setParameter(SpeechConstant.PARAMS, null);
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, TEXT_TO_SOUND_CONFIG.ENGINE_TYPE);
        mTts.setParameter(ResourceUtil.TTS_RES_PATH, TEXT_TO_SOUND_CONFIG.TTS_RES_PATH);
        mTts.setParameter(SpeechConstant.VOICE_NAME, TEXT_TO_SOUND_CONFIG.VOICER_XTTS);
        //mTts.setParameter(SpeechConstant.TTS_DATA_NOTIFY,"1");//支持实时音频流抛出，仅在synthesizeToUri条件下支持
        mTts.setParameter(SpeechConstant.SPEED, TEXT_TO_SOUND_CONFIG.SPEED);
        mTts.setParameter(SpeechConstant.PITCH, TEXT_TO_SOUND_CONFIG.PITCH);
        mTts.setParameter(SpeechConstant.VOLUME, TEXT_TO_SOUND_CONFIG.VOLUME);
        mTts.setParameter(SpeechConstant.STREAM_TYPE, TEXT_TO_SOUND_CONFIG.STREAM_TYPE);
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, TEXT_TO_SOUND_CONFIG.KEY_REQUEST_FOCUS);
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, TEXT_TO_SOUND_CONFIG.AUDIO_FORMAT);
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, TEXT_TO_SOUND_CONFIG.TTS_AUDIO_PATH);
    }

}
