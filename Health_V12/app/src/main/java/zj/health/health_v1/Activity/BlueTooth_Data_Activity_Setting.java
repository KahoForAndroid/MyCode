//package zj.health.health_v1.Activity;
//
//import android.bluetooth.BluetoothAdapter;
//import android.bluetooth.BluetoothManager;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.provider.Settings;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import zj.health.health_v1.Adapter.BlueToothData_Adapter;
//import zj.health.health_v1.Base.BaseActivity;
//import zj.health.health_v1.Base.Constant;
//import zj.health.health_v1.Base.Health_AppLocation;
////import zj.health.health_v1.Fragment.Body_TypeSetting_Fragment;
//import zj.health.health_v1.Fragment.Body_type_Fragment;
//import zj.health.health_v1.Implements.OnItemClick;
//import zj.health.health_v1.Model.UserDevices;
//import zj.health.health_v1.R;
//import zj.health.health_v1.Utils.BlueToothUtils;
//import zj.health.health_v1.Utils.PermissionUtils;
//import zj.health.health_v1.Utils.view.PostUtils;
//
///**
// * Created by Administrator on 2018/4/28.
// */
//
//public class BlueTooth_Data_Activity_Setting extends BaseActivity implements View.OnClickListener{
//
//    private ImageView back;
//    private TextView title;
//    private RecyclerView body_type_recy;
//    private FragmentManager fragmentManager;
//    private BlueToothData_Adapter adapter;
//    private String postName = "device/userDevices";
//    private String posTypeName = "device/types";
//    private FragmentTransaction fragmentTransaction;
//    private List<Fragment> fragmentList = new ArrayList<>();
//    private Bundle bundle = null;
//    private boolean isUpdate = false; //是否需要重新刷新数据
//    public List<UserDevices> userDevices = new ArrayList<>();
//    public int topPosition;
//    public BlueToothUtils blueToothUtils;
//    public boolean isPermissionPass = false;
//    private static final int REQUEST_BLUETOOTH_PERMISSION=10;
//    public Handler TopHandler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            try {
//                if(msg.what == 200){
//                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
//                    if(jsonObject.optString("msg").equals("ok")){
//                        Health_Log_Activity.isUploadGraphic = true;
//                        PostTypeList();
//                    }else{
//                        Toast.makeText(BlueTooth_Data_Activity_Setting.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
//                    }
//                }else{
//                    Toast.makeText(BlueTooth_Data_Activity_Setting.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
//                }
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//                Toast.makeText(BlueTooth_Data_Activity_Setting.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
//            }
//        }
//    };
//
//
//    public Handler SwitchHandler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            try {
//                if(msg.what == 200){
//                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
//                    if(jsonObject.optString("msg").equals("ok")){
//                        Health_Log_Activity.isUploadGraphic = true;
////                        Toast.makeText(BlueTooth_Data_Activity_Setting.this, getString(R.string.close_deviceType), Toast.LENGTH_SHORT).show();
//                    }else{
//                        Toast.makeText(BlueTooth_Data_Activity_Setting.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
//                    }
//                }else{
//                    Toast.makeText(BlueTooth_Data_Activity_Setting.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
//                }
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//                Toast.makeText(BlueTooth_Data_Activity_Setting.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
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
//                        userDevices = new Gson().fromJson(jsonObject.optString("info"),new TypeToken<List<UserDevices>>(){}.getType());
//                        fragmentList.clear();
//                        for(int i = 0;i<userDevices.size();i++){
//
//                            Body_TypeSetting_Fragment fragment = new Body_TypeSetting_Fragment();
//                            Bundle bundle = new Bundle();
//                            bundle.putInt("type",Integer.valueOf(userDevices.get(i).getId()));
//                            bundle.putInt("position",i);
//                            if(i == 0){
//                                bundle.putBoolean("isTop",true);
//                            }
//                            fragment.setArguments(bundle);
//                            fragmentList.add(fragment);
//                        }
//                        isUpdate = true;
//                        adapter.setList(userDevices);
//                        adapter.setOnClickPositionBgColor(0);
//                        adapter.notifyDataSetChanged();
//                        setFragment(fragmentList.get(0));
//
//                    }else{
//                        Toast.makeText(BlueTooth_Data_Activity_Setting.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
//                    }
//                }else if(msg.what == Constant.UserErrorCode){
//                    Toast.makeText(BlueTooth_Data_Activity_Setting.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
//                }else{
//                    Toast.makeText(BlueTooth_Data_Activity_Setting.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
//                }
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//                Toast.makeText(BlueTooth_Data_Activity_Setting.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
//            }
//        }
//    };
//
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.bluetooth_setting);
//        initView();
//        BindListener();
//        setData();
//    }
//
//    private void initView(){
//        back = (ImageView)findViewById(R.id.back);
//        title = (TextView)findViewById(R.id.title);
//        body_type_recy = (RecyclerView)findViewById(R.id.body_type_recy);
//    }
//    private void BindListener(){
//        back.setOnClickListener(this);
//
////        list = getResources().getStringArray(R.array.body_type);
//    }
//    private void setData(){
//
//        setBlueToothPermission();
//        title.setText(getString(R.string.device_management));
////        userDevices = (List<UserDevices>) getIntent().getSerializableExtra("userDevices");
//        adapter = new BlueToothData_Adapter(this,userDevices);
//        body_type_recy.setLayoutManager(new LinearLayoutManager(this));
//        body_type_recy.setAdapter(adapter);
//        adapter.setOnItemClick(new OnItemClick() {
//            @Override
//            public void OnItemClickListener(View view, int position) {
//                adapter.setOnClickPositionBgColor(position);
//                adapter.notifyDataSetChanged();
//                setFragment(fragmentList.get(position));
//            }
//        });
//        PostTypeList();
//
//
//    }
//    private void setFragment(Fragment fragment){
//        fragmentManager = getSupportFragmentManager();
//        fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.fragment_layout,fragment);
////        bundle = new Bundle();
////        bundle.putInt("type",position);
//////        bundle.putSerializable("userDevices",userDevices.get(position));
////        fragment.setArguments(bundle);
//        fragmentTransaction.commit();
//    }
//
//    private void setBlueToothPermission(){
//        blueToothUtils = new BlueToothUtils(this);
//        if (Build.VERSION.SDK_INT >= 23 && !PermissionUtils.isLocationOpen(getApplicationContext())) {
//            Intent enableLocate = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//            startActivityForResult(enableLocate, REQUEST_BLUETOOTH_PERMISSION);
//        }else{
//            blueToothUtils.RequestBluetoothPermission();
//            blueToothUtils.CheckBLE();
//            isPermissionPass = true;
//        }
//
//    }
//
//
//    private void PostTypeList(){
//        Map<String,String> map = new HashMap<>();
//        map.put("appUserId", Health_AppLocation.instance.userid);
//        new PostUtils().getPost(posTypeName,map,postHandler,this);
//    }
//
//    @Override
//    public void onClick(View view) {
//        switch (view.getId()){
//            case R.id.back:
//                Intent it = new Intent();
//                it.putExtra("isUpdate",isUpdate);
//                setResult(RESULT_OK,it);
//                finish();
//                break;
//        }
//    }
//
//    @Override
//    public void onBackPressed() {
//        Intent it = new Intent();
//        it.putExtra("isUpdate",isUpdate);
//        setResult(RESULT_OK,it);
//        finish();
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_BLUETOOTH_PERMISSION) {
//            if (PermissionUtils.isLocationOpen(getApplicationContext())) {
//                blueToothUtils.RequestBluetoothPermission();
//                blueToothUtils.CheckBLE();
//                isPermissionPass = true;
//            }else{
//                isPermissionPass = false;
//                Toast.makeText(this, getString(R.string.location_open_tips), Toast.LENGTH_SHORT).show();
//            }
//        } else if(requestCode == 0x2){
//            if(resultCode == 0x123){
//                isPermissionPass = true;
//            }
//
//        }
////
//// else{
////            isPermissionPass = false;
////            Toast.makeText(this, getString(R.string.location_open_tips), Toast.LENGTH_SHORT).show();
////        }
//    }
//
//}
