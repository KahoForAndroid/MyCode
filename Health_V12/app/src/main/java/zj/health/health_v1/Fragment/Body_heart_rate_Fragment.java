package zj.health.health_v1.Fragment;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zj.health.health_v1.Activity.BlueTooth_Data_Activity;
import zj.health.health_v1.Activity.Health_Log_Activity;
import zj.health.health_v1.Activity.MainsActivity;
import zj.health.health_v1.Activity.Search_BlueToothDevice_Activity;
import zj.health.health_v1.Adapter.BlueToothDeviceListAdapter;
import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Base.BlueToothConstant;
import zj.health.health_v1.Base.Constant;
import zj.health.health_v1.Base.Health_AppLocation;
import zj.health.health_v1.Implements.OnItemClick;
import zj.health.health_v1.Model.BlueToothDeviceModel;
import zj.health.health_v1.R;
import zj.health.health_v1.Service.DeviceService;
import zj.health.health_v1.Utils.DateUtil;
import zj.health.health_v1.Utils.StringUtil;
import zj.health.health_v1.Utils.view.PostUtils;

/**
 * Created by Administrator on 2018/5/10.
 */

public class Body_heart_rate_Fragment extends Fragment implements View.OnClickListener{

    private View view = null;
    private TimePickerView timePickerView = null;
    private EditText heart_rate_edit;
    private RelativeLayout date_rela,Rest_in_rest_rela,Sport_rela;
    private TextView dateText,Rest_in_rest_text,Sport_text,device_null_text;
    private Button commit_Button;
    private ImageView add_bluetooth;
    private BlueTooth_Data_Activity activity;
    private int TYPE =4;//体征类型
    private int bodyStatus = 1;//状态
    private long timesLong;
    private List<BlueToothDeviceModel> blueToothDeviceModellist = new ArrayList<>();
    private RecyclerView deviceList_recy;
    private BlueToothDeviceListAdapter adapter;
    private Handler submitHandler = new Handler(){
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
                                getActivity().setResult(4);
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
                if(activity.isPermissionPass){
                    if (!Health_AppLocation.instance.mBluetoothAdapter.isEnabled()) {
                        Health_AppLocation.instance.mBluetoothAdapter.enable();
                    }else{
                    Intent it = new Intent(getActivity(), Search_BlueToothDevice_Activity.class);
                    it.putExtra("TYPE", TYPE);
                    startActivityForResult(it, 0x2);
                    }
                }

//                if (activity.isPermissionPass) {
//                    Intent it = new Intent(getActivity(), Search_BlueToothDevice_Activity.class);
//                    it.putExtra("TYPE", TYPE);
//                    startActivityForResult(it, 0x2);
//                } else {
//                    Toast.makeText(getActivity(), getString(R.string.location_open_tips), Toast.LENGTH_SHORT).show();
//                }
            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null){
            view = inflater.inflate(R.layout.body_heart_rate_fragment,container,false);
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
        heart_rate_edit = (EditText)view.findViewById(R.id.heart_rate_edit);
        commit_Button = (Button)view.findViewById(R.id.commit_Button);
        add_bluetooth = (ImageView)view.findViewById(R.id.add_bluetooth);
        Sport_rela = (RelativeLayout)view.findViewById(R.id.Sport_rela);
        Sport_text = (TextView)view.findViewById(R.id.Sport_text);
        Rest_in_rest_rela = (RelativeLayout)view.findViewById(R.id.Rest_in_rest_rela);
        Rest_in_rest_text = (TextView)view.findViewById(R.id.Rest_in_rest_text);
        device_null_text = (TextView)view.findViewById(R.id.device_null_text);
        deviceList_recy = (RecyclerView)view.findViewById(R.id.deviceList_recy);

    }
    private void BindListener(){
        commit_Button.setOnClickListener(this);
        add_bluetooth.setOnClickListener(this);
        date_rela.setOnClickListener(this);
        Rest_in_rest_rela.setOnClickListener(this);
        Sport_rela.setOnClickListener(this);
    }
    private void setData(){
        dateText.setText(DateUtil.getNowDay());
        timesLong = System.currentTimeMillis();
        TimePickerView.OnTimeSelectListener listener = new TimePickerView.OnTimeSelectListener(){

            @Override
            public void onTimeSelect(Date date, View v) {
                timesLong = date.getTime();
                dateText.setText(DateUtil.getNowdayymd()+" "+DateUtil.getDaysStrhm(date));
            }
        };
        timePickerView = new DateUtil().ShowTimePickerView(getActivity(),listener, Calendar.getInstance(),
                true,true,true,true,true,false);
    }


