package com.lisa.carpartner.host.utils.voice.ring;

import android.media.MediaPlayer;

import com.lisa.carpartner.host.R;
import com.lisa.carpartner.host.utils.AppUtils;
import com.lisa.carpartner.host.utils.LogUtils;

import java.io.IOException;

public class MediaPlayerUtils {
    private static final String TAG = MediaPlayerUtils.class.getSimpleName();

    public static void playDing(MediaPlayer.OnCompletionListener onCompletionListener, MediaPlayer.OnErrorListener onErrorListener) {
        MediaPlayer mediaPlayer = MediaPlayer.create(AppUtils.getContext(), R.raw.ding);
        mediaPlayer.setOnCompletionListener(onCompletionListener);
        mediaPlayer.setOnErrorListener(onErrorListener);
        mediaPlayer.start();
    }

    public static void play(String path, MediaPlayer.OnCompletionListener onCompletionListener, MediaPlayer.OnErrorListener onErrorListener) {
        LogUtils.d(TAG, "play  " + path);
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(path);
        } catch (IOException e) {
            LogUtils.d(TAG, "play error " + e.getMessage());
            e.printStackTrace();
        }
        mediaPlayer.setOnCompletionListener(onCompletionListener);
        mediaPlayer.setOnErrorListener(onErrorListener);
        mediaPlayer.start();
    }
}
