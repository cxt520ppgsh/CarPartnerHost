package com.lisa.carpartner.host;

import android.app.Application;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.lisa.carpartner.host.utils.Constant;

public class MainApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initIfly();
    }

    private void initIfly() {
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=" + Constant.IFLY_APP_ID);
    }

}