    private void CheckDeviceList(){
        blueToothDeviceModellist.clear();
        adapter = new BlueToothDeviceListAdapter(getActivity(),blueToothDeviceModellist,TYPE);
        adapter.setOnItemClick(new OnItemClick() {
            @Override
            public void OnItemClickListener(View view, int position) {
                //根据类型判断从服务断开设备连接还是直接断开
                if(TYPE == 4){
//                    getActivity().unbindService(MainsActivity.mServiceConnection);
                    MainsActivity.mBluetoothLeService.disconnect();
                    adapter.notifyDataSetChanged();
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

        new PostUtils().JsonPost(Constant.post_heart_rate,jsonObject,submitHandler, (BaseActivity) getActivity());
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.commit_Button:
                if(heart_rate_edit.getText().toString().length()<=0){
                    Toast.makeText(getActivity(), getString(R.string.all_input_tips), Toast.LENGTH_SHORT).show();
                }else{
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("heartRate",heart_rate_edit.getText().toString());
                        jsonObject.put("type",bodyStatus);
                        jsonObject.put("recordTime",timesLong);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Health_AppLocation.instance.UpdateHeartTime = timesLong;
                    Submit(jsonObject);
                }
                break;
            case R.id.add_bluetooth:
                if(activity.isPermissionPass){
                    if (!Health_AppLocation.instance.mBluetoothAdapter.isEnabled()) {
                        Health_AppLocation.instance.mBluetoothAdapter.enable();
                    }
                    if (activity.isPermissionPass) {
                        Intent it = new Intent(getActivity(), Search_BlueToothDevice_Activity.class);
                        it.putExtra("TYPE", TYPE);
                        startActivityForResult(it, 0x2);
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.location_open_tips), Toast.LENGTH_SHORT).show();
                    }
                }else{
//                    activity.setBlueToothPermission(BlueToothPermissionHandler);
                    Toast.makeText(getActivity(), getString(R.string.location_open_tips), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.date_rela:
                timePickerView.show();
                break;
            case R.id.Rest_in_rest_rela:
                bodyStatus = 2;
                Sport_rela.setBackground(getResources().getDrawable(R.drawable.radius_black));
                Rest_in_rest_rela.setBackground(getResources().getDrawable(R.drawable.radius_blue));
                Rest_in_rest_text.setTextColor(getResources().getColor(R.color.white));
                Sport_text.setTextColor(getResources().getColor(R.color.black));
                break;
            case R.id.Sport_rela:
                bodyStatus = 1;
                Sport_rela.setBackground(getResources().getDrawable(R.drawable.radius_blue));
                Rest_in_rest_rela.setBackground(getResources().getDrawable(R.drawable.radius_black));
                Rest_in_rest_text.setTextColor(getResources().getColor(R.color.black));
                Sport_text.setTextColor(getResources().getColor(R.color.white));
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
        if(resultCode == BlueToothConstant.BODY_HEARTRATE_CONNECTRESULT){
            activity.isPermissionPass = true;
            BluetoothDevice bluetoothDevice = data.getParcelableExtra("device");
            BluetoothDevice device = null;

            if(bluetoothDevice!=null){
                device = Health_AppLocation.instance.mBluetoothAdapter.getRemoteDevice(bluetoothDevice.getAddress());
//                if(MainsActivity.mBluetoothLeService!=null){
////                            MainsActivity.mBluetoothLeService.mBluetoothGatt = device.connectGatt(getActivity(),false,MainsActivity.mBluetoothLeService.mGattCallback);
//                    MainsActivity.mBluetoothLeService.connect(device.getAddress(),TYPE);
//                }else{
//                    Intent intent = new Intent();
//                    intent.setAction("connected");
//                    intent.putExtra("mDeviceAddress",device.getAddress());
//                    intent.putExtra("DriveType",TYPE);
//                    getActivity().sendBroadcast(intent);
////                    Toast.makeText(activity, getString(R.string.connect_device_error), Toast.LENGTH_SHORT).show();
//
//                }
                Intent intent = new Intent();
                intent.setAction("connected");
                intent.putExtra("mDeviceAddress",device.getAddress());
                intent.putExtra("DriveType",TYPE);
                getActivity().sendBroadcast(intent);

            }else{
                Toast.makeText(activity, getString(R.string.connect_device_error), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
