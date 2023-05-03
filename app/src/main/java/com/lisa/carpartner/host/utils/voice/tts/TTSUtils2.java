package com.lisa.carpartner.host.utils.voice.tts;

import android.media.MediaPlayer;

import com.alibaba.fastjson.JSONObject;
import com.lisa.carpartner.host.utils.voice.ring.MediaPlayerUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

public class TTSUtils2 {
    private static final String TTS_URL = "https://open.mobvoi.com/api/tts/v1";
    private static final String APPKEY = "D9DC5D2F326997E79212C4C814A5FA19";
    private static final String SECRET = "F6A0AFF690CC1443A12E269072F81DF1";
    private static String SPEAKER = "mopeiqi_meet_24k";
    private static String PATH = "/sdcard/sample.mp3";

    /**
     * 文字转语音
     */
    public static void startTextToSound(String text, TTSUtils.TTSCallback ttsCallback) {
        CloseableHttpResponse audioResponse = null;
        try {
            JSONObject params = new JSONObject();
            params.put("text", text);
            params.put("speaker", SPEAKER);
            params.put("audio_type", "mp3");
            params.put("speed", 1.0);
            // 停顿调节需要对appkey授权后才可以使用，授权前传参无效。
            params.put("symbol_sil",
                    "semi_250,exclamation_300,question_250,comma_200,stop_300,pause_150,colon_200");
            // 忽略1000字符长度限制，需要对appkey授权后才可以使用
//    params.put("ignore_limit", true);
            // 是否生成srt字幕文件，默认不开启。如果开启生成字幕，需要额外计费。生成好的srt文件地址将通过response header中的srt_address字段返回。
            params.put("gen_srt", false);
            params.put("appkey", APPKEY);
            String timestamp = System.currentTimeMillis() / 1000 + "";
            params.put("timestamp", timestamp);
            params.put("signature", SignatureUtil.getSignature(APPKEY, SECRET, timestamp));

            System.out.println(params.toJSONString());
            audioResponse = HttpClientUtil.doPostJsonStreaming(TTS_URL, params.toJSONString());
            Header firstHeader = audioResponse.getFirstHeader("Content-Type");
            if (audioResponse.getEntity().isStreaming() &&
                    !firstHeader.getValue().contains("application/json")) {
                // 下载audio文件
                InputStream input = audioResponse.getEntity().getContent();
                byte[] bytes = IOUtils.toByteArray(input);
                FileUtils.writeByteArrayToFile(new File(PATH), bytes);
            } else {
                System.out.println(EntityUtils.toString(audioResponse.getEntity(), "utf-8"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (audioResponse != null) {
                    audioResponse.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        MediaPlayerUtils.play(PATH, mp -> {
            if (ttsCallback != null) {
                ttsCallback.onCompelete();
            }
        }, (mp, what, extra) -> {
            if (ttsCallback != null) {
                ttsCallback.onCompelete();
            }
            return false;
        });
    }
}
