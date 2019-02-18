package zj.health.health_v1.Broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import zj.health.health_v1.Activity.OnePiexlActivity;

/**
 * Created by Administrator on 2018/12/24.
 */

public class OnePixelReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {    //屏幕关闭启动1像素Activity
            Intent it = new Intent(context, OnePiexlActivity.class);
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(it);
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {   //屏幕打开 结束1像素
            context.sendBroadcast(new Intent("finish"));
            Intent main = new Intent(Intent.ACTION_MAIN);
            main.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            main.addCategory(Intent.CATEGORY_HOME);
            context.startActivity(main);
        }

    }
}
