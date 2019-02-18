package zj.health.health_v1.Fragment;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import zj.health.health_v1.Activity.BlueTooth_Data_Activity;
import zj.health.health_v1.Activity.Health_Log_Activity;
import zj.health.health_v1.Activity.MainsActivity;
import zj.health.health_v1.Activity.NewSearch_BlueTooth_Activity;
import zj.health.health_v1.Activity.Search_BlueToothDevice_Activity;
import zj.health.health_v1.Adapter.BlueToothDeviceListAdapter;
import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Base.BlueToothConstant;
import zj.health.health_v1.Base.Constant;
import zj.health.health_v1.Base.Health_AppLocation;
import zj.health.health_v1.Implements.OnItemClick;
import zj.health.health_v1.Model.BlueToothDeviceModel;
import zj.health.health_v1.Model.ReportModel;
import zj.health.health_v1.Model.UserDevices;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.BloodBlueToothUtil.BloodUtil;
import zj.health.health_v1.Utils.DateUtil;
import zj.health.health_v1.Utils.StringUtil;
import zj.health.health_v1.Utils.view.PostUtils;

/**
 * Created by Administrator on 2018/5/10.
 */

public class Body_blood_Fragment extends Fragment implements View.OnClickListener{

    private View view = null;
    private RelativeLayout date_rela;
    private TextView dateText,device_null_text;
    private EditText systolic_edit,diastolic_edit,heart_rate_edit;
    private Button submit_Button;
    private ImageView add_bluetooth;
    private TimePickerView timePickerView = null;
    private  int DEVICEID = 1;
    private  int systolicId = 7;
    private  int diastolicId = 8;
    private  int heart_rateId = 3;
    private String time = null;//只拿时分
    private UserDevices userDevices = null;
    private Bundle bundle = new Bundle();
    private BlueTooth_Data_Activity activity;
    private int TYPE =1;
    private List<BlueToothDeviceModel> blueToothDeviceModellist = new ArrayList<>();
    private RecyclerView deviceList_recy;
    private BlueToothDeviceListAdapter adapter;
    private boolean SearchSuccess = false;
    private long timesLong;


    //用于蓝牙设备变量
    private IntentFilter intentFilter = new IntentFilter();
    // 获取到向设备写的输出流，全局变量，否则连接在方法执行完就结束了
    private OutputStream os;
    private InputStream in;
    private Thread listenThread;
    private boolean listen = true;
    private BloodUtil bloodUtil;


