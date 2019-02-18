package zj.health.health_v1.Broadcast;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import zj.health.health_v1.R;
import zj.health.health_v1.Service.AlarmManageService;

/**
 * Created by Administrator on 2018/5/13.
 */

public class RemindReceiver extends BroadcastReceiver{

    private static Notification.Builder  builder;
    private static NotificationManager manager;
    private Context context;
    private static int notification_id = 0;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        Intent it = new Intent(context, AlarmManageService.class);
        if(!intent.getStringExtra("time").equals("")){
            getNotification();
            int delay = 2 * 1000;
            it.putExtra("delay",delay);
        }else{
//            int delay = 60 * 1000 * 60;
            int delay = 10 * 1000;
            it.putExtra("delay",delay);
        }
        context.startService(it);
    }

    private NotificationManager getManagerInstance(){
        if(manager == null){
            manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }

    private void getNotification(){
        if(builder == null){
            Toast.makeText(context, "remind", Toast.LENGTH_SHORT).show();
            Notification.Builder builder = new Notification.Builder(context);
            builder.setSmallIcon(R.drawable.logo_icon);
            builder.setTicker("是时候吃药啦");
            builder.setWhen(System.currentTimeMillis());
            builder.setContentTitle("新通知");
            builder.setContentText("请按照服药提醒上的时间按时吃药");

//            Intent intent = new Intent(MainActivity.this, Activity.class);
//            PendingIntent ma = PendingIntent.getActivity(MainActivity.this,0,intent,0);
//            builder.setContentIntent(ma);//设置点击过后跳转的activity

                builder.setDefaults(Notification.DEFAULT_SOUND);//设置声音
//                builder.setDefaults(Notification.DEFAULT_LIGHTS);//设置指示灯
                builder.setDefaults(Notification.DEFAULT_VIBRATE);//设置震动*/
            builder.setDefaults(Notification.DEFAULT_ALL);//设置全部

            Notification notification = builder.build();//4.1以上用.build();
            notification.flags |= Notification.FLAG_AUTO_CANCEL;// 点击通知的时候cancel掉
            getManagerInstance().notify(notification_id,notification);
            notification_id++;
        }else{
            Notification notification = builder.build();
            getManagerInstance().notify(notification_id,notification);
            notification_id++;
        }
    }
}
