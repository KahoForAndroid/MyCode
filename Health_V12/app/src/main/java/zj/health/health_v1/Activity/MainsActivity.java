package zj.health.health_v1.Activity;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupCacheInfo;
import com.tencent.qcloud.presentation.viewfeatures.ConversationView;

import org.greenrobot.greendao.query.QueryBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import zj.health.health_v1.Adapter.Main_DoctorIcon_Recycleview_Adapter;
import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Base.Constant;
import zj.health.health_v1.Base.Health_AppLocation;
import zj.health.health_v1.Broadcast.ClockReceiver;
import zj.health.health_v1.Broadcast.OnePixelReceiver;
import zj.health.health_v1.IM.ui.ChatActivity;
import zj.health.health_v1.Model.BodyChartListModel;
import zj.health.health_v1.Model.DoctorOnLineModel;
import zj.health.health_v1.Model.LiveMode;
import zj.health.health_v1.Model.ReminderListModel;
import zj.health.health_v1.Model.UserInfo;
import zj.health.health_v1.MyView.AutoPollRecyclerView;
import zj.health.health_v1.MyView.LoadingDialog;
import zj.health.health_v1.R;
import zj.health.health_v1.Service.AlarmManageService;
import zj.health.health_v1.Service.DeviceService;
import zj.health.health_v1.Service.WebScoketService;
import zj.health.health_v1.Utils.DateUtil;
import zj.health.health_v1.Utils.DbUtils;
import zj.health.health_v1.Utils.GreenDaoManager;
import zj.health.health_v1.Utils.HttpUtil;
import zj.health.health_v1.Utils.PermissionUtils;
import zj.health.health_v1.Utils.SharedPreferencesUtils;
import zj.health.health_v1.Utils.StringUtil;
import zj.health.health_v1.Utils.view.PostUtils;

/**
 * Created by Administrator on 2018/4/2.
 */

public class MainsActivity extends BaseActivity implements View.OnClickListener {

