package com.lisa.carpartner.host.utils;

import android.Manifest;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import java.util.Map;

public class PermissionUtils {
    private static final String[] REQUEST_PERMISSION = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION};

    public static void requestPermissionIfNeed(ComponentActivity activity, Runnable allGrantedCallBack) {
        ActivityResultLauncher<String[]> requestPermissionLauncher = activity.registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
            @Override
            public void onActivityResult(Map<String, Boolean> result) {
                boolean allGranted = true;
                for (Map.Entry<String, Boolean> entry : result.entrySet()) {
                    if (!entry.getValue()) {
                        allGranted = false;
                        break;
                    }
                }
                if (allGranted) {
                    if (allGrantedCallBack != null) allGrantedCallBack.run();
                } else {
                    Toast.makeText(activity, "需要所有权限授权方可使用", Toast.LENGTH_SHORT).show();
                }
            }
        });
        requestPermissionLauncher.launch(REQUEST_PERMISSION);
    }
}
