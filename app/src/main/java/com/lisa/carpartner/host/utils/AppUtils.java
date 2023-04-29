package com.lisa.carpartner.host.utils;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Process;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public final class AppUtils {

    private AppUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    private static volatile Application application;

    public static synchronized void init(Application application) {
        AppUtils.application = application;
    }

    public static <T extends Application> T getApplication() {
        try {
            if (application == null) {
                synchronized (AppUtils.class) {
                    if (application == null) {
                        Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
                        Method method = activityThreadClass.getMethod("currentActivityThread");
                        Object localObject = method.invoke(null, (Object[]) null);
                        Field appField = activityThreadClass.getDeclaredField("mInitialApplication");
                        appField.setAccessible(true);
                        application = (Application) appField.get(localObject);
                        if (application == null) {
                            Method method2 = activityThreadClass.getMethod("getApplication");
                            application = (Application) method2.invoke(localObject, (Object[]) null);
                        }
                    }
                }
            }
            // noinspection unchecked
            return (T) application;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Context getContext() {
        return getApplicationContext();
    }

    public static Context getApplicationContext() {
        return getApplication();
    }

    public static String getPackageName() {
        return getContext().getPackageName();
    }

    public static int getVersionCode() {
        PackageInfo packageInfo = getPackageInfo();
        if (packageInfo == null) {
            return 0;
        }
        return getPackageInfo().versionCode;
    }

    public static String getVersionName() {
        PackageInfo packageInfo = getPackageInfo();
        if (packageInfo == null) {
            return "";
        }
        return getPackageInfo().versionName;
    }

    public <T> T getSystemService(Class<T> serviceClass) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getContext().getSystemService(serviceClass);
        }
        return null;
    }

    public static <T> T getSystemService(String serviceName) {
        // noinspection unchecked
        return (T) getContext().getSystemService(serviceName);
    }

    public static PackageManager getPackageManager() {
        return getContext().getPackageManager();
    }

    public static PackageInfo getPackageInfo() {
        return getPackageInfo(getPackageName());
    }

    public static PackageInfo getPackageInfo(String packageName) {
        return getPackageInfo(packageName, 0);
    }

    public static PackageInfo getPackageInfo(String packageName, int flags) {
        try {
            return getPackageManager().getPackageInfo(packageName, flags);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ApplicationInfo getApplicationInfo() {
        return getApplicationInfo(getPackageName());
    }

    public static ApplicationInfo getApplicationInfo(String packageName) {
        return getApplicationInfo(packageName, 0);
    }

    public static ApplicationInfo getApplicationInfo(String packageName, int flags) {
        try {
            return getPackageManager().getApplicationInfo(packageName, flags);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getProcessName() {
        return getProcessName(Process.myPid());
    }

    public static String getProcessName(int pid) {
        ActivityManager am = getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps != null && !runningApps.isEmpty()) {
            for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
                if (procInfo.pid == pid) {
                    return procInfo.processName;
                }
            }
        }
        return null;
    }

    public static int getProcessId(String processName) {
        ActivityManager am = getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps != null && !runningApps.isEmpty()) {
            for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
                if (procInfo.processName.equals(processName)) {
                    return procInfo.pid;
                }
            }
        }
        return -1;
    }

    public static boolean isMainProcess() {
        return getPackageName().equals(getProcessName());
    }

    public static boolean isDebug() {
        try {
            ApplicationInfo applicationInfo = getApplicationInfo();
            return applicationInfo != null && (applicationInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isAppBackground() {
        ActivityManager am = getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(getPackageName())) {
                return appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
            }
        }
        return true;
    }

    public static boolean isAppForeground() {
        return !isAppBackground();
    }

    @SuppressLint("PrivateApi")
    @SuppressWarnings("unchecked")
    public static void closeApiWarningDialog() {
        try {
            Class aClass = Class.forName("android.content.pm.PackageParser$Package");
            Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
            declaredConstructor.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class cls = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
            declaredMethod.setAccessible(true);
            Object activityThread = declaredMethod.invoke(null);
            Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean launchApp(String packageName) {
        try {
            Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
            getContext().startActivity(intent);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
