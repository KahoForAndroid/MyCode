package zj.health.health_v1.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Base.Constant;
import zj.health.health_v1.Base.Health_AppLocation;
import zj.health.health_v1.Model.FriendModel;
import zj.health.health_v1.MyView.SwitchView;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.view.PostUtils;

/**
 * Created by Administrator on 2018/4/18.
 */

public class Friend_permissions_setting_Activity extends BaseActivity implements View.OnClickListener{

    private ImageView back;
    private SwitchView blood_pressure_switchbutton,weight_switchbutton,temperature_switchbutton,
            heart_rate_switchbutton,blood_sugar_switchbutton,medicineReminder_switchbutton,healthReport_switchbutton,particularReport_switchbutton;
    private Button save_button;
    private RelativeLayout check_friend_fraph;
    private String userId = null;
    private FriendModel.FriendPermission.Permission friendPermission;
    private Gson gson = new Gson();
    private Handler checkHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(msg.obj.toString());
                    if(jsonObject1.optString("msg").equals("success")){
                        friendPermission = gson.fromJson(jsonObject1.optString("data"),
                                FriendModel.FriendPermission.Permission.class);
                        if (friendPermission!=null){
                            blood_pressure_switchbutton.setState(friendPermission.isBloodPressure());
                            weight_switchbutton.setState(friendPermission.isWeight());
                            temperature_switchbutton.setState(friendPermission.isTemp());
                            heart_rate_switchbutton.setState(friendPermission.isHeartRate());
                            blood_sugar_switchbutton.setState(friendPermission.isBloodGlucose());
                            medicineReminder_switchbutton.setState(friendPermission.isMedicineReminder());
                            healthReport_switchbutton.setState(friendPermission.isHealthReport());
                            particularReport_switchbutton.setState(friendPermission.isParticularReport());

                        }
                    }else{
                        Toast.makeText(Friend_permissions_setting_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if(msg.what == Constant.UserErrorCode){
                Toast.makeText(Friend_permissions_setting_Activity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(Friend_permissions_setting_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
            }
        }
    };

    private Handler jsonHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(msg.obj.toString());
                    if(jsonObject1.optString("msg").equals("success")){
                        Toast.makeText(Health_AppLocation.instance, R.string.updateSuccess, Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(Friend_permissions_setting_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(Friend_permissions_setting_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_permissions_setting_activity);
        initView();
        BindListener();
        setData();
    }
    private void initView(){
        back = (ImageView)findViewById(R.id.back);
        save_button = (Button)findViewById(R.id.save_button);
        blood_pressure_switchbutton = (SwitchView)findViewById(R.id.blood_pressure_switchbutton);
        weight_switchbutton = (SwitchView)findViewById(R.id.weight_switchbutton);
        temperature_switchbutton = (SwitchView)findViewById(R.id.temperature_switchbutton);
        heart_rate_switchbutton = (SwitchView)findViewById(R.id.heart_rate_switchbutton);
        blood_sugar_switchbutton = (SwitchView)findViewById(R.id.blood_sugar_switchbutton);
        check_friend_fraph = (RelativeLayout)findViewById(R.id.check_friend_fraph);
        medicineReminder_switchbutton = (SwitchView)findViewById(R.id.medicineReminder_switchbutton);
        healthReport_switchbutton = (SwitchView)findViewById(R.id.healthReport_switchbutton);
        particularReport_switchbutton = (SwitchView)findViewById(R.id.particularReport_switchbutton);

//        blood_pressure_switchbutton.setState(false);
//        weight_switchbutton.setState(false);
//        temperature_switchbutton.setState(false);
//        heart_rate_switchbutton.setState(false);
//        blood_sugar_switchbutton.setState(false);
    }
    private void BindListener(){
        back.setOnClickListener(this);
        save_button.setOnClickListener(this);
        check_friend_fraph.setOnClickListener(this);
        blood_pressure_switchbutton.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn() {
                blood_pressure_switchbutton.setState(true);
                friendPermission.setBloodPressure(true);
            }

            @Override
            public void toggleToOff() {
                blood_pressure_switchbutton.setState(false);
                friendPermission.setBloodPressure(false);
            }
        });
        weight_switchbutton.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn() {
                weight_switchbutton.setState(true);
                friendPermission.setWeight(true);
            }

            @Override
            public void toggleToOff() {
                weight_switchbutton.setState(false);
                friendPermission.setWeight(false);
            }
        });
        temperature_switchbutton.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn() {
                temperature_switchbutton.setState(true);
                friendPermission.setTemp(true);
            }

            @Override
            public void toggleToOff() {
                temperature_switchbutton.setState(false);
                friendPermission.setTemp(false);
            }
        });
        heart_rate_switchbutton.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn() {
                heart_rate_switchbutton.setState(true);
                friendPermission.setHeartRate(true);
            }

            @Override
            public void toggleToOff() {
                heart_rate_switchbutton.setState(false);
                friendPermission.setHeartRate(false);
            }
        });
        blood_sugar_switchbutton.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn() {
                blood_sugar_switchbutton.setState(true);
                friendPermission.setBloodGlucose(true);
            }

            @Override
            public void toggleToOff() {
                blood_sugar_switchbutton.setState(false);
                friendPermission.setBloodGlucose(false);
            }
        });
        medicineReminder_switchbutton.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn() {
                medicineReminder_switchbutton.setState(true);
                friendPermission.setMedicineReminder(true);
            }

            @Override
            public void toggleToOff() {
                medicineReminder_switchbutton.setState(false);
                friendPermission.setMedicineReminder(false);
            }
        });
        healthReport_switchbutton.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn() {
                healthReport_switchbutton.setState(true);
                friendPermission.setHealthReport(true);
            }

            @Override
            public void toggleToOff() {
                healthReport_switchbutton.setState(false);
                friendPermission.setHealthReport(false);
            }
        });
        particularReport_switchbutton.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn() {
                particularReport_switchbutton.setState(true);
                friendPermission.setParticularReport(true);
            }

            @Override
            public void toggleToOff() {
                particularReport_switchbutton.setState(false);
                friendPermission.setParticularReport(false);
            }
        });
    }

    private void setData(){
        userId = getIntent().getStringExtra("userId");
        getFriendPermission();

    }
    private void getFriendPermission(){
        String json = "friendId="+userId;
        new PostUtils().Get(Constant.checkPermission+json,true,checkHandler,this);
    }
    private void UpdatePermission(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            new PostUtils().JsonPost(Constant.permission_friend,jsonObject,jsonHandler,this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.save_button:
                FriendModel.FriendPermission friendPermissions = new FriendModel.FriendPermission();
                friendPermissions.setUserId(userId);
                friendPermissions.setPermission(friendPermission);
                String json =  gson.toJson(friendPermissions);
                UpdatePermission(json);
                break;
            case R.id.check_friend_fraph:
                Intent it = new Intent(this,Friend_Graph_Activity.class);
                it.putExtra("friendId",userId);
                startActivity(it);
                break;
                default:
                    break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
