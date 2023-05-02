package com.lisa.carpartner.host.utils.voice.stt;

import com.iflytek.cloud.SpeechConstant;

public class SoundToTextConfig {
    //设置语法ID和 SUBJECT 为空，以免因之前有语法调用而设置了此参数；或直接清空所有参数，具体可参考 DEMO 的示例。
    public String CLOUD_GRAMMAR = null;
    public String SUBJECT = null;

    //设置返回结果格式，目前支持json,xml以及plain 三种格式，其中plain为纯听写文本内容
    public String RESULT_TYPE = "plain";

    //此处engineType为“cloud”
    public String ENGINE_TYPE = SpeechConstant.TYPE_CLOUD;

    //设置语音输入语言，zh_cn为简体中文
    public String LANGUAGE = "zh_cn";

    //设置结果返回语言
    public String ACCENT = "mandarin";

    // 设置语音前端点:静音超时时间，单位ms，即用户多长时间不说话则当做超时处理
    //取值范围{1000～10000}
    public String VAD_BOS = "4000";

    //设置语音后端点:后端点静音检测时间，单位ms，即用户停止说话多长时间内即认为不再输入，
    //自动停止录音，范围{0~10000}
    public String VAD_EOS = "1000";

    //设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
    public String ASR_PTT = "1";

    public String NET_TIMEOUT = "10000";
}
