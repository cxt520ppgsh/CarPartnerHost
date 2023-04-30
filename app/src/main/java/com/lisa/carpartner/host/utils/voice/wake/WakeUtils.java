package com.lisa.carpartner.host.utils.voice.wake;

import android.os.Bundle;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.VoiceWakeuper;
import com.iflytek.cloud.WakeuperListener;
import com.iflytek.cloud.WakeuperResult;
import com.lisa.carpartner.host.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class WakeUtils {
    private static final String TAG = WakeUtils.class.getSimpleName();
    private static final WakeConfig WAKE_CONFIG = new WakeConfig();
    private static VoiceWakeuper mIvw;

    public interface OnWakeupCallback {
        void onWakeUp();

        void onError(SpeechError speechError);
    }

    public static boolean isWakeUpListing() {
        LogUtils.d(TAG, "isWakeUpListing " + (mIvw != null && mIvw.isListening()));
        return mIvw != null && mIvw.isListening();
    }

    public static void stopWakeUp() {
        if (mIvw == null) return;
        LogUtils.d(TAG, "stopWakeUp ");
        mIvw.stopListening();
    }

    public static void startWakeUp(OnWakeupCallback onWakeupCallback) {
        LogUtils.d(TAG, "startWakeUp ");
        mIvw = VoiceWakeuper.getWakeuper();
        if (mIvw == null) return;
        mIvw.setParameter(SpeechConstant.PARAMS, null);
        mIvw.setParameter(SpeechConstant.IVW_THRESHOLD, "0:" + WAKE_CONFIG.SENSITIVITY);
        mIvw.setParameter(SpeechConstant.IVW_SST, WAKE_CONFIG.IVW_SST);
        mIvw.setParameter(SpeechConstant.KEEP_ALIVE, WAKE_CONFIG.KEEP_ALIVE);
        mIvw.setParameter(SpeechConstant.IVW_NET_MODE, WAKE_CONFIG.IVW_NET_MODE);
        mIvw.setParameter(SpeechConstant.IVW_RES_PATH, WAKE_CONFIG.IVW_RES_PATH);
        mIvw.setParameter(SpeechConstant.IVW_AUDIO_PATH, WAKE_CONFIG.IVW_AUDIO_PATH);
        mIvw.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIvw.startListening(new WakeuperListener() {
            String resultString = "";

            @Override
            public void onBeginOfSpeech() {
                LogUtils.d(TAG, "onBeginOfSpeech");
            }

            @Override
            public void onResult(WakeuperResult wakeuperResult) {
                try {
                    String text = wakeuperResult.getResultString();
                    JSONObject object;
                    object = new JSONObject(text);
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("【RAW】 " + text);
                    buffer.append("\n");
                    buffer.append("【操作类型】" + object.optString("sst"));
                    buffer.append("\n");
                    buffer.append("【唤醒词id】" + object.optString("id"));
                    buffer.append("\n");
                    buffer.append("【得分】" + object.optString("score"));
                    buffer.append("\n");
                    buffer.append("【前端点】" + object.optString("bos"));
                    buffer.append("\n");
                    buffer.append("【尾端点】" + object.optString("eos"));
                    resultString = buffer.toString();
                    if (onWakeupCallback != null) onWakeupCallback.onWakeUp();
                } catch (JSONException e) {
                    resultString = "结果解析出错";
                    e.printStackTrace();
                }
                LogUtils.d(TAG, "onResult " + resultString);
            }

            @Override
            public void onError(SpeechError speechError) {
                LogUtils.d(TAG, "onError " + speechError.getErrorDescription());
                if (onWakeupCallback != null) onWakeupCallback.onError(speechError);
            }

            @Override
            public void onEvent(int eventType, int isLast, int arg2, Bundle obj) {
                switch (eventType) {
                    // EVENT_RECORD_DATA 事件仅在 NOTIFY_RECORD_DATA 参数值为 真 时返回
//                    case SpeechEvent.EVENT_RECORD_DATA:
//                        final byte[] audio = obj.getByteArray(SpeechEvent.KEY_EVENT_RECORD_DATA);
//                        Log.i(TAG, "ivw audio length: " + audio.length);
//                        break;
                }
            }

            @Override
            public void onVolumeChanged(int val) {
//                LogUtils.d(TAG, "onVolumeChanged " + val);
            }
        });
    }
}
