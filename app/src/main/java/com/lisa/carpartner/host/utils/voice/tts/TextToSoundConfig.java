package com.lisa.carpartner.host.utils.voice.tts;

import android.media.AudioManager;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.util.ResourceUtil;
import com.lisa.carpartner.host.R;
import com.lisa.carpartner.host.utils.AppUtils;

public class TextToSoundConfig {
    public String ENGINE_TYPE = SpeechConstant.TYPE_XTTS;
    //设置发音人
    public String VOICER_XTTS = "xiaoyan"; //assets/xtts/*.jet
    //设置发音人资源路径
    public String TTS_RES_PATH = getResourcePath();
    //设置合成语速
    public String SPEED = "100";
    //设置合成音调
    public String PITCH = "50";
    //设置合成音量
    public String VOLUME = "50";
    //设置播放器音频流类型
    public String STREAM_TYPE = AudioManager.STREAM_MUSIC + "";
    // 设置播放合成音频打断音乐播放，默认为true
    public String KEY_REQUEST_FOCUS = "true";
    // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
    public String AUDIO_FORMAT = "wav";
    public String TTS_AUDIO_PATH = AppUtils.getContext().getExternalFilesDir("msc").getAbsolutePath() + "/tts.pcm";

    //获取发音人资源路径
    private String getResourcePath() {
        StringBuffer tempBuffer = new StringBuffer();
        String type = "xtts";
        //合成通用资源
        tempBuffer.append(ResourceUtil.generateResourcePath(AppUtils.getContext(), ResourceUtil.RESOURCE_TYPE.assets, type + "/common.jet"));
        tempBuffer.append(";");
        //发音人资源
        tempBuffer.append(ResourceUtil.generateResourcePath(AppUtils.getContext(), ResourceUtil.RESOURCE_TYPE.assets, type + "/" + VOICER_XTTS + ".jet"));
        return tempBuffer.toString();
    }
}
