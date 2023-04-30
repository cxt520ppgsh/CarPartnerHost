package com.lisa.carpartner.host.utils.voice.wake;

import com.iflytek.cloud.util.ResourceUtil;
import com.lisa.carpartner.host.R;
import com.lisa.carpartner.host.utils.AppUtils;
import com.lisa.carpartner.host.utils.Constant;

public class WakeConfig {
    // 唤醒门限值，根据资源携带的唤醒词个数按照“id:门限;id:门限”的格式传入
    public final int MIN_SENSITIVITY = 3000;
    public final int MAX_SENSITIVITY = 20;
    public final int SENSITIVITY = 500;
    //模式 0：关闭优化功能，禁止向服务端发送本地挑选数据；
    //模式 1：开启优化功能，允许向服务端发送本地挑选数据；需要自行管理优化数据
    public final String IVW_NET_MODE = "0";
    // 设置唤醒模式
    public final String IVW_SST = "wakeup";
    //0：单次唤醒
    //1：循环唤醒
    public final String KEEP_ALIVE = "1";
    // 设置唤醒资源路径
    public final String IVW_RES_PATH = ResourceUtil.generateResourcePath(AppUtils.getContext(), ResourceUtil.RESOURCE_TYPE.assets, "ivw/" + Constant.IFLY_APP_ID + ".jet");
    // 设置唤醒录音保存路径，保存最近一分钟的音频
    public final String IVW_AUDIO_PATH = AppUtils.getContext().getExternalFilesDir("msc").getAbsolutePath() + "/ivw.wav";
}
