package com.lisa.carpartner.host.conversation;

public class ConversationMsg {
    public static String SPEAKER_AI = "LiSA";
    public static String SPEAKER_USER = "您";
    public String speaker = "";
    public String content = "";

    private ConversationMsg() {

    }

    public ConversationMsg(String speaker, String content) {
        this.speaker = speaker;
        this.content = content;
    }
}