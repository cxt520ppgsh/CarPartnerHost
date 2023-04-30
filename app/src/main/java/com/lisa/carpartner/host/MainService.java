package com.lisa.carpartner.host;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import com.iflytek.cloud.SpeechError;
import com.lisa.carpartner.host.utils.LogUtils;
import com.lisa.carpartner.host.utils.voice.wake.WakeUtils;

public class MainService extends Service implements WakeUtils.OnWakeupCallback {
    private static final String TAG = MainService.class.getSimpleName();

    public MainService() {

    }

    public static void startWakeUp() {
        ConversationManager.getInstance().startWakeUp();
    }

    public static void stopWakeUp() {
        ConversationManager.getInstance().stopWakeUp();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        onStart();
        return super.onStartCommand(intent, flags, startId);
    }

    private void onStart() {
        LogUtils.d(TAG, "onStart");
        startForeground();
        ConversationManager.getInstance().startWakeUp();
    }

    @Override
    public void onWakeUp() {

    }

    private void startForeground() {
        String title = "LiSA~";
        String text = "LiSA正在为你服务";
        boolean isSilent = true;//是否静音
        boolean isOngoing = true;//是否持续(为不消失的常驻通知)
        String channelName = "服务常驻通知";
        String channelId = "Service_Id";
        String category = Notification.CATEGORY_SERVICE;
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent nfIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, nfIntent, PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setContentIntent(pendingIntent) //设置PendingIntent
                .setSmallIcon(R.mipmap.ic_launcher) //设置状态栏内的小图标
                .setContentTitle(title) //设置标题
                .setContentText(text) //设置内容
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)//设置通知公开可见
                .setOngoing(isOngoing)//设置持续(不消失的常驻通知)
                .setCategory(category)//设置类别
                .setPriority(NotificationCompat.PRIORITY_MAX);//优先级为：重要通知
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//安卓8.0以上系统要求通知设置Channel,否则会报错
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);//锁屏显示通知
            notificationManager.createNotificationChannel(notificationChannel);
            builder.setChannelId(channelId);
        }
        startForeground(1, builder.build());//创建一个通知，创建通知前记得获取开启通知权限
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public void onError(SpeechError speechError) {

    }
}