    private LineChart lineChart;
    private AutoPollRecyclerView main_doctor_iconhead_recy;
    private RelativeLayout Health_log_rela,Doctor_service_rela,
            goto_Consultation_Room_rela,remind_rela,Personal_Center;
    private TextView Scrolling_TextView;
    private ImageView refresh_img,room_refresh_img;
    private Main_DoctorIcon_Recycleview_Adapter adapter = null;
    private List<DoctorOnLineModel> doctorOnLineModelList = new ArrayList<>();
    private Intent it = null;
    private long time = 0;
    private Gson gson = new Gson();
    public static DeviceService mBluetoothLeService;//蓝牙服务,用于蓝牙手环 目前预留
    private List<Long> AlarmList = new ArrayList<>();//存放当天需要被提醒的吃药时间
    private boolean needGetRminder ;
//    private OnePixelReceiver onePixelReceiver;
    private static String mDeviceAddress;
    private static int DriveType;
    public static boolean isConnected_once = false; //手环之前是否已经连接过(包括连接后断开)
    private Handler audit_passHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                if(msg.obj!=null){
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        if(jsonObject.optString("msg").equals("success")){
                            DbUtils dbUtils = new DbUtils();
                            Health_AppLocation.instance.users.setDoctorStatus("2");
                            dbUtils.updateUsersData(MainsActivity.this,Health_AppLocation.instance.users);
                            goto_Consultation_Room_rela.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(MainsActivity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                }
            }else if(msg.what == Constant.UserErrorCode){
                Toast.makeText(MainsActivity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(MainsActivity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
            }
        }
    };

    private Handler OnLineHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                if(msg.obj!=null){
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        if(jsonObject.optString("msg").equals("success")){
                            doctorOnLineModelList = gson.fromJson(jsonObject.optString("data"),new TypeToken<List<DoctorOnLineModel>>(){}.getType());
                            if(doctorOnLineModelList.size()<5){
                                Scrolling_TextView.setText(getString(R.string.have).toString()+doctorOnLineModelList.size()+getString(R.string.scroll_doctor_tips_text).toString());
                                for (int i = doctorOnLineModelList.size()-1;i<5;i++){
                                    doctorOnLineModelList.add(new DoctorOnLineModel());
                                }
                            }
//                            else{
//                                Scrolling_TextView.setText(getString(R.string.have).toString()+doctorOnLineModelList.size()+getString(R.string.scroll_doctor_tips_text).toString());
//                                if(doctorOnLineModelList.size()<5){
//                                    for (int i = 0;i<5;i++){
//                                        doctorOnLineModelList.add(new DoctorOnLineModel());
//                                    }
//                                }
//                            }
                            adapter = new Main_DoctorIcon_Recycleview_Adapter(MainsActivity.this,doctorOnLineModelList);
                            main_doctor_iconhead_recy.setLayoutManager(new LinearLayoutManager(MainsActivity.this, LinearLayoutManager.HORIZONTAL, false));
                            main_doctor_iconhead_recy.setAdapter(adapter);
                            main_doctor_iconhead_recy.start();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(MainsActivity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                }
            }else if(msg.what == Constant.UserErrorCode){
                Toast.makeText(MainsActivity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(MainsActivity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
            }
        }
    };

    private Handler reminderListHandler = new Handler(){
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                needGetRminder = false;
                try {
                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
                    if(jsonObject.optString("msg").equals("success")){
                        Health_AppLocation.instance.reminderListModelList = gson.fromJson(jsonObject.optString("data"),new TypeToken<List<ReminderListModel>>(){}.getType());
                        if(Health_AppLocation.instance.reminderListModelList.size()>0){
                            Date nowDate = DateUtil.str2Dates2(DateUtil.getNowdayymd()+" "+"00:00:01");;
                            List<ReminderListModel> reminderListModelList = new ArrayList<>();

                            for (int i = 0;i<Health_AppLocation.instance.reminderListModelList.size();i++){
                                try {
                                    String repString = Health_AppLocation.instance.reminderListModelList.get(i).getStartTime().replace("T"," ");
                                    String startString = repString.
                                            substring(0,repString.indexOf("."));
                                    Health_AppLocation.instance.reminderListModelList.get(i).setStartTime(DateUtil.dateToStamp(startString));
                                    Date startDate = DateUtil.str2Dates2(repString.
                                            substring(0,10)+" "+"00:00:00");

                                    String endRepString = Health_AppLocation.instance.reminderListModelList.get(i).getEndTime().replace("T"," ");
                                    String endString = endRepString.
                                            substring(0,endRepString.indexOf("."));

                                    Health_AppLocation.instance.reminderListModelList.get(i).setEndTime(DateUtil.dateToStamp(endString));
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
                        LoadingDialog.getLoadingDialog().StopLoadingDialog();
                    }else{
                        LoadingDialog.getLoadingDialog().StopLoadingDialog();
                        Toast.makeText(MainsActivity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    LoadingDialog.getLoadingDialog().StopLoadingDialog();
                    Toast.makeText(MainsActivity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                }
            }else if(msg.what == Constant.UserErrorCode){
                LoadingDialog.getLoadingDialog().StopLoadingDialog();
                Toast.makeText(MainsActivity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                LoadingDialog.getLoadingDialog().StopLoadingDialog();
                Toast.makeText(MainsActivity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
            }
        }
    };

    private Handler HeartList_pressureHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(msg.obj.toString());
                    if(jsonObject.optString("msg").equals("success")) {
                        JSONObject jsonObject1 = new JSONObject(jsonObject.optString("data"));
                        if(jsonObject.optString("data") == null ){
                            bindBleService(System.currentTimeMillis() - 1000 * 60 * 5);
                        }else{
                            try {
                                BodyChartListModel bodyChartListModel = gson.fromJson(jsonObject.optString("data"),BodyChartListModel.class);
                                bindBleService(DateUtil.UTCToCST_long(bodyChartListModel.getRecordTime()));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }
//                        List<BodyChartListModel> bodyChartListModelList = gson.fromJson(jsonObject1.optString("list"),new TypeToken<List<BodyChartListModel>>(){}.getType());
//                        if(bodyChartListModelList!=null && bodyChartListModelList.size()>0){
//                            try {
//                                bindBleService(DateUtil.UTCToCST_long(bodyChartListModelList.get(0).getRecordTime()));
//                            } catch (ParseException e) {
//                                e.printStackTrace();
//                            }
//                        }else{
//                            bindBleService(System.currentTimeMillis() - 1000 * 60 * 5);
//                        }

                    }else if(jsonObject.optString("code").equals("2")){
                        bindBleService(System.currentTimeMillis() - 1000 * 60 * 5);
                    }else{
                        Toast.makeText(MainsActivity.this,"连接异常,请稍后再试", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if(msg.what == Constant.UserErrorCode){
                Toast.makeText(MainsActivity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(MainsActivity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        BindListener();
        initData();

//        bindBleService();

    }

    private void initView(){
        main_doctor_iconhead_recy = (AutoPollRecyclerView)findViewById(R.id.main_doctor_iconhead_recy);
        Health_log_rela = (RelativeLayout)findViewById(R.id.Health_log_rela);
        Doctor_service_rela = (RelativeLayout)findViewById(R.id.Doctor_service_rela);
        goto_Consultation_Room_rela = (RelativeLayout)findViewById(R.id.goto_Consultation_Room_rela);
        remind_rela = (RelativeLayout)findViewById(R.id.remind_rela);
        Personal_Center = (RelativeLayout)findViewById(R.id.Personal_Center);
        Scrolling_TextView = (TextView)findViewById(R.id.Scrolling_TextView);
        refresh_img = (ImageView)findViewById(R.id.refresh_img);
        room_refresh_img = (ImageView)findViewById(R.id.room_refresh_img);
    }
    private void BindListener(){
        Health_log_rela.setOnClickListener(this);
        Doctor_service_rela.setOnClickListener(this);
        goto_Consultation_Room_rela.setOnClickListener(this);
        remind_rela.setOnClickListener(this);
        Personal_Center.setOnClickListener(this);
        Scrolling_TextView.setOnClickListener(this);
    }
    private void initData(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("imflag");
        registerReceiver(imFlagBroadReceiver,intentFilter);

        IntentFilter driveIntentFilter = new IntentFilter();
        driveIntentFilter.addAction("connected");
        driveIntentFilter.addAction("disconnected_device");
        registerReceiver(connectBroad,driveIntentFilter);

            Intent it = new Intent(this, AlarmManageService.class);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                startForegroundService(it);
//            }else{
//                startService(it);
//            }
            startService(it);
            Intent ita = new Intent(this, WebScoketService.class);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                startForegroundService(ita);
//            }else{
//                startService(ita);
//            }
        startService(ita);


//        onePixelReceiver = new OnePixelReceiver();
//        IntentFilter intentFilter1 = new IntentFilter();
//        intentFilter.addAction("android.intent.action.SCREEN_OFF");
//        intentFilter.addAction("android.intent.action.SCREEN_ON");
//        intentFilter.addAction("android.intent.action.USER_PRESENT");
//        registerReceiver(onePixelReceiver,intentFilter1);
        needGetRminder = true;
        if(Health_AppLocation.instance.users.getDoctorStatus()!=null && Health_AppLocation.instance.users.getDoctorStatus().equals("2")){
             goto_Consultation_Room_rela.setVisibility(View.VISIBLE);
        }else{
            goto_Consultation_Room_rela.setVisibility(View.GONE);
            if(Health_AppLocation.instance.users.getDoctorStatus().equals("0")){
                audit_pass();
            }else{
                goto_Consultation_Room_rela.setVisibility(View.GONE);
            }
        }
        time = System.currentTimeMillis();

        getDoctorOnLine();
        getIMData_toMe();

    }


    /**
     * 通过医生审核(目前预留)
     */
    private void audit_pass(){
        Map<String,String> map = new HashMap<>();
        map.put("userId",Health_AppLocation.instance.users.getId());
        new PostUtils().getNewPost(Constant.audit_pass,map,audit_passHandler,this);
    }

    /**
     * 获取在线医生
     */
    private void getDoctorOnLine(){
        new PostUtils().Get(Constant.getDoctorOnLine,true,OnLineHandler,this);
    }


    /**
     * 绑定设备服务
     */
    public void bindBleService(long TimeMillis) {
        if(!isConnected_once){
            isConnected_once = true;
            Intent gattServiceIntent = new Intent(this, DeviceService.class);
            gattServiceIntent.putExtra("TimeMillis",TimeMillis);
            bindService(gattServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
        }else{
            mBluetoothLeService.connect(mDeviceAddress,DriveType);
            Intent it = new Intent(this,DeviceService.class);
            it.setAction("for_im_to_this");
            sendBroadcast(it);
            bindService(it,mServiceConnection,Context.BIND_ABOVE_CLIENT);
        }
    }


    public static final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((DeviceService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
//                Toast.makeText(MainsActivity.this, "", Toast.LENGTH_SHORT).show();
            }
            // Automatically connects to the device upon successful start-up initialization
            mBluetoothLeService.connect(mDeviceAddress,DriveType);

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
//            if(mServiceConnection!=null){
//                unbindService(mServiceConnection);
//            }
            mBluetoothLeService = null;
//            bindBleService();
        }
    };

    private void getIMData_toMe(){
//        LoadingDialog.getLoadingDialog().StartLoadingDialog(this);
        //获取自己的资料
        TIMFriendshipManager.getInstance().getSelfProfile(new TIMValueCallBack<TIMUserProfile>(){
            @Override
            public void onError(int code, String desc){
                //错误码 code 和错误描述 desc，可用于定位请求失败原因
                //错误码 code 列表请参见错误码表
//                Log.e(tag, "getSelfProfile failed: " + code + " desc");
//                LoadingDialog.getLoadingDialog().StopLoadingDialog();
            }

            @Override
            public void onSuccess(TIMUserProfile result){
//                Log.e(tag, "getSelfProfile succ");
//                Log.e(tag, "identifier: " + result.getIdentifier() + " nickName: " + result.getNickName()
//                        + " remark: " + result.getRemark() + " allow: " + result.getAllowType());
//                LoadingDialog.getLoadingDialog().StopLoadingDialog();
                if(StringUtil.isEmpty(result.getFaceUrl()) || result.getNickName().contains("?")){
                    String name = Health_AppLocation.instance.users.getNickname();
                    setIM_Icon(Health_AppLocation.instance.Icon,name);
                }
//                setIM_Icon(Health_AppLocation.instance.Icon,Health_AppLocation.instance.users.getNickname());
            }
        });
    }


    /**
     * 设置自己的腾讯IM资料
     * @param url 用户头像地址
     * @param nickName 用户名
     */
    private void setIM_Icon(String url,String nickName){
        TIMFriendshipManager.ModifyUserProfileParam param = new TIMFriendshipManager.ModifyUserProfileParam();
        param.setFaceUrl(url);
        param.setNickname(nickName);

        TIMFriendshipManager.getInstance().modifyProfile(param, new TIMCallBack() {
            @Override
            public void onError(int code, String desc) {
                //错误码 code 和错误描述 desc，可用于定位请求失败原因
                //错误码 code 列表请参见错误码表
            }

            @Override
            public void onSuccess() {
                Log.e("mainActivity", "设置个人IM头像成功");
            }
        });
    }





    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.Health_log_rela:
                it = new Intent(this,Health_Log_Activity.class);
                startActivity(it);
                break;
            case R.id.Doctor_service_rela:
                if(refresh_img.getVisibility() == View.VISIBLE){
                    refresh_img.setVisibility(View.GONE);
                }
                Health_AppLocation.instance.ImFlag = 0;
                it = new Intent(this,Doctor_service_Activity.class);
                startActivity(it);
                break;
            case R.id.goto_Consultation_Room_rela:
                if(room_refresh_img.getVisibility() == View.VISIBLE){
                    room_refresh_img.setVisibility(View.GONE);
                }
                Health_AppLocation.instance.ImFlag = 0;
                it = new Intent(this,Consultation_Room_Activity.class);
                startActivity(it);
                break;
            case R.id.remind_rela:
                 it = new Intent(this,Remind_Calendar_Activity.class);
                startActivity(it);
//                Toast.makeText(this, getString(R.string.coming_opening), Toast.LENGTH_SHORT).show();
                break;
            case R.id.Personal_Center:
                 it = new Intent(this,My_Activity.class);
                startActivity(it);
                break;
            case R.id.Scrolling_TextView:
                it = new Intent(this,Free_doctor_List_Activity.class);
                startActivity(it);
                break;
        }
    }
    private void getReminderList(){
        new PostUtils().Get(Constant.reminderList,true,reminderListHandler,this);
    }

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
            alarmMgr.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pendIntent);
        }else{
            alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pendIntent);
        }


    }




    @Override
    protected void onResume() {
        super.onResume();
//        if (needGetRminder){
////            LoadingDialog.getLoadingDialog().StartLoadingDialog(this);
//            getReminderList();
//        }
        if(Health_AppLocation.instance.ImFlag == 1){
            refresh_img.setVisibility(View.VISIBLE);
            room_refresh_img.setVisibility(View.GONE);
        }else if(Health_AppLocation.instance.ImFlag == 2){
            refresh_img.setVisibility(View.GONE);
            room_refresh_img.setVisibility(View.VISIBLE);
        }else{
            refresh_img.setVisibility(View.GONE);
            room_refresh_img.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    //退出方法
    private void exit() {
        //如果在两秒大于2秒
        if (System.currentTimeMillis() - time > 2000) {
        //获得当前的时间
            time = System.currentTimeMillis();
            Toast.makeText(this, "再点击一次退出应用程序", Toast.LENGTH_SHORT).show();
        } else {

            Intent it = new Intent(this, AlarmManageService.class);
            stopService(it);
            //点击在两秒以内
            finish();//执行移除所以Activity方法
        }
    }

    /**
     * 获取指定日期的图表数据(列表数据)
     * 下面参数指的是时间区间 搜索指定时间范围内的数据
     */
    private void getHeart_for_List(){
        new PostUtils().Get(Constant.data_heart_rate_latest,true,HeartList_pressureHandler,this);
    }


    private BroadcastReceiver imFlagBroadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("imflag")){
                if(Health_AppLocation.instance.ImFlag == 1){
                    refresh_img.setVisibility(View.VISIBLE);
                    room_refresh_img.setVisibility(View.GONE);
                }else if(Health_AppLocation.instance.ImFlag == 2){
                    refresh_img.setVisibility(View.GONE);
                    room_refresh_img.setVisibility(View.VISIBLE);
                }else{
                    refresh_img.setVisibility(View.GONE);
                    room_refresh_img.setVisibility(View.GONE);
                }
            }
        }
    };

    private BroadcastReceiver connectBroad = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("connected")){
                mDeviceAddress = intent.getStringExtra("mDeviceAddress");
                DriveType = intent.getIntExtra("DriveType",0);
                if(mDeviceAddress!=null && DriveType!=0){
                    getHeart_for_List();
                }
            }else if(intent.getAction().equals("disconnected_device")){
                if(mServiceConnection!=null){
                    unbindService(mServiceConnection);
                }
                mBluetoothLeService.disconnect();
                stopService(new Intent(MainsActivity.this,DeviceService.class));
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mServiceConnection!=null){
            unbindService(mServiceConnection);
        }
        stopService(new Intent(this,DeviceService.class));
        unregisterReceiver(imFlagBroadReceiver);
        unregisterReceiver(connectBroad);
//        unregisterReceiver(onePixelReceiver);
//        Intent it = new Intent()
//        stopService(mBluetoothLeService);
        mBluetoothLeService.disconnect();
        mBluetoothLeService = null;
    }


}

