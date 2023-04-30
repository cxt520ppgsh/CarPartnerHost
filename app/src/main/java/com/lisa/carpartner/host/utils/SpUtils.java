package com.lisa.carpartner.host.utils;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.Collections;
import java.util.Set;

public final class SpUtils {
    private static volatile SharedPreferences sharedPreferences;

    private SpUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    private static SharedPreferences getSharedPreferences() {
        if (sharedPreferences == null) {
            Class var0 = SpUtils.class;
            synchronized(SpUtils.class) {
                if (sharedPreferences == null) {
                    Context context = AppUtils.getContext();
                    sharedPreferences = context.getSharedPreferences(context.getPackageName(), 0);
                }
            }
        }

        return sharedPreferences;
    }

    private static Editor getEditor() {
        return getSharedPreferences().edit();
    }

    public static void put(String key, String value) {
        put(key, value, false);
    }

    public static void put(String key, String value, boolean byCommit) {
        if (byCommit) {
            getEditor().putString(key, value).commit();
        } else {
            getEditor().putString(key, value).apply();
        }

    }

    public static void put(String key, int value) {
        put(key, value, false);
    }

    public static void put(String key, int value, boolean byCommit) {
        if (byCommit) {
            getEditor().putInt(key, value).commit();
        } else {
            getEditor().putInt(key, value).apply();
        }

    }

    public static void put(String key, long value) {
        put(key, value, false);
    }

    public static void put(String key, long value, boolean byCommit) {
        if (byCommit) {
            getEditor().putLong(key, value).commit();
        } else {
            getEditor().putLong(key, value).apply();
        }

    }

    public static void put(String key, float value) {
        put(key, value, false);
    }

    public static void put(String key, float value, boolean byCommit) {
        if (byCommit) {
            getEditor().putFloat(key, value).commit();
        } else {
            getEditor().putFloat(key, value).apply();
        }

    }

    public static void put(String key, boolean value) {
        put(key, value, false);
    }

    public static void put(String key, boolean value, boolean byCommit) {
        if (byCommit) {
            getEditor().putBoolean(key, value).commit();
        } else {
            getEditor().putBoolean(key, value).apply();
        }

    }

    public static void put(String key, Set<String> values) {
        put(key, values, false);
    }

    public static void put(String key, Set<String> values, boolean byCommit) {
        if (byCommit) {
            getEditor().putStringSet(key, values).commit();
        } else {
            getEditor().putStringSet(key, values).apply();
        }

    }

    public static String getString(String key) {
        return getString(key, "");
    }

    public static String getString(String key, String defaultValue) {
        return getSharedPreferences().getString(key, defaultValue);
    }

    public static int getInt(String key) {
        return getInt(key, 0);
    }

    public static int getInt(String key, int defaultValue) {
        return getSharedPreferences().getInt(key, defaultValue);
    }

    public static long getLong(String key) {
        return getLong(key, 0L);
    }

    public static long getLong(String key, long defaultValue) {
        return getSharedPreferences().getLong(key, defaultValue);
    }

    public static float getFloat(String key) {
        return getFloat(key, 0.0F);
    }

    public static float getFloat(String key, float defaultValue) {
        return getSharedPreferences().getFloat(key, defaultValue);
    }

    public static boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return getSharedPreferences().getBoolean(key, defaultValue);
    }

    public static Set<String> getStringSet(String key) {
        return getStringSet(key, Collections.<String>emptySet());
    }

    public static Set<String> getStringSet(String key, Set<String> defaultValue) {
        return getSharedPreferences().getStringSet(key, defaultValue);
    }
}
