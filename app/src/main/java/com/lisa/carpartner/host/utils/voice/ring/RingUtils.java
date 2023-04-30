package com.lisa.carpartner.host.utils.voice.ring;

import android.media.MediaPlayer;

import com.lisa.carpartner.host.ConversationManager;
import com.lisa.carpartner.host.R;
import com.lisa.carpartner.host.utils.AppUtils;
import com.lisa.carpartner.host.utils.voice.stt.STTUtils;

public class RingUtils {
    public static void playDing(MediaPlayer.OnCompletionListener onCompletionListener) {
        MediaPlayer mediaPlayer = MediaPlayer.create(AppUtils.getContext(), R.raw.ding);
        mediaPlayer.setOnCompletionListener(onCompletionListener);
        mediaPlayer.start();
    }
}
