package zj.health.health_v1.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Base.Constant;
import zj.health.health_v1.Base.Health_AppLocation;
import zj.health.health_v1.Model.FriendModel;
import zj.health.health_v1.MyView.SwitchView;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.GlideCircleTransform;
import zj.health.health_v1.Utils.StringUtil;
import zj.health.health_v1.Utils.view.PostUtils;

/**
 * Created by Administrator on 2018/8/6.
 */

public class Friends_Activity extends BaseActivity implements View.OnClickListener{

    private FriendModel.FriendPermission friendPermission = new FriendModel.FriendPermission();
    private FriendModel.FriendPermission.Permission permission = new FriendModel.FriendPermission.Permission();
    private FriendModel friendModel = new FriendModel();
    private ImageView user_icon,back;
    private RequestOptions requestOptions ;
    private TextView user_name,title;
    private Button save_button;
    private SwitchView blood_pressure_switchbutton,weight_switchbutton,temperature_switchbutton,
            heart_rate_switchbutton,blood_sugar_switchbutton,medicineReminder_switchbutton,healthReport_switchbutton,particularReport_switchbutton;
    private Handler FriendRequestHandler = new Handler(){
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
                        Toast.makeText(Friends_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(Friends_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_activity);
        initView();
        BindListener();
        setData();
    }

    private void initView(){
        user_icon = (ImageView)findViewById(R.id.user_icon);
        back = (ImageView)findViewById(R.id.back);
        user_name = (TextView)findViewById(R.id.user_name);
        title = (TextView)findViewById(R.id.title);
        save_button = (Button)findViewById(R.id.save_button);
        blood_pressure_switchbutton = (SwitchView)findViewById(R.id.blood_pressure_switchbutton);
        weight_switchbutton = (SwitchView)findViewById(R.id.weight_switchbutton);
        temperature_switchbutton = (SwitchView)findViewById(R.id.temperature_switchbutton);
        heart_rate_switchbutton = (SwitchView)findViewById(R.id.heart_rate_switchbutton);
        blood_sugar_switchbutton = (SwitchView)findViewById(R.id.blood_sugar_switchbutton);
        medicineReminder_switchbutton = (SwitchView)findViewById(R.id.medicineReminder_switchbutton);
        healthReport_switchbutton = (SwitchView)findViewById(R.id.healthReport_switchbutton);
        particularReport_switchbutton = (SwitchView)findViewById(R.id.particularReport_switchbutton);

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

        medicineReminder_switchbutton.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn() {
                medicineReminder_switchbutton.setState(true);
                friendPermission.getPermission().setMedicineReminder(true);
            }

            @Override
            public void toggleToOff() {
                medicineReminder_switchbutton.setState(false);
                friendPermission.getPermission().setMedicineReminder(false);
            }
        });
        healthReport_switchbutton.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn() {
                healthReport_switchbutton.setState(true);
                friendPermission.getPermission().setHealthReport(true);
            }

            @Override
            public void toggleToOff() {
                healthReport_switchbutton.setState(false);
                friendPermission.getPermission().setHealthReport(false);
            }
        });
        particularReport_switchbutton.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn() {
                particularReport_switchbutton.setState(true);
                friendPermission.getPermission().setParticularReport(true);
            }

            @Override
            public void toggleToOff() {
                particularReport_switchbutton.setState(false);
                friendPermission.getPermission().setParticularReport(false);
            }
        });
    }
    private void setData(){
        title.setText(getString(R.string.detailed_data));
        friendModel = (FriendModel) getIntent().getBundleExtra("bundle").getSerializable("model");
        friendPermission.setUserId(friendModel.getId());
        friendPermission.setPermission(permission);
        user_name.setText(friendModel.getNickname());
        requestOptions = new RequestOptions()
                .placeholder(R.drawable.icon_head) //加载中图片
                .error(R.drawable.icon_head) //加载失败图片
                .fallback(R.drawable.icon_head) //url为空图片
                .centerCrop()
                .transform(new GlideCircleTransform(this))
                // 填充方式
//            .override(600,600) //尺寸
//            .transform(new CircleCrop()) //圆角
//            .priority(Priority.HIGH) //优先级
                .diskCacheStrategy(DiskCacheStrategy.NONE); //缓存策略
        Glide.with(this).
                load(Constant.photo_IP+ StringUtil.trimNull(friendModel.getIconUrl())).
                apply(requestOptions).
                into(user_icon);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.save_button:
                if(friendModel.getId().equals(Health_AppLocation.instance.users.getId())){
                    Toast.makeText(this, getString(R.string.add_friend_to_me_error), Toast.LENGTH_SHORT).show();
                    return;
                }
                String json = new Gson().toJson(friendPermission);
                postFriendRequest(json);
                break;
         default:
             break;
        }
    }
    private void postFriendRequest(String json){
        new PostUtils().JsonPost(Constant.add_friend,json,FriendRequestHandler,this);
    }
}
