package com.lisa.carpartner.host.utils.chat;

import com.lisa.carpartner.host.conversation.ConversationMsg;

public class ChatGptConversationMsg extends ConversationMsg {
    public static String SPEAKER_CHATGPT = "assistant";
    public static String SPEAKER_USER = "user";
    public String speaker = "";
    public String content = "";


    public ChatGptConversationMsg(String speaker, String content) {
        super(speaker, content);
    }
}