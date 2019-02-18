//package zj.health.health_v1.Fragment;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.bluetooth.BluetoothAdapter;
//import android.bluetooth.BluetoothDevice;
//import android.bluetooth.BluetoothGatt;
//import android.bluetooth.BluetoothGattCallback;
//import android.bluetooth.BluetoothGattCharacteristic;
//import android.bluetooth.BluetoothGattDescriptor;
//import android.bluetooth.BluetoothGattService;
//import android.bluetooth.BluetoothProfile;
//import android.bluetooth.le.BluetoothLeScanner;
//import android.bluetooth.le.ScanCallback;
//import android.bluetooth.le.ScanResult;
//import android.content.BroadcastReceiver;
//import android.content.ComponentName;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.ServiceConnection;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.IBinder;
//import android.os.Message;
//import android.support.annotation.Nullable;
//import android.support.annotation.RequiresApi;
//import android.support.v4.app.Fragment;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.UUID;
//
//import zj.health.health_v1.Activity.BlueTooth_Data_Activity;
//import zj.health.health_v1.Activity.BlueTooth_Data_Activity_Setting;
//import zj.health.health_v1.Activity.MainsActivity;
//import zj.health.health_v1.Activity.NewSearch_BlueTooth_Activity;
//import zj.health.health_v1.Activity.Search_BlueToothDevice_Activity;
//import zj.health.health_v1.Adapter.BlueToothDeviceListAdapter;
//import zj.health.health_v1.Base.BaseActivity;
//import zj.health.health_v1.Base.BlueToothConstant;
//import zj.health.health_v1.Base.Constant;
//import zj.health.health_v1.Base.Health_AppLocation;
//import zj.health.health_v1.Model.BlueToothDeviceModel;
//import zj.health.health_v1.Model.UserDevices;
//import zj.health.health_v1.MyView.SwitchView;
//import zj.health.health_v1.R;
//import zj.health.health_v1.Service.DeviceService;
//import zj.health.health_v1.Utils.BlueToothUtil.WearfitUtil;
//import zj.health.health_v1.Utils.StringUtil;
//import zj.health.health_v1.Utils.view.PostUtils;
//
///**
// * Created by Administrator on 2018/4/28.
// */
//
//public class Body_TypeSetting_Fragment extends Fragment{
//
//    private View view;
//    private int TYPE;//1血压 2体重 3体温 4心率 5血糖
//    private Bundle bundle = null;
//    private ImageView type_img,add_bluetooth;
//    private BluetoothGatt bluetoothGatt;
//    private TextView type_text_open,typeUtil_text,device_null_text;
//    private SwitchView setting_Blood_pressure_switch,setting_First_page_switch,switch_bottom;
//    private RecyclerView deviceList_recy;
//    private BlueToothDeviceListAdapter adapter;
//    private RelativeLayout BodyTypeUtil_Rela;
//    private String postName_delelte = "device/delete?";
//    private String postName_top = "device/top?";
//    private String postName_switch = "device/switchDevice?";
//    private List<BlueToothDeviceModel> blueToothDeviceModellist = new ArrayList<>();
//    private int position;
//    private List<UserDevices> userDevices;
//    private BlueTooth_Data_Activity_Setting activity;
//    public  IntentFilter filter = new IntentFilter();
//    private List<String> list = new ArrayList<>();
//    private Handler deleteHandler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            try {
//                if(msg.what == 200){
//                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
//                    if(jsonObject.optString("msg").equals("ok")){
//                        switch_bottom.setState(false);
//                        activity.userDevices.get(position).getDevices().clear();
//                    }else{
//                        Toast.makeText(getActivity(), getString(R.string.http_error), Toast.LENGTH_SHORT).show();
//                    }
//                }else if(msg.what == Constant.UserErrorCode){
//                    Toast.makeText(getActivity(), R.string.user_error_message, Toast.LENGTH_SHORT).show();
//                }else{
//                    Toast.makeText(getActivity(), getString(R.string.http_error), Toast.LENGTH_SHORT).show();
//                }
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//                Toast.makeText(getActivity(), getString(R.string.http_error), Toast.LENGTH_SHORT).show();
//            }
//        }
//    };
//
//
//    private Handler postHandler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            try {
//                if(msg.what == 200){
//                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
//                    if(jsonObject.optString("msg").equals("ok")){
//
//                    }else{
//                        Toast.makeText(getActivity(), getString(R.string.http_error), Toast.LENGTH_SHORT).show();
//                    }
//                }else{
//                    Toast.makeText(getActivity(), getString(R.string.http_error), Toast.LENGTH_SHORT).show();
//                }
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//                Toast.makeText(getActivity(), getString(R.string.http_error), Toast.LENGTH_SHORT).show();
//            }
//        }
//    };
//
//
//
////    private Handler connectHandler = new Handler(){
////        @Override
////        public void handleMessage(Message msg) {
////            super.handleMessage(msg);
////            if (msg.what == BluetoothProfile.STATE_CONNECTED) {
////                bluetoothGatt.discoverServices();
////                Toast.makeText(activity, getString(R.string.connect_success), Toast.LENGTH_SHORT).show();
////            }else if (msg.what == BluetoothProfile.STATE_DISCONNECTED) {
////                Toast.makeText(activity, getString(R.string.connect_device_error), Toast.LENGTH_SHORT).show();
////            }else{
////                Toast.makeText(activity, getString(R.string.connect_device_error), Toast.LENGTH_SHORT).show();
////            }
////        }
////    };
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        view = inflater.inflate(R.layout.body_type_setting_fragment,container,false);
//        bundle = getArguments();
//        TYPE = bundle.getInt("type");
//        position = bundle.getInt("position");
//        initView();
//        BindListener();
//        setData();
//        return view;
//    }
//
//
//
//    private void initView(){
//        type_img = (ImageView)view.findViewById(R.id.type_img);
//        add_bluetooth = (ImageView)view.findViewById(R.id.add_bluetooth);
//        type_text_open = (TextView)view.findViewById(R.id.type_text_open);
//        typeUtil_text = (TextView)view.findViewById(R.id.typeUtil_text);
//        setting_Blood_pressure_switch  = (SwitchView)view.findViewById(R.id.setting_Blood_pressure_switch);
//        setting_First_page_switch  = (SwitchView)view.findViewById(R.id.setting_First_page_switch);
//        switch_bottom  = (SwitchView)view.findViewById(R.id.switch_bottom);
//        BodyTypeUtil_Rela = (RelativeLayout)view.findViewById(R.id.BodyTypeUtil_Rela);
//        device_null_text = (TextView)view.findViewById(R.id.device_null_text);
//        deviceList_recy = (RecyclerView)view.findViewById(R.id.deviceList_recy);
//    };
//
//    private void BindListener(){
//
//
//        add_bluetooth.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//        setting_Blood_pressure_switch.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
//            @Override
//            public void toggleToOn() {
//                setting_Blood_pressure_switch.setState(true);
//                if(userDevices.get(position).getDevices().size()>0){
//                    if(StringUtil.isEmpty(userDevices.get(position).getDevices().get(0).getUserDeviceId())){
//                        PostSwitch(null);
//                    }else{
//                        PostSwitch(userDevices.get(position).getDevices().get(0).getUserDeviceId());
//                    }
//                }else{
//                    PostSwitch(null);
//                }
//
//            }
//
//            @Override
//            public void toggleToOff() {
//                setting_Blood_pressure_switch.setState(false);
//                if(userDevices.get(position).getDevices().size()>0){
//                    if(StringUtil.isEmpty(userDevices.get(position).getDevices().get(0).getUserDeviceId())){
//                        PostSwitch(null);
//                    }else{
//                        PostSwitch(userDevices.get(position).getDevices().get(0).getUserDeviceId());
//                    }
//                }else{
//                    PostSwitch(null);
//                }
//            }
//        });
//        setting_First_page_switch.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
//            @Override
//            public void toggleToOn() {
//                setting_First_page_switch.setState(true);
//                PostTop(TYPE);
////                boolean a = writeRXCharacteristic(WearfitUtil.getInstance(getActivity()).setOnceOrRealTimeMeasure(0X09,1));
////                Toast.makeText(getActivity(), "send", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void toggleToOff() {
//////                bluetoothGattCharacteristic.get
////                if((bluetoothGattCharacteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_READ) != 0){
////                    boolean a = bluetoothGatt.readCharacteristic(bluetoothGattCharacteristic);
////                }
////                int b = 0;
//            }
//        });
//        switch_bottom.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
//            @Override
//            public void toggleToOn() {
//                Toast.makeText(activity, "请连接设备", Toast.LENGTH_SHORT).show();
////                switch_bottom.setState(true);
////                PostDeleteDevices(position);
//            }
//
//            @Override
//            public void toggleToOff() {
//                switch_bottom.setState(false);
//                PostDeleteDevices(position);
//            }
//        });
//
//        add_bluetooth.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                if (!MainsActivity.mBluetoothLeService.mBluetoothAdapter.isEnabled()) {
////                    MainsActivity.mBluetoothLeService.mBluetoothAdapter.enable();
////                }
//////                //如果当前在搜索，就先取消搜索
//////                if (mBluetoothAdapter.isDiscovering()) {
//////                    mBluetoothAdapter.cancelDiscovery();
//////                }
//////                //开启搜索
//////                mBluetoothAdapter.startDiscovery();
////
//////                if(Build.VERSION.SDK_INT >= 23){
//////                    bluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
//////                    bluetoothLeScanner.startScan(scanCallback);
//////                }else{
//////
//////                }
////                if(activity.isPermissionPass){
////                    Intent it = new Intent(getActivity(), Search_BlueToothDevice_Activity.class);
//////                    Intent it = new Intent(getActivity(), NewSearch_BlueTooth_Activity.class);
////                    it.putExtra("TYPE",TYPE);
////                    startActivityForResult(it,0x2);
//////                    mBluetoothAdapter.startLeScan(mLeScanCallBack);
////                }else{
////                    Toast.makeText(getActivity(), getString(R.string.location_open_tips), Toast.LENGTH_SHORT).show();
////                }
//            }
//
//        });
//    }
//
////    private BluetoothAdapter.LeScanCallback mLeScanCallBack = new BluetoothAdapter.LeScanCallback() {
////        @Override
////        public void onLeScan(final BluetoothDevice device, int rssi, final byte[] scanRecord) {
////            activity.runOnUiThread(new Runnable() {
////                @Override
////                public void run() {
////                    list.add(device.getAddress());
////                    MainsActivity.mBluetoothLeService.mBluetoothAdapter.stopLeScan(mLeScanCallBack);
////                    String a = StringUtil.bytesToHexString(scanRecord);
////
////                }
////            });
////        }
////    };
//
//    private void CheckDeviceList(){
//        blueToothDeviceModellist.clear();
//        adapter = new BlueToothDeviceListAdapter(getActivity(),blueToothDeviceModellist);
//        if(Health_AppLocation.instance.blueToothDeviceModels.size() > 0){
//            device_null_text.setVisibility(View.GONE);
//            for (int i = 0;i<Health_AppLocation.instance.blueToothDeviceModels.size();i++){
//                if(Health_AppLocation.instance.blueToothDeviceModels.get(i).getFromType() == TYPE){
//                    blueToothDeviceModellist.add(Health_AppLocation.instance.blueToothDeviceModels.get(i));
//                }
//            }
//            if(blueToothDeviceModellist.size() == 0){
//                deviceList_recy.setVisibility(View.GONE);
//                device_null_text.setVisibility(View.VISIBLE);
//            }else{
//                deviceList_recy.setVisibility(View.VISIBLE);
//                deviceList_recy.setLayoutManager(new LinearLayoutManager(getActivity()));
//                deviceList_recy.setAdapter(adapter);
//            }
//        }else{
//            deviceList_recy.setVisibility(View.GONE);
//            device_null_text.setVisibility(View.VISIBLE);
//        }
//    }
//    private void setData(){
////        // 设置广播信息过滤
////        filter.addAction(BluetoothDevice.ACTION_FOUND);//每搜索到一个设备就会发送一个该广播
////        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);//当全部搜索完后发送该广播
////        filter.setPriority(Integer.MAX_VALUE);//设置优先级
////       // 注册蓝牙搜索广播接收者，接收并处理搜索结果
//        filter.addAction(BlueToothConstant.UPDATECONNECTDEVICELIST);
//        getActivity().registerReceiver(receiver, filter);
//        CheckDeviceList();
//
//        //判断是否为首页优先设置
//        boolean isTop = bundle.getBoolean("isTop",false);
//        if(isTop){
//            setting_First_page_switch.setState(true);
//        }
//        //判断是否有开启设备
//        if(userDevices.get(position).getDevices().size() == 0){
//            switch_bottom.setState(false);
//        }else{
//            switch_bottom.setState(true);
//        }
//        //判断是否测量开启
//        if(StringUtil.isEmpty(userDevices.get(position).getFlag())){
//            setting_Blood_pressure_switch.setState(true);
//        }else{
//            setting_Blood_pressure_switch.setState(false);
//        }
//        switch (TYPE){
//            case 1:
//                setTypeStatus(R.drawable.blood_pressure,getString(R.string.Blood_pressure_measurement_open),
//                        getString(R.string.Blood_pressure_instrument));
//                break;
//            case 2:
//                setTypeStatus(R.drawable.weight,getString(R.string.weight_pressure_measurement_open),
//                        getString(R.string.Weighing_machine));
//                break;
//            case 3:
//                setTypeStatus(R.drawable.temperature,getString(R.string.temperature_pressure_measurement_open),
//                        getString(R.string.A_thermometer_gun));
//                break;
//            case 4:
//                setTypeStatus(R.drawable.heart_rate,getString(R.string.heart_rate_pressure_measurement_open),
//                        getString(R.string.watch));
//                break;
//            case 5:
//                setTypeStatus(R.drawable.blood_sugar,getString(R.string.blood_sugar_pressure_measurement_open),
//                        getString(R.string.watch));
//                break;
//        }
//
//
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        activity = ((BlueTooth_Data_Activity_Setting)context);
//        userDevices = activity.userDevices;
//    }
//
//    /**
//     * 根据fragment类型改变文字图片内容显示
//     * @param Type_img 图片样式
//     * @param type_text_content 文本内容
//     * @param typeUtil_content 设备名称
//     */
//    private void setTypeStatus(int Type_img,String type_text_content,String typeUtil_content){
//        type_img.setImageDrawable(getResources().getDrawable(Type_img));
//        type_text_open.setText(type_text_content);
//        typeUtil_text.setText(typeUtil_content);
//    }
//
//    private void PostDeleteDevices(int position){
//        Map<String,String> map = new HashMap<>();
//        map.put("appUserId", Health_AppLocation.instance.userid);
//        map.put("userDeviceId",userDevices.get(position).getDevices().get(0).getUserDeviceId());
//        new PostUtils().getPost(postName_delelte,map,deleteHandler, (BaseActivity) getActivity());
//    }
//
//    private void PostTop(int position){
//        Map<String,String> map = new HashMap<>();
//        map.put("appUserId", Health_AppLocation.instance.userid);
//        map.put("deviceType",position+"");
//        new PostUtils().getPost(postName_top,map,activity.TopHandler, (BaseActivity) getActivity());
//
//    }
//    private void PostSwitch(String supportDeviceId){
//        Map<String,String> map = new HashMap<>();
//        map.put("appUserId", Health_AppLocation.instance.userid);
//        map.put("deviceType",TYPE+"");
//        if(supportDeviceId != null){
//            map.put("supportDeviceId",supportDeviceId);
//        }
//        new PostUtils().getPost(postName_switch,map,activity.SwitchHandler, (BaseActivity) getActivity());
//    }
//
//    ScanCallback scanCallback =  new ScanCallback() {
//        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//        @Override
//        public void onScanResult(int callbackType, ScanResult result) {
//            super.onScanResult(callbackType, result);
//            if(result!=null){
//                list.add(result.getDevice().getAddress());
//            }
//        }
//    };
//
//
//    private final BroadcastReceiver receiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
////            String action = intent.getAction();
////            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
////                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
////                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
////                    list.add(device.getAddress());
//////                    tvDevices.append(device.getName() + ":"+ device.getAddress());
////                }
////            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
////                //已搜素完成
////                Toast.makeText(context, "search_bluetooth", Toast.LENGTH_SHORT).show();
////                MainsActivity.mBluetoothLeService.mBluetoothAdapter.cancelDiscovery();
////            }
//             CheckDeviceList();
//
//        }
//    };
//
//
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(resultCode == 0x123){
//            switch (TYPE){
//                case 1:
//                    break;
//                case 2:
//                    break;
//                case 3:
//                    break;
//                case 4:
////                    activity.isPermissionPass = true;
////                    BluetoothDevice bluetoothDevice = data.getParcelableExtra("device");
////                    BluetoothDevice device = null;
////
////                    if(bluetoothDevice!=null){
////                        device = MainsActivity.mBluetoothLeService.mBluetoothAdapter.getRemoteDevice(bluetoothDevice.getAddress());
////                        if(MainsActivity.mBluetoothLeService!=null){
//////                            MainsActivity.mBluetoothLeService.mBluetoothGatt = device.connectGatt(getActivity(),false,MainsActivity.mBluetoothLeService.mGattCallback);
////                            MainsActivity.mBluetoothLeService.connect(device.getAddress(),TYPE);
////                        }else{
////                            Toast.makeText(activity, getString(R.string.connect_device_error), Toast.LENGTH_SHORT).show();
////
////                        }
////
////                    }else{
////                        Toast.makeText(activity, getString(R.string.connect_device_error), Toast.LENGTH_SHORT).show();
////                    }
//                    break;
//                case 5:
//                    break;
//                    default:
//                        break;
//            }
//        }
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        getActivity().unregisterReceiver(receiver);
//    }
//}
