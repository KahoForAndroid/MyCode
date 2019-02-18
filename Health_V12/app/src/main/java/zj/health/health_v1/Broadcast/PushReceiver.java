package zj.health.health_v1.Broadcast;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import zj.health.health_v1.Activity.Consultation_Room_Activity;
import zj.health.health_v1.Activity.Counselling_list_Activity;
import zj.health.health_v1.Activity.Doctor_service_Activity;
import zj.health.health_v1.Activity.MainsActivity;
import zj.health.health_v1.Base.Health_AppLocation;
import zj.health.health_v1.IM.utils.Foreground;
import zj.health.health_v1.R;

/**
 * Created by Administrator on 2018/12/12.
 */

public class PushReceiver extends BroadcastReceiver{

    private final int pushId=1;


    @Override
    public void onReceive(Context context, Intent intent) {

        switch (intent.getIntExtra("cmd", 0)) {
            case 1001:
                if (Foreground.get().isForeground() && isMainActivityTop(context, Consultation_Room_Activity.class.getName())) {
                    Intent it = new Intent();
                    it.setAction("updateList");
                    context.sendBroadcast(it);
                }else if(intent.getStringExtra("userId").equals(Health_AppLocation.instance.users.getId())){
                    Log.d("messgae","自己发送的咨询");
                } else {

                    NotificationManager mNotificationManager = (NotificationManager) Health_AppLocation.instance.getSystemService(Health_AppLocation.instance.NOTIFICATION_SERVICE);
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(Health_AppLocation.instance);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        @SuppressLint("WrongConstant") NotificationChannel channel = new NotificationChannel("1", "channel_name",
                                NotificationManager.IMPORTANCE_DEFAULT);
                        channel.canBypassDnd();//是否绕过请勿打扰模式
                        channel.enableLights(true);//闪光灯
                        channel.setLockscreenVisibility(NotificationManager.IMPORTANCE_HIGH);//锁屏显示通知
                        channel.setLightColor(Color.RED);//闪关灯的灯光颜色
                        channel.canShowBadge();//桌面launcher的消息角标
                        channel.enableVibration(true);//是否允许震动
                        channel.getAudioAttributes();//获取系统通知响铃声音的配置
                        channel.getGroup();//获取通知取到组
                        channel.setBypassDnd(true);//设置可绕过  请勿打扰模式
                        channel.setVibrationPattern(new long[]{100, 100, 200});//设置震动模式
                        channel.shouldShowLights();//是否会有灯光
                        mBuilder.setChannelId("1");
                        mNotificationManager.createNotificationChannel(channel);
                    }


                    Intent notificationIntent = new Intent(Health_AppLocation.instance, Consultation_Room_Activity.class);
                    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    PendingIntent pendingIntent = PendingIntent.getActivity(Health_AppLocation.instance, 0,
                            notificationIntent, 0);
                    mBuilder.setContentTitle("新的健康咨询")//设置通知栏标题
                            .setContentText("有用户发起了一个咨询,请查看")
                            .setContentIntent(pendingIntent) //设置通知栏点击意图
//                .setNumber(++pushNum) //设置通知集合的数量
                            .setTicker("有患者需要您的帮助") //通知首次出现在通知栏，带上升动画效果的
                            .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                            .setDefaults(Notification.DEFAULT_ALL)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                            .setSmallIcon(R.drawable.logo_icon);//设置通知小ICON
                    Notification notify = mBuilder.build();
                    notify.flags |= Notification.FLAG_AUTO_CANCEL;
                    mNotificationManager.notify(pushId, notify);

                }
                break;
            case 1002:
                if (Foreground.get().isForeground() && isMainActivityTop(context, Counselling_list_Activity.class.getName())) {
                    Intent it = new Intent();
                    it.setAction("updateCounselling_list");
                    context.sendBroadcast(it);
                }else {

                    NotificationManager mNotificationManager = (NotificationManager) Health_AppLocation.instance.getSystemService(Health_AppLocation.instance.NOTIFICATION_SERVICE);
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(Health_AppLocation.instance);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        @SuppressLint("WrongConstant") NotificationChannel channel = new NotificationChannel("1", "channel_name",
                                NotificationManager.IMPORTANCE_DEFAULT);
                        channel.canBypassDnd();//是否绕过请勿打扰模式
                        channel.enableLights(true);//闪光灯
                        channel.setLockscreenVisibility(NotificationManager.IMPORTANCE_HIGH);//锁屏显示通知
                        channel.setLightColor(Color.RED);//闪关灯的灯光颜色
                        channel.canShowBadge();//桌面launcher的消息角标
                        channel.enableVibration(true);//是否允许震动
                        channel.getAudioAttributes();//获取系统通知响铃声音的配置
                        channel.getGroup();//获取通知取到组
                        channel.setBypassDnd(true);//设置可绕过  请勿打扰模式
                        channel.setVibrationPattern(new long[]{100, 100, 200});//设置震动模式
                        channel.shouldShowLights();//是否会有灯光
                        mBuilder.setChannelId("1");
                        mNotificationManager.createNotificationChannel(channel);
                    }


                    Intent notificationIntent = new Intent(Health_AppLocation.instance, Doctor_service_Activity.class);
                    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    PendingIntent pendingIntent = PendingIntent.getActivity(Health_AppLocation.instance, 0,
                            notificationIntent, 0);
                    mBuilder.setContentTitle("健康咨询")//设置通知栏标题
                            .setContentText("有医生对您的咨询发起了邀请,请查看")
                            .setContentIntent(pendingIntent) //设置通知栏点击意图
//                .setNumber(++pushNum) //设置通知集合的数量
                            .setTicker("有医生对您的咨询发起了邀请,请查看") //通知首次出现在通知栏，带上升动画效果的
                            .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                            .setDefaults(Notification.DEFAULT_ALL)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                            .setSmallIcon(R.drawable.logo_icon);//设置通知小ICON
                    Notification notify = mBuilder.build();
                    notify.flags |= Notification.FLAG_AUTO_CANCEL;
                    mNotificationManager.notify(pushId, notify);
                }
                break;
                default:
                    break;

        }
    }

     private boolean isMainActivityTop(Context context,String activityName){
          ActivityManager manager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
          String name = manager.getRunningTasks(1).get(0).topActivity.getClassName();
       return name.equals(activityName);
    }

}
