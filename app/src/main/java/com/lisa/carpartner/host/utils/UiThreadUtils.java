package com.lisa.carpartner.host.utils;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public final class UiThreadUtils {
    private static volatile Handler handler;

    private UiThreadUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static synchronized void setHandler(Handler handler) {
        if (handler == null) {
            handler = new Handler(Looper.getMainLooper());
        }

        UiThreadUtils.handler = handler;
    }

    public static Handler getHandler() {
        if (handler == null) {
            Class var0 = UiThreadUtils.class;
            synchronized (UiThreadUtils.class) {
                if (handler == null) {
                    handler = new Handler(Looper.getMainLooper());
                }
            }
        }

        return handler;
    }

    public static boolean isUiThread() {
        return isUiThread(Thread.currentThread());
    }

    public static boolean isUiThread(Thread thread) {
        return thread != null && isUiThread(thread.getId());
    }

    public static boolean isUiThread(long id) {
        return getLooper().getThread().getId() == id;
    }

    public static boolean runOnUiThread(Runnable action) {
        if (isUiThread()) {
            if (action != null) {
                action.run();
                return true;
            } else {
                return false;
            }
        } else {
            return post(action);
        }
    }

    public static boolean runOnUiThread(Runnable action, long delay) {
        return postDelayed(action, delay);
    }

    public static String getMessageName(Message message) {
        return getHandler().getMessageName(message);
    }

    public static Message obtainMessage() {
        return getHandler().obtainMessage();
    }

    public static Message obtainMessage(int what) {
        return getHandler().obtainMessage(what);
    }

    public static Message obtainMessage(int what, Object obj) {
        return getHandler().obtainMessage(what, obj);
    }

    public static Message obtainMessage(int what, int arg1, int arg2) {
        return getHandler().obtainMessage(what, arg1, arg2);
    }

    public static Message obtainMessage(int what, int arg1, int arg2, Object obj) {
        return getHandler().obtainMessage(what, arg1, arg2, obj);
    }

    public static boolean post(Runnable action) {
        return getHandler().post(action);
    }

    public static boolean postAtTime(Runnable action, long uptimeMillis) {
        return getHandler().postAtTime(action, uptimeMillis);
    }

    public static boolean postAtTime(Runnable action, Object token, long uptimeMillis) {
        return getHandler().postAtTime(action, token, uptimeMillis);
    }

    public static boolean postDelayed(Runnable action, long delayMillis) {
        return getHandler().postDelayed(action, delayMillis);
    }

    public static boolean postAtFrontOfQueue(Runnable action) {
        return getHandler().postAtFrontOfQueue(action);
    }

    public static void removeCallbacks(Runnable action) {
        getHandler().removeCallbacks(action);
    }

    public static void removeCallbacks(Runnable action, Object token) {
        getHandler().removeCallbacks(action, token);
    }

    public static boolean sendMessage(Message msg) {
        return getHandler().sendMessage(msg);
    }

    public static boolean sendEmptyMessage(int what) {
        return getHandler().sendEmptyMessage(what);
    }

    public static boolean sendEmptyMessageDelayed(int what, long delayMillis) {
        return getHandler().sendEmptyMessageDelayed(what, delayMillis);
    }

    public static boolean sendEmptyMessageAtTime(int what, long uptimeMillis) {
        return getHandler().sendEmptyMessageAtTime(what, uptimeMillis);
    }

    public static boolean sendMessageDelayed(Message msg, long delayMillis) {
        return getHandler().sendMessageDelayed(msg, delayMillis);
    }

    public static boolean sendMessageAtTime(Message msg, long uptimeMillis) {
        return getHandler().sendMessageAtTime(msg, uptimeMillis);
    }

    public static boolean sendMessageAtFrontOfQueue(Message msg) {
        return getHandler().sendMessageAtFrontOfQueue(msg);
    }

    public static void removeMessages(int what) {
        getHandler().removeMessages(what);
    }

    public static void removeMessages(int what, Object object) {
        getHandler().removeMessages(what, object);
    }

    public static void removeCallbacksAndMessages(Object token) {
        getHandler().removeCallbacksAndMessages(token);
    }

    public static boolean hasMessages(int what) {
        return getHandler().hasMessages(what);
    }

    public static boolean hasMessages(int what, Object object) {
        return getHandler().hasMessages(what, object);
    }

    public static Looper getLooper() {
        return getHandler().getLooper();
    }
}
