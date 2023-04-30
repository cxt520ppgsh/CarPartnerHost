package com.lisa.carpartner.host;

import com.iflytek.cloud.SpeechError;
import com.lisa.carpartner.host.utils.UiThreadUtils;
import com.lisa.carpartner.host.utils.voice.ring.RingUtils;
import com.lisa.carpartner.host.utils.voice.stt.STTUtils;
import com.lisa.carpartner.host.utils.voice.wake.WakeUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConversationManager implements WakeUtils.OnWakeupCallback, STTUtils.OnlineSoundToTextCallback {

    public Set<ConversationCallback> conversationCallbacks = new HashSet<>();
    private Content currentContent;
    private boolean isConversationStart = false;
    private String soundToTextResult = "";
    private static final int CONVERSATION_TIMOUNT = 5000;
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
    public void onWakeUpStartListening() {
        clearContent();
    }

    @Override
    public void onWakeUpStopListening() {
        clearContent();
    }

    @Override
    public void onWakeUp() {
        startConversation();
    }

    @Override
    public void onStartSoundToText() {
        soundToTextResult = "";
        resetConversationTimount();
        appendContent(new Content(Content.SPEAKER_USER, ""));
    }

    @Override
    public void onSoundToText(String text, boolean isLast) {
        soundToTextResult += text;
        resetConversationTimount();
        if (isLast) modifyCurrentContent(new Content(Content.SPEAKER_USER, soundToTextResult));
    }

    @Override
    public void onError(SpeechError speechError) {
        soundToTextResult = speechError.getErrorDescription();
        resetConversationTimount();
        modifyCurrentContent(new Content(Content.SPEAKER_VOICEUI, speechError.getErrorDescription()));
    }

    public interface ConversationCallback {

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

    private void fireConversationStart() {
        for (ConversationCallback callback : conversationCallbacks) {
            if (callback == null) continue;
            callback.onConversationStart();
        }
    }

    private void fireConversationEnd() {
        for (ConversationCallback callback : conversationCallbacks) {
            if (callback == null) continue;
            callback.onConversationEnd();
        }
    }

    private void resetConversationTimount() {
        UiThreadUtils.removeCallbacks(onConversationTimountRunnable);
        UiThreadUtils.postDelayed(onConversationTimountRunnable, CONVERSATION_TIMOUNT);
    }

    private void startConversation() {
        if (isConversationStart) {
            resetConversationTimount();
            return;
        }
        isConversationStart = true;
        fireConversationStart();
        resetConversationTimount();
        contents.clear();
        fireContentsUpdate();
        RingUtils.playDing(mp -> {
            currentContent = new Content();
            currentContent.speaker = Content.SPEAKER_VOICEUI;
            currentContent.words = Content.SPEAKER_WORDS;
            contents.add(currentContent);
            fireContentsUpdate();
            STTUtils.startOnlineSoundToText(ConversationManager.this);
        });
    }

    private void stopConversation() {
        if (!isConversationStart) return;
        isConversationStart = false;
        contents.clear();
        fireContentsUpdate();
        fireConversationEnd();
    }

    private void clearContent() {
        contents.clear();
        fireContentsUpdate();
    }

    private void appendContent(Content content) {
        if (content == null) return;
        if (contents.contains(content)) return;
        currentContent = content;
        contents.add(content);
        fireContentsUpdate();
    }

    private void modifyCurrentContent(Content content) {
        currentContent.speaker = content.speaker;
        currentContent.words = content.words;
        fireContentsUpdate();
    }

    private Runnable onConversationTimountRunnable = () -> {
        stopConversation();
    };

    public static class Content {
        public static String SPEAKER_VOICEUI = "LiSA";
        public static String SPEAKER_WORDS = "请说";
        public static String SPEAKER_USER = "你";
        String speaker;
        String words = "";

        private Content() {

        }

        public Content(String speaker, String words) {
            this.speaker = speaker;
            this.words = words;
        }
    }

    private static class Holder {
        static ConversationManager instance = new ConversationManager();
    }

}
