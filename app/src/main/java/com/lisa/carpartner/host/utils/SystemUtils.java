package com.lisa.carpartner.host.utils;

import android.content.Context;
import android.content.Intent;

public class SystemUtils {

    public static boolean dontReboot = false;

    public static void setDontReboot(boolean dontReboot) {
        SystemUtils.dontReboot = dontReboot;
    }

    public static void reboot(Context context) {
        if (dontReboot) return;
        ToastUtils.show(getRebootSHint(context));
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_REBOOT);
        intent.putExtra("nowait", 1);
        intent.putExtra("interval", 1);
        intent.putExtra("window", 0);
        context.sendBroadcast(intent);
    }

    private static String getRebootSHint(Context context) {
        String str = "";
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$string");
            Object object = clazz.newInstance();
            int id = Integer.parseInt(clazz.getField("reboot_to_update_reboot")
                    .get(object).toString());
            str = context.getResources().getString(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }
}