    private Handler DeviceCallBackHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                Message message = new Message();
                message.what = msg.what;
                activity.getMeasureStatusHandler().sendMessage(message);
                int device_measure_result []= (int[]) msg.obj;
                try {
                    in.close();
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                systolic_edit.setText(device_measure_result[0]+"");
                diastolic_edit.setText(device_measure_result[1]+"");
                heart_rate_edit.setText(device_measure_result[2]+"");
                Toast.makeText(getActivity(), getString(R.string.measure_success_post), Toast.LENGTH_SHORT).show();
            }else if(msg.what == 100){
                try {
                    in.close();
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Message message = new Message();
                message.what = msg.what;
                activity.getMeasureStatusHandler().sendMessage(message);
                Toast.makeText(getActivity(), getString(R.string.device_measure_error), Toast.LENGTH_SHORT).show();
            }else if(msg.what == 404){
                Toast.makeText(activity, getString(R.string.connect_blooddevice_error), Toast.LENGTH_SHORT).show();
            }else if(msg.what == 199){
                Message message = new Message();
                message.what = msg.what;
                activity.getMeasureStatusHandler().sendMessage(message);
            }
        }
    };

    private Handler submitHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                if (msg.obj!=null) {
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        if(jsonObject.optString("msg").equals("success")){
                            JSONObject jsonObject1 = new JSONObject();
                            jsonObject1.put("heartRate",heart_rate_edit.getText().toString());
                            jsonObject1.put("type",1);
                            jsonObject1.put("recordTime",timesLong);
                            SubmitHeart(jsonObject1);
                        }else{
                            Toast.makeText(getActivity(), getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getActivity(), getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                }
            }else if(msg.what == Constant.UserErrorCode){
                Toast.makeText(getActivity(), R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getActivity(), getString(R.string.http_error), Toast.LENGTH_SHORT).show();
            }
        }
    };

    private Handler submitHeart_Handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                if (msg.obj!=null) {
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        if(jsonObject.optString("msg").equals("success")){
                            Health_Log_Activity.isUploadGraphic = true;
                            Toast.makeText(Health_AppLocation.instance, getString(R.string.commit_success), Toast.LENGTH_SHORT).show();
                            getActivity().setResult(1);
                            getActivity().finish();
                        }else{
                            Toast.makeText(getActivity(), getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getActivity(), getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                }
            }else if(msg.what == Constant.UserErrorCode){
                Toast.makeText(getActivity(), R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getActivity(), getString(R.string.http_error), Toast.LENGTH_SHORT).show();
            }
        }
    };


    private Handler BlueToothPermissionHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                if (!Health_AppLocation.instance.mBluetoothAdapter.isEnabled()) {
                    Health_AppLocation.instance.mBluetoothAdapter.enable();
                }
                if (activity.isPermissionPass) {
//                    Intent it = new Intent(getActivity(), NewSearch_BlueTooth_Activity.class);
//                    it.putExtra("TYPE", TYPE);
//                    startActivityForResult(it, 0x2);
                    Health_AppLocation.instance.mBluetoothAdapter.startDiscovery();
                } else {
                    Toast.makeText(getActivity(), getString(R.string.location_open_tips), Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null){
            view = inflater.inflate(R.layout.body_blood_fragment,container,false);
        }
        initView();
        BindListener();
        setData();
        CheckDeviceList();
        return view;
    }
    private void initView(){
        date_rela = (RelativeLayout)view.findViewById(R.id.date_rela);
        dateText = (TextView)view.findViewById(R.id.dateText);
        systolic_edit = (EditText)view.findViewById(R.id.systolic_edit);
        diastolic_edit = (EditText)view.findViewById(R.id.diastolic_edit);
        heart_rate_edit = (EditText)view.findViewById(R.id.heart_rate_edit);
        submit_Button = (Button)view.findViewById(R.id.submit_Button);
        add_bluetooth = (ImageView)view.findViewById(R.id.add_bluetooth);
        device_null_text = (TextView)view.findViewById(R.id.device_null_text);
        deviceList_recy = (RecyclerView)view.findViewById(R.id.deviceList_recy);
    }
    private void BindListener(){
        submit_Button.setOnClickListener(this);
        add_bluetooth.setOnClickListener(this);
        date_rela.setOnClickListener(this);
    }
    private void setData(){
        bloodUtil = new BloodUtil();
        dateText.setText(DateUtil.getNowDay());
        time = DateUtil.getNowDayHM();
        timesLong = System.currentTimeMillis();
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        getActivity().registerReceiver(new MyBtReceiver(),intentFilter);


        TimePickerView.OnTimeSelectListener listener = new TimePickerView.OnTimeSelectListener(){

            @Override
            public void onTimeSelect(Date date, View v) {
                time = DateUtil.getDaysStrhm(date);
                timesLong = date.getTime();
                dateText.setText(DateUtil.getNowdayymd()+" "+DateUtil.getDaysStrhm(date));
            }
        };
        timePickerView = new DateUtil().ShowTimePickerView(getActivity(),listener, Calendar.getInstance(),
                false,false,false,true,true,false);
    }


    private void CheckDeviceList(){
        blueToothDeviceModellist.clear();
        adapter = new BlueToothDeviceListAdapter(getActivity(),blueToothDeviceModellist);
        adapter.setOnItemClick(new OnItemClick() {
            @Override
            public void OnItemClickListener(View view, int position) {
                //查看连接状态，如果为空的话需要重新连接
                if(activity.clientSocket!=null){
                    boolean search = false;
                    //循环检索是否有血压仪,如果没有的话也需要重新连接获取
                    for (int i = 0;i<blueToothDeviceModellist.size();i++){
                        if(bloodUtil.isBPDevice(blueToothDeviceModellist.get(position).getBluetoothDevice())){
                            connDevice(blueToothDeviceModellist.get(position).getBluetoothDevice(),DeviceCallBackHandler);
                            search = true;
                            break;
                        }
                    }
                    if(!search){
                        Toast.makeText(getActivity(), getString(R.string.connect_outtime_error), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getActivity(), getString(R.string.connect_outtime_error), Toast.LENGTH_SHORT).show();
                }
            }
        });

        if(Health_AppLocation.instance.blueToothDeviceModels.size() > 0){
            device_null_text.setVisibility(View.GONE);
            for (int i = 0;i<Health_AppLocation.instance.blueToothDeviceModels.size();i++){
                if(Health_AppLocation.instance.blueToothDeviceModels.get(i).getFromType() == TYPE){
                    blueToothDeviceModellist.add(Health_AppLocation.instance.blueToothDeviceModels.get(i));
                }
            }
            if(blueToothDeviceModellist.size() == 0){
                deviceList_recy.setVisibility(View.GONE);
                device_null_text.setVisibility(View.VISIBLE);
            }else{
                deviceList_recy.setVisibility(View.VISIBLE);
                deviceList_recy.setLayoutManager(new LinearLayoutManager(getActivity()));
                deviceList_recy.setAdapter(adapter);
            }
        }else{
            deviceList_recy.setVisibility(View.GONE);
            device_null_text.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 提交测量数据
     */
    private void Submit(JSONObject jsonObject){

        new PostUtils().JsonPost(Constant.post_blood_pressure,jsonObject,submitHandler, (BaseActivity) getActivity());
    }


    /**
     * 提交测量数据
     */
    private void SubmitHeart(JSONObject jsonObject){

        new PostUtils().JsonPost(Constant.post_heart_rate,jsonObject,submitHeart_Handler, (BaseActivity) getActivity());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit_Button:
                if (systolic_edit.getText().toString().length()<=0
                        || diastolic_edit.getText().toString().length()<=0
                        || heart_rate_edit.getText().toString().length()<=0) {
                    Toast.makeText(getActivity(), getString(R.string.all_input_tips), Toast.LENGTH_SHORT).show();
                } else {

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("systolicPressure",systolic_edit.getText().toString());
                        jsonObject.put("diastolicPressure",diastolic_edit.getText().toString());
                        jsonObject.put("heartRate",heart_rate_edit.getText().toString());
                        jsonObject.put("recordTime",timesLong);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Submit(jsonObject);
                }
                break;
            case R.id.add_bluetooth:
                if(activity.isPermissionPass){
                    if (!Health_AppLocation.instance.mBluetoothAdapter.isEnabled()) {
                        Health_AppLocation.instance.mBluetoothAdapter.enable();
                    }
                    if (activity.isPermissionPass) {
//                        Intent it = new Intent(getActivity(), NewSearch_BlueTooth_Activity.class);
//                        it.putExtra("TYPE", TYPE);
//                        startActivityForResult(it, 0x2);
                        Health_AppLocation.instance.mBluetoothAdapter.startDiscovery();
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.location_open_tips), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    activity.setBlueToothPermission(BlueToothPermissionHandler);
                }
                break;
            case R.id.date_rela:
                timePickerView.show();
                break;
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (BlueTooth_Data_Activity) context;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == BlueToothConstant.BODY_BLOODPRESSIRE_CONNECTRESULT){
            activity.isPermissionPass = true;
            BluetoothDevice bluetoothDevice = data.getParcelableExtra("device");
            BluetoothDevice device = null;

            if(bluetoothDevice!=null){
                device = Health_AppLocation.instance.mBluetoothAdapter.getRemoteDevice(bluetoothDevice.getAddress());
                if(MainsActivity.mBluetoothLeService!=null){
//                            MainsActivity.mBluetoothLeService.mBluetoothGatt = device.connectGatt(getActivity(),false,MainsActivity.mBluetoothLeService.mGattCallback);
                    MainsActivity.mBluetoothLeService.connect(device.getAddress(),TYPE);
                }else{
                    Toast.makeText(activity, getString(R.string.connect_device_error), Toast.LENGTH_SHORT).show();

                }

            }else{
                Toast.makeText(activity, getString(R.string.connect_device_error), Toast.LENGTH_SHORT).show();
            }
        }
    }

//    private void unlisten() {
//        listen = false;
//        if(listenThread!=null){
//            if (listenThread.isAlive())
//                listenThread.interrupt();
//            listenThread = null;
//        }
//    }



    private void listen() {
        listenThread = new Thread(){
            @Override
            public void run() {
                int len;
                byte[] bytes = new byte[20];
                try {
                    while (listen) {
                        if ((len = in.available()) > 0) {
                            in.read(bytes, 0, len);
                            //SimpleLog.print("inputStream9999 " + BytesUtils.toString(bytes));
                            bloodUtil.backBytes(bytes,DeviceCallBackHandler);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        listenThread.start();
    }

    public void connDevice(BluetoothDevice bluetoothDevice, final Handler connErrorHandler){

        // 这里需要try catch一下，以防异常抛出
        BluetoothSocket tmp = null;
        try {
            tmp = bluetoothDevice
                    .createInsecureRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
            activity.clientSocket = tmp;
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 判断客户端接口是否为空
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (activity.clientSocket != null) {
                        // 获取到客户端接口
                        activity.clientSocket.connect();
                        // 获取到输出流，向外写数据
                        os = activity.clientSocket.getOutputStream();
                        in = activity.clientSocket.getInputStream();
                        os.write(bloodUtil.hex2byte(bloodUtil.connectDevice.getBytes()));
                        Thread.sleep(1000);
                        Message message = new Message();
                        message.what = 199;
                        DeviceCallBackHandler.sendMessage(message);
                        os.write(bloodUtil.hex2byte(bloodUtil.StartMeasure.getBytes()));
                        listen();

                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    // 如果发生异常则告诉用户发送失败
                    Message message = new Message();
                    message.what = 404;
                    connErrorHandler.sendMessage(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();



    }


    /**
     * 广播接受器
     */
    private class MyBtReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                Toast.makeText(context, "开始搜索", Toast.LENGTH_SHORT).show();
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                Toast.makeText(context, "搜索结束", Toast.LENGTH_SHORT).show();
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (bloodUtil.isBPDevice(device)) {
                    if(!SearchSuccess){
                        SearchSuccess = true;
                        Health_AppLocation.instance.mBluetoothAdapter.cancelDiscovery();
                        BlueToothDeviceModel blueToothDeviceModel = new BlueToothDeviceModel();
                        blueToothDeviceModel.setFromType(TYPE);
                        blueToothDeviceModel.setBluetoothDevice(device);
                        blueToothDeviceModellist.add(blueToothDeviceModel);
                        deviceList_recy.setVisibility(View.VISIBLE);
                        device_null_text.setVisibility(View.GONE);
                        deviceList_recy.setLayoutManager(new LinearLayoutManager(getActivity()));
                        deviceList_recy.setAdapter(adapter);
                        adapter.setList(blueToothDeviceModellist);
                        connDevice(device,DeviceCallBackHandler);
                    }
                    Log.e("deviceName", "---------------- " + device.getName());
                }
            }else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
//                Toast.makeText(context, "搜索结束", Toast.LENGTH_SHORT).show();
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                int boundState = device.getBondState();
                if(boundState == BluetoothDevice.BOND_BONDED){
                    Toast.makeText(context, device.getName()+"配对成功", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}
