package com.lisa.carpartner.host;

import android.app.Application;
import android.content.Intent;
import android.media.MediaPlayer;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.VoiceWakeuper;
import com.lisa.carpartner.host.utils.Constant;
import com.lisa.carpartner.host.utils.LogUtils;

import java.io.IOException;

public class MainApp extends Application {
    private static final String TAG = MainApp.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        initIfly();
        startService(new Intent(this, MainService.class));
    }

    private void initIfly() {
        // 应用程序入口处调用,避免手机内存过小,杀死后台进程后通过历史intent进入Activity造成SpeechUtility对象为null
        // 注意：此接口在非主进程调用会返回null对象，如需在非主进程使用语音功能，请增加参数：SpeechConstant.FORCE_LOGIN+"=true"
        // 参数间使用“,”分隔。
        // 设置你申请的应用appid
        // 注意： appid 必须和下载的SDK保持一致，否则会出现10407错误
        SpeechUtility.createUtility(this, new StringBuffer()
                .append("appid=" + Constant.IFLY_APP_ID)
                .append(",")
                .append(SpeechConstant.ENGINE_MODE + "=" + SpeechConstant.MODE_MSC)
                .toString());
        VoiceWakeuper.createWakeuper(this, i -> LogUtils.d(TAG, "VoiceWakeuper on init " + i));
    }

}
