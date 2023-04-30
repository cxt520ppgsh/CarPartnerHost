package com.lisa.carpartner.host;

import android.media.MediaPlayer;

import com.iflytek.cloud.SpeechError;
import com.lisa.carpartner.host.utils.AppUtils;
import com.lisa.carpartner.host.utils.voice.stt.STTUtils;
import com.lisa.carpartner.host.utils.voice.wake.WakeUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConversationManager implements WakeUtils.OnWakeupCallback, STTUtils.OnlineSoundToTextCallback {

    public Set<ConversationCallback> conversationCallbacks = new HashSet<>();

    private Content currentContent;

    private List<Content> contents = new ArrayList<>();

    public static ConversationManager getInstance() {
        return ConversationManager.Holder.instance;
    }

    public List<Content> getContents() {
        return contents;
    }

    public void startWakeUp() {
        WakeUtils.startWakeUp(this);
    }

    public void stopWakeUp() {
        WakeUtils.stopWakeUp();
    }

    public void registerConversationCallback(ConversationCallback callback) {
        conversationCallbacks.add(callback);
    }

    public void unRegisterConversationCallback(ConversationCallback callback) {
        conversationCallbacks.remove(callback);
    }

    @Override
    public void onWakeUp() {
        contents.clear();
        fireContentsUpdate();
        MediaPlayer mediaPlayer = MediaPlayer.create(AppUtils.getContext(), R.raw.ding);
        mediaPlayer.setOnCompletionListener(mp -> {
                    currentContent = new Content();
                    currentContent.speaker = Content.SPEAKER_VOICEUI;
                    currentContent.words = Content.SPEAKER_WORDS;
                    contents.add(currentContent);
                    fireContentsUpdate();
                    STTUtils.startOnlineSoundToText(ConversationManager.this);
                }
        );
        mediaPlayer.start();
    }

    @Override
    public void onStartSoundToText() {
        currentContent = new Content();
        contents.add(currentContent);
        currentContent.speaker = Content.SPEAKER_USER;
        currentContent.words = "...";
        fireContentsUpdate();
    }

    @Override
    public void onSoundToText(String text, boolean isLast) {
        currentContent.speaker = Content.SPEAKER_USER;
        currentContent.words += text;
        if (isLast) {
            fireContentsUpdate();
        }
    }

    @Override
    public void onError(SpeechError speechError) {
        currentContent = new Content();
        contents.add(currentContent);
        currentContent.speaker = Content.SPEAKER_VOICEUI;
        currentContent.words += speechError.getErrorDescription();
        fireContentsUpdate();
    }

    public interface ConversationCallback {

        void onWakeUpListeningStart();

        void onWakeUpListeningStop();

        void onConversationStart();

        void onConversationUpdate(List<Content> content);

        void onConversationEnd();
    }

    private void fireContentsUpdate() {
        for (ConversationCallback callback : conversationCallbacks) {
            if (callback == null) continue;
            callback.onConversationUpdate(contents);
        }
    }


    public static class Content {
        public static String SPEAKER_VOICEUI = "LiSA";
        public static String SPEAKER_WORDS = "请说";
        public static String SPEAKER_USER = "你";
        String speaker;
        String words;
    }

    private static class Holder {
        static ConversationManager instance = new ConversationManager();
    }

}
