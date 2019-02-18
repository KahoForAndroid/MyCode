package zj.health.health_v1.Activity;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zj.health.health_v1.Adapter.BlueToothData_Adapter;
import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Base.Constant;
import zj.health.health_v1.Base.Health_AppLocation;
//import zj.health.health_v1.Fragment.Body_TypeSetting_Fragment;
import zj.health.health_v1.Fragment.Body_blood_Fragment;
import zj.health.health_v1.Fragment.Body_blood_sugar_Fragment;
import zj.health.health_v1.Fragment.Body_heart_rate_Fragment;
import zj.health.health_v1.Fragment.Body_temperature_Fragment;
import zj.health.health_v1.Fragment.Body_type_Fragment;
import zj.health.health_v1.Fragment.Body_weight_Fragment;
import zj.health.health_v1.Fragment.Question_Fragment;
import zj.health.health_v1.Implements.OnItemClick;
import zj.health.health_v1.Model.UserDevices;
import zj.health.health_v1.R;
import zj.health.health_v1.Service.AlarmManageService;
import zj.health.health_v1.Utils.BlueToothUtils;
import zj.health.health_v1.Utils.PermissionUtils;
import zj.health.health_v1.Utils.view.PostUtils;

/**
 * Created by Administrator on 2018/4/17.
 */

public class BlueTooth_Data_Activity extends BaseActivity implements View.OnClickListener{

    private ImageView back,setting_img,measureing_bg;
    private TextView title;
    private RecyclerView body_type_recy;
    private FragmentManager fragmentManager;
    private BlueToothData_Adapter adapter;
    private String [] list = null;
    private FragmentTransaction fragmentTransaction;
    private List<Fragment> fragmentList = new ArrayList<>();
    private Bundle bundle = null;
    public List<UserDevices> userDevices = new ArrayList<>();
    public boolean isPermissionPass = Health_AppLocation.instance.isPermissionPass;
    private static final int REQUEST_BLUETOOTH_PERMISSION=10;
    public BlueToothUtils blueToothUtils;

    // 获取到选中设备的客户端串口，全局变量，
    // 否则连接在方法执行完就结束了(应用到所有体征)
    public BluetoothSocket clientSocket = null;


    private Handler MeasureStatusHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 199){
                measureing_bg.setVisibility(View.VISIBLE);
            }else if(msg.what == 100){
                measureing_bg.setVisibility(View.GONE);
            }else if(msg.what == 200){
                measureing_bg.setVisibility(View.GONE);
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blue_tooth_data_activity);
        initView();
        BindListener();
        setData();

    }
    private void initView(){
        back = (ImageView)findViewById(R.id.back);
        title = (TextView)findViewById(R.id.title);
        setting_img = (ImageView)findViewById(R.id.setting_img);
        body_type_recy = (RecyclerView)findViewById(R.id.body_type_recy);
        measureing_bg = (ImageView)findViewById(R.id.measureing_bg);
    }
    private void BindListener(){
        back.setOnClickListener(this);
        setting_img.setOnClickListener(this);

//        list = getResources().getStringArray(R.array.body_type);
        adapter = new BlueToothData_Adapter(this,userDevices);
        adapter.setOnItemClick(new OnItemClick() {
            @Override
            public void OnItemClickListener(View view, int position) {
                adapter.setOnClickPositionBgColor(position);
                adapter.notifyDataSetChanged();
                setFragment(fragmentList.get(position));
            }
        });

    }
    private void setData(){
        title.setText(getString(R.string.set_data));
        body_type_recy.setLayoutManager(new LinearLayoutManager(this));
        body_type_recy.setAdapter(adapter);
        setBlueToothPermission(null);
        setDateToFragment();

    }

    public Handler getMeasureStatusHandler(){
        return MeasureStatusHandler;
    }

    public void setBlueToothPermission(Handler handler){
        blueToothUtils = new BlueToothUtils(this);
        if (Build.VERSION.SDK_INT >= 23 && !PermissionUtils.isLocationOpen(getApplicationContext())) {
            Intent enableLocate = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(enableLocate, REQUEST_BLUETOOTH_PERMISSION);
        }else{
            blueToothUtils.RequestBluetoothPermission();
            blueToothUtils.CheckBLE();
            isPermissionPass = true;
//            Message message = new Message();
//            message.what = 200;
//            handler.sendMessage(message);
        }
    }

    private void setDateToFragment(){
        fragmentList.add(new Body_blood_Fragment());
        fragmentList.add(new Body_weight_Fragment());
        fragmentList.add(new Body_temperature_Fragment());
        fragmentList.add(new Body_heart_rate_Fragment());
        fragmentList.add(new Body_blood_sugar_Fragment());
        String bodytype[] = getResources().getStringArray(R.array.body_type);
        for (int i = 0;i<bodytype.length;i++){
            UserDevices userdevices = new UserDevices();
            userdevices.setTitle(bodytype[i]);
            userdevices.setId(i+1+"");
            userDevices.add(userdevices);
        }
        setFragment(fragmentList.get(0));
        adapter.setList(userDevices);
        adapter.notifyDataSetChanged();
    }
    private void setFragment(Fragment fragment){
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_layout,fragment);
        fragmentTransaction.commit();
    }


    private Fragment setArguments(Fragment fragment){
//        Bundle bundle = new Bundle();
//        bundle.putInt("TYPE",Integer.parseInt(userDevices.get(position).getId()));
//        fragment.setArguments(bundle);

        return fragment;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                setResult(0x3);
                finish();
                break;
            case R.id.setting_img:
//                Intent it = new Intent(this,BlueTooth_Data_Activity_Setting.class);
//                it.putExtra("userDevices", (Serializable) userDevices);
//                startActivityForResult(it,0x1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
//            boolean isUpdate = data.getBooleanExtra("isUpdate",false);
//            if(isUpdate){
//                Post();
//            }
        }else  if (requestCode == REQUEST_BLUETOOTH_PERMISSION) {
            if (PermissionUtils.isLocationOpen(getApplicationContext())) {
                blueToothUtils.RequestBluetoothPermission();
                blueToothUtils.CheckBLE();
                isPermissionPass = true;
            }else{
                isPermissionPass = false;
                Toast.makeText(this, getString(R.string.location_open_tips), Toast.LENGTH_SHORT).show();
            }
        }
//        else if(requestCode == 0x2){
//            if(resultCode == 0x123){
//                isPermissionPass = true;
//            }
//
//        }
    }

    public void disConn() {
        try {
            if(clientSocket != null){
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disConn();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(0x3);
        finish();
    }
}
