package zj.health.health_v1.Base;

import android.Manifest;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.multidex.MultiDexApplication;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

//import com.squareup.leakcanary.LeakCanary;
import com.tencent.imsdk.TIMGroupReceiveMessageOpt;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMOfflinePushListener;
import com.tencent.imsdk.TIMOfflinePushNotification;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.qalsdk.sdk.MsfSdkUtils;

import java.util.ArrayList;
import java.util.List;

import zj.health.health_v1.Activity.MainsActivity;
import zj.health.health_v1.IM.utils.Foreground;
import zj.health.health_v1.Model.AllCheckFilterModel;
import zj.health.health_v1.Model.BlueToothDeviceModel;
import zj.health.health_v1.Model.BodyModel;
import zj.health.health_v1.Model.Contacts;
import zj.health.health_v1.Model.DeviceType;
import zj.health.health_v1.Model.ReminderListModel;
import zj.health.health_v1.Model.UserInfo;
import zj.health.health_v1.Model.UserMessage;
import zj.health.health_v1.Model.Users;
import zj.health.health_v1.R;
import zj.health.health_v1.Service.AlarmManageService;
import zj.health.health_v1.Utils.GreenDaoManager;

/**
 * Created by Administrator on 2018/4/2.
 */

public class Health_AppLocation extends Application {

    public static Health_AppLocation instance = null;
    public DisplayMetrics displayMetrics = null;
    public List<AppCompatActivity> ActivityList = new ArrayList<>();
    public List<BlueToothDeviceModel> blueToothDeviceModels = new ArrayList<>();
    public List<ReminderListModel> reminderListModelList = new ArrayList<>();
    public List<BodyModel> bodyModelList = new ArrayList<>();
    public boolean isPermissionPass = false;
    public BluetoothManager mBluetoothManager;
    public BluetoothAdapter mBluetoothAdapter;
    public String Token;
    public String Icon;
    public Users users;
    public UserMessage userMessage;
    public IWXAPI iwxapi;
    public int ImFlag = 0;
    public long UpdateHeartTime;


    @Override
    public void onCreate() {
        super.onCreate();
        if(instance == null){
            instance = this;
        }
//        MobSDK.init(this);
        GreenDaoManager.getInstance();
        displayMetrics = new DisplayMetrics();
        mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();
//        LeakCanary.install(this);
        Foreground.init(this);
        if(MsfSdkUtils.isMainProcess(this)) {
            TIMManager.getInstance().setOfflinePushListener(new TIMOfflinePushListener() {
                @Override
                public void handleNotification(TIMOfflinePushNotification notification) {
                    if (notification.getGroupReceiveMsgOpt() == TIMGroupReceiveMessageOpt.ReceiveAndNotify) {
                        //消息被设置为需要提醒
                        notification.doNotify(getApplicationContext(), R.mipmap.ic_launcher);
                    }
                }
            });
        }
        Token  = Health_AppLocation.instance.
                getSharedPreferences("loginToken", 0).
                getString("token",null);
        Icon  = Constant.photo_IP+Health_AppLocation.instance.
                getSharedPreferences("loginToken", 0).
                getString("iconUrl",null);
        iwxapi = WXAPIFactory.createWXAPI(this,Constant.WeChat_ShareID,true);
        iwxapi.registerApp(Constant.WeChat_ShareID);


    }


    public void CleanActivity(){
//        for(int i = 0;i<ActivityList.size();i++){
//            if(!(ActivityList.get(i) instanceof BaseActivity)){
//                ActivityList.get(i).finish();
//            }
//        }
        ActivityList.clear();
    }






}
