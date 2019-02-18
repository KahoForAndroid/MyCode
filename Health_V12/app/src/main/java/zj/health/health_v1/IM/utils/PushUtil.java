package zj.health.health_v1.IM.utils;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMGroupReceiveMessageOpt;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.qcloud.presentation.event.MessageEvent;


import java.util.Observable;
import java.util.Observer;

import zj.health.health_v1.Activity.Consultation_Room_Activity;
import zj.health.health_v1.Activity.Doctor_service_Activity;
import zj.health.health_v1.Activity.MainsActivity;
import zj.health.health_v1.Base.Health_AppLocation;
import zj.health.health_v1.IM.model.CustomMessage;
import zj.health.health_v1.IM.model.Message;
import zj.health.health_v1.IM.model.MessageFactory;
import zj.health.health_v1.IM.ui.ChatActivity;
import zj.health.health_v1.IM.ui.HomeActivity;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.StringUtil;

/**
 * 在线消息通知展示
 */
public class PushUtil implements Observer {

    private static final String TAG = PushUtil.class.getSimpleName();

    private static int pushNum=0;

    private final int pushId=1;

    private static PushUtil instance = new PushUtil();

    private PushUtil() {
        MessageEvent.getInstance().addObserver(this);
    }

    public static PushUtil getInstance(){
        return instance;
    }



    private void PushNotify(TIMMessage msg){
        if(!msg.isSelf() ){

            if(MessageFactory.getMessage(msg) instanceof CustomMessage){
                if(((CustomMessage) MessageFactory.getMessage(msg)).getType() == CustomMessage.Type.TYPING){
                    return;
                }
            }
            //如果医生服务列表里包含推送过来的用户id 则在主页面更新红点
            if(Doctor_service_Activity.consultModelList!=null && Doctor_service_Activity.consultModelList.size()>0){
                for (int i = 0;i<Doctor_service_Activity.consultModelList.size();i++){
                    if(Doctor_service_Activity.consultModelList.get(i).getDoctorId().equals(StringUtil.trimNull(msg.getSenderProfile().getIdentifier()))){
                        Health_AppLocation.instance.ImFlag = 1;
                        if(Foreground.get().isForeground()){
                            Intent it = new Intent("imflag");
                            Health_AppLocation.instance.sendBroadcast(it);
                        }
                        break;
                    }
                }
            }else if(Consultation_Room_Activity.consultListModelList!=null && Consultation_Room_Activity.consultListModelList.size()>0){
                //如果医生出诊列表里包含推送过来的用户id 则在主页面更新红点
                for (int i = 0;i<Consultation_Room_Activity.consultListModelList.size();i++){
                    if(Consultation_Room_Activity.consultListModelList.get(i).getUserId().equals(StringUtil.trimNull(msg.getSenderProfile().getIdentifier()))){

                        Health_AppLocation.instance.ImFlag = 2;
                        if(Foreground.get().isForeground()){
                            Intent it = new Intent("imflag");
                            Health_AppLocation.instance.sendBroadcast(it);
                        }
                        break;
                    }
                }

            }else{
                Health_AppLocation.instance.ImFlag = 0;
            }
        }
        //系统消息，自己发的消息，程序在前台的时候不通知
        if (msg==null||
                (msg.getConversation().getType()!= TIMConversationType.Group&&
                        msg.getConversation().getType()!= TIMConversationType.C2C)||
                msg.isSelf()|| isMainActivityTop(Health_AppLocation.instance, ChatActivity.class.getName())||
                msg.getRecvFlag() == TIMGroupReceiveMessageOpt.ReceiveNotNotify ||
                MessageFactory.getMessage(msg) instanceof CustomMessage) return;


        String senderStr,contentStr;
        Message message = MessageFactory.getMessage(msg);
        if (message == null) return;
        senderStr = message.getMessage().getSenderProfile().getNickName();
        contentStr = message.getSummary();


        Log.d(TAG, "recv msg " + contentStr);
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


        Intent notificationIntent = new Intent(Health_AppLocation.instance, MainsActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(Health_AppLocation.instance, 0,
                notificationIntent, 0);
        mBuilder.setContentTitle(senderStr)//设置通知栏标题
                .setContentText(contentStr)
                .setContentIntent(intent) //设置通知栏点击意图
//                .setNumber(++pushNum) //设置通知集合的数量
                .setTicker(senderStr+":"+contentStr) //通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                .setDefaults(Notification.DEFAULT_ALL)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                .setSmallIcon(R.drawable.logo_icon);//设置通知小ICON
        Notification notify = mBuilder.build();
        notify.flags |= Notification.FLAG_AUTO_CANCEL;
        mNotificationManager.notify(pushId, notify);


    }

    private boolean isMainActivityTop(Context context, String activityName){
        ActivityManager manager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        String name = manager.getRunningTasks(1).get(0).topActivity.getClassName();
        return name.equals(activityName);
    }

    public static void resetPushNum(){
        pushNum=0;
    }

    public void reset(){
        NotificationManager notificationManager = (NotificationManager)Health_AppLocation.instance.getSystemService(Health_AppLocation.instance.NOTIFICATION_SERVICE);
        notificationManager.cancel(pushId);
    }

    /**
     * This method is called if the specified {@code Observable} object's
     * {@code notifyObservers} method is called (because the {@code Observable}
     * object has been updated.
     *
     * @param observable the {@link Observable} object.
     * @param data       the data passed to {@link Observable#notifyObservers(Object)}.
     */
    @Override
    public void update(Observable observable, Object data) {
        if (observable instanceof MessageEvent){
            if (data instanceof TIMMessage) {
                TIMMessage msg = (TIMMessage) data;
                if (msg != null){
                    PushNotify(msg);
                }
            }
        }
    }
}
