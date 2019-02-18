package zj.health.health_v1.Broadcast;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import zj.health.health_v1.R;
import zj.health.health_v1.Service.AlarmManageService;

/**
 * Created by Administrator on 2018/10/16.
 */

public class ClockReceiver extends BroadcastReceiver{

    private int notifiationId = 1;
    @Override
    public void onReceive(Context context, Intent intent) {
//        Toast.makeText(context, "chiyao", Toast.LENGTH_SHORT).show();
        ShowNotification(context);
        Intent it = new Intent(context,AlarmManageService.class);
        context.startService(it);
    }

    private void ShowNotification(Context context){
        Bitmap btm = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo_icon);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                                        context).setSmallIcon(R.drawable.logo_icon)
                              .setContentTitle("您有一条新提醒")
                                .setContentText("您设置的服药提醒时间已到,请按时服药");
                         mBuilder.setTicker("新提醒");//第一次提示消息的时候显示在通知栏上
                        mBuilder.setNumber(12);
                        mBuilder.setLargeIcon(btm);
                       Uri uri=Uri.parse("android.resource://" + context.getPackageName() + "/" +R.raw.remind);
                        mBuilder.setSound(uri);
                         mBuilder.setAutoCancel(true);//自己维护通知的消失

//                         //构建一个Intent
//                         Intent resultIntent = new Intent(MainActivity.this,
//                                        ResultActivity.class);
//                         //封装一个Intent
//                         PendingIntent resultPendingIntent = PendingIntent.getActivity(
//                                         MainActivity.this, 0, resultIntent,
//                                        PendingIntent.FLAG_UPDATE_CURRENT);
//                         // 设置通知主题的意图
//                       mBuilder.setContentIntent(resultPendingIntent);
                    //获取通知管理器对象
        notifiationId++;
         NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                     mNotificationManager.cancel(notifiationId);
                     mNotificationManager.notify(notifiationId, mBuilder.build());
    }
}
