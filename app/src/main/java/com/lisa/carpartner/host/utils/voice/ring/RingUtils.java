package com.lisa.carpartner.host.utils.voice.ring;

import android.media.MediaPlayer;

import com.lisa.carpartner.host.R;
import com.lisa.carpartner.host.utils.AppUtils;

public class RingUtils {
    public static void playDing(MediaPlayer.OnCompletionListener onCompletionListener, MediaPlayer.OnErrorListener onErrorListener) {
        MediaPlayer mediaPlayer = MediaPlayer.create(AppUtils.getContext(), R.raw.ding);
        mediaPlayer.setOnCompletionListener(onCompletionListener);
        mediaPlayer.setOnErrorListener(onErrorListener);
        mediaPlayer.start();
    }
}
