package zj.health.health_v1.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Base.Constant;
import zj.health.health_v1.Base.Health_AppLocation;
import zj.health.health_v1.Model.FriendModel;
import zj.health.health_v1.MyView.SwitchView;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.view.PostUtils;

/**
 * Created by Administrator on 2018/8/7.
 */

public class FriendsPermission_Activity extends BaseActivity implements View.OnClickListener{

    private FriendModel.FriendPermission friendPermission = new FriendModel.FriendPermission();
    private FriendModel.FriendPermission.Permission permission = new FriendModel.FriendPermission.Permission();
    private ImageView back;
    private TextView title;
    private Button save_button;
    private SwitchView blood_pressure_switchbutton,weight_switchbutton,temperature_switchbutton,
            heart_rate_switchbutton,blood_sugar_switchbutton;
    private Handler CheckPermission_Handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                try {
                    JSONObject jsonObject1 = new JSONObject(msg.obj.toString());
                    if(jsonObject1.optString("msg").equals("success")){
                        friendPermission = new Gson().fromJson(jsonObject1.optString("data"),FriendModel.FriendPermission.class);
                    }else{
                        Toast.makeText(FriendsPermission_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if(msg.what == Constant.UserErrorCode){
                Toast.makeText(FriendsPermission_Activity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(FriendsPermission_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
            }
        }
    };

    private Handler UpdatePermission_Handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                try {
                    JSONObject jsonObject1 = new JSONObject(msg.obj.toString());
                    if(jsonObject1.optString("msg").equals("success")){
                        Toast.makeText(Health_AppLocation.instance, R.string.add_success, Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(FriendsPermission_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(FriendsPermission_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friendspermission_activity);
        initView();
        BindListener();
        setData();
    }

    private void initView(){
        back = (ImageView)findViewById(R.id.back);
        title = (TextView)findViewById(R.id.title);
        save_button = (Button)findViewById(R.id.save_button);
        blood_pressure_switchbutton = (SwitchView)findViewById(R.id.blood_pressure_switchbutton);
        weight_switchbutton = (SwitchView)findViewById(R.id.weight_switchbutton);
        temperature_switchbutton = (SwitchView)findViewById(R.id.temperature_switchbutton);
        heart_rate_switchbutton = (SwitchView)findViewById(R.id.heart_rate_switchbutton);
        blood_sugar_switchbutton = (SwitchView)findViewById(R.id.blood_sugar_switchbutton);

    }
    private void BindListener(){
        save_button.setOnClickListener(this);
        blood_pressure_switchbutton.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn() {
                blood_pressure_switchbutton.setState(true);
                friendPermission.getPermission().setBloodPressure(true);
            }

            @Override
            public void toggleToOff() {
                blood_pressure_switchbutton.setState(false);
                friendPermission.getPermission().setBloodPressure(false);
            }
        });
        weight_switchbutton.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn() {
                weight_switchbutton.setState(true);
                friendPermission.getPermission().setWeight(true);
            }

            @Override
            public void toggleToOff() {
                weight_switchbutton.setState(false);
                friendPermission.getPermission().setWeight(false);
            }
        });
        temperature_switchbutton.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn() {
                temperature_switchbutton.setState(true);
                friendPermission.getPermission().setTemp(true);
            }

            @Override
            public void toggleToOff() {
                temperature_switchbutton.setState(false);
                friendPermission.getPermission().setTemp(false);
            }
        });
        heart_rate_switchbutton.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn() {
                heart_rate_switchbutton.setState(true);
                friendPermission.getPermission().setHeartRate(true);
            }

            @Override
            public void toggleToOff() {
                heart_rate_switchbutton.setState(false);
                friendPermission.getPermission().setHeartRate(false);
            }
        });
        blood_sugar_switchbutton.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn() {
                blood_sugar_switchbutton.setState(true);
                friendPermission.getPermission().setBloodGlucose(true);
            }

            @Override
            public void toggleToOff() {
                blood_sugar_switchbutton.setState(false);
                friendPermission.getPermission().setBloodGlucose(false);
            }
        });
    }
    private void setData(){
        title.setText(getString(R.string.detailed_data));
        String userid = getIntent().getStringExtra("friendId");
        friendPermission.setUserId(userid);
        friendPermission.setPermission(permission);
        CheckFriendPermission(userid);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.save_button:
                String json = new Gson().toJson(friendPermission);
                postFriendPermission(json);
                break;
        }
    }
    private void postFriendPermission(String json){
        new PostUtils().JsonPost(Constant.permission_friend,json,UpdatePermission_Handler,this);
    }
    private void CheckFriendPermission(String params){
        new PostUtils().Get(Constant.permission_friend+params,true,CheckPermission_Handler,this);
    }
}
