package com.lisa.carpartner.host;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lisa.carpartner.host.conversation.ConversationManager;
import com.lisa.carpartner.host.conversation.ConversationMsg;
import com.lisa.carpartner.host.utils.PermissionUtils;
import com.lisa.carpartner.host.utils.UiThreadUtils;
import com.lisa.carpartner.host.utils.voice.wake.WakeUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button startBt;
    private RecyclerView contentRv;
    private ConversationRvAdapter conversationRvAdapter;
    private int dataSize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PermissionUtils.requestPermissionIfNeed(this);
        findById();
        setListener();
        UiThreadUtils.runOnUiThread(() -> refreshUI(), 1000);
        init();
    }

    private void findById() {
        startBt = findViewById(R.id.startBt);
        contentRv = findViewById(R.id.contentRv);
        contentRv.setLayoutManager(new LinearLayoutManager(this));
        conversationRvAdapter = new ConversationRvAdapter(ConversationManager.getInstance().getContents());
        contentRv.setAdapter(conversationRvAdapter);
    }

    private void setListener() {
        startBt.setOnClickListener(v -> {
            if (WakeUtils.isWakeUpListing()) {
                MainService.stop();
            } else {
                MainService.start();
            }
            startBt.setEnabled(false);
            UiThreadUtils.runOnUiThread(() -> refreshUI(), 1500);
        });
    }

    private void refreshUI() {
        startBt.setEnabled(true);
        startBt.setText(WakeUtils.isWakeUpListing() ? "停止" : "开始");
    }

    private void init() {
        ConversationManager.getInstance().registerConversationCallback(new ConversationManager.ConversationCallback() {

            @Override
            public void onConversationStart() {

            }

            @Override
            public void onConversationUpdate(List<ConversationMsg> conversationMsgs) {
                contentRv.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onConversationEnd() {

            }
        });
    }


}