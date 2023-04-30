package com.lisa.carpartner.host.utils;


import android.widget.Toast;

import java.util.Locale;

public final class ToastUtils {

    private ToastUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    private static volatile Toast mToast;

    public static void show(final int resId, final Object... formatArgs) {
        show(AppUtils.getApplication().getResources().getString(resId), formatArgs);
    }

    public static void show(final String text, final Object... formatArgs) {
        UiThreadUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mToast != null)
                    mToast.cancel();
                mToast = Toast.makeText(AppUtils.getApplicationContext(),
                        String.format(Locale.getDefault(), text, formatArgs),
                        Toast.LENGTH_SHORT);
                mToast.show();
            }
        });
    }
}
