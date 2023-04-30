package com.lisa.carpartner.host.utils;

import android.Manifest;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import java.util.Map;

public class PermissionUtils {
    private static final String TAG = PermissionUtils.class.getSimpleName();

    private static final String[] REQUEST_PERMISSION = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_NETWORK_STATE,
            Manifest.permission.READ_CONTACTS,
//            Manifest.permission.WRITE_SETTINGS,
            Manifest.permission.MODIFY_AUDIO_SETTINGS,
            Manifest.permission.FOREGROUND_SERVICE,
    };

    public static void requestPermissionIfNeed(ComponentActivity activity) {
        ActivityResultLauncher<String[]> requestPermissionLauncher = activity.registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
            @Override
            public void onActivityResult(Map<String, Boolean> result) {
                boolean allGranted = true;
                for (Map.Entry<String, Boolean> entry : result.entrySet()) {
                    if (!entry.getValue()) {
                        Toast.makeText(activity, "permission not granted " + entry.getKey(), Toast.LENGTH_SHORT).show();
                        LogUtils.d(TAG, "permission not granted " + entry.getKey());
                        allGranted = false;
                        break;
                    }
                }
                if (allGranted) {

                } else {
//                    activity.finish();
                    Toast.makeText(activity, "需要所有权限授权方可使用", Toast.LENGTH_SHORT).show();
                }
            }
        });
        requestPermissionLauncher.launch(REQUEST_PERMISSION);
    }
}
