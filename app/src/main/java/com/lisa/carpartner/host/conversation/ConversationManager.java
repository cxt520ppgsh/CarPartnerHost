package com.lisa.carpartner.host.conversation;

import com.iflytek.cloud.SpeechError;
import com.lisa.carpartner.host.utils.ToastUtils;
import com.lisa.carpartner.host.utils.UiThreadUtils;
import com.lisa.carpartner.host.utils.chat.ChatCallBack;
import com.lisa.carpartner.host.utils.chat.ChatGptSession;
import com.lisa.carpartner.host.utils.voice.ring.RingUtils;
import com.lisa.carpartner.host.utils.voice.stt.STTUtils;
import com.lisa.carpartner.host.utils.voice.wake.WakeUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConversationManager implements WakeUtils.OnWakeupCallback,
        STTUtils.OnlineSoundToTextCallback,
        ChatCallBack {

    public Set<ConversationCallback> conversationCallbacks = new HashSet<>();
    private ConversationMsg currentContent;
    private boolean isConversationStart = false;
    private String soundToTextResult = "";
    private static final int CONVERSATION_TIMOUNT = 50000;
    private List<ConversationMsg> contents = new ArrayList<>();
    private ChatGptSession chatGptSession = new ChatGptSession();

    public static ConversationManager getInstance() {
        return ConversationManager.Holder.instance;
    }

    public List<ConversationMsg> getContents() {
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
        if (!isConversationStart) return;
        soundToTextResult = "";
        resetConversationTimount();
    }

    @Override
    public void onSoundToText(String text, boolean isLast) {
        if (!isConversationStart) return;
        soundToTextResult += text;
        resetConversationTimount();
        if (isLast) {
            modifyCurrentContent(new ConversationMsg(ConversationMsg.SPEAKER_USER, soundToTextResult));
            appendContent(new ConversationMsg(ConversationMsg.SPEAKER_AI, "..."));
            chatGptSession.askToGpt(soundToTextResult, this);
        }
    }

    @Override
    public void onChatGptResponse(String response) {
        if (!isConversationStart) return;
        modifyCurrentContent(new ConversationMsg(ConversationMsg.SPEAKER_AI, response));
        RingUtils.playDing(mp -> {
            appendContent(new ConversationMsg(ConversationMsg.SPEAKER_USER, "..."));
            STTUtils.startOnlineSoundToText(ConversationManager.this);
        });
    }

    @Override
    public void onError(SpeechError speechError) {
        soundToTextResult = speechError.getErrorDescription();
        resetConversationTimount();
        modifyCurrentContent(new ConversationMsg(ConversationMsg.SPEAKER_AI, speechError.getErrorDescription()));
        RingUtils.playDing(mp -> {
            appendContent(new ConversationMsg(ConversationMsg.SPEAKER_USER, "..."));
            STTUtils.startOnlineSoundToText(ConversationManager.this);
        });
    }

    public interface ConversationCallback {

        void onConversationStart();

        void onConversationUpdate(List<ConversationMsg> content);

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
        chatGptSession.startNewChat();
        RingUtils.playDing(mp -> {
            appendContent(new ConversationMsg(ConversationMsg.SPEAKER_AI, "请说"));
            appendContent(new ConversationMsg(ConversationMsg.SPEAKER_USER, ""));
            STTUtils.startOnlineSoundToText(ConversationManager.this);
        });
    }

    private void stopConversation() {
        if (!isConversationStart) return;
        isConversationStart = false;
        chatGptSession.stopChat();
        contents.clear();
        fireContentsUpdate();
        fireConversationEnd();
    }

    private void clearContent() {
        contents.clear();
        fireContentsUpdate();
    }

    private void appendContent(ConversationMsg content) {
        if (content == null) return;
        if (contents.contains(content)) return;
        currentContent = content;
        contents.add(content);
        fireContentsUpdate();
    }

    private void modifyCurrentContent(ConversationMsg content) {
        currentContent.speaker = content.speaker;
        currentContent.content = content.content;
        fireContentsUpdate();
    }

    private Runnable onConversationTimountRunnable = () -> {
        stopConversation();
    };

    private static class Holder {
        static ConversationManager instance = new ConversationManager();
    }

}
