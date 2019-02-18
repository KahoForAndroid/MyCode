package zj.health.health_v1.Activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import zj.health.health_v1.Adapter.SearchDevice_Adapter;
import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Base.BlueToothConstant;
import zj.health.health_v1.Base.Health_AppLocation;
import zj.health.health_v1.Implements.OnItemClick;
import zj.health.health_v1.MyView.CreateDialog;
import zj.health.health_v1.MyView.LoadingDialog;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.StringUtil;

/**
 * Created by Administrator on 2018/6/19.
 */

public class Search_BlueToothDevice_Activity extends BaseActivity{

    private TextView title,serch_again_text;
    private ImageView back;
    private RecyclerView device_recy;
    private SearchDevice_Adapter adapter;
    private List<BluetoothDevice> bluetoothDeviceList = new ArrayList<>();
    private BluetoothGatt bluetoothGatt;
    private Timer timer;
    private int times = 15;//设备只会搜索15秒
    private Handler timerHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                LoadingDialog.getLoadingDialog().StopLoadingDialog();
                Health_AppLocation.instance.mBluetoothAdapter.stopLeScan(mLeScanCallBack);
                times = 15;
                timer.cancel();
            }
        }
    };
    private Handler popupHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    timer = new Timer();
                    timer.schedule(new TimerTask(){

                        @Override
                        public void run() {
                            times --;
                            if(times == 0){
                                Message message = new Message();
                                message.what = 1;
                                timerHandler.sendMessage(message);
                            }
                        }
                    },0,1000);
                    startSearchDevice();
                    break;
            }
        }

    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_bluetooth_activity);
        initView();
        BindListener();
        setData();
        popupHandler.sendEmptyMessageDelayed(0, 1000);
    }
    private void initView(){
        title = (TextView)findViewById(R.id.title);
        back = (ImageView)findViewById(R.id.back);
        device_recy = (RecyclerView)findViewById(R.id.device_recy);
        serch_again_text = (TextView)findViewById(R.id.serch_again_text);
    }
    private void BindListener(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
        serch_again_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothDeviceList.clear();
                adapter.notifyDataSetChanged();
                popupHandler.sendEmptyMessageDelayed(0, 300);
            }
        });
    }
    private void setData(){
        title.setText(getString(R.string.search_device));
        adapter = new SearchDevice_Adapter(this,bluetoothDeviceList);
        device_recy.setLayoutManager(new LinearLayoutManager(this));
        device_recy.setAdapter(adapter);
        adapter.setOnItemClick(new OnItemClick() {
            @Override
            public void OnItemClickListener(View view, int position) {
                Health_AppLocation.instance.mBluetoothAdapter.stopLeScan(mLeScanCallBack);
                timer.cancel();
                Intent it = new Intent();
                it.putExtra("TYPE",getIntent().getIntExtra("TYPE",0));
                it.putExtra("device",bluetoothDeviceList.get(position));
                int TYPE = getIntent().getIntExtra("TYPE",0);
                if(TYPE == 1){
                    setResult(BlueToothConstant.BODY_BLOODPRESSIRE_CONNECTRESULT,it);
                }else  if(TYPE == 2){
                    setResult(BlueToothConstant.BODY_WEIGHT_CONNECTRESULT,it);
                }else  if(TYPE == 3){
                    setResult(BlueToothConstant.BODY_TEMPERATURE_CONNECTRESULT,it);
                }else  if(TYPE == 4){
                    setResult(BlueToothConstant.BODY_HEARTRATE_CONNECTRESULT,it);
                }else  if(TYPE == 1){
                    setResult(BlueToothConstant.BODY_BLOODSUGAR_CONNECTRESULT,it);
                }
                finish();
            }
        });


    }


    /**
     * 开始搜索蓝牙BLE设备
     */
    private void startSearchDevice(){
        LoadingDialog.getLoadingDialog().StartLoadingDialog(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Health_AppLocation.instance.mBluetoothAdapter.startLeScan(mLeScanCallBack);
            }
        }).start();
    }


//    /**
//     * 默认搜索15秒
//     */
//    private TimerTask timerTask = new TimerTask() {
//        @Override
//        public void run() {
//            times --;
//            if(times == 0){
//                Message message = new Message();
//                message.what = 1;
//                timerHandler.sendMessage(message);
//            }
//        }
//    };


    /**
     * 搜索到的设备将回调到这里
     */
    private BluetoothAdapter.LeScanCallback mLeScanCallBack = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, final byte[] scanRecord) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    synchronized (this){
                        boolean isEquals = false;
                            if(!StringUtil.isEmpty(device.getName()) && device.getName().contains("GT101")){
                                for (int i = 0; i<bluetoothDeviceList.size();i++){
                                    if(bluetoothDeviceList.get(i).getAddress().equals(device.getAddress())){
                                        isEquals = true;
                                    }
                                }
                                if(!isEquals){
                                    bluetoothDeviceList.add(device);
                                }
                            }
//                        boolean isContains = false;
//                        for(int i = 0; i<bluetoothDeviceList.size();i++){
//                            if(bluetoothDeviceList.get(i).getAddress().equals(device.getAddress())){
//                                isContains = true;
//                                break;
//                            }else{
//                                isContains = false;
//                            }
//                        }
//                        if(!isContains){
//                            bluetoothDeviceList.add(device);
//                        }
                        adapter.setBluetoothDeviceList(bluetoothDeviceList);
                        String a = StringUtil.bytesToHexString(scanRecord);
                        String b = StringUtil.decode(a);

                    }


                }
            });
        }

    };




}
