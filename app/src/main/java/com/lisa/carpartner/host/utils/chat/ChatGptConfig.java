package com.lisa.carpartner.host.utils.chat;

public class ChatGptConfig {
    public String URL = "https://api.openai.com/v1/chat/completions";
    public String API_KEY = "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";
    public String MODEL = "gpt-3.5-turbo";
    public double TEMPERATURE = 0.7;
    public int MAX_TOKEN = 50;
    public boolean IS_STREAM = false;

    public String ROLE = "" +
            "你是一个傲娇大小姐，" +
            "用这个语气和我讲话";
}
