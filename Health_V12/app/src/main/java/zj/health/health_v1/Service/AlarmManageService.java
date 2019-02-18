package zj.health.health_v1.Service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

//import zj.health.health_v1.Activity.BlueTooth_Data_Activity_Setting;
import zj.health.health_v1.Activity.MainsActivity;
//import zj.health.health_v1.Activity .Measurement_schedule_Activity;
import zj.health.health_v1.Activity.Remind_Calendar_Activity;
import zj.health.health_v1.Base.Constant;
import zj.health.health_v1.Base.Health_AppLocation;
import zj.health.health_v1.Broadcast.ClockReceiver;
import zj.health.health_v1.Broadcast.RemindReceiver;
import zj.health.health_v1.Model.CalendarMeasureNotice;
import zj.health.health_v1.Model.ReminderListModel;
import zj.health.health_v1.MyView.LoadingDialog;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.DateUtil;
import zj.health.health_v1.Utils.StringUtil;
import zj.health.health_v1.Utils.view.PostUtils;

/**
 * 用于添加新的药物提醒
 * Created by Administrator on 2018/5/13.
 */

public class AlarmManageService extends Service{

    private static AlarmManager alarmManager = null;
    private static int delay = 1000;
    private List<Long> AlarmList = new ArrayList<>();//存放当天需要被提醒的吃药时间


    // 设置时间段提醒 时间 到就发送广播
    public void sendBroadcast(long nowTime,long alarmTime) {
        Date date = new Date(alarmTime);
        AlarmManager alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        // 启动广播界面
        Intent intent = new Intent(this, ClockReceiver.class);
        intent.setAction("reminder");
//        //生成一个随机数对象
//        Random r=new Random();
        PendingIntent pendIntent = PendingIntent.getBroadcast(this,303, intent, 0);
        // triggerAtTime毫秒后发送广播，只发送一次 6.0以上的系统需要使用setExactAndAllowWhileIdle的方法
        // triggerAtMillis的参数上必须是SystemClock.elapsedRealtime()的绝对时间加上triggerAtTime延迟时间
        long triggerAtTime = alarmTime - nowTime;
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            alarmMgr.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()+triggerAtTime,pendIntent);
        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            alarmMgr.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP,SystemClock.elapsedRealtime()+triggerAtTime,pendIntent);
        }else{
            alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,SystemClock.elapsedRealtime()+triggerAtTime,pendIntent);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        getAlarmManager();
    }


    private AlarmManager getAlarmManager(){
        if(alarmManager == null){
            alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        }
        return alarmManager;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        delay = intent.getIntExtra("delay",0);
//        new Timer().schedule(new TimerTask() {
//            @Override
//            public void run() {
//                getNearlyForDate(DateUtil.getNowdayymd());
//            }
//        }, delay);
        getReminderList();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        alarmManager = null;
    }



    private void getReminderList(){
        new PostUtils().Get(Constant.reminderList,true,reminderListHandler,getApplicationContext());
    }

    private Handler reminderListHandler = new Handler(){
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){

                try {
                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
                    if(jsonObject.optString("msg").equals("success")){
                        Health_AppLocation.instance.reminderListModelList = new Gson().fromJson(jsonObject.optString("data"),new TypeToken<List<ReminderListModel>>(){}.getType());
                        if(Health_AppLocation.instance.reminderListModelList.size()>0){
                            Date nowDate = DateUtil.str2Dates2(DateUtil.getNowdayymd()+" "+"00:00:01");;
                            List<ReminderListModel> reminderListModelList = new ArrayList<>();

                            for (int i = 0;i<Health_AppLocation.instance.reminderListModelList.size();i++){
                                try {
                                    String repString = DateUtil.UTCToCST(Health_AppLocation.instance.reminderListModelList.get(i).getStartTime())+":00";
                                    Health_AppLocation.instance.reminderListModelList.get(i).setStartTime(DateUtil.dateToStamp(repString));
                                    Date startDate = DateUtil.str2Dates2(repString.
                                            substring(0,10)+" "+"00:00:00");

                                    String endRepString = DateUtil.UTCToCST(Health_AppLocation.instance.reminderListModelList.get(i).getEndTime())+":00";
                                    Health_AppLocation.instance.reminderListModelList.get(i).setEndTime(DateUtil.dateToStamp(endRepString));
                                    Date endDate = DateUtil.str2Dates2(endRepString.
                                            substring(0,10)+" "+"23:59:59");

                                    boolean isTimes = DateUtil.belongCalendar(nowDate,startDate,endDate);
                                    if(isTimes){
                                        reminderListModelList.add(Health_AppLocation.instance.reminderListModelList.get(i));
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                            if(reminderListModelList.size()>0){
                                Set<Long> set = new HashSet<>();
                                for (int i = 0;i<reminderListModelList.size();i++){
                                    for (int j = 0;j<reminderListModelList.get(i).getReminderTime().size();j++){
                                        Date time = DateUtil.str2Dates2(DateUtil.getNowdayymd()+" "
                                                +reminderListModelList.get(i).getReminderTime().get(j)+":00");
                                        set.add(time.getTime());
                                    }
                                }

                                AlarmList.addAll(set);
                                Collections.sort(AlarmList);
                                for (int i = 0;i<AlarmList.size();i++){
                                    if(AlarmList.get(i)>System.currentTimeMillis()){
                                        sendBroadcast(System.currentTimeMillis(),AlarmList.get(i));
                                        break;
                                    }
                                }
                            }
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                }
            }else if(msg.what == Constant.UserErrorCode){
                Toast.makeText(getApplicationContext(), R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(), getString(R.string.http_error), Toast.LENGTH_SHORT).show();
            }
        }
    };
}
