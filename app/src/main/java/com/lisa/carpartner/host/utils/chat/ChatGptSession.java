package com.lisa.carpartner.host.utils.chat;

import androidx.annotation.NonNull;

import com.lisa.carpartner.host.utils.LogUtils;
import com.lisa.carpartner.host.utils.UiThreadUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatGptSession {
    private static final String TAG = ChatGptSession.class.getSimpleName();
    private static final ChatGptConfig CHAT_GPT_CONFIG = new ChatGptConfig();
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");
    private OkHttpClient client;
    private List<ChatGptConversationMsg> messageList = new ArrayList<>();
    private Call call;
    private boolean isStart = false;

    public List<ChatGptConversationMsg> getChatMessageList() {
        return messageList;
    }

    public void startNewChat() {
        LogUtils.d(TAG, "startNewChat");
        isStart = true;
        client = new OkHttpClient.Builder()
                .readTimeout(5, TimeUnit.SECONDS)
                .build();
        if (call != null) call.cancel();
        messageList.clear();
        messageList.add(new ChatGptConversationMsg("system", CHAT_GPT_CONFIG.ROLE));
    }

    public void stopChat() {
        LogUtils.d(TAG, "stopChat");
        if (!isStart) return;
        isStart = false;
        client = null;
        if (call != null) call.cancel();
        messageList.clear();
    }

    public void askToGpt(String question, ChatCallBack chatCallBack) {
        if (!isStart) return;
        LogUtils.d(TAG, "askToGpt " + question);
        messageList.add(new ChatGptConversationMsg(ChatGptConversationMsg.SPEAKER_USER, question));

        RequestBody body = RequestBody.create(getParams().toString(), JSON);
        Request request = new Request.Builder()
                .url(CHAT_GPT_CONFIG.URL)
                .header("Authorization", "Bearer " + CHAT_GPT_CONFIG.API_KEY)
                .post(body)
                .build();

        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                LogUtils.d(TAG, "onFailure" + "Failed to load response due to " + e.getMessage());
                ChatGptSession.this.onError("Failed to load response due to " + e.getMessage(), chatCallBack);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    JSONObject jsonObject = null;
                    try {
                        String resultBodyJson = response.body().string();
                        LogUtils.d(TAG, "onResponse result " + resultBodyJson);
                        jsonObject = new JSONObject(resultBodyJson);
                        JSONArray jsonArray = jsonObject.getJSONArray("choices");
                        String result = jsonArray.getJSONObject(0)
                                .getJSONObject("message")
                                .getString("content");
                        ChatGptSession.this.onResponse(result, chatCallBack);
                        LogUtils.d(TAG, "Gpt response : " + result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    LogUtils.d(TAG, "onFailure" + "Failed to load response due to " + response.body().toString());
                    ChatGptSession.this.onError("Failed to load response due to " + response.body().toString(), chatCallBack);
                }
            }
        });
    }

    private void onResponse(String response, ChatCallBack chatCallBack) {
        messageList.add(new ChatGptConversationMsg(ChatGptConversationMsg.SPEAKER_CHATGPT, response));
        UiThreadUtils.runOnUiThread(() -> {
            if (chatCallBack != null) chatCallBack.onChatGptResponse(response);
        });
    }

    private void onError(String error, ChatCallBack chatCallBack) {
        messageList.add(new ChatGptConversationMsg(ChatGptConversationMsg.SPEAKER_CHATGPT_ERROR, error));
        UiThreadUtils.runOnUiThread(() -> {
            if (chatCallBack != null) chatCallBack.onChatGptResponse(error);
        });
    }

    private JSONObject getParams() {
        JSONObject jsonBody = new JSONObject();

        try {
            jsonBody.put("model", CHAT_GPT_CONFIG.MODEL);
            jsonBody.put("temperature", CHAT_GPT_CONFIG.TEMPERATURE);
            jsonBody.put("stream", CHAT_GPT_CONFIG.IS_STREAM);
            jsonBody.put("max_tokens", CHAT_GPT_CONFIG.MAX_TOKEN);

            JSONArray messageArr = new JSONArray();
            for (ChatGptConversationMsg conversationMsg : messageList) {
                if (conversationMsg.speaker.equals(ChatGptConversationMsg.SPEAKER_CHATGPT_ERROR)) {
                    continue;
                }
                JSONObject obj = new JSONObject();
                obj.put("role", conversationMsg.speaker);
                obj.put("content", conversationMsg.content);
                messageArr.put(obj);
            }
            jsonBody.put("messages", messageArr);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonBody;
    }

}
