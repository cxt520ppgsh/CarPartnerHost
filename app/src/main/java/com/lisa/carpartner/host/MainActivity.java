package com.lisa.carpartner.host;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.result.ActivityResultCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.lisa.carpartner.host.utils.PermissionUtils;

public class MainActivity extends AppCompatActivity {

    private Button startOfflineWake;
    private Button startStt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermissionIfNeed();
        findById();
        setListener();
    }

    private void requestPermissionIfNeed() {
        PermissionUtils.requestPermissionIfNeed(this, new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    private void findById() {
        startOfflineWake = findViewById(R.id.startOfflineWake);
        startStt = findViewById(R.id.startStt);
    }

    private void setListener() {
        startOfflineWake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        startStt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}