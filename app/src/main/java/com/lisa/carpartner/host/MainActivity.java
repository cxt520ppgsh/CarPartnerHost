package com.lisa.carpartner.host;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lisa.carpartner.host.utils.PermissionUtils;
import com.lisa.carpartner.host.utils.UiThreadUtils;
import com.lisa.carpartner.host.utils.voice.stt.STTUtils;
import com.lisa.carpartner.host.utils.voice.wake.WakeUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button startBt;
    private RecyclerView contentRv;
    private ConversationRvAdapter conversationRvAdapter;

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
                MainService.stopWakeUp();
            } else {
                MainService.startWakeUp();
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
            public void onWakeUpListeningStart() {

            }

            @Override
            public void onWakeUpListeningStop() {

            }

            @Override
            public void onConversationStart() {

            }

            @Override
            public void onConversationUpdate(List<ConversationManager.Content> content) {
                conversationRvAdapter.notifyDataSetChanged();
            }

            @Override
            public void onConversationEnd() {

            }
        });
    }


}