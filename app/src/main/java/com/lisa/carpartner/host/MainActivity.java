package com.lisa.carpartner.host;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.lisa.carpartner.host.utils.PermissionUtils;
import com.lisa.carpartner.host.utils.voice.stt.STSUtils;

public class MainActivity extends AppCompatActivity {

    private Button startOfflineWakeBt;
    private Button startOnlineSttBt;
    private TextView onlineSttTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PermissionUtils.requestPermissionIfNeed(this);
        findById();
        setListener();
    }

    private void findById() {
        startOfflineWakeBt = findViewById(R.id.startOfflineWakeBt);
        startOnlineSttBt = findViewById(R.id.startOnlineSttBt);
        onlineSttTv = findViewById(R.id.onlineSttTv);
    }

    private void setListener() {
        startOfflineWakeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        startOnlineSttBt.setOnClickListener(v -> STSUtils.startOnlineSoundToText(new STSUtils.OnlineSoundToTextCallback() {
            @Override
            public void onStart() {
                onlineSttTv.setText("");
                startOnlineSttBt.setEnabled(false);
            }

            @Override
            public void onSoundToText(String text, boolean isLast) {
                onlineSttTv.setText(onlineSttTv.getText() + "" + text);
                if (isLast) startOnlineSttBt.setEnabled(true);
            }

            @Override
            public void onError(SpeechError speechError) {
                startOnlineSttBt.setEnabled(true);
            }
        }));
    }
